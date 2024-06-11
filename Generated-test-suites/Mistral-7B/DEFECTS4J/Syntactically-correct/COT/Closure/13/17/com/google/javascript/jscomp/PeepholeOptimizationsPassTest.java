package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.test.TestCompiler;
import com.google.javascript.rhino.NodeOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * Test cases for the PeepholeOptimizationsPass class.
 */
public class PeepholeOptimizationsPassTest {

  @SuppressWarnings("unchecked")
  private static Compiler<Node> createCompiler() {
    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    options.setClassLoader(PeepholeOptimizationsPassTest.class.getClassLoader());
    return new TestCompiler(options);
  }

  /**
   * Test the constructor.
   */
  @org.junit.Test
  public void testConstructor() {
    Compiler<Node> compiler = createCompiler();
    AbstractPeepholeOptimization optimization1 = new NoopPeepholeOptimization();
    AbstractPeepholeOptimization optimization2 = new ConstantFoldingPeepholeOptimization();

    PeepholeOptimizationsPass optimizationsPass = new PeepholeOptimizationsPass(compiler, optimization1, optimization2);
    assertNotNull(optimizationsPass);
  }

  /**
   * Test that process method is working properly with a simple AST.
   */
  @org.junit.Test
  public void testProcess() {
    Compiler<Node> compiler = createCompiler();
    AbstractPeepholeOptimization optimization1 = new NoopPeepholeOptimization();
    AbstractPeepholeOptimization optimization2 = new ConstantFoldingPeepholeOptimization();

    PeepholeOptimizationsPass optimizationsPass = new PeepholeOptimizationsPass(compiler, optimization1, optimization2);

    Node root = NodeOperations.parse("function add(x, y) {\n" +
        "  return x + y;\n" +
        "}");

    optimizationsPass.process(null, root);

    Node result = root;
    List<Node> expectedNodes = ImmutableList.of(
        NodeOperations.parse("function add(x, y) {\n" +
            "  return x + y;\n" +
            "}"),
        NodeOperations.parse("function add(x, y) {\n" +
            "  return x + y;\n" +
            "}"));

    NodeOperations.traverse(result, new NodeVisitor() {
      private int nodeCount = 0;

      @Override
      public void visit(Node node) {
        nodeCount++;
      }

      @Override
      public boolean visitEnd(Node node) {
        assertTrue("Unexpected number of nodes encountered during traversal: " + nodeCount,
            nodeCount == expectedNodes.size());
        for (int i = 0; i < expectedNodes.size(); i++) {
          Node expectedNode = expectedNodes.get(i);
          Node actualNode = node;
          while (actualNode != null && !actualNode.equals(expectedNode)) {
            actualNode = actualNode.getParent();
          }
          assertTrue("Unexpected node encountered during traversal: " + node,
              actualNode != null);
        }
        return true;
      }
    });
  }

  /**
   * Test that shouldVisit method returns false for a non-function-like node.
   */
  @org.junit.Test
  public void testShouldVisitForNonFunctionLikeNode() {
    Compiler<Node> compiler = createCompiler();
    AbstractPeepholeOptimization optimization = new NoopPeepholeOptimization();
    PeepholeOptimizationsPass optimizationsPass = new PeepholeOptimizationsPass(compiler, optimization);

    Node root = NodeOperations.parse("1 + 2");
    assertFalse("shouldVisit should return false for non-function-like node",
        optimizationsPass.shouldVisit(root));
  }

  /**
   * Test that shouldVisit method returns true for a function-like node.
   */
  @org.junit.Test
  public void testShouldVisitForFunctionLikeNode() {
    Compiler<Node> compiler = createCompiler();
    AbstractPeepholeOptimization optimization = new NoopPeepholeOptimization();
    PeepholeOptimizationsPass optimizationsPass = new PeepholeOptimizationsPass(compiler, optimization);

    Node root = NodeOperations.parse("function add(x, y) {\n" +
        "  return x + y;\n" +
        "}");
    assertTrue("shouldVisit should return true for function-like node",
        optimizationsPass.shouldVisit(root));
  }

  /**
   * Test that shouldRetraverse method returns true for a node with a changed parent.
   */
  @org.junit.Test
  public void testShouldRetraverseForChangedParent() {
    Compiler<Node> compiler = createCompiler();
    AbstractPeepholeOptimization optimization = new NoopPeepholeOptimization();
    PeepholeOptimizationsPass optimizationsPass = new PeepholeOptimizationsPass(compiler, optimization);

    Node root = NodeOperations.parse("function add(x, y) {\n" +
        "  return x + y;\n" +
        "}");
    Node functionNode = root.getFirstChild();
    Node childNode = functionNode.getFirstChild();

    optimizationsPass.traverse(root);
    optimizationsPass.push();
    childNode.setName("newChildName");

    assertTrue("shouldRetraverse should return true for a node with a changed parent",
        optimizationsPass.shouldRetraverse(childNode));
  }

