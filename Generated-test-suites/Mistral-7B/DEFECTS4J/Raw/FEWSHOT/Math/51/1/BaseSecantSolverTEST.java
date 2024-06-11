package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.apache.commons.commons.math.analysis.PolynomialFunction;
import org.apache.commons.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.commons.math.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.commons.math.exception.MaxEvalExceededException;
import org.apache.commons.commons.math.exception.NumberIsTooLargeException;
import org.apache.commons.commons.math.exception.UnbracketedException;
import org.junit.Test;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    @Test
    public void testSolverWithRegulaFalsiMethod() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.REGULA_FALSI);
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{1, 1, 1});

        try {
            double root = solver.solve(100, function, -10.0, 10.0, 0.0);
            assertEquals(0.0, root, 1e-6);
        } catch (MaxEvalExceededException e) {
            // This should not happen
            fail("Max evaluation exceeded");
        } catch (NumberIsTooLargeException e) {
            // This should not happen
            fail("Number is too large");
        } catch (UnbracketedException e) {
            // This should not happen
            fail("Function not bracketed");
        }
    }

    @Test
    public void testSolverWithIllinoisMethod() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.ILLINOIS);
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{1, 1, 1});

        try {
            double root = solver.solve(100, function, -10.0, 10.0, 0.0);
            assertEquals(0.0, root, 1e-6);
        } catch (MaxEvalExceededException e) {
            // This should not happen
            fail("Max evaluation exceeded");
        } catch (NumberIsTooLargeException e) {
            // This should not happen
            fail("Number is too large");
        } catch (UnbracketedException e) {
            // This should not happen
            fail("Function not bracketed");
        }
    }

    @Test
    public void testSolverWithPegasusMethod() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.PEGASUS);
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{1, 1, 1});

        try {
            double root = solver.solve(100, function, -10.0, 10.0, 0.0);
            assertEquals(0.0, root, 1e-6);
        } catch (MaxEvalExceededException e) {
            // This should not happen
            fail("Max evaluation exceeded");
        } catch (NumberIsTooLargeException e) {
            // This should not happen
            fail("Number is too large");
        } catch (UnbracketedException e) {
            // This should not happen
            fail("Function not bracketed");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolverWithInvalidMethod() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.INVALID_METHOD);
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{1, 1, 1});

        solver.solve(100, function, -10.0, 10.0, 0.0);
    }

    @Test
    public void testSolverWithAllowedSolutionsLeftSide() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.REGULA_FALSI);
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{1, 1, 1});

        try {
            double root = solver.solve(100, function, -10.0, 10.0, 0.0, BracketedUnivariateRealSolver.AllowedSolution.LEFT_SIDE);
            assertEquals(0.0, root, 1e-6);
        } catch (MaxEvalExceededException e) {
            // This should not happen
            fail("Max evaluation exceeded");
        } catch (NumberIsTooLargeException e) {
            // This should not happen
            fail("Number is too large");
        } catch (UnbracketedException e) {
            // This should not happen
            fail("Function not bracketed");
        }
    }

    @Test
    public void testSolverWithAllowedSolutionsRightSide() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.REGULA_FALSI);
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{1, 1, 1});

        try {
            double root = solver.solve(100, function, -10.0, 10.0, 0.0, BracketedUnivariateRealSolver.AllowedSolution.RIGHT_SIDE);
            assertEquals(0.0, root, 1e-6);
        } catch (MaxEvalExceededException e) {
            // This should not happen
            fail("Max evaluation exceeded");
        } catch (NumberIsTooLargeException e) {
            // This should not happen
            fail("Number is too large");
        } catch (UnbracketedException e) {
            // This should not happen
            fail("Function not bracketed");
        }
    }

    @Test
    public void testSolverWithAllowedSolutionsBelowSide() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.REGULA_FALSI);
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{1, 1, 1});

        try {
            double root = solver.solve(100, function, -10.0, 10.0, 0.0, BracketedUnivariateRealSolver.AllowedSolution.BELOW_SIDE);
            assertEquals(0.0, root, 1e-6);
        } catch (MaxEvalExceededException e) {
            // This should not happen
            fail("Max evaluation exceeded");
        } catch (NumberIsTooLargeException e) {
            // This should not happen
            fail("Number is too large");
        } catch (UnbracketedException e) {
            // This should not happen
            fail("Function not bracketed");
        }
    }

    @Test
    public void testSolverWithAllowedSolutionsAboveSide() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.REGULA_FALSI);
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{1, 1, 1});

        try {
            double root = solver.solve(100, function, -10.0, 10.0, 0.0, BracketedUnivariateRealSolver.AllowedSolution.ABOVE_SIDE);
            assertEquals(0.0, root, 1e-6);
        } catch (MaxEvalExceededException e) {
            // This should not happen
            fail("Max evaluation exceeded");
        } catch (NumberIsTooLargeException e) {
            // This should not happen
            fail("Number is too large");
        } catch (UnbracketedException e) {
            // This should not happen
            fail("Function not bracketed");
        }
    }
}