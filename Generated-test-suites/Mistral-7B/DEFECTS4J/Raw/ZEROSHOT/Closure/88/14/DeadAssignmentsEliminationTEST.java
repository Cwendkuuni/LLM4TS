import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeUtil.has;
import static com.google.javascript.rhino.NodeUtil.isAssign;
import static com.google.javascript.rhino.NodeUtil.isAssignmentOp;
import static com.google.javascript.rhino.NodeUtil.isFunction;
import static com.google.javascript.rhino.NodeUtil.isName;
import static com.google.javascript.rhino.NodeUtil.isLhs;
import static com.google.javascript.rhino.NodeUtil.getNode;
import static com.google.javascript.rhino.NodeUtil.getOpFromAssignmentOp;
import static com.google.common.base.Predicates.alwaysTrue;
import static com.google.common.base.Predicates.or;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractNodeTraversal;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.DataFlowAnalysis.FlowState;
import com.google.javascript.jscomp.GraphUtil;
import com.google.javascript.jscomp.LiveVariablesAnalysis.LiveVariableLattice;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.rhino.NodeUtil;

@RunWith(JUnit4.class)
public class DeadAssignmentsEliminationTest {

  private AbstractCompiler compiler;
  private ControlFlowGraph<Node> cfg;
  private Node root;
  private DiGraph<Node, Branch> graph;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    root = new Node(Token.PROGRAM, null);

    Node block = new Node(Token.BLOCK, null, root);
    root.addChild(block);

    Node cond = new Node(Token.IF, null, block);
    Node condExpr = new Node(Token.IDENT, "cond", null);
    cond.addChild(condExpr);
    Node trueBranch = new Node(Token.BLOCK, null, block);
    cond.addChild(trueBranch);
    Node falseBranch = new Node(Token.BLOCK, null, block);
    cond.addChild(falseBranch);

    Node assign1 = new Node(Token.ASSIGN, null, trueBranch);
    Node lhs1 = new Node(Token.IDENT, "x", null);
    Node rhs1 = new Node(Token.NUMBER, 1, null);
    assign1.addChild(lhs1);
    assign1.addChild(rhs1);

    Node assign2 = new Node(Token.ASSIGN, null, trueBranch);
    Node lhs2 = new Node(Token.IDENT, "x", null);
    Node rhs2 = new Node(Token.NUMBER, 2, null);
    assign2.addChild(lhs2);
    assign2.addChild(rhs2);

    Node assign3 = new Node(Token.ASSIGN, null, falseBranch);
    Node lhs3 = new Node(Token.IDENT, "x", null);
    Node rhs3 = new Node(Token.NUMBER, 3, null);
    assign3.addChild(lhs3);
    assign3.addChild(rhs3);

    Node assign4 = new Node(Token.ASSIGN, null, falseBranch);
    Node lhs4 = new Node(Token.IDENT, "x", null);
    Node rhs4 = new Node(Token.IDENT, "y", null);
    assign4.addChild(lhs4);
    assign4.addChild(rhs4);

    Node assign5 = new Node(Token.ASSIGN, null, block);
    Node lhs5 = new Node(Token.IDENT, "x", null);
    Node rhs5 = new Node(Token.IDENT, "y", null);
    assign5.addChild(lhs5);
    assign5.addChild(rhs5);

    Node varX = new Node(Token.VAR, null, block, "x");
    Var x = new Var(NodeUtil.getName(varX), NodeUtil.getLocation(varX));
    x.setInitialValue(NodeUtil.newNumber(1));
    block.setVariable("x", x);

    Node varY = new Node(Token.VAR, null, block, "y");
    Var y = new Var(NodeUtil.getName(varY), NodeUtil.getLocation(varY));
    y.setInitialValue(NodeUtil.newNumber(2));
    block.setVariable("y", y);

    cfg = new ControlFlowGraph<>(compiler, root);
    graph = cfg.getDirectedGraph();
  }

  @Test
  public void testDeadAssignmentsElimination() {
    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);

    assertEquals(3, Iterables.size(graph.nodes()));

    // Check that dead assignments were removed.
    Node nodeX1 = Iterables.get(graph.nodes(), 0);
    assertEquals(assign1, nodeX1.getValue());
    assertFalse(has(assign1, matchRemovableAssigns));

    Node nodeX2 = Iterables.get(graph.nodes(), 1);
    assertEquals(assign2, nodeX2.getValue());
    assertFalse(has(assign2, matchRemovableAssigns));

    Node nodeX3 = Iterables.get(graph.nodes(), 2);
    assertEquals(assign3, nodeX3.getValue());
    assertFalse(has(assign3, matchRemovableAssigns));

    // Check that assignments that are still live were not removed.
    Node nodeAssign5 = getNode(graph,getOpFromAssignmentOp(assign5), assign5);
    assertEquals(assign5, nodeAssign5.getValue());
    assertTrue(has(nodeAssign5, matchRemovableAssigns));

    // Check that there is only one path from the entry to the exit.
    assertEquals(1, GraphUtil.getPathsBetween(cfg.getEntry(), cfg.getExit()).size());
  }

  private static Predicate<Node> matchRemovableAssigns = new Predicate<Node>() {
    @Override
    public boolean apply(Node n) {
      return (isAssignmentOp(n) || n.getType() == Token.INC
          || n.getType() == Token.DEC)
          && (NodeUtil.isName(n.getFirstChild())
              || n.getFirstChild().getType() == Token.NAME);
    }
  };

  private static Predicate<Node> isVarAssign = new Predicate<Node>() {
    @Override
    public boolean apply(Node n) {
      return isAssign(n) && isName(n.getFirstChild());
    }
  };

  private static Predicate<Node> isName(Node n) {
    return or(isFunction(n), n.getType() == Token.NAME);
  }
}