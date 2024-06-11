import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class BaseOptimizerTest {
  
  private BaseOptimizer<PAIR> optimizer;
  
  @Test
  public void testGetMaxEvaluations() {
    optimizer = new BaseOptimizerImpl<>();
    assertEquals(0, optimizer.getMaxEvaluations());
  }
  
  @Test
  public void testGetEvaluations() {
    optimizer = new BaseOptimizerImpl<>();
    assertEquals(0, optimizer.getEvaluations());
  }
  
  @Test
  public void testGetConvergenceChecker() {
    optimizer = new BaseOptimizerImpl<>();
    assertNotNull(optimizer.getConvergenceChecker());
  }
  
  @Test
  public void testGetMaxEvaluations_CustomValue() {
    final int maxEvaluations = 100;
    optimizer = new BaseOptimizerImpl<>(maxEvaluations);
    assertEquals(maxEvaluations, optimizer.getMaxEvaluations());
  }
  
  @Test
  public void testGetEvaluations_AfterOptimize() {
    optimizer = new BaseOptimizerImpl<>();
    optimizer.optimize();
    assertNotEquals(0, optimizer.getEvaluations());
  }
  
  @Test
  public void testGetConvergenceChecker_CustomChecker() {
    final ConvergenceChecker<PAIR> checker = new CustomConvergenceChecker<>();
    optimizer = new BaseOptimizerImpl<>(checker);
    assertEquals(checker, optimizer.getConvergenceChecker());
  }
  
  @Test(expected = NullPointerException.class)
  public void testGetConvergenceChecker_NullChecker() {
    optimizer = new BaseOptimizerImpl<>(null);
    optimizer.getConvergenceChecker();
  }
  
  // Additional test cases for edge cases and error scenarios
  
  @Test(expected = UnsupportedOperationException.class)
  public void testUnsupportedGetMaxEvaluations() {
    optimizer = new BaseOptimizerImpl<>();
    optimizer.getMaxEvaluations();
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testUnsupportedGetEvaluations() {
    optimizer = new BaseOptimizerImpl<>();
    optimizer.getEvaluations();
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testUnsupportedGetConvergenceChecker() {
    optimizer = new BaseOptimizerImpl<>();
    optimizer.getConvergenceChecker();
  }
  
}