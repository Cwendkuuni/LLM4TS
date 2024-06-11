package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_RegulaFalsi() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, 1.0}, 3);
        BaseSecantSolver solver = new IllinoisSolver(1e-6, Method.REGULA_FALSI);
        double result = 0.0;
        try {
            result = solver.solve(10, f, -1.0, 1.0, 0.5);
        } catch (MaxEvalException e) {
            fail("Solver should not have thrown MaxEvalException");
        }
        assertEquals(0.0, result, 1e-6);
    }

    @Test
    public void testSolve_Illinois() {
        UnivariateFunction f = new PolynomialFunction(new double[]{1.0, -2.0, 1.0}, 3);
        BaseSecantSolver solver = new IllinoisSolver(1e-6, Method.ILLINOIS);
        double result = 1.0;
        try {
            result = solver.solve(10, f, -2.0, 2.0, 1.0);
        } catch (MaxEvalException e) {
            fail("Solver should not have thrown MaxEvalException");
        }
        assertEquals(1.0, result, 1e-6);
    }

    @Test
    public void testSolve_Pegasus() {
        UnivariateFunction f = new PolynomialFunction(new double[]{1.0, -2.0, 1.0}, 3);
        BaseSecantSolver solver = new PegasusSolver(1e-6, Method.PEGASUS);
        double result = 1.0;
        try {
            result = solver.solve(10, f, -2.0, 2.0, 1.0);
        } catch (MaxEvalException e) {
            fail("Solver should not have thrown MaxEvalException");
        }
        assertEquals(1.0, result, 1e-6);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSolve_MethodNotSupported() {
        UnivariateFunction f = new PolynomialFunction(new double[]{1.0, -2.0, 1.0}, 3);
        BaseSecantSolver solver = new PegasusSolver(1e-6, Method.REGULA_FALSI);
        solver.solve(10, f, -2.0, 2.0, 1.0);
    }
}