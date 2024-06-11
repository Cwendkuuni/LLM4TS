package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import org.apache.commons.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.commons.math.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.commons.math.analysis.solvers.IllinoisSolver;
import org.apache.commons.commons.math.analysis.solvers.PegasusSolver;
import org.apache.commons.commons.math.analysis.solvers.AllowedSolution;

@RunWith(JUnit4.class)
public class BaseSecantSolverTest {

    @Test
    public void testRegulaFalsi() {
        UnivariateFunction f = new PolynomialFunction(2, new double[]{-1.0, 1.0, 1.0});
        BracketedUnivariateRealSolver<UnivariateFunction> solver = new RegulaFalsiSolver(1.0E-6);
        double root = solver.solve(f, -1.0, 1.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root, 1.0E-8);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testRegulaFalsiStuck() {
        UnivariateFunction f = new PolynomialFunction(2, new double[]{-1.0, 1.0, 1.0, 1.0, 1.0});
        BracketedUnivariateRealSolver<UnivariateFunction> solver = new RegulaFalsiSolver(1.0E-12);
        solver.solve(f, -1.0, 1.0, AllowedSolution.ANY_SIDE);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testRegulaFalsiMaxIterations() {
        UnivariateFunction f = new PolynomialFunction(2, new double[]{-1.0, 1.0, 1.0, 1.0, 1.0});
        BracketedUnivariateRealSolver<UnivariateFunction> solver = new RegulaFalsiSolver(1.0E-6, 10);
        solver.solve(f, -1.0, 1.0, AllowedSolution.ANY_SIDE);
    }

    @Test
    public void testIllinois() {
        UnivariateFunction f = new PolynomialFunction(2, new double[]{-1.0, 1.0, 1.0});
        BracketedUnivariateRealSolver<UnivariateFunction> solver = new IllinoisSolver(1.0E-6);
        double root = solver.solve(f, -1.0, 1.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root, 1.0E-8);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testIllinoisStuck() {
        UnivariateFunction f = new PolynomialFunction(2, new double[]{-1.0, 1.0, 1.0, 1.0, 1.0});
        BracketedUnivariateRealSolver<UnivariateFunction> solver = new IllinoisSolver(1.0E-12);
        solver.solve(f, -1.0, 1.0, AllowedSolution.ANY_SIDE);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testIllinoisMaxIterations() {
        UnivariateFunction f = new PolynomialFunction(2, new double[]{-1.0, 1.0, 1.0, 1.0, 1.0});
        BracketedUnivariateRealSolver<UnivariateFunction> solver = new IllinoisSolver(1.0E-6, 10);
        solver.solve(f, -1.0, 1.0, AllowedSolution.ANY_SIDE);
    }

    @Test
    public void testPegasus() {
        UnivariateFunction f = new PolynomialFunction(2, new double[]{-1.0, 1.0, 1.0});
        BracketedUnivariateRealSolver<UnivariateFunction> solver = new PegasusSolver(1.0E-6);
        double root = solver.solve(f, -1.0, 1.0, AllowedSolution.ANY_SIDE);
        assertEquals(0.0, root, 1.0E-8);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testPegasusStuck() {
        UnivariateFunction f = new PolynomialFunction(2, new double[]{-1.0, 1.0, 1.0, 1.0, 1.0});
        BracketedUnivariateRealSolver<UnivariateFunction> solver = new PegasusSolver(1.0E-12);
        solver.solve(f, -1.0, 1.0, AllowedSolution.ANY_SIDE);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testPegasusMaxIterations() {
        UnivariateFunction f = new PolynomialFunction(2, new double[]{-1.0, 1.0, 1.0, 1.0, 1.0});
        BracketedUnivariateRealSolver<UnivariateFunction> solver = new PegasusSolver(1.0E-6, 10);
        solver.solve(f, -1.0, 1.0, AllowedSolution.ANY_SIDE);
    }

    @Test
    public void testAllowedSolutions() {
        UnivariateFunction f = new PolynomialFunction(2, new double[]{-1.0, 1.0, 1.0});
        double min = -1.0;
        double max = 1.0;
        double startValue = 0.0;

        BracketedUnivariateRealSolver<UnivariateFunction> solver = new BaseSecantSolver(1.0E-6, Method.REGULA_FALSI);
        double root1 = solver.solve(f, min, max, AllowedSolution.ANY_SIDE);
        double root2 = solver.solve(f, min, max, AllowedSolution.LEFT_SIDE);
        double root3 = solver.solve(f, min, max, AllowedSolution.RIGHT_SIDE);
        double root4 = solver.solve(f, min, max, AllowedSolution.BELOW_SIDE);
        double root5 = solver.solve(f, min, max, AllowedSolution.ABOVE_SIDE);

        assertEquals(0.0, root1, 1.0E-8);
        assertEquals(0.0, root2, 1.0E-8);
        assertEquals(0.0, root3, 1.0E-8);
        assertFalse(root4 > 0.0);
        assertFalse(root5 < 0.0);
    }
}