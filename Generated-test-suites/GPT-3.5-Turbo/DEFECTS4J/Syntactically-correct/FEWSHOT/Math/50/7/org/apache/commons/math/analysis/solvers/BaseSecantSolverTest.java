package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class BaseSecantSolverTest {
    private static final double ABSOLUTE_ACCURACY = 1e-6;
    private static final double RELATIVE_ACCURACY = 1e-6;
    private static final double FUNCTION_VALUE_ACCURACY = 1e-6;

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x) - x;
            }
        };
        double result = solver.solve(100, f, 0, 10);
        assertEquals(0.8767262153953165, result, ABSOLUTE_ACCURACY);
    }

    @Test
    public void testSolveWithMaxEval() {
        BaseSecantSolver solver = new BaseSecantSolver(ABSOLUTE_ACCURACY, BaseSecantSolver.Method.Illinois);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.cos(x) - x;
            }
        };
        double result = solver.solve(10, f, 0, 10);
        assertEquals(0.7390851332157039, result, ABSOLUTE_ACCURACY);
    }

    @Test
    public void testSolveWithMinAndMax() {
        BaseSecantSolver solver = new BaseSecantSolver(ABSOLUTE_ACCURACY, BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.tan(x) - x;
            }
        };
        double result = solver.solve(100, f, 0, 10);
        assertEquals(9.21478583363561, result, ABSOLUTE_ACCURACY);
    }

    @Test
    public void testSolveWithStartValue() {
        BaseSecantSolver solver = new BaseSecantSolver(ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double result = solver.solve(100, f, 0, 10, 1);
        assertEquals(2.000000693627351, result, ABSOLUTE_ACCURACY);
    }

    @Test
    public void testSolveWithAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(ABSOLUTE_ACCURACY, BaseSecantSolver.Method.ILLINOIS);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sqrt(x) - x;
            }
        };

        // Test ANY_SIDE
        double result = solver.solve(100, f, 0, 1, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(0.9999999997817216, result, ABSOLUTE_ACCURACY);

        // Test LEFT_SIDE
        result = solver.solve(100, f, 0, 1, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(0.9999999997817216, result, ABSOLUTE_ACCURACY);

        // Test RIGHT_SIDE
        result = solver.solve(100, f, 0, 1, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        assertEquals(0.9999999997817216, result, ABSOLUTE_ACCURACY);

        // Test BELOW_SIDE
        result = solver.solve(100, f, 0, 1, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        assertEquals(0.9999999997817216, result, ABSOLUTE_ACCURACY);

        // Test ABOVE_SIDE
        try {
            result = solver.solve(100, f, 0, 1, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
            fail("Expected MathInternalError");
        } catch (MathInternalError e) {
            // Expected
        }
    }

    @Test
    public void testDoSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(ABSOLUTE_ACCURACY, BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sqrt(x) - x;
            }
        };
        solver.setFunctionValueAccuracy(FUNCTION_VALUE_ACCURACY);
        solver.setAbsoluteAccuracy(ABSOLUTE_ACCURACY);
        solver.setRelativeAccuracy(RELATIVE_ACCURACY);

        // Test when the solution is found on the first iteration
        double result = solver.doSolve();
        assertEquals(0.9999599450545738, result, ABSOLUTE_ACCURACY);
        assertEquals(1, solver.getEvaluations());

        // Test when the solution is found on the second iteration
        solver.setMaxEvaluations(100);
        result = solver.doSolve();
        assertEquals(0.9999599450545738, result, ABSOLUTE_ACCURACY);
        assertEquals(2, solver.getEvaluations());

        // Test when the solution is found after multiple iterations
        solver.setMaxEvaluations(100);
        f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x) + Math.cos(x) - x;
            }
        };
        result = solver.doSolve();
        assertEquals(0.5893269460120274, result, ABSOLUTE_ACCURACY);
        assertEquals(59, solver.getEvaluations());

        // Test when the solution is not found within the maximum number of iterations
        solver.setMaxEvaluations(3);
        try {
            result = solver.doSolve();
            fail("Expected MathInternalError");
        } catch (MathInternalError e) {
            // Expected
        }
    }
}