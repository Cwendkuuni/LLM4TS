package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.apache.commons.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.commons.math.exception.MathIllegalArgumentException;
import org.apache.commons.commons.math.exception.MathInternalError;
import org.apache.commons.commons.math.util.Precision;
import org.apache.commons.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.commons.math.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.commons.math.analysis.solvers.IllinoisSolver;
import org.apache.commons.commons.math.analysis.solvers.PegasusSolver;

public class BaseSecantSolverTest {

    /**
     * Test constructor with default values and {@link RegulaFalsiSolver} method.
     */
    @Test
    public void testRegulaFalsiSolverDefault() {
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, RegulaFalsiSolver.INSTANCE);
        assertEquals(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(RegulaFalsiSolver.INSTANCE, solver.getMethod());
    }

    /**
     * Test constructor with custom values and {@link RegulaFalsiSolver} method.
     */
    @Test
    public void testRegulaFalsiSolverCustom() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, 0.0001, RegulaFalsiSolver.INSTANCE);
        assertEquals(0.001, solver.getRelativeAccuracy(), 0.0);
        assertEquals(0.0001, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(RegulaFalsiSolver.INSTANCE, solver.getMethod());
    }

    /**
     * Test constructor with custom values and {@link IllinoisSolver} method.
     */
    @Test
    public void testIllinoisSolverCustom() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, 0.0001, IllinoisSolver.INSTANCE);
        assertEquals(0.001, solver.getRelativeAccuracy(), 0.0);
        assertEquals(0.0001, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(IllinoisSolver.INSTANCE, solver.getMethod());
    }

    /**
     * Test constructor with custom values and {@link PegasusSolver} method.
     */
    @Test
    public void testPegasusSolverCustom() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, 0.0001, PegasusSolver.INSTANCE);
        assertEquals(0.001, solver.getRelativeAccuracy(), 0.0);
        assertEquals(0.0001, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(PegasusSolver.INSTANCE, solver.getMethod());
    }

    /**
     * Test constructor with custom values and invalid method.
     */
    @Test(expected = MathInternalError.class)
    public void testInvalidMethod() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, 0.0001, new Object());
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method with {@link RegulaFalsiSolver} method.
     */
    @Test
    public void testRegulaFalsiSolver() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, RegulaFalsiSolver.INSTANCE);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        double root = solver.solve(10, function, -10, 10, 0.0);
        assertFalse(Double.isNaN(root));
        assertEquals(0.0, root, 1e-5);
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)} method with {@link RegulaFalsiSolver} method.
     */
    @Test
    public void testRegulaFalsiSolverAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, RegulaFalsiSolver.INSTANCE);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        double root = solver.solve(10, function, -10, 10, 0.0, AllowedSolution.LEFT_SIDE);
        assertFalse(Double.isNaN(root));
        assertEquals(0.0, root, 1e-5);
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method with {@link IllinoisSolver} method.
     */
    @Test
    public void testIllinoisSolver() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, IllinoisSolver.INSTANCE);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        double root = solver.solve(10, function, -10, 10, 0.0);
        assertFalse(Double.isNaN(root));
        assertEquals(0.0, root, 1e-5);
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)} method with {@link IllinoisSolver} method.
     */
    @Test
    public void testIllinoisSolverAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, IllinoisSolver.INSTANCE);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        double root = solver.solve(10, function, -10, 10, 0.0, AllowedSolution.LEFT_SIDE);
        assertFalse(Double.isNaN(root));
        assertEquals(0.0, root, 1e-5);
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method with {@link PegasusSolver} method.
     */
    @Test
    public void testPegasusSolver() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, PegasusSolver.INSTANCE);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        double root = solver.solve(10, function, -10, 10, 0.0);
        assertFalse(Double.isNaN(root));
        assertEquals(0.0, root, 1e-5);
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)} method with {@link PegasusSolver} method.
     */
    @Test
    public void testPegasusSolverAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, PegasusSolver.INSTANCE);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        double root = solver.solve(10, function, -10, 10, 0.0, AllowedSolution.LEFT_SIDE);
        assertFalse(Double.isNaN(root));
        assertEquals(0.0, root, 1e-5);
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)} method with invalid function.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testSolveInvalidFunction() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, RegulaFalsiSolver.INSTANCE);
        UnivariateRealFunction function = null;
        solver.solve(10, function, -10, 10, 0.0);
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)} method with invalid bounds.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testSolveInvalidBounds() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, RegulaFalsiSolver.INSTANCE);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        solver.solve(10, function, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0.0);
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method with invalid start value.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testSolveInvalidStartValue() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, RegulaFalsiSolver.INSTANCE);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        solver.solve(10, function, Double.POSITIVE_INFINITY, 10, 0.0);
    }

    /**
     * Test {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)} method with invalid start value.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testSolveInvalidStartValueAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(0.001, RegulaFalsiSolver.INSTANCE);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        solver.solve(10, function, Double.POSITIVE_INFINITY, 10, 0.0, AllowedSolution.LEFT_SIDE);
    }
}