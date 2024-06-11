package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testBrentSolverWithDefaults() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();
        UnivariateFunction function = new PolynomialFunction(new double[]{-1.0, 2.0, -1.0}, new double[]{1.0, 0.0, 1.0});
        double root = solver.solve(100, function, -5.0, 5.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root, 1e-8);
    }

    @Test
    public void testBrentSolverWithCustomAccuracy() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-9, 5);
        UnivariateFunction function = new PolynomialFunction(new double[]{-1.0, 2.0, -1.0}, new double[]{1.0, 0.0, 1.0});
        double root = solver.solve(100, function, -5.0, 5.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root, 1e-9);
    }

    @Test
    public void testBrentSolverWithCustomAccuracyAndMaximalOrder() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-9, 10);
        UnivariateFunction function = new PolynomialFunction(new double[]{-1.0, 2.0, -1.0}, new double[]{1.0, 0.0, 1.0});
        double root = solver.solve(100, function, -5.0, 5.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root, 1e-9);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentSolverWithInvalidMaximalOrder() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 0);
    }

    @Test
    public void testBrentSolverWithAllowedSolutionLeft() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction function = new PolynomialFunction(new double[]{1.0, 2.0, 1.0}, new double[]{0.0, 1.0, 0.0});
        double root = solver.solve(100, function, -5.0, 5.0, AllowedSolution.LEFT_SIDE);
        assertEquals(-3.0, root, 1e-6);
    }

    @Test
    public void testBrentSolverWithAllowedSolutionRight() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction function = new PolynomialFunction(new double[]{-1.0, 2.0, -1.0}, new double[]{1.0, 0.0, 1.0});
        double root = solver.solve(100, function, -5.0, 5.0, AllowedSolution.RIGHT_SIDE);
        assertEquals(3.0, root, 1e-6);
    }

    @Test
    public void testBrentSolverWithAllowedSolutionBelow() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction function = new PolynomialFunction(new double[]{-1.0, -2.0, -1.0}, new double[]{1.0, 0.0, 1.0});
        double root = solver.solve(100, function, -5.0, 5.0, AllowedSolution.BELOW_SIDE);
        assertEquals(-2.0, root, 1e-6);
    }

    @Test
    public void testBrentSolverWithAllowedSolutionAbove() throws NumberIsTooSmallException {
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver(1e-6, 5);
        UnivariateFunction function = new PolynomialFunction(new double[]{-1.0, -2.0, -1.0}, new double[]{1.0, 0.0, 1.0});
        double root = solver.solve(100, function, -5.0, 5.0, AllowedSolution.ABOVE_SIDE);
        assertEquals(2.0, root, 1e-6);
    }

}