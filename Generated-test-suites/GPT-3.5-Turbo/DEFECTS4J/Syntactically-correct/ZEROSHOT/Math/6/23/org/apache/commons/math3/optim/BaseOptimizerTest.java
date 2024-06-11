import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest {
  
  private BaseOptimizer<Object> optimizer;
  
  @Before
  public void setUp() {
    optimizer = new BaseOptimizer<Object>() {
      public int getMaxEvaluations() {
        return 0;
      }
      public int getEvaluations() {
        return 0;
      }
      public ConvergenceChecker<Object> getConvergenceChecker() {
        return null;
      }
    };
  }
  
  @After
  public void tearDown() {
    optimizer = null;
  }
  
  @Test
  public void testGetMaxEvaluations() {
    int maxEvaluations = optimizer.getMaxEvaluations();
    assertEquals(0, maxEvaluations);
  }
  
  @Test
  public void testGetEvaluations() {
    int evaluations = optimizer.getEvaluations();
    assertEquals(0, evaluations);
  }
  
  @Test
  public void testGetConvergenceChecker() {
    ConvergenceChecker<Object> convergenceChecker = optimizer.getConvergenceChecker();
    assertNull(convergenceChecker);
  }
  
}