import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.BitSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class LiveVariablesAnalysisTest {
  @Test
  public void testLiveVariablesAnalysis() {
    // Create a sample scope for testing
    String functionName = "testFunction";
    Scope scope = new Scope(functionName);
    scope.declareVar("x", Node.INT_TYPE);
    scope.declareVar("y", Node.INT_TYPE);
    scope.declareVar("z", Node.INT_TYPE);

    // Create a sample control flow graph for testing
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(functionName);
    Node entryNode = cfg.getEntryNode();
    Node assignXNode = new Node(Token.ASSIGN);
    assignXNode.setNext(entryNode.getNext());
    assignXNode.setChild(new Node(Token.NAME, "x"));
    assignXNode.setChild(new Node(Token.INT_LITERAL, 10));
    entryNode.setNext(assignXNode);

    Node assignYNode = new Node(Token.ASSIGN);
    assignYNode.setNext(assignXNode);
    assignYNode.setChild(new Node(Token.NAME, "y"));
    assignYNode.setChild(new Node(Token.INT_LITERAL, 20));

    Node ifNode = new Node(Token.IF);
    ifNode.setNext(assignYNode);
    ifNode.setChild(new Node(Token.NAME, "x"));
    ifNode.setChild(new Node.BooleanLiteral(true));
    ifNode.setChild(new Node(Token.ASSIGN));
    ifNode.setChild(new Node(Token.NAME, "z"));
    ifNode.setChild(new Node(Token.INT_LITERAL, 30));
    assignYNode.setNext(ifNode);

    Node returnNode = new Node(Token.RETURN);
    returnNode.setNext(ifNode);
    entryNode.setNext(returnNode);

    // Create a LiveVariablesAnalysis instance for testing
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);

    // Test the getEscapedLocals method
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    assertNotNull(escapedLocals);
    assertTrue(escapedLocals.isEmpty());

    // Create a scope with escaped locals for testing
    Scope escapedScope = new Scope(functionName);
    escapedScope.declareVar("x", Node.INT_TYPE);
    escapedScope.declareVar("y", Node.INT_TYPE);
    escapedScope.declareVar("z", Node.INT_TYPE);
    escapedScope.declareVar("arguments", Node.OBJECT_TYPE);

    LiveVariablesAnalysis escapedAnalysis = new LiveVariablesAnalysis(cfg, escapedScope, null);
    escapedLocals = escapedAnalysis.getEscapedLocals();
    assertNotNull(escapedLocals);
    assertTrue(escapedLocals.size() > 0);
    assertTrue(escapedLocals.contains(escapedScope.getVar("arguments")));

    // Test the isLive method
    Var x = scope.getVar("x");
    assertTrue(analysis.isLive(x));

    Var y = scope.getVar("y");
    assertTrue(analysis.isLive(y));

    Var z = scope.getVar("z");
    assertFalse(analysis.isLive(z));

    // Test the getVarIndex method
    int xIndex = analysis.getVarIndex("x");
    assertEquals(xIndex, x.index);

    int yIndex = analysis.getVarIndex("y");
    assertEquals(yIndex, y.index);

    int zIndex = analysis.getVarIndex("z");
    assertEquals(zIndex, z.index);

    // Test the flowThrough method with a basic use case
    LiveVariableLattice entryLattice = analysis.createEntryLattice();
    LiveVariableLattice resultLattice = analysis.flowThrough(entryNode, entryLattice);

    BitSet xLive = resultLattice.liveSet;
    assertTrue(xLive.get(x.index));

    BitSet yLive = resultLattice.liveSet;
    assertTrue(yLive.get(y.index));

    BitSet zLive = resultLattice.liveSet;
    assertFalse(zLive.get(z.index));

    // Test the flowThrough method with a conditional use case
    LiveVariableLattice conditionalEntryLattice = analysis.createEntryLattice();
    LiveVariableLattice conditionalResultLattice = analysis.flowThrough(ifNode, conditionalEntryLattice);

    xLive = conditionalResultLattice.liveSet;
    assertTrue(xLive.get(x.index));

    yLive = conditionalResultLattice.liveSet;
    assertTrue(yLive.get(y.index));

    zLive = conditionalResultLattice.liveSet;
    assertFalse(zLive.get(z.index));

    // Test the flowThrough method with an exception handling use case
    LiveVariableLattice exceptionEntryLattice = analysis.createEntryLattice();
    Node throwNode = new Node(Token.THROW);
    throwNode.setNext(ifNode);
    LiveVariableLattice exceptionResultLattice = analysis.flowThrough(throwNode, exceptionEntryLattice);

    xLive = exceptionResultLattice.liveSet;
    assertTrue(xLive.get(x.index));

    yLive = exceptionResultLattice.liveSet;
    assertTrue(yLive.get(y.index));

    zLive = exceptionResultLattice.liveSet;
    assertNull(zLive);
  }
}