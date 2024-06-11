import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.abstractTraverse;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractCompiler.LifeCycleStage;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TokenStream;
import org.junit.Test;

public class RenamePrototypesTest {

  @Test
  public void testRenamePrototypes() {
    // Given
    String inputCode = "function Foo() {} \n" +
                      "Foo.prototype.bar = function() {}; \n" +
                      "Foo.prototype.baz = function() {}; \n" +
                      "var foo = new Foo(); \n" +
                      "Foo.prototype = { getBar: function() {} }; \n" +
                      "foo.getBar(); \n";

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);

    CompilerPass renamePrototypes = new RenamePrototypes(compiler, true, null, null);

    // When
    Node root = new TokenStream(new Token(Token.START_FILE, "test.js", 0, 0)).parse();
    CompilerInput input = new CompilerInput("test.js", inputCode);
    compiler.setInputs(Arrays.asList(input));

    abstractTraverse(compiler, root, renamePrototypes);

    // Then
    NodeTraversal traversal = compiler.getInputProcessor().getTraversals().get(0);
    Node result = traversal.getOutput();

    // Basic functionality test
    assertNotNull(result);
    assertEquals(Token.END_FILE, result.getType());

    // Test for edge cases and exception handling

    // Test for renaming prototype properties with leading and trailing underscores
    String inputCodeWithUnderscores = "function Foo() {} \n" +
                                      "_Foo_prototype._bar = function() {}; \n" +
                                      "_Foo_prototype._baz = function() {}; \n" +
                                      "var foo = new Foo(); \n" +
                                      "_Foo_prototype = { _getBar: function() {} }; \n" +
                                      "foo._getBar(); \n";

    // Set up test environment
    compiler.reset();
    compiler.setInputs(Arrays.asList(new CompilerInput("test.js", inputCodeWithUnderscores)));

    // Perform renaming
    abstractTraverse(compiler, root, renamePrototypes);

    // Verify that the prototype properties with leading and trailing underscores are not renamed
    NodeTraversal traversalWithUnderscores = compiler.getInputProcessor().getTraversals().get(0);
    Node resultWithUnderscores = traversalWithUnderscores.getOutput();

    Node getPrototypeBar = resultWithUnderscores.getFirstChild().getLastChild();
    assertEquals("_getBar", getPrototypeBar.getString());

    Node getPrototypeBaz = resultWithUnderscores.getLastChild().getFirstChild().getLastChild();
    assertEquals("_getBaz", getPrototypeBaz.getString());

    // Test for renaming duplicate prototype properties
    String inputCodeWithDuplicates = "function Foo() {} \n" +
                                     "Foo.prototype.bar = function() {}; \n" +
                                     "Foo.prototype.bar = function() {}; \n" +
                                     "var foo = new Foo(); \n" +
                                     "Foo.prototype = { getBar: function() {} }; \n" +
                                     "foo.getBar(); \n";

    // Set up test environment
    compiler.reset();
    compiler.setInputs(Arrays.asList(new CompilerInput("test.js", inputCodeWithDuplicates)));

    // Perform renaming
    abstractTraverse(compiler, root, renamePrototypes);

    // Verify that only one of the duplicate prototype properties is renamed
    NodeTraversal traversalWithDuplicates = compiler.getInputProcessor().getTraversals().get(0);
    Node resultWithDuplicates = traversalWithDuplicates.getOutput();

    Node getPrototypeBar1 = resultWithDuplicates.getFirstChild().getLastChild();
    Node getPrototypeBar2 = resultWithDuplicates.getLastChild().getFirstChild().getLastChild();

    assertNotSame(getPrototypeBar1, getPrototypeBar2);
    assertFalse(getPrototypeBar1.equals(getPrototypeBar2));

    // Test for renaming prototype properties that are not renamed based on the coding convention
    String inputCodeWithConvention = "function Foo() {} \n" +
                                     "Foo.prototype._privateBar = function() {}; \n" +
                                     "Foo.prototype.publicBar = function() {}; \n" +
                                     "var foo = new Foo(); \n" +
                                     "Foo.prototype = { getPublicBar: function() {} }; \n" +
                                     "foo.getPublicBar(); \n";

    // Set up test environment
    compiler.reset();
    compiler.setInputs(Arrays.asList(new CompilerInput("test.js", inputCodeWithConvention)));

    // Perform renaming
    abstractTraverse(compiler, root, renamePrototypes);

