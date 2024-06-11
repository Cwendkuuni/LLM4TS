package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.commons.math.analysis.solvers.Method;
import org.apache.commons.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.commons.math.exception.MathIllegalArgumentException;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testNullFunction() {
        try {
            BaseSecantSolver solver = new BaseSecantSolver(0.001, Method.ILLINOIS);
            solver.solve(1, null, 1.0, 2.0, 1.5);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testNullAllowedSolution() {
        try {
            BaseSecantSolver solver = new BaseSecantSolver(0.001, Method.ILLINOIS);
            solver.solve(1, new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    return 0;
                }
            }, 1.0, 2.0, null);
            fail("Expected MathIllegalArgumentException");
        } catch (MathIllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testSolverWithDefaultConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(Method.ILLINOIS);

        try {
            solver.solve(1, new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    return Math.sin(x) - 0.5;
                }
            }, -10.0, 10.0);
            fail("Expected exception");
        } catch (Exception e) {
            // Expected exception
        }

        solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);

        double root = solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        }, -10.0, 10.0);

        assertEquals(Math.PI / 2, root, 1e-5);
    }

    @Test
    public void testSolverWithDoubleConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, 1e-6, Method.ILLINOIS);

        double root = solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        }, -10.0, 10.0);

        assertEquals(Math.PI / 2, root, 1e-5);
    }

    @Test
    public void testSolverWithTripleConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, 1e-6, 1e-6, Method.ILLINOIS);

        double root = solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        }, -10.0, 10.0);

        assertEquals(Math.PI / 2, root, 1e-5);
    }

    @Test
    public void testSolverWithAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS, AllowedSolution.LEFT_SIDE);

        double root = solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        }, -10.0, 10.0);

        assertEquals(Math.PI / 2, root, 1e-5);
    }

    @Test
    public void testSolverWithAllowedSolutionAbove() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS, AllowedSolution.ABOVE_SIDE);

        double root = solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        }, -10.0, 10.0);

        assertTrue(root > Math.PI / 2);
    }

    @Test
    public void testSolverWithAllowedSolutionBelow() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS, AllowedSolution.BELOW_SIDE);

        double root = solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        }, -10.0, 10.0);

        assertTrue(root < Math.PI / 2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSolverWithoutBracketing() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);

        double root = solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }
        }, -3.0, -2.0);

        fail("Expected UnsupportedOperationException");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSolverWithInvalidAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.ILLINOIS);

        try {
            BracketedUnivariateRealSolver<UnivariateRealFunction> solver = new BaseSecantSolver(1e-6, Method.ILLINOIS, AllowedSolution.INVALID_SIDE);
            solver.solve(100, new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    return Math.sin(x) - 0.5;
                }
            }, -10.0, 10.0);
            fail("Expected UnsupportedOperationException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
}

enum AllowedSolution {
    ANY_SIDE, LEFT_SIDE, RIGHT_SIDE, BELOW_SIDE, ABOVE_SIDE, INVALID_SIDE;
}