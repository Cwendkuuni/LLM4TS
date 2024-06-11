import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

   @Test
   public void testFindRoot() throws FunctionException {
      BrentSolver solver = new BrentSolver();
      
      // Test case 1 - Root exists in the interval
      ComputableFunction function1 = new ComputableFunction() {
         public double valueAt(double x) throws FunctionException {
            return x * x - 2;
         }
      };
      
      ConvergenceChecker checker1 = new ConvergenceChecker() {
         public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            if (Math.abs(fLow) < 0.001) {
               return ConvergenceChecker.LOW;
            } else if (Math.abs(fHigh) < 0.001) {
               return ConvergenceChecker.HIGH;
            } else {
               return ConvergenceChecker.NONE;
            }
         }
      };
      
      assertTrue(solver.findRoot(function1, checker1, 100, 0, -2, 2, 2));
      assertEquals(1.414, solver.getRoot(), 0.001);
      
      // Test case 2 - Root does not exist in the interval
      ComputableFunction function2 = new ComputableFunction() {
         public double valueAt(double x) throws FunctionException {
            return x * x - 2;
         }
      };
      
      ConvergenceChecker checker2 = new ConvergenceChecker() {
         public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            if (Math.abs(fLow) < 0.001) {
               return ConvergenceChecker.LOW;
            } else if (Math.abs(fHigh) < 0.001) {
               return ConvergenceChecker.HIGH;
            } else {
               return ConvergenceChecker.NONE;
            }
         }
      };
      
      assertFalse(solver.findRoot(function2, checker2, 100, 0, -2, 2, -2));
      
   }

   @Test
   public void testGetRoot() throws FunctionException {
      BrentSolver solver = new BrentSolver();
      
      ComputableFunction function = new ComputableFunction() {
         public double valueAt(double x) throws FunctionException {
            return x * x - 2;
         }
      };
      
      ConvergenceChecker checker = new ConvergenceChecker() {
         public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            if (Math.abs(fLow) < 0.001) {
               return ConvergenceChecker.LOW;
            } else if (Math.abs(fHigh) < 0.001) {
               return ConvergenceChecker.HIGH;
            } else {
               return ConvergenceChecker.NONE;
            }
         }
      };
      
      assertTrue(solver.findRoot(function, checker, 100, 0, -2, 2, 2));
      assertEquals(1.414, solver.getRoot(), 0.001);
   }

}