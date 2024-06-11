import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;

public class FunctionRewriterTest {

  @Test
  public void testEmptyFunctionReduction() {
    FunctionRewriter rewriter = new FunctionRewriter(null);

    Node root = NodeFactory.createProgram();
    Node emptyFunction = NodeFactory.createFunction(
        NodeFactory.createName("emptyFunction"),
        NodeFactory.createList(NodeFactory.createName("a")),
        NodeFactory.createBlock(NodeFactory.createList()));

    root.addChildToBack(emptyFunction);

    Node newRoot = NodeFactory.createProgram();

    rewriter.process(null, root);
    NodeTraversal.traverse(new NodeTraversal.SimpleCallback() {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node node, Node parent) {
        if (node.isFunction()) {
          if (node.getString().equals("emptyFunction")) {
            Node expected = NodeFactory.createCall(
                NodeFactory.createName("JSCompiler_emptyFn"));
            assertEquals(expected, node);
            return false;
          }
        }
        return true;
      }

      @Override
      public void visit(NodeTraversal traversal, Node node, Node parent) {
      }
    }, newRoot);

    assertNotNull(newRoot);
  }

  @Test
  public void testIdentityFunctionReduction() {
    FunctionRewriter rewriter = new FunctionRewriter(null);

    Node root = NodeFactory.createProgram();
    Node identityFunction = NodeFactory.createFunction(
        NodeFactory.createName("identityFunction"),
        NodeFactory.createList(NodeFactory.createName("a")),
        NodeFactory.createBlock(
            NodeFactory.createReturnStatement(NodeFactory.createName("a"))));

    root.addChildToBack(identityFunction);

    Node newRoot = NodeFactory.createProgram();

    rewriter.process(null, root);
    NodeTraversal.traverse(new NodeTraversal.SimpleCallback() {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node node, Node parent) {
        if (node.isFunction()) {
          if (node.getString().equals("identityFunction")) {
            Node expected = NodeFactory.createCall(
                NodeFactory.createName("JSCompiler_identityFn"),
                NodeFactory.createName("a_"));
            assertEquals(expected, node);
            return false;
          }
        }
        return true;
      }

      @Override
      public void visit(NodeTraversal traversal, Node node, Node parent) {
      }
    }, newRoot);

    assertNotNull(newRoot);
  }

  @Test
  public void testReturnConstantReduction() {
    FunctionRewriter rewriter = new FunctionRewriter(null);

    Node root = NodeFactory.createProgram();
    Node returnFunction = NodeFactory.createFunction(
        NodeFactory.createName("returnFunction"),
        NodeFactory.createList(),
        NodeFactory.createBlock(
            NodeFactory.createExpressionStatement(
                NodeFactory.createReturnStatement(
                    NodeFactory.createNumberLiteral(42)))));

    root.addChildToBack(returnFunction);

    Node newRoot = NodeFactory.createProgram();

    rewriter.process(null, root);
    NodeTraversal.traverse(new NodeTraversal.SimpleCallback() {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node node, Node parent) {
        if (node.isFunction()) {
          if (node.getString().equals("returnFunction")) {
            Node expected = NodeFactory.createCall(
                NodeFactory.createName("JSCompiler_returnArg"),
                NodeFactory.createNumberLiteral(42));
            assertEquals(expected, node);
            return false;
          }
        }
        return true;
      }

      @Override
      public void visit(NodeTraversal traversal, Node node, Node parent) {
      }
    }, newRoot);

    assertNotNull(newRoot);
  }

  @Test
  public void testGetterReduction() {
    FunctionRewriter rewriter = new FunctionRewriter(null);

    Node root = NodeFactory.createProgram();
    Node getterFunction = NodeFactory.createFunction(
        NodeFactory.createName("getterFunction"),
        NodeFactory.createList(),
        NodeFactory.createBlock(
            NodeFactory.createVariableDeclarationStatement(
                NodeFactory.createName("a"), NodeFactory.createName("a"),
                null, NodeFactory.createNumberLiteral(42),
                NodeFactory.createEmptyStatement()),
            NodeFactory.createReturnStatement(
                NodeFactory.createGetPropStatement(
                    NodeFactory.createName("this"),
                    NodeFactory.createName("a")))));

    root.addChildToBack(getterFunction);

    Node newRoot = NodeFactory.createProgram();

    rewriter.process(null, root);
    NodeTraversal.traverse(new NodeTraversal.SimpleCallback() {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node node, Node parent) {
        if (node.isFunction()) {
          if (node.getString().equals("getterFunction")) {
            Node expected = NodeFactory.createCall(
                NodeFactory.createName("JSCompiler_get"),
                NodeFactory.createName("a_"));
            assertEquals(expected, node);
            return false;
          }
        }
        return true;
      }

      @Override
      public void visit(NodeTraversal traversal, Node node, Node parent) {
      }
    }, newRoot);

    assertNotNull(newRoot);
  }

  @Test
  public void testSetterReduction() {
    FunctionRewriter rewriter = new FunctionRewriter(null);

    Node root = NodeFactory.createProgram();
    Node setterFunction = NodeFactory.createFunction(
        NodeFactory.createName("setterFunction"),
        NodeFactory.createList(NodeFactory.createName("a"), NodeFactory.createName("b")),
        NodeFactory.createBlock(
            NodeFactory.createVariableDeclarationStatement(
                NodeFactory.createName("a"), NodeFactory.createName("a"),
                null, NodeFactory.createName("a"),
                NodeFactory.createEmptyStatement()),
            NodeFactory.createExpressionStatement(
                NodeFactory.createAssign(
                    NodeFactory.createGetPropStatement(
                        NodeFactory.createName("this"),
                        NodeFactory.createName("a")),
                    NodeFactory.createName("b")))));

    root.addChildToBack(setterFunction);

    Node newRoot = NodeFactory.createProgram();

    rewriter.process(null, root);
    NodeTraversal.traverse(new NodeTraversal.SimpleCallback() {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node node, Node parent) {
        if (node.isFunction()) {
          if (node.getString().equals("setterFunction")) {
            Node expected = NodeFactory.createCall(
                NodeFactory.createName("JSCompiler_set"),
                NodeFactory.createName("a_"),
                NodeFactory.createName("b"));
            assertEquals(expected, node);
            return false;
          }
        }
        return true;
      }

      @Override
      public void visit(NodeTraversal traversal, Node node, Node parent) {
      }
    }, newRoot);

    assertNotNull(newRoot);
  }

  @Test(expected = AssertionError.class)
  public void testNonFunctionNodeReduction() {
    FunctionRewriter rewriter = new FunctionRewriter(null);

    Node root = NodeFactory.createProgram();
    Node nonFunctionNode = NodeFactory.createNumberLiteral(42);

    root.addChildToBack(nonFunctionNode);

    Node newRoot = NodeFactory.createProgram();

    rewriter.process(null, root);

    NodeTraversal.traverse(new NodeTraversal.SimpleCallback() {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node node, Node parent) {
        if (node.isFunction()) {
          fail("Expected no functions");
        }
        return true;
      }

      @Override
      public void visit(NodeTraversal traversal, Node node, Node parent) {
      }
    }, newRoot);

    fail("Expected an AssertionError");
  }

  @Test(expected = IllegalStateException.class)
  public void testNonFunctionExpressionNodeReduction() {
    FunctionRewriter rewriter = new FunctionRewriter(null);

    Node root = NodeFactory.createProgram();
    Node nonFunctionExpressionNode = NodeFactory.createConditionalExpression(
        NodeFactory.createFalse(), NodeFactory.createNumberLiteral(1),
        NodeFactory.createNumberLiteral(2));

    root.addChildToBack(nonFunctionExpressionNode);

    Node newRoot = NodeFactory.createProgram();

    rewriter.process(null, root);

    NodeTraversal.traverse(new NodeTraversal.SimpleCallback() {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node node, Node parent) {
        if (node.isFunction()) {
          fail("Expected no functions");
        }
        return true;
      }

      @Override
      public void visit(NodeTraversal traversal, Node node, Node parent) {
      }
    }, newRoot);

    fail("Expected an IllegalStateException");
  }
}