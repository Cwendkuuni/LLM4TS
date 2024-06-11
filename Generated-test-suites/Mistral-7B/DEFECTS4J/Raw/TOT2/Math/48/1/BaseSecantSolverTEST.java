package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;

import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.AllowedSolution;
import org.apache.commons.math.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.math.analysis.solvers.IllinoisSolver;
import org.apache.commons.math.analysis.solvers.PegasusSolver;

public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, Method)}
     */
    @org.junit.Test
    public void testConstructionWithDefaults() {
        BaseSecantSolver baseSolver = new BaseSecantSolver(BaseSecantSolver.Method.REGULA_FALSI);
        assertNotNull(baseSolver);
        assertEquals(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, baseSolver.getAbsoluteAccuracy(), 0.0);
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, baseSolver.getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, Method)}
     */
    @org.junit.Test
    public void testConstructionWithRelativeAndAbsoluteAccuracies() {
        BaseSecantSolver baseSolver = new BaseSecantSolver(0.01, 0.001, BaseSecantSolver.Method.REGULA_FALSI);
        assertNotNull(baseSolver);
        assertEquals(0.01, baseSolver.getRelativeAccuracy(), 0.0);
        assertEquals(0.001, baseSolver.getAbsoluteAccuracy(), 0.0);
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, baseSolver.getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, double, Method)}
     */
    @org.junit.Test
    public void testConstructionWithRelativeAccuracyFunctionValueAccuracyAndMethod() {
        BaseSecantSolver baseSolver = new BaseSecantSolver(0.01, 0.001, 0.0001, BaseSecantSolver.Method.REGULA_FALSI);
        assertNotNull(baseSolver);
        assertEquals(0.01, baseSolver.getRelativeAccuracy(), 0.0);
        assertEquals(0.001, baseSolver.getAbsoluteAccuracy(), 0.0);
        assertEquals(0.0001, baseSolver.getFunctionValueAccuracy(), 0.0);
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, baseSolver.getMethod());
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     */
    @org.junit.Test
    public void testSolveWithMinAndMax() {
        UnivariateFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0});
        BaseSecantSolver baseSolver = new RegulaFalsiSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double root = baseSolver.solve(function, -1.0, 1.0);
        assertEquals(0.0, root, 1e-6);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     */
    @org.junit.Test
    public void testSolveWithMinAndMaxAndAllowedSolution() {
        UnivariateFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0});
        BaseSecantSolver baseSolver = new RegulaFalsiSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double root = baseSolver.solve(function, -1.0, 1.0, AllowedSolution.LEFT_SIDE);
        assertEquals(0.0, root, 1e-6);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * with function that has no bracketing
     */
    @org.junit.Test(expected = ConvergenceException.class)
    public void testSolveWithNoBracketing() {
        UnivariateFunction function = new PolynomialFunction(new double[]{1.0, 1.0});
        BaseSecantSolver baseSolver = new RegulaFalsiSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        baseSolver.solve(function, 1.0, 1.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * with function that has no bracketing and Left_Side allowed solution
     */
    @org.junit.Test(expected = ConvergenceException.class)
    public void testSolveWithNoBracketingAndAllowedLeftSide() {
        UnivariateFunction function = new PolynomialFunction(new double[]{1.0, 1.0});
        BaseSecantSolver baseSolver = new RegulaFalsiSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        baseSolver.solve(function, 1.0, 1.0, AllowedSolution.LEFT_SIDE);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}
     * with function that has no convergence
     */
    @org.junit.Test(expected = MaxEvalExceededException.class)
    public void testSolveWithMaxEvalExceeded() {
        UnivariateFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, 1.0});
        BaseSecantSolver baseSolver = new PegasusSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        baseSolver.solve(function, -1.0, 1.0, 1000);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}
     * with function that has no convergence and Left_Side allowed solution
     */
    @org.junit.Test(expected = MaxEvalExceededException.class)
    public void testSolveWithMaxEvalExceededAndAllowedLeftSide() {
        UnivariateFunction function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, 1.0});
        BaseSecantSolver baseSolver = new PegasusSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        baseSolver.solve(function, -1.0, 1.0, 1000, AllowedSolution.LEFT_SIDE);
    }
}