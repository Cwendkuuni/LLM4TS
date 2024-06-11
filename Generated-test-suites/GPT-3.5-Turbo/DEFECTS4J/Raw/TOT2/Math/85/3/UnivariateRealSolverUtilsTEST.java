import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.apache.commons.math.analysis.Sin;
import org.apache.commons.math.analysis.polynomials.PolynomialFunction;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;
import org.apache.commons.math.analysis.solvers.FunctionUtils;

import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;

import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolveWithDefaultSolver() {
        UnivariateRealFunction f = new Sin();
        double x0 = 0.0;
        double x1 = Math.PI;

        try {
            double result = UnivariateRealSolverUtils.solve(f, x0, x1);
            assertEquals(0.0, f.value(result), FunctionUtils.EPSILON);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Exception thrown for valid inputs");
        }
    }

    @Test
    public void testSolveWithCustomSolver() {
        UnivariateRealFunction f = new Sin();
        double x0 = -Math.PI;
        double x1 = Math.PI;
        double absoluteAccuracy = 1e-6;

        try {
            double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
            assertEquals(0.0, f.value(result), absoluteAccuracy);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Exception thrown for valid inputs");
        }
    }

    @Test
    public void testBracket() {
        UnivariateRealFunction f = new PolynomialFunction(new double[] {1, -3, 2});
        double initial = 0.0;
        double lowerBound = -10.0;
        double upperBound = 10.0;

        try {
            double[] bracket = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
            double a = bracket[0];
            double b = bracket[1];

            assertEquals(2.0, f.value(a), FunctionUtils.EPSILON);
            assertEquals(0.0, f.value(b), FunctionUtils.EPSILON);
        } catch (ConvergenceException | FunctionEvaluationException e) {
            fail("Exception thrown for valid inputs");
        }
    }

    @Test
    public void testMidpoint() {
        double a = -1.0;
        double b = 3.0;

        double result = UnivariateRealSolverUtils.midpoint(a, b);

        assertEquals(1.0, result, FunctionUtils.EPSILON);
    }
}