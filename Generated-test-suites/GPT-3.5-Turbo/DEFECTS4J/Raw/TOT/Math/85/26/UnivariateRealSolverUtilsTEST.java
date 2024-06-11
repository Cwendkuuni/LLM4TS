import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() {
        // Typical case: valid function and interval
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 5;
            }
        };
        double x0 = 4;
        double x1 = 6;
        double result1 = UnivariateRealSolverUtils.solve(f1, x0, x1);
        assertEquals(5, result1, 0.0001);

        // Edge case: lower bound equals to upper bound
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 5;
            }
        };
        double x2 = 5;
        double x3 = 5;
        double result2 = UnivariateRealSolverUtils.solve(f2, x2, x3);
        assertEquals(5, result2, 0.0001);
    }

    @Test
    public void testSolveWithAccuracy() {
        // Typical case: valid function, interval and accuracy
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 5;
            }
        };
        double x0 = 4;
        double x1 = 6;
        double accuracy1 = 0.001;
        double result1 = UnivariateRealSolverUtils.solve(f1, x0, x1, accuracy1);
        assertEquals(5, result1, 0.001);

        // Error scenario: invalid accuracy
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 5;
            }
        };
        double x2 = 4;
        double x3 = 6;
        double accuracy2 = -0.001;
        try {
            double result2 = UnivariateRealSolverUtils.solve(f2, x2, x3, accuracy2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("bad value for maximum iterations number: -0.001", e.getMessage());
        }
    }

    @Test
    public void testBracket() {
        // Typical case: valid function, initial, lower bound and upper bound
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 5;
            }
        };
        double initial1 = 4;
        double lowerBound1 = 2;
        double upperBound1 = 6;
        double[] result1 = UnivariateRealSolverUtils.bracket(f1, initial1, lowerBound1, upperBound1);
        assertEquals(3, result1[0], 0.0001);
        assertEquals(5, result1[1], 0.0001);

        // Error scenario: initial value outside of the lower bound and upper bound
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 5;
            }
        };
        double initial2 = 1;
        double lowerBound2 = 2;
        double upperBound2 = 6;
        try {
            double[] result2 = UnivariateRealSolverUtils.bracket(f2, initial2, lowerBound2, upperBound2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("invalid bracketing parameters:  lower bound=2.0,  initial=1.0, upper bound=6.0", e.getMessage());
        }
    }

    @Test
    public void testMidpoint() {
        double a1 = 1;
        double b1 = 3;
        double result1 = UnivariateRealSolverUtils.midpoint(a1, b1);
        assertEquals(2, result1, 0.0001);

        double a2 = -5;
        double b2 = 10;
        double result2 = UnivariateRealSolverUtils.midpoint(a2, b2);
        assertEquals(2.5, result2, 0.0001);
    }

}