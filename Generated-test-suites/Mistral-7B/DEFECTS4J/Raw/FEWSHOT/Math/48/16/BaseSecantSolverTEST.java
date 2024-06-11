package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.math.analysis.PolynomialFunction;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.RootFindersException;
import org.junit.Test;

import org.apache.commons.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.commons.math.analysis.solvers.Method;

public class BaseSecantSolverTest {

    @Test
    public void testSolver_RegulaFalsi() {
        UnivariateRealFunction function = new PolynomialFunction(3, -4.0, 1.0, 2.0);
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);

        try {
            double root = solver.solve(10, function, -5.0, 5.0, 0.0);
            assertEquals(1.0, root, 1e-8);
        } catch (RootFindersException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test(expected = RootFindersException.class)
    public void testSolver_RegulaFalsi_NoBracketing() {
        UnivariateRealFunction function = new PolynomialFunction(3, -4.0, 1.0, 2.0);
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);

        try {
            solver.solve(10, function, 5.0, 5.0, 0.0);
            fail("No Bracketing Exception should be thrown");
        } catch (NoBracketingException e) {
            // Expected
        } catch (RootFindersException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testSolver_RegulaFalsi_MaxEvalExceeded() {
        UnivariateRealFunction function = new PolynomialFunction(3, -4.0, 1.0, 2.0);
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);

        try {
            solver.solve(0, function, -5.0, 5.0, 0.0);
            fail("MaxEvalExceededException should be thrown");
        } catch (MaxEvalExceededException e) {
            // Expected
        } catch (RootFindersException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSolver_Illinois() {
        UnivariateRealFunction function = new PolynomialFunction(3, -4.0, 1.0, 2.0);
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);

        try {
            double root = solver.solve(10, function, -5.0, 5.0, 0.0);
            assertEquals(1.0, root, 1e-8);
        } catch (RootFindersException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test(expected = RootFindersException.class)
    public void testSolver_Illinois_NoBracketing() {
        UnivariateRealFunction function = new PolynomialFunction(3, -4.0, 1.0, 2.0);
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);

        try {
            solver.solve(10, function, 5.0, 5.0, 0.0);
            fail("No Bracketing Exception should be thrown");
        } catch (NoBracketingException e) {
            // Expected
        } catch (RootFindersException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testSolver_Illinois_MaxEvalExceeded() {
        UnivariateRealFunction function = new PolynomialFunction(3, -4.0, 1.0, 2.0);
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);

        try {
            solver.solve(0, function, -5.0, 5.0, 0.0);
            fail("MaxEvalExceededException should be thrown");
        } catch (MaxEvalExceededException e) {
            // Expected
        } catch (RootFindersException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSolver_Pegasus() {
        UnivariateRealFunction function = new PolynomialFunction(-3, 2.0, 1.0, 3.0);
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(1e-6, Method.PEGASUS);

        try {
            double root = solver.solve(10, function, -5.0, 5.0, 0.0);
            assertEquals(1.0, root, 1e-8);
        } catch (RootFindersException e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test(expected = RootFindersException.class)
    public void testSolver_Pegasus_NoBracketing() {
        UnivariateRealFunction function = new PolynomialFunction(-3, 2.0, 1.0, 3.0);
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(1e-6, Method.PEGASUS);

        try {
            solver.solve(10, function, 5.0, 5.0, 0.0);
            fail("No Bracketing Exception should be thrown");
        } catch (NoBracketingException e) {
            // Expected
        } catch (RootFindersException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testSolver_Pegasus_MaxEvalExceeded() {
        UnivariateRealFunction function = new PolynomialFunction(-3, 2.0, 1.0, 3.0);
        BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(1e-6, Method.PEGASUS);

        try {
            solver.solve(0, function, -5.0, 5.0, 0.0);
            fail("MaxEvalExceededException should be thrown");
        } catch (MaxEvalExceededException e) {
            // Expected
        } catch (RootFindersException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}