import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    double[] coefficients = {1.0, -3.0, 2.0};
    ComputableFunction function = new PolynomialFunction(coefficients);
    ConvergenceChecker checker = new RelativeDifferenceChecker();
    int maxIter = 100;
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 2.0;
    double f1 = function.valueAt(x1);
   
    boolean success = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    
    assertEquals(true, success);
    assertEquals(1.0, solver.getRoot(), 1e-6);
  }
  
  @Test
  public void testFindRoot_invalidInterval() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    double[] coefficients = {1.0, -3.0, 2.0};
    ComputableFunction function = new PolynomialFunction(coefficients);
    ConvergenceChecker checker = new RelativeDifferenceChecker();
    int maxIter = 100;
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 1.0;
    double f1 = function.valueAt(x1);
   
    boolean success = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    
    assertEquals(false, success);
  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    double root = 1.0;
    
    solver.root = root;
    
    assertEquals(root, solver.getRoot(), 1e-6);
  }

}