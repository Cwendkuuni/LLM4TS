import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;
import org.apache.commons.math.analysis.UnivariateRealFunction;

public class UnivariateRealSolverUtilsTest {

    private final double EPSILON = 1e-10;

    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };

        double result = UnivariateRealSolverUtils.solve(f, -10, 10);
        assertEquals(2, result, EPSILON);
    }

    @Test
    public void testSolveWithAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };

        double result = UnivariateRealSolverUtils.solve(f, -10, 10, EPSILON);
        assertEquals(2, result, EPSILON);
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };

        double[] result = UnivariateRealSolverUtils.bracket(f, 0, -1, 1);
        assertEquals(-1, result[0], EPSILON);
        assertEquals(1, result[1], EPSILON);
    }

    @Test
    public void testMidpoint() {
        double a = 1;
        double b = 3;
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(2, result, EPSILON);
    }
}