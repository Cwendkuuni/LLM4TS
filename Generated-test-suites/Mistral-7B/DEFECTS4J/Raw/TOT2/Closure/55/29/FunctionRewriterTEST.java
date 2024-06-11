import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import org.junit.Test;

public class FunctionRewriterTest {

  @Test
  public void testEmptyFunctionReducer() {
    Compiler compiler = new Compiler();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    String testSource = "function Test() {}";
    Node root = compiler.parse(testSource, null, null);
    functionRewriter.process(null, root);

    Node testFunction = findNode("Test", root);
    assertNotNull(testFunction);
    assertEquals(NodeType.CALL, testFunction.getType());
    assertEquals("JSCompiler_emptyFn", testFunction.getFirstChild().getString());
  }

  @Test
  public void testIdentityReducer() {
    Compiler compiler = new Compiler();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    String testSource = "function Test(value) { return value }";
    Node root = compiler.parse(testSource, null, null);
    functionRewriter.process(null, root);

    Node testFunction = findNode("Test", root);
    assertNotNull(testFunction);
    assertEquals(NodeType.CALL, testFunction.getType());
    assertEquals("JSCompiler_identityFn", testFunction.getFirstChild().getString());
  }

  @Test
  public void testGetPropertyReducer() {
    Compiler compiler = new Compiler();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    String testSource = "function Test() { return this.a }";
    Node root = compiler.parse(testSource, null, null);
    functionRewriter.process(null, root);

    Node testFunction = findNode("Test", root);
    assertNotNull(testFunction);
    assertEquals(NodeType.CALL, testFunction.getType());
    assertEquals("JSCompiler_get", testFunction.getFirstChild().getString());
    assertEquals("a", testFunction.getFirstChild().getLastChild().getString());
  }

  @Test
  public void testSetPropertyReducer() {
    Compiler compiler = new Compiler();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    String testSource = "function Test(value) { this.a = value }";
    Node root = compiler.parse(testSource, null, null);
    functionRewriter.process(null, root);

    Node testFunction = findNode("Test", root);
    assertNotNull(testFunction);
    assertEquals(NodeType.CALL, testFunction.getType());
    assertEquals("JSCompiler_set", testFunction.getFirstChild().getString());
    assertEquals("a", testFunction.getFirstChild().getLastChild().getString());
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidReducer() {
    Compiler compiler = new Compiler();
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

    String testSource = "function Test() { return 1 }";
    Node root = compiler.parse(testSource, null, null);
    functionRewriter.process(null, root);

    Node testFunction = findNode("Test", root);
    assertNotNull(testFunction);
    // This statement should not be reached as the reducer for constants is not able to handle this case
  }

  private Node findNode(String name, Node root) {
    NodeTraversal traversal = new NodeTraversal(root, new FunctionNameFinder(name));
    traversal.traverse();
    return traversal.foundNode;
  }

  private static class FunctionNameFinder implements Callback {
    private final String name;

    FunctionNameFinder(String name) { this.name = name; }

    @Override
    public boolean shouldTraverse(NodeTraversal traverser, Node node, Node parent) {
      if (NodeUtil.isFunctionDeclaration(node) && name.equals(node.getFirstChild().getString())) {
        traversal.foundNode = node;
        return false;
      }
      return true;
    }

    @Override
    public void visit(NodeTraversal traversal, Node node, Node parent) {
    }
  }
}