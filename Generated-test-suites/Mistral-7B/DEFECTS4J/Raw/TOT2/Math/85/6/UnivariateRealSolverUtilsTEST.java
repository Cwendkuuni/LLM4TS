import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.solvers.BrentSolver;
import org.apache.commons.math3.solvers.BisectionSolver;
import org.apache.commons.math3.solvers.BracketedRealRootFinder;
import org.apache.commons.math3.solvers.SecantSolver;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve() throws Exception {
        // Test with a simple polynomial function
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 0.0});
        double x0 = 1.0;
        double x1 = 0.0;
        double expected = 1.0;
        double actual = UnivariateRealSolverUtils.solve(f, x0, x1);
        assertEquals(expected, actual, 1e-6);

        // Test with a function that does not have a root in the given interval
        UnivariateFunction g = new PolynomialFunction(new double[]{1.0, 0.0, 1.0});
        x0 = -1.0;
        x1 = 1.0;
        try {
            UnivariateRealSolverUtils.solve(g, x0, x1);
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // Expected
        }

        // Test with a function that throws an exception during evaluation
        UnivariateFunction h = new UnivariateFunction() {
            @Override
            public double value(double x) throws Exception {
                if (x == 0.0) {
                    throw new UnsupportedOperationException("Function does not support x equals to 0");
                }
                return Math.sin(x);
            }
        };
        x0 = 0.1;
        x1 = 1.0;
        try {
            UnivariateRealSolverUtils.solve(h, x0, x1);
            fail("Expected MathRuntimeException");
        } catch (MathRuntimeException e) {
            // Expected
        }

        // Test with a function that has multiple roots
        UnivariateFunction i = new PolynomialFunction(new double[]{-1.0, 3.0, 3.0, -1.0});
        x0 = -2.0;
        x1 = 2.0;
        double[] roots = UnivariateRealSolverUtils.bracket(i, 0.0, x0, x1);
        double actualRoot1 = UnivariateRealSolverUtils.solve(i, roots[0], roots[1]);
        double actualRoot2 = UnivariateRealSolverUtils.solve(i, roots[0], roots[1]);
        double expectedRoot1 = 1.0;
        double expectedRoot2 = -1.0;
        assertEquals(expectedRoot1, actualRoot1, 1e-6);
        assertEquals(expectedRoot2, actualRoot2, 1e-6);

        // Test with a function that has a root very close to the end of the interval
        UnivariateFunction j = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, 1e10});
        x0 = 0.0;
        x1 = 1.0;
        try {
            UnivariateRealSolverUtils.solve(j, x0, x1);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }

        // Test with a function with arbitrary precision
        UnivariateFunction k = new PolynomialFunction(new double[]{1.0e-10, 1.0, 1.0});
        x0 = 0.0;
        x1 = 1.0;
        UnivariateRealSolver solver = new BrentSolver();
        double actual = solver.solve(k, x0, x1);
        double expected = 1.0e-7;
        assertEquals(expected, actual, 1e-7);
    }
}