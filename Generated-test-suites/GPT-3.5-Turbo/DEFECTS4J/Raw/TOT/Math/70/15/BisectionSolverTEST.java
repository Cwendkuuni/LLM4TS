import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.solvers.BisectionSolver;
import org.junit.Test;
import static org.junit.Assert.*;

public class BisectionSolverTest {

    @Test
    public void testSolve() throws FunctionEvaluationException, MaxIterationsExceededException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };

        BisectionSolver solver = new BisectionSolver();

        // Test typical use cases

        // Test with a function that has a zero at x = 2
        double result = solver.solve(f, 1, 3);
        assertEquals(2.0, result, 1E-6);

        // Test with a function that has a zero at x = 0.5
        result = solver.solve(f, 0, 1);
        assertEquals(0.5, result, 1E-6);

        // Test with a function that has multiple zeros
        // in the interval [0, 10], but the bisection algorithm
        // will only find one of them
        UnivariateRealFunction fMultipleZeros = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return Math.sin(x);
            }
        };
        result = solver.solve(fMultipleZeros, 0, 10);
        // Only the zero at x = 1.5708 will be found by the bisection algorithm
        assertEquals(1.5708, result, 1E-4);

        // Test edge cases

        // Test with a function that has no zeros in the interval [0, 10]
        UnivariateRealFunction fNoZero = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x + 1;
            }
        };
        try {
            result = solver.solve(fNoZero, 0, 10);
            fail("Expected MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // This exception should be thrown because the bisection algorithm
            // will not converge to a zero in the given interval
        }

        // Test with a function that has a zero at x = 0
        result = solver.solve(fNoZero, -1, 0);
        assertEquals(0.0, result, 1E-6);

        // Test with a function that has a zero at x = 10
        result = solver.solve(fNoZero, 9, 10);
        assertEquals(10.0, result, 1E-6);

        // Test error scenarios

        // Test with a null function
        try {
            result = solver.solve(null, 0, 1);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // This exception should be thrown because the function is null
        }

        // Test with a NaN return value from the function
        UnivariateRealFunction fNaN = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return Double.NaN;
            }
        };
        try {
            result = solver.solve(fNaN, 0, 1);
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // This exception should be thrown because the function returns NaN
        }
    }

}