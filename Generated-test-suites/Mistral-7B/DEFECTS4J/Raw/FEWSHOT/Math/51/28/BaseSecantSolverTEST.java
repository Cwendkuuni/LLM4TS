package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.RootNotFoundException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolverRegulaFalsi() {
        double[] x = new double[]{0.0, 1.0, 2.0};
        double[] fx = new double[]{1.0, 0.0, -1.0};
        UnivariateRealFunction function = new PolynomialFunction(2, x);
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);

        try {
            assertEquals(1.0, Precision.round(solver.solve(30, function, 0.0, 2.0, 1.0), 6), 1e-6);
        } catch (RootNotFoundException | MaxEvalExceededException | NumberIsTooSmallException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testSolverIllinois() {
        double[] x = new double[]{0.0, 1.0, 2.0};
        double[] fx = new double[]{1.0, 0.0, -1.0};
        UnivariateRealFunction function = new PolynomialFunction(2, x);
        BaseSecantSolver solver = new IllinoisSolver(1e-6);

        try {
            assertEquals(1.0, Precision.round(solver.solve(30, function, 0.0, 2.0, 1.0), 6), 1e-6);
        } catch (RootNotFoundException | MaxEvalExceededException | NumberIsTooSmallException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testSolverPegasus() {
        double[] x = new double[]{0.0, 1.0, 2.0};
        double[] fx = new double[]{1.0, 0.0, -1.0};
        UnivariateRealFunction function = new PolynomialFunction(2, x);
        BaseSecantSolver solver = new PegasusSolver(1e-6);

        try {
            assertEquals(1.0, Precision.round(solver.solve(30, function, 0.0, 2.0, 1.0), 6), 1e-6);
        } catch (RootNotFoundException | MaxEvalExceededException | NumberIsTooSmallException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolverInvalidConstructor() {
        new BaseSecantSolver(null, Method.PEGASUS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolverInvalidMethod() {
        new BaseSecantSolver(1e-6, null);
    }
}