import org.junit.*;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

  @Test
  public void testDefaultConstructor() {
    BrentOptimizer optimizer = new BrentOptimizer();
    assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
    assertEquals(100, optimizer.getMaximalIterationCount());
    assertEquals(1E-10, optimizer.getAbsoluteAccuracy(), 0);
    assertEquals(1.0e-14, optimizer.getRelativeAccuracy(), 0);
  }

  @Test
  public void testOptimize() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - x;
      }
    };
    BrentOptimizer optimizer = new BrentOptimizer();
    try {
      double result = optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 1.0);
      assertEquals(0.5, result, 1e-6);
    } catch (Exception e) {
      fail("Exception thrown: " + e.getMessage());
    }
  }
}