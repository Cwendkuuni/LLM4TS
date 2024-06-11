package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

public class PeepholeOptimizationsPassTest {
  @Test
  public void testEmptyPass() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public Node parse(String source, ParseInfo info) {
        return null;
      }
    };
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler);
    Node root = null;
    pass.process(null, root);
  }

  @Test
  public void testSingleOptimizationPass() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public Node parse(String source, ParseInfo info) {
        return new Node();
      }
    };

    AbstractPeepholeOptimization optimization = new AbstractPeepholeOptimization() {
      @Override
      public Node optimizeSubtree(Node node) {
        return node;
      }
    };
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization);
    Node root = null;
    pass.process(null, root);
  }

  @Test
  public void testMultipleOptimizationsPass() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public Node parse(String source, ParseInfo info) {
        return new Node();
      }
    };

    AbstractPeepholeOptimization optimization1 = new AbstractPeepholeOptimization() {
      @Override
      public Node optimizeSubtree(Node node) {
        return node;
      }
    };

    AbstractPeepholeOptimization optimization2 = new AbstractPeepholeOptimization() {
      @Override
      public Node optimizeSubtree(Node node) {
        return node;
      }
    };
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization1, optimization2);
    Node root = null;
    pass.process(null, root);
  }

  @Test
  public void testTraversalState() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public Node parse(String source, ParseInfo info) {
        return new Node();
      }
    };

    AbstractPeepholeOptimization optimization = new AbstractPeepholeOptimization() {
      @Override
      public Node optimizeSubtree(Node node) {
        return node;
      }

      @Override
      public void beginTraversal(AbstractCompiler compiler) {
        Preconditions.checkState(compiler != null, "compiler cannot be null");
        // This optimization should report an error if a function is encountered
        // during traversal since this optimization doesn't support functions.
        if (node.isFunction()) {
          throw new RuntimeException("Encountered a function during traversal");
        }
      }

      @Override
      public void endTraversal(AbstractCompiler compiler) {
        // No-op
      }
    };

    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization);
    Node root = new Node();
    pass.process(null, root);

    Node functionNode = new Node();
    functionNode.setType(Node.FUNCTION);
    pass.traverse(functionNode);

    // The optimizations should not have traversed the function node
    assertFalse(optimization.wasFunctionEncounteredDuringTraversal());
  }

  @Test
  public void testTraversal() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public Node parse(String source, ParseInfo info) {
        return new Node();
      }
    };

    AbstractPeepholeOptimization optimization = new AbstractPeepholeOptimization() {
      @Override
      public Node optimizeSubtree(Node node) {
        return new Node();
      }

      @Override
      public void beginTraversal(AbstractCompiler compiler) {
        // No-op
      }

      @Override
      public void endTraversal(AbstractCompiler compiler) {
        // No-op
      }
    };

    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization);

    Node root = new Node();
    root.setType(Node.SCRIPT);

    // No children
    traverseAndAssertEqual(pass, root, root);

    // One child
    Node child = new Node();
    root.setFirstChild(child);
    traverseAndAssertEqual(pass, root, root);

    // Multiple children
    Node child2 = new Node();
    Node child3 = new Node();
    root.setFirstChild(child);
    root.setNext(child2);
    child.setNext(child3);
    traverseAndAssertEqual(pass, root, root);
  }

  @Test
  public void testTraversalWithChanges() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public Node parse(String source, ParseInfo info) {
        return new Node();
      }
    };

    AbstractPeepholeOptimization optimization = new AbstractPeepholeOptimization() {
      @Override
      public Node optimizeSubtree(Node node) {
        if (node.getType() == Node.NUMBER) {
          Node newNode = new Node();
          newNode.setType(Node.STRING);
          newNode.setString("42");
          return newNode;
        }
        return node;
      }

      @Override
      public void beginTraversal(AbstractCompiler compiler) {
        // No-op
      }

      @Override
      public void endTraversal(AbstractCompiler compiler) {
        // No-op
      }
    };

    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization);

    Node root = new Node();
    root.setType(Node.SCRIPT);

    Node numberNode = new Node();
    numberNode.setType(Node.NUMBER);
    numberNode.setInt(42);
    root.setFirstChild(numberNode);

    Node stringNode = new Node();
    stringNode.setType(Node.STRING);
    stringNode.setString("42");

    pass.process(null, root);
    traverseAndAssertEqual(pass, root, stringNode);
  }

  @Test
  public void testTraversalWithFunction() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public Node parse(String source, ParseInfo info) {
        return new Node();
      }
    };

    AbstractPeepholeOptimization optimization = new AbstractPeepholeOptimization() {
      @Override
      public Node optimizeSubtree(Node node) {
        return node;
      }

      @Override
      public void beginTraversal(AbstractCompiler compiler) {
        // This optimization should report an error if a function is encountered
        // during traversal since this optimization doesn't support functions.
        if (node.isFunction()) {
          throw new RuntimeException("Encountered a function during traversal");
        }
      }

      @Override
      public void endTraversal(AbstractCompiler compiler) {
        // No-op
      }
    };

    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization);

    Node root = new Node();
    root.setType(Node.SCRIPT);

    Node functionNode = new Node();
    functionNode.setType(Node.FUNCTION);
    root.setFirstChild(functionNode);

    try {
      pass.process(null, root);
      fail("Expected an exception to be thrown");
    } catch (RuntimeException e) {
      // Expected
    }
  }

  @Test
  public void testTraversalWithFunctionAndChanges() {
    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public Node parse(String source, ParseInfo info) {
        return new Node();
      }
    };

    AbstractPeepholeOptimization optimization = new AbstractPeepholeOptimization() {
      @Override
      public Node optimizeSubtree(Node node) {
        if (node.isFunction()) {
          Node newNode = new Node();
          newNode.setType(Node.BLOCK);
          ArrayList<Node> statements = Lists.newArrayList();
          statements.add(new Node());
          newNode.setFirstChild(new Node());
          newNode.setNext(new Node());
          newNode.setNext(new Node());
          newNode.setStatementList(statements);
          return newNode;
        }
        return node;
      }

      @Override
      public void beginTraversal(AbstractCompiler compiler) {
        // No-op
      }

      @Override
      public void endTraversal(AbstractCompiler compiler) {
        // No-op
      }
    };

    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization);

    Node root = new Node();
    root.setType(Node.SCRIPT);

    Node functionNode = new Node();
    functionNode.setType(Node.FUNCTION);
    root.setFirstChild(functionNode);

    Node parameter = new Node();
    parameter.setType(Node.IDENTIFIER);
    parameter.setName("p");
    functionNode.setParameters(new ArrayList<Node>() {{
      add(parameter);
    }});

    Node body = new Node();
    body.setType(Node.BLOCK);
    functionNode.setBody(body);

    Node statement = new Node();
    statement.setType(Node.ASSIGN);
    statement.setRightHandSide(new Node());
    statement.setRightHandSide(0, parameter);
    statement.setRightHandSide(1, new Node());
    statement.setRightHandSide(1, 0, new Node());
    statement.setRightHandSide(1, 1, new Node());
    statement.setRightHandSide(1, 1, 0, new Node());
    statement.setRightHandSide(1, 1, 1, new Node());
    statement.setRightHandSide(1, 1, 1, 0, new Node());
    statement.setRightHandSide(1, 1, 1, 1, new Node());
    statement.setStatementList(new ArrayList<Node>() {{
      add(statement);
    }});
    body.setStatementList(statement);

    pass.process(null, root);
    Node newFunctionNode = traversalState.peek().peekFunction();
    assertNull(newFunctionNode);
  }

  private void traverseAndAssertEqual(PeepholeOptimizationsPass pass, Node expected, Node actual) {
    pass.process(null, null);
    pass.traverse(actual);
    Node actualNode = traversalState.peek().getCurrentNode();
    assertSame(expected, actualNode);
  }

  private StateStack traversalState = new StateStack();
}