import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverFactory;
import static org.junit.Assert.*;

public class UnivariateRealSolverUtilsTest {

  @Test
  public void testSolve() {
      UnivariateRealFunction f = new TestFunction();
      double x0 = 0.0;
      double x1 = 1.0;
      try {
          double result = UnivariateRealSolverUtils.solve(f, x0, x1);
          assertEquals(0.5, result, 0.0001);
      } catch (ConvergenceException e) {
          fail("ConvergenceException thrown");
      } catch (FunctionEvaluationException e) {
          fail("FunctionEvaluationException thrown");
      }
  }

  @Test
  public void testSolveWithAccuracy() {
      UnivariateRealFunction f = new TestFunction();
      double x0 = 0.0;
      double x1 = 1.0;
      double absoluteAccuracy = 0.001;
      try {
          double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
          assertEquals(0.5, result, 0.001);
      } catch (ConvergenceException e) {
          fail("ConvergenceException thrown");
      } catch (FunctionEvaluationException e) {
          fail("FunctionEvaluationException thrown");
      }
  }

  @Test
  public void testBracket() {
      UnivariateRealFunction f = new TestFunction();
      double initial = 0.5;
      double lowerBound = 0.0;
      double upperBound = 1.0;
      try {
          double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
          assertEquals(0.0, result[0], 0.0001);
          assertEquals(1.0, result[1], 0.0001);
      } catch (ConvergenceException e) {
          fail("ConvergenceException thrown");
      } catch (FunctionEvaluationException e) {
          fail("FunctionEvaluationException thrown");
      }
  }

  @Test
  public void testMidpoint() {
      double a = 0.0;
      double b = 1.0;
      double result = UnivariateRealSolverUtils.midpoint(a, b);
      assertEquals(0.5, result, 0.0001);
  }

  private static class TestFunction implements UnivariateRealFunction {
      public double value(double x) {
          return x - 0.5;
      }
  }
}