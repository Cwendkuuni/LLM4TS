import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class BaseOptimizerTest {
  
  private BaseOptimizer baseOptimizer;
  
  @Before
  public void setUp() {
    baseOptimizer = new BaseOptimizer();
  }
  
  @Test
  public void testGetMaxEvaluations() {
    int maxEvaluations = baseOptimizer.getMaxEvaluations();
    assertEquals(0, maxEvaluations);
  }
  
  @Test
  public void testGetEvaluations() {
    int evaluations = baseOptimizer.getEvaluations();
    assertEquals(0, evaluations);
  }
  
  @Test
  public void testGetConvergenceChecker() {
    ConvergenceChecker<PAIR> convergenceChecker = baseOptimizer.getConvergenceChecker();
    // Perform appropriate checks
  }
  
}