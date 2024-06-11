import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    // Create instance
    BrentSolver solver = new BrentSolver();

    try {
      // Define input values
      double x0 = 0.0;
      double f0 = 0.0;
      double x1 = 1.0;
      double f1 = 1.0;

      // Call findRoot method
      boolean result = solver.findRoot(new ComputableFunction() {
        @Override
        public double valueAt(double x) {
          return x * x - 3;
        }
      }, new ConvergenceChecker() {

        @Override
        public int converged(double xLow, double fLow, double xHigh, double fHigh) {
          if (fLow == 0) {
            return LOW;
          } else if (fHigh == 0) {
            return HIGH;
          } else {
            return NONE;
          }
        }

      }, 100, x0, f0, x1, f1);

      // Check if a root has been found
      assertTrue(result);

      // Check the calculated root value
      assertEquals(Math.sqrt(3), solver.getRoot(), 1e-10);
    } catch (FunctionException e) {
      fail("Exception thrown");
    }
  }

  @Test
  public void testGetRoot() {
    // Create instance
    BrentSolver solver = new BrentSolver();

    // Set a root value
    solver.root = 5.0;

    // Get the root value and check it
    assertEquals(5.0, solver.getRoot(), 0.0);
  }

}