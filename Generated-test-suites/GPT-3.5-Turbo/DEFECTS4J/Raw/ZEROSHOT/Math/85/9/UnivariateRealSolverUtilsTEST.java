import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() throws ConvergenceException, FunctionEvaluationException {
        // Test case 1
        double result1 = UnivariateRealSolverUtils.solve(new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        }, 0, 10);
        assertEquals(5, result1, 0.0001);

        // Test case 2
        double result2 = UnivariateRealSolverUtils.solve(new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x);
            }
        }, 0, Math.PI);
        assertEquals(Math.PI, result2, 0.0001);
    }

    @Test
    public void testSolveWithAccuracy() throws ConvergenceException, FunctionEvaluationException {
        // Test case 1
        double result1 = UnivariateRealSolverUtils.solve(new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        }, 0, 10, 0.0001);
        assertEquals(5, result1, 0.0001);

        // Test case 2
        double result2 = UnivariateRealSolverUtils.solve(new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x);
            }
        }, 0, Math.PI, 0.0001);
        assertEquals(Math.PI, result2, 0.0001);
    }

    @Test
    public void testBracket() throws ConvergenceException, FunctionEvaluationException {
        // Test case 1
        double[] result1 = UnivariateRealSolverUtils.bracket(new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        }, 2, 0, 10);
        assertEquals(1, result1[0], 0.0001);
        assertEquals(3, result1[1], 0.0001);

        // Test case 2
        double[] result2 = UnivariateRealSolverUtils.bracket(new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x);
            }
        }, Math.PI/4, 0, Math.PI);
        assertEquals(0, result2[0], 0.0001);
        assertEquals(Math.PI/2, result2[1], 0.0001);
    }

    @Test
    public void testBracketWithMaxIterations() throws ConvergenceException, FunctionEvaluationException {
        // Test case 1
        double[] result1 = UnivariateRealSolverUtils.bracket(new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        }, 2, 0, 10, 100);
        assertEquals(1, result1[0], 0.0001);
        assertEquals(3, result1[1], 0.0001);

        // Test case 2
        double[] result2 = UnivariateRealSolverUtils.bracket(new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x);
            }
        }, Math.PI/4, 0, Math.PI, 100);
        assertEquals(0, result2[0], 0.0001);
        assertEquals(Math.PI/2, result2[1], 0.0001);
    }

    @Test
    public void testMidpoint() {
        // Test case 1
        double result1 = UnivariateRealSolverUtils.midpoint(0, 10);
        assertEquals(5, result1, 0.0001);

        // Test case 2
        double result2 = UnivariateRealSolverUtils.midpoint(0, -10);
        assertEquals(-5, result2, 0.0001);
    }
}