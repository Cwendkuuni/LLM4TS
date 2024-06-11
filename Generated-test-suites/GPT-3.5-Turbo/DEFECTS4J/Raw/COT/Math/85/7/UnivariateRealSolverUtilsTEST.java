import static org.junit.Assert.*;

import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.QuinticFunction;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    // Test solve(UnivariateRealFunction f, double x0, double x1)

    @Test
    public void testSolve_X0X1BracketRoot() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new QuinticFunction();
        double x0 = -2;
        double x1 = 2;
        double result = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(0, f.value(result), 1e-10);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolve_X0X1NoBracketRoot() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new QuinticFunction();
        double x0 = 1;
        double x1 = 2;
        UnivariateRealSolverUtils.solve(f, x0, x1);
    }

    // Test solve(UnivariateRealFunction f, double x0, double x1, double absoluteAccuracy)

    @Test
    public void testSolve_X0X1AbsoluteAccuracyBracketRoot()
            throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new QuinticFunction();
        double x0 = -2;
        double x1 = 2;
        double absoluteAccuracy = 1e-10;
        double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
        assertEquals(0, f.value(result), absoluteAccuracy);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolve_X0X1AbsoluteAccuracyNoBracketRoot()
            throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new QuinticFunction();
        double x0 = 1;
        double x1 = 2;
        double absoluteAccuracy = 1e-10;
        UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
    }

    // Test bracket(UnivariateRealFunction function, double initial, double lowerBound, double upperBound)

    @Test
    public void testBracket_InitialInBounds_FunctionIncrementsMonotonically()
            throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x + 1;
            }
        };
        double initial = 0;
        double lowerBound = -10;
        double upperBound = 10;
        double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        assertTrue(result[0] < initial);
        assertTrue(result[1] > initial);
        assertEquals(f.value(result[0]) * f.value(result[1]), 0, 1e-10);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracket_InitialOutOfBounds()
            throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new QuinticFunction();
        double initial = 10;
        double lowerBound = -10;
        double upperBound = 10;
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    }

    // Test bracket(UnivariateRealFunction function, double initial, double lowerBound, double upperBound, int maximumIterations)

    @Test
    public void testBracketWithIterationLimit_InitialInBounds_FunctionIncrementsMonotonically()
            throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x + 1;
            }
        };
        double initial = 0;
        double lowerBound = -10;
        double upperBound = 10;
        int maximumIterations = 5;
        double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
        assertTrue(result[0] < initial);
        assertTrue(result[1] > initial);
        assertEquals(f.value(result[0]) * f.value(result[1]), 0, 1e-10);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketWithIterationLimit_MaximumIterationsExceeded()
            throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new QuinticFunction();
        double initial = 1;
        double lowerBound = -10;
        double upperBound = 10;
        int maximumIterations = 5;
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
    }

    // Test midpoint(double a, double b)

    @Test
    public void testMidpoint() {
        double a = 2;
        double b = 4;
        double expected = 3;
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(expected, result, 1e-10);
    }

}