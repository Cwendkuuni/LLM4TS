import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.InlineCostEstimator;
import com.google.javascript.rhino.Node;

public class InlineCostEstimatorTest {

  @org.junit.Test
  public void testGetCost() {
    Node root = null; // add a test for null root node
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);

    root = new Node(); // add a test for empty node
    cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);

    // add tests for nodes with different types and children
    Node rootWithIdentifier = new Node("Identifier", "identifier");
    cost = InlineCostEstimator.getCost(rootWithIdentifier);
    assertEquals(InlineCostEstimator.ESTIMATED_IDENTIFIER_COST, cost);

    Node rootWithBoolean = new Node("Boolean", true);
    cost = InlineCostEstimator.getCost(rootWithBoolean);
    assertEquals(0, cost);

    Node rootWithNumber = new Node("Number", 1.0);
    cost = InlineCostEstimator.getCost(rootWithNumber);
    assertEquals(0, cost);

    Node rootWithString = new Node("String", "test");
    cost = InlineCostEstimator.getCost(rootWithString);
    assertEquals(rootWithString.toString().length(), cost);

    Node rootWithArray = new Node("Array", new Node[0]);
    cost = InlineCostEstimator.getCost(rootWithArray);
    assertEquals(0, cost);

    Node rootWithObject = new Node("Object", new Node[0]);
    cost = InlineCostEstimator.getCost(rootWithObject);
    assertEquals(0, cost);

    Node rootWithOperators = new Node("BinaryOperator", "op_Add", new Node("Number", 1), new Node("Number", 2));
    cost = InlineCostEstimator.getCost(rootWithOperators);
    assertEquals(3, cost); // 'op_Add' + '(' + ')' + 'N' + 'u' + 'm' + 'b' + 'e' + 'r'

    Node rootWithFunction = new Node("FunctionDeclaration", "functionName", null, new Node("Block"), null);
    cost = InlineCostEstimator.getCost(rootWithFunction);
    assertEquals(0, cost);

    Node rootWithIfStatement = new Node("IfStatement", null, new Node("Boolean", true), new Node("Block"), new Node("Block"));
    cost = InlineCostEstimator.getCost(rootWithIfStatement);
    assertEquals(2, cost); // 'I' + 'f'

    Node rootWithWhileStatement = new Node("WhileStatement", null, new Node("Boolean", true), new Node("Block"), new Node("Statement"));
    cost = InlineCostEstimator.getCost(rootWithWhileStatement);
    assertEquals(4, cost); // 'W' + 'h' + 'i' + 'l' + 'e'
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testGetCostNegativeCostThreshhold() {
    InlineCostEstimator.getCost(new Node("Identifier", "identifier"), -1);
  }
}