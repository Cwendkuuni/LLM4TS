package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.commons.math.exception.MathIllegalArgumentException;
import org.apache.commons.commons.math.exception.MathInternalError;
import org.apache.commons.commons.math.analysis.PolynomialFunction;
import org.apache.commons.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.commons.math.analysis.solvers.RegulaFalsiSolver;
import org.apache.commons.commons.math.analysis.solvers.SecantSolver;
import org.apache.commons.commons.math.util.FastMath;

public class BaseSecantSolverTest {

    @Test(expected = MathIllegalArgumentException.class)
    public void testSolverNullFunction() {
        new BaseSecantSolver(0.001, new BaseSecantSolver.Method(0)).solve(1, null, 0.0, 1.0, 0.5);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSolverNullMinMax() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 0.0});
        new BaseSecantSolver(0.001, new BaseSecantSolver.Method(0)).solve(1, f, null, null, 0.5);
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testSolverEmptyInterval() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1.0, 1.0, -1.0});
        new BaseSecantSolver(0.001, new BaseSecantSolver.Method(0)).solve(1, f, 0.0, 0.0, 0.5);
    }

    @Test
    public void testRegulaFalsiSolverBracketing() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1.0, 0.5, -0.25});
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(0.001);
        double root = solver.solve(1, f, 0.0, 1.0, 0.5);
        assertEquals(0.5, root, 1e-6);
    }

    @Test
    public void testIllinoisSolverBracketing() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 0.0});
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(0.001, new BaseSecantSolver.Method.ILLINOIS(), null);
        double root = solver.solve(1, f, 0.0, 1.0, 0.5);
        assertEquals(0.5, root, 1e-6);
    }

    @Test
    public void testPegasusSolverBracketing() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 0.0});
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(0.001, new BaseSecantSolver.Method.PEGASUS(), null);
        double root = solver.solve(1, f, 0.0, 1.0, 0.5);
        assertEquals(0.5, root, 1e-6);
    }

    @Test(expected = MathInternalError.class)
    public void testPegasusSolverSingularity() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{1.0, 0.0, 0.0});
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(0.001, new BaseSecantSolver.Method.PEGASUS(), null);
        solver.solve(1, f, 0.0, 1.0, 0.5);
    }

    @Test
    public void testDifferentMethodsWithSameFunction() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 0.0});
        BracketedUnivariateRealSolver<UnivariateRealFunction> solverRegulaFalsi = new RegulaFalsiSolver(0.001);
        double rootRegulaFalsi = solverRegulaFalsi.solve(1, f, 0.0, 1.0, 0.5);
        BracketedUnivariateRealSolver<UnivariateRealFunction> solverIllinois = new BaseSecantSolver(0.001, new BaseSecantSolver.Method.ILLINOIS(), null);
        double rootIllinois = solverIllinois.solve(1, f, 0.0, 1.0, 0.5);
        BracketedUnivariateRealSolver<UnivariateRealFunction> solverPegasus = new BaseSecantSolver(0.001, new BaseSecantSolver.Method.PEGASUS(), null);
        double rootPegasus = solverPegasus.solve(1, f, 0.0, 1.0, 0.5);
        assertEquals(rootRegulaFalsi, rootIllinois, 1e-6);
        assertEquals(rootRegulaFalsi, rootPegasus, 1e-6);
    }

    @Test(expected = MathInternalError.class)
    public void testDifferentMethodsWithDifferentFunction() {
        UnivariateRealFunction f1 = new PolynomialFunction(new double[]{1.0, 1.0, -0.25});
        UnivariateRealFunction f2 = new PolynomialFunction(new double[]{1.0, 0.5, -0.25});

        BracketedUnivariateRealSolver<UnivariateRealFunction> solverRegulaFalsi = new RegulaFalsiSolver(0.001);
        double rootRegulaFalsi = solverRegulaFalsi.solve(1, f1, 0.0, 1.0, 0.5);
        BracketedUnivariateRealSolver<UnivariateRealFunction> solverIllinois = new BaseSecantSolver(0.001, new BaseSecantSolver.Method.ILLINOIS(), null);
        solverIllinois.solve(1, f2, 0.0, 1.0, 0.5);
    }
}