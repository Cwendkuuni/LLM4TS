import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;
import org.apache.commons.math.analysis.FunctionUtils;
import org.apache.commons.math.analysis.Sin;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() {
        UnivariateRealFunction sin = new Sin();
        double result = UnivariateRealSolverUtils.solve(sin, 0, 2);
        assertEquals(0, sin.value(result), 0.0001);
    }

    @Test
    public void testSolveWithAccuracy() {
        UnivariateRealFunction fn = FunctionUtils.compose(new Sin(),
                FunctionUtils.add(1.0, FunctionUtils.multiply(-2, FunctionUtils.identity())));
        double result = UnivariateRealSolverUtils.solve(fn, -1, 1, 0.0001);
        assertEquals(1, fn.value(result), 0.0001);
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction sin = new Sin();
        double[] bounds = UnivariateRealSolverUtils.bracket(sin, 0.5, 0, 1);
        assertTrue(sin.value(bounds[0]) * sin.value(bounds[1]) < 0);
    }

    @Test
    public void testMidpoint() {
        double a = 2.0;
        double b = 4.0;
        double mid = UnivariateRealSolverUtils.midpoint(a, b);
        assertEquals(3.0, mid, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithNullFunction() {
        UnivariateRealSolverUtils.solve(null, -1, 1);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolveWithInvalidInterval() {
        UnivariateRealFunction constant = FunctionUtils.constant(3);
        UnivariateRealSolverUtils.solve(constant, 2, 1);
    }

    @Test(expected = ConvergenceException.class)
    public void testBrackWithInvalidBounds() {
        UnivariateRealFunction sin = new Sin();
        UnivariateRealSolverUtils.bracket(sin, 0.5, 1, 0);
    }

}