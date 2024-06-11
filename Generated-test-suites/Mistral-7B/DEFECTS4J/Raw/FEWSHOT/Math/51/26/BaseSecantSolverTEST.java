package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.commons.math.analysis.FunctionUtils;
import org.apache.commons.commons.math.analysis.UnivariateFunction;
import org.apache.commons.commons.math.analysis.solvers.AbstractUnivariateRealSolver;
import org.apache.commons.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.apache.commons.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.commons.math.analysis.solvers.Methods;
import org.apache.commons.commons.math.exception.MathIllegalArgumentException;
import org.apache.commons.commons.math.exception.MathInternalError;
import org.apache.commons.commons.math.util.Precision;

public class BaseSecantSolverTest {

    @Test(expected = MathIllegalArgumentException.class)
    public void testIllegalArguments() {
        try {
            new BaseSecantSolver(null, Methods.ILLINOIS);
        } catch (IllegalArgumentException e) {
            assertEquals("'f' must not be null", e.getMessage());
            throw e;
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            new BaseSecantSolver(new UnivariateFunction() {
                @Override
                public double value(double x) {
                    throw new UnsupportedOperationException();
                }

                @Override
                public double derivative(double x) {
                    throw new UnsupportedOperationException();
                }
            }, Methods.ILLINOIS);
        } catch (MathInternalError e) {
            assertEquals("Unsupported function", e.getMessage());
            throw e;
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        try {
            new BaseSecantSolver(FunctionUtils.SIN, null);
        } catch (IllegalArgumentException e) {
            assertEquals("'method' must not be null", e.getMessage());
            throw e;
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testRegulaFalsi() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Methods.REGULA_FALSI);

        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }

            @Override
            public double derivative(double x) {
                return Math.cos(x);
            }
        };

        double root = solver.solve(10, f, -3, 3, 0.0);
        assertEquals(Math.PI / 4, root, 1e-6);
    }

    @Test
    public void testIllinois() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Methods.ILLINOIS);

        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }

            @Override
            public double derivative(double x) {
                return Math.cos(x);
            }
        };

        double root = solver.solve(10, f, -3, 3, 0.0);
        assertEquals(Math.PI / 4, root, 1e-6);
    }

    @Test
    public void testPegasus() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Methods.PEGASUS);

        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }

            @Override
            public double derivative(double x) {
                return Math.cos(x);
            }
        };

        double root = solver.solve(10, f, -3, 3, 0.0);
        assertEquals(Math.PI / 4, root, 1e-6);
    }

    @Test
    public void testAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Methods.ILLINOIS, AllowedSolution.ABOVE_SIDE);

        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) + 0.5;
            }

            @Override
            public double derivative(double x) {
                return Math.cos(x);
            }
        };

        try {
            solver.solve(1, f, -3, 3, 0.0);
            fail("Should not find a root with the given allowed solution.");
        } catch (AssertionError e) {
            // Expected
        }

        double root = solver.solve(10, f, -3, 3, 0.0);
        assertEquals(Math.PI / 2, root, 1e-6);
    }

    @Test
    public void testAllowedSolutionBelowSide() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Methods.ILLINOIS, AllowedSolution.BELOW_SIDE);

        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }

            @Override
            public double derivative(double x) {
                return Math.cos(x);
            }
        };

        double root = solver.solve(10, f, -3, 3, 0.0);
        assertEquals(Math.PI / 4, root, 1e-6);
    }

    @Test
    public void testAllowedSolutionAboveSide() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Methods.ILLINOIS, AllowedSolution.ABOVE_SIDE);

        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) + 0.5;
            }

            @Override
            public double derivative(double x) {
                return Math.cos(x);
            }
        };

        double root = solver.solve(10, f, -3, 3, 0.0);
        assertEquals(Math.PI * 3 / 2, root, 1e-6);
    }

    @Test
    public void testBracketing() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Methods.ILLINOIS);

        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }

            @Override
            public double derivative(double x) {
                return Math.cos(x);
            }
        };

        try {
            solver.solve(1, f, 0, 0, 0.0);
            fail("Should not find a root without bracketing.");
        } catch (AssertionError e) {
            // Expected
        }

        double root = solver.solve(10, f, -3, 3, 0.0);
        assertEquals(Math.PI / 4, root, 1e-6);
    }

    @Test
    public void testBracketingInverted() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Methods.ILLINOIS);

        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) + 0.5;
            }

            @Override
            public double derivative(double x) {
                return Math.cos(x);
            }
        };

        try {
            solver.solve(1, f, -3, 3, 0.0);
            fail("Should not find a root without bracketing.");
        } catch (AssertionError e) {
            // Expected
        }

        double root = solver.solve(10, f, -3, 3, 0.0);
        assertEquals(Math.PI * 3 / 2, root, 1e-6);
    }

    @Test
    public void testFunctionValueAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-8, 1e-6, Methods.ILLINOIS);

        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }

            @Override
            public double derivative(double x) {
                return Math.cos(x);
            }
        };

        double root = solver.solve(10, f, -3, 3, 0.0);
        assertEquals(Math.PI / 4, root, 1e-8);
    }

    @Test
    public void testAbsoluteAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, 1e-6, Methods.ILLINOIS);

        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }

            @Override
            public double derivative(double x) {
                return Math.cos(x);
            }
        };

        double root = solver.solve(10, f, -3, 3, 0.0);
        assertEquals(Math.PI / 4, root, 1e-6);
    }

    @Test
    public void testRelativeAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-4, 1e-6, 1e-6, Methods.ILLINOIS);

        UnivariateFunction f = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) - 0.5;
            }

            @Override
            public double derivative(double x) {
                return Math.cos(x);
            }
        };

        double root = solver.solve(10, f, -3, 3, 0.0);
        double absError = Precision.absError(Math.PI / 4, root);
        double relError = absError / Math.abs(Math.PI / 4);
        assertEquals(1e-4, relError, 1e-6);
    }
}