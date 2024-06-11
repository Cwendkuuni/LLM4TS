
package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class BisectionSolverTest {

    private static final double EPSILON = 1e-6;

    @Test
    public void testSolveWithLinearFunction() {
        UnivariateRealFunction linearFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return 3 * x - 2;
            }
        };

        BisectionSolver solver = new BisectionSolver(linearFunction);
        try {
            double result = solver.solve(-10, 10);
            assertEquals(2.0 / 3.0, result, EPSILON);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testSolveWithQuadraticFunction() {
        UnivariateRealFunction quadraticFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4 * x + 3;
            }
        };

        BisectionSolver solver = new BisectionSolver(quadraticFunction);
        try {
            double result = solver.solve(0, 10);
            assertEquals(1.0, result, EPSILON);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testSolveWithConstantFunction() {
        UnivariateRealFunction constantFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return 5.0;
            }
        };

        BisectionSolver solver = new BisectionSolver(constantFunction);
        try {
            double result = solver.solve(-10, 10);
            assertEquals(0.0, result, EPSILON);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testSolveWithExponentialFunction() {
        UnivariateRealFunction exponentialFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return Math.exp(x) - 1;
            }
        };

        BisectionSolver solver = new BisectionSolver(exponentialFunction);
        try {
            double result = solver.solve(-10, 10);
            assertEquals(0.0, result, EPSILON);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testSolveWithTrigonometricFunction() {
        UnivariateRealFunction trigonometricFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                return Math.sin(x);
            }
        };

        BisectionSolver solver = new BisectionSolver(trigonometricFunction);
        try {
            double result = solver.solve(0, 2 * Math.PI);
            assertEquals(Math.PI, result, EPSILON);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}
