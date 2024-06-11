import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.LiveVariablesAnalysis;

public class LiveVariablesAnalysisTest {
  @Test
  public void testLiveVariablesAnalysis() {
    // Given
    Scope scope = new Scope();
    ControlFlowGraph cfg = new ControlFlowGraph(new Node("root"), null);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, null);

    // When
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.computeLattice();

    // Then
    // Basic functionality test - A local variable is live in its defining block
    Var localVariable = scope.addVar("localVariable");
    Node variableAssignment = new Node(Token.ASSIGN, null, null, null, null, null, null, localVariable.index);
    Node block = new Node(Token.BLOCK, null, new Node[]{variableAssignment}, null, null, null, null, null);
    analysis.flowThrough(block, analysis.createEntryLattice());
    assertTrue(analysis.isLive(localVariable.index));

    // Edge cases and exception handling
    // A local variable is killed in its defining block if it is assigned and then immediately killed
    localVariable = scope.addVar("localVariable");
    variableAssignment = new Node(Token.ASSIGN, null, null, null, null, null, null, localVariable.index);
    Node killStatement = new Node(Token.RETURN, null, null, null, null, null, null, null);
    Node blockWithKill = new Node(Token.BLOCK, null, new Node[]{variableAssignment, killStatement}, null, null, null, null, null);
    analysis.flowThrough(blockWithKill, analysis.createEntryLattice());
    assertFalse(analysis.isLive(localVariable.index));

    // A local variable is live if it is a loop variable
    Var loopVariable = scope.addVar("loopVariable");
    Node forStatement = new Node(Token.FOR, null, null, null, null, null, null, null);
    Node initialization = new Node(Token.ASSIGN, null, null, null, null, null, null, loopVariable.index);
    Node condition = new Node(Token.TRUE, null, null, null, null, null, null, null);
    Node increment = new Node(Token.ASSIGN, null, null, null, null, null, null, loopVariable.index);
    Node loopBody = new Node(Token.BLOCK, null, null, null, null, null, null, null);
    Node loop = new Node(Token.BLOCK, null, new Node[]{forStatement}, new Node[]{loopBody}, null, null, null, null);
    analysis.flowThrough(loop, analysis.createEntryLattice());
    assertTrue(analysis.isLive(loopVariable.index));

    // A local variable is live if it is a function parameter
    Var functionParameter = scope.addVar("functionParameter");
    Node functionDeclaration = new Node(Token.FUNCTION, null, null, null, null, null, null, null);
    Node parameterDeclaration = new Node(Token.VAR, null, null, null, null, null, null, functionParameter.index);
    Node functionBody = new Node(Token.BLOCK, null, null, null, null, null, null, null);
    Node function = new Node(Token.BLOCK, null, new Node[]{functionDeclaration}, new Node[]{functionBody}, null, null, null, null);
    analysis.flowThrough(function, analysis.createEntryLattice());
    assertTrue(analysis.isLive(functionParameter.index));

    // A local variable is live if it is a global variable
    String globalVariableName = "globalVariable";
    Var globalVariable = scope.getVar(globalVariableName);
    Node globalVariableAssignment = new Node(Token.ASSIGN, null, null, null, null, null, null, globalVariable.index);
    Node globalVariableBlock = new Node(Token.BLOCK, null, new Node[]{globalVariableAssignment}, null, null, null, null, null);
    analysis.flowThrough(globalVariableBlock, analysis.createEntryLattice());
    assertTrue(analysis.isLive(globalVariable.index));

    // A local variable is dead if it is never assigned or read
    Var deadVariable = scope.addVar("deadVariable");
    Node deadVariableBlock = new Node(Token.BLOCK, null, null, null, null, null, null, null);
    analysis.flowThrough(deadVariableBlock, analysis.createEntryLattice());
    assertFalse(analysis.isLive(deadVariable.index));

    // A local variable is dead if it is assigned and then immediately killed
    localVariable = scope.addVar("localVariable");
    variableAssignment = new Node(Token.ASSIGN, null, null, null, null, null, null, localVariable.index);
    killStatement = new Node(Token.ASSIGN, null, null, null, null, null, null, null);
    Node blockWithDeadVariable = new Node(Token.BLOCK, null, new Node[]{variableAssignment, killStatement}, null, null, null, null, null);
    analysis.flowThrough(blockWithDeadVariable, analysis.createEntryLattice());
    assertFalse(analysis.isLive(localVariable.index));

    // A local variable is live if it is assigned in a try block and then assigned in the catch block
    Var tryLocalVariable = scope.addVar("tryLocalVariable");
    node tryBlock = new Node(Token.TRY, null, null, null, null, null, null, null);
    Node tryStatement = new Node(Token.VAR, null, null, null, null, null, null, tryLocalVariable.index);
    Node tryBody = new Node(Token.BLOCK, null, null, null, null, null, null, null);
    Node catchBlock = new Node(Token.BLOCK, null, null, null, null, null, null, null);
    Node catchStatement = new Node(Token.ASSIGN, null, null, null, null, null, null, tryLocalVariable.index);
    Node finallyBlock = new Node(Token.BLOCK, null, null, null, null, null, null, null);
    Node finallyStatement = new Node(Token.ASSIGN, null, null, null, null, null, null, tryLocalVariable.index);
    analysis.flowThrough(tryBlock, analysis.createEntryLattice());
    analysis.flowThrough(tryBody, analysis.createEntryLattice());
    analysis.flowThrough(catchBlock, analysis.createEntryLattice());
    analysis.flowThrough(catchStatement, analysis.createEntryLattice());
    analysis.flowThrough(finallyBlock, analysis.createEntryLattice());
    analysis.flowThrough(finallyStatement, analysis.createEntryLattice());
    assertTrue(analysis.isLive(tryLocalVariable.index));

    // A local variable is dead if it is assigned in a try block and then never assigned again
    Var tryDeadVariable = scope.addVar("tryDeadVariable");
    node tryBlock2 = new Node(Token.TRY, null, null, null, null, null, null, null);
    Node tryStatement2 = new Node(Token.VAR, null, null, null, null, null, null, tryDeadVariable.index);
    Node tryBody2 = new Node(Token.BLOCK, null, null, null, null, null, null, null);
    Node finallyBlock2 = new Node(Token.BLOCK, null, null, null, null, null, null, null);
    Node finallyStatement2 = new Node(Token.ASSIGN, null, null, null, null, null, null, null);
    analysis.flowThrough(tryBlock2, analysis.createEntryLattice());
    analysis.flowThrough(tryBody2, analysis.createEntryLattice());
    analysis.flowThrough(finallyBlock2, analysis.createEntryLattice());
    analysis.flowThrough(finallyStatement2, analysis.createEntryLattice());
    assertFalse(analysis.isLive(tryDeadVariable.index));
  }
}