import static org.junit.Assert.*;
import static com.google.javascript.jscomp.graph.NodeUtil.*;

import com.google.common.collect.Multimaps;
import com.google.javascript.jscomp.graph.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MaybeReachingVariableUseTest {

  private static ControlFlowGraph<Node> createSimpleGraph() {
    // Simple control flow graph for testing purposes
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new CompilerOptions();
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    Node block = newNode(Token.BLOCK);
    Node assignment = newNode(Token.ASSIGN, newNode(Token.NAME, "x"), newNode(Token.INT_LITERAL, 1));
    Node print = newNode(Token.PRINT, newNode(Token.NAME, "x"));

    cfg.addEdge(new DiGraphEdge<Node>(block, assignment));
    cfg.addEdge(new DiGraphEdge<Node>(assignment, print));

    return cfg;
  }

  @org.junit.Test
  public void testSimpleReachingUses() {
    ControlFlowGraph<Node> cfg = createSimpleGraph();
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, null, null);
    Node entryNode = cfg.getEntryNode();

    ReachingUses initialEstimate = analysis.createInitialEstimateLattice();
    ReachingUses expectedResult = new ReachingUses();
    expectedResult.mayUseMap.put(jsScope.getVar("x"), entryNode);

    // Run the analysis
    Collection<Node> uses = analysis.compute(entryNode, initialEstimate);

    // Check the result of the analysis
    assertEquals(expectedResult, analysis.getData(uses.iterator().next()));
  }

  @org.junit.Test
  public void testConditionalReachingUses() {
    ControlFlowGraph<Node> cfg = createSimpleGraph();
    Node ifNode = newNode(Token.IF, newNode(Token.NAME, "x"), newNode(Token.BLOCK), newNode(Token.NAME, "x"));
    Node assignment1 = newNode(Token.ASSIGN, newNode(Token.NAME, "x"), newNode(Token.INT_LITERAL, 1));
    Node assignment2 = newNode(Token.ASSIGN, newNode(Token.NAME, "x"), newNode(Token.INT_LITERAL, 2));
    Node print1 = newNode(Token.PRINT, newNode(Token.NAME, "x"));
    Node print2 = newNode(Token.PRINT, newNode(Token.NAME, "x"));

    cfg.addEdge(new DiGraphEdge<Node>(entryNode, ifNode));
    cfg.addEdge(new DiGraphEdge<Node>(ifNode, assignment1));
    cfg.addEdge(new DiGraphEdge<Node>(ifNode, print1));
    cfg.addEdge(new DiGraphEdge<Node>(newNode(Token.ELSE), assignment2));
    cfg.addEdge(new DiGraphEdge<Node>(newNode(Token.ELSE), print2));

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, null, null);
    Node entryNode = cfg.getEntryNode();

    ReachingUses initialEstimate = analysis.createInitialEstimateLattice();
    ReachingUses expectedResult1 = new ReachingUses();
    expectedResult1.mayUseMap.put(jsScope.getVar("x"), assignment1);
    ReachingUses expectedResult2 = new ReachingUses();
    expectedResult2.mayUseMap.put(jsScope.getVar("x"), assignment2);

    // Run the analysis for the first condition being true
    Collection<Node> uses1 = analysis.compute(entryNode, initialEstimate);
    assertEquals(expectedResult1, analysis.getData(uses1.iterator().next()));

    // Run the analysis for the first condition being false
    ReachingUses updatedEstimate = analysis.createInitialEstimateLattice();
    updatedEstimate.mayUseMap.remove(jsScope.getVar("x"));
    Collection<Node> uses2 = analysis.compute(entryNode, updatedEstimate);
    assertEquals(expectedResult2, analysis.getData(uses2.iterator().next()));

    // Check the result of the analysis for the second condition being true
    ReachingUses updatedEstimate2 = new ReachingUses();
    updatedEstimate2.mayUseMap.put(jsScope.getVar("x"), assignment2);
    Collection<Node> uses3 = analysis.compute(entryNode, updatedEstimate2);
    assertEquals(expectedResult2, analysis.getData(uses3.iterator().next()));
  }

  @org.junit.Test
  public void testCatchBlockReachingUses() {
    ControlFlowGraph<Node> cfg = createSimpleGraph();

    Node ifNode = newNode(Token.IF, newNode(Token.NAME, "x"), newNode(Token.BLOCK), newNode(Token.NAME, "x"));
    Node assignment = newNode(Token.ASSIGN, newNode(Token.NAME, "x"), newNode(Token.INT_LITERAL, 1));
    Node throwNode = newNode(Token.THROW, newNode(Token.NAME, "e"), newNode(Token.INT_LITERAL, 1));
    Node catchNode = newNode(Token.TRY_CATCH, ifNode, new Node(Token.NAME, "e"), new Node(Token.BLOCK), newNode(Token.NAME, "x"));
    Node print = newNode(Token.PRINT, newNode(Token.NAME, "x"));

    cfg.addEdge(new DiGraphEdge<Node>(entryNode, ifNode));
    cfg.addEdge(new DiGraphEdge<Node>(ifNode, assignment));
    cfg.addEdge(new DiGraphEdge<Node>(ifNode, throwNode));
    cfg.addEdge(new DiGraphEdge<Node>(catchNode, print));

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, null, null);
    Node entryNode = cfg.getEntryNode();

    ReachingUses initialEstimate = analysis.createInitialEstimateLattice();
    ReachingUses expectedResult = new ReachingUses();
    expectedResult.mayUseMap.put(jsScope.getVar("x"), assignment);

    // Run the analysis
    Collection<Node> uses = analysis.compute(entryNode, initialEstimate);
    assertEquals(expectedResult, analysis.getData(uses.iterator().next()));
  }

  @org.junit.Test
  public void testForwardFlow() {
    ControlFlowGraph<Node> cfg = createSimpleGraph();

    Node assignment1 = newNode(Token.ASSIGN, newNode(Token.NAME, "x"), newNode(Token.INT_LITERAL, 1));
    Node assignment2 = newNode(Token.ASSIGN, newNode(Token.NAME, "x"), newNode(Token.INT_LITERAL, 2));
    Node print1 = newNode(Token.PRINT, newNode(Token.NAME, "x"));
    Node print2 = newNode(Token.PRINT, newNode(Token.NAME, "x"));

    cfg.addEdge(new DiGraphEdge<Node>(new Node(Token.BLOCK), assignment1));
    cfg.addEdge(new DiGraphEdge<Node>(assignment1, print1));
    cfg.addEdge(new DiGraphEdge<Node>(new Node(Token.BLOCK), assignment2));
    cfg.addEdge(new DiGraphEdge<Node>(assignment2, print2));

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, null, null);
    Node entryNode = cfg.getEntryNode();

    ReachingUses initialEstimate = analysis.createInitialEstimateLattice();
    ReachingUses expectedResult1 = new ReachingUses();
    expectedResult1.mayUseMap.put(jsScope.getVar("x"), assignment1);
    ReachingUsuses expectedResult2 = new ReachingUses();
    expectedResult2.mayUseMap.put(jsScope.getVar("x"), assignment2);

    // Run the analysis with a forward flow
    analysis.setForward(true);
    Collection<Node> uses1 = analysis.compute(entryNode, initialEstimate);
    assertEquals(expectedResult1, analysis.getData(uses1.iterator().next()));

    // Reset to false to check backward flow
    analysis.setForward(false);
    ReachingUses updatedEstimate = new ReachingUses();
    updatedEstimate.mayUseMap.put(jsScope.getVar("x"), assignment2);
    Collection<Node> uses2 = analysis.compute(entryNode, updatedEstimate);
    assertEquals(expectedResult2, analysis.getData(uses2.iterator().next()));
  }

  @org.junit.Test
  public void testNestedBlockReachingUses() {
    ControlFlowGraph<Node> cfg = createSimpleGraph();

    Node block1 = newNode(Token.BLOCK);
    Node assignment1 = newNode(Token.ASSIGN, newNode(Token.NAME, "x"), newNode(Token.INT_LITERAL, 1));
    Node block2 = newNode(Token.BLOCK);
    Node assignment2 = newNode(Token.ASSIGN, newNode(Token.NAME, "x"), newNode(Token.INT_LITERAL, 2));
    Node print1 = newNode(Token.PRINT, newNode(Token.NAME, "x"));
    Node print2 = newNode(Token.PRINT, newNode(Token.NAME, "x"));

    cfg.addEdge(new DiGraphEdge<Node>(block1, assignment1));
    cfg.addEdge(new DiGraphEdge<Node>(assignment1, print1));
    cfg.addEdge(new DiGraphEdge<Node>(new Node(Token.BLOCK), assignment2));
    cfg.addEdge(new DiGraphEdge<Node>(assignment2, print2));
    cfg.addEdge(new DiGraphEdge<Node>(block1, block2));

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, null, null);
    Node entryNode = cfg.getEntryNode();

    ReachingUses initialEstimate = analysis.createInitialEstimateLattice();
    ReachingUses expectedResult1 = new ReachingUses();
    expectedResult1.mayUseMap.put(jsScope.getVar("x"), assignment1);
    ReachingUses expectedResult2 = new ReachingUses();
    expectedResult2.mayUseMap.put(jsScope.getVar("x"), assignment2);

    // Run the analysis
    Collection<Node> uses1 = analysis.compute(entryNode, initialEstimate);
    assertEquals(expectedResult1, analysis.getData(uses1.iterator().next()));

    // Run the analysis again with the updated estimate
    ReachingUses updatedEstimate = new ReachingUses();
    updatedEstimate.mayUseMap.put(jsScope.getVar("x"), assignment2);
    Collection<Node> uses2 = analysis.compute(entryNode, updatedEstimate);
    assertEquals(expectedResult2, analysis.getData(uses2.iterator().next()));
  }

  @org.junit.Test
  public void testLoopReachingUses() {
    ControlFlowGraph<Node> cfg = createSimpleGraph();

    Node forNode = newNode(Token.FOR, new Node(Token.NAME, "x"), new Node(Token.NAME, "y"), new Node(Token.BLOCK), new Node(Token.NAME, "x"));
    Node assignment = newNode(Token.ASSIGN, newNode(Token.NAME, "x"), newNode(Token.INT_LITERAL, 1));
    Node print = newNode(Token.PRINT, newNode(Token.NAME, "x"));

    cfg.addEdge(new DiGraphEdge<Node>(new Node(Token.BLOCK), forNode));
    cfg.addEdge(new DiGraphEdge<Node>(forNode, assignment));
    cfg.addEdge(new DiGraphEdge<Node>(forNode, print));
    cfg.addEdge(new DiGraphEdge<Node>(new Node(Token.NAME, "x"), forNode));

    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, null, null);
    Node entryNode = cfg.getEntryNode();

    ReachingUses initialEstimate = analysis.createInitialEstimateLattice();
    ReachingUses expectedResult = new ReachingUses();
    expectedResult.mayUseMap.put(jsScope.getVar("x"), assignment);

    // Run the analysis
    Collection<Node> uses = analysis.compute(entryNode, initialEstimate);
    assertEquals(expectedResult, analysis.getData(uses.iterator().next()));
  }
}