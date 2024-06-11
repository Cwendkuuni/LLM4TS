package org.apache.commons.cli.test; // change the package name if necessary

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.graph.DiGraphNode;
import com.google.javascript.jscomp.graph.DiGraph.DiGraphEdge;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;

public class UnreachableCodeEliminationTest {
  private AbstractCompiler compiler;
  private ControlFlowAnalysis cfa;
  private ControlFlowGraph cfg;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    cfa = new ControlFlowAnalysis(compiler, false, false);
    cfg = cfa.getCfg();
  }

  @Test
  public void testSimple() {
    Node node = new Node(Token.RETURN, null, null);
    DiGraphNode<Node, Branch> gNode = cfg.getDirectedGraphNode(node);
    when(compiler.reportCodeChange()).thenReturn(true);

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, null);

    assertFalse(gNode.isPresent());
    if (logger.isLoggable(Level.FINE)) {
      logger.fine("Removed " + node.toString());
    }
  }

  @Test
  public void testBreak() {
    Node label = new Node(Token.LABEL, "label", null);
    Node breakNode = new Node(Token.BREAK, null, new Node[] { label });

    DiGraphNode<Node, Branch> labelNode = cfg.getDirectedGraphNode(label);
    DiGraphNode<Node, Branch> breakNodeGNode = cfg.getDirectedGraphNode(breakNode);

    when(compiler.reportCodeChange()).thenReturn(true);

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, null);

    assertFalse(breakNodeGNode.isPresent());
    assertSame(labelNode.getValue(), breakNode.getNext());
    if (logger.isLoggable(Level.FINE)) {
      logger.fine("Removed " + breakNode.toString());
    }
  }

  @Test
  public void testContinue() {
    Node label = new Node(Token.LABEL, "label", null);
    Node continueNode = new Node(Token.CONTINUE, null, new Node[] { label });

    DiGraphNode<Node, Branch> labelNode = cfg.getDirectedGraphNode(label);
    DiGraphNode<Node, Branch> continueNodeGNode = cfg.getDirectedGraphNode(continueNode);

    when(compiler.reportCodeChange()).thenReturn(true);

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, null);

    assertFalse(continueNodeGNode.isPresent());
    assertSame(labelNode.getValue(), continueNode.getNext());
    if (logger.isLoggable(Level.FINE)) {
      logger.fine("Removed " + continueNode.toString());
    }
  }

  @Test
  public void testReturn() {
    Node label = new Node(Token.LABEL, "label", null);
    Node returnNode = new Node(Token.RETURN, null, new Node[] { label });

    DiGraphNode<Node, Branch> labelNode = cfg.getDirectedGraphNode(label);
    DiGraphNode<Node, Branch> returnNodeGNode = cfg.getDirectedGraphNode(returnNode);

    when(compiler.reportCodeChange()).thenReturn(true);

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, null);

    assertFalse(returnNodeGNode.isPresent());
    assertSame(labelNode.getValue(), returnNode.getNext());
    if (logger.isLoggable(Level.FINE)) {
      logger.fine("Removed " + returnNode.toString());
    }
  }

  @Test
  public void testRemoveNoOpStatements() {
    Node node1 = new Node(Token.ASSIGN, null, new Node[] {
        new Node(Token.IDENT, "x"),
        new Node(Token.IDENT, "y")
    });

    Node node2 = new Node(Token.ASSIGN, null, new Node[] {
        new Node(Token.IDENT, "y"),
        new Node(Token.NUMBER, 1)
    });

    Node node3 = new Node(Token.ASSIGN, null, new Node[] { node1, node2 });

    Node parent = new Node(Token.BLOCK, null, Arrays.asList(node1, node2, node3));

    DiGraphNode<Node, Branch> node1GNode = cfg.getDirectedGraphNode(node1);
    DiGraphNode<Node, Branch> node2GNode = cfg.getDirectedGraphNode(node2);
    DiGraphNode<Node, Branch> node3GNode = cfg.getDirectedGraphNode(node3);

    when(compiler.reportCodeChange()).thenReturn(true);

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(null, null);

    assertFalse(node1GNode.isPresent());
    assertFalse(node2GNode.isPresent());
    assertFalse(node3GNode.isPresent());
    if (logger.isLoggable(Level.FINE)) {
      logger.fine("Removed " + node1.toString());
      logger.fine("Removed " + node2.toString());
      logger.fine("Removed " + node3.toString());
    }
  }

  @Test
  public void testRemoveDeadBranch() {
    Node label1 = new Node(Token.LABEL, "label1", null);
    Node ifNode = new Node(Token.IF, null, new Node[] {
        new Node(Token.IDENT, "x"),
        label1,
        new Node(Token.RETURN, null, null)
    });
    Node elseNode = new Node(Token.BLOCK, null, Arrays.asList(new Node(Token.RETURN, null, null)));

    DiGraphNode<Node, Branch> label1Node = cfg.getDirectedGraphNode(label1);
    DiGraphNode<Node, Branch> ifNodeGNode = cfg.getDirectedGraphNode(ifNode);
    DiGraphNode<Node, Branch> elseNodeGNode = cfg.getDirectedGraphNode(elseNode);

    when(compiler.reportCodeChange()).thenReturn(true);

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, null);

    assertFalse(ifNodeGNode.isPresent());
    assertFalse(elseNodeGNode.isPresent());
    if (logger.isLoggable(Level.FINE)) {
      logger.fine("Removed " + ifNode.toString());
      logger.fine("Removed " + elseNode.toString());
    }
  }

  @Test
  public void testRemoveNoOpStatementsWithTryCatch() {
    Node tryNode = new Node(Token.TRY, null, new Node[] {
        new Node(Token.BLOCK, null, Arrays.asList(
            new Node(Token.CATCH, null, null),
            new Node(Token.BLOCK, null, Arrays.asList(
                new Node(Token.ASSIGN, null, new Node[] {
                  new Node(Token.IDENT, "x"),
                  new Node(Token.NUMBER, 1)
                })
            ))
        ))
    });

    DiGraphNode<Node, Branch> tryNodeGNode = cfg.getDirectedGraphNode(tryNode);

    when(compiler.reportCodeChange()).thenReturn(true);

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(null, null);

    assertFalse(tryNodeGNode.isPresent());
    if (logger.isLoggable(Level.FINE)) {
      logger.fine("Removed " + tryNode.toString());
    }
  }

  @Test
  public void testRemoveForInHeader() {
    Node variable = new Node(Token.VAR, null, null);
    Node header = new Node(Token.FOR_IN, null, new Node[] {
        new Node(Token.IDENT, "x"),
        variable
    });
    Node statements = new Node(Token.BLOCK, null, Arrays.asList(new Node(Token.RETURN, null, null)));

    DiGraphNode<Node, Branch> headerGNode = cfg.getDirectedGraphNode(header);

    when(compiler.reportCodeChange()).thenReturn(true);

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, null);

    assertTrue(headerGNode.isPresent());
    if (logger.isLoggable(Level.FINE)) {
      logger.fine("Did not remove " + header.toString());
    }
  }

  @Test
  public void testRemoveBlockWithVar() {
    Node variable = new Node(Token.VAR, null, null);
    Node statements = new Node(Token.BLOCK, null, Arrays.asList(variable, new Node(Token.RETURN, null, null)));
    Node parent = new Node(Token.BLOCK, null, Arrays.asList(statements));

    DiGraphNode<Node, Branch> statementsGNode = cfg.getDirectedGraphNode(statements);
    DiGraphNode<Node, Branch> variableGNode = cfg.getDirectedGraphNode(variable);

    when(compiler.reportCodeChange()).thenReturn(true);

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, null);

    assertFalse(statementsGNode.isPresent());
    assertFalse(variableGNode.isPresent());
    if (logger.isLoggable(Level.FINE)) {
      logger.fine("Removed " + statements.toString());
      logger.fine("Removed " + variable.toString());
    }
  }
}