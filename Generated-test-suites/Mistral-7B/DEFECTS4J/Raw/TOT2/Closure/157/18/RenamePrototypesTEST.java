import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenamePrototypes.*;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerResult;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.TokenStream;

import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;
import java.util.Collections;
import java.util.Map;

/**
 * Tests for the {@link RenamePrototypes} compiler pass.
 */
public class RenamePrototypesTest {

  private Compiler compiler;
  private CompilerPass renamePrototypes;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions(Collections.emptyMap());
    compiler = new Compiler(options);
    nodeFactory = compiler.getNodeFactory();

    String testCode = "function Foo() {} Foo.prototype.bar = function() {}; "
                      + "Foo.prototype.qux = 42; "
                      + "var foo = new Foo();";

    TokenStream input = new TokenStream(new StringReader(testCode));
    compiler.process(input, null, null);
    renamePrototypes = new RenamePrototypes(compiler, false, null, null);
  }

  /**
   * Test that properties defined on the prototype are renamed.
   */
  @Test
  public void testRenamePrototypeProperties() {
    renamePrototypes.process(null, compiler.getRootNode());

    Node bar = compiler.getRootNode().getFirstChild().getLastChild();
    assertEquals("function Foo$bar() {}", bar.toString());

    Node qux = compiler.getRootNode().getChildAt(2);
    assertEquals("var foo$qux = 42", qux.toString());
  }

  /**
   * Test that properties defined as object literals are not renamed.
   */
  @Test
  public void testDontRenameObjLitProperties() {
    String testCode = "function Foo() {} Foo.prototype = { bar : function() {}, qux : 42 };";

    Compiler compilerForObjLitTest = new Compiler(new CompilerOptions(Collections.emptyMap()));
    NodeFactory nodeFactoryForObjLitTest = compilerForObjLitTest.getNodeFactory();
    TokenStream inputForObjLitTest = new TokenStream(new StringReader(testCode));

    compilerForObjLitTest.process(inputForObjLitTest, null, null);
    RenamePrototypes objLitRenamePrototypes = new RenamePrototypes(compilerForObjLitTest, false, null, null);

    objLitRenamePrototypes.process(null, compilerForObjLitTest.getRootNode());

    Node bar = compilerForObjLitTest.getRootNode().getFirstChild().getLastChild();
    assertEquals("function Foo$bar() {}", bar.toString());

    Node qux = compilerForObjLitTest.getRootNode().getChildAt(3);
    assertEquals("var qux = 42", qux.toString());

    assertEquals("qux", qux.getFirstChild().getString());
  }

  /**
   * Test that properties defined on the prototype and as object literals have
   * the same name are not renamed.
   */
  @Test
  public void testDontRenameDuplicateProperties() {
    String testCode = "function Foo() {} Foo.prototype.bar = function() {}; "
                      + "Foo.prototype = { bar : function() {}, qux : 42 };";

    Compiler compilerForDuplicateTest = new Compiler(new CompilerOptions(Collections.emptyMap()));
    NodeFactory nodeFactoryForDuplicateTest = compilerForDuplicateTest.getNodeFactory();
    TokenStream inputForDuplicateTest = new TokenStream(new StringReader(testCode));

    compilerForDuplicateTest.process(inputForDuplicateTest, null, null);
    RenamePrototypes duplicateRenamePrototypes = new RenamePrototypes(compilerForDuplicateTest, false, null, null);

    duplicateRenamePrototypes.process(null, compilerForDuplicateTest.getRootNode());

    Node prototypeBar = compilerForDuplicateTest.getRootNode().getFirstChild().getLastChild();
    assertEquals("function Foo$bar() {}", prototypeBar.toString());

    Node objLitBar = compilerForDuplicateTest.getRootNode().getChildAt(3);
    assertEquals("function Foo$bar() {}", objLitBar.toString());
  }

  /**
   * Test that properties defined on the prototype and as object literals have
   * different names are renamed on the prototype.
   */
  @Test
  public void testRenameDuplicatePropertiesWithDifferentNames() {
    String testCode = "function Foo() {} Foo.prototype.bar = function() {}; "
                      + "Foo.prototype = { qux : 42 };";

    Compiler compilerForDuplicateTest = new Compiler(new CompilerOptions(Collections.emptyMap()));
    NodeFactory nodeFactoryForDuplicateTest = compilerForDuplicateTest.getNodeFactory();
    TokenStream inputForDuplicateTest = new TokenStream(new StringReader(testCode));

    compilerForDuplicateTest.process(inputForDuplicateTest, null, null);
    RenamePrototypes duplicateRenamePrototypes = new RenamePrototypes(compilerForDuplicateTest, false, null, null);

    duplicateRenamePrototypes.process(null, compilerForDuplicateTest.getRootNode());

    Node prototypeBar = compilerForDuplicateTest.getRootNode().getFirstChild().getLastChild();
    assertEquals("function Foo$bar() {}", prototypeBar.toString());

    Node objLitQux = compilerForDuplicateTest.getRootNode().getChildAt(3);
    assertEquals("var qux = 42", objLitQux.toString());

    assertEquals("bar", prototypeBar.getFirstChild().getString());
    assertNotEquals("bar", objLitQux.getFirstChild().getString());
  }

  /**
   * Test that properties defined on the prototype are not renamed when aggressive
   * renaming is enabled.
   */
  @Test
  public void testDontRenamePrototypePropertiesWithAggressiveRenaming() {
    renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    renamePrototypes.process(null, compiler.getRootNode());

    Node bar = compiler.getRootNode().getFirstChild().getLastChild();
    assertEquals("function Foo.bar() {}", bar.toString());

    Node qux = compiler.getRootNode().getChildAt(2);
    assertEquals("var foo.qux = 42", qux.toString());
  }

  /**
   * Test that methods defined on the prototype have their names changed when
   * the method name does not start or end with an underscore.
   */
  @Test
  public void testRenamePrototypeMethodsWithNonUnderscoreNames() {
    String testCode = "function Foo() {} Foo.prototype.fooBar = function() {};";

    Compiler compilerForNonUnderscoreTest = new Compiler(new CompilerOptions(Collections.emptyMap()));
    NodeFactory nodeFactoryForNonUnderscoreTest = compilerForNonUnderscoreTest.getNodeFactory();
    TokenStream inputForNonUnderscoreTest = new TokenStream(new StringReader(testCode));

    compilerForNonUnderscoreTest.process(inputForNonUnderscoreTest, null, null);
    RenamePrototypes nonUnderscoreRenamePrototypes = new RenamePrototypes(compilerForNonUnderscoreTest, false, null, null);

    nonUnderscoreRenamePrototypes.process(null, compilerForNonUnderscoreTest.getRootNode());

    Node fooBar = compilerForNonUnderscoreTest.getRootNode().getFirstChild().getLastChild();
    assertEquals("function Foo$fooBar() {}", fooBar.toString());
  }

  /**
   * Test that methods defined on the prototype are not renamed when the method
   * name starts or ends with an underscore.
   */
  @Test
  public void testDontRenamePrototypeMethodsWithUnderscoreNames() {
    String testCode = "function Foo() {} Foo.prototype._fooBar = function() {};";

    Compiler compilerForUnderscoreTest = new Compiler(new CompilerOptions(Collections.emptyMap()));
    NodeFactory nodeFactoryForUnderscoreTest = compilerForUnderscoreTest.getNodeFactory();
    TokenStream inputForUnderscoreTest = new TokenStream(new StringReader(testCode));

    compilerForUnderscoreTest.process(inputForUnderscoreTest, null, null);
    RenamePrototypes underscoreRenamePrototypes = new RenamePrototypes(compilerForUnderscoreTest, false, null, null);

    underscoreRenamePrototypes.process(null, compilerForUnderscoreTest.getRootNode());

    Node fooBar = compilerForUnderscoreTest.getRootNode().getFirstChild().getLastChild();
    assertEquals("function Foo._fooBar() {}", fooBar.toString());
  }

  /**
   * Test that a method with the same name as a reserved name is not renamed.
   */
  @Test
  public void testDontRenameMethodWithReservedName() {
    String testCode = "function Foo() {} Foo.prototype.indexOf = function() {};";

    Compiler compilerForReservedNameTest = new Compiler(new CompilerOptions(Collections.emptyMap()));
    NodeFactory nodeFactoryForReservedNameTest = compilerForReservedNameTest.getNodeFactory();
    TokenStream inputForReservedNameTest = new TokenStream(new StringReader(testCode));

    compilerForReservedNameTest.process(inputForReservedNameTest, null, null);
    RenamePrototypes reservedNameRenamePrototypes = new RenamePrototypes(compilerForReservedNameTest, false, null, null);

    reservedNameRenamePrototypes.process(null, compilerForReservedNameTest.getRootNode());

    Node indexOf = compilerForReservedNameTest.getRootNode().getFirstChild().getLastChild();
    assertEquals("function Foo.indexOf() {}", indexOf.toString());
  }

  /**
   * Test that the {@code VariableMap} generated by the
   * {@code RenamePrototypes} pass correctly maps the original names to the
   * renamed names.
   */
  @Test
  public void testGenerateVariableMap() {
    renamePrototypes.process(null, compiler.getRootNode());

    VariableMap variableMap = renamePrototypes.getPropertyMap();
    Map<String, String> expectedVariableMap = Maps.newHashMap();
    expectedVariableMap.put("qux", "foo$qux");

    assertEquals(expectedVariableMap, variableMap.asMap());
  }
}