import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.InlineCostEstimator;
import com.google.javascript.rhino.Node;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    Node root = null; // edge case: null input
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);

    root = new Node(Node.IDENTIFIER, "identifier");
    cost = InlineCostEstimator.getCost(root);
    assertEquals(InlineCostEstimator.ESTIMATED_IDENTIFIER_COST, cost);

    root = new Node(Node.OBJECT_LITERAL, null); // edge case: empty object literal
    cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);

    root = new Node(Node.ARRAY_LITERAL, null); // edge case: empty array literal
    cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);

    // test with a simple expression
    root = new Node(Node.OP_ASSIGN, null, new Node(Node.NUMBER_LITERAL, 5), new Node(Node.NAME, "x"));
    cost = InlineCostEstimator.getCost(root);
    assertEquals(2 + InlineCostEstimator.ESTIMATED_IDENTIFIER_COST, cost); // 2 for OP_ASSIGN and NUMBER_LITERAL

    // test with a complex expression
    root = new Node(Node.BINARY_EXPRESSION, Node.LOGICAL_OR, new Node(Node.BINARY_EXPRESSION, Node.LOGICAL_AND, new Node(Node.NAME, "a"), new Node(Node.NUMBER_LITERAL, 1)), new Node(Node.BINARY_EXPRESSION, Node.LOGICAL_OR, new Node(Node.NAME, "b"), new Node(Node.NUMBER_LITERAL, 2)));
    cost = InlineCostEstimator.getCost(root);
    // Assuming each logical operator costs 1, each identifier costs 2, and each number literal costs 3
    // Thus, the cost of the complex expression should be 15 + 5 * 3 = 25
    assertEquals(25, cost);
  }

  @Test(expected = NullPointerException.class)
  public void testGetCostWithNullNode() {
    Node root = null;
    InlineCostEstimator.getCost(root);
  }
}