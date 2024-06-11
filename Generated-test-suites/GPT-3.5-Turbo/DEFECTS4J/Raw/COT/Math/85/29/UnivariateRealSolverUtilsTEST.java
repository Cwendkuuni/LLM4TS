import org.junit.Test;
import static org.junit.Assert.*;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void solveTest() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        double result = UnivariateRealSolverUtils.solve(f, 0, 3);
        assertEquals(2, result, 0.0001);
    }

    @Test(expected = ConvergenceException.class)
    public void solveConvergenceExceptionTest() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        UnivariateRealSolverUtils.solve(f, 0, 3, 0.0001);
    }

    @Test
    public void bracketTest() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        double[] result = UnivariateRealSolverUtils.bracket(f, 2, 0, 4);
        assertEquals(1, result[0], 0.0001);
        assertEquals(3, result[1], 0.0001);
    }

    @Test(expected = ConvergenceException.class)
    public void bracketConvergenceExceptionTest() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        UnivariateRealSolverUtils.bracket(f, 2, 0, 4, 5);
    }

    @Test
    public void midpointTest() {
        double result = UnivariateRealSolverUtils.midpoint(1, 3);
        assertEquals(2, result, 0.0001);
    }
}