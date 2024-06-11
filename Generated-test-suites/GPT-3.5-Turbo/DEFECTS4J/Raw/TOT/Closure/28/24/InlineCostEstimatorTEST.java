import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class InlineCostEstimatorTest {

   @Test
   public void testGetCost_rootReturnsCost() {
      Node rootNode = new Node("root");
      int expectedCost = 5; // Assuming "root" has length 4
      int actualCost = InlineCostEstimator.getCost(rootNode);
      assertEquals(expectedCost, actualCost);
   }

   @Test
   public void testGetCostWithThreshold_rootReturnsCost() {
      Node rootNode = new Node("root");
      int threshold = 10;
      int expectedCost = 5; // Assuming "root" has length 4
      int actualCost = InlineCostEstimator.getCost(rootNode, threshold);
      assertEquals(expectedCost, actualCost);
   }

   @Test
   public void testGetCostWithThreshold_thresholdExceeded() {
      Node rootNode = new Node("root");
      int threshold = 3; // A threshold smaller than the cost of "root"
      int expectedCost = 0;
      int actualCost = InlineCostEstimator.getCost(rootNode, threshold);
      assertEquals(expectedCost, actualCost);
   }

   @Test
   public void testCompiledSizeEstimator_add_singleNodeCost() {
      InlineCostEstimator.CompiledSizeEstimator estimator = 
         new InlineCostEstimator.CompiledSizeEstimator(Integer.MAX_VALUE);
      Node node = new Node("node");
      int expectedCost = 4; // Assuming "node" has length 4
      estimator.add(node);
      int actualCost = estimator.getCost();
      assertEquals(expectedCost, actualCost);
   }

   @Test
   public void testContinueProcessing_true() {
      InlineCostEstimator.CompiledSizeEstimator estimator = 
         new InlineCostEstimator.CompiledSizeEstimator(Integer.MAX_VALUE);
      assertTrue(estimator.continueProcessing());
   }

   @Test
   public void testContinueProcessing_false() {
      InlineCostEstimator.CompiledSizeEstimator estimator = 
         new InlineCostEstimator.CompiledSizeEstimator(10);
      assertFalse(estimator.continueProcessing());
   }

   @Test
   public void testAppend_updatesCostAndLastChar() {
      InlineCostEstimator.CompiledSizeEstimator estimator = 
         new InlineCostEstimator.CompiledSizeEstimator(Integer.MAX_VALUE);
      String str = "testString";
      int expectedCost = str.length();
      char expectedLastChar = str.charAt(str.length() - 1);
      estimator.append(str);
      int actualCost = estimator.getCost();
      char actualLastChar = estimator.getLastChar();
      assertEquals(expectedCost, actualCost);
      assertEquals(expectedLastChar, actualLastChar);
   }

   @Test
   public void testAddIdentifier_addsEstimatedIdentifier() {
      InlineCostEstimator.CompiledSizeEstimator estimator = 
         new InlineCostEstimator.CompiledSizeEstimator(Integer.MAX_VALUE);
      String identifier = "testIdentifier";
      int expectedCost = InlineCostEstimator.ESTIMATED_IDENTIFIER_COST;
      estimator.addIdentifier(identifier);
      int actualCost = estimator.getCost();
      assertEquals(expectedCost, actualCost);
   }

}