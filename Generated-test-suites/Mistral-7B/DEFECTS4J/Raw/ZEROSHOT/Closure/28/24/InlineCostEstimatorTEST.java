import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class InlineCostEstimatorTest {

 @Test
 public void testGetCost() {
 InlineCostEstimator estimator = new InlineCostEstimator();
 Node root = mock(Node.class);

 when(root.getTreeType()).thenReturn(Node.ROOT);

 // Test with no threshold
 int costNoThreshold = estimator.getCost(root);
 assertEquals(0, costNoThreshold);

 // Test with a threshold
 int costWithThreshold = estimator.getCost(root, 10);
 assertEquals(0, costWithThreshold);
 }

 @Test
 public void testGetCostWithIdentifiers() {
 InlineCostEstimator estimator = new InlineCostEstimator();
 Node identifierNode = mock(Node.class);
 when(identifierNode.getTreeType()).thenReturn(Node.IDENTIFIER);
 String identifier = "myIdentifier";
 when(identifierNode.getText()).thenReturn(identifier);

 Node root = mock(Node.class);
 root.addChild(identifierNode);

 int cost = estimator.getCost(root, 10);
 assertEquals(identifier.length() + 2, cost); // +2 for estimated identifier cost
 }

 @Test
 public void testGetCostWithMultipleIdentifiers() {
 InlineCostEstimator estimator = new InlineCostEstimator();
 Node identifierNode1 = mock(Node.class);
 Node identifierNode2 = mock(Node.class);
 when(identifierNode1.getTreeType()).thenReturn(Node.IDENTIFIER);
 when(identifierNode2.getTreeType()).thenReturn(Node.IDENTIFIER);
 String identifier1 = "identifier1";
 String identifier2 = "identifier2";
 when(identifierNode1.getText()).thenReturn(identifier1);
 when(identifierNode2.getText()).thenReturn(identifier2);

 Node root = mock(Node.class);
 root.addChild(identifierNode1);
 root.addChild(identifierNode2);

 int cost = estimator.getCost(root, 10);
 assertEquals(identifier1.length() + identifier2.length() + 4, cost); // +4 for estimated identifier costs
 }
}