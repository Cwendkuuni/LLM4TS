import org.junit.Test;

public class BrentSolverTest {

   @Test
   public void testFindRoot() throws FunctionException {
      BrentSolver solver = new BrentSolver();

      // Test case 1
      double x0 = 1.0;
      double f0 = 1.0;
      double x1 = 2.0;
      double f1 = 4.0;
      ComputableFunction function = new ComputableFunction() {
         public double valueAt(double x) {
            return x * x - 3.0 * x + 2.0;
         }
      };
      ConvergenceChecker checker = new ConvergenceChecker() {
         public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            if (Math.abs(fHigh) < 1e-6) {
               return RootsFinder.HIGH;
            } else if (Math.abs(fLow) < 1e-6) {
               return RootsFinder.LOW;
            } else {
               return RootsFinder.NONE;
            }
         }
      };
      int maxIter = 100;
      boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
      assertTrue(result);
      assertEquals(1.0, solver.getRoot(), 1e-6);

      // Test case 2
      x0 = -1.0;
      f0 = 4.0;
      x1 = 0.0;
      f1 = 1.0;
      function = new ComputableFunction() {
         public double valueAt(double x) {
            return x * x + 3.0 * x + 2.0;
         }
      };
      checker = new ConvergenceChecker() {
         public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            if (Math.abs(fHigh) < 1e-6) {
               return RootsFinder.HIGH;
            } else if (Math.abs(fLow) < 1e-6) {
               return RootsFinder.LOW;
            } else {
               return RootsFinder.NONE;
            }
         }
      };
      maxIter = 100;
      result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
      assertTrue(result);
      assertEquals(-2.0, solver.getRoot(), 1e-6);

      // Test case 3
      x0 = -2.0;
      f0 = -1.0;
      x1 = -1.0;
      f1 = -3.0;
      function = new ComputableFunction() {
         public double valueAt(double x) {
            return x * x - 5.0 * x + 6.0;
         }
      };
      checker = new ConvergenceChecker() {
         public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            if (Math.abs(fHigh) < 1e-6) {
               return RootsFinder.HIGH;
            } else if (Math.abs(fLow) < 1e-6) {
               return RootsFinder.LOW;
            } else {
               return RootsFinder.NONE;
            }
         }
      };
      maxIter = 100;
      result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
      assertTrue(result);
      assertEquals(2.0, solver.getRoot(), 1e-6);
   }
}