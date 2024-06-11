import static org.junit.Assert.*;
import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost_rootIsNull_returnZero() {
    int result = InlineCostEstimator.getCost(null);
    assertEquals(0, result);
  }
  
  @Test
  public void testGetCost_emptyRoot_returnZero() {
    Node root = new Node();
    int result = InlineCostEstimator.getCost(root);
    assertEquals(0, result);
  }
  
  @Test
  public void testGetCost_singleNode_returnNodeLength() {
    Node root = new Node();
    String code = "sampleCode";
    root.setCode(code);
    int result = InlineCostEstimator.getCost(root);
    assertEquals(code.length(), result);
  }
  
  @Test
  public void testGetCost_largeNode_returnNodeLength() {
    Node root = new Node();
    String code = "this is a very large code snippet";
    root.setCode(code);
    int result = InlineCostEstimator.getCost(root);
    assertEquals(code.length(), result);
  }
  
  @Test
  public void testGetCost_thresholdExceeded_returnThresholdValue() {
    Node root = new Node();
    String code = "sampleCode";
    root.setCode(code);
    int result = InlineCostEstimator.getCost(root, 5);
    assertEquals(5, result);
  }
  
  @Test
  public void testGetCost_thresholdExceededWithLargeNode_returnThresholdValue() {
    Node root = new Node();
    String code = "this is a very large code snippet";
    root.setCode(code);
    int result = InlineCostEstimator.getCost(root, 5);
    assertEquals(5, result);
  }
  
  @Test
  public void testGetCost_thresholdNotExceeded_returnNodeLength() {
    Node root = new Node();
    String code = "sampleCode";
    root.setCode(code);
    int result = InlineCostEstimator.getCost(root, 20);
    assertEquals(code.length(), result);
  }
  
  @Test
  public void testGetCost_thresholdNotExceededWithLargeNode_returnNodeLength() {
    Node root = new Node();
    String code = "this is a very large code snippet";
    root.setCode(code);
    int result = InlineCostEstimator.getCost(root, 50);
    assertEquals(code.length(), result);
  }

}