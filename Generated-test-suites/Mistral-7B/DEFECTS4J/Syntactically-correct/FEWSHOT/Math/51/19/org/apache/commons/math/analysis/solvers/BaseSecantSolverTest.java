package org.apache.commons.cli.test;

import static org.apache.commons.math3.util.Precision.abs;
import static org.apache.commons.math3.util.Precision.equals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.BaseSecantSolver;
import org.apache.commons.cli.BracketedUnivariateRealSolver;
import org.apache.commons.cli.AllowedSolution;
import org.apache.commons.commons.math3.analysis.FunctionUtils;
import org.apache.commons.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.commons.math3.analysis.UnivariateRealFunction;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testRegulaFalsiSolver() {
        UnivariateRealFunction function = new PolynomialFunction(x -> x * x - 2);

        BaseSecantSolver solver = new BaseSecantSolver.RegulaFalsiSolver(1e-6);
        double root = solver.solve(function, -10.0, 10.0, AllowedSolution.ANY_SIDE);
        assertTrue(equals(0.0, root, 1e-6));
    }

    @Test
    public void testIllinoisSolver() {
        UnivariateRealFunction function = new PolynomialFunction(x -> x * x - 2);

        BaseSecantSolver solver = new BaseSecantSolver.IllinoisSolver(1e-6);
        double root = solver.solve(function, -10.0, 10.0, AllowedSolution.ANY_SIDE);
        assertTrue(equals(0.0, root, 1e-6));
    }

    @Test
    public void testPegasusSolver() {
        UnivariateRealFunction function = new PolynomialFunction(x -> x * x - 2);

        BaseSecantSolver solver = new BaseSecantSolver.PegasusSolver(1e-6);
        double root = solver.solve(function, -10.0, 10.0, AllowedSolution.ANY_SIDE);
        assertTrue(equals(0.0, root, 1e-6));
    }

    @Test
    public void testAllowedSolutions() {
        UnivariateRealFunction function = new PolynomialFunction(x -> x * x - 2);

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);

        double root = solver.solve(function, -10.0, 10.0, AllowedSolution.LEFT_SIDE);
        assertFalse(equals(0.0, root, 1e-6));

        root = solver.solve(function, -10.0, 10.0, AllowedSolution.RIGHT_SIDE);
        assertFalse(equals(0.0, root, 1e-6));

        root = solver.solve(function, -10.0, 10.0, AllowedSolution.BELOW_SIDE);
        assertFalse(equals(0.0, root, 1e-6));

        root = solver.solve(function, -10.0, 10.0, AllowedSolution.ABOVE_SIDE);
        assertFalse(equals(0.0, root, 1e-6));

        root = solver.solve(function, -10.0, 10.0, AllowedSolution.ANY_SIDE);
        assertTrue(equals(0.0, root, 1e-6));
    }

    @Test
    public void testMinMaxValues() {
        UnivariateRealFunction function = x -> x * x - 2;

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);

        double root = solver.solve(function, -10.0, 10.0, AllowedSolution.ANY_SIDE);
        assertTrue(equals(0.0, root, 1e-6));

        double min = solver.getMin();
        double max = solver.getMax();

        assertTrue(min < 0);
        assertTrue(max > 0);
    }

    @Test
    public void testFunctionValueAccuracy() {
        UnivariateRealFunction function = x -> x * x - 2;

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS, 1e-8);

        double root = solver.solve(function, -10.0, 10.0, AllowedSolution.ANY_SIDE);
        assertTrue(equals(0.0, root, 1e-6));

        double min = solver.getMin();
        double max = solver.getMax();

        double fMin = FunctionUtils.value(function, min);
        double fMax = FunctionUtils.value(function, max);

        assertTrue(abs(fMin) < 1e-8);
        assertTrue(abs(fMax) < 1e-8);
    }

    @Test
    public void testSolveWithStartValue() {
        UnivariateRealFunction function = x -> x * x - 2;

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);

        double root = solver.solve(function, -10.0, 10.0, AllowedSolution.ANY_SIDE, 3.0);
        assertTrue(equals(0.0, root, 1e-6));
    }

    @Test
    public void testVerifyBracketing() {
        UnivariateRealFunction function = x -> x * x - 2;

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);

        double min = solver.getMin();
        double max = solver.getMax();

        double x0 = min;
        double x1 = max;
        double f0 = FunctionUtils.value(function, x0);
        double f1 = FunctionUtils.value(function, x1);

        solver.verifyBracketing(x0, x1);

        x1 = x0 + 0.1;
        f1 = FunctionUtils.value(function, x1);

        assertFalse(solver.verifyBracketing(x0, x1));
    }

    @Test
    public void testGetMethods() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);

        assertTrue(solver.getMethods().length == 3);

        for (BaseSecantSolver.Method method : solver.getMethods()) {
            switch (method) {
            case REGULA_FALSI:
                break;
            case ILLINOIS:
                break;
            case PEGASUS:
                break;
            default:
                fail("Unexpected method: " + method);
            }
        }
    }
}