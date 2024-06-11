import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

    // Test the constructor
    @Test
    public void testConstructor() {
        BrentSolver solver = new BrentSolver();
        assertEquals(Double.NaN, solver.getRoot(), 0.0001);
    }

    // Test the findRoot() method with a valid interval
    @Test
    public void testFindRoot_ValidInterval() {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new TestFunction();
        ConvergenceChecker checker = new TestConvergenceChecker();
        double x0 = -1.0;
        double f0 = -3.0;
        double x1 = 1.0;
        double f1 = 3.0;
        try {
            assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
            assertEquals(0.0, solver.getRoot(), 0.0001);
        } catch (FunctionException e) {
            fail("FunctionException thrown");
        }
    }

    // Test the findRoot() method with an invalid interval
    @Test
    public void testFindRoot_InvalidInterval() {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new TestFunction();
        ConvergenceChecker checker = new TestConvergenceChecker();
        double x0 = -1.0;
        double f0 = -3.0;
        double x1 = -2.0;
        double f1 = 3.0;
        try {
            assertFalse(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
        } catch (FunctionException e) {
            fail("FunctionException thrown");
        }
    }

    // Test the findRoot() method with a linear function
    @Test
    public void testFindRoot_LinearFunction() {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new LinearFunction();
        ConvergenceChecker checker = new TestConvergenceChecker();
        double x0 = -1.0;
        double f0 = -3.0;
        double x1 = 1.0;
        double f1 = 3.0;
        try {
            assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
            assertEquals(0.0, solver.getRoot(), 0.0001);
        } catch (FunctionException e) {
            fail("FunctionException thrown");
        }
    }

    // Test the findRoot() method with a constant function
    @Test
    public void testFindRoot_ConstantFunction() {
        BrentSolver solver = new BrentSolver();
        ComputableFunction function = new ConstantFunction();
        ConvergenceChecker checker = new TestConvergenceChecker();
        double x0 = -1.0;
        double f0 = 3.0;
        double x1 = 1.0;
        double f1 = 3.0;
        try {
            assertFalse(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
        } catch (FunctionException e) {
            fail("FunctionException thrown");
        }
    }

    // Custom test function implementing the ComputableFunction interface
    private class TestFunction implements ComputableFunction {
        public double valueAt(double x) throws FunctionException {
            return x * x - 4;
        }
    }

    // Custom test function implementing the ComputableFunction interface that represents a linear function
    private class LinearFunction implements ComputableFunction {
        public double valueAt(double x) throws FunctionException {
            return 2 * x + 1;
        }
    }

    // Custom test function implementing the ComputableFunction interface that represents a constant function
    private class ConstantFunction implements ComputableFunction {
        public double valueAt(double x) throws FunctionException {
            return 3.0;
        }
    }

    // Custom convergence checker implementing the ConvergenceChecker interface
    private class TestConvergenceChecker implements ConvergenceChecker {
        public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            if (Math.abs(fLow) < 0.0001) {
                return ConvergenceChecker.LOW;
            } else if (Math.abs(fHigh) < 0.0001) {
                return ConvergenceChecker.HIGH;
            } else {
                return ConvergenceChecker.NONE;
            }
        }
    }

}