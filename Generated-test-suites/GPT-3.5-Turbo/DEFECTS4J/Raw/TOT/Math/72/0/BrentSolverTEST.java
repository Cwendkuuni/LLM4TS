import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  //Test case for the findRoot method
  @Test
  public void testFindRoot() {
    //create an instance of the BrentSolver class
    BrentSolver solver = new BrentSolver();

    //create a dummy function that returns x^2
    ComputableFunction function = new ComputableFunction() {
        public double valueAt(double x) throws FunctionException {
          return x * x;
        }
    };
    
    //create a simple convergence checker that returns true when the function values are close enough
    ConvergenceChecker checker = new ConvergenceChecker() {
        public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            if (Math.abs(fLow - fHigh) < 0.001) {
                return ConvergenceChecker.LOW;
            }
            return ConvergenceChecker.NONE;
        }
    };

    //test with a simple interval that is known to contain a root
    assertTrue("Root not found", solver.findRoot(function, checker, 1000, -10, 100, 10, 100));
    assertEquals("Incorrect root value", 0, solver.getRoot(), 0.001);

    //test with an interval that does not contain a root
    assertFalse("Found root when there is none", solver.findRoot(function, checker, 1000, -10, 100, -1, 101));

    //test with an interval that contains multiple roots
    assertTrue("Root not found", solver.findRoot(function, checker, 1000, -2, 0, 2, 0));
    assertEquals("Incorrect root value", 0, solver.getRoot(), 0.001);
    
    //test with an interval that contains an oscillation
    assertTrue("Root not found", solver.findRoot(function, checker, 1000, -2, 4, 2, 4));
    assertEquals("Incorrect root value", 2, solver.getRoot(), 0.001);

    //test with an interval that has a very small length
    assertTrue("Root not found", solver.findRoot(function, checker, 1000, 0, 0, 0.001, 0.001));
    assertEquals("Incorrect root value", 0, solver.getRoot(), 0.001);
  }

  //Test case for the getRoot method
  @Test
  public void testGetRoot() {
    //create an instance of the BrentSolver class
    BrentSolver solver = new BrentSolver();

    //test with a root that has been found
    solver.findRoot(new ComputableFunction() {
        public double valueAt(double x) throws FunctionException {
          return x * x - 4;
        }
    }, new ConvergenceChecker() {
        public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            if (Math.abs(fLow - fHigh) < 0.001) {
                return ConvergenceChecker.LOW;
            }
            return ConvergenceChecker.NONE;
        }
    }, 1000, 0, -4, 2, 0);
    assertEquals("Incorrect root value", 2, solver.getRoot(), 0.001);

    //test with no root found
    solver.findRoot(new ComputableFunction() {
        public double valueAt(double x) throws FunctionException {
          return x * x - 4;
        }
    }, new ConvergenceChecker() {
        public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            if (Math.abs(fLow - fHigh) < 0.001) {
                return ConvergenceChecker.LOW;
            }
            return ConvergenceChecker.NONE;
        }
    }, 1000, -10, -4, -5, -4);
    assertEquals("Incorrect root value", Double.NaN, solver.getRoot(), 0.001);
  }

}