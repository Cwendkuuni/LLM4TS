import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

   @Test
   public void testFindRoot() {
      BrentSolver solver = new BrentSolver();
      ComputableFunction function = new ComputableFunction() {
         public double valueAt(double x) {
            return x * x - 4; // x^2 - 4
         }
      };
      ConvergenceChecker checker = new ConvergenceChecker() {
         public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            if (xHigh - xLow < 0.001) {
               return ConvergenceChecker.HIGH;
            } else if (xHigh - xLow > 1000) {
               return ConvergenceChecker.LOW;
            } else {
               return ConvergenceChecker.NO_CONVERGENCE;
            }
         }
      };
      int maxIter = 100;
      double x0 = -5;
      double f0 = -21; // (-5)^2 - 4
      double x1 = 5;
      double f1 = 21;  // 5^2 - 4

      boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

      assertTrue(result);
      assertEquals(2.000244140625, solver.getRoot(), 0.000001);
   }

   @Test
   public void testFindRootWithLargeDifference() {
      BrentSolver solver = new BrentSolver();
      ComputableFunction function = new ComputableFunction() {
         public double valueAt(double x) {
            return x * x - 4; // x^2 - 4
         }
      };
      ConvergenceChecker checker = new ConvergenceChecker() {
         public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            if (xHigh - xLow < 0.001) {
               return ConvergenceChecker.HIGH;
            } else if (xHigh - xLow > 1000) {
               return ConvergenceChecker.LOW;
            } else {
               return ConvergenceChecker.NO_CONVERGENCE;
            }
         }
      };
      int maxIter = 100;
      double x0 = -100000;
      double f0 = 9999999996.0; // (-100000)^2 - 4
      double x1 = 100000;
      double f1 = 9999999996.0;  // 100000^2 - 4

      boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

      assertTrue(result);
      assertEquals(2.000244140625, solver.getRoot(), 0.000001);
   }

   @Test
   public void testFindRootWithNoConvergence() {
      BrentSolver solver = new BrentSolver();
      ComputableFunction function = new ComputableFunction() {
         public double valueAt(double x) {
            return x * x - 1; // x^2 - 1
         }
      };
      ConvergenceChecker checker = new ConvergenceChecker() {
         public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            if (xHigh - xLow < 0.001) {
               return ConvergenceChecker.HIGH;
            } else if (xHigh - xLow > 1000) {
               return ConvergenceChecker.LOW;
            } else {
               return ConvergenceChecker.NO_CONVERGENCE;
            }
         }
      };
      int maxIter = 100;
      double x0 = -5;
      double f0 = -24; // (-5)^2 - 1
      double x1 = 5;
      double f1 = 24;  // 5^2 - 1

      boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

      assertFalse(result);
   }

   @Test
   public void testFindRootWithLinearInterpolation() {
      BrentSolver solver = new BrentSolver();
      ComputableFunction function = new ComputableFunction() {
         public double valueAt(double x) {
            return x * x - 10; // x^2 - 10
         }
      };
      ConvergenceChecker checker = new ConvergenceChecker() {
         public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            if (xHigh - xLow < 0.001) {
               return ConvergenceChecker.HIGH;
            } else if (xHigh - xLow > 1000) {
               return ConvergenceChecker.LOW;
            } else {
               return ConvergenceChecker.NO_CONVERGENCE;
            }
         }
      };
      int maxIter = 100;
      double x0 = -4;
      double f0 = -6; // (-4)^2 - 10
      double x1 = 5;
      double f1 = 15;  // 5^2 - 10

      boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

      assertTrue(result);
      assertEquals(3.162277660250604, solver.getRoot(), 0.000001);
   }
}