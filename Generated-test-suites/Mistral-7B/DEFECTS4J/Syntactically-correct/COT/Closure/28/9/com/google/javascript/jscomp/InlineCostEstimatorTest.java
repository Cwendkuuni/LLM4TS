import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.InlineCostEstimator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeOperations;

import org.junit.Test;

public class InlineCostEstimatorTest {

 private Node rootNode;
 private Node leafNode;
 private CompiledSizeEstimator estimator;

 @Test
 public void testGetCostOfEmptyNode() {
 InlineCostEstimator.getCost(null);
 estimator = new InlineCostEstimator.CompiledSizeEstimator(1);
 assertEquals(0, estimator.getCost());
 }

 @Test
 public void testGetCostOfSingleNode() {
 rootNode = NodeOperations.createTextNode("someCode");
 estimator = new InlineCostEstimator.CompiledSizeEstimator(1);
 InlineCostEstimator.getCost(rootNode, estimator.getMaxCost());
 estimator = mock(InlineCostEstimator.CompiledSizeEstimator.class);
 when(estimator.getCost()).thenReturn(4);
 Node node = mock(Node.class);
 when(node.getClass()).thenReturn(Node.class);
 when(node.getFirstChild()).thenReturn(rootNode);
 InlineCostEstimator.getCost(node, estimator.getMaxCost());
 assertEquals(4, estimator.getCost());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetCostWithNegativeCostThreshold() {
 InlineCostEstimator.getCost(rootNode, -1);
 }

 @Test
 public void testGetCostWithMaxCostReached() {
 rootNode = NodeOperations.createTextNode("someCode1");
 leafNode = NodeOperations.createTextNode("someCode2");
 estimator = new InlineCostEstimator.CompiledSizeEstimator(4);
 when(estimator.getMaxCost()).thenReturn(4);
 when(leafNode.getFirstChild()).thenReturn(null);
 Node parent = mock(Node.class);
 when(parent.getFirstChild()).thenReturn(rootNode);
 when(parent.getLastChild()).thenReturn(leafNode);
 InlineCostEstimator.getCost(parent, estimator.getMaxCost());
 assertEquals(8, estimator.getCost());
 }

 @Test
 public void testAddIdentifier() {
 estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
 estimator.addIdentifier("identifier1");
 estimator.addIdentifier("identifier2");
 assertEquals(6, estimator.getCost());
 }

 @Test
 public void testAddConstant() {
 estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
 estimator.add(InlineCostEstimator.ESTIMATED_IDENTIFIER);
 estimator.add("true");
 estimator.add("false");
 estimator.add(null);
 estimator.add(InlineCostEstimator.ESTIMATED_IDENTIFIER);
 assertEquals(10, estimator.getCost());
 }
}