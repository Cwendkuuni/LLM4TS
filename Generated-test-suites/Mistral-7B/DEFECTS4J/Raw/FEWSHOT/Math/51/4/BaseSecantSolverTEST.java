package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RuntimeConfiguration;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testRegulaFalsi() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.REGULA_FALSI);

        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{0, 1, 1});
        double root = solver.solve(100, f, -1.5, 1.5, 0.0);
        assertEquals(0.0, root, 1e-8);
    }

    @Test(expected = NullArgumentException.class)
    public void testRegulaFalsiNullFunction() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.REGULA_FALSI);
        solver.solve(100, null, -1.5, 1.5, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegulaFalsiWrongMethod() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, Method.ILLINOIS);
        solver.solve(100, new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{0, 1, 1}), -1.5, 1.5, 0.0);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testRegulaFalsiMaxEval() throws MaxEvalExceededException {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.REGULA_FALSI);
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{0, 1, 1});
        solver.solve(0, f, -1.5, 1.5, 0.0);
    }

    @Test(expected = TooManyIterationsException.class)
    public void testRegulaFalsiTooManyIterations() throws TooManyIterationsException {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, RegulaFalsiSolver.REGULA_FALSI);
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{0, 1, 1});
        solver.setMaxIterations(0);
        solver.solve(100, f, -1.5, 1.5, 0.0);
    }

    @Test
    public void testIllinois() {
        BaseSecantSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.ILLINOIS);

        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{0, 1, 1});
        double root = solver.solve(100, f, -1.5, 1.5, 0.0);
        assertEquals(0.0, root, 1e-8);
    }

    @Test(expected = NullArgumentException.class)
    public void testIllinoisNullFunction() {
        BaseSecantSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.ILLINOIS);
        solver.solve(100, null, -1.5, 1.5, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllinoisWrongMethod() {
        BaseSecantSolver solver = new IllinoisSolver(1e-6, Method.PEGASUS);
        solver.solve(100, new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{0, 1, 1}), -1.5, 1.5, 0.0);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testIllinoisMaxEval() throws MaxEvalExceededException {
        BaseSecantSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.ILLINOIS);
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{0, 1, 1});
        solver.solve(0, f, -1.5, 1.5, 0.0);
    }

    @Test(expected = TooManyIterationsException.class)
    public void testIllinoisTooManyIterations() throws TooManyIterationsException {
        BaseSecantSolver solver = new IllinoisSolver(1e-6, IllinoisSolver.ILLINOIS);
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{0, 1, 1});
        solver.setMaxIterations(0);
        solver.solve(100, f, -1.5, 1.5, 0.0);
    }

    @Test
    public void testPegasus() {
        BaseSecantSolver solver = new PegasusSolver(1e-6, PegasusSolver.PEGASUS);

        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{0, 1, 1});
        double root = solver.solve(100, f, -1.5, 1.5, 0.0);
        assertEquals(0.0, root, 1e-8);
    }

    @Test(expected = NullArgumentException.class)
    public void testPegasusNullFunction() {
        BaseSecantSolver solver = new PegasusSolver(1e-6, PegasusSolver.PEGASUS);
        solver.solve(100, null, -1.5, 1.5, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPegasusWrongMethod() {
        BaseSecantSolver solver = new PegasusSolver(1e-6, Method.ILLINOIS);
        solver.solve(100, new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{0, 1, 1}), -1.5, 1.5, 0.0);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testPegasusMaxEval() throws MaxEvalExceededException {
        BaseSecantSolver solver = new PegasusSolver(1e-6, PegasusSolver.PEGASUS);
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{0, 1, 1});
        solver.solve(0, f, -1.5, 1.5, 0.0);
    }

    @Test(expected = TooManyIterationsException.class)
    public void testPegasusTooManyIterations() throws TooManyIterationsException {
        BaseSecantSolver solver = new PegasusSolver(1e-6, PegasusSolver.PEGASUS);
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{0, 1, 1});
        solver.setMaxIterations(0);
        solver.solve(100, f, -1.5, 1.5, 0.0);
    }
}