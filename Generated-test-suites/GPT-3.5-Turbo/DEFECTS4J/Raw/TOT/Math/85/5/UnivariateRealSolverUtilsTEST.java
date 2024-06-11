import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;

import static org.junit.Assert.*;

public class UnivariateRealSolverUtilsTest {

    /*
     * Test case for solve method
     * Should return the root (midpoint) of the given function within the given interval
     */
    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double x0 = 1.0;
        double x1 = 3.0;

        double result = UnivariateRealSolverUtils.solve(f, x0, x1);

        assertEquals(2.0, result, 0.0001);
    }

    /*
     * Test case for bracket method
     * Should return an array with two values that bracket the root of the given function within the given interval
     */
    @Test
    public void testBracket() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double initial = 2.0;
        double lowerBound = 1.0;
        double upperBound = 3.0;
        int maximumIterations = 100;

        double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);

        assertEquals(1.0, result[0], 0.0001);
        assertEquals(3.0, result[1], 0.0001);
    }

    /*
     * Test case for midpoint method
     * Should return the midpoint between the two given values
     */
    @Test
    public void testMidpoint() {
        double a = 1.0;
        double b = 3.0;

        double result = UnivariateRealSolverUtils.midpoint(a, b);

        assertEquals(2.0, result, 0.0001);
    }

    /*
     * Test case for solve method with invalid arguments
     * Should throw IllegalArgumentException if f is null or the endpoints do not specify a valid interval
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSolveInvalidArguments() {
        UnivariateRealFunction f = null;
        double x0 = 3.0;
        double x1 = 1.0;

        UnivariateRealSolverUtils.solve(f, x0, x1);
    }

    /*
     * Test case for solve method with convergence exception
     * Should throw ConvergenceException if the iteration count was exceeded
     */
    @Test(expected = ConvergenceException.class)
    public void testSolveConvergenceException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double x0 = 1.0;
        double x1 = 3.0;

        UnivariateRealSolverUtils.solve(f, x0, x1);
    }

    /*
     * Test case for bracket method with invalid arguments
     * Should throw IllegalArgumentException if function is null, maximumIterations is not positive, or initial is not between lowerBound and upperBound
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBracketInvalidArguments() {
        UnivariateRealFunction f = null;
        double initial = 2.0;
        double lowerBound = 3.0;
        double upperBound = 1.0;
        int maximumIterations = -1;

        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
    }

    /*
     * Test case for bracket method with convergence exception
     * Should throw ConvergenceException if the algorithm fails to find a and b satisfying the desired conditions
     */
    @Test(expected = ConvergenceException.class)
    public void testBracketConvergenceException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double initial = 1.0;
        double lowerBound = 2.0;
        double upperBound = 3.0;
        int maximumIterations = 1;

        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
    }

}