import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
  
    // Test with a simple linear function that intersects x-axis at x = 1
    ComputableFunction linearFunction = new ComputableFunction() {
      public double valueAt(double x) {
        return x - 1;
      }
    };
  
    ConvergenceChecker convergenceChecker = new ConvergenceChecker() {
      public int converged(double x1, double f1, double x2, double f2) {
        if (Math.abs(f1) < epsilon) {
          return ConvergenceChecker.LOW;
        }
        if (Math.abs(f2) < epsilon) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NONE;
      }
    };
  
    BrentSolver solver = new BrentSolver();
    boolean rootFound = solver.findRoot(linearFunction, convergenceChecker, 100, 0, -1, 2, 1);
  
    assertTrue(rootFound);
    assertEquals(1.0, solver.getRoot(), epsilon);
  
  }
  
  @Test
  public void testFindRootWithMaxIterationsReached() throws FunctionException {
  
    // Test with a function that does not converge within the maximum number of iterations
    ComputableFunction nonConvergingFunction = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 2;
      }
    };
  
    ConvergenceChecker convergenceChecker = new ConvergenceChecker() {
      public int converged(double x1, double f1, double x2, double f2) {
        if (Math.abs(f1) < epsilon) {
          return ConvergenceChecker.LOW;
        }
        if (Math.abs(f2) < epsilon) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NONE;
      }
    };
  
    BrentSolver solver = new BrentSolver();
    boolean rootFound = solver.findRoot(nonConvergingFunction, convergenceChecker, 10, 0, -1, 2, 1);
  
    assertFalse(rootFound);
  
  }
  
  @Test
  public void testGetRoot() throws FunctionException {
  
    ComputableFunction linearFunction = new ComputableFunction() {
      public double valueAt(double x) {
        return x - 1;
      }
    };
  
    ConvergenceChecker convergenceChecker = new ConvergenceChecker() {
      public int converged(double x1, double f1, double x2, double f2) {
        if (Math.abs(f1) < epsilon) {
          return ConvergenceChecker.LOW;
        }
        if (Math.abs(f2) < epsilon) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NONE;
      }
    };
  
    BrentSolver solver = new BrentSolver();
    solver.findRoot(linearFunction, convergenceChecker, 100, 0, -1, 2, 1);
  
    assertEquals(1.0, solver.getRoot(), epsilon);
  
  }
  
}