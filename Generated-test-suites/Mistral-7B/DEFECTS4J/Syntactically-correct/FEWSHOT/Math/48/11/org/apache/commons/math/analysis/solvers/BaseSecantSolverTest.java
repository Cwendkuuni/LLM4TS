package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.interpolators.LinearInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.commons.math.analysis.solvers.ConvergenceException;
import org.apache.commons.commons.math.analysis.solvers.Method;
import org.apache.commons.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.commons.math.analysis.solvers.AllowedSolution;

public class BaseSecantSolverTest {

    private UnivariateRealFunction f;

    @Before
    public void setUp() {
        f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x, 3) - 2 * x * Math.pow(x, 2) + 2 * x - 1;
            }

            public double value(double x, double precision) {
                return FunctionUtils.value(this, x, precision);
            }

            public int getMaxima() {
                return -1;
            }

            public int getMinima() {
                return 1;
            }

            public double[] getDerivatives(double x, int order) {
                if (order > 0) {
                    return new double[] { 3 * Math.pow(x, 2) - 4 * x + 2 };
                }
                return new double[] { 1 };
            }

            public double[] getDerivatives(double x, int order, double precision) {
                return FunctionUtils.derivatives(this, x, order, precision);
            }
        };
    }

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BracketedBrentSolver(0.0001, 0.001, 1e-5, Method.REGULA_FALSI);
        double root = solver.solve(f, -5, 5);
        assertFalse("Solver did not find a root within the interval.", Double.isNaN(root));
        assertEquals("Root is not accurate.", -1.3247179572447460259609088556, root, 1e-6);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSolveWithoutBracketing() {
        BaseSecantSolver solver = new BracketedBrentSolver(0.0001, 0.001, 1e-5, Method.REGULA_FALSI);
        solver.solve(f, 2.5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithNullFunction() {
        BaseSecantSolver solver = new BracketedBrentSolver(0.0001, 0.001, 1e-5, Method.REGULA_FALSI);
        solver.solve(null, 2.5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithEmptyInterval() {
        BaseSecantSolver solver = new BracketedBrentSolver(0.0001, 0.001, 1e-5, Method.REGULA_FALSI);
        solver.solve(f, 2.5, 2.5);
    }

    @Test
    public void testSolveWithAllowedSolution() {
        BaseSecantSolver solver = new BracketedBrentSolver(0.0001, 0.001, 1e-5, Method.REGULA_FALSI, AllowedSolution.BELOW_SIDE);
        double root = solver.solve(f, -5, 5);
        assertEquals("Root is not below the solution.", -1.3247179572447460259609088556, root, 1e-6);
    }

    @Test
    public void testSolveConvergenceException() {
        BaseSecantSolver solver = new BracketedBrentSolver(0.0001, 0.001, 1e-5, Method.REGULA_FALSI);
        try {
            solver.solve(new UnivariateRealFunction() {
                public double value(double x) {
                    throw new ConvergenceException("Test");
                }
            }, -5, 5);
            fail("Solver did not throw ConvergenceException.");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    @Test(expected = MaxEvalException.class)
    public void testSolveMaxEvalException() {
        BaseSecantSolver solver = new BracketedBrentSolver(0.0001, 0.001, 1e-5, Method.REGULA_FALSI);
        solver.setMaxIterations(0);
        solver.solve(f, -5, 5);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSolveNumberIsTooSmallException() {
        BaseSecantSolver solver = new BracketedBrentSolver(0.0001, 0.001, 1e-12, Method.REGULA_FALSI);
        solver.solve(f, -5, 5);
    }

    @Test
    public void testSolveIterations() {
        BaseSecantSolver solver = new BracketedBrentSolver(0.0001, 0.001, 1e-5, Method.REGULA_FALSI);
        int iterations = 0;
        double root = 0;
        try {
            root = solver.solve(f, -5, 5);
            iterations = solver.getIterations();
        } finally {
            solver.resetIterations();
        }
        assertTrue("Solver did not perform any iterations.", iterations > 0);
        assertEquals("Root is not accurate.", -1.3247179572447460259609088556, root, 1e-6);
    }

    @Test
    public void testSolveDerivatives() {
        BaseSecantSolver solver = new BracketedSecantSolver(0.0001, 0.001, 1e-5, Method.REGULA_FALSI);
        double root = solver.solve(f, -5, 5);
        double df = f.value(root);
        double[] derivatives = solver.getDerivatives(root);
        assertArrayEquals("Derivatives are not correct.", new double[] { df }, derivatives);
    }

    @Test
    public void testInterpolatingPolynomial() {
        BaseSecantSolver solver = new BracketedBrentSolver(0.0001, 0.001, 1e-5, Method.REGULA_FALSI);
        double root = solver.solve(f, -5, 5);
        UnivariateRealFunction polynomial = new PolynomialSplineFunction(new double[] { root }, new double[] { f.value(root) }, new double[] { 1 });
        LinearInterpolator interpolator = new LinearInterpolator();
        double x = 0.5 * (getMin() + getMax());
        double fx = f.value(x);
        double approx = interpolator.interpolate(x, polynomial);
        assertEquals("Approximation is not accurate.", root, approx, 1e-6);
        double df = f.value(root);
        double dfapprox = polynomial.value(x);
        assertEquals("Derivative approximation is not accurate.", df, dfapprox, 1e-6);
    }

    private Pair<Double, Double> getMinMax() {
        double x1 = 2.0;
        double x2 = 3.0;
        double fx1 = f.value(x1);
        double fx2 = f.value(x2);
        if (fx1 * fx2 > 0) {
            throw new IllegalStateException("Function values have the same sign. The function is not bracketing the root.");
        }
        return new Pair<>(x1, x2);
    }

    private double getMin() {
        Pair<Double, Double> bounds = getMinMax();
        return bounds.getKey();
    }

    private double getMax() {
        Pair<Double, Double> bounds = getMinMax();
        return bounds.getValue();
    }
}