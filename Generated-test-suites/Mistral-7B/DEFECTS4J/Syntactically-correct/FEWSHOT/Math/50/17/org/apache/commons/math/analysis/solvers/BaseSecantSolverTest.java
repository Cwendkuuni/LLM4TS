package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;

import org.apache.commons.cli.BaseSecantSolver;
import org.apache.commons.cli.BracketedUnivariateRealSolver;
import org.apache.commons.cli. AbstractUnivariateRealSolver;
import org.apache.commons.cli.AllowedSolution;
import org.apache.commons.cli.Method;

public class BaseSecantSolverTest {

    @Test
    public void testRegulaFalsi() {
        UnivariateFunction f = new PolynomialFunction(1.0, new double[]{-1.0, 1.0, 1.0});
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, Method.REGULA_FALSI);
        double root = solver.solve(10, f, -1.0, 1.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, Precision.abs(root - 0.3333333333333333), 1e-6);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRegulaFalsiNoBracketing() {
        UnivariateFunction f = new PolynomialFunction(1.0, new double[]{-1.0, 1.0, 1.0});
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, Method.REGULA_FALSI);
        double root = solver.solve(10, f, 1.0, 0.0, AllowedSolution.ANY_SIDE);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testRegulaFalsiMaxEval() {
        UnivariateFunction f = new PolynomialFunction(1.0, new double[]{-1.0, 1.0, 1.0});
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, Method.REGULA_FALSI);
        solver.solve(0, f, -1.0, 1.0, AllowedSolution.ANY_SIDE);
    }

    @Test
    public void testIllinois() {
        UnivariateFunction f = new PolynomialFunction(1.0, new double[]{-1.0, 1.0, 1.0});
        BaseSecantSolver solver = new IllinoisSolver(1e-6, Method.ILLINOIS);
        double root = solver.solve(10, f, -1.0, 1.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, Precision.abs(root - 0.3333333333333333), 1e-6);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIllinoisNoBracketing() {
        UnivariateFunction f = new PolynomialFunction(1.0, new double[]{-1.0, 1.0, 1.0});
        BaseSecantSolver solver = new IllinoisSolver(1e-6, Method.ILLINOIS);
        double root = solver.solve(10, f, 1.0, 0.0, AllowedSolution.ANY_SIDE);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testIllinoisMaxEval() {
        UnivariateFunction f = new PolynomialFunction(1.0, new double[]{-1.0, 1.0, 1.0});
        BaseSecantSolver solver = new IllinoisSolver(1e-6, Method.ILLINOIS);
        solver.solve(0, f, -1.0, 1.0, AllowedSolution.ANY_SIDE);
    }

    @Test
    public void testPegasus() {
        UnivariateFunction f = new PolynomialFunction(1.0, new double[]{-1.0, 1.0, 1.0});
        BaseSecantSolver solver = new PegasusSolver(1e-6, Method.PEGASUS);
        double root = solver.solve(10, f, -1.0, 1.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, Precision.abs(root - 0.3333333333333333), 1e-6);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPegasusNoBracketing() {
        UnivariateFunction f = new PolynomialFunction(1.0, new double[]{-1.0, 1.0, 1.0});
        BaseSecantSolver solver = new PegasusSolver(1e-6, Method.PEGASUS);
        double root = solver.solve(10, f, 1.0, 0.0, AllowedSolution.ANY_SIDE);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testPegasusMaxEval() {
        UnivariateFunction f = new PolynomialFunction(1.0, new double[]{-1.0, 1.0, 1.0});
        BaseSecantSolver solver = new PegasusSolver(1e-6, Method.PEGASUS);
        solver.solve(0, f, -1.0, 1.0, AllowedSolution.ANY_SIDE);
    }

    @Test
    public void testAllowedSolutions() {
        UnivariateFunction f = new PolynomialFunction(1.0, new double[]{-1.0, 1.0, 1.0});
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, Method.REGULA_FALSI);

        try {
            double root = solver.solve(10, f, -1.0, 1.0, AllowedSolution.ABOVE_SIDE);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        double rootBelow = solver.solve(10, f, -1.0, 1.0, AllowedSolution.BELOW_SIDE);
        assertEquals(0.0, Precision.abs(rootBelow - 0.6666666666666666), 1e-6);

        double rootLeft = solver.solve(10, f, -1.0, 1.0, AllowedSolution.LEFT_SIDE);
        assertEquals(0.0, Precision.abs(rootLeft - 0.3333333333333333), 1e-6);

        double rootRight = solver.solve(10, f, -1.0, 1.0, AllowedSolution.RIGHT_SIDE);
        assertEquals(0.0, Precision.abs(rootRight - 0.3333333333333333), 1e-6);

        double rootAnySide = solver.solve(10, f, -1.0, 1.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, Precision.abs(rootAnySide - 0.3333333333333333), 1e-6);
    }

}