  /**
   * Test that shouldRetraverse method returns false for a node with an unchanged parent.
   */
  @org.junit.Test
  public void testShouldRetraverseForUnchangedParent() {
    Compiler<Node> compiler = createCompiler();
    AbstractPeepholeOptimization optimization = new NoopPeepholeOptimization();
    PeepholeOptimizationsPass optimizationsPass = new PeepholeOptimizationsPass(compiler, optimization);

    Node root = NodeOperations.parse("function add(x, y) {\n" +
        "  return x + y;\n" +
        "}");
    Node functionNode = root.getFirstChild();
    Node childNode = functionNode.getFirstChild();

    optimizationsPass.traverse(root);
    optimizationsPass.push();
    childNode.setName("newChildName");
    optimizationsPass.pop();

    assertFalse("shouldRetraverse should return false for a node with an unchanged parent",
        optimizationsPass.shouldRetraverse(childNode));
  }

  /**
   * Test that visit method does not modify the original node.
   */
  @org.junit.Test
  public void testVisitDoesNotModifyOriginalNode() {
    Compiler<Node> compiler = createCompiler();
    AbstractPeepholeOptimization optimization = new NoopPeepholeOptimization();
    PeepholeOptimizationsPass optimizationsPass = new PeepholeOptimizationsPass(compiler, optimization);

    Node root = NodeOperations.parse("function add(x, y) {\n" +
        "  return x + y;\n" +
        "}");

    optimizationsPass.process(null, root);

    Node originalNode = root;
    optimizationsPass.visit(root);

    assertNull("visit should not modify the original node",
        NodeOperations.findChild(originalNode, "visitReturnNode"));
  }

  /**
   * Test that visit method handles multiple optimizations.
   */
  @org.junit.Test
  public void testVisitHandlesMultipleOptimizations() {
    Compiler<Node> compiler = createCompiler();
    AbstractPeepholeOptimization optimization1 = new ConstantFoldingPeepholeOptimization();
    AbstractPeepholeOptimization optimization2 = new DeadCodeEliminationPeepholeOptimization();

    PeepholeOptimizationsPass optimizationsPass = new PeepholeOptimizationsPass(compiler, optimization1, optimization2);

    Node root = NodeOperations.parse("function add(x, y) {\n" +
        "  if (x > 10) {\n" +
        "    return y;\n" +
        "  } else {\n" +
        "    return x + y;\n" +
        "  }\n" +
        "}");

    optimizationsPass.process(null, root);

    Node expectedNode = NodeOperations.parse("function add(x) {\n" +
        "  if (x > 10) {\n" +
        "    return null;\n" +
        "  } else {\n" +
        "    return x + null;\n" +
        "  }\n" +
        "}");

    Node actualNode = NodeOperations.findChild(root, "returnStatement");

    NodeOperations.traverse(expectedNode, new NodeVisitor() {
      @Override
      public void visit(Node node) {
        assertTrue("Unexpected node encountered during traversal: " + node,
            node.getFirstChild().isNull());
      }

      @Override
      public boolean visitEnd(Node node) {
        return true;
      }
    });

    NodeOperations.traverse(actualNode, new NodeVisitor() {
      @Override
      public void visit(Node node) {
        assertTrue("Unexpected node encountered during traversal: " + node,
            node.getFirstChild().isNull());
      }

      @Override
      public boolean visitEnd(Node node) {
        assertTrue("Unexpected node structure encountered during traversal",
            actualNode.structureEquals(expectedNode));
        return true;
      }
    });
  }

  /**
   * Test that beginTraversal method calls beginTraversal on all optimizations.
   */
  @org.junit.Test
  public void testBeginTraversalCallsBeginTraversalOnAllOptimizations() {
    Compiler<Node> compiler = createCompiler();
    AbstractPeepholeOptimization optimization1 = new NoopPeepholeOptimization();
    AbstractPeepholeOptimization optimization2 = new NoopPeepholeOptimization();

    PeepholeOptimizationsPass optimizationsPass = new PeepholeOptimizationsPass(compiler, optimization1, optimization2);

    optimizationsPass.beginTraversal();

    assertTrue("beginTraversal should call beginTraversal on all optimizations: optimization1",
        optimization1.beginTraversalCalled);
    assertTrue("beginTraversal should call beginTraversal on all optimizations: optimization2",
        optimization2.beginTraversalCalled);
  }

  /**
   * Test that endTraversal method calls endTraversal on all optimizations.
   */
  @org.junit.Test
  public void testEndTraversalCallsEndTraversalOnAllOptimizations() {
    Compiler<Node> compiler = createCompiler();
    AbstractPeepholeOptimization optimization1 = new NoopPeepholeOptimization();
    AbstractPeepholeOptimization optimization2 = new NoopPeepholeOptimization();

    PeepholeOptimizationsPass optimizationsPass = new PeepholeOptimizationsPass(compiler, optimization1, optimization2);

    optimizationsPass.endTraversal();

    assertTrue("endTraversal should call endTraversal on all optimizations: optimization1",
        optimization1.endTraversalCalled);
    assertTrue("endTraversal should call endTraversal on all optimizations: optimization2",
        optimization2.endTraversalCalled);
  }
}