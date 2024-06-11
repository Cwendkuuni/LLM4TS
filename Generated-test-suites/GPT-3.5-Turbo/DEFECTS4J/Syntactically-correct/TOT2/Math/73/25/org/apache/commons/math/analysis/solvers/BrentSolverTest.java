import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

    @Test
    public void testFindRoot() {
        BrentSolver brentSolver = new BrentSolver();

        // Define a simple linear function: f(x) = x
        ComputableFunction linearFunction = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                return x;
            }
        };

        // Define a convergence checker that always converges at the low bound
        ConvergenceChecker convergenceChecker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                return ConvergenceChecker.LOW;
            }
        };

        // Test finding the root of the linear function within the interval [0, 1]
        boolean rootFound = brentSolver.findRoot(linearFunction, convergenceChecker, 100, 0.0, 0.0, 1.0, 1.0);
        
        assertEquals(true, rootFound);
        assertEquals(0.0, brentSolver.getRoot(), 1e-10);
    }

    // Add additional test cases for other methods in BrentSolver class

}