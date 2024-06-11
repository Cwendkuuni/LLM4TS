import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineCostEstimator.*;
import com.google.javascript.rhino.Node;
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
 int costThreshhold = 10;
 int cost = getCost(root, costThreshhold);
 assertTrue(cost <= costThreshhold);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeThreshold() {
 Node root = createTestNode("root");
 int costThreshhold = -1;
 try {
 getCost(root, costThreshhold);
 fail("Should throw IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 //expected
 }
 }

 @Test
 public void testCompiledSizeEstimatorAddString() {
 CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
 String str = "testString";
 estimator.add(new Node("StringLiteral", null, str));
 assertEquals(str.length(), estimator.getCost());
 }

 @Test
 public void testCompiledSizeEstimatorAddIdentifier() {
 CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
 String identifier = "testIdentifier";
 estimator.addIdentifier(identifier);
 assertEquals(ESTIMATED_IDENTIFIER_COST, estimator.getCost());
 }

 @Test
 public void testCompiledSizeEstimatorAddMultipleIdentifiers() {
 CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
 String identifier1 = "testIdentifier1";
 String identifier2 = "testIdentifier2";
 estimator.addIdentifier(identifier1);
 estimator.addIdentifier(identifier2);
 assertEquals(2 * ESTIMATED_IDENTIFIER_COST, estimator.getCost());
 }

 @Test
 public void testCompiledSizeEstimatorAddMultipleStrings() {
 CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
 String str1 = "testString1";
 String str2 = "testString2";
 estimator.add(new Node("StringLiteral", null, str1));
 estimator.add(new Node("StringLiteral", null, str2));
 assertEquals(str1.length() + str2.length(), estimator.getCost());
 }

 @Test
 public void testCompiledSizeEstimatorAddNodeWithLimit() {
 CompiledSizeEstimator estimator = new CompiledSizeEstimator(5);
 Node root = createTestNode("root");
 estimator.add(root);
 assertEquals(5, estimator.getCost());
 }
}