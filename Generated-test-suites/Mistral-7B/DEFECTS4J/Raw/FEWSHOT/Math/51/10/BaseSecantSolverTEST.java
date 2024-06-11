package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.RootFindFailureException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;

import org.apache.commons.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.commons.math.analysis.solvers.AllowedSolution;
import org.apache.commons.commons.math.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.commons.math.analysis.solvers.IllinoisSolver;
import org.apache.commons.commons.math.analysis.solvers.PegasusSolver;

public class BaseSecantSolverTest {

    @Test
    public void testRegulaFalsiSolver() {
        BaseSecantSolver<UnivariateFunction> solver = new RegulaFalsiSolver(0.0001);
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0});
        try {
            double root = solver.solve(10, f, -2.0, 2.0, AllowedSolution.ANY_SIDE);
            assertEquals(0.0, root, 0.0001);
        } catch (MaxEvalException e) {
            fail("Maximum number of evaluations exceeded.");
        } catch (RootFindFailureException e) {
            fail("Root finding failed.");
        }
    }

    @Test
    public void testIllinoisSolver() {
        BaseSecantSolver<UnivariateFunction> solver = new IllinoisSolver(0.0001);
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0});
        try {
            double root = solver.solve(10, f, -2.0, 2.0, AllowedSolution.ANY_SIDE);
            assertEquals(0.0, root, 0.0001);
        } catch (MaxEvalException e) {
            fail("Maximum number of evaluations exceeded.");
        } catch (RootFindFailureException e) {
            fail("Root finding failed.");
        }
    }

    @Test
    public void testPegasusSolver() {
        BaseSecantSolver<UnivariateFunction> solver = new PegasusSolver(0.0001);
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0});
        try {
            double root = solver.solve(10, f, -2.0, 2.0, AllowedSolution.ANY_SIDE);
            assertEquals(0.0, root, 0.0001);
        } catch (MaxEvalException e) {
            fail("Maximum number of evaluations exceeded.");
        } catch (RootFindFailureException e) {
            fail("Root finding failed.");
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetMaxIterations() {
        BaseSecantSolver<UnivariateFunction> solver = new RegulaFalsiSolver(0.0001);
        solver.setMaxIterations(10);
    }
}