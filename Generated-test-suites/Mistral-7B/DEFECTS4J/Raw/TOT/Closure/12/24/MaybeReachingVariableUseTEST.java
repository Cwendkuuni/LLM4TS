import static org.junit.Assert.*;
import static com.google.javascript.jscomp.MaybeReachingVariableUse.*;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.Collection;
import java.util.List;

public class MaybeReachingVariableUseTest {

  private static ControlFlowGraph<Node> createSimpleGraph() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope scope = new Scope();

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

    Node A = Node.newIntLiteral(0);
    Node B = Node.newName("B");
    Node C = Node.newName("C");

    Node assignA = Node.newAssign(A, Node.newName("A"));
    Node assignB = Node.newAssign(B, Node.newName("A"));
    Node assignC = Node.newAssign(C, Node.newName("A"));

    Node ifStatement = Node.newIf(Node.newExprAnd(Node.newBooleanLiteral(true), Node.newBooleanLiteral(false)), assignB, assignC);
    Node printStatement = Node.newPrint(Node.newName("A"));

    List<Node> entryBlock = Node.newList(assignA, ifStatement, printStatement);
    List<Node> exitBlock = Node.newList();

    Node startNode = cfg.addBlock(entryBlock, exitBlock, null, null);
    cfg.addEdge(assignA, startNode, null);
    cfg.addEdge(ifStatement, startNode, null);
    cfg.addEdge(printStatement, startNode, null);

    return cfg;
  }

  @org.junit.Test
  public void testSimpleGraph() {
    ControlFlowGraph<Node> cfg = createSimpleGraph();
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, null, null);

    // Check that 'A' may be used at print statement
    Collection<Node> uses = analysis.getUses("A", cfg.getNode(cfg.getEntryNode()));
    assertEquals(1, uses.size());
    assertEquals(cfg.getNode(cfg.getEntryNode()).getChild(2), uses.iterator().next());
  }

  @org.junit.Test
  public void testNoUses() {
    ControlFlowGraph<Node> cfg = createSimpleGraph();
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, null, null);

    // Check that 'B' has no uses, because it is assigned after 'A'
    Collection<Node> uses = analysis.getUses("B", cfg.getNode(cfg.getEntryNode()));
    assertTrue(uses.isEmpty());
  }

  @org.junit.Test
  public void testEscapedVariable() {
    ControlFlowGraph<Node> cfg = createSimpleGraphWithEscapedVariable();
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, null, null);

    // Check that 'A' may be used at print statement, because it is not escaped
    Collection<Node> uses = analysis.getUses("A", cfg.getNode(cfg.getEntryNode()));
    assertEquals(1, uses.size());
    assertEquals(cfg.getNode(cfg.getEntryNode()).getChild(2), uses.iterator().next());

    // Check that 'C' has no uses, because it is an escaped variable
    Collection<Node> escapedUses = analysis.getUses("C", cfg.getNode(cfg.getEntryNode()));
    assertTrue(escapedUses.isEmpty());
  }

  @org.junit.Test
  public void testConditionalBlock() {
    ControlFlowGraph<Node> cfg = createConditionalBlockGraph();
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, null, null);

    // Check that 'A' may be used at print statement when condition is true
    Collection<Node> usesTrue = analysis.getUses("A", cfg.getNode(cfg.getNode(cfg.getEntryNode()).getChild(0)));
    assertEquals(1, usesTrue.size());
    assertEquals(cfg.getNode(cfg.getNode(cfg.getEntryNode()).getChild(2)), usesTrue.iterator().next());

    // Check that 'A' may be used at print statement when condition is false
    Collection<Node> usesFalse = analysis.getUses("A", cfg.getNode(cfg.getNode(cfg.getEntryNode()).getChild(1)));
    assertEquals(1, usesFalse.size());
    assertEquals(cfg.getNode(cfg.getNode(cfg.getEntryNode()).getChild(3)), usesFalse.iterator().next());
  }

  private ControlFlowGraph<Node> createSimpleGraphWithEscapedVariable() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope scope = new Scope();

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

    Node A = Node.newName("A");
    Node C = Node.newName("C");

    Node assignA = Node.newAssign(A, Node.newName("A"));
    Node assignC = Node.newAssign(C, Node.newName("A"));

    Node ifStatement = Node.newIf(Node.newBooleanLiteral(true), assignA, assignC);
    Node printStatement = Node.newPrint(Node.newName("A"));

    List<Node> entryBlock = Node.newList(assignA, printStatement);
    List<Node> exitBlock = Node.newList();

    Node startNode = cfg.addBlock(entryBlock, exitBlock, null, null);
    cfg.addEdge(assignA, startNode, null);
    cfg.addEdge(printStatement, startNode, null);

    List<Node> escapedBlockEntry = Node.newList(assignC);
    List<Node> escapedBlockExit = Node.newList();

    Node escapedNode = cfg.addBlock(escapedBlockEntry, escapedBlockExit, null, null);
    cfg.addEdge(assignC, escapedNode, null);

    cfg.addEdge(ifStatement, escapedNode, null);

    return cfg;
  }

  private ControlFlowGraph<Node> createConditionalBlockGraph() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope scope = new Scope();

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

    Node A = Node.newName("A");

    Node ifNode = Node.newIf(Node.newBooleanLiteral(true), null, null);

    Node trueBlockEntry = Node.newList(Node.newAssign(A, Node.newIntLiteral(1)));
    Node trueBlockExit = Node.newList();

    Node trueBlock = cfg.addBlock(trueBlockEntry, trueBlockExit, null, null);
    cfg.addEdge(ifNode, trueBlock, null);

    Node printTrue = Node.newPrint(A);
    Node trueBlockNode = cfg.addBlock(Node.newList(printTrue), null, null, trueBlock);
    cfg.addEdge(printTrue, trueBlockNode, null);

    Node falseBlockEntry = Node.newList(Node.newAssign(A, Node.newIntLiteral(2)));
    Node falseBlockExit = Node.newList();

    Node falseBlock = cfg.addBlock(falseBlockEntry, falseBlockExit, null, null);
    cfg.addEdge(ifNode, falseBlock, null);

    Node printFalse = Node.newPrint(A);
    Node falseBlockNode = cfg.addBlock(Node.newList(printFalse), null, null, falseBlock);
    cfg.addEdge(printFalse, falseBlockNode, null);

    List<Node> entryBlock = Node.newList(ifNode);
    List<Node> exitBlock = Node.newList();

    Node startNode = cfg.addBlock(entryBlock, exitBlock, null, null);
    cfg.addEdge(ifNode, startNode, null);

    return cfg;
  }
}