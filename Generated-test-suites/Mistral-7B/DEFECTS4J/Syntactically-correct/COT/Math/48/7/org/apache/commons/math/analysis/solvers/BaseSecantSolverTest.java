package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test case for {@link BaseSecantSolver} class.
 */
public class BaseSecantSolverTest {

    

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver()}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testDefaultConstructor() {
        new BaseSecantSolver<>();
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double)}.
     */
    @Test
    public void testConstructorWithDefaultAccuracy() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(function.getValue(0.0), BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(2.0, Precision.round(solver.solve(10), 4), 0.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double)}.
     */
    @Test
    public void testConstructorWithRelativeAndAbsoluteAccuracy() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(function.getValue(0.0), 1e-4, 1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(2.0, Precision.round(solver.solve(10), 4), 0.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#BaseSecantSolver(double, double, double)}.
     */
    @Test
    public void testConstructorWithRelativeAccuracyFunctionValueAccuracyAndMethod() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(function.getValue(0.0), 1e-4, 1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(2.0, Precision.round(solver.solve(10), 4), 0.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testSolveWithMinMax() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        new BaseSecantSolver(function.getValue(0.0), BaseSecantSolver.Method.REGULA_FALSI).solve(10, function, -1, 1, 0.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveWithMinMaxAndAllowedSolution() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(function.getValue(0.0), BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(2.0, Precision.round(solver.solve(10, function, -1, 1, 0.0, AllowedSolution.LEFT_SIDE), 4), 0.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @Test
    public void testSolveWithMaxIterations() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(function.getValue(0.0), BaseSecantSolver.Method.REGULA_FALSI);
        assertThrows(MaxIterationsExceededException.class, () -> solver.solve(0, function, -1, 1, 0.0));
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveWithMaxIterationsAndAllowedSolution() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(function.getValue(0.0), BaseSecantSolver.Method.REGULA_FALSI);
        solver.solve(0, function, -1, 1, 0.0, AllowedSolution.LEFT_SIDE);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testSolveWithMinLessThanMax() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(function.getValue(0.0), BaseSecantSolver.Method.REGULA_FALSI);
        solver.solve(10, function, -1, 0, 0.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testSolveWithMinLessThanMaxAndAllowedSolution() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(function.getValue(0.0), BaseSecantSolver.Method.REGULA_FALSI);
        solver.solve(10, function, -1, 0, 0.0, AllowedSolution.LEFT_SIDE);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double)}.
     */
    @Test
    public void testSolveWithStartValue() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(function.getValue(0.0), BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(2.0, Precision.round(solver.solve(10, function, -1, 1, 1.0), 4), 0.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolveWithStartValueAndAllowedSolution() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(function.getValue(0.0), BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(2.0, Precision.round(solver.solve(10, function, -1, 1, 1.0, AllowedSolution.LEFT_SIDE), 4), 0.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#verifyBracketing(double, double)}.
     */
    @Test(expected = ConvergenceException.class)
    public void testVerifyBracketingWithNonBracketing() {
        double x0 = 1.0;
        double x1 = 0.0;
        BaseSecantSolver solver = new BaseSecantSolver(x1, BaseSecantSolver.Method.REGULA_FALSI);
        solver.verifyBracketing(x0, x1);
    }

    /**
     * Test method for {@link BaseSecantSolver#verifyBracketing(double, double)}.
     */
    @Test
    public void testVerifyBracketingWithBracketing() {
        double x0 = 1.0;
        double x1 = 2.0;
        BaseSecantSolver solver = new BaseSecantSolver(x1, BaseSecantSolver.Method.REGULA_FALSI);
        solver.verifyBracketing(x0, x1);
    }

    /**
     * Test method for {@link BaseSecantSolver#verifyBracketing(double, double)}.
     */
    @Test(expected = ConvergenceException.class)
    public void testVerifyBracketingWithNonBracketingAndAllowedSolution() {
        double x0 = 1.0;
        double x1 = 0.0;
        BaseSecantSolver solver = new BaseSecantSolver(x1, BaseSecantSolver.Method.REGULA_FALSI, AllowedSolution.LEFT_SIDE);
        solver.verifyBracketing(x0, x1);
    }

    /**
     * Test method for {@link BaseSecantSolver#verifyBracketing(double, double)}.
     */
    @Test
    public void testVerifyBracketingWithBracketingAndAllowedSolution() {
        double x0 = 1.0;
        double x1 = 2.0;
        BaseSecantSolver solver = new BaseSecantSolver(x1, BaseSecantSolver.Method.REGULA_FALSI, AllowedSolution.LEFT_SIDE);
        solver.verifyBracketing(x0, x1);
    }

    /**
     * Test method for {@link BaseSecantSolver#computeObjectiveValue(double)}.
     */
    @Test
    public void testComputeObjectiveValue() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4.0;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(function.getValue(0.0), BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(-0.16, Precision.round(solver.computeObjectiveValue(0.0), 4), 0.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#computeObjectiveValue(double)}.
     */
    @Test
    public void testComputeObjectiveValueWithNegativeValue() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return -(x * x - 4.0);
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(function.getValue(0.0), BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(0.16, Precision.round(solver.computeObjectiveValue(0.0), 4), 0.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#computeObjectiveValue(double)}.
     */
    @Test
    public void testComputeObjectiveValueWithZeroFunctionValue() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return 0.0;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(function.getValue(0.0), BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(0.0, Precision.round(solver.computeObjectiveValue(0.0), 4), 0.0);
    }

    /**
     * Test method for {@link BaseSecantSolver#computeObjectiveValue(double)}.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testComputeObjectiveValueWithNegativeFunctionValue() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return -1.0e-20;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(function.getValue(0.0), BaseSecantSolver.Method.REGULA_FALSI);
        solver.computeObjectiveValue(0.0);
    }

    
}