package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;

public class BaseSecantSolverTest {

    @Test
    public void testSolver_RegulaFalsi_PolynomialFunction() {
        double relativeAccuracy = 1e-6;
        double absoluteAccuracy = 1e-8;
        double functionValueAccuracy = 1e-8;
        Method method = Method.REGULA_FALSI;

        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{0, 1, 1});

        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, method);
        double root = solver.solve(10, f, -1.5, 1.5, 0.0);
        assertEquals(0.0, root, 1e-8);
    }

    @Test
    public void testSolver_Illinois_PolynomialFunction() {
        double relativeAccuracy = 1e-6;
        double absoluteAccuracy = 1e-8;
        double functionValueAccuracy = 1e-8;
        Method method = Method.ILLINOIS;

        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{0, 1, 1});

        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, method);
        double root = solver.solve(10, f, -1.5, 1.5, 0.0);
        assertEquals(0.0, root, 1e-8);
    }

    @Test
    public void testSolver_Pegasus_PolynomialFunction() {
        double relativeAccuracy = 1e-6;
        double absoluteAccuracy = 1e-8;
        double functionValueAccuracy = 1e-8;
        Method method = Method.PEGASUS;

        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1, 1, 1}, new double[]{0, 1, 1});

        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, method);
        double root = solver.solve(10, f, -1.5, 1.5, 0.0);
        assertEquals(0.0, root, 1e-8);
    }

    @Test
    public void testSolver_RegulaFalsi_FunctionUtils_Sin() {
        double relativeAccuracy = 1e-6;
        double absoluteAccuracy = 1e-8;
        double functionValueAccuracy = 1e-8;
        Method method = Method.REGULA_FALSI;

        UnivariateRealFunction f = FunctionUtils.sin;

        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, method);
        double root = solver.solve(10, f, -1.5, 1.5, 0.0);
        assertEquals(0.0, Math.PI / 2, root);
    }

    @Test
    public void testSolver_Illinois_FunctionUtils_Sin() {
        double relativeAccuracy = 1e-6;
        double absoluteAccuracy = 1e-8;
        double functionValueAccuracy = 1e-8;
        Method method = Method.ILLINOIS;

        UnivariateRealFunction f = FunctionUtils.sin;

        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, method);
        double root = solver.solve(10, f, -1.5, 1.5, 0.0);
        assertEquals(0.0, Math.PI / 2, root);
    }

    @Test
    public void testSolver_Pegasus_FunctionUtils_Sin() {
        double relativeAccuracy = 1e-6;
        double absoluteAccuracy = 1e-8;
        double functionValueAccuracy = 1e-8;
        Method method = Method.PEGASUS;

        UnivariateRealFunction f = FunctionUtils.sin;

        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, method);
        double root = solver.solve(10, f, -1.5, 1.5, 0.0);
        assertEquals(0.0, Math.PI / 2, root);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolver_RegulaFalsi_FunctionUtils_SquareRootOfNegativeNumber() {
        double relativeAccuracy = 1e-6;
        double absoluteAccuracy = 1e-8;
        double functionValueAccuracy = 1e-8;
        Method method = Method.REGULA_FALSI;

        UnivariateRealFunction f = x -> Math.sqrt(x) * Math.sqrt(x) - 1.0;

        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, method);
        solver.solve(10, f, -1.0, 1.0, 0.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolver_Illinois_FunctionUtils_SquareRootOfNegativeNumber() {
        double relativeAccuracy = 1e-6;
        double absoluteAccuracy = 1e-8;
        double functionValueAccuracy = 1e-8;
        Method method = Method.ILLINOIS;

        UnivariateRealFunction f = x -> Math.sqrt(x) * Math.sqrt(x) - 1.0;

        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, method);
        solver.solve(10, f, -1.0, 1.0, 0.0);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolver_Pegasus_FunctionUtils_SquareRootOfNegativeNumber() {
        double relativeAccuracy = 1e-6;
        double absoluteAccuracy = 1e-8;
        double functionValueAccuracy = 1e-8;
        Method method = Method.PEGASUS;

        UnivariateRealFunction f = x -> Math.sqrt(x) * Math.sqrt(x) - 1.0;

        BaseSecantSolver solver = new BaseSecantSolver(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, method);
        solver.solve(10, f, -1.0, 1.0, 0.0);
    }
}