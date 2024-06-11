import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.google.javascript.jscomp.MustBeReachingVariableDef;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MustBeReachingVariableDefTest {

  private static final Scope JS_SCOPE = new Scope();
  private static final AbstractCompiler COMPILER = new AbstractCompiler();
  private static final ControlFlowGraph<Node> CFG = new ControlFlowGraph<>();

  @Before
  public void setUp() {
    JS_SCOPE.setRootNode(Node.createBlock(null));
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(CFG, JS_SCOPE, COMPILER);
  }

  @Test
  public void testBasicFunctionality() {
    Node functionNode = Node.createFunction(Token.FUNCTION, null, Node.createBlock(null), null);
    MustDef result = analysis.compute(functionNode, analysis.createEntryLattice());
    assertEquals(analysis.createInitialEstimateLattice(), result);
  }

  @Test
  public void testForwardAnalysis() {
    final String variableName = "x";
    Node assignmentNode = Node.createAssign(Token.ASSIGN, Node.createName(Token.IDENTIFIER, variableName), Node.createInt(5));
    Node useNode = Node.createName(Token.IDENTIFIER, variableName);

    Node functionNode = Node.createBlock(null);
    functionNode.addChild(assignmentNode);
    functionNode.addChild(useNode);

    MustDef result = analysis.compute(functionNode, analysis.createEntryLattice());
    Node expectedDefinition = assignmentNode;
    assertEquals(expectedDefinition, result.reachingDef.get(JS_SCOPE.getVar(variableName)).node);
  }

  @Test
  public void testForwardAnalysisWithConditional() {
    final String variableName = "x";
    Node assignmentNode = Node.createAssign(Token.ASSIGN, Node.createName(Token.IDENTIFIER, variableName), Node.createInt(5));
    Node ifNode = Node.createIf(Token.IF, Node.createName(Token.IDENTIFIER, "condition"), null);
    ifNode.addChild(assignmentNode);
    ifNode.addChild(Node.createName(Token.IDENTIFIER, variableName));

    Node functionNode = Node.createBlock(null);
    functionNode.addChild(ifNode);

    MustDef result = analysis.compute(functionNode, analysis.createEntryLattice());
    Node expectedDefinition = assignmentNode;
    assertEquals(expectedDefinition, result.reachingDef.get(JS_SCOPE.getVar(variableName)).node);
  }

  @Test
  public void testForwardAnalysisWithMultipleDefinitions() {
    final String variableName = "x";
    Node definition1 = Node.createAssign(Token.ASSIGN, Node.createName(Token.IDENTIFIER, variableName), Node.createInt(5));
    Node definition2 = Node.createAssign(Token.ASSIGN, Node.createName(Token.IDENTIFIER, variableName), Node.createInt(10));
    Node useNode = Node.createName(Token.IDENTIFIER, variableName);

    Node functionNode = Node.createBlock(null);
    functionNode.addChild(definition1);
    functionNode.addChild(useNode);
    functionNode.addChild(definition2);

    MustDef result = analysis.compute(functionNode, analysis.createEntryLattice());
    Node expectedDefinition = definition2; // Last definition wins
    assertEquals(expectedDefinition, result.reachingDef.get(JS_SCOPE.getVar(variableName)).node);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testForwardAnalysisWithInvalidNode() {
    Node invalidNode = Node.createInvalid();
    MustDef result = analysis.compute(invalidNode, analysis.createEntryLattice());
    assertNull(result);
  }

  @Test
  public void testForwardAnalysisWithLoop() {
    final String variableName = "x";
    Node initializationNode = Node.createAssign(Token.ASSIGN, Node.createName(Token.IDENTIFIER, variableName), Node.createInt(1));
    Node loopNode = Node.createWhile(Token.WHILE, Node.createName(Token.IDENTIFIER, "condition"), null);
    loopNode.addChild(initializationNode);
    loopNode.addChild(Node.createName(Token.IDENTIFIER, variableName));

    Node functionNode = Node.createBlock(null);
    functionNode.addChild(loopNode);

    MustDef result = analysis.compute(functionNode, analysis.createEntryLattice());
    Node expectedDefinition = initializationNode;
    assertEquals(expectedDefinition, result.reachingDef.get(JS_SCOPE.getVar(variableName)).node);
  }

  @Test
  public void testForwardAnalysisWithFunctionCall() {
    final String variableName = "x";
    Node functionCallNode = Node.createCall(
        Token.CALL, Node.createName(Token.IDENTIFIER, "someFunction"), Node.createName(Token.IDENTIFIER, variableName));
    Node useNode = Node.createName(Token.IDENTIFIER, variableName);

    Node functionNode = Node.createBlock(null);
    functionNode.addChild(functionCallNode);
    functionNode.addChild(useNode);

    MustDef result = analysis.compute(functionNode, analysis.createEntryLattice());
    Node expectedDefinition = null; // Undefined in this scope
    assertNull(result.reachingDef.get(JS_SCOPE.getVar(variableName)));
  }

  @Test
  public void testForwardAnalysisWithVariableInConditional() {
    final String variableName = "x";
    Node variableNode = Node.createName(Token.IDENTIFIER, variableName);
    Node ifNode = Node.createIf(Token.IF, Node.createBinaryOp(Token.LOGICAL_OR, variableNode, Node.createInt(0)), null);
    Node useNode = Node.createName(Token.IDENTIFIER, variableName);

    Node functionNode = Node.createBlock(null);
    functionNode.addChild(ifNode);
    functionNode.addChild(useNode);

    MustDef result = analysis.compute(functionNode, analysis.createEntryLattice());
    Node expectedDefinition = null; // Undefined in this scope
    assertNull(result.reachingDef.get(JS_SCOPE.getVar(variableName)));
  }

  @Test
  public void testForwardAnalysisWithVariableInLoopCondition() {
    final String variableName = "x";
    Node variableNode = Node.createName(Token.IDENTIFIER, variableName);
    Node loopNode = Node.createWhile(Token.WHILE, variableNode, null);
    Node useNode = Node.createName(Token.IDENTIFIER, variableName);

    Node functionNode = Node.createBlock(null);
    functionNode.addChild(loopNode);
    functionNode.addChild(useNode);

    MustDef result = analysis.compute(functionNode, analysis.createEntryLattice());
    Node expectedDefinition = null; // Undefined in this scope
    assertNull(result.reachingDef.get(JS_SCOPE.getVar(variableName)));
  }
}