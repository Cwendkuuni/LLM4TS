import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.NodeUtil.isAssign;
import static com.google.javascript.jscomp.NodeUtil.isName;

import com.google.common.collect.Sets;

import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.rhino.Node;

import java.util.BitSet;
import java.util.List;
import java.util.Set;

public class LiveVariablesAnalysisTest {

  @Test
  public void testLiveVariablesAnalysis() {
    // Test creating a new instance of LiveVariablesAnalysis
    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(new Node(Token.SCRIPT, 0));
    AbstractCompiler compiler = new AbstractCompiler();
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    // Test getEscapedLocals method
    Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
    assertTrue(escapedLocals.isEmpty());

    // Test creating a simple Node for testing
    Node testNode = new Node(Token.VAR, 1);
    Var testVar = jsScope.addVar("testVar");
    testNode.setFirstChild(testVar.nameNode);

    // Test isForward method
    assertFalse(liveVariablesAnalysis.isForward());

    // Test createEntryLattice method
    LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();
    assertTrue(entryLattice.isLive(testVar));

    // Test createInitialEstimateLattice method
    LiveVariableLattice initialEstimateLattice = liveVariablesAnalysis.createInitialEstimateLattice();
    assertTrue(initialEstimateLattice.isLive(testVar));

    // Test flowThrough method for a simple Node (testNode)
    LiveVariableLattice inputLattice = new LiveVariableLattice(jsScope.getVarCount());
    inputLattice.liveSet.set(testVar.index);
    LiveVariableLattice outputLattice = liveVariablesAnalysis.flowThrough(testNode, inputLattice);
    assertTrue(outputLattice.isLive(testVar));

    // Test edge cases and exception handling scenarios

    // Test flowThrough method for a Node with assignments and conditionals
    Node conditionalNode = new Node(Token.IF, 2);
    Node assignmentNode = new Node(Token.ASSIGN, 3);
    Node assignedNode = new Node(Token.NAME, 4);
    assignedNode.setString("testVar");
    assignmentNode.setFirstChild(assignedNode);
    conditionalNode.setFirstChild(assignmentNode);
    conditionalNode.setLastChild(new Node(Token.NAME, 5).setString("testVar"));

    LiveVariableLattice conditionalInputLattice = new LiveVariableLattice(jsScope.getVarCount());
    conditionalInputLattice.liveSet.set(testVar.index);
    LiveVariableLattice conditionalOutputLattice = liveVariablesAnalysis.flowThrough(conditionalNode, conditionalInputLattice);
    assertTrue(conditionalOutputLattice.isLive(testVar));

    // Test flowThrough method for a Node with a loop
    Node whileNode = new Node(Token.WHILE, 6);
    Node whileCondition = new Node(Token.NAME, 7).setString("testCondition");
    whileNode.setFirstChild(whileCondition);
    Node loopBody = new Node(Token.SCRIPT, 8);
    whileNode.setLastChild(loopBody);

    LiveVariableLattice whileInputLattice = new LiveVariableLattice(jsScope.getVarCount());
    whileInputLattice.liveSet.set(testVar.index);
    LiveVariableLattice whileOutputLattice = liveVariablesAnalysis.flowThrough(whileNode, whileInputLattice);
    assertTrue(whileOutputLattice.isLive(testVar));

    // Test flowThrough method for a Node with a function call
    Node functionCallNode = new Node(Token.CALL, 9);
    functionCallNode.setFirstChild(new Node(Token.NAME, 10).setString("testFunction"));
    LiveVariableLattice functionCallInputLattice = new LiveVariableLattice(jsScope.getVarCount());
    functionCallInputLattice.liveSet.set(testVar.index);
    LiveVariableLattice functionCallOutputLattice = liveVariablesAnalysis.flowThrough(functionCallNode, functionCallInputLattice);
    assertTrue(functionCallOutputLattice.isLive(testVar));

    // Test flowThrough method for a Node with an array assignment
    Node arrayNode = new Node(Token.NAME, 11).setString("[0]");
    Node arrayAssignmentNode = new Node(Token.ASSIGN, 12);
    Node arrayAssignedNode = new Node(Token.NUMBER, 13).setInt(0);
    Node arrayValueNode = new Node(Token.NAME, 14).setString("testVar");
    arrayAssignmentNode.setFirstChild(arrayNode);
    arrayAssignmentNode.setLastChild(arrayValueNode);
    LiveVariableLattice arrayInputLattice = new LiveVariableLattice(jsScope.getVarCount());
    arrayInputLattice.liveSet.set(testVar.index);
    LiveVariableLattice arrayOutputLattice = liveVariablesAnalysis.flowThrough(arrayAssignmentNode, arrayInputLattice);
    assertTrue(arrayOutputLattice.isLive(testVar));

    // Test flowThrough method for a Node with a try-catch block
    Node tryNode = new Node(Token.TRY, 15);
    Node catchNode = new Node(Token.CATCH, 16);
    Node tryBodyNode = new Node(Token.SCRIPT, 17);
    Node catchBodyNode = new Node(Token.SCRIPT, 18);
    tryNode.setFirstChild(tryBodyNode);
    tryNode.setLastChild(catchNode);
    catchNode.setFirstChild(new Node(Token.NAME, 19).setString("catchBlock"));
    LiveVariableLattice tryInputLattice = new LiveVariableLattice(jsScope.getVarCount());
    tryInputLattice.liveSet.set(testVar.index);
    LiveVariableLattice tryOutputLattice = liveVariablesAnalysis.flowThrough(tryNode, tryInputLattice);
    assertTrue(tryOutputLattice.isLive(testVar));

    // Test flowThrough method for a Node with a switch statement
    Node switchNode = new Node(Token.SWITCH, 20);
    Node switchExpressionNode = new Node(Token.NAME, 21).setString("switchExpression");
    switchNode.setFirstChild(switchExpressionNode);
    Node caseNode1 = new Node(Token.CASE, 22).setInteger(1);
    Node caseBody1Node = new Node(Token.SCRIPT, 23);
    caseNode1.setFirstChild(caseBody1Node);
    Node caseNode2 = new Node(Token.CASE, 24).setInteger(2);
    Node caseBody2Node = new Node(Token.SCRIPT, 25);
    caseNode2.setFirstChild(caseBody2Node);
    switchNode.setLastChild(caseNode2);

    LiveVariableLattice switchInputLattice = new LiveVariableLattice(jsScope.getVarCount());
    switchInputLattice.liveSet.set(testVar.index);
    LiveVariableLattice switchOutputLattice = liveVariablesAnalysis.flowThrough(switchNode, switchInputLattice);
    assertTrue(switchOutputLattice.isLive(testVar));

    // Test flowThrough method for a Node with a for loop
    Node forNode = new Node(Token.FOR, 26);
    Node forConditionNode = new Node(Token.NAME, 27).setString("forCondition");
    Node forInitializationNode = new Node(Token.SCRIPT, 28);
    Node forAssignmentNode = new Node(Token.ASSIGN, 29);
    Node forAssignedNode = new Node(Token.NAME, 30).setString("forVariable");
    Node forUpdateNode = new Node(Token.SCRIPT, 31);
    forNode.setFirstChild(forConditionNode);
    forNode.setLastChild(forUpdateNode);
    forConditionNode.setNext(forInitializationNode);
    forInitializationNode.setNext(forAssignmentNode);
    forAssignmentNode.setFirstChild(forAssignedNode);
    LiveVariableLattice forInputLattice = new LiveVariableLattice(jsScope.getVarCount());
    forInputLattice.liveSet.set(testVar.index);
    LiveVariableLattice forOutputLattice = liveVariablesAnalysis.flowThrough(forNode, forInputLattice);
    assertTrue(forOutputLattice.isLive(testVar));

    // Test flowThrough method for a Node with a return statement
    Node returnNode = new Node(Token.RETURN, 32);
    Node returnValueNode = new Node(Token.NAME, 33).setString("returnValue");
    returnNode.setFirstChild(returnValueNode);
    LiveVariableLattice returnInputLattice = new LiveVariableLattice(jsScope.getVarCount());
    returnInputLattice.liveSet.set(testVar.index);
    LiveVariableLattice returnOutputLattice = liveVariablesAnalysis.flowThrough(returnNode, returnInputLattice);
    assertTrue(returnOutputLattice.isLive(testVar));

    // Test flowThrough method for a Node with a throw statement
    Node throwNode = new Node(Token.THROW, 34);
    LiveVariableLattice throwInputLattice = new LiveVariableLattice(jsScope.getVarCount());
    throwInputLattice.liveSet.set(testVar.index);
    LiveVariableLattice throwOutputLattice = liveVariablesAnalysis.flowThrough(throwNode, throwInputLattice);
    assertTrue(throwOutputLattice.isLive(testVar));

    // Test flowThrough method for a Node with a continue statement
    Node continueNode = new Node(Token.CONTINUE, 35);
    LiveVariableLattice continueInputLattice = new LiveVariableLattice(jsScope.getVarCount());
    continueInputLattice.liveSet.set(testVar.index);
    LiveVariableLattice continueOutputLattice = liveVariablesAnalysis.flowThrough(continueNode, continueInputLattice);
    assertTrue(continueOutputLattice.isLive(testVar));

    // Test flowThrough method for a Node with a break statement
    Node breakNode = new Node(Token.BREAK, 36);
    LiveVariableLattice breakInputLattice = new LiveVariableLattice(jsScope.getVarCount());
    breakInputLattice.liveSet.set(testVar.index);
    LiveVariableLattice breakOutputLattice = liveVariablesAnalysis.flowThrough(breakNode, breakInputLattice);
    assertTrue(breakOutputLattice.isLive(testVar));

    // Test flowThrough method for a Node with a with statement
    Node withNode = new Node(Token.WITH, 37);
    Node withObjectNode = new Node(Token.NAME, 38).setString("withObject");
    withNode.setFirstChild(withObjectNode);
    Node withBodyNode = new Node(Token.SCRIPT, 39);
    withNode.setLastChild(withBodyNode);

    LiveVariableLattice withInputLattice = new LiveVariableLattice(jsScope.getVarCount());
    withInputLattice.liveSet.set(testVar.index);
    LiveVariableLattice withOutputLattice = liveVariablesAnalysis.flowThrough(withNode, withInputLattice);
    assertTrue(withOutputLattice.isLive(testVar));

    // Test flowThrough method for a Node with a delete operator
    Node deleteNode = new Node(Token.DELETE, 40);
    Node deleteTargetNode = new Node(Token.NAME, 41).setString("deleteTarget");
    deleteNode.setFirstChild(deleteTargetNode);
    LiveVariableLattice deleteInputLattice = new LiveVariableLattice(jsScope.getVarCount());
    deleteInputLattice.liveSet.set(testVar.index);
    LiveVariableLattice deleteOutputLattice = liveVariablesAnalysis.flowThrough(deleteNode, deleteInputLattice);
    assertTrue(deleteOutputLattice.isLive(testVar));
  }
}