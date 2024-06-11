package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.util.Precision;

public class BaseSecantSolverTest {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static UnivariateRealFunction f(final double x) {
        return () -> Math.pow(x, 3) - 2;
    }

    @Test
    public void testSolve_RegulaFalsi() {
        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double doSolve() {
                throw new UnsupportedOperationException();
            }
        };
        final double root = solver.solve(10, f, -10.0, 10.0, 0.0);
        assertTrue(root > 0);
        assertEquals(2.0, root, 1e-6);
    }

    @Test
    public void testSolve_Illinois() {
        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS) {
            @Override
            protected double doSolve() {
                throw new UnsupportedOperationException();
            }
        };
        final double root = solver.solve(10, f, -10.0, 10.0, 0.0);
        assertTrue(root > 0);
        assertEquals(2.0, root, 1e-6);
    }

    @Test
    public void testSolve_Pegasus() {
        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS) {
            @Override
            protected double doSolve() {
                throw new UnsupportedOperationException();
            }
        };
        final double root = solver.solve(10, f, -10.0, 10.0, 0.0);
        assertTrue(root > 0);
        assertEquals(2.0, root, 1e-6);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testSolve_MaxEvalExceeded_RegulaFalsi() {
        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double doSolve() {
                throw new UnsupportedOperationException();
            }
        };
        solver.solve(0, f, -10.0, 10.0, 0.0);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testSolve_MaxEvalExceeded_Illinois() {
        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS) {
            @Override
            protected double doSolve() {
                throw new UnsupportedOperationException();
            }
        };
        solver.solve(0, f, -10.0, 10.0, 0.0);
    }

    @Test(expected = MaxEvalExceededException.class)
    public void testSolve_MaxEvalExceeded_Pegasus() {
        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS) {
            @Override
            protected double doSolve() {
                throw new UnsupportedOperationException();
            }
        };
        solver.solve(0, f, -10.0, 10.0, 0.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSolve_NumberIsTooSmall_RegulaFalsi() {
        final BaseSecantSolver solver = new BaseSecantSolver(1e-12, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double doSolve() {
                throw new UnsupportedOperationException();
            }
        };
        solver.solve(10, f, -10.0, 10.0, 0.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSolve_NumberIsTooSmall_Illinois() {
        final BaseSecantSolver solver = new BaseSecantSolver(1e-12, BaseSecantSolver.Method.ILLINOIS) {
            @Override
            protected double doSolve() {
                throw new UnsupportedOperationException();
            }
        };
        solver.solve(10, f, -10.0, 10.0, 0.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSolve_NumberIsTooSmall_Pegasus() {
        final BaseSecantSolver solver = new BaseSecantSolver(1e-12, BaseSecantSolver.Method.PEGASUS) {
            @Override
            protected double doSolve() {
                throw new UnsupportedOperationException();
            }
        };
        solver.solve(10, f, -10.0, 10.0, 0.0);
    }

    @Test
    public void testSolve_AllowedSolution_LeftSide() {
        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS) {
            @Override
            protected double doSolve() {
                throw new UnsupportedOperationException();
            }
        };
        double root = solver.solve(10, f, -10.0, 10.0, 0.0, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertTrue(root < 0);
        assertEquals(2.0, root, 1e-6);
    }

    @Test
    public void testSolve_AllowedSolution_RightSide() {
        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS) {
            @Override
            protected double doSolve() {
                throw new UnsupportedOperationException();
            }
        };
        double root = solver.solve(10, f, -10.0, 10.0, 0.0, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        assertTrue(root > 0);
        assertEquals(2.0, root, 1e-6);
    }

    @Test
    public void testSolve_AllowedSolution_BelowSide() {
        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS) {
            @Override
            protected double doSolve() {
                throw new UnsupportedOperationException();
            }
        };
        double root = solver.solve(10, f, -10.0, 10.0, 0.0, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        assertTrue(root < 0);
        assertEquals(2.0, root, 1e-6);
    }

    @Test
    public void testSolve_AllowedSolution_AboveSide() {
        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS) {
            @Override
            protected double doSolve() {
                throw new UnsupportedOperationException();
            }
        };
        double root = solver.solve(10, f, -10.0, 10.0, 0.0, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        assertTrue(root > 0);
        assertEquals(2.0, root, 1e-6);
    }

    @Test
    public void testSolve_AllowedSolution_AnySide() {
        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS) {
            @Override
            protected double doSolve() {
                throw new UnsupportedOperationException();
            }
        };
        double root = solver.solve(10, f, -10.0, 10.0, 0.0, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertTrue(root > 0);
        assertEquals(2.0, root, 1e-6);
    }

    @Test
    public void testVerifyBracketing() {
        final double min = -1.0;
        final double max = 1.0;
        final double x = 0.0;
        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS) {
            @Override
            protected double doSolve() {
                throw new UnsupportedOperationException();
            }
        };
        solver.verifyBracketing(min, max);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifyBracketing_MinGreaterThanMax() {
        final double min = 1.0;
        final double max = -1.0;
        final BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS) {
            @Override
            protected double doSolve() {
                throw new UnsupportedOperationException();
            }
        };
        solver.verifyBracketing(min, max);
    }

    @Test
    public void testComputeObjectiveValue() {
        final UnivariateRealFunction f = BaseSecantSolverTest::f;
        final double x = 3.0;
        assertEquals(-1.0, f.value(x), 1e-6);
    }
}