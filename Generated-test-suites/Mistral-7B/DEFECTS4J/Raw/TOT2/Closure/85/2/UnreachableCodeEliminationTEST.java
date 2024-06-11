import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.EnterScopeAction.ENTER;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.EnterScopeAction.EXIT;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.UnreachableCodeElimination;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {
  private Node rootNode;
  private Compiler compiler;

  @Before
  public void setUp() {
    compiler = new Compiler();
    String sourceCode = "function foo() {\n" +
        "  var x = 1;\n" +
        "  if (true) {\n" +
        "    return;\n" +
        "  }\n" +
        "  alert('unreachable');\n" +
        "}";
    rootNode = compiler.parse(sourceCode, null, null);
  }

  @Test
  public void testBasicFunctionality() {
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, rootNode);

    NodeTraversal traversal = NodeTraversal.traverse(compiler, rootNode, new TestCallback());
    NodeUtil.assertNodeEquals(
        "function foo() {\n" +
        "  var x = 1;\n" +
        "  if (true) {\n" +
        "  } // empty block\n" +
        "}",
        traversal.getRootNode());
  }

  @Test
  public void testEdgeCases() {
    // Test function with empty body
    rootNode = NodeUtil.createFunctionNode("emptyFunction", new Node[0], new Node[0]);
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, rootNode);

    NodeTraversal traversal = NodeTraversal.traverse(compiler, rootNode, new TestCallback());
    NodeUtil.assertNodeEquals(
        "function emptyFunction() {}",
        traversal.getRootNode());

    // Test function with only 'return' statement
    rootNode = NodeUtil.createFunctionNode("returnFunction", new Node[0], new Node[0]);
    rootNode.setType(Token.RETURN);
    eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, rootNode);

    traversal = NodeTraversal.traverse(compiler, rootNode, new TestCallback());
    NodeUtil.assertNodeEquals(
        "function returnFunction() { return; }",
        traversal.getRootNode());

    // Test function with empty block after return statement
    rootNode = NodeUtil.createFunctionNode("returnWithEmptyBlockFunction", new Node[0], new Node[0]);
    rootNode.setType(Token.RETURN);
    rootNode.addChildToEnd(NodeUtil.createBlockNode(new Node[0]));
    eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, rootNode);

    traversal = NodeTraversal.traverse(compiler, rootNode, new TestCallback());
    NodeUtil.assertNodeEquals(
        "function returnWithEmptyBlockFunction() { return; }",
        traversal.getRootNode());

    // Test function with 'continue' statement
    rootNode = NodeUtil.createFunctionNode("continueFunction", new Node[0], new Node[0]);
    rootNode.addChildToEnd(NodeUtil.createForNode(NodeUtil.createIdentifierNode("i"), NodeUtil.createNumberNode(0), null, null, null, null));
    rootNode.addChildToEnd(NodeUtil.createStatementNode(NodeUtil.createContinueStatementNode()));
    eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, rootNode);

    traversal = NodeTraversal.traverse(compiler, rootNode, new TestCallback());
    NodeUtil.assertNodeEquals(
        "function continueFunction(i) { for (i = 0; i < 1; i++) { } }",
        traversal.getRootNode());

    // Test function with 'break' statement
    rootNode = NodeUtil.createFunctionNode("breakFunction", new Node[0], new Node[0]);
    rootNode.addChildToEnd(NodeUtil.createForNode(NodeUtil.createIdentifierNode("i"), NodeUtil.createNumberNode(0), null, null, null, null));
    rootNode.addChildToEnd(NodeUtil.createStatementNode(NodeUtil.createBreakStatementNode()));
    eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, rootNode);

    traversal = NodeTraversal.traverse(compiler, rootNode, new TestCallback());
    NodeUtil.assertNodeEquals(
        "function breakFunction(i) { for (i = 0; i < 1; i++) { break; } }",
        traversal.getRootNode());
  }

  @Test
  public void testExceptionHandling() {
    rootNode = NodeUtil.createFunctionNode("tryCatchFunction", new Node[0], new Node[0]);
    rootNode.setType(Token.TRY);
    rootNode.addChildToEnd(NodeUtil.createStatementNode(NodeUtil.createThrowStatementNode()));
    rootNode.addChildToEnd(NodeUtil.createStatementNode(NodeUtil.createCatchNode(NodeUtil.createIdentifierNode("e"), null, null, null)));
    eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, rootNode);

    traversal = NodeTraversal.traverse(compiler, rootNode, new TestCallback());
    NodeUtil.assertNodeEquals(
        "function tryCatchFunction() {\n" +
        "  try {\n" +
        "    throw new Error();\n" +
        "  } catch (e) {}\n" +
        "}",
        traversal.getRootNode());
  }

  @Test(expected = AssertionError.class)
  public void testUnreachableCodeEliminationFailsWithException() {
    rootNode = NodeUtil.createFunctionNode("throwFunction", new Node[0], new Node[0]);
    rootNode.setType(Token.RETURN);
    rootNode.addChildToEnd(NodeUtil.createStatementNode(NodeUtil.createThrowStatementNode()));
    eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, rootNode);

    NodeTraversal traversal = NodeTraversal.traverse(compiler, rootNode, new TestCallback());
  }

  private static class TestCallback extends AbstractPostOrderCallback {
    @Override
    public EnterScopeAction onEnterScope(NodeTraversal t, Node n, Node parent) {
      return ENTER;
    }

    @Override
    public void onExitScope(NodeTraversal t, Node n, Node parent) {
      EXIT;
    }
  }
}