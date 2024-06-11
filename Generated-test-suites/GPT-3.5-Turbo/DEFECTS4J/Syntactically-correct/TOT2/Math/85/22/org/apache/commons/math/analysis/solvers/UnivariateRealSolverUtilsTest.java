import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolveBasic() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = UnivariateRealSolverUtils.solve(f, 1, 3);
        assertEquals(2.0, result, 0.0001);
    }

    @Test
    public void testSolveWithAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = UnivariateRealSolverUtils.solve(f, 1, 3, 0.0001);
        assertEquals(2.0, result, 0.0001);
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        double[] result = UnivariateRealSolverUtils.bracket(f, 2, 1, 3);
        assertEquals(1.0, result[0], 0.0001);
        assertEquals(3.0, result[1], 0.0001);
    }

    @Test
    public void testMidpoint() {
        double result = UnivariateRealSolverUtils.midpoint(1, 3);
        assertEquals(2.0, result, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithNullFunction() {
        UnivariateRealSolverUtils.solve(null, 1, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithInvalidInterval() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        UnivariateRealSolverUtils.solve(f, 3, 1);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketWithNoBracket() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x + 4;
            }
        };
        UnivariateRealSolverUtils.bracket(f, 2, 1, 3);
    }

}