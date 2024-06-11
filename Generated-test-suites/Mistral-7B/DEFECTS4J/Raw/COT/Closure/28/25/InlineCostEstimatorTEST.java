import static org.junit.Assert.assertEquals;
 import static org.junit.Assert.assertFalse;
 import static org.junit.Assert.assertNotNull;
 import static org.junit.Assert.assertNull;
 import static org.junit.Assert.assertTrue;
 import static com.google.javascript.jscomp.InlineCostEstimator.ESTIMATED_IDENTIFIER;
 import static com.google.javascript.jscomp.InlineCostEstimator.ESTIMATED_IDENTIFIER_COST;

 import org.junit.Test;
 import org.junit.runner.RunWith;
 import org.junit.runners.JUnit4;
 import com.google.javascript.rhino.Node;

 @RunWith(JUnit4.class)
 public class InlineCostEstimatorTest {

 @Test
 public void testGetCostRoot() {
 InlineCostEstimator.getCost(createNode("root"));
 }

 @Test
 public void testGetCostRootWithThreshold() {
 InlineCostEstimator.getCost(createNode("root"), 1);
 }

 @Test(expected = NullPointerException.class)
 public void testGetCostNullNode() {
 InlineCostEstimator.getCost(null);
 }

 @Test
 public void testCompiledSizeEstimatorAdd() {
 Node root = createNode("root");
 CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
 estimator.add(root);
 assertEquals(0, estimator.getCost());
 }

 @Test
 public void testCompiledSizeEstimatorAppendString() {
 Node root = createNode("root");
 CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
 estimator.add(root);
 estimator.append("test");
 assertEquals(5, estimator.getCost());
 }

 @Test
 public void testCompiledSizeEstimatorAddIdentifier() {
 CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
 estimator.addIdentifier("test");
 estimator.addIdentifier("identifier");
 assertEquals(ESTIMATED_IDENTIFIER_COST * 2, estimator.getCost());
 }

 @Test
 public void testCompiledSizeEstimatorContinueProcessing() {
 CompiledSizeEstimator estimator = new CompiledSizeEstimator(5);
 estimator.append("test");
 estimator.append("test");
 assertFalse(estimator.continueProcessing());
 }

 private Node createNode(String nodeName) {
 return new Node(nodeName);
 }

 }