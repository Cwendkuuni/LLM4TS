import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x*x - 4;
      }
    };
    
    double x0 = 1.0;
    double f0 = function.valueAt(x0);
    double x1 = 3.0;
    double f1 = function.valueAt(x1);
    
    boolean result = solver.findRoot(function, new ConvergenceChecker(), 100, x0, f0, x1, f1);
    
    assertTrue(result);
    assertEquals(2.0, solver.getRoot(), 0.0001);
  }
  
  @Test
  public void testFindRoot_NoRootInInterval() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x*x + 4;
      }
    };
    
    double x0 = 1.0;
    double f0 = function.valueAt(x0);
    double x1 = 3.0;
    double f1 = function.valueAt(x1);
    
    boolean result = solver.findRoot(function, new ConvergenceChecker(), 100, x0, f0, x1, f1);
    
    assertFalse(result);
  }
  
  @Test(expected = FunctionException.class)
  public void testFindRoot_FunctionException() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        throw new FunctionException("Function not implemented");
      }
    };
    
    double x0 = 1.0;
    double f0 = function.valueAt(x0);
    double x1 = 3.0;
    double f1 = function.valueAt(x1);
    
    solver.findRoot(function, new ConvergenceChecker(), 100, x0, f0, x1, f1);
  }

}