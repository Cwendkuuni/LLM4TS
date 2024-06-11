import com.google.javascript.jscomp.MaybeReachingVariableUse;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.*;

public class MaybeReachingVariableUseTest {

  private MaybeReachingVariableUse analysis;
  private Scope jsScope;

  @Before
  public void setUp() {
    // Assuming you have created the ControlFlowGraph and Scope objects
    Node entryNode = Node.newNode(Token.BLOCK);
    jsScope = new Scope(null);
    analysis = new MaybeReachingVariableUse(
        new ControlFlowGraph<Node>(entryNode), jsScope, null);
  }

  @Test
  public void testSimpleAssignment() {
    Node assignmentNode = Node.newNode(Token.ASSIGN, Token.IDENT, Token.NUM, null);
    analysis.compute(entryNode, null);
    Collection<Node> uses = analysis.getUses("variableName", assignmentNode);
    assertNotNull(uses);
    assertTrue(uses.isEmpty());
  }

  @Test
  public void testAssignmentWithConditional() {
    Node conditionNode = Node.newNode(Token.IF, Token.IDENT, Token.NUM, null);
    Node assignmentNode = Node.newNode(Token.ASSIGN, Token.IDENT, Token.NUM, null);
    Node trueNode = Node.newNode(Token.BLOCK);
    Node falseNode = Node.newNode(Token.BLOCK);

    analysis.compute(entryNode, null);

    analysis.getCFG().addEdge(conditionNode, trueNode, null);
    analysis.getCFG().addEdge(conditionNode, falseNode, null);
    analysis.getCFG().addEdge(trueNode, assignmentNode, null);

    analysis.compute(conditionNode, null);

    Collection<Node> uses = analysis.getUses("variableName", assignmentNode);
    assertNotNull(uses);
    assertTrue(uses.contains(conditionNode));
  }

  @Test
  public void testAssignmentWithLoop() {
    Node loopStartNode = Node.newNode(Token.WHILE, null, null, null);
    Node assignmentNode = Node.newNode(Token.ASSIGN, Token.IDENT, Token.NUM, null);

    analysis.compute(entryNode, null);

    analysis.getCFG().addEdge(loopStartNode, assignmentNode, null);
    analysis.compute(loopStartNode, null);

    Collection<Node> uses = analysis.getUses("variableName", assignmentNode);
    assertNotNull(uses);
    assertTrue(uses.contains(loopStartNode));
  }

  @Test(expected = AssertionError.class)
  public void testAssignmentWithEscapedVariable() {
    Node escapeNode = Node.newNode(Token.VAR, Token.IDENT, null, null);
    Node assignmentNode = Node.newNode(Token.ASSIGN, Token.IDENT, Token.NUM, null);

    analysis.compute(entryNode, null);

    analysis.getCFG().addEdge(escapeNode, assignmentNode, null);
    analysis.compute(escapeNode, null);

    Collection<Node> uses = analysis.getUses("escapedVariableName", assignmentNode);
    assertNotNull(uses);
    fail();
  }

  @Test
  public void testGetUsesForNonExistentVariable() {
    Node assignmentNode = Node.newNode(Token.ASSIGN, Token.IDENT, Token.NUM, null);

    analysis.compute(entryNode, null);

    Collection<Node> uses = analysis.getUses("nonExistentVariableName", assignmentNode);
    assertNotNull(uses);
    assertTrue(uses.isEmpty());
  }

  @Test
  public void testGetUsesForFunctionParameter() {
    Node functionNode = Node.newNode(Token.FUNCTION, null, null, null);
    Node parameterNode = functionNode.getFirstChild();
    Node assignmentNode = Node.newNode(Token.ASSIGN, Token.IDENT, Token.NUM, null);

    analysis.compute(entryNode, null);

    analysis.getCFG().addEdge(functionNode, assignmentNode, null);
    analysis.compute(functionNode, null);

    Collection<Node> uses = analysis.getUses("functionParameterName", assignmentNode);
    assertNotNull(uses);
    assertTrue(uses.isEmpty());
  }
}