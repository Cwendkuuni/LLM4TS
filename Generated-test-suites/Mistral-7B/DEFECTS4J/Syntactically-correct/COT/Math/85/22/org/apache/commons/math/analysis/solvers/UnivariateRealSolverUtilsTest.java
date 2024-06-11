import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.PrecisionException;
import org.apache.commons.math3.solver.BrentSolver;
import org.apache.commons.math3.solver.BracketedRealRootFinder;
import org.apache.commons.math3.solver.LaguerreSolver;
import org.apache.commons.math3.solver.SecantSolver;
import org.apache.commons.math3.solver.BisectionSolver;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {
    // Test for solve method with default solver
    @Test
    public void testSolveDefault() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, new double[]{0.0});
        double x0 = 1.0;
        double x1 = 2.0;
        double expectedResult = 1.0;

        double result = UnivariateRealSolverUtils.solve(f, x0, x1);

        assertEquals(expectedResult, result, 1e-9);
    }

    // Test for solve method with specific solver and absolute accuracy
    @Test
    public void testSolveSpecificSolverAndAccuracy() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, new double[]{0.0});
        double x0 = 1.0;
        double x1 = 2.0;
        double absoluteAccuracy = 1e-12;
        double expectedResult = 1.0;

        UnivariateRealSolver solver = new BrentSolver();

        double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy, solver);

        assertEquals(expectedResult, result, absoluteAccuracy);
    }

    // Test for bracket method with positive maximum iterations
    @Test
    public void testBracketPositiveMaxIterations() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, new double[]{0.0});
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        int maximumIterations = 10;

        double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);

        double a = result[0];
        double b = result[1];

        // Check that a and b satisfy the conditions
        assertTrue(lowerBound <= a && a < initial && initial < b && b <= upperBound);
        assertTrue(f.value(a) * f.value(b) < 0.0);
    }

    // Test for bracket method with negative maximum iterations
    @Test(expected = ConvergenceException.class)
    public void testBracketNegativeMaxIterations() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, new double[]{0.0});
        double initial = 1.0;
        double lowerBound = 0.0;
        double upperBound = 2.0;
        int maximumIterations = -10;

        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
    }

    // Test for bracket method with lower bound greater than upper bound
    @Test(expected = IllegalArgumentException.class)
    public void testBracketInvalidBracketingParameters() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, new double[]{0.0});
        double initial = 1.0;
        double lowerBound = 2.0;
        double upperBound = 0.0;
        int maximumIterations = 10;

        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
    }

    // Test for midpoint method
    @Test
    public void testMidpoint() {
        double a = 1.0;
        double b = 2.0;
        double expectedResult = 1.5;

        double result = UnivariateRealSolverUtils.midpoint(a, b);

        assertEquals(expectedResult, result, 1e-9);
    }
}