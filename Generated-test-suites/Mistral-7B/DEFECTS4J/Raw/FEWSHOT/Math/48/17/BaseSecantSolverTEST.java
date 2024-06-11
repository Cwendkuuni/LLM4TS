package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.commons.math.exception.ConvergenceException;
import org.apache.commons.commons.math.exception.MaxEvalExceededException;
import org.apache.commons.commons.math.util.Precision;
import org.apache.commons.commons.math.util.FastMath;

import org.apache.commons.cli.BaseSecantSolver;
import org.apache.commons.cli.RegulaFalsiSolver;
import org.apache.commons.cli.IllinoisSolver;
import org.apache.commons.cli.PegasusSolver;

public class BaseSecantSolverTest {

    @Test
    public void testRegulaFalsi() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        try {
            double root = solver.solve(10, function, -2.0, 2.0, AllowedSolution.ANY_SIDE);
            assertEquals(0.0, Precision.doubleValue(root), 1e-7);
        } catch (MaxEvalExceededException e) {
            fail("RegulaFalsiSolver should have found the root within 10 evaluations.");
        }

        // Test with a function that has no roots in the given interval.
        solver = new RegulaFalsiSolver(1e-6);
        function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(x) / x;
            }
        };
        assertThrows(ConvergenceException.class, () -> solver.solve(10, function, -2.0, 2.0, AllowedSolution.ANY_SIDE));

        // Test with a function that has multiple roots in the given interval.
        solver = new RegulaFalsiSolver(1e-6);
        function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 1;
            }
        };
        try {
            double root = solver.solve(10, function, -2.0, 2.0, AllowedSolution.ANY_SIDE);
            assertEquals(0.0, Precision.doubleValue(root), 1e-7);
        } catch (MaxEvalExceededException e) {
            fail("RegulaFalsiSolver should have found one of the roots within 10 evaluations.");
        }
    }

    @Test
    public void testIllinois() {
        BaseSecantSolver solver = new IllinoisSolver(1e-6);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        try {
            double root = solver.solve(10, function, -2.0, 2.0, AllowedSolution.ANY_SIDE);
            assertEquals(0.0, Precision.doubleValue(root), 1e-7);
        } catch (MaxEvalExceededException e) {
            fail("IllinoisSolver should have found the root within 10 evaluations.");
        }
    }

    @Test
    public void testPegasus() {
        BaseSecantSolver solver = new PegasusSolver(1e-6);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        try {
            double root = solver.solve(10, function, -2.0, 2.0, AllowedSolution.ANY_SIDE);
            assertEquals(0.0, Precision.doubleValue(root), 1e-7);
        } catch (MaxEvalExceededException e) {
            fail("PegasusSolver should have found the root within 10 evaluations.");
        }
    }

    @Test
    public void testRelativeAccuracy() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-4, 1e-6);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        try {
            double root = solver.solve(10, function, -2.0, 2.0, AllowedSolution.ANY_SIDE);
            double expected = 0.5;
            assertEquals(expected, Precision.doubleValue(root), 1e-7);
            assertEquals(expected, Precision.doubleValue(solver.getLastIncludedX()), 1e-7);
        } catch (MaxEvalExceededException e) {
            fail("RegulaFalsiSolver should have found the root within 10 evaluations.");
        }
    }

    @Test
    public void testAbsoluteAccuracy() {
        BaseSecantSolver solver = new RegulaFalsiSolver(0.01, 1e-6);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1;
            }
        };
        try {
            double root = solver.solve(10, function, -2.0, 2.0, AllowedSolution.ANY_SIDE);
            double expected = -0.997;
            assertEquals(expected, Precision.doubleValue(root), 1e-7);
            assertEquals(expected, Precision.doubleValue(solver.getLastIncludedX()), 1e-7);
        } catch (MaxEvalExceededException e) {
            fail("RegulaFalsiSolver should have found the root within 10 evaluations.");
        }
    }

    @Test
    public void testFunctionValueAccuracy() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6, 1e-8);
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) - 1 + 0.001;
            }
        };
        try {
            double root = solver.solve(10, function, -2.0, 2.0, AllowedSolution.ANY_SIDE);
            double expected = 0.0;
            assertEquals(expected, Precision.doubleValue(solver.getFunctionValue(root)), 1e-10);
        } catch (MaxEvalExceededException e) {
            fail("RegulaFalsiSolver should have found the root within 10 evaluations.");
        }
    }

    @Test
    public void testAllowedSolutions() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);

        // Test with allowed solution 'ANY_SIDE'.
        try {
            double root = solver.solve(10, new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    return x * x - 2;
                }
            }, -3.0, 3.0, AllowedSolution.ANY_SIDE);
            assertEquals(0.0, Precision.doubleValue(root), 1e-7);
        } catch (MaxEvalExceededException e) {
            fail("RegulaFalsiSolver should have found the root within 10 evaluations.");
        }

        // Test with allowed solution 'LEFT_SIDE'.
        BaseSecantSolver solverLeft = new RegulaFalsiSolver(1e-6);

        try {
            double root = solverLeft.solve(10, new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    return x * x - 2;
                }
            }, -3.0, 3.0, AllowedSolution.LEFT_SIDE);
            assertEquals(0.0, Precision.doubleValue(root), 1e-7);
            assertEquals(-3.0, Precision.doubleValue(solverLeft.getLastIncludedX()), 1e-7);
        } catch (MaxEvalExceededException e) {
            fail("RegulaFalsiSolver should have found the root within 10 evaluations.");
        }

        // Test with allowed solution 'RIGHT_SIDE'.
        BaseSecantSolver solverRight = new RegulaFalsiSolver(1e-6);

        try {
            double root = solverRight.solve(10, new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    return x * x - 2;
                }
            }, -3.0, 3.0, AllowedSolution.RIGHT_SIDE);
            assertEquals(0.0, Precision.doubleValue(root), 1e-7);
            assertEquals(3.0, Precision.doubleValue(solverRight.getLastIncludedX()), 1e-7);
        } catch (MaxEvalExceededException e) {
            fail("RegulaFalsiSolver should have found the root within 10 evaluations.");
        }

        // Test with allowed solution 'ABOVE_SIDE'.
        BaseSecantSolver solverAbove = new RegulaFalsiSolver(1e-6);

        try {
            double root = solverAbove.solve(10, new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    return x * x - 2;
                }
            }, -3.0, 3.0, AllowedSolution.ABOVE_SIDE);
            assertEquals(0.0, Precision.doubleValue(root), 1e-7);
            assertEquals(-3.0, Precision.doubleValue(solverAbove.getLastIncludedX()), 1e-7);
            assertEquals(FastMath.sqrt(2.0), Precision.doubleValue(solverAbove.getLastFunctionValue()), 1e-7);
        } catch (MaxEvalExceededException e) {
            fail("RegulaFalsiSolver should have found the root within 10 evaluations.");
        }

        // Test with allowed solution 'BELOW_SIDE'.
        BaseSecantSolver solverBelow = new RegulaFalsiSolver(1e-6);

        try {
            double root = solverBelow.solve(10, new UnivariateRealFunction() {
                @Override
                public double value(double x) {
                    return x * x - 2;
                }
            }, -3.0, 3.0, AllowedSolution.BELOW_SIDE);
            assertEquals(0.0, Precision.doubleValue(root), 1e-7);
            assertEquals(3.0, Precision.doubleValue(solverBelow.getLastIncludedX()), 1e-7);
            assertEquals(-FastMath.sqrt(2.0), Precision.doubleValue(solverBelow.getLastFunctionValue()), 1e-7);
        } catch (MaxEvalExceededException e) {
            fail("RegulaFalsiSolver should have found the root within 10 evaluations.");
        }
    }

    @Test
    public void testDerivedFromAbstractUnivariateRealSolver() {
        BaseSecantSolver solver = new RegulaFalsiSolver(1e-6);
        assertEquals("BaseSecantSolver", solver.getClass().getSimpleName());
        assertEquals("AbstractUnivariateRealSolver", solver.getClass().getSuperclass().getSimpleName());
    }
}