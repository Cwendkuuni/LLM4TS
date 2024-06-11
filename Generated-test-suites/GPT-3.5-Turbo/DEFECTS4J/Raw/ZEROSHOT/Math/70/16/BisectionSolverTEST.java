package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;

import org.junit.Test;

import static org.junit.Assert.*;

public class BisectionSolverTest {

    @Test
    public void testSolve() throws FunctionEvaluationException, MaxIterationsExceededException {
        // Create a test function
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x*x*x - x - 1;
            }
        };

        // Create a BisectionSolver object
        BisectionSolver solver = new BisectionSolver();

        // Test the solve method with different inputs
        try {
            double result = solver.solve(f, 0, 2);
            assertEquals(1.3247, result, 0.0001);
        } catch (MaxIterationsExceededException e) {
            fail("Max iterations exceeded");
        }

        try {
            double result = solver.solve(f, -10, 10);
            assertEquals(1.3247, result, 0.0001);
        } catch (MaxIterationsExceededException e) {
            fail("Max iterations exceeded");
        }

        try {
            double result = solver.solve(f, 1, 1.5);
            assertEquals(1.3247, result, 0.0001);
        } catch (MaxIterationsExceededException e) {
            fail("Max iterations exceeded");
        }

        try {
            double result = solver.solve(f, 2, 3);
            fail("Expected exception not thrown");
        } catch (MaxIterationsExceededException e) {
            // Expected exception
        }
    }

    @Test
    public void testSolveWithInitial() throws FunctionEvaluationException, MaxIterationsExceededException {
        // Create a test function
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x*x - 5*x + 6;
            }
        };

        // Create a BisectionSolver object
        BisectionSolver solver = new BisectionSolver();

        // Test the solve method with different inputs
        try {
            double result = solver.solve(f, 0, 3, 0.5);
            assertEquals(2, result, 0.0001);
        } catch (MaxIterationsExceededException e) {
            fail("Max iterations exceeded");
        }

        try {
            double result = solver.solve(f, 0, 3, 2.5);
            assertEquals(2, result, 0.0001);
        } catch (MaxIterationsExceededException e) {
            fail("Max iterations exceeded");
        }

        try {
            double result = solver.solve(f, 0, 3, 1);
            assertEquals(2, result, 0.0001);
        } catch (MaxIterationsExceededException e) {
            fail("Max iterations exceeded");
        }

        try {
            double result = solver.solve(f, 0, 3, 4);
            fail("Expected exception not thrown");
        } catch (MaxIterationsExceededException e) {
            // Expected exception
        }
    }

    @Test
    public void testSolveWithDeprecated() throws FunctionEvaluationException, MaxIterationsExceededException {
        // Create a test function
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x*x - 2;
            }
        };

        // Create a BisectionSolver object
        BisectionSolver solver = new BisectionSolver(f);

        // Test the solve method with different inputs
        try {
            double result = solver.solve(1, 2);
            assertEquals(1.4142, result, 0.0001);
        } catch (MaxIterationsExceededException e) {
            fail("Max iterations exceeded");
        }

        try {
            double result = solver.solve(-10, 10);
            assertEquals(1.4142, result, 0.0001);
        } catch (MaxIterationsExceededException e) {
            fail("Max iterations exceeded");
        }

        try {
            double result = solver.solve(0, 10);
            assertEquals(1.4142, result, 0.0001);
        } catch (MaxIterationsExceededException e) {
            fail("Max iterations exceeded");
        }
    }
}