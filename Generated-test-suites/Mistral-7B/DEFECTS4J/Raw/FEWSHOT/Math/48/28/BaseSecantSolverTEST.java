package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.commons.math.analysis.PolynomialFunction;
import org.apache.commons.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.commons.math.exception.MaxEvalExceededException;
import org.apache.commons.commons.math.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.commons.math.solvers.RegulaFalsiSolver;
import org.apache.commons.commons.math.solvers.SecantSolver;

public class BaseSecantSolverTest {

    @Test
    public void testSolver_RegulaFalsi() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1, 4, 0, 5, 0, 3, 0, 1, 0, 0, 1});
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(1e-6);
        try {
            double result = solver.solve(10, function, -5.0, 5.0);
            assertEquals(0.0, result, 1e-6);
        } catch (MaxEvalExceededException e) {
            fail("Solver should have found the root within the given number of evaluations.");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolver_RegulaFalsi_NullFunction() {
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(1e-6);
        solver.solve(10, null, 0.0, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolver_RegulaFalsi_InvalidBounds() {
        UnivariateRealFunction function = (x) -> Math.pow(x, 2) - 1;
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(1e-6);
        solver.solve(10, function, 1.0, 0.0);
    }

    @Test
    public void testSolver_Illinois() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1, 4, 0, 5, 0, 3, 0, 1, 0, 0, 1});
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(1e-6, 1e-6, 1e-6, Method.ILLINOIS);
        try {
            double result = solver.solve(10, function, -5.0, 5.0);
            assertEquals(0.0, result, 1e-6);
        } catch (MaxEvalExceededException e) {
            fail("Solver should have found the root within the given number of evaluations.");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolver_Illinois_NullFunction() {
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(1e-6, 1e-6, 1e-6, Method.ILLINOIS);
        solver.solve(10, null, 0.0, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolver_Illinois_InvalidBounds() {
        UnivariateRealFunction function = (x) -> Math.pow(x, 2) - 1;
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(1e-6, 1e-6, 1e-6, Method.ILLINOIS);
        solver.solve(10, function, 1.0, 0.0);
    }

    @Test
    public void testSolver_Pegasus() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1, 4, 0, 5, 0, 3, 0, 1, 0, 0, 1});
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(1e-6, 1e-6, 1e-6, 1e-6, Method.PEGASUS);
        try {
            double result = solver.solve(10, function, -5.0, 5.0);
            assertEquals(0.0, result, 1e-6);
        } catch (MaxEvalExceededException e) {
            fail("Solver should have found the root within the given number of evaluations.");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolver_Pegasus_NullFunction() {
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(1e-6, 1e-6, 1e-6, 1e-6, Method.PEGASUS);
        solver.solve(10, null, 0.0, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolver_Pegasus_InvalidBounds() {
        UnivariateRealFunction function = (x) -> Math.pow(x, 2) - 1;
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new RegulaFalsiSolver(1e-6, 1e-6, 1e-6, 1e-6, Method.PEGASUS);
        solver.solve(10, function, 1.0, 0.0);
    }
}