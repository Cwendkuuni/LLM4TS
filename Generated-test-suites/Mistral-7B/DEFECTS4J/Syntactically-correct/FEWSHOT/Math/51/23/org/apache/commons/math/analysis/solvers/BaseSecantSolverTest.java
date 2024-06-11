package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.commons.math.analysis.PolynomialFunction;
import org.apache.commons.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.commons.math.exception.MaxEvalException;
import org.apache.commons.commons.math.util.Precision;
import org.apache.commons.commons.math.util.FastMath;
import org.apache.commons.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.commons.math.analysis.solvers.AllowedSolution;
import org.apache.commons.commons.math.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.commons.math.analysis.solvers.IllinoisSolver;
import org.apache.commons.commons.math.analysis.solvers.PegasusSolver;

public class BaseSecantSolverTest {

    @Test
    public void testRegulaFalsi() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 2.0, -1.0}, 3);
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        try {
            double root = solver.solve(100, function, -5.0, 5.0, AllowedSolution.ANY_SIDE);
            assertEquals(2.0, Precision.round(root, 5), 1e-6);
        } catch (MaxEvalException e) {
            fail("Should have found the root within 100 evaluations.");
        }
    }

    @Test
    public void testIllinois() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 2.0, -1.0}, 3);
        BaseSecantSolver solver = new IllinoisSolver(1e-6);
        try {
            double root = solver.solve(100, function, -5.0, 5.0, AllowedSolution.ANY_SIDE);
            assertEquals(2.0, Precision.round(root, 5), 1e-6);
        } catch (MaxEvalException e) {
            fail("Should have found the root within 100 evaluations.");
        }
    }

    @Test
    public void testPegasus() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 2.0, -1.0}, 3);
        BaseSecantSolver solver = new PegasusSolver(1e-6);
        try {
            double root = solver.solve(100, function, -5.0, 5.0, AllowedSolution.ANY_SIDE);
            assertEquals(2.0, Precision.round(root, 5), 1e-6);
        } catch (MaxEvalException e) {
            fail("Should have found the root within 100 evaluations.");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAllowedSolution() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 2.0, -1.0}, 3);
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.PEGASUS);
        double root;
        try {
            root = solver.solve(100, function, -5.0, 5.0, AllowedSolution.INVALID);
            fail("Should have thrown an IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = MaxEvalException.class)
    public void testMaxEvalException() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 2.0, -1.0}, 3);
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        try {
            solver.solve(0, function, -5.0, 5.0, AllowedSolution.ANY_SIDE);
            fail("Should have thrown a MaxEvalException.");
        } catch (MaxEvalException e) {
            // Expected
        }
    }

    @Test
    public void testAllowedSolution() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1.0, 2.0, -1.0}, 3);
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        double root;
        try {
            root = solver.solve(100, function, -5.0, 5.0, AllowedSolution.LEFT_SIDE);
            assertEquals(2.0, Precision.round(root, 5), 1e-6);
        } catch (MaxEvalException e) {
            fail("Should have found the root within 100 evaluations.");
        }

        try {
            root = solver.solve(100, function, -5.0, 5.0, AllowedSolution.RIGHT_SIDE);
            assertEquals(-2.0, Precision.round(root, 5), 1e-6);
        } catch (MaxEvalException e) {
            fail("Should have found the root within 100 evaluations.");
        }

        try {
            root = solver.solve(100, function, -5.0, 5.0, AllowedSolution.BELOW_SIDE);
            assertEquals(2.0, Precision.round(root, 5), 1e-6);
        } catch (MaxEvalException e) {
            fail("Should have found the root within 100 evaluations.");
        }

        try {
            root = solver.solve(100, function, -5.0, 5.0, AllowedSolution.ABOVE_SIDE);
            assertEquals(-2.0, Precision.round(root, 5), 1e-6);
        } catch (MaxEvalException e) {
            fail("Should have found the root within 100 evaluations.");
        }
    }
}