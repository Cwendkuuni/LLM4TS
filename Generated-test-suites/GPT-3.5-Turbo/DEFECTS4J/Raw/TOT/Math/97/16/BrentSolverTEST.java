import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

    private BrentSolver solver;

    @Before
    public void setup() {
        solver = new BrentSolver();
    }

    // Test findRoot method with a simple linear function
    @Test
    public void testFindRootWithLinearFunction() throws FunctionException {
        // Create a linear function: f(x) = x + 1
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x + 1;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker();

        // Find root between -2 and 2
        double x0 = -2;
        double f0 = function.valueAt(x0);
        double x1 = 2;
        double f1 = function.valueAt(x1);
        boolean rootFound = solver.findRoot(function, checker, 1000, x0, f0, x1, f1);

        // Check if root is found and within the expected range
        assertTrue(rootFound);
        double root = solver.getRoot();
        assertTrue(root > -1 && root < 1);
    }

    // Test findRoot method with a constant function
    @Test
    public void testFindRootWithConstantFunction() throws FunctionException {
        // Create a constant function: f(x) = 5
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return 5;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker();

        // Find root between -2 and 2
        double x0 = -2;
        double f0 = function.valueAt(x0);
        double x1 = 2;
        double f1 = function.valueAt(x1);
        boolean rootFound = solver.findRoot(function, checker, 1000, x0, f0, x1, f1);

        // Check if root is found and within the expected range
        assertFalse(rootFound);
    }

    // Test findRoot method with a quadratic function
    @Test
    public void testFindRootWithQuadraticFunction() throws FunctionException {
        // Create a quadratic function: f(x) = x^2 - 4
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker();

        // Find root between -2 and 2
        double x0 = -2;
        double f0 = function.valueAt(x0);
        double x1 = 2;
        double f1 = function.valueAt(x1);
        boolean rootFound = solver.findRoot(function, checker, 1000, x0, f0, x1, f1);

        // Check if root is found and within the expected range
        assertTrue(rootFound);
        double root = solver.getRoot();
        assertTrue(root > -2 && root < 2);
    }

    // Test findRoot method with an exponential function
    @Test
    public void testFindRootWithExponentialFunction() throws FunctionException {
        // Create an exponential function: f(x) = e^x - 1
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return Math.exp(x) - 1;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker();

        // Find root between -2 and 2
        double x0 = -2;
        double f0 = function.valueAt(x0);
        double x1 = 2;
        double f1 = function.valueAt(x1);
        boolean rootFound = solver.findRoot(function, checker, 1000, x0, f0, x1, f1);

        // Check if root is found and within the expected range
        assertTrue(rootFound);
        double root = solver.getRoot();
        assertTrue(root > -1 && root < 1);
    }

    // Test findRoot method with a function that throws an exception
    @Test(expected = FunctionException.class)
    public void testFindRootWithFunctionException() throws FunctionException {
        // Create a function that always throws an exception
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) throws FunctionException {
                throw new FunctionException("Exception thrown by function");
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker();

        // Find root between -2 and 2
        double x0 = -2;
        double f0 = function.valueAt(x0);
        double x1 = 2;
        double f1 = function.valueAt(x1);
        solver.findRoot(function, checker, 1000, x0, f0, x1, f1);
    }

    // Test findRoot method with a large number of iterations
    @Test
    public void testFindRootWithLargeNumIterations() throws FunctionException {
        // Create a linear function: f(x) = x + 1
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x + 1;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker();

        // Find root between -2 and 2 with a maximum of 1 iteration
        double x0 = -2;
        double f0 = function.valueAt(x0);
        double x1 = 2;
        double f1 = function.valueAt(x1);
        boolean rootFound = solver.findRoot(function, checker, 1, x0, f0, x1, f1);

        // Check if root is not found due to exceeding maximum iterations
        assertFalse(rootFound);
    }

}