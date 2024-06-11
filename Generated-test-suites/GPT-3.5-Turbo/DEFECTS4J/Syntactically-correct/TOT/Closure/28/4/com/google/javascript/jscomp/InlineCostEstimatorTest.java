import org.junit.Test;
import static org.junit.Assert.*;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost_RootNotNull() {
    Node root = new Node();
    int cost = InlineCostEstimator.getCost(root);
    assertNotNull(cost);
  }

  @Test
  public void testGetCost_RootMaxCost() {
    Node root = new Node();
    int maxCost = Integer.MAX_VALUE;
    int cost = InlineCostEstimator.getCost(root, maxCost);
    assertEquals(maxCost, cost);
  }

  @Test
  public void testGetCost_CompiledSizeEstimator_MaxCost() {
    int maxCost = Integer.MAX_VALUE;
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(maxCost);
    estimator.add(new Node());
    int cost = estimator.getCost();
    assertEquals(0, cost);
  }

  @Test
  public void testGetCost_CompiledSizeEstimator_AddIdentifier() {
    int costThreshhold = Integer.MAX_VALUE;
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(costThreshhold);
    String identifier = "identifier";
    estimator.addIdentifier(identifier);
    int cost = estimator.getCost();
    assertEquals(InlineCostEstimator.ESTIMATED_IDENTIFIER_COST, cost);
  }

  @Test
  public void testGetCost_CompiledSizeEstimator_Append() {
    int costThreshhold = Integer.MAX_VALUE;
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(costThreshhold);
    String str = "sample string";
    estimator.append(str);
    int cost = estimator.getCost();
    assertEquals(str.length(), cost);
  }

  @Test
  public void testGetCost_CompiledSizeEstimator_Add_IdentifierCount() {
    int costThreshhold = Integer.MAX_VALUE;
    InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(costThreshhold);
    String identifier = "identifier";
    int identifierCount = 5;
    for (int i = 0; i < identifierCount; i++) {
      estimator.addIdentifier(identifier);
    }
    int cost = estimator.getCost();
    assertEquals(identifierCount * InlineCostEstimator.ESTIMATED_IDENTIFIER_COST, cost);
  }
}