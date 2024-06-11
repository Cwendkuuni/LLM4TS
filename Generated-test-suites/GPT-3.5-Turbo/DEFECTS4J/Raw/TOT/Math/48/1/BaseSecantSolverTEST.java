import org.apache.commons.math.analysis.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BaseSecantSolverTest {

    private static final double ABSOLUTE_ERROR = 1e-6;
    private BaseSecantSolver solver;

    @Before
    public void setUp() {
        solver = new BaseSecantSolver(ABSOLUTE_ERROR, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double point) {
                return point * point - 4;
            }
        };
    }

    @Test
    public void testSolve_WithDefaultValues_ReturnsExpectedResult() {
        double result = solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        }, -10, 10);

        assertEquals(2, result, ABSOLUTE_ERROR);
    }

    @Test
    public void testSolve_WithMaxEvalAndValidSolutionAllowed_ReturnsExpectedResult() {
        double result = solver.solve(10, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        }, -10, 10, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);

        assertEquals(2, result, ABSOLUTE_ERROR);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolve_WithInvalidMinAndMax_ThrowsIllegalArgumentException() {
        solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        }, 10, -10);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolve_WithInsufficientMaxEval_ThrowsConvergenceException() {
        solver.solve(1, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        }, -10, 10);
    }

    @Test
    public void testSolve_WithFunctionEqualToZeroAtMin_ReturnsMin() {
        double result = solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        }, -2, 10);

        assertEquals(-2.0, result, ABSOLUTE_ERROR);
    }

    @Test
    public void testSolve_WithFunctionEqualToZeroAtMax_ReturnsMax() {
        double result = solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        }, -10, 2);

        assertEquals(2.0, result, ABSOLUTE_ERROR);
    }

    @Test
    public void testSolve_WithFunctionCloseToZero_ReturnsExpectedResult() {
        double result = solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4 + 1e-7;
            }
        }, -10, 10);

        assertEquals(2.00001, result, ABSOLUTE_ERROR);
    }

    @Test
    public void testSolve_WithNegativeFunction_ReturnsExpectedResult() {
        double result = solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return -(x * x - 4);
            }
        }, -10, 10);

        assertEquals(-2.0, result, ABSOLUTE_ERROR);
    }

    @Test
    public void testSolve_WithFunctionAtBounds_ReturnsExpectedResult() {
        double result = solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                if (x == -10) {
                    return -96.0;
                } else if (x == 10) {
                    return 96.0;
                }
                return x * x - 4;
            }
        }, -10, 10);

        assertEquals(2, result, ABSOLUTE_ERROR);
    }

    @Test
    public void testSolve_WithDifferentMethodAndValidSolutionAllowed_ReturnsExpectedResult() {
        solver = new BaseSecantSolver(ABSOLUTE_ERROR, BaseSecantSolver.Method.ILLINOIS) {
            @Override
            protected double computeObjectiveValue(double point) {
                return point * point - 4;
            }
        };

        double result = solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        }, -10, 10, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);

        assertEquals(2, result, ABSOLUTE_ERROR);
    }

    @Test
    public void testSolve_WithDifferentMethodAndInvalidSolutionAllowed_ReturnsNaN() {
        solver = new BaseSecantSolver(ABSOLUTE_ERROR, BaseSecantSolver.Method.ILLINOIS) {
            @Override
            protected double computeObjectiveValue(double point) {
                return point * point - 4;
            }
        };

        double result = solver.solve(100, new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        }, -10, 10, BaseSecantSolver.AllowedSolution.LEFT_SIDE);

        assertEquals(Double.NaN, result, ABSOLUTE_ERROR);
    }
}