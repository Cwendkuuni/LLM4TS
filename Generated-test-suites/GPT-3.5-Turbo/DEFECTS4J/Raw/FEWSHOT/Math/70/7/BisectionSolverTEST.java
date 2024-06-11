package org.apache.commons.math.analysis.solvers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Before;
import org.junit.Test;

public class BisectionSolverTest {
    private BisectionSolver solver;
    private UnivariateRealFunction function;

    @Before
    public void setUp() {
        solver = new BisectionSolver();
        function = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
    }

    @Test
    public void testSolve() throws MaxIterationsExceededException, FunctionEvaluationException {
        double result = solver.solve(function, -10, 10);
        assertEquals(2.0, result, 0.01);
    }

    @Test
    public void testSolveWithInvalidInterval() {
        try {
            solver.solve(function, 5, 3);
            fail("Expected MaxIterationsExceededException not thrown");
        } catch (MaxIterationsExceededException e) {
            assertNotNull(e);
        } catch (FunctionEvaluationException e) {
            fail("Unexpected FunctionEvaluationException thrown");
        }
    }
}