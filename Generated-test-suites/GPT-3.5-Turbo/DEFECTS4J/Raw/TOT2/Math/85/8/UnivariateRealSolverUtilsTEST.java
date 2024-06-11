import org.junit.Test;
import static org.junit.Assert.*;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolveDefault() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = UnivariateRealSolverUtils.solve(f, 1.0, 3.0);
        assertEquals(2.0, result, 0.0001);
    }

    @Test
    public void testSolveWithAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = UnivariateRealSolverUtils.solve(f, 1.0, 3.0, 0.001);
        assertEquals(2.0, result, 0.001);
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double[] bounds = UnivariateRealSolverUtils.bracket(f, 2.0, 1.0, 3.0);
        assertEquals(1.0, bounds[0], 0.0001);
        assertEquals(3.0, bounds[1], 0.0001);
    }

    @Test
    public void testMidpoint() {
        double result = UnivariateRealSolverUtils.midpoint(1.0, 3.0);
        assertEquals(2.0, result, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveNullFunction() {
        UnivariateRealSolverUtils.solve(null, 1.0, 3.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketInvalidBounds() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        UnivariateRealSolverUtils.bracket(f, 2.0, 3.0, 1.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBracketEvaluationException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                throw new FunctionEvaluationException(x);
            }
        };
        UnivariateRealSolverUtils.bracket(f, 2.0, 1.0, 3.0);
    }
}