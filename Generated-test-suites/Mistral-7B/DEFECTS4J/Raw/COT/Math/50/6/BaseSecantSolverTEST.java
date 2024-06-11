package org.apache.commons.math.analysis.solvers.test;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class BaseSecantSolverTest {

    @Test
    public void testSolver_methodRegulaFalsi() {
        double absAccuracy = 1e-6;
        double relAccuracy = 1e-4;
        BaseSecantSolver solver = new BaseSecantSolver(relAccuracy, absAccuracy, UnivariateRealFunctionTestUtils.F_X_SQUARED, BaseSecantSolver.Method.REGULA_FALSI);
        double root = 2.0;
        double result = solver.solve(10, UnivariateRealFunctionTestUtils.F_X_SQUARED, -10.0, 10.0, root);
        Assert.assertEquals(Precision.round(root, 7), Precision.round(result, 7));
    }

    @Test
    public void testSolver_methodIllinois() {
        double absAccuracy = 1e-6;
        double relAccuracy = 1e-4;
        BaseSecantSolver solver = new BaseSecantSolver(relAccuracy, absAccuracy, UnivariateRealFunctionTestUtils.F_X_SQUARED, BaseSecantSolver.Method.ILLINOIS);
        double root = 2.0;
        double result = solver.solve(10, UnivariateRealFunctionTestUtils.F_X_SQUARED, -10.0, 10.0, root);
        Assert.assertEquals(Precision.round(root, 7), Precision.round(result, 7));
    }

    @Test
    public void testSolver_methodPegasus() {
        double absAccuracy = 1e-6;
        double relAccuracy = 1e-4;
        BaseSecantSolver solver = new BaseSecantSolver(relAccuracy, absAccuracy, UnivariateRealFunctionTestUtils.F_X_SQUARED, BaseSecantSolver.Method.PEGASUS);
        double root = 2.0;
        double result = solver.solve(10, UnivariateRealFunctionTestUtils.F_X_SQUARED, -10.0, 10.0, root);
        Assert.assertEquals(Precision.round(root, 7), Precision.round(result, 7));
    }

    @Test(expected = MaxEvalException.class)
    public void testSolver_maxEvalReached() {
        double absAccuracy = 1e-6;
        double relAccuracy = 1e-4;
        BaseSecantSolver solver = new BaseSecantSolver(relAccuracy, absAccuracy, UnivariateRealFunctionTestUtils.F_X_SQUARED, BaseSecantSolver.Method.ILLINOIS);
        double root = 2.0;
        solver.solve(0, UnivariateRealFunctionTestUtils.F_X_SQUARED, -10.0, 10.0, root);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolver_nullFunction() {
        double absAccuracy = 1e-6;
        double relAccuracy = 1e-4;
        BaseSecantSolver solver = new BaseSecantSolver(relAccuracy, absAccuracy, null, BaseSecantSolver.Method.ILLINOIS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolver_emptyInterval() {
        double absAccuracy = 1e-6;
        double relAccuracy = 1e-4;
        BaseSecantSolver solver = new BaseSecantSolver(relAccuracy, absAccuracy, UnivariateRealFunctionTestUtils.F_X_SQUARED, BaseSecantSolver.Method.ILLINOIS);
        solver.solve(10, UnivariateRealFunctionTestUtils.F_X_SQUARED, -10.0, -10.0, 0.0);
    }

}