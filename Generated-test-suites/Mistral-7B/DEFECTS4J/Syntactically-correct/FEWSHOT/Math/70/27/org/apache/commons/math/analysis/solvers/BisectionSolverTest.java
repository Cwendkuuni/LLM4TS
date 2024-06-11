package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.solvers.BisectionSolver;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testBisectionSolver() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, new double[]{0.0});
        BisectionSolver solver = new BisectionSolver();

        try {
            double result = solver.solve(f, -10.0, 10.0);
            assertEquals(0.0, result, 1e-6);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() {
        UnivariateRealFunction f = new FunctionUtils.SineFunction();
        BisectionSolver solver = new BisectionSolver();

        try {
            solver.solve(f, -10.0, 10.0);
            fail("MaxIterationsExceededException should be thrown");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }
    }

    @Test
    public void testBisectionSolverWithInitialValue() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, new double[]{0.0});
        BisectionSolver solver = new BisectionSolver();

        try {
            double result = solver.solve(f, -5.0, 5.0, 0.0);
            assertEquals(0.0, result, 1e-6);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceededWithInitialValue() {
        UnivariateRealFunction f = new FunctionUtils.SineFunction();
        BisectionSolver solver = new BisectionSolver();

        try {
            solver.solve(f, -10.0, 10.0, 0.0);
            fail("MaxIterationsExceededException should be thrown");
        } catch (MaxIterationsExceededException e) {
            // Expected
        }
    }

    @Test
    public void testVerifyInterval() {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, new double[]{0.0});
        BisectionSolver solver = new BisectionSolver();

        try {
            solver.solve(f, 10.0, -10.0);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}