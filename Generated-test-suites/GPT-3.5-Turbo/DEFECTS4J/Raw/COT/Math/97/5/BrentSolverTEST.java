import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.BrentSolver;

public class BrentSolverTest {

    @Test
    public void testFindRoot() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                // TODO: implement function here
                return 0;
            }
        };
        
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                // TODO: implement convergence checker here
                return 0;
            }
        };

        BrentSolver solver = new BrentSolver();

        assertTrue(solver.findRoot(function, checker, 100, 0, 0, 1, 1));
    }

    @Test
    public void testGetRoot() {
        BrentSolver solver = new BrentSolver();
        
        solver.getRoot();
        // TODO: add assertions
    }
}


// Test cases for BrentSolver class