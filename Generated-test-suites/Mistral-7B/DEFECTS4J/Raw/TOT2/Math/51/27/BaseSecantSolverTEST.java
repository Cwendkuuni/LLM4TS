package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathRuntimeError;
import org.apache.commons.math3.util.Precision;

import org.junit.Test;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, Method)}.
     */
    @Test
    public void testConstructor() {
        // Test with default constructor
        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.ILLINOIS);
        assertNotNull(solver);

        // Test with custom constructor
        BaseSecantSolver solver2 = new BaseSecantSolver(0.001, 0.01, Method.PEGASUS);
        assertNotNull(solver2);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolve() {
        // Test with default constructor and sample function
        BaseSecantSolver solver = new BaseSecantSolver(DEFAULT_ABSOLUTE_ACCURACY, Method.ILLINOIS);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }
        };
        double root = solver.solve(100, f, -5.0, 5.0, 2.0, AllowedSolution.LEFT_SIDE);
        assertEquals(0.0, root, Precision.EPSILON);

        // Test with custom constructor and sample function
        BaseSecantSolver solver2 = new BaseSecantSolver(0.001, 0.01, 1e-5, Method.PEGASUS);
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, x) - 2;
            }
        };
        double root2 = solver2.solve(100, f2, 0.0, 10.0, 1.0, AllowedSolution.ANY_SIDE);
        assertEquals(Math.E - 1.0, root2, 1e-5);

        // Test with custom constructor, invalid function, and allowed solutions on both sides
        BaseSecantSolver solver3 = new BaseSecantSolver(0.001, 0.01, 1e-5, Method.PEGASUS);
        UnivariateRealFunction f3 = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new MathRuntimeError("Function error");
            }
        };
        try {
            solver3.solve(100, f3, 0.0, 10.0, 1.0, AllowedSolution.BELOW_SIDE);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }

        try {
            solver3.solve(100, f3, 0.0, 10.0, 1.0, AllowedSolution.ABOVE_SIDE);
            fail("Expected MathRuntimeError");
        } catch (MathRuntimeError e) {
            // Expected
        }
    }
}