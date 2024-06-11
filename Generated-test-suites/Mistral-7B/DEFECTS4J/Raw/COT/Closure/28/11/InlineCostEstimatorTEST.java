import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.InlineCostEstimator;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeOperations;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class InlineCostEstimatorTest {

 private Node mockNode;
 private CompiledSizeEstimator estimator;

 @Before
 public void setup() {
 mockNode = mock(Node.class);
 estimator = new InlineCostEstimator.CompiledSizeEstimator(Integer.MAX_VALUE);
 }

 @Test
 public void testGetCost_RootNode_ThreshholdIsMax() {
 // Test case for getCost(Node root) method with root node and max threshold
 when(mockNode.getClass()).thenReturn(NodeOperations.classForNode(NodeOperations.ROOT_NODE_TYPE));

 estimator.add(mockNode);
 assertEquals(0, estimator.getCost());
 }

 @Test
 public void testGetCost_RootNode_ThreshholdIsBelowMax() {
 // Test case for getCost(Node root, int costThreshhold) method with root node and cost threshold less than max
 when(mockNode.getClass()).thenReturn(NodeOperations.classForNode(NodeOperations.ROOT_NODE_TYPE));

 estimator.add(mockNode);
 estimator.maxCost = 10;
 assertEquals(0, estimator.getCost());
 }

 @Test(expected = AssertionError.class)
 public void testGetCost_RootNode_ThreshholdIsReached() {
 // Test case for getCost(Node root, int costThreshhold) method with root node and cost threshold reached
 when(mockNode.getClass()).thenReturn(NodeOperations.classForNode(NodeOperations.ROOT_NODE_TYPE));

 estimator.add(mockNode);
 estimator.maxCost = 1;
 estimator.add(mockNode);
 }

 @Test
 public void testAddIdentifier() {
 // Test case for addIdentifier method
 estimator.addIdentifier("identifier");
 assertEquals(3, estimator.getCost());
 }

 @Test
 public void testAddConstant() {
 // Test case for add method with constant value
 estimator.append("true");
 assertEquals(1, estimator.getCost());

 estimator.append("null");
 assertEquals(1, estimator.getCost());

 estimator.append("false");
 assertEquals(1, estimator.getCost());
 }

 @Test
 public void testAddString() {
 // Test case for add method with string
 estimator.append("testString");
 assertEquals(7, estimator.getCost());
 }

 @Test
 public void testContinueProcessing_False() {
 // Test case for continueProcessing method returning false
 estimator.last = 'a';
 estimator.cost = 1;
 estimator.continueProcessing = false;

 assertEquals(1, estimator.getCost());
 assertFalse(estimator.continueProcessing());
 }

 @Test
 public void testGetLastChar() {
 // Test case for getLastChar method
 estimator.last = 'a';
 assertEquals('a', estimator.getLastChar());
 }
}