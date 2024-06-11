import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;
import org.apache.commons.math.analysis.solvers.ConvergenceException;
import org.apache.commons.math.analysis.solvers.FunctionEvaluationException;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolveWithDefaultSolver() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        double result = UnivariateRealSolverUtils.solve(f, 0, 10);
        assertEquals(5.0, result, 0.0001);
    }

    @Test
    public void testSolveWithCustomAbsoluteAccuracy() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 25;
            }
        };
        double result = UnivariateRealSolverUtils.solve(f, 0, 10, 0.0001);
        assertEquals(5.0, result, 0.0001);
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 16;
            }
        };
        try {
            double[] bracket = UnivariateRealSolverUtils.bracket(f, 0, -10, 10);
            assertEquals(-4.0, bracket[0], 0.0001);
            assertEquals(4.0, bracket[1], 0.0001);
        } catch (ConvergenceException e) {
            fail("ConvergenceException should not be thrown");
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException should not be thrown");
        }
    }

    @Test
    public void testMidpoint() {
        double midpoint = UnivariateRealSolverUtils.midpoint(-10, 10);
        assertEquals(0.0, midpoint, 0.0001);
    }

}