import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.google.javascript.jscomp.InlineCostEstimator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.CompilationUnit;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

public class InlineCostEstimatorTest {

  private NodeFactory nodeFactory = mock(NodeFactory.class);
  private CompilationUnit unit = mock(CompilationUnit.class);

  @Test
  public void testGetCostOfRootNode() {
    // Given
    Node rootNode = Node.newCompoundStatement(Node.EMPTY_NODE_LIST);

    // When
    int cost = InlineCostEstimator.getCost(rootNode);

    // Then
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostOfEmptyStatement() {
    // Given
    Node emptyStatement = Node.newEmptyStatement();

    // When
    int cost = InlineCostEstimator.getCost(emptyStatement, Integer.MAX_VALUE);

    // Then
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostOfIdentifier() {
    // Given
    String identifier = "myVariable";
    Node identifierNode = Node.newIdentifier(identifier);

    // When
    int cost = InlineCostEstimator.getCost(identifierNode, Integer.MAX_VALUE);

    // Then
    assertEquals(identifier.length(), cost);
  }

  @Test
  public void testGetCostOfSimpleExpression() {
    // Given
    String identifier = "myVariable";
    Node identifierNode = Node.newIdentifier(identifier);
    Node expression = Node.newName(identifier);

    when(nodeFactory.newIdentifier(unit, Token.IDENTIFIER, identifier)).thenReturn(identifierNode);

    // When
    int cost = InlineCostEstimator.getCost(expression, Integer.MAX_VALUE);

    // Then
    assertEquals(identifier.length(), cost);
  }

  @Test
  public void testGetCostOfComplexExpression() {
    // Given
    String identifier1 = "myVariable1";
    String identifier2 = "myVariable2";
    Node identifierNode1 = Node.newIdentifier(identifier1);
    Node identifierNode2 = Node.newIdentifier(identifier2);
    Node expression = Node.newBinaryOperator("+", Node.newName(identifier1), Node.newName(identifier2));

    when(nodeFactory.newIdentifier(unit, Token.IDENTIFIER, identifier1)).thenReturn(identifierNode1);
    when(nodeFactory.newIdentifier(unit, Token.IDENTIFIER, identifier2)).thenReturn(identifierNode2);

    // When
    int cost = InlineCostEstimator.getCost(expression, Integer.MAX_VALUE);

    // Then
    assertEquals(identifier1.length() + identifier2.length(), cost);
  }

  @Test
  public void testGetCostOfStatement() {
    // Given
    String identifier = "myVariable";
    Node identifierNode = Node.newIdentifier(identifier);
    Node variableAssignment = Node.newAssignment(identifierNode, Node.newName(identifier), Node.newNumber(1));

    when(nodeFactory.newIdentifier(unit, Token.IDENTIFIER, identifier)).thenReturn(identifierNode);

    // When
    int cost = InlineCostEstimator.getCost(variableAssignment, Integer.MAX_VALUE);

    // Then
    assertEquals(identifier.length() + 1, cost);
  }

  @Test
  public void testGetCostOfCompoundStatement() {
    // Given
    String identifier1 = "myVariable1";
    String identifier2 = "myVariable2";
    Node identifierNode1 = Node.newIdentifier(identifier1);
    Node identifierNode2 = Node.newIdentifier(identifier2);
    Node expression1 = Node.newName(identifier1);
    Node expression2 = Node.newName(identifier2);
    Node statement1 = Node.newAssignment(identifierNode1, expression1, Node.newNumber(1));
    Node statement2 = Node.newAssignment(identifierNode2, expression2, Node.newNumber(2));
    Node compoundStatement = Node.newCompoundStatement(Node.nodeList(statement1, statement2));

    when(nodeFactory.newIdentifier(unit, Token.IDENTIFIER, identifier1)).thenReturn(identifierNode1);
    when(nodeFactory.newIdentifier(unit, Token.IDENTIFIER, identifier2)).thenReturn(identifierNode2);

    // When
    int cost = InlineCostEstimator.getCost(compoundStatement, Integer.MAX_VALUE);

    // Then
    assertEquals(identifier1.length() + identifier2.length() * 2 + 1, cost);
  }
}