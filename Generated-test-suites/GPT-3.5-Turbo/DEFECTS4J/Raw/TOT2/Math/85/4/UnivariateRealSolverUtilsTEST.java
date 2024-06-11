import org.junit.Test;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.function.Sin;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.ConvergenceException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve1() {
        UnivariateRealFunction f = new Sin();
        try {
            double result = UnivariateRealSolverUtils.solve(f, 0, Math.PI);
            assertEquals(0, f.value(result), 1e-6);
        } catch (FunctionEvaluationException | ConvergenceException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testSolve2() {
        UnivariateRealFunction f = new Sin();
        try {
            double result = UnivariateRealSolverUtils.solve(f, 0, Math.PI, 1e-6);
            assertEquals(0, f.value(result), 1e-6);
        } catch (FunctionEvaluationException | ConvergenceException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testBracket1() {
        UnivariateRealFunction f = new Sin();
        try {
            double[] bracket = UnivariateRealSolverUtils.bracket(f, Math.PI / 2, 0, Math.PI);
            assertTrue(f.value(bracket[0]) * f.value(bracket[1]) <= 0);
        } catch (FunctionEvaluationException | ConvergenceException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testBracket2() {
        UnivariateRealFunction f = new Sin();
        try {
            double[] bracket = UnivariateRealSolverUtils.bracket(f, Math.PI, 0, Math.PI, 1000);
            assertTrue(f.value(bracket[0]) * f.value(bracket[1]) <= 0);
        } catch (FunctionEvaluationException | ConvergenceException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testMidpoint() {
        double result = UnivariateRealSolverUtils.midpoint(0, 2);
        assertEquals(1, result, 0);
    }

}