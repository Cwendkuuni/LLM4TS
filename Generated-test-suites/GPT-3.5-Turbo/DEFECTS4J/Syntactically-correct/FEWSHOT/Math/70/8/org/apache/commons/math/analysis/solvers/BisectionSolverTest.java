package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testSolve() throws FunctionEvaluationException, MaxIterationsExceededException {
        // create an instance of BisectionSolver
        BisectionSolver solver = new BisectionSolver();

        // create an instance of the function to solve
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 5;
            }
        };

        try {
            // test solving a function that has known root
            double solution = solver.solve(function, 2, 3);
            assertEquals(2.23607, solution, 0.00001);

            // test solving a function that does not have a root in the given interval
            solver.solve(function, 1, 2);
            fail("Expected MaxIterationsExceededException to be thrown");
        } catch (MaxIterationsExceededException e) {
            assertTrue(true);
        }
    }
}