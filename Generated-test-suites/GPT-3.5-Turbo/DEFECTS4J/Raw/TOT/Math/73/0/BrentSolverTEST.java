
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

   @Test
   public void testFindRoot_WhenRootIsWithinGivenInterval_ReturnsTrue() throws FunctionException {
      BrentSolver solver = new BrentSolver();
      ComputableFunction function = new ComputableFunction() {
         public double valueAt(double x) {
            return x * x - 4;
         }
      };
      ConvergenceChecker checker = new ConvergenceChecker() {
         public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            if (Math.abs(xHigh - xLow) <= 0.01) {
               return 1;
            } else {
               return -1;
            }
         }
      };
      int maxIter = 1000;
      double x0 = 0;
      double f0 = -4;
      double x1 = 10;
      double f1 = 96;

      boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

      assertTrue(result);
      assertEquals(2, solver.getRoot(), 0.01);
   }

   @Test
   public void testFindRoot_WhenRootIsNotWithinGivenInterval_ReturnsFalse() throws FunctionException {
      BrentSolver solver = new BrentSolver();
      ComputableFunction function = new ComputableFunction() {
         public double valueAt(double x) {
            return x * x - 4;
         }
      };
      ConvergenceChecker checker = new ConvergenceChecker() {
         public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            if (Math.abs(fLow) <= 0.01 || Math.abs(fHigh) <= 0.01) {
               return 1;
            } else {
               return -1;
            }
         }
      };
      int maxIter = 1000;
      double x0 = 0;
      double f0 = -4;
      double x1 = 1;
      double f1 = -4;

      boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

      assertFalse(result);
      assertTrue(Double.isNaN(solver.getRoot()));
   }

   @Test(expected = FunctionException.class)
   public void testFindRoot_WhenComputableFunctionThrowsException_ThrowsFunctionException() throws FunctionException {
      BrentSolver solver = new BrentSolver();
      ComputableFunction function = new ComputableFunction() {
         public double valueAt(double x) throws FunctionException {
            throw new FunctionException();
         }
      };
      ConvergenceChecker checker = new ConvergenceChecker() {
         public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            return -1;
         }
      };
      int maxIter = 1000;
      double x0 = 0;
      double f0 = 0;
      double x1 = 1;
      double f1 = 1;

      solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
   }
}

