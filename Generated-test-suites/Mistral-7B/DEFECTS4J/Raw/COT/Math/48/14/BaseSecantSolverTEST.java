package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math3.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math3.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.math3.analysis.solvers.IllinoisSolver;
import org.apache.commons.math3.analysis.solvers.PegasusSolver;
import org.junit.Test;

/**
 * Tests for the {@link BaseSecantSolver} class.
 */
public class BaseSecantSolverTest {

    /**
     * Test the {@link BaseSecantSolver#BaseSecantSolver(double, Method)} constructor.
     * @throws ConvergenceException If an error occurs during the test.
     */
    @Test
    public void testBaseSecantSolver_Constructor_Double_Method()
            throws ConvergenceException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(100, f, -10, 10, 0.0);
        assertEquals("The root should be approximately -1.4142135625",
                Precision.round(FastMath.sqrt(2), 15), root);
    }

    /**
     * Test the {@link BaseSecantSolver#BaseSecantSolver(double, double, Method)} constructor.
     * @throws ConvergenceException If an error occurs during the test.
     */
    @Test
    public void testBaseSecantSolver_Constructor_Double_Double_Method()
            throws ConvergenceException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, 1e-8, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(100, f, -10, 10, 0.0);
        assertEquals("The root should be approximately -1.4142135625",
                Precision.round(FastMath.sqrt(2), 15), root);
    }

    /**
     * Test the {@link BaseSecantSolver#BaseSecantSolver(double, double, double, Method)} constructor.
     * @throws ConvergenceException If an error occurs during the test.
     */
    @Test
    public void testBaseSecantSolver_Constructor_Double_Double_Double_Method()
            throws ConvergenceException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, 1e-8, 1e-8, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(100, f, -10, 10, 0.0);
        assertEquals("The root should be approximately -1.4142135625",
                Precision.round(FastMath.sqrt(2), 15), root);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method with {@link RegulaFalsiSolver}.
     * @throws ConvergenceException If an error occurs during the test.
     */
    @Test
    public void testSolve_RegulaFalsi()
            throws ConvergenceException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        double root = solver.solve(100, f, -10, 10, 0.0);
        assertEquals("The root should be approximately -1.4142135625",
                Precision.round(FastMath.sqrt(2), 15), root);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method with {@link IllinoisSolver}.
     * @throws ConvergenceException If an error occurs during the test.
     */
    @Test
    public void testSolve_Illinois()
            throws ConvergenceException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        BaseSecantSolver solver = new IllinoisSolver(1e-6);
        double root = solver.solve(100, f, -FastMath.PI, FastMath.PI, 0.0);
        assertEquals("The root should be approximately 0.643496",
                Precision.round(Math.PI / 2, 15), root);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method with {@link PegasusSolver}.
     * @throws ConvergenceException If an error occurs during the test.
     */
    @Test
    public void testSolve_Pegasus()
            throws ConvergenceException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - 2;
            }
        };
        BaseSecantSolver solver = new PegasusSolver(1e-6, 1e-8);
        double root = solver.solve(100, f, -10, 10, 0.0);
        assertEquals("The root should be approximately 0.69314718056",
                Precision.round(Math.log(2), 15), root);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method when function is constant.
     * @throws ConvergenceException If an error occurs during the test.
     */
    @Test(expected = ConvergenceException.class)
    public void testSolve_ConstantFunction()
            throws ConvergenceException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 2;
            }
        };
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        solver.solve(100, f, -10, 10, 0.0);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method when function is not monotonic.
     * @throws ConvergenceException If an error occurs during the test.
     * @throws MathIllegalArgumentException If an error occurs during the test.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testSolve_NonMonotonicFunction()
            throws ConvergenceException, MathIllegalArgumentException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x > 0) {
                    return x * x;
                } else {
                    return -x * x;
                }
            }
        };
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        solver.solve(100, f, -10, 10, 0.0);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method when function is not bracketing the root.
     * @throws ConvergenceException If an error occurs during the test.
     * @throws MathIllegalArgumentException If an error occurs during the test.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testSolve_NotBracketingFunction()
            throws ConvergenceException, MathIllegalArgumentException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 3;
            }
        };
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        solver.solve(100, f, 1, 2, 0.0);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method when maximum number of iterations is exceeded.
     * @throws ConvergenceException If an error occurs during the test.
     * @throws MaxIterationsExceededException If an error occurs during the test.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testSolve_MaxIterationsExceeded()
            throws ConvergenceException, MaxIterationsExceededException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        };
        BaseSecantSolver solver = new IllinoisSolver(1e-6);
        solver.setMaxIterations(0);
        solver.solve(100, f, -FastMath.PI, FastMath.PI, 0.0);
    }

    /**
     * Test the {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)} method when accuracy is too high.
     * @throws ConvergenceException If an error occurs during the test.
     */
    @Test
    public void testSolve_HighAccuracy()
            throws ConvergenceException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.exp(x) - 2;
            }
        };
        BaseSecantSolver solver = new PegasusSolver(1e-20, 1e-20);
        double root = solver.solve(100, f, -10, 10, 0.0);
        assertEquals("The root should be approximately 0.69314718056",
                Precision.round(Math.log(2), 15), root);
    }

}