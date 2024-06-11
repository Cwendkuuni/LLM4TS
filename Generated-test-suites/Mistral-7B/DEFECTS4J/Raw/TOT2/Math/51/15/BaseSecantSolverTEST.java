package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link BaseSecantSolver}.
 */
public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double)}.
     */
    @Test
    public void testBaseSecantSolver() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6);
        assertNotNull(solver);
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, Method)}.
     */
    @Test
    public void testBaseSecantSolverMethod() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        assertNotNull(solver);
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, Method)}.
     */
    @Test
    public void testBaseSecantSolverAccuracies() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, 1e-8, BaseSecantSolver.Method.ILLINOIS);
        assertNotNull(solver);
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, double, Method)}.
     */
    @Test
    public void testBaseSecantSolverAccuraciesFunctionValue() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, 1e-8, 1e-8, BaseSecantSolver.Method.ILLINOIS);
        assertNotNull(solver);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6);

        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 2.0;
            }
        };

        double root = solver.solve(50, function, -3.0, 3.0, 0.0);
        assertEquals(0.0, root, 1e-6);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6);

        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x > 0) {
                    throw new MathIllegalArgumentException("x should be non-positive");
                }
                return x * x - 4.0;
            }
        };

        try {
            solver.solve(50, function, 0.0, 0.0, 0.0, AllowedSolution.ABOVE_SIDE);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }

        try {
            solver.solve(50, function, -2.0, 0.0, 0.0, AllowedSolution.BELOW_SIDE);
            fail("Expected NoRootsException");
        } catch (NoRootsException e) {
            // Expected
        }

        try {
            solver.solve(50, function, -2.0, 0.0, 0.0, AllowedSolution.ANY_SIDE);
            double root = Precision.round(solver.getRoot(), 6);
            assertEquals(-2.0, root, 1e-6);
        } finally {
            solver.dispose();
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution, double, double)}.
     */
    @Test
    public void testSolveWithStartValue() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6);

        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, x) - 2.0;
            }
        };

        try {
            solver.solve(50, function, -10.0, 0.0, 0.0, AllowedSolution.BELOW_SIDE);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }

        try {
            solver.solve(50, function, -1.0, 0.0, 0.0, AllowedSolution.ABOVE_SIDE);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }

        try {
            solver.solve(50, function, -1.0, 0.0, 0.0, AllowedSolution.RIGHT_SIDE);
            fail("Expected NoRootsException");
        } catch (NoRootsException e) {
            // Expected
        }

        try {
            solver.solve(50, function, -1.0, 0.0, 0.0, AllowedSolution.LEFT_SIDE);
            double root = Precision.round(solver.getRoot(), 6);
            assertEquals(Math.log(2.0), root, 1e-6);
        } finally {
            solver.dispose();
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution, double, double, double)}.
     */
    @Test
    public void testSolveWithRelAndAbsAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-4, 1e-8);

        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, x) - 2.0;
            }
        };

        try {
            solver.solve(50, function, -10.0, 0.0, 0.0, AllowedSolution.BELOW_SIDE);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }

        try {
            solver.solve(50, function, -1.0, 0.0, 0.0, AllowedSolution.ABOVE_SIDE);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }

        try {
            solver.solve(50, function, -1.0, 0.0, 0.0, AllowedSolution.RIGHT_SIDE);
            fail("Expected NoRootsException");
        } catch (NoRootsException e) {
            // Expected
        }

        try {
            solver.solve(50, function, -1.0, 0.0, 0.0, AllowedSolution.LEFT_SIDE);
            double root = Precision.round(solver.getRoot(), 6);
            assertEquals(Math.log(2.0), root, 1e-8);
        } finally {
            solver.dispose();
        }
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution, double, double, double, double)}.
     */
    @Test
    public void testSolveWithFunctionValueAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, 1e-8, 1e-8);

        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.E, x) - 2.0;
            }
        };

        try {
            solver.solve(50, function, -10.0, 0.0, 0.0, AllowedSolution.BELOW_SIDE);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }

        try {
            solver.solve(50, function, -1.0, 0.0, 0.0, AllowedSolution.ABOVE_SIDE);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected
        }

        try {
            solver.solve(50, function, -1.0, 0.0, 0.0, AllowedSolution.RIGHT_SIDE);
            fail("Expected NoRootsException");
        } catch (NoRootsException e) {
            // Expected
        }

        try {
            solver.solve(50, function, -1.0, 0.0, 0.0, AllowedSolution.LEFT_SIDE);
            double root = Precision.round(solver.getRoot(), 6);
            assertEquals(Math.log(2.0), root, 1e-8);
        } finally {
            solver.dispose();
        }
    }
}