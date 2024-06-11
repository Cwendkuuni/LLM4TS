import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  private BrentSolver solver;
  
  @Before
  public void setUp() {
    solver = new BrentSolver();
  }
  
  @Test
  public void testFindRoot() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker();
    int maxIter = 100;
    double x0 = 0;
    double f0 = function.valueAt(x0);
    double x1 = 2;
    double f1 = function.valueAt(x1);
    
    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    
    assertTrue(rootFound);
    assertEquals(2.0, solver.getRoot(), 1e-6);
  }
  
  // Additional test cases for potential edge cases and exceptions
  
  @Test
  public void testZeroFunction() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return 0;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker();
    int maxIter = 100;
    double x0 = 0;
    double f0 = function.valueAt(x0);
    double x1 = 2;
    double f1 = function.valueAt(x1);
    
    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    
    assertTrue(rootFound);
    assertEquals(0.0, solver.getRoot(), 1e-6);
  }
  
  @Test
  public void testNoRoot() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x + 1;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker();
    int maxIter = 100;
    double x0 = 0;
    double f0 = function.valueAt(x0);
    double x1 = 2;
    double f1 = function.valueAt(x1);
    
    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    
    assertTrue(!rootFound);
  }

}

//