    // Verify that the prototype property with the private identifier is not renamed
    NodeTraversal traversalWithConvention = compiler.getInputProcessor().getTraversals().get(0);
    Node resultWithConvention = traversalWithConvention.getOutput();

    Node getPrototypePrivateBar = resultWithConvention.getFirstChild().getLastChild();
    assertEquals("_privateBar", getPrototypePrivateBar.getString());

    Node getPrototypePublicBar = resultWithConvention.getLastChild().getFirstChild().getLastChild();
    assertNull(getPrototypePublicBar.getString());

    // Test for renaming obj lit properties with leading and trailing underscores
    String inputCodeWithObjLitUnderscores = "function Foo() {} \n" +
                                           "Foo.prototype = {}; \n" +
                                           "Foo.prototype._myObj = {}; \n" +
                                           "Foo.prototype._myObj._propWithUnderscores = 'value'; \n" +
                                           "var foo = new Foo(); \n";

    // Set up test environment
    compiler.reset();
    compiler.setInputs(Arrays.asList(new CompilerInput("test.js", inputCodeWithObjLitUnderscores)));

    // Perform renaming
    abstractTraverse(compiler, root, renamePrototypes);

    // Verify that the obj lit property with leading and trailing underscores is not renamed
    NodeTraversal traversalWithObjLitUnderscores = compiler.getInputProcessor().getTraversals().get(0);
    Node resultWithObjLitUnderscores = traversalWithObjLitUnderscores.getOutput();

    Node getPrototypeMyObj = resultWithObjLitUnderscores.getFirstChild().getLastChild();
    Node propWithUnderscores = getPrototypeMyObj.getFirstChild();

    assertEquals("_propWithUnderscores", propWithUnderscores.getString());

    // Test for renaming obj lit properties with same name as prototype properties
    String inputCodeWithObjLitPrototype = "function Foo() {} \n" +
                                          "Foo.prototype.myProp = function() {}; \n" +
                                          "Foo.prototype = {}; \n" +
                                          "Foo.prototype.myProp = 'value'; \n" +
                                          "var foo = new Foo(); \n";

    // Set up test environment
    compiler.reset();
    compiler.setInputs(Arrays.asList(new CompilerInput("test.js", inputCodeWithObjLitPrototype)));

    // Perform renaming
    abstractTraverse(compiler, root, renamePrototypes);

    // Verify that the obj lit property is renamed if its name does not conflict with the prototype property
    NodeTraversal traversalWithObjLitPrototype = compiler.getInputProcessor().getTraversals().get(0);
    Node resultWithObjLitPrototype = traversalWithObjLitPrototype.getOutput();

    Node getPrototypeMyProp = resultWithObjLitPrototype.getFirstChild().getLastChild();
    Node propWithSameName = getPrototypeMyProp.getFirstChild();

    assertFalse(getPrototypeMyProp.equals(propWithSameName));

    assertEquals("_myProp_0", getPrototypeMyProp.getString());
    assertEquals("_myProp_1", propWithSameName.getString());

    // Test for failing when the input code is malformed
    String inputCodeWithMalformedCode = "function Foo() {} \n" +
                                       "Foo.prototype = {} \n" +
                                       "var foo = new Foo(); \n" +
                                       "Foo.prototype = { getBar: function() {} }; \n" +
                                       "foo.getBar(); \n" +
                                       "Foo.prototype = 123; \n";

    // Set up test environment
    compiler.reset();
    compiler.setInputs(Arrays.asList(new CompilerInput("test.js", inputCodeWithMalformedCode)));

    try {
      abstractTraverse(compiler, root, renamePrototypes);
      fail("Expected an exception to be thrown.");
    } catch (RuntimeException e) {
      // Expected
      assertEquals("Unexpected end of input.", e.getMessage());
    }
  }

  @Test(expected = RuntimeException.class)
  public void testRenamePrototypesWithInvalidInput() {
    // Given
    String inputCodeWithInvalidInput = "function Foo() {} \n" +
                                      "Foo.prototype = {}; \n" +
                                      "var foo = new Foo(); \n";

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);

    CompilerPass renamePrototypes = new RenamePrototypes(compiler, true, null, null);

    // When and Then
    Node root = new TokenStream(new Token(Token.START_FILE, "test.js", 0, 0)).parse();
    CompilerInput input = new CompilerInput("test.js", inputCodeWithInvalidInput);
    compiler.setInputs(Arrays.asList(input));

    abstractTraverse(compiler, root, renamePrototypes);
  }
}