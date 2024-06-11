import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;

public class BaseOptimizerTest {
  
  private BaseOptimizer<Object> optimizer;
  
  @Before
  public void setUp() {
    optimizer = new BaseOptimizer<Object>() {
      public int getMaxEvaluations() { return 100; }
      public int getEvaluations() { return 0; }
      public ConvergenceChecker<Object> getConvergenceChecker() { return null; }
    };
  }
  
  @Test
  public void testGetMaxEvaluations() {
    Assert.assertEquals(100, optimizer.getMaxEvaluations());
  }
  
  @Test
  public void testGetEvaluations_zero() {
    Assert.assertEquals(0, optimizer.getEvaluations());
  }
  
  @Test
  public void testGetConvergenceChecker_null() {
    Assert.assertNull(optimizer.getConvergenceChecker());
  }
  
  @Test
  public void testGetEvaluations_nonZero() {
    optimizer = new BaseOptimizer<Object>() {
      public int getMaxEvaluations() { return 0; }
      public int getEvaluations() { return 10; }
      public ConvergenceChecker<Object> getConvergenceChecker() { return null; }
    };
    
    Assert.assertEquals(10, optimizer.getEvaluations());
  }
  
  @Test
  public void testGetConvergenceChecker_notNull() {
    optimizer = new BaseOptimizer<Object>() {
      public int getMaxEvaluations() { return 0; }
      public int getEvaluations() { return 0; }
      public ConvergenceChecker<Object> getConvergenceChecker() { return new ConvergenceChecker<Object>() {
        public boolean converged(int iteration, Object previous, Object current) { return false; }
      }; }
    };
    
    Assert.assertNotNull(optimizer.getConvergenceChecker());
  }

}