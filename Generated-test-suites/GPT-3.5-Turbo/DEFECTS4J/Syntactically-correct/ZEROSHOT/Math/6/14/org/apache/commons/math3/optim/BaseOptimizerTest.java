import static org.junit.Assert.*;
import org.junit.Test;

public class BaseOptimizerTest {

  @Test
  public void testGetMaxEvaluations() {
    BaseOptimizer optimizer = new BaseOptimizerImpl();
    int maxEvaluations = optimizer.getMaxEvaluations();
    assertEquals(0, maxEvaluations);
  }

  @Test
  public void testGetEvaluations() {
    BaseOptimizer optimizer = new BaseOptimizerImpl();
    int evaluations = optimizer.getEvaluations();
    assertEquals(0, evaluations);
  }

  @Test
  public void testGetConvergenceChecker() {
    BaseOptimizer optimizer = new BaseOptimizerImpl();
    ConvergenceChecker<PAIR> convergenceChecker = optimizer.getConvergenceChecker();
    assertNotNull(convergenceChecker);
  }

}

class BaseOptimizerImpl implements BaseOptimizer<PAIR> {

  @Override
  public int getMaxEvaluations() {
    return 0;
  }

  @Override
  public int getEvaluations() {
    return 0;
  }

  @Override
  public ConvergenceChecker<PAIR> getConvergenceChecker() {
    return new ConvergenceCheckerImpl();
  }

}

class ConvergenceCheckerImpl implements ConvergenceChecker<PAIR> {

  @Override
  public boolean converged(int iteration, PAIR previous, PAIR current) {
    return true;
  }
}