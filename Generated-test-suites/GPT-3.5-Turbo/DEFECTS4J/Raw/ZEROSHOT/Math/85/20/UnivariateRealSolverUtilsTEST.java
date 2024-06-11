import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;

import static org.junit.Assert.*;

public class UnivariateRealSolverUtilsTest {

    private static final double EPSILON = 1e-15;

    @Test
    public void solveDefaultSolverTest() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction(){
            public double value(double x) {
                return x * x - 2;
            }
        };

        double solution = UnivariateRealSolverUtils.solve(f, 0, 2);
        double expected = Math.sqrt(2);
        assertEquals(expected, solution, EPSILON);
    }

    @Test
    public void solveCustomSolverTest() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction(){
            public double value(double x) {
                return x * x - 2;
            }
        };

        double solution = UnivariateRealSolverUtils.solve(f, 0, 2, 1e-10);
        double expected = Math.sqrt(2);
        assertEquals(expected, solution, EPSILON);
    }

    @Test
    public void bracketTest() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction(){
            public double value(double x) {
                return x * x - 2;
            }
        };

        double[] bracket = UnivariateRealSolverUtils.bracket(f, 0, -1, 1);
        double[] expected = {-1, 1};
        assertArrayEquals(expected, bracket, EPSILON);
    }

    @Test
    public void midpointTest() {
        double a = -2;
        double b = 2;
        double midpoint = UnivariateRealSolverUtils.midpoint(a, b);
        double expected = 0;
        assertEquals(expected, midpoint, EPSILON);
    }
}