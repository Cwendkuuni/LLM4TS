import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

import static org.junit.Assert.*;

public class BrentSolverTest {

   @Test
   public void testFindRoot() throws FunctionException {
   
      BrentSolver solver = new BrentSolver();
      
      ComputableFunction function = new ComputableFunction() {
         public double valueAt(double x) throws FunctionException {
            return x * x - 4;
         }
      };
      
      ConvergenceChecker checker = new ConvergenceChecker() {
         public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            if (Math.abs(fHigh) < 1.0e-10) {
               return HIGH;
            } else if (Math.abs(fLow) < 1.0e-10) {
               return LOW;
            } else {
               return NOT_CONVERGED;
            }
         }
      };
      
      double x0 = -2.0;
      double f0 = -4.0;
      double x1 = 2.0;
      double f1 = 0.0;
      int maxIter = 100;
      
      boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
      
      assertTrue(result);
      assertEquals(2.0, solver.getRoot(), 1.0e-10);
   }

   @Test
   public void testGetRoot() {
   
      BrentSolver solver = new BrentSolver();
      
      double root = solver.getRoot();
      
      assertEquals(Double.NaN, root, 0.0);
   }

}