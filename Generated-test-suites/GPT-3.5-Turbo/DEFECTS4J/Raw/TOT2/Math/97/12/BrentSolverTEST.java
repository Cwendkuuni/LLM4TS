import org.junit.Test;
import static org.junit.Assert.*;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

    @Test
    public void testFindRoot_WhenFunctionHasRootInInterval_ReturnsTrue() throws FunctionException {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                return x * x - 4;
            }
        };
        ConvergenceChecker checker = (xLow, fLow, xHigh, fHigh) -> {
            if (fLow == 0) {
                return ConvergenceChecker.LOW;
            } else if (fHigh == 0) {
                return ConvergenceChecker.HIGH;
            } else {
                return ConvergenceChecker.NONE;
            }
        };
        int maxIter = 100;
        double x0 = 0;
        double f0 = -4;
        double x1 = 3;
        double f1 = 5;

        assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
        assertEquals(2.0, solver.getRoot(), 1e-15);
    }

    @Test
    public void testFindRoot_WhenFunctionHasMultipleRootsInInterval_ReturnsTrueForOneRoot() throws FunctionException {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                return x * x - 4;
            }
        };
        ConvergenceChecker checker = (xLow, fLow, xHigh, fHigh) -> ConvergenceChecker.NONE;
        int maxIter = 100;
        double x0 = 0;
        double f0 = -4;
        double x1 = 3;
        double f1 = 5;

        assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
        assertEquals(2.0, solver.getRoot(), 1e-15);
    }

    @Test(expected = FunctionException.class)
    public void testFindRoot_WhenFunctionThrowsException_ThrowsFunctionException() throws FunctionException {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                throw new FunctionException("Error evaluating function");
            }
        };
        ConvergenceChecker checker = (xLow, fLow, xHigh, fHigh) -> ConvergenceChecker.NONE;
        int maxIter = 100;
        double x0 = 0;
        double f0 = -4;
        double x1 = 3;
        double f1 = 5;

        solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    }

    // Additional test cases can be added here

}