import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.CompilerOptionsCategory.*;
import static com.google.javascript.jscomp.CompilerOptions.CompilerOptionsType.*;

import com.google.common.collect.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.rhino.TokenStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.*;

@RunWith(JUnit4.class)
public class RenamePrototypesTest {

  @Test
  public void testRenamePrototypes() {
    

    // Given
    AbstractCompiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions("--warn_unused_require:off");
    options.setOption(KNOWN_NAMES, Arrays.asList("Print", "String", "Array"));

    // When
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    Node root = createTestCode();
    renamePrototypes.process(null, root);
    CompilerInput input = compiler.compile(options, root);

    // Then
    Node result = input.getRootNode();

    // Test for basic functionality
    testRenamePrototypeProperty(compiler, result, "Foo", "foo");
    testRenamePrototypeProperty(compiler, result, "Foo.prototype.bar", "fooBar");
    testRenamePrototypeProperty(compiler, result, "Foo.prototype.baz", "baz");
    testRenameObjLitProperty(compiler, result, "Foo.prototype = { bar : 1, baz : 2 }", "fooBar", "fooBaz");

    // Test for edge cases and exceptions
    testRenamePrivatePrototypeProperty(compiler, result, "Foo__prototype__bar", "fooBar");
    testRenameReservedPrototypeProperty(compiler, result, "indexOf", "indexOf_");
    testRenameStringLiteral(compiler, result, "\"Foo.prototype.bar\"", "\"fooBar\"");
    testRenamePrototypeWithSameName(compiler, result, "Foo__prototype__bar", "fooBar");
    testRenamePrototypeWithSameName(compiler, result, "Foo__prototype__baz", "baz");
    testRenamePrototypeWithAggressiveRenaming(compiler, result, "Foo__prototype__qux", "qux_");
    testRenamePrototypeWithAggressiveRenaming(compiler, result, "Foo__prototype__quux", "quux_");

    
  }

  private Node createTestCode() {
    TokenStream ts = new TokenStream(new StringBuilder("function Foo() { " +
        "this.bar = 1; " +
        "this.baz = 2; " +
        "} " +
        "Foo.prototype.bar = function() { return 3; }; " +
        "Foo.prototype.baz = function() { return 4; }; " +
        "Foo.prototype = { bar : 5, baz : 6 };"));
    return ts.parse();
  }

  private void testRenamePrototypeProperty(Compiler compiler, Node input, String oldName, String newName) {
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    renamePrototypes.process(null, input);

    Node result = input.getRootNode();
    assertEquals("Expected 'oldName' to be renamed to 'newName'", newName, getPropertyName(result, oldName));
  }

  private void testRenameObjLitProperty(Compiler compiler, Node input, String code, String oldName, String newName) {
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    Node root = createTestCodeWithObjLit(code);
    renamePrototypes.process(null, root);
    CompilerInput resultInput = compiler.compile(new CompilerOptions("--warn_unused_require:off"), root);

    Node result = resultInput.getRootNode();
    assertEquals("Expected 'oldName' to be renamed to 'newName' in object literal", newName, getPropertyName(result, oldName));
  }

  private Node createTestCodeWithObjLit(String code) {
    TokenStream ts = new TokenStream(new StringBuilder(code));
    return ts.parse();
  }

  private void testRenamePrivatePrototypeProperty(Compiler compiler, Node input, String oldName) {
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    renamePrototypes.process(null, input);

    Node result = input.getRootNode();
    Node prototype = getPrototype(result, "Foo");
    assertEquals("Expected 'oldName' to be renamed to '_oldName' when it is a private name", "_" + oldName, getPropertyName(prototype, oldName));
  }

  private void testRenameReservedPrototypeProperty(Compiler compiler, Node input, String oldName) {
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    renamePrototypes.process(null, input);

    Node result = input.getRootNode();
    Node prototype = getPrototype(result, "Foo");
    assertNull("Expected 'oldName' to not be renamed when it is a reserved name", getPropertyName(prototype, oldName));
  }

  private void testRenameStringLiteral(Compiler compiler, Node input, String oldStringLiteral) {
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    renamePrototypes.process(null, input);

    Node result = input.getRootNode();
    assertEquals("Expected 'oldStringLiteral' to be renamed to the same string literal", oldStringLiteral, getStringLiteral(result, oldStringLiteral));
  }

  private void testRenamePrototypeWithSameName(RenamePrototypes renamePrototypes, Node input, String oldName, String newName) {
    renamePrototypes.process(null, input);

    Node result = input.getRootNode();
    Property oldProperty = getProperty(result, oldName);
    Property newProperty = getProperty(result, newName);

    assertNotNull("Expected 'newName' to be present after renaming", newProperty);
    assertEquals("Expected 'oldName' and 'newName' to have the same count", oldProperty.count(), newProperty.count());
  }

  private void testRenamePrototypeWithAggressiveRenaming(Compiler compiler, Node input, String oldName) {
    RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    renamePrototypes.process(null, input);

    Node result = input.getRootNode();
    Node prototype = getPrototype(result, "Foo");
    String newName = "_" + oldName.substring(0, 1) + oldName.substring(1).replaceAll("[^a-z]", "_");

    assertEquals("Expected 'oldName' to be renamed aggressively", newName, getPropertyName(prototype, oldName));
  }

  private Node getPrototype(Node result, String name) {
    for (Iterator<Node> it = result.getChildren().iterator(); it.hasNext(); ) {
      Node child = it.next();
      if (child.getType() == Token.GETPROP && child.getString().equals(name + " prototype")) {
        return child.getFirstChild();
      }
    }

    throw new AssertionError("Expected a prototype node for '" + name + "'");
  }

  private String getPropertyName(Node node, String name) {
    for (Iterator<Node> it = node.getChildren().iterator(); it.hasNext(); ) {
      Node child = it.next();
      if (child.getType() == Token.GETPROP && child.getString().equals(name)) {
        return child.getFirstChild().getString();
      }
    }

    throw new AssertionError("Expected a property node for '" + name + "'");
  }

  private String getStringLiteral(Node node, String stringLiteral) {
    for (Iterator<Node> it = node.getChildren().iterator(); it.hasNext(); ) {
      Node child = it.next();
      if (child.getType() == Token.STRING && child.getString().equals(stringLiteral)) {
        return child.getString();
      }
    }

    throw new AssertionError("Expected a string literal node for '" + stringLiteral + "'");
  }

  private Property getProperty(Node result, String name) {
    Iterator<Map.Entry<String, Property>> it = RenamePrototypes.this.properties.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<String, Property> entry = it.next();
      if (entry.getKey().equals(name)) {
        return entry.getValue();
      }
    }

    throw new AssertionError("Expected a property node for '" + name + "'");
  }
}