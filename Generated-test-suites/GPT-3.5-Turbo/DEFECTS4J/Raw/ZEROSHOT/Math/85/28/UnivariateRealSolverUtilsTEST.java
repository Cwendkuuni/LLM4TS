import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.analysis.UnivariateRealFunction;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = UnivariateRealSolverUtils.solve(f, -2, 2);
        assertEquals(2.0, result, 0.001);
    }

    @Test
    public void testSolveWithAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = UnivariateRealSolverUtils.solve(f, -2, 2, 0.001);
        assertEquals(2.0, result, 0.001);
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double[] result = UnivariateRealSolverUtils.bracket(f, 0, -2, 2);
        assertEquals(-1.0, result[0], 0.001);
        assertEquals(1.0, result[1], 0.001);
    }

    @Test
    public void testBracketWithMaxIterations() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double[] result = UnivariateRealSolverUtils.bracket(f, 0, -2, 2, 10);
        assertEquals(-1.0, result[0], 0.001);
        assertEquals(1.0, result[1], 0.001);
    }

    @Test
    public void testMidpoint() {
        double a = -1.0;
        double b = 1.0;
        double result = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(0.0, result, 0.001);
    }

}