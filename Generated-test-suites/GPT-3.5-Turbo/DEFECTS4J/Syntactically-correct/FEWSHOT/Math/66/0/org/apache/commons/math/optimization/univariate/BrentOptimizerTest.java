import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.analysis.UnivariateRealFunction;

public class BrentOptimizerTest {
  
  @Test
  public void testConstructSolver() {
    BrentOptimizer solver = new BrentOptimizer();
  
    assertEquals(Integer.MAX_VALUE, solver.getMaxEvaluations());
    assertEquals(100, solver.getMaximalIterationCount());
    assertEquals(1E-10, solver.getAbsoluteAccuracy(), 0);
    assertEquals(1.0e-14, solver.getRelativeAccuracy(), 0);
  }
  
  @Test
  public void testOptimize() {
    UnivariateRealFunction function = new UnivariateRealFunction() {
        public double value(double x) {
            return x * x + 2 * x + 1;
        }
    };
    
    BrentOptimizer solver = new BrentOptimizer();
    double result = solver.optimize(function, GoalType.MINIMIZE, -10, 10);
    
    assertEquals(-1.0, result, 0);
  }
  
  @Test(expected = NotStrictlyPositiveException.class)
  public void testOptimizeWithNegativeRelativeAccuracy() {
    UnivariateRealFunction function = new UnivariateRealFunction() {
        public double value(double x) {
            return x * x + 2 * x + 1;
        }
    };
    
    BrentOptimizer solver = new BrentOptimizer();
    solver.setRelativeAccuracy(-0.5);
    solver.optimize(function, GoalType.MINIMIZE, -10, 10);
  }
  
  @Test(expected = NotStrictlyPositiveException.class)
  public void testOptimizeWithNegativeAbsoluteAccuracy() {
    UnivariateRealFunction function = new UnivariateRealFunction() {
        public double value(double x) {
            return x * x + 2 * x + 1;
        }
    };
    
    BrentOptimizer solver = new BrentOptimizer();
    solver.setAbsoluteAccuracy(-0.1);
    solver.optimize(function, GoalType.MINIMIZE, -10, 10);
  }
  
  @Test(expected = MaxIterationsExceededException.class)
  public void testOptimizeWithMaxIterationsExceeded() {
    UnivariateRealFunction function = new UnivariateRealFunction() {
        public double value(double x) {
            return x * x + 2 * x + 1;
        }
    };
    
    BrentOptimizer solver = new BrentOptimizer();
    solver.setMaximalIterationCount(5);
    solver.optimize(function, GoalType.MINIMIZE, -10, 10);
  }
}