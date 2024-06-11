import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {
  
  private static BrentSolver solver;
  
  @BeforeClass
  public static void setUp() {
    solver = new BrentSolver();
  }
  
  @Test
  public void testFindRoot() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 2;
      }
    };
    
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(xHigh - xLow) < 1e-6) {
          return ConvergenceChecker.LOW;
        }
        return ConvergenceChecker.NONE;
      }
    };
    
    boolean rootFound = solver.findRoot(function, checker, 100, 0, -2, 2, 2);
    assertTrue(rootFound);
    assertEquals(solver.getRoot(), Math.sqrt(2), 1e-6);
  }
  
  @Test
  public void testGetRoot() {
    double root = 3.14;
    solver.setRoot(root);
    assertEquals(solver.getRoot(), root, 0);
  }
  
  @Test
  public void testSetRoot() {
    double root = 1.41;
    solver.setRoot(root);
    assertEquals(solver.getRoot(), root, 0);
  }
  
}