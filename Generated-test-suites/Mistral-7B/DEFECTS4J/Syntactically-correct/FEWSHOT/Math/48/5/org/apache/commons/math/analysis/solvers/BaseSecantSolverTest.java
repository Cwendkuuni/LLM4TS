package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.commons.math.analysis.PolynomialFunction;
import org.apache.commons.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.commons.math.exception.ConvergenceException;
import org.apache.commons.commons.math.util.Precision;
import org.apache.commons.commons.math.util.FastMath;
import org.apache.commons.cli.BaseSecantSolver;
import org.apache.commons.cli.BracketedUnivariateRealSolver;
import org.apache.commons.cli.AllowedSolution;
import org.apache.commons.cli.BaseSecantSolver.Method;

public class BaseSecantSolverTest {

    @Test
    public void testSolver_RegulaFalsi() {
        double relativeAccuracy = 1e-6;
        double absoluteAccuracy = 1e-6;
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{0, 1, 1});
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, Method.REGULA_FALSI);
        double root = solver.solve(10, function, -1.0, 1.0, AllowedSolution.ANY_SIDE);
        assertTrue(Precision.isClose(root, 0.0, 1e-5));
    }

    @Test
    public void testSolver_Illinois() {
        double relativeAccuracy = 1e-6;
        double absoluteAccuracy = 1e-6;
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{0, 1, 1});
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, Method.ILLINOIS);
        double root = solver.solve(10, function, -1.0, 1.0, AllowedSolution.ANY_SIDE);
        assertTrue(Precision.isClose(root, 0.0, 1e-5));
    }

    @Test
    public void testSolver_Pegasus() {
        double relativeAccuracy = 1e-6;
        double absoluteAccuracy = 1e-6;
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{0, 1, 1});
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, Method.PEGASUS);
        double root = solver.solve(10, function, -1.0, 1.0, AllowedSolution.ANY_SIDE);
        assertTrue(Precision.isClose(root, 0.0, 1e-5));
    }

    @Test
    public void testSolver_RegulaFalsi_RelativeAccuracyAndFunctionValueAccuracy() {
        double relativeAccuracy = 1e-4;
        double absoluteAccuracy = 1e-6;
        double functionValueAccuracy = 1e-6;
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{0, 1, 1});
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, Method.REGULA_FALSI);
        double root = solver.solve(10, function, -1.0, 1.0, AllowedSolution.ANY_SIDE);
        assertTrue(Precision.isClose(root, 0.0, 1e-5));
    }

    @Test
    public void testSolver_Illinois_RelativeAccuracyAndFunctionValueAccuracy() {
        double relativeAccuracy = 1e-4;
        double absoluteAccuracy = 1e-6;
        double functionValueAccuracy = 1e-6;
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{0, 1, 1});
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, Method.ILLINOIS);
        double root = solver.solve(10, function, -1.0, 1.0, AllowedSolution.ANY_SIDE);
        assertTrue(Precision.isClose(root, 0.0, 1e-5));
    }

    @Test
    public void testSolver_Pegasus_RelativeAccuracyAndFunctionValueAccuracy() {
        double relativeAccuracy = 1e-4;
        double absoluteAccuracy = 1e-6;
        double functionValueAccuracy = 1e-6;
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{0, 1, 1});
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, Method.PEGASUS);
        double root = solver.solve(10, function, -1.0, 1.0, AllowedSolution.ANY_SIDE);
        assertTrue(Precision.isClose(root, 0.0, 1e-5));
    }

    @Test(expected = ConvergenceException.class)
    public void testSolver_RegulaFalsi_ConvergenceException() {
        double relativeAccuracy = 1e-4;
        double absoluteAccuracy = 1e-6;
        UnivariateRealFunction function = x -> FastMath.sin(x) / x;
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, Method.REGULA_FALSI);
        solver.solve(10, function, -1.0, 1.0, AllowedSolution.ANY_SIDE);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolver_Illinois_ConvergenceException() {
        double relativeAccuracy = 1e-4;
        double absoluteAccuracy = 1e-6;
        UnivariateRealFunction function = x -> FastMath.sin(x) / x;
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, Method.ILLINOIS);
        solver.solve(10, function, -1.0, 1.0, AllowedSolution.ANY_SIDE);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolver_Pegasus_ConvergenceException() {
        double relativeAccuracy = 1e-4;
        double absoluteAccuracy = 1e-6;
        UnivariateRealFunction function = x -> FastMath.sin(x) / x;
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, Method.PEGASUS);
        solver.solve(10, function, -1.0, 1.0, AllowedSolution.ANY_SIDE);
    }

}