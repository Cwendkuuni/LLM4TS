package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testRegulaFalsi() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x - 2;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };

        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        try {
            double root = solver.solve(f, -10.0, 10.0);
            assertEquals(0.0, root, 1e-6);
        } catch (NoRootsException e) {
            fail("Expected a root to be found.");
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testRegulaFalsiInvalidBracketing() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }

            @Override
            public double derivative(double x) {
                return Math.cos(x);
            }
        };

        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        try {
            solver.solve(f, 1.0, 1.0);
            fail("Expected an IllegalArgumentException for invalid bracketing.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = NoRootsException.class)
    public void testRegulaFalsiNoRoots() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };

        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        try {
            solver.solve(f, -10.0, 10.0);
            fail("Expected a NoRootsException for the given function.");
        } catch (NoRootsException e) {
            // Expected
        }
    }

    @Test
    public void testIllinois() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 2;
            }

            @Override
            public double derivative(double x) {
                return 3 * Math.pow(x, 2);
            }
        };

        BaseSecantSolver solver = new IllinoisSolver(1e-6);
        try {
            double root = solver.solve(f, -3.0, 3.0);
            assertEquals(0.0, root, 1e-6);
        } catch (NoRootsException e) {
            fail("Expected a root to be found.");
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testIllinoisInvalidBracketing() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }

            @Override
            public double derivative(double x) {
                return Math.cos(x);
            }
        };

        BaseSecantSolver solver = new IllinoisSolver(1e-6);
        try {
            solver.solve(f, 1.0, 1.0);
            fail("Expected an IllegalArgumentException for invalid bracketing.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = NoRootsException.class)
    public void testIllinoisNoRoots() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };

        BaseSecantSolver solver = new IllinoisSolver(1e-6);
        try {
            solver.solve(f, -10.0, 10.0);
            fail("Expected a NoRootsException for the given function.");
        } catch (NoRootsException e) {
            // Expected
        }
    }

    @Test
    public void testPegasus() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 4) - 2;
            }

            @Override
            public double derivative(double x) {
                return 4 * Math.pow(x, 3);
            }
        };

        BaseSecantSolver solver = new PegasusSolver(1e-6);
        try {
            double root = solver.solve(f, -2.0, 2.0);
            assertEquals(0.0, root, 1e-6);
        } catch (NoRootsException e) {
            fail("Expected a root to be found.");
        }
    }

    @Test(expected = MathIllegalArgumentException.class)
    public void testPegasusInvalidBracketing() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x);
            }

            @Override
            public double derivative(double x) {
                return Math.cos(x);
            }
        };

        BaseSecantSolver solver = new PegasusSolver(1e-6);
        try {
            solver.solve(f, 1.0, 1.0);
            fail("Expected an IllegalArgumentException for invalid bracketing.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = NoRootsException.class)
    public void testPegasusNoRoots() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };

        BaseSecantSolver solver = new PegasusSolver(1e-6);
        try {
            solver.solve(f, -10.0, 10.0);
            fail("Expected a NoRootsException for the given function.");
        } catch (NoRootsException e) {
            // Expected
        }
    }

    @Test
    public void testSolverWithAbsoluteAccuracy() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };

        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, 1e-9);
        double root = solver.solve(f, -10.0, 10.0);
        assertEquals(0.0, root, 1e-9);
    }

    @Test
    public void testSolverWithRelativeAccuracy() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };

        BaseSecantSolver solver = new RegulaFalsiSolver(1e-3, 1e-6);
        double root = solver.solve(f, -10.0, 10.0);
        assertEquals(0.0, root, 1e-6);
        assertTrue(Math.abs(root - 0.0) < 1e-3);
    }

    @Test
    public void testSolverWithFunctionValueAccuracy() {
        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }

            @Override
            public double derivative(double x) {
                return 2 * x;
            }
        };

        BaseSecantSolver solver = new RegulaFalsiSolver(1e-3, 1e-6, 1e-9);
        double root = solver.solve(f, -10.0, 10.0);
        assertEquals(0.0, root, 1e-9);
    }
}