package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractCompiler.CompilerPass;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.DataFlowAnalysis.FlowState;
import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.jscomp.graph.DiGraphNode;
import com.google.javascript.rhino.NodeTraversal.ScopedCallback;

import org.junit.Test;
import org.mockito.Mockito;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ControlFlowGraph cfg = mock(ControlFlowGraph.class);

    LiveVariablesAnalysis liveness = new LiveVariablesAnalysis(cfg, null, compiler);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Scope scope = Mockito.mock(Scope.class);

    Node root = new Node(Token.PROGRAM, null);

    Node a = new Node(Token.VAR, new Node(Token.NAME, "a"));
    Node b = new Node(Token.VAR, new Node(Token.NAME, "b"));
    Node c = new Node(Token.VAR, new Node(Token.NAME, "c"));

    Node x = new Node(Token.ASSIGN, a, new Node(Token.NAME, "x"));
    Node y = new Node(Token.ASSIGN, b, new Node(Token.NAME, "y"));
    Node z = new Node(Token.ASSIGN, c, new Node(Token.NAME, "z"));

    Node d = new Node(Token.ASSIGN, a, new Node(Token.NAME, "d"));
    Node e = new Node(Token.ASSIGN, b, new Node(Token.NAME, "e"));
    Node f = new Node(Token.ASSIGN, c, new Node(Token.NAME, "f"));

    Node condition = new Node(Token.IF, new Node(Node.isCondition(a), null), null);
    Node ifBlock = new Node(Token.BLOCK, null);
    Node elseBlock = new Node(Token.BLOCK, null);

    Node assignA = new Node(Token.ASSIGN, a, new Node(Token.NUM, 1));
    Node assignB = new Node(Token.ASSIGN, b, new Node(Token.NUM, 1));
    Node assignC = new Node(Token.ASSIGN, c, new Node(Token.NUM, 1));

    Node assignmentOp = new Node(Token.ASSIGN, a, a);

    Node assignments = new Node(Token.BLOCK, null);
    assignments.addChild(assignA);
    assignments.addChild(d);
    assignments.addChild(assignB);
    assignments.addChild(e);
    assignments.addChild(assignC);
    assignments.addChild(f);

    condition.addChild(assignments);
    ifBlock.addChild(x);
    ifBlock.addChild(y);
    ifBlock.addChild(z);
    elseBlock.addChild(new Node(Token.ASSIGN, a, new Node(Token.NUM, 2)));
    elseBlock.addChild(new Node(Token.ASSIGN, b, new Node(Token.NUM, 2)));
    elseBlock.addChild(new Node(Token.ASSIGN, c, new Node(Token.NUM, 2)));

    condition.addChild(ifBlock);
    condition.addChild(elseBlock);

    root.addChild(condition);

    when(cfg.getDirectedGraphNodes()).thenReturn(ImmutableList.of(
        Mockito.mock(DiGraphNode.class),
        Mockito.mock(DiGraphNode.class)));

    when(liveness.getEscapedLocals()).thenReturn(ImmutableList.of());

    when(liveness.getIn()).thenReturn(
        FlowState.builder().build());

    when(liveness.getOut()).thenReturn(
        FlowState.builder().build());

    when(cfg.getControlFlowGraph()).thenReturn(cfg);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, root);
    nodeTraversal.setScopedCallback(deadAssignmentsElimination);

    nodeTraversal.traverse();

    Var aVar = Mockito.mock(Var.class);
    when(scope.getVar("a")).thenReturn(aVar);

    Var bVar = Mockito.mock(Var.class);
    when(scope.getVar("b")).thenReturn(bVar);

    Var cVar = Mockito.mock(Var.class);
    when(scope.getVar("c")).thenReturn(cVar);

    when(scope.isDeclared(Mockito.anyString(), Mockito.eq(false))).then(
        invocation -> {
          Object arg = invocation.getArgumentAt(0, 0);
          if (arg.equals("a")) {
            return aVar;
          } else if (arg.equals("b")) {
            return bVar;
          } else if (arg.equals("c")) {
            return cVar;
          }
          throw new IllegalStateException();
        });

    when(compiler.getCompilerOptions().getOption(CompilerPass.ClassIds.DEAD_ELIMINATION))
        .thenReturn(true);

    DeadAssignmentsElimination.tryRemoveDeadAssignments(nodeTraversal, cfg);

    DiGraphNode<Node, Branch> node1 = Mockito.mock(DiGraphNode.class);
    DiGraphNode<Node, Branch> node2 = Mockito.mock(DiGraphNode.class);

    when(node1.getValue()).thenReturn(a);
    when(node2.getValue()).thenReturn(condition);

    when(node1.getAnnotation()).thenReturn(
        FlowState.builder().setIn(FlowState.builder().setLive(aVar).build())
            .setOut(FlowState.builder().setLive(aVar).build()).build());

    when(node2.getAnnotation()).thenReturn(
        FlowState.builder().build());

    Branch branch = Mockito.mock(Branch.class);
    when(node1.getNext()).thenReturn(node2);
    when(node2.getPrev()).thenReturn(node1);
    when(node2.getBranch()).thenReturn(branch);

    when(cfg.getCFGNodeFor(a)).thenReturn(node1);
    when(cfg.getCFGNodeFor(condition)).thenReturn(node2);

    assertEquals(0, deadAssignmentsElimination.countDeadAssignments(nodeTraversal));

    assertEquals(2, root.getChildCount());

    assertEquals(2, condition.getChildCount());

    assertEquals(3, ifBlock.getChildCount());

    assertEquals(3, elseBlock.getChildCount());

    assertEquals(6, root.getDescendantsCount());

    assertEquals(3, condition.getDescendantsCount());

    assertEquals(3, ifBlock.getDescendantsCount());

    assertEquals(3, elseBlock.getDescendantsCount());

    assertEquals(0, a.getChildCount());

    assertEquals(1, x.getChildCount());

    assertEquals(0, b.getChildCount());

    assertEquals(1, y.getChildCount());

    assertEquals(0, c.getChildCount());

    assertEquals(1, z.getChildCount());

    assertEquals(1, d.getChildCount());

    assertEquals(0, e.getChildCount());

    assertEquals(1, f.getChildCount());

    assertEquals(1, assignA.getChildCount());

    assertEquals(0, assignB.getChildCount());

    assertEquals(0, assignC.getChildCount());

    assertEquals(0, assignmentOp.getChildCount());

    assertEquals(1, z.getFirstChild().getChildCount());

    assertEquals(1, d.getFirstChild().getChildCount());

    assertEquals(0, d.getLastChild().getChildCount());

    assertEquals(0, e.getFirstChild().getChildCount());

    assertEquals(0, e.getLastChild().getChildCount());

    assertEquals(0, f.getFirstChild().getChildCount());

    assertEquals(0, f.getLastChild().getChildCount());

    assertEquals(1, x.getFirstChild().getChildCount());

    assertEquals(1, y.getFirstChild().getChildCount());

    assertEquals(1, y.getLastChild().getChildCount());

    // Dead assignments have been eliminated.
    assertEquals(0, root.getChildAt(1).getChildCount());

    assertEquals(0, root.getChildAt(1).getDescendantsCount());

    // a is still live before and after the assignment.
    assertEquals(VariableLiveness.READ,
        DeadAssignmentsElimination.isVariableReadBeforeKill(assignA, "a"));

    assertEquals(VariableLiveness.READ,
        DeadAssignmentsElimination.isVariableReadBeforeKill(assignments, "a"));

    // b is only live after the assignment.
    assertEquals(VariableLiveness.MAYBE_LIVE,
        DeadAssignmentsElimination.isVariableReadBeforeKill(assignB, "b"));

    assertEquals(VariableLiveness.READ,
        DeadAssignmentsElimination.isVariableReadBeforeKill(assignments, "b"));

    // c is only live after the assignment.
    assertEquals(VariableLiveness.MAYBE_LIVE,
        DeadAssignmentsElimination.isVariableReadBeforeKill(assignC, "c"));

    assertEquals(VariableLiveness.READ,
        DeadAssignmentsElimination.isVariableReadBeforeKill(assignments, "c"));

    // a is killed by the assignment to d.
    assertEquals(VariableLiveness.KILL,
        DeadAssignmentsElimination.isVariableReadBeforeKill(d, "a"));

    assertEquals(VariableLiveness.READ,
        DeadAssignmentsElimination.isVariableReadBeforeKill(assignments, "a"));

    // b is not killed by any assignment.
    assertEquals(VariableLiveness.READ,
        DeadAssignmentsElimination.isVariableReadBeforeKill(e, "b"));

    assertEquals(VariableLiveness.READ,
        DeadAssignmentsElimination.isVariableReadBeforeKill(assignments, "b"));

    // c is not killed by any assignment.
    assertEquals(VariableLiveness.READ,
        DeadAssignmentsElimination.isVariableReadBeforeKill(f, "c"));

    assertEquals(VariableLiveness.READ,
        DeadAssignmentsElimination.isVariableReadBeforeKill(assignments, "c"));

    // a is re-assigned, so it must be still live.
    assertEquals(VariableLiveness.READ,
        DeadAssignmentsElimination.isVariableReadBeforeKill(assignments, "a"));

    // b is re-assigned, so it must be still live.
    assertEquals(VariableLiveness.READ,
        DeadAssignmentsElimination.isVariableReadBeforeKill(assignments, "b"));

    // c is re-assigned, so it must be still live.
    assertEquals(VariableLiveness.READ,
        DeadAssignmentsElimination.isVariableReadBeforeKill(assignments, "c"));

    // a is killed by the conditional branch.
    assertEquals(VariableLiveness.KILL,
        DeadAssignmentsElimination.isVariableReadBeforeKill(condition, "a"));

    assertEquals(VariableLiveness.READ,
        DeadAssignmentsElimination.isVariableReadBeforeKill(assignments, "a"));

    // b is still live before the conditional branch.
    assertEquals(VariableLiveness.READ,
        DeadAssignmentsElimination.isVariableReadBeforeKill(condition, "b"));

    assertEquals(VariableLiveness.READ,
        DeadAssignmentsElimination.isVariableReadBeforeKill(assignments, "b"));

    // c is still live before the conditional branch.
    assertEquals(VariableLiveness.READ,
        DeadAssignmentsElimination.isVariableReadBeforeKill(condition, "c"));

    assertEquals(VariableLiveness.READ,
        DeadAssignmentsElimination.isVariableReadBeforeKill(assignments, "c"));

    // a is not killed by the assignment in the else block.
    assertEquals(VariableLiveness.READ,
        DeadAssignmentsElimination.isVariableReadBeforeKill(
            root.getChildAt(1), "a"));

    assertEquals(VariableLiveness.READ,
        DeadAssignmentsElimination.isVariableReadBeforeKill(assignments, "a"));

    // b is still live in the else block.
    assertEquals(VariableLiveness.READ,
        DeadAssignmentsElimination.isVariableReadBeforeKill(
            root.getChildAt(1), "b"));

    assertEquals(VariableLiveness.READ,
        DeadAssignmentsElimination.isVariableReadBeforeKill(assignments, "b"));

    // c is still live in the else block.
    assertEquals(VariableLiveness.READ,
        DeadAssignmentsElimination.isVariableReadBeforeKill(
            root.getChildAt(1), "c"));

    assertEquals(VariableLiveness.READ,
        DeadAssignmentsElimination.isVariableReadBeforeKill(assignments, "c"));
  }

  private static int countDeadAssignments(
      NodeTraversal nodeTraversal) {
    int counter = 0;
    nodeTraversal.traverse();
    for (Node node = nodeTraversal.getCurrentNode(); node != null;
        node = node.getParent()) {
      if (DeadAssignmentsElimination.matchRemovableAssigns.apply(node)) {
        counter++;
      }
    }
    return counter;
  }
}