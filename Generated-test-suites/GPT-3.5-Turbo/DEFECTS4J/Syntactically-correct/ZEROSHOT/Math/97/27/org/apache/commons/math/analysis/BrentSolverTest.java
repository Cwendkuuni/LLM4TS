import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

   @Test
   public void testFindRoot() {
      BrentSolver solver = new BrentSolver();
      ComputableFunction function = new ComputableFunction() {
         public double valueAt(double x) {
            return Math.sin(x);
         }
      };
      ConvergenceChecker checker = new ConvergenceChecker() {
         public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            if (Math.abs(fLow) < 1e-6) {
               return ConvergenceChecker.LOW;
            } else if (Math.abs(fHigh) < 1e-6) {
               return ConvergenceChecker.HIGH;
            } else {
               return ConvergenceChecker.NONE;
            }
         }
      };
      double x0 = 0.0;
      double f0 = function.valueAt(x0);
      double x1 = 1.0;
      double f1 = function.valueAt(x1);
      boolean result = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
      assertTrue(result);
      assertEquals(0.0, solver.getRoot(), 1e-6);
    }
}