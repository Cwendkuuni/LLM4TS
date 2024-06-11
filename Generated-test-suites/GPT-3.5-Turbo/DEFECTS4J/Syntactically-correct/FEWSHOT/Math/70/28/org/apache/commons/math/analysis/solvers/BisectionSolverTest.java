
package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class BisectionSolverTest {

    @Test
    public void testSolve() {
        BisectionSolver solver = new BisectionSolver();

        // test normal case
        UnivariateRealFunction function1 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.pow(x, 2) - 4;
            }
        };
        try {
            double result = solver.solve(function1, 1, 3);
            assertEquals(2, result, 0);
        } catch (MaxIterationsExceededException e) {
            fail("MaxIterationsExceededException should not be thrown");
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException should not be thrown");
        }

        // test case where function is always positive
        UnivariateRealFunction function2 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.pow(x, 2) + 4;
            }
        };
        try {
            solver.solve(function2, 1, 3);
            fail("MaxIterationsExceededException should be thrown");
        } catch (MaxIterationsExceededException e) {
            // expected behavior
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException should not be thrown");
        }

        // test case where function is always negative
        UnivariateRealFunction function3 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.pow(x, 2) - 10;
            }
        };
        try {
            solver.solve(function3, 1, 3);
            fail("MaxIterationsExceededException should be thrown");
        } catch (MaxIterationsExceededException e) {
            // expected behavior
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException should not be thrown");
        }

        // test case where the root is at the lower bound
        UnivariateRealFunction function4 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.pow(x, 2) - 1;
            }
        };
        try {
            double result = solver.solve(function4, 1, 3);
            assertEquals(1, result, 0);
        } catch (MaxIterationsExceededException e) {
            fail("MaxIterationsExceededException should not be thrown");
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException should not be thrown");
        }

        // test case where the root is at the upper bound
        UnivariateRealFunction function5 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.pow(x, 2) - 9;
            }
        };
        try {
            double result = solver.solve(function5, 1, 3);
            assertEquals(3, result, 0);
        } catch (MaxIterationsExceededException e) {
            fail("MaxIterationsExceededException should not be thrown");
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException should not be thrown");
        }

        // test case where the initial guess is the root
        UnivariateRealFunction function6 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.pow(x, 2) - 4;
            }
        };
        try {
            double result = solver.solve(function6, 2, 3, 2);
            assertEquals(2, result, 0);
        } catch (MaxIterationsExceededException e) {
            fail("MaxIterationsExceededException should not be thrown");
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException should not be thrown");
        }

        // test case where the initial guess is not close to the root
        UnivariateRealFunction function7 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.pow(x, 2) - 4;
            }
        };
        try {
            double result = solver.solve(function7, 1, 3, 5);
            assertEquals(2, result, 0);
        } catch (MaxIterationsExceededException e) {
            fail("MaxIterationsExceededException should not be thrown");
        } catch (FunctionEvaluationException e) {
            fail("FunctionEvaluationException should not be thrown");
        }
    }
}
