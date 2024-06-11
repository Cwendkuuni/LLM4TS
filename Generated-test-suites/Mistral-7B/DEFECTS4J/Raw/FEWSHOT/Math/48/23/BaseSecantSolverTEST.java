package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.solver.BracketedUnivariateRealSolver;
import org.apache.commons.math3.solver.LagrangeSolver;
import org.apache.commons.math3.solver.secant.RegulaFalsiSolver;
import org.apache.commons.math3.solver.secant.ScottsRuleSolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class BaseSecantSolverTest {

    @Test
    public void testRegulaFalsiSolver() {
        UnivariateRealFunction function = new PolynomialFunction(2, -1.0, 1.0, 0.0);
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.Method.REGULA_FALSI);
        double root = solver.solve(function, 0.0, 1.0);
        assertEquals(0.5, root, 1e-6);
        assertArrayEquals(new double[]{0.0, 0.5, 1.0}, solver.getBracketing(), 1e-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testRegulaFalsiSolverMaxIterations() {
        UnivariateRealFunction function = new PolynomialFunction(2, -1.0, 1.0, 0.0);
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.Method.REGULA_FALSI, 0);
        solver.solve(function, 0.0, 1.0);
    }

    @Test
    public void testIllinoisSolver() {
        UnivariateRealFunction function = new PolynomialFunction(2, -1.0, 1.0, 0.0);
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.Method.ILLINOIS);
        double root = solver.solve(function, 0.0, 1.0);
        assertEquals(0.5, root, 1e-6);
        assertArrayEquals(new double[]{0.0, 0.5, 1.0}, solver.getBracketing(), 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllinoisSolverNegativeMaxEval() {
        UnivariateRealFunction function = new PolynomialFunction(2, -1.0, 1.0, 0.0);
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.Method.ILLINOIS, -1);
        solver.solve(function, 0.0, 1.0);
    }

    @Test
    public void testPegasusSolver() {
        UnivariateRealFunction function = new PolynomialFunction(2, -1.0, 1.0, 0.0);
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.Method.PEGASUS);
        double root = solver.solve(function, 0.0, 1.0);
        assertEquals(0.5, root, 1e-6);
        assertArrayEquals(new double[]{0.0, 0.5, 1.0}, solver.getBracketing(), 1e-6);
    }

    @Test
    public void testPegasusSolverIterations() {
        UnivariateRealFunction function = new PolynomialFunction(2, -1.0, 1.0, 0.0);
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.Method.PEGASUS, 10);
        double root = solver.solve(function, 0.0, 1.0);
        assertEquals(0.5, root, 1e-6);
        assertArrayEquals(new double[]{0.0, 0.5, 1.0}, solver.getBracketing(), 1e-6);
    }

    @Test
    public void testAllowedSolutions() {
        UnivariateRealFunction function = new PolynomialFunction(2, -1.0, 1.0, 0.0);
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.Method.PEGASUS);

        double rootLeft = solver.solve(function, 0.0, 1.0, AllowedSolution.LEFT_SIDE);
        assertFalse(Double.isNaN(rootLeft));
        assertTrue(rootLeft < 0.5);

        double rootRight = solver.solve(function, 0.0, 1.0, AllowedSolution.RIGHT_SIDE);
        assertFalse(Double.isNaN(rootRight));
        assertTrue(rootRight > 0.5);

        double rootBelow = solver.solve(function, 0.0, 1.0, AllowedSolution.BELOW_SIDE);
        assertFalse(Double.isNaN(rootBelow));
        assertTrue(rootBelow < 0.5);

        double rootAbove = solver.solve(function, 0.0, 1.0, AllowedSolution.ABOVE_SIDE);
        assertFalse(Double.isNaN(rootAbove));
        assertTrue(rootAbove > 0.5);

        // Test invalid allowed solution
        try {
            solver.solve(function, 0.0, 1.0, AllowedSolution.BOTH_SIDES);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAllowedSolutionsWithBracketing() {
        UnivariateRealFunction function = new PolynomialFunction(2, -1.0, 1.0, 0.0);
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.Method.PEGASUS);

        double rootLeft = solver.solve(function, 0.0, 1.0, AllowedSolution.LEFT_SIDE, null);
        assertFalse(Double.isNaN(rootLeft));
        assertTrue(rootLeft < 0.5);

        double rootRight = solver.solve(function, 0.0, 1.0, AllowedSolution.RIGHT_SIDE, null);
        assertFalse(Double.isNaN(rootRight));
        assertTrue(rootRight > 0.5);

        double rootBelow = solver.solve(function, 0.0, 1.0, AllowedSolution.BELOW_SIDE, null);
        assertFalse(Double.isNaN(rootBelow));
        assertTrue(rootBelow < 0.5);

        double rootAbove = solver.solve(function, 0.0, 1.0, AllowedSolution.ABOVE_SIDE, null);
        assertFalse(Double.isNaN(rootAbove));
        assertTrue(rootAbove > 0.5);

        double[] bracketing = solver.getBracketing();
        assertNotNull(bracketing);
        assertArrayEquals(new double[]{0.0, 0.5, 1.0}, bracketing, 1e-6);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetBracketingWithoutAllowedSolutions() {
        UnivariateRealFunction function = new PolynomialFunction(2, -1.0, 1.0, 0.0);
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.Method.PEGASUS);
        solver.getBracketing();
    }

    @Test
    public void testGetBracketingWithAllowedSolutions() {
        UnivariateRealFunction function = new PolynomialFunction(2, -1.0, 1.0, 0.0);
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.Method.PEGASUS);
        double[] bracketing = solver.getBracketing(AllowedSolution.LEFT_SIDE);
        assertNotNull(bracketing);
        assertArrayEquals(new double[]{0.0, 0.5, 1.0}, bracketing, 1e-6);

        bracketing = solver.getBracketing(AllowedSolution.RIGHT_SIDE);
        assertNotNull(bracketing);
        assertArrayEquals(new double[]{0.0, 0.5, 1.0}, bracketing, 1e-6);

        bracketing = solver.getBracketing(AllowedSolution.BELOW_SIDE);
        assertNotNull(bracketing);
        assertArrayEquals(new double[]{0.0, 0.5, 1.0}, bracketing, 1e-6);

        bracketing = solver.getBracketing(AllowedSolution.ABOVE_SIDE);
        assertNotNull(bracketing);
        assertArrayEquals(new double[]{0.0, 0.5, 1.0}, bracketing, 1e-6);
    }

    @Test
    public void testGetBracketingWithDifferentAllowedSolutions() {
        UnivariateRealFunction function = new PolynomialFunction(2, -1.0, 1.0, 0.0);
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.Method.PEGASUS);
        double[] bracketingLeft = solver.getBracketing(AllowedSolution.LEFT_SIDE);
        double[] bracketingRight = solver.getBracketing(AllowedSolution.RIGHT_SIDE);
        double[] bracketingBelow = solver.getBracketing(AllowedSolution.BELOW_SIDE);
        double[] bracketingAbove = solver.getBracketing(AllowedSolution.ABOVE_SIDE);

        assertNotNull(bracketingLeft);
        assertArrayEquals(new double[]{0.0, 0.5, 1.0}, bracketingLeft, 1e-6);
        assertNotNull(bracketingRight);
        assertArrayEquals(new double[]{0.0, 0.5, 1.0}, bracketingRight, 1e-6);
        assertNotNull(bracketingBelow);
        assertArrayEquals(new double[]{0.0, 0.5, 1.0}, bracketingBelow, 1e-6);
        assertNotNull(bracketingAbove);
        assertArrayEquals(new double[]{0.0, 0.5, 1.0}, bracketingAbove, 1e-6);

        assertArrayEquals(bracketingLeft, bracketingRight);
        assertArrayEquals(bracketingLeft, bracketingBelow);
        assertArrayEquals(bracketingLeft, bracketingAbove);
    }

    @Test
    public void testScottsRuleSolver() {
        UnivariateRealFunction function = new PolynomialFunction(3, -1.0, 1.0, 0.0, 0.0);
        BaseSecantSolver solver = new ScottsRuleSolver(1e-6);
        double root = solver.solve(function, 0.0, 1.0);
        assertEquals(0.433472461, root, 1e-6);
        assertArrayEquals(new double[]{0.0, 0.433472461, 1.0}, solver.getBracketing(), 1e-6);
    }

}