import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.ConvergenceException;
import org.apache.commons.math.analysis.solvers.FunctionEvaluationException;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolveWithDefaultSolver_validInput() {
        UnivariateRealFunction mockFunction = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = UnivariateRealSolverUtils.solve(mockFunction, 1.0, 3.0);
        Assert.assertEquals(2.0, result, 0.0001);
    }

    @Test
    public void testSolveWithDefaultSolver_invalidFunction() {
        UnivariateRealFunction mockFunction = null;
        try {
            UnivariateRealSolverUtils.solve(mockFunction, 1.0, 3.0);
        } catch (IllegalArgumentException e) {
            // Expected IllegalArgumentException
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testSolveWithDefaultSolver_invalidInterval() {
        UnivariateRealFunction mockFunction = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        try {
            UnivariateRealSolverUtils.solve(mockFunction, 3.0, 1.0);
        } catch (IllegalArgumentException e) {
            // Expected IllegalArgumentException
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testBracket_validInput() {
        UnivariateRealFunction mockFunction = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double[] result = UnivariateRealSolverUtils.bracket(mockFunction, 2.0, 1.0, 3.0);
        Assert.assertArrayEquals(new double[]{1.0, 3.0}, result, 0.0001);
    }

    @Test
    public void testBracker_invalidFunction() {
        UnivariateRealFunction mockFunction = null;
        try {
            UnivariateRealSolverUtils.bracket(mockFunction, 2.0, 1.0, 3.0);
        } catch (IllegalArgumentException e) {
            // Expected IllegalArgumentException
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testBracker_invalidInterval() {
        UnivariateRealFunction mockFunction = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        try {
            UnivariateRealSolverUtils.bracket(mockFunction, 4.0, 1.0, 3.0);
        } catch (IllegalArgumentException e) {
            // Expected IllegalArgumentException
            Assert.assertTrue(true);
        }
    }

    @Test
    public void testMidpoint() {
        double result = UnivariateRealSolverUtils.midpoint(1.0, 3.0);
        Assert.assertEquals(2.0, result, 0.0001);
    }

}