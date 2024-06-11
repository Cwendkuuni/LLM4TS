package com.google.javascript.jscomp.tests;

import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Test;

import java.util.List;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

    String sourceCode = "function foo() {};\n" +
        "function bar() {\n" +
        "  var x = 1;\n" +
        "  x = foo();\n" +
        "}\n" +
        "function test() {\n" +
        "  bar();\n" +
        "  return 1;\n" +
        "}";

    Node root = NodeFactory.createRootNode(sourceCode);
    NodeTraversal.traverse(compiler, root, checkSideEffects);

    List<Node> problemNodes = checkSideEffects.problemNodes;
    assertTrue("No problem nodes found.", !problemNodes.isEmpty());
    Node problemNode = problemNodes.get(0);
    assertTrue("Found problem node is not in bar() function.", problemNode.getParent().isName() && problemNode.getParent().getString().equals("bar"));
    assertTrue("Problem node is not an assignment.", problemNode.isAssignment());

    problemNodes.clear();
    sourceCode = "function foo() {};\n" +
        "function test() {\n" +
        "  foo();;\n" +
        "  return 1;\n" +
        "}";
    root = NodeFactory.createRootNode(sourceCode);
    NodeTraversal.traverse(compiler, root, checkSideEffects);

    assertTrue("No problem nodes found.", problemNodes.isEmpty());

    problemNodes.clear();
    sourceCode = "function foo() {};\n" +
        "function test() {\n" +
        "  function bar() {\n" +
        "    var x = 1;\n" +
        "    x = foo();\n" +
        "  }\n" +
        "  bar();\n" +
        "  return 1;\n" +
        "}";
    root = NodeFactory.createRootNode(sourceCode);
    NodeTraversal.traverse(compiler, root, checkSideEffects);

    assertTrue("No problem nodes found.", problemNodes.isEmpty());

    problemNodes.clear();
    sourceCode = "function foo() { throw new Error('Something went wrong.'); }";
    root = NodeFactory.createRootNode(sourceCode);
    NodeTraversal.traverse(compiler, root, checkSideEffects);

    assertTrue("No problem nodes found.", problemNodes.isEmpty());

    problemNodes.clear();
    sourceCode = "function foo() { return 1; }";
    root = NodeFactory.createRootNode(sourceCode);
    NodeTraversal.traverse(compiler, root, checkSideEffects);

    assertTrue("No problem nodes found.", problemNodes.isEmpty());

    problemNodes.clear();
    sourceCode = "function test() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  x = y + 1;\n" +
        "  y = x + 1;\n" +
        "  return x + y;\n" +
        "}";
    root = NodeFactory.createRootNode(sourceCode);
    NodeTraversal.traverse(compiler, root, checkSideEffects);

    assertTrue("No problem nodes found.", problemNodes.isEmpty());

    problemNodes.clear();
    sourceCode = "function test() {\n" +
        "  var x = 0;\n" +
        "  if (foo()) {\n" +
        "    x = 1;\n" +
        "  }\n" +
        "  return x;\n" +
        "}";
    root = NodeFactory.createRootNode(sourceCode);
    NodeTraversal.traverse(compiler, root, checkSideEffects);

    assertTrue("No problem nodes found.", problemNodes.isEmpty());

    problemNodes.clear();
    sourceCode = "function test() {\n" +
        "  var x = 0;\n" +
        "  if (foo()) {\n" +
        "    x = 1;\n" +
        "  } else {\n" +
        "    x = 2;\n" +
        "  }\n" +
        "  return x;\n" +
        "}";
    root = NodeFactory.createRootNode(sourceCode);
    NodeTraversal.traverse(compiler, root, checkSideEffects);

    assertTrue("No problem nodes found.", problemNodes.isEmpty());

    problemNodes.clear();
    sourceCode = "function test() {\n" +
        "  var x = 0;\n" +
        "  if (foo()) {\n" +
        "    x = 1;\n" +
        "  } else if (foo()) {\n" +
        "    x = 2;\n" +
        "  } else {\n" +
        "    x = 3;\n" +
        "  }\n" +
        "  return x;\n" +
        "}";
    root = NodeFactory.createRootNode(sourceCode);
    NodeTraversal.traverse(compiler, root, checkSideEffects);

    assertTrue("No problem nodes found.", problemNodes.isEmpty());

    problemNodes.clear();
    sourceCode = "function test() {\n" +
        "  var x = 0;\n" +
        "  if (foo()) {\n" +
        "    x = 1;\n" +
        "  } else if (foo()) {\n" +
        "    x = 2;\n" +
        "  } else if (foo()) {\n" +
        "    x = 3;\n" +
        "  } else {\n" +
        "    x = 4;\n" +
        "  }\n" +
        "  return x;\n" +
        "}";
    root = NodeFactory.createRootNode(sourceCode);
    NodeTraversal.traverse(compiler, root, checkSideEffects);

    assertTrue("No problem nodes found.", problemNodes.isEmpty());

    problemNodes.clear();
    sourceCode = "function test() {\n" +
        "  var x = 0;\n" +
        "  if (foo()) {\n" +
        "    x = 1;\n" +
        "  } else if (foo()) {\n" +
        "    x = 2;\n" +
        "  } else if (foo()) {\n" +
        "    x = 3;\n" +
        "  } else if (foo()) {\n" +
        "    x = 4;\n" +
        "  } else {\n" +
        "    x = 5;\n" +
        "  }\n" +
        "  return x;\n" +
        "}";
    root = NodeFactory.createRootNode(sourceCode);
    NodeTraversal.traverse(compiler, root, checkSideEffects);

    assertTrue("No problem nodes found.", problemNodes.isEmpty());

    problemNodes.clear();
    sourceCode = "function test() {\n" +
        "  var x = 0;\n" +
        "  if (foo()) {\n" +
        "    x = 1;\n" +
        "  } else if (foo()) {\n" +
        "    x = 2;\n" +
        "  } else if (foo()) {\n" +
        "    x = 3;\n" +
        "  } else if (foo()) {\n" +
        "    x = 4;\n" +
        "  } else if (foo()) {\n" +
        "    x = 5;\n" +
        "  } else {\n" +
        "    x = 6;\n" +
        "  }\n" +
        "  return x;\n" +
        "}";
    root = NodeFactory.createRootNode(sourceCode);
    NodeTraversal.traverse(compiler, root, checkSideEffects);

    assertTrue("No problem nodes found.", problemNodes.isEmpty());

    problemNodes.clear();
    sourceCode = "function test() {\n" +
        "  var x = 0;\n" +
        "  if (foo()) {\n" +
        "    x = 1;\n" +
        "  } else if (foo()) {\n" +
        "    x = 2;\n" +
        "  } else if (foo()) {\n" +
        "    x = 3;\n" +
        "  } else if (foo()) {\n" +
        "    x = 4;\n" +
        "  } else if (foo()) {\n" +
        "    x = 5;\n" +
        "  } else if (foo()) {\n" +
        "    x = 6;\n" +
        "  } else if (foo()) {\n" +
        "    x = 7;\n" +
        "  }\n" +
        "  return x;\n" +
        "}";
    root = NodeFactory.createRootNode(sourceCode);
    NodeTraversal.traverse(compiler, root, checkSideEffects);

    assertTrue("No problem nodes found.", problemNodes.isEmpty());

    problemNodes.clear();
    sourceCode = "function test() {\n" +
        "  var x = 0;\n" +
        "  if (foo()) {\n" +
        "    x = 1;\n" +
        "  } else if (foo()) {\n" +
        "    x = 2;\n" +
        "  } else if (foo()) {\n" +
        "    x = 3;\n" +
        "  } else if (foo()) {\n" +
        "    x = 4;\n" +
        "  } else if (foo()) {\n" +
        "    x = 5;\n" +
        "  } else if (foo()) {\n" +
        "    x = 6;\n" +
        "  } else if (foo()) {\n" +
        "    x = 7;\n" +
        "  } else if (foo()) {\n" +
        "    x = 8;\n" +
        "  } else if (foo()) {\n" +
        "    x = 9;\n" +
        "  } else if (foo()) {\n" +
        "    x = 10;\n" +
        "  }\n" +
        "  return x;\n" +
        "}";
    root = NodeFactory.createRootNode(sourceCode);
    NodeTraversal.traverse(compiler, root, checkSideEffects);

    assertTrue("No problem nodes found.", problemNodes.isEmpty());

    problemNodes.clear();
    sourceCode = "function test() {\n" +
        "  var x = 0;\n" +
        "  if (foo()) {\n" +
        "    x = 1;\n" +
        "  } else if (foo()) {\n" +
        "    x = 2;\n" +
        "  } else if (foo()) {\n" +
        "    x = 3;\n" +
        "  } else if (foo()) {\n" +
        "    x = 4;\n" +
        "  } else if (foo()) {\n" +
        "    x = 5;\n" +
        "  } else if (foo()) {\n" +
        "    x = 6;\n" +
        "  } else if (foo()) {\n" +
        "    x = 7;\n" +
        "  } else if (foo()) {\n" +
        "    x = 8;\n" +
        "  } else if (foo()) {\n" +
        "    x = 9;\n" +
        "  } else if (foo()) {\n" +
        "    x = 10;\n" +
        "  } else if (foo()) {\n" +
        "    x = 11;\n" +
        "  } else if (foo()) {\n" +
        "    x = 12;\n" +
        "  } else if (foo()) {\n" +
        "    x = 13;\n" +
        "  } else if (foo()) {\n" +
        "    x = 14;\n" +
        "  } else if (foo()) {\n" +
        "    x = 15;\n" +
        "  }\n" +
        "  return x;\n" +
        "}";
    root = NodeFactory.createRootNode(sourceCode);
    NodeTraversal.traversalFailed = false;
    try {
      NodeTraversal.traverse(compiler, root, checkSideEffects);
      fail("Expected evaluation to fail due to stack overflow.");
    } catch (RuntimeException e) {
      // Expected
    }

    assertTrue("Problem nodes found.", !problemNodes.isEmpty());
  }

  @Test
  public void testProtectSideEffects() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

    String sourceCode = "function foo() {};\n" +
        "function test() {\n" +
        "  var x = 0;\n" +
        "  x = foo();\n" +
        "  return x;\n" +
        "}";

    Node root = NodeFactory.createRootNode(sourceCode);
    NodeTraversal.traverse(compiler, root, checkSideEffects);

    List<Node> problemNodes = checkSideEffects.problemNodes;
    assertTrue("No problem nodes found.", !problemNodes.isEmpty());

    compiler.addPass(new StripProtection(compiler));
    NodeTraversal.traverse(compiler, root, checkSideEffects);

    problemNodes.clear();
    assertTrue("Problem nodes not removed.", problemNodes.isEmpty());
  }
}

class StripProtection extends CompilerPass {
  private final Compiler compiler;

  StripProtection(Compiler compiler) {
    this.compiler = compiler;
  }

  @Override
  public void process(Node externs, Node root) {
    // Empty implementation
  }

  @Override
  public void visit(NodeTraversal t, Node n, Node parent) {
    if (n.isCall()) {
      Node target = n.getFirstChild();
      if (target.isName() && target.getString().equals("JSCOMPILER_PRESERVE")) {
        Node expr = n.getLastChild();
        n.detachChildren();
        parent.replaceChild(n, expr);
      }
    }
  }
}