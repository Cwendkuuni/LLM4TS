import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

   @Test
   public void testFindRoot() {
      BrentSolver solver = new BrentSolver();
      ComputableFunction function = new ComputableFunction() {
         public double valueAt(double x) {
            return x * x - 4;
         }
      };
      ConvergenceChecker checker = new ConvergenceChecker();

      boolean result = solver.findRoot(function, checker, 100, 1, -3, 3, 7);
      assertTrue(result);
      assertEquals(2, solver.getRoot(), 0.001);
   }

}