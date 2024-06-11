import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.google.javascript.jscomp.InlineCostEstimator;
import com.google.javascript.rhino.Node;

public class InlineCostEstimatorTest {

 private Node mockNode;
 private InlineCostEstimator estimator;

 @Test
 public void testGetCost() {
 estimator = new InlineCostEstimator();
 mockNode = mock(Node.class);
 when(mockNode.getFirstChild()).thenReturn(null); // empty node

 int cost = InlineCostEstimator.getCost(mockNode);
 assertEquals(0, cost);
 }

 @Test
 public void testGetCostWithRoot() {
 estimator = new InlineCostEstimator();
 mockNode = mock(Node.class);
 Node root = new Node(1, null); // root node
 when(mockNode.getFirstChild()).thenReturn(root);

 int cost = InlineCostEstimator.getCost(mockNode);
 assertEquals(0, cost); // cost should be 0 for empty root node
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetCostWithNegativeCostThreshold() {
 estimator = new InlineCostEstimator();
 int costThreshhold = -1;
 InlineCostEstimator.getCost(mockNode, costThreshhold);
 }

 @Test
 public void testGetCostWithRootAndCostThreshold() {
 estimator = new InlineCostEstimator();
 mockNode = mock(Node.class);
 Node root = new Node(1, null); // root node
 when(mockNode.getFirstChild()).thenReturn(root);

 int costThreshhold = 1;
 int cost = InlineCostEstimator.getCost(mockNode, costThreshhold);
 assertEquals(costThreshhold, cost);
 }

 @Test
 public void testGetCostWithComplexNode() {
 estimator = new InlineCostEstimator();
 mockNode = mock(Node.class);
 Node identifierNode = new Node(2, null);
 Node stringNode = new Node(8, "test");
 Node operatorNode = new Node(7, "/");
 Node numberNode = new Node(1, "10");
 Node binaryExpressionNode = new Node(5, identifierNode, operatorNode, numberNode);
 Node assignmentNode = new Node(4, identifierNode, numberNode);
 Node statementNode = new Node(3, assignmentNode);
 when(mockNode.getFirstChild()).thenReturn(statementNode);

 int costThreshhold = 10;
 int cost = InlineCostEstimator.getCost(mockNode, costThreshhold);
 assertEquals(costThreshhold, cost);
 }

 @Test
 public void testAddIdentifier() {
 estimator = new InlineCostEstimator().new CompiledSizeEstimator(5);
 estimator.addIdentifier("test");
 estimator.addIdentifier("identifier");
 assertEquals(12, estimator.getCost());
 }

 @Test
 public void testContinueProcessing() {
 estimator = new InlineCostEstimator().new CompiledSizeEstimator(10);
 estimator.add("test");
 estimator.add("identifier");
 estimator.add("test");
 estimator.add("identifier");
 estimator.add("test");
 estimator.add("identifier");
 assertEquals(30, estimator.getCost());
 assertEquals(true, estimator.continueProcessing());
 }

 @Test
 public void testContinueProcessingWithMaxCostReached() {
 estimator = new InlineCostEstimator().new CompiledSizeEstimator(10);
 estimator.add("test");
 estimator.add("identifier");
 estimator.add("test");
 estimator.add("identifier");
 estimator.add("test");
 estimator.add("identifier");
 estimator.add("test");
 estimator.add("identifier");
 estimator.add("test");
 estimator.add("identifier");
 estimator.add("test");
 estimator.add("identifier");
 assertEquals(30, estimator.getCost());
 assertEquals(false, estimator.continueProcessing());
 }
}