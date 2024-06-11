import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testBrentSolverConstructor() {
    BrentSolver solver = new BrentSolver();
    assertNotNull(solver);
  }

  @Test
  public void testFindRoot() {
    BrentSolver solver = new BrentSolver();
    double[] interval = {0.0, 1.0};
    double root = solver.findRoot(new ComputableFunction() {
        public double valueAt(double x) {
            return x * x - 4;
        }
    }, new ConvergenceChecker(), 100, interval[0], interval[0] * interval[0] - 4, interval[1], interval[1] * interval[1] - 4);
    
    assertEquals(2.0, root, 0.001);
  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    double root = 10.0;
    solver.findRoot(new ComputableFunction() {
        public double valueAt(double x) {
            return x * x - 100;
        }
    }, new ConvergenceChecker(), 100, 0, -100, root, root * root - 100);
    
    assertEquals(root, solver.getRoot(), 0.001);
  }

  @Test
  public void testExceptionHandling() {
    BrentSolver solver = new BrentSolver();
    try {
      solver.findRoot(null, new ConvergenceChecker(), 100, 0, -100, 1, 1);
      fail("Expected FunctionException not thrown");
    } catch (FunctionException e) {
      // Expected exception
    }
  }
}