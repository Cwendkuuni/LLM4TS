import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double x0 = 1;
        double x1 = 3;
        double result = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(2.0, result, 0.00001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveNullFunction() {
        UnivariateRealFunction f = null;
        double x0 = 1;
        double x1 = 3;
        UnivariateRealSolverUtils.solve(f, x0, x1);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveNoRoot() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 4;
            }
        };
        double x0 = 1;
        double x1 = 3;
        UnivariateRealSolverUtils.solve(f, x0, x1);
    }

    @Test
    public void testSolveWithAbsoluteAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double x0 = 1;
        double x1 = 3;
        double absoluteAccuracy = 0.00001;
        double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
        assertEquals(2.0, result, absoluteAccuracy);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithAbsoluteAccuracyNullFunction() {
        UnivariateRealFunction f = null;
        double x0 = 1;
        double x1 = 3;
        double absoluteAccuracy = 0.00001;
        UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveWithAbsoluteAccuracyNoRoot() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 4;
            }
        };
        double x0 = 1;
        double x1 = 3;
        double absoluteAccuracy = 0.00001;
        UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double initial = 2;
        double lowerBound = 0;
        double upperBound = 4;
        double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
        assertEquals(1.0, result[0], 0.00001);
        assertEquals(3.0, result[1], 0.00001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketNullFunction() {
        UnivariateRealFunction f = null;
        double initial = 2;
        double lowerBound = 0;
        double upperBound = 4;
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketInvalidBounds() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double initial = 2;
        double lowerBound = 3;
        double upperBound = 1;
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    }

    @Test
    public void testBracketWithMaximumIterations() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double initial = 2;
        double lowerBound = 0;
        double upperBound = 4;
        int maximumIterations = 10;
        double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
        assertEquals(1.0, result[0], 0.00001);
        assertEquals(3.0, result[1], 0.00001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithMaximumIterationsNullFunction() {
        UnivariateRealFunction f = null;
        double initial = 2;
        double lowerBound = 0;
        double upperBound = 4;
        int maximumIterations = 10;
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketWithMaximumIterationsNoRoot() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 4;
            }
        };
        double initial = 2;
        double lowerBound = 0;
        double upperBound = 4;
        int maximumIterations = 10;
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
    }

    @Test
    public void testMidpoint() {
        double a = 1;
        double b = 3;
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(2.0, result, 0.00001);
    }

}