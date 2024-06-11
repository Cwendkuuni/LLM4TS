package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.junit.Assert;
import org.junit.Test;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testSimpleRoot() {
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        final UnivariateFunction function = f -> f.value(0.5) * f.value(0.5) - 1;
        final double root = solver.solve(100, function, -10, 10, AllowedSolution.ANY_SIDE);
        Assert.assertEquals(0.0, root, 1e-6);
    }

    @Test
    public void testFunctionWithMultipleRoots() {
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        final UnivariateFunction function = f -> f.value(f.argument()) * (f.argument() - 1) * (f.argument() - 2);
        final double[] roots = {-1.0, 1.0, 2.0};
        for (double root : roots) {
            final double result = solver.solve(100, function, -5, 5, AllowedSolution.ANY_SIDE);
            Assert.assertEquals(root, result, 1e-6);
        }
    }

    @Test(expected = NoBracketingException.class)
    public void testFunctionWithoutBracketing() {
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        final UnivariateFunction function = f -> Math.sin(f.argument());
        solver.solve(100, function, -10, 10, AllowedSolution.ANY_SIDE);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testFunctionWithInvalidMaximalOrder() {
        new BracketingNthOrderBrentSolver(1e-6, 0);
    }

    @Test
    public void testFunctionWithAllowedSolution() {
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5, AllowedSolution.RIGHT_SIDE);
        final UnivariateFunction function = f -> f.value(f.argument()) * (f.argument() - 1) * (f.argument() - 2);
        final double root = solver.solve(100, function, -5, 5, AllowedSolution.RIGHT_SIDE);
        Assert.assertTrue(root >= 1.0);
    }

    @Test
    public void testFunctionWithFunctionValueAccuracy() {
        final BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5, 1e-7);
        final UnivariateFunction function = f -> 1 / (f.argument() * f.argument());
        final double root = solver.solve(100, function, -10, 10, AllowedSolution.ANY_SIDE);
        Assert.assertEquals(1.0 / Math.sqrt(2.0), root, 1e-7);
    }

}