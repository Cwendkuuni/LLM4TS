import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.PointValuePair;
import org.junit.Test;

public class BaseOptimizerTest {
  @Test
  public void testGetMaxEvaluations() {
    BaseOptimizer<Object> optimizer = new BaseOptimizer<Object>() {
      @Override
      public int getMaxEvaluations() {
        return 100;
      }
    };
    assertEquals(100, optimizer.getMaxEvaluations());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeMaxEvaluations() {
    BaseOptimizer<Object> optimizer = new BaseOptimizer<Object>() {
      @Override
      public int getMaxEvaluations() {
        return -5;
      }
    };
    optimizer.getMaxEvaluations();
  }

  @Test(expected = NullPointerException.class)
  public void testNullConvergenceChecker() {
    BaseOptimizer<Object> optimizer = new BaseOptimizer<Object>() {
      @Override
      public ConvergenceChecker<Object> getConvergenceChecker() {
        return null;
      }
    };
    optimizer.getConvergenceChecker();
  }

  @Test
  public void testGetEvaluationsBeforeOptimization() {
    BaseOptimizer<Object> optimizer = new BaseOptimizer<Object>() {
      private int evaluations = 0;

      @Override
      public int getEvaluations() {
        return evaluations;
      }

      @Override
      public void optimize(PointValuePair initialGuess) {}
    };
    assertEquals(0, optimizer.getEvaluations());
    optimizer.optimize(new PointValuePair(new Point2D(), new PointValue(0)));
    assertEquals(1, optimizer.getEvaluations());
  }

  @Test
  public void testGetEvaluationsAfterOptimization() {
    BaseOptimizer<Object> optimizer = new BaseOptimizer<Object>() {
      private int evaluations = 0;

      @Override
      public int getEvaluations() {
        return evaluations;
      }

      @Override
      public void optimize(PointValuePair initialGuess) {}
    };
    optimizer.optimize(new PointValuePair(new Point2D(), new PointValue(0)));
    assertEquals(1, optimizer.getEvaluations());
  }
}