import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.InlineCostEstimator;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class InlineCostEstimatorTest {

 private Node rootNode;
 private CompiledSizeEstimator estimator;

 @Test
 public void testGetCostOfRootNode() {
 rootNode = mock(Node.class);
 estimator = new InlineCostEstimator.CompiledSizeEstimator(Integer.MAX_VALUE);

 InlineCostEstimator.getCost(rootNode, Integer.MAX_VALUE);

 verify(estimator).add(rootNode);
 }

 @Test
 public void testGetCostOfRootNodeWithThreshold() {
 rootNode = mock(Node.class);
 estimator = new InlineCostEstimator.CompiledSizeEstimator(5);

 InlineCostEstimator.getCost(rootNode, 5);

 verify(estimator, times(1)).add(rootNode);
 verify(estimator, never()).append(anyString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetCostWithNegativeThreshold() {
 estimator = new InlineCostEstimator.CompiledSizeEstimator(-1);

 InlineCostEstimator.getCost(rootNode, -1);
 }

 @Test
 public void testAddIdentifier() {
 estimator = new InlineCostEstimator.CompiledSizeEstimator(Integer.MAX_VALUE);

 when(rootNode.getFirstChild()).thenReturn(createIdentifierNode("identifier"));

 InlineCostEstimator.getCost(rootNode, Integer.MAX_VALUE);

 verify(estimator, times(1)).addIdentifier("identifier");
 }

 @Test
 public void testAddMultipleIdentifiers() {
 estimator = new InlineCostEstimator.CompiledSizeEstimator(Integer.MAX_VALUE);

 when(rootNode.getFirstChild()).thenReturn(createIdentifierNode("identifier1"));
 when(rootNode.getLastChild()).thenReturn(createIdentifierNode("identifierN"));

 InlineCostEstimator.getCost(rootNode, Integer.MAX_VALUE);

 verify(estimator, times(2)).addIdentifier("identifier");
 }

 @Test
 public void testAppendString() {
 estimator = new InlineCostEstimator.CompiledSizeEstimator(Integer.MAX_VALUE);

 InlineCostEstimator.getCost(createStringNode("string"), Integer.MAX_VALUE);

 verify(estimator, times(1)).append("string");
 }

 @Test
 public void testAppendMultipleStrings() {
 estimator = new InlineCostEstimator.CompiledSizeEstimator(Integer.MAX_VALUE);

 InlineCostEstimator.getCost(createMultiStringNode("string1", "string2"), Integer.MAX_VALUE);

 verify(estimator, times(1)).append("string1");
 verify(estimator, times(1)).append("string2");
 }

 private Node createIdentifierNode(String identifier) {
 Node node = mock(Node.class);
 when(node.getType()).thenReturn(Node.IDENTIFIER);
 when(node.getFirstChild()).thenReturn(Node.NULL);
 when(node.getLastChild()).thenReturn(Node.NULL);
 when(node.getString()).thenReturn(identifier);
 return node;
 }

 private Node createStringNode(String str) {
 Node node = mock(Node.class);
 when(node.getType()).thenReturn(Node.STRING);
 when(node.getString()).thenReturn(str);
 return node;
 }

 private Node createMultiStringNode(String... strings) {
 Node node = mock(Node.class);
 String string = String.join("", strings);
 when(node.getType()).thenReturn(Node.STRING);
 when(node.getString()).thenReturn(string);
 return node;
 }

}