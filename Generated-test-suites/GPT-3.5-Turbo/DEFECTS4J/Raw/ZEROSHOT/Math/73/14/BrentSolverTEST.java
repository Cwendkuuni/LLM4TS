import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {
  
  private BrentSolver solver;
  
  @Before
  public void setup() {
    solver = new BrentSolver();
  }
  
  @Test
  public void testFindRoot() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
        public double valueAt(double x) {
            return x * x - 4;
        }
    };
    
    ConvergenceChecker checker = new ConvergenceChecker() {
        public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            return Math.abs(fHigh) < 1e-10 ? HIGH : Math.abs(fLow) < 1e-10 ? LOW : NO_CONVERGENCE;
        }
    };
    
    int maxIter = 100;
    double x0 = 1;
    double f0 = function.valueAt(x0);
    double x1 = 3;
    double f1 = function.valueAt(x1);
    
    assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
    assertEquals(2, solver.getRoot(), 1e-10);
  }
}