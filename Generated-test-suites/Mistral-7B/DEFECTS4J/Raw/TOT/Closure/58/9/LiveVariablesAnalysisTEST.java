import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.LiveVariablesAnalysis;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LiveVariablesAnalysisTest {

 private ControlFlowGraph<Node> testGraph;
 private Scope testScope;
 private LiveVariablesAnalysis analysis;

 @Before
 public void setUp() {
 // Create a simple control flow graph for testing.
 Node rootNode = new Node(Token.SCRIPT, 0, 0);
 Node funcNode = new Node(Token.FUNCTION, 0, 0);
 rootNode.addChild(funcNode);

 Node paramNode1 = new Node(Token.NAME, 0, 1);
 paramNode1.addString("a");
 Node paramNode2 = new Node(Token.NAME, 0, 2);
 paramNode2.addString("b");
 funcNode.addChild(paramNode1);
 funcNode.addChild(paramNode2);

 Node assignNode1 = new Node(Token.ASSIGN, 0, 3);
 assignNode1.addChild(paramNode1);
 assignNode1.addChild(new Node(Token.INTEGER, 0, 4, 1));
 Node assignNode2 = new Node(Token.ASSIGN, 0, 5);
 assignNode2.addChild(paramNode2);
 assignNode2.addChild(new Node(Token.INTEGER, 0, 6, 2));
 funcNode.addChild(assignNode1);
 funcNode.addChild(assignNode2);

 Node returnNode = new Node(Token.RETURN, 0, 7);
 funcNode.addChild(returnNode);

 Node conditionalNode = new Node(Token.IF, 0, 8);
 Node condition = new Node(Token.NAME, 0, 9);
 condition.addString("true");
 conditionalNode.addChild(condition);
 rootNode.addChild(conditionalNode);
 conditionalNode.addChild(funcNode);

 Node endNode = new Node(Token.SCRIPT, 0, 0);
 rootNode.addChild(endNode);

 testGraph = new ControlFlowGraph<>(rootNode);
 testScope = testGraph.getScope();
 analysis = new LiveVariablesAnalysis(testGraph, testScope, null);
 }

 @Test
 public void testLiveVariablesAnalysis_SimpleLocalVariables() {
 LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.getInitialLattice();
 // Check initial state, all variables should be live.
 assertTrue(lattice.isLive(analysis.getVarIndex("a")));
 assertTrue(lattice.isLive(analysis.getVarIndex("b")));

 // Perform analysis.
 analysis.computeLattice();

 // Check final state, both variables should still be live.
 assertTrue(analysis.getLattice().isLive(analysis.getVarIndex("a")));
 assertTrue(analysis.getLattice().isLive(analysis.getVarIndex("b")));
 }

 @Test
 public void testLiveVariablesAnalysis_DeadVariable() {
 // Add a dead variable to the scope.
 testScope.declareVar("c", NodeType.VAR, null, false);

 LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.getInitialLattice();
 // Check initial state, 'c' should be live.
 assertTrue(lattice.isLive(analysis.getVarIndex("c")));

 // Perform analysis.
 analysis.computeLattice();

 // Check final state, 'c' should not be live anymore.
 assertFalse(analysis.getLattice().isLive(analysis.getVarIndex("c")));
 }

 @Test
 public void testLiveVariablesAnalysis_EscapedVariable() {
 // Add an escaped variable to the scope.
 Node argumentNode = testGraph.getRootNode().getFirstChild().getNext();
 String argumentName = argumentNode.getString();
 testScope.declareVar(argumentName, NodeType.VAR, null, true);
 LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.getInitialLattice();
 // Check initial state, the argument should be live.
 assertTrue(lattice.isLive(analysis.getVarIndex(argumentName)));

 // Perform analysis.
 analysis.computeLattice();

 // Check final state, the argument should still be live.
 assertTrue(analysis.getLattice().isLive(analysis.getVarIndex(argumentName)));
 }
}