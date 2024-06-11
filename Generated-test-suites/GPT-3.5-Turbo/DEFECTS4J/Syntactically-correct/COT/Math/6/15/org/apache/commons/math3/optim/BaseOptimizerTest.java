
import static org.junit.Assert.*;
import org.junit.Test;

public class BaseOptimizerTest {

  @Test
  public void testGetMaxEvaluations() {
    BaseOptimizer optimizer = new BaseOptimizer();
    int result = optimizer.getMaxEvaluations();
    assertEquals(0, result);
  }

  @Test
  public void testGetEvaluations() {
    BaseOptimizer optimizer = new BaseOptimizer();
    int result = optimizer.getEvaluations();
    assertEquals(0, result);
  }

  @Test
  public void testGetConvergenceChecker() {
    BaseOptimizer optimizer = new BaseOptimizer();
    ConvergenceChecker<PAIR> result = optimizer.getConvergenceChecker();
    assertNotNull(result);
  }
}
