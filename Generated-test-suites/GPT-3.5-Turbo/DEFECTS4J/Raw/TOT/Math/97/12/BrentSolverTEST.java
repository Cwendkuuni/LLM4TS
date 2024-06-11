import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

import static org.junit.Assert.*;

public class BrentSolverTest {

    private BrentSolver brentSolver;
    private ComputableFunction function;

    @Before
    public void setup() {
        brentSolver = new BrentSolver();
        function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                // Custom function implementation
                return x * x - 4 * x + 3;
            }
        };
    }

    @Test
    public void testFindRoot_TypicalCase() throws FunctionException {
        double a = 0.0;
        double fa = function.valueAt(a);
        double b = 5.0;
        double fb = function.valueAt(b);
        int maxIter = 100;

        boolean rootFound = brentSolver.findRoot(function, new ConvergenceChecker(), maxIter, a, fa, b, fb);

        assertTrue(rootFound);
        assertEquals(1.0, brentSolver.getRoot(), 1e-10);
    }

    @Test
    public void testFindRoot_EdgeCase_IntervalContainingRoot() throws FunctionException {
        double a = 1.0;
        double fa = function.valueAt(a);
        double b = 3.0;
        double fb = function.valueAt(b);
        int maxIter = 100;

        boolean rootFound = brentSolver.findRoot(function, new ConvergenceChecker(), maxIter, a, fa, b, fb);

        assertTrue(rootFound);
        assertEquals(2.0, brentSolver.getRoot(), 1e-10);
    }

    @Test
    public void testFindRoot_EdgeCase_IntervalNotContainingRoot() throws FunctionException {
        double a = -1.0;
        double fa = function.valueAt(a);
        double b = 1.0;
        double fb = function.valueAt(b);
        int maxIter = 100;

        boolean rootFound = brentSolver.findRoot(function, new ConvergenceChecker(), maxIter, a, fa, b, fb);

        assertFalse(rootFound);
    }

    @Test(expected = FunctionException.class)
    public void testFindRoot_ErrorCase_FunctionException() throws FunctionException {
        double a = 0.0;
        double fa = function.valueAt(a);
        double b = 5.0;
        double fb = function.valueAt(b);
        int maxIter = 100;

        ComputableFunction invalidFunction = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                // Function always throws an exception
                throw new FunctionException("Error evaluating function");
            }
        };

        brentSolver.findRoot(invalidFunction, new ConvergenceChecker(), maxIter, a, fa, b, fb);
    }

    @Test(timeout = 1000)
    public void testFindRoot_Performance() throws FunctionException {
        double a = 0.0;
        double fa = function.valueAt(a);
        double b = 5.0;
        double fb = function.valueAt(b);
        int maxIter = 1000000;

        brentSolver.findRoot(function, new ConvergenceChecker(), maxIter, a, fa, b, fb);
    }

}