import static org.junit.Assert.*;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.TokenStream;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class RenamePrototypesTest {

  @Test
  public void testRenamePrototypeProperties() {
    // Given a simple JavaScript code with prototype properties
    String jsCode = "function Foo() {} Foo.prototype.bar = function() {};";

    // When processing the code with RenamePrototypes
    TokenStream input = new TokenStream(new TokenInputStream(jsCode));
    RenamePrototypes renamer = new RenamePrototypes(null, true, null, null);
    Node root = new Node(Token.BLOCK, null);
    Node externs = new Node(Token.PROGRAM, null);
    renamer.process(externs, root);

    // Then the prototype properties should be renamed
    Map<String, String> expectedRenameMap = new HashMap<>();
    expectedRenameMap.put("bar", "b_");
    VariableMap actualRenameMap = renamer.getPropertyMap();
    assertEquals(expectedRenameMap, actualRenameMap);

    // And the renamed properties should be in use in the program
    Node barFunction = root.mayContainNode("Foo.prototype." + expectedRenameMap.get("bar"));
    assertNotNull(barFunction);
  }

  @Test
  public void testDoNotRenameExternedProperty() {
    // Given a JavaScript code with an externed property
    String jsCode = "var indexOf = Array.prototype.indexOf;";

    // When processing the code with RenamePrototypes
    TokenStream input = new TokenStream(new TokenInputStream(jsCode));
    RenamePrototypes renamer = new RenamePrototypes(null, false, null, null);
    Node root = new Node(Token.BLOCK, null);
    Node externs = new Node(Token.PROGRAM, null);
    renamer.process(externs, root);

    // Then the externed property should not be renamed
    Map<String, String> expectedRenameMap = new HashMap<>();
    VariableMap actualRenameMap = renamer.getPropertyMap();
    assertEquals(expectedRenameMap, actualRenameMap);
    assertFalse(actualRenameMap.containsKey("indexOf"));
  }

  @Test
  public void testAggressiveRenaming() {
    // Given a JavaScript code with custom properties
    String jsCode = "function Foo() { this.foo = 1; } Foo.prototype.bar = function() {};";

    // When processing the code with RenamePrototypes in aggressive mode
    TokenStream input = new TokenStream(new TokenInputStream(jsCode));
    RenamePrototypes renamer = new RenamePrototypes(null, true, null, null);
    Node root = new Node(Token.BLOCK, null);
    Node externs = new Node(Token.PROGRAM, null);
    renamer.process(externs, root);

    // Then all properties should be renamed
    Map<String, String> expectedRenameMap = new HashMap<>();
    expectedRenameMap.put("foo", "f_");
    expectedRenameMap.put("bar", "b_");
    VariableMap actualRenameMap = renamer.getPropertyMap();
    assertEquals(expectedRenameMap, actualRenameMap);

    // And the renamed properties should be in use in the program
    Node fooProperty = root.mayContainNode("Foo.prototype." + expectedRenameMap.get("foo"));
    assertNotNull(fooProperty);
    Node barFunction = root.mayContainNode("Foo.prototype." + expectedRenameMap.get("bar"));
    assertNotNull(barFunction);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullCompiler() {
    // Given an instance of RenamePrototypes with a null compiler
    RenamePrototypes renamer = new RenamePrototypes(null, false, null, null);
    // Then an IllegalArgumentException should be thrown
    renamer.process(null, null);
  }

  @Test(expected = NullPointerException.class)
  public void testNullInput() {
    // Given an instance of RenamePrototypes with a null input
    RenamePrototypes renamer = new RenamePrototypes(null, false, null, null);
    // Then a NullPointerException should be thrown
    renamer.process(null, null);
  }
}