import static org.junit.Assert.*;
import org.junit.*;

public class InlineCostEstimatorTest {

  // Test case for getCost() method
  @Test
  public void testGetCost() {
    // Typical use case
    Node root1 = new Node();
    int result1 = InlineCostEstimator.getCost(root1);
    assertEquals(0, result1);

    // Edge case: Empty node
    Node root2 = null;
    int result2 = InlineCostEstimator.getCost(root2);
    assertEquals(0, result2);

    // Error scenario: Negative cost threshold
    Node root3 = new Node();
    int costThreshold3 = -1;
    int result3 = InlineCostEstimator.getCost(root3, costThreshold3);
    assertEquals(0, result3);
  }

  // Test case for getCost() method (with cost threshold)
  @Test
  public void testGetCostWithThreshold() {
    // Typical use case
    Node root1 = new Node();
    int costThreshold1 = 10;
    int result1 = InlineCostEstimator.getCost(root1, costThreshold1);
    assertEquals(0, result1);

    // Edge case: Empty node
    Node root2 = null;
    int costThreshold2 = 10;
    int result2 = InlineCostEstimator.getCost(root2, costThreshold2);
    assertEquals(0, result2);

    // Error scenario: Negative cost threshold
    Node root3 = new Node();
    int costThreshold3 = -1;
    int result3 = InlineCostEstimator.getCost(root3, costThreshold3);
    assertEquals(0, result3);
  }

  // Test case for continueProcessing() method
  @Test
  public void testContinueProcessing() {
    // Typical use case
    CompiledSizeEstimator estimator1 = new CompiledSizeEstimator(10);
    boolean result1 = estimator1.continueProcessing();
    assertTrue(result1);

    // Edge case: Cost threshold reached
    CompiledSizeEstimator estimator2 = new CompiledSizeEstimator(0);
    boolean result2 = estimator2.continueProcessing();
    assertFalse(result2);
  }

  // Test case for getLastChar() method
  @Test
  public void testGetLastChar() {
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    estimator.append("Hello");
    char result = estimator.getLastChar();
    assertEquals('o', result);
  }

  // Test case for append() method
  @Test
  public void testAppend() {
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    estimator.append("Hello");
    int result = estimator.getCost();
    assertEquals(5, result);
  }

  // Test case for addIdentifier() method
  @Test
  public void testAddIdentifier() {
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    estimator.addIdentifier("test");
    int result = estimator.getCost();
    assertEquals(2, result);
  }
}