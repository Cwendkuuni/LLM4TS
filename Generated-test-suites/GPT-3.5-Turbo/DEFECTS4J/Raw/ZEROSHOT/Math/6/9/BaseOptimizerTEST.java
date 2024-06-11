import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class BaseOptimizerTest {

  @Test
  public void testGetMaxEvaluations() {
    BaseOptimizer optimizer = mock(BaseOptimizer.class);
    when(optimizer.getMaxEvaluations()).thenReturn(100);

    int result = optimizer.getMaxEvaluations();

    assertEquals(100, result);
  }

  @Test
  public void testGetEvaluations() {
    BaseOptimizer optimizer = mock(BaseOptimizer.class);
    when(optimizer.getEvaluations()).thenReturn(50);

    int result = optimizer.getEvaluations();

    assertEquals(50, result);
  }

  @Test
  public void testGetConvergenceChecker() {
    ConvergenceChecker<PAIR> convergenceChecker = mock(ConvergenceChecker.class);
    BaseOptimizer optimizer = mock(BaseOptimizer.class);
    when(optimizer.getConvergenceChecker()).thenReturn(convergenceChecker);

    ConvergenceChecker<PAIR> result = optimizer.getConvergenceChecker();

    assertEquals(convergenceChecker, result);
  }
}