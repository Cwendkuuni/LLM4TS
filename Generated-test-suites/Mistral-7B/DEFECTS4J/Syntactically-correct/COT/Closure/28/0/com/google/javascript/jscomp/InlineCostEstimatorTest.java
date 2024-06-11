import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineCostEstimator.*;
import org.junit.Test;

public class InlineCostEstimatorTest {

 private Node createTestNode(String nodeValue) {
 return new Node("ExpressionStatement", new Node("Identifier", null, nodeValue));
 }

 @Test
 public void testGetCost() {
 Node root = createTestNode("root");
 int cost = getCost(root);
 assertEquals(ESTIMATED_IDENTIFIER_COST, cost);
 }

 @Test
 public void testGetCostWithThreshold() {
 Node root = createTestNode("root");
 int cost = getCost(root, 10);
 assertEquals(ESTIMATED_IDENTIFIER_COST, cost);
 }

 @Test
 public void testAddIdentifier() {
 CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
 estimator.addIdentifier("identifier1");
 estimator.addIdentifier("identifier2");
 assertEquals(2 * ESTIMATED_IDENTIFIER_COST, estimator.getCost());
 }

 @Test(expected = RuntimeException.class)
 public void testContinueProcessingFalse() {
 CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
 estimator.addIdentifier("identifier1");
 estimator.addIdentifier("identifier2");
 estimator.addIdentifier("identifier3"); // This should throw an exception
 }

 @Test
 public void testAppendString() {
 CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
 estimator.append("string1");
 estimator.append("string2");
 assertEquals(2 * (1 + "string1".length()), estimator.getCost());
 }

 @Test
 public void testGetLastChar() {
 CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
 estimator.append("string1");
 assertEquals("string1".charAt(0), estimator.getLastChar());
 }
}