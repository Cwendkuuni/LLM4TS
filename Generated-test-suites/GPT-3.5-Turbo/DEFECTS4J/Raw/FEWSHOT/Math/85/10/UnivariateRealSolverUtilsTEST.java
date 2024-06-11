import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;

import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverFactory;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {
    private static final UnivariateRealFunction F = new UnivariateRealFunction() {
        @Override
        public double value(double x) throws FunctionEvaluationException {
            return x - 1;
        }
    };

    @Test
    public void testSolve() throws ConvergenceException, FunctionEvaluationException {
        double solution = UnivariateRealSolverUtils.solve(F, 0.5, 1.5);
        assertEquals(1.0, solution, 0.0);
    }

    @Test
    public void testSolveWithAbsoluteAccuracy() throws ConvergenceException, FunctionEvaluationException {
        double solution = UnivariateRealSolverUtils.solve(F, 0.5, 1.5, 0.1);
        assertEquals(1.0, solution, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithNullFunction() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.solve(null, 0.5, 1.5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithInvalidInterval() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.solve(F, 1.5, 0.5);
    }

    @Test
    public void testBracket() throws ConvergenceException, FunctionEvaluationException {
        double[] bracket = UnivariateRealSolverUtils.bracket(F, 1.0, 0.0, 2.0);
        assertArrayEquals(new double[] {0.0, 2.0}, bracket, 0.0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithNullFunction() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.bracket(null, 1.0, 0.0, 2.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBracketWithInvalidInterval() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.bracket(F, 1.0, 2.0, 0.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracketWithConvergenceException() throws ConvergenceException, FunctionEvaluationException {
        UnivariateRealSolverUtils.bracket(F, 1.0, 0.0, 1.0);
    }

    @Test
    public void testMidpoint() {
        double midpoint = UnivariateRealSolverUtils.midpoint(0.0, 2.0);
        assertEquals(1.0, midpoint, 0.0);
    }
}