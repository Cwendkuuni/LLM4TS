package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.junit.Test;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testBracketingNthOrderBrentSolverWithDefaultValues() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();

        UnivariateFunction function = new PolynomialFunction(new double[]{-1.0, 2.0, 1.0}, new double[]{1.0, 0.0, 1.0});
        double root = solver.solve(100, function, -3.0, 3.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root, 1e-6);
    }

    @Test
    public void testBracketingNthOrderBrentSolverWithCustomValues() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 10);

        UnivariateFunction function = new PolynomialFunction(new double[]{-1.0, 2.0, 1.0}, new double[]{1.0, 0.0, 1.0});
        double root = solver.solve(100, function, -3.0, 3.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root, 1e-8);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBracketingNthOrderBrentSolverWithInvalidMaximalOrder() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 1);
    }

    @Test
    public void testBracketingNthOrderBrentSolverWithCustomAllowedSolution() {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-8, 10);

        UnivariateFunction function = new PolynomialFunction(new double[]{-1.0, 2.0, 1.0}, new double[]{1.0, 0.0, 1.0});

        double rootBelow = solver.solve(100, function, -3.0, 3.0, AllowedSolution.BELOW_SIDE);
        assertLessThan(0.0, rootBelow);

        double rootAbove = solver.solve(100, function, -3.0, 3.0, AllowedSolution.ABOVE_SIDE);
        assertGreaterThan(0.0, rootAbove);

        double rootLeft = solver.solve(100, function, -3.0, 3.0, AllowedSolution.LEFT_SIDE);
        assertLessThan(0.0, rootLeft);

        double rootRight = solver.solve(100, function, -3.0, 3.0, AllowedSolution.RIGHT_SIDE);
        assertGreaterThan(0.0, rootRight);
    }
}