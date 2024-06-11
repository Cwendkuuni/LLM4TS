package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.RootFindersException;
import org.apache.commons.math3.util.Precision;

import org.apache.commons.cli.analysis.solvers.BaseSecantSolver;
import org.apache.commons.cli.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.cli.analysis.solvers.IllinoisSolver;
import org.apache.commons.cli.analysis.solvers.PegasusSolver;
import org.apache.commons.cli.analysis.solvers.RegulaFalsiSolver;

public class BaseSecantSolverTest {

    @Test
    public void testRegulaFalsiSolver() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0, 1.0}, 0);
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);

        double x = solver.solve(f, -2.0, 2.0);
        assertEquals(0.0, x, 1e-6);
    }

    @Test(expected = NoBracketingException.class)
    public void testRegulaFalsiSolverNoBracketing() {
        UnivariateFunction f = new PolynomialFunction(new double[]{1.0}, 0);
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);

        solver.solve(f, -1.0, 1.0);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testRegulaFalsiSolverMaxIterations() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0, 1.0}, 0);
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, 10);

        solver.solve(f, -2.0, 2.0);
    }

    @Test
    public void testIllinoisSolver() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0, 1.0}, 0);
        BaseSecantSolver solver = new IllinoisSolver(1e-6);

        double x = solver.solve(f, -2.0, 2.0);
        assertEquals(0.0, x, 1e-6);
    }

    @Test(expected = NoBracketingException.class)
    public void testIllinoisSolverNoBracketing() {
        UnivariateFunction f = new PolynomialFunction(new double[]{1.0}, 0);
        BaseSecantSolver solver = new IllinoisSolver(1e-6);

        solver.solve(f, -1.0, 1.0);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testIllinoisSolverMaxIterations() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0, 1.0}, 0);
        BaseSecantSolver solver = new IllinoisSolver(1e-6, 10);

        solver.solve(f, -2.0, 2.0);
    }

    @Test
    public void testPegasusSolver() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0, 1.0}, 0);
        BaseSecantSolver solver = new PegasusSolver(1e-6);

        double x = solver.solve(f, -2.0, 2.0);
        assertEquals(0.0, x, 1e-6);
    }

    @Test(expected = NoBracketingException.class)
    public void testPegasusSolverNoBracketing() {
        UnivariateFunction f = new PolynomialFunction(new double[]{1.0}, 0);
        BaseSecantSolver solver = new PegasusSolver(1e-6);

        solver.solve(f, -1.0, 1.0);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testPegasusSolverMaxIterations() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0, 1.0}, 0);
        BaseSecantSolver solver = new PegasusSolver(1e-6, 10);

        solver.solve(f, -2.0, 2.0);
    }

    @Test
    public void testRegulaFalsiSolverWithAbsoluteAccuracy() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0, 1.0}, 0);
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-5, 1e-6);

        double x = solver.solve(f, -2.0, 2.0);
        assertEquals(0.0, x, 1e-5);
    }

    @Test
    public void testIllinoisSolverWithAbsoluteAccuracy() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0, 1.0}, 0);
        BaseSecantSolver solver = new IllinoisSolver(1e-5, 1e-6);

        double x = solver.solve(f, -2.0, 2.0);
        assertEquals(0.0, x, 1e-5);
    }

    @Test
    public void testPegasusSolverWithAbsoluteAccuracy() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0, 1.0}, 0);
        BaseSecantSolver solver = new PegasusSolver(1e-5, 1e-6);

        double x = solver.solve(f, -2.0, 2.0);
        assertEquals(0.0, x, 1e-5);
    }

    @Test
    public void testRegulaFalsiSolverWithRelativeAndAbsoluteAccuracy() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0, 1.0}, 0);
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-3, 1e-6);

        double x = solver.solve(f, -2.0, 2.0);
        double maxError = Precision.safeSub(FunctionUtils.value(f, x), 0.0);
        assertTrue(maxError < 1e-3 || maxError > -1e-3);
    }

    @Test
    public void testIllinoisSolverWithRelativeAndAbsoluteAccuracy() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0, 1.0}, 0);
        BaseSecantSolver solver = new IllinoisSolver(1e-3, 1e-6);

        double x = solver.solve(f, -2.0, 2.0);
        double maxError = Precision.safeSub(FunctionUtils.value(f, x), 0.0);
        assertTrue(maxError < 1e-3 || maxError > -1e-3);
    }

    @Test
    public void testPegasusSolverWithRelativeAndAbsoluteAccuracy() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0, 1.0}, 0);
        BaseSecantSolver solver = new PegasusSolver(1e-3, 1e-6);

        double x = solver.solve(f, -2.0, 2.0);
        double maxError = Precision.safeSub(FunctionUtils.value(f, x), 0.0);
        assertTrue(maxError < 1e-3 || maxError > -1e-3);
    }

    @Test
    public void testBracketing() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 4.0, 4.0, 1.0}, 0);
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);

        double x = solver.solve(f, -2.0, 2.0);

        double min = getMin(f, 1e-6);
        double max = getMax(f, 1e-6);

        assertTrue(x >= min && x <= max);
    }

    @Test
    public void testBracketingNegative() {
        UnivariateFunction f = new PolynomialFunction(new double[]{1.0, -4.0, 4.0, 1.0}, 0);
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);

        double x = solver.solve(f, -2.0, 2.0);

        double min = getMin(f, 1e-6);
        double max = getMax(f, 1e-6);

        assertTrue(x <= min || x >= max);
    }

    @Test
    public void testBracketingZero() {
        UnivariateFunction f = new PolynomialFunction(new double[]{0.0, 0.0, 0.0, 1.0}, 0);
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);

        try {
            solver.solve(f, -2.0, 2.0);
            fail();
        } catch (RootFindersException e) {
            // expected
        }
    }

    private double getMin(UnivariateFunction f, double tolerance) {
        double x = Double.POSITIVE_INFINITY;
        double min = x;

        for (int i = 0; i < 10; i++) {
            double nextX = f.valuePlusDelta(x, tolerance);
            if (nextX < x) {
                x = nextX;
            }
            if (x < min) {
                min = x;
            }
        }

        return min;
    }

    private double getMax(UnivariateFunction f, double tolerance) {
        double x = Double.NEGATIVE_INFINITY;
        double max = x;

        for (int i = 0; i < 10; i++) {
            double nextX = f.valueMinusDelta(x, tolerance);
            if (nextX > x) {
                x = nextX;
            }
            if (x > max) {
                max = x;
            }
        }

        return max;
    }
}