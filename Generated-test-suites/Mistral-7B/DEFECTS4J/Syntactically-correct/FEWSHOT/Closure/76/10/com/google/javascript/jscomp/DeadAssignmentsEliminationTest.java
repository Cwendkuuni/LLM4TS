package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.jscomp.graph.DiGraphNode;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DataFlowAnalysis.FlowState;
import com.google.javascript.jscomp.LiveVariablesAnalysis.LiveVariableLattice;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.ScopedCallback;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() throws Exception {
    Compiler compiler = new CompilerWithSourceMapping(new CompilerOptions());
    String sourceCode = "function foo() {\n" +
        "  var x = 1;\n" +
        "  x = 2;\n" +
        "  if (true) {\n" +
        "    x = 3;\n" +
        "  } else {\n" +
        "    x = 4;\n" +
        "  }\n" +
        "  return x;\n" +
        "}";

    Node root = Util.parseJs(sourceCode, null);

    DeadAssignmentsElimination elimination = new DeadAssignmentsElimination(compiler);
    elimination.process(null, root);

    String outputCode = compiler.getSource().getCode();
    Node outputRoot = Util.parseJs(outputCode, null);

    Node expectedOutputRoot = new Node(Token.FUNCTION, null, null, "foo");
    expectedOutputRoot.setLineInfo(new LineInfo(1, 0));
    expectedOutputRoot.addChild(new Node(Token.VAR, null, null, "x"));
    expectedOutputRoot.addChild(new Node(Token.ASSIGN, null, "x",
        new Node(Token.NUM, null, null, 2)));
    expectedOutputRoot.addChild(new Node(Token.IF, null, null, null, null,
        new Node(Token.BOOLEAN, null, null, true)));
    expectedOutputRoot.addChild(new Node(Token.ASSIGN, null, "x",
        new Node(Token.NUM, null, null, 3)));
    expectedOutputRoot.addChild(new Node(Token.ELSE));
    expectedOutputRoot.addChild(new Node(Token.ASSIGN, null, "x",
        new Node(Token.NUM, null, null, 4)));
    expectedOutputRoot.addChild(new Node(Token.RETURN, null, null, null, null,
        new Node(Token.VAR, null, null, "x")));

    NodeTraversal traversal = new NodeTraversal(expectedOutputRoot, null);
    DiGraph<Node, Branch> controlFlowGraph = traversal.getControlFlowGraph();

    // Check the control flow graph is the same as expected
    DiGraphNode<Node, Branch> rootNode = controlFlowGraph.getNode(expectedOutputRoot);
    assertEquals(5, controlFlowGraph.getEdges(rootNode).size());

    // Perform dead code elimination test
    elimination.process(null, root);

    String finalOutputCode = compiler.getSource().getCode();
    Node finalOutputRoot = Util.parseJs(finalOutputCode, null);

    Node deadXAssignment = null;
    for (Node node = finalOutputRoot.getFirstChild(); node != null;
        node = node.getNext()) {
      if (node.getType() == Token.ASSIGN &&
          node.getFirstChild().getString().equals("x")) {
        deadXAssignment = node;
        break;
      }
    }

    // Check the dead X assignment is removed
    assertNull(deadXAssignment);
  }
}

class Util {
  // Helper methods for parsing JS code and checking if nodes are equal
}