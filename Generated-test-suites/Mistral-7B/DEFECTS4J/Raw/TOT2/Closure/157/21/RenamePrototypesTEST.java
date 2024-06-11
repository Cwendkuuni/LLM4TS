import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeTree.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerResults;
import com.google.javascript.jscomp.CompilerTestBase;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tests for the RenamePrototypes compiler pass.
 */
public class RenamePrototypesTest extends CompilerTestBase {

  public void testBasicRename() {
    String source = "function Foo() {} Foo.prototype.bar = function() {};";
    CompilerOptions options = new CompilerOptions();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);

    CompilerResults results = compile(options, source);
    assertEquals(0, results.getDiagnostics().length());

    Node ast = results.getSourceRootNode();
    Node prototype = getTopLevelNode(ast, "Foo");
    Node prototypeBar = getTopLevelNode(prototype, "bar");

    // The prototype's bar method should have been renamed
    String expectedRenamedName = "_bar";
    assertNotNull(prototypeBar);
    assertEquals(TokenType.FUNCTION, prototypeBar.getType());
    assertEquals(expectedRenamedName, prototypeBar.getFirstChild().getString());
  }

  public void testNonRenamedExtern() {
    String source = "String.prototype.indexOf = function() {};";
    CompilerOptions options = new CompilerOptions();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);

    CompilerResults results = compile(options, source);
    assertEquals(0, results.getDiagnostics().length());

    Node ast = results.getSourceRootNode();
    Node prototypeIndexOf = getTopLevelNode(ast, "String", "prototype", "indexOf");

    // The indexOf method should not have been renamed
    assertNotNull(prototypeIndexOf);
    assertEquals("indexOf", prototypeIndexOf.getFirstChild().getString());
  }

  public void testAggressiveRename() {
    String source = "function Foo() {} Foo.prototype.qux = function() {};";
    CompilerOptions options = new CompilerOptions().isStrict(true);
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);

    CompilerResults results = compile(options, source);
    assertEquals(0, results.getDiagnostics().length());

    Node ast = results.getSourceRootNode();
    Node prototype = getTopLevelNode(ast, "Foo");
    Node prototypeQux = getTopLevelNode(prototype, "qux");

    // The qux method should have been renamed
    String expectedRenamedName = "_qux";
    assertNotNull(prototypeQux);
    assertEquals(TokenType.FUNCTION, prototypeQux.getType());
    assertEquals(expectedRenamedName, prototypeQux.getFirstChild().getString());
  }

  public void testNonRenamedPrivate() {
    String source = "function Foo() {} Foo.prototype._bar = function() {};";
    CompilerOptions options = new CompilerOptions();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);

    CompilerResults results = compile(options, source);
    assertEquals(0, results.getDiagnostics().length());

    Node ast = results.getSourceRootNode();
    Node prototype = getTopLevelNode(ast, "Foo");
    Node prototypeBar = getTopLevelNode(prototype, "_bar");

    // The _bar method should not have been renamed
    assertNotNull(prototypeBar);
    assertEquals("_bar", prototypeBar.getFirstChild().getString());
  }

  public void testNonRenamedObjLit() {
    String source = "var obj = { qux: function() {} };";
    CompilerOptions options = new CompilerOptions();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);

    CompilerResults results = compile(options, source);
    assertEquals(0, results.getDiagnostics().length());

    Node ast = results.getSourceRootNode();
    Node obj = getTopLevelNode(ast, "obj");
    Node objQux = getTopLevelNode(obj, "qux");

    // The qux method should not have been renamed
    assertNotNull(objQux);
    assertEquals("qux", objQux.getFirstChild().getString());
  }

  public void testEdgeCase() {
    String source = "function Foo() {} Foo.prototype.qux = function() {}; function Foo2() {} Foo2.prototype.qux = function() {};";
    CompilerOptions options = new CompilerOptions();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);

    CompilerResults results = compile(options, source);
    assertEquals(0, results.getDiagnostics().length());

    Node ast = results.getSourceRootNode();
    Node fooPrototype = getTopLevelNode(ast, "Foo", "prototype");
    Node fooPrototypeQux = getTopLevelNode(fooPrototype, "qux");
    Node foo2Prototype = getTopLevelNode(ast, "Foo2", "prototype");
    Node foo2PrototypeQux = getTopLevelNode(foo2Prototype, "qux");

    // Both qux methods should have been renamed
    String expectedRenamedName = "_qux";
    assertNotNull(fooPrototypeQux);
    assertEquals(TokenType.FUNCTION, fooPrototypeQux.getType());
    assertEquals(expectedRenamedName, fooPrototypeQux.getFirstChild().getString());
    assertNotNull(foo2PrototypeQux);
    assertEquals(TokenType.FUNCTION, foo2PrototypeQux.getType());
    assertEquals(expectedRenamedName, foo2PrototypeQux.getFirstChild().getString());
  }

  public void testExceptionHandling() {
    String source = "function Foo() { try { throw new Error(" +
        "\"Renaming prototype properties is not allowed.\"); } catch(e) {} } " +
        "Foo.prototype.bar = function() {};";
    CompilerOptions options = new CompilerOptions();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);

    CompilerResults results = compile(options, source);
    assertEquals(1, results.getDiagnostics().length());
    assertEquals("Renaming prototype properties is not allowed.",
        results.getDiagnostics()[0].getMessage());

    Node ast = results.getSourceRootNode();
    Node prototypeBar = getTopLevelNode(ast, "Foo", "prototype", "bar");

    // The bar method should not have been renamed
    assertNotNull(prototypeBar);
    assertEquals("bar", prototypeBar.getFirstChild().getString());
  }

  /**
   * Helper method to get the top-level node for a given name in the given Node.
   * @param root The Node to start traversing from
   * @param names The sequence of names to traverse through
   * @return The Node that matches the given name, or null if it wasn't found
   */
  private Node getTopLevelNode(Node root, String... names) {
    Node current = root;
    for (String name : names) {
      Node child = null;
      switch (current.getType()) {
        case Token.OBJECTLIT:
          for (Node childNode = current.getFirstChild();
               childNode != null; childNode = childNode.getNext()) {
            if (childNode.getType() == Token.STRING) {
              if (childNode.getString().equals(name)) {
                child = childNode.getNext();
                break;
              }
            } else if (childNode.getType() == Token.GETPROP) {
              if (childNode.getString().equals(name)) {
                child = childNode.getFirstChild().getNext();
                break;
              }
            }
          }
          if (child == null) {
            return null;
          }
          current = child;
          break;
        case Token.GETPROP:
          if (current.getString().equals(names[0])) {
            current = current.getFirstChild().getNext();
          } else {
            return null;
          }
          break;
        default:
          return null;
      }
    }
    return current;
  }

  @Override
  protected CompilerPass getPass() {
    return new RenamePrototypes(compiler, true, null, null);
  }
}