import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;

public class UnivariateRealSolverUtilsTest {

    private UnivariateRealFunction mockFunction;

    @Before
    public void setUp() throws Exception {
        // Setup a mock function for testing
        mockFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                // Return a constant value of 1 for testing
                return 1;
            }
        };
    }

    @After
    public void tearDown() throws Exception {
        // Reset the mock function after each test
        mockFunction = null;
    }

    @Test
    public void testSolve() {
        try {
            // Test solving a univariate real function with default solver
            double result = UnivariateRealSolverUtils.solve(mockFunction, 0, 1);
            assertEquals(0, result, 0.001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Exception thrown while solving: " + e.getMessage());
        }
    }

    @Test
    public void testSolveWithAccuracy() {
        try {
            // Test solving a univariate real function with default solver and accuracy
            double result = UnivariateRealSolverUtils.solve(mockFunction, 0, 1, 0.001);
            assertEquals(0, result, 0.001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Exception thrown while solving: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithNullFunction() throws ConvergenceException, FunctionEvaluationException {
        // Test solving with null function, should throw IllegalArgumentException
        UnivariateRealSolverUtils.solve(null, 0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithInvalidInterval() throws ConvergenceException, FunctionEvaluationException {
        // Test solving with invalid interval, should throw IllegalArgumentException
        UnivariateRealSolverUtils.solve(mockFunction, 1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithInvalidAccuracy() throws ConvergenceException, FunctionEvaluationException {
        // Test solving with invalid accuracy, should throw IllegalArgumentException
        UnivariateRealSolverUtils.solve(mockFunction, 0, 1, -0.001);
    }

    @Test
    public void testBracket() {
        try {
            // Test bracketing a root with default maximum iterations
            double[] result = UnivariateRealSolverUtils.bracket(mockFunction, 0, -1, 1);
            assertEquals(-1, result[0], 0.001);
            assertEquals(1, result[1], 0.001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Exception thrown while bracketing: " + e.getMessage());
        }
    }

    @Test
    public void testBracketWithMaximumIterations() {
        try {
            // Test bracketing a root with specified maximum iterations
            double[] result = UnivariateRealSolverUtils.bracket(mockFunction, 0, -1, 1, 10);
            assertEquals(-1, result[0], 0.001);
            assertEquals(1, result[1], 0.001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Exception thrown while bracketing: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithNullFunction() throws ConvergenceException, FunctionEvaluationException {
        // Test bracketing with null function, should throw IllegalArgumentException
        UnivariateRealSolverUtils.bracket(null, 0, -1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithInvalidInterval() throws ConvergenceException, FunctionEvaluationException {
        // Test bracketing with invalid interval, should throw IllegalArgumentException
        UnivariateRealSolverUtils.bracket(mockFunction, 1, 0, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithInvalidMaximumIterations() throws ConvergenceException, FunctionEvaluationException {
        // Test bracketing with invalid maximum iterations, should throw IllegalArgumentException
        UnivariateRealSolverUtils.bracket(mockFunction, 0, -1, 1, -10);
    }

    @Test
    public void testMidpoint() {
        // Test computing the midpoint of two values
        double result = UnivariateRealSolverUtils.midpoint(-1, 1);
        assertEquals(0, result, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetupWithNullFunction() {
        // Test setup with null function, should throw IllegalArgumentException
        UnivariateRealSolverUtils.setup(null);
    }

    @Test
    public void testSuccessfulSetup() {
        // Test successful setup with a valid function
        try {
            UnivariateRealSolverUtils.setup(mockFunction);
        } catch (IllegalArgumentException e) {
            fail("IllegalArgumentException thrown when setting up valid function: " + e.getMessage());
        }
    }

}