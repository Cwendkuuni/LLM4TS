package org.apache.commons.math.analysis.solvers;

import org.junit.Test;
import static org.junit.Assert.*;

public class BisectionSolverTest {

    @Test
    public void testSolveWithFunction() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        BisectionSolver solver = new BisectionSolver(function);
        double result = solver.solve(0, 3);
        assertEquals(2.0, result, 1E-6);
    }

    @Test
    public void testSolveWithoutFunction() {
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(0, 3, 0.5);
        assertEquals(2.0, result, 1E-6);
    }

    @Test
    public void invalidIntervalTest() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        BisectionSolver solver = new BisectionSolver(function);
        try {
            solver.solve(3, 0);
            fail("Expected exception not thrown");
        } catch (MaxIterationsExceededException e) {
            // Expected exception
        } catch (FunctionEvaluationException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void maxIterationsExceededTest() {
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4;
            }
        };
        BisectionSolver solver = new BisectionSolver(function);
        try {
            solver.solve(0, 3);
            fail("Expected exception not thrown");
        } catch (MaxIterationsExceededException e) {
            // Expected exception
        } catch (FunctionEvaluationException e) {
            fail("Unexpected exception");
        }
    }
}