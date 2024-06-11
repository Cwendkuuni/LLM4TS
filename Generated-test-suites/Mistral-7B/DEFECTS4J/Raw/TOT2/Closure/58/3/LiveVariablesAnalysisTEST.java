// LiveVariablesAnalysisTest.java - Test for basic functionality

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class LiveVariablesAnalysisTest {

  @Test
  public void testLiveVariablesAnalysis() {
    // Given: A simple program with no dead variables
    Node rootNode = Node.newProgram();
    Scope jsScope = new Scope(rootNode);
    jsScope.declareVar("x", 0);
    jsScope.declareVar("y", 0);
    Node assignment1 = Node.newAssignment(Token.ASSIGN, jsScope.getVar("x"), Node.newNumber(1));
    Node assignment2 = Node.newAssignment(Token.ASSIGN, jsScope.getVar("y"), Node.newNumber(2));
    rootNode.addChild(assignment1);
    rootNode.addChild(assignment2);
    rootNode.addChild(Node.newReturn(Token.RETURN, jsScope.getVar("x")));

    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(
        new ControlFlowGraph<>(rootNode), jsScope, null);
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.process();

    // When: Checking if the correct variables are considered live
    assertTrue(result.isLive(analysis.getVarIndex("x")));
    assertTrue(result.isLive(analysis.getVarIndex("y")));

    // Then: No dead variables should be detected
    assertFalse(result.isLive(analysis.getVarIndex("deadVariable"))); // Hypothetical dead variable
  }
}