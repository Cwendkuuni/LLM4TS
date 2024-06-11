package org.apache.commons.math.analysis.solvers.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.TestUtils;
import org.apache.commons.math3.util.math.NumberTheoryUtils;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testBaseSecantSolver() {
        UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2.0;
            }
        };

        // Test with default constructor, which uses Regula Falsi method
        final BaseSecantSolver solver = new BaseSecantSolver(0.0);
        double result = solver.solve(testFunction, 1.0, 2.0);
        double expected = NumberTheoryUtils.sqrt(2.0);
        assertTrue(Precision.abs(result - expected) < 1e-6);

        // Test with custom constructor, which uses Illinois method
        solver = new BaseSecantSolver(0.0, BaseSecantSolver.Method.ILLINOIS);
        result = solver.solve(testFunction, 1.0, 2.0);
        expected = NumberTheoryUtils.sqrt(2.0);
        assertTrue(Precision.abs(result - expected) < 1e-6);

        // Test with custom constructor, which uses Pegasus method
        solver = new BaseSecantSolver(0.0, BaseSecantSolver.Method.PEGASUS);
        result = solver.solve(testFunction, 1.0, 2.0);
        expected = NumberTheoryUtils.sqrt(2.0);
        assertTrue(Precision.abs(result - expected) < 1e-6);

        // Test with custom constructor and custom accuracy
        solver = new BaseSecantSolver(1e-8, 0.0, BaseSecantSolver.Method.PEGASUS);
        result = solver.solve(testFunction, 1.0, 2.0);
        expected = NumberTheoryUtils.sqrt(2.0);
        assertTrue(Precision.abs(result - expected) < 1e-8);

        // Test with custom constructor and custom maximum iterations
        solver = new BaseSecantSolver(1e-6, 0.0, BaseSecantSolver.Method.PEGASUS, 10);
        result = solver.solve(testFunction, 1.0, 2.0);
        expected = NumberTheoryUtils.sqrt(2.0);
        assertTrue(Precision.abs(result - expected) < 1e-6);

        // Test with function value accuracy
        solver = new BaseSecantSolver(1e-6, 1e-5, 0.0, BaseSecantSolver.Method.PEGASUS);
        result = solver.solve(testFunction, 1.0, 2.0);
        expected = NumberTheoryUtils.sqrt(2.0);
        assertEquals(expected, result, 1e-5);

        // Test with custom constructor and custom allowed solution
        solver = new BaseSecantSolver(1e-6, 0.0, BaseSecantSolver.Method.PEGASUS,
                BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        result = solver.solve(testFunction, 1.0, 2.0);
        expected = 1.0;
        assertTrue(result <= 1.0);

        // Test for convergence exception with function that does not bracket root
        solver = new BaseSecantSolver(1e-6, 0.0, BaseSecantSolver.Method.PEGASUS,
                BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        try {
            result = solver.solve(new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    return Math.pow(x, 2) - 2.0;
                }
            }, 1.0, 0.0);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }

        // Test for MathInternalError
        try {
            solver = new BaseSecantSolver(1e-6, 0.0, BaseSecantSolver.Method.INVALID,
                    BaseSecantSolver.AllowedSolution.LEFT_SIDE);
            solver.solve(testFunction, 1.0, 2.0);
            fail("Expected MathInternalError");
        } catch (MathInternalError e) {
            // Expected
        }
    }
}