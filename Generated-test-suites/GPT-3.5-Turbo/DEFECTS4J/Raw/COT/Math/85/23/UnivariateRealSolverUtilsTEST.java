import org.junit.Test;
import org.apache.commons.math.analysis.solvers.*;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x - 1; // sample function
            }
        };
        try {
            double result = UnivariateRealSolverUtils.solve(f, 0, 2);
            assertEquals(1.0, result, 0.0001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testSolveWithAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x - 1; // sample function
            }
        };
        try {
            double result = UnivariateRealSolverUtils.solve(f, 0, 2, 0.0001);
            assertEquals(1.0, result, 0.0001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x - 1; // sample function
            }
        };
        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, 1, 0, 2);
            assertEquals(0.0, result[0], 0.0001);
            assertEquals(2.0, result[1], 0.0001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testBracketWithIterations() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x - 1; // sample function
            }
        };
        try {
            double[] result = UnivariateRealSolverUtils.bracket(f, 1, 0, 2, 10);
            assertEquals(0.0, result[0], 0.0001);
            assertEquals(2.0, result[1], 0.0001);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testMidpoint() {
        double result = UnivariateRealSolverUtils.midpoint(0, 2);
        assertEquals(1.0, result, 0.0001);
    }
}