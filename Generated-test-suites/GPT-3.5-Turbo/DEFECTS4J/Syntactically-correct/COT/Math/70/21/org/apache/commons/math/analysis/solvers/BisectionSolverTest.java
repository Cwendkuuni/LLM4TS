package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Assert;
import org.junit.Test;

public class BisectionSolverTest {

    @Test
    public void testSolve() throws MaxIterationsExceededException, FunctionEvaluationException {
        BisectionSolver solver = new BisectionSolver();

        // Test case 1
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x - 3;
            }
        };
        double solution1 = solver.solve(f1, 0, 5);
        Assert.assertEquals(3, solution1, 0);

        // Test case 2
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        double solution2 = solver.solve(f2, -2, 2);
        Assert.assertEquals(2, solution2, 0);

        // Test case 3
        UnivariateRealFunction f3 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x - 4;
            }
        };
        try {
            solver.solve(f3, -1, 1);
            Assert.fail("MaxIterationsExceededException should be thrown");
        } catch (MaxIterationsExceededException e) {
            // Expected exception
        }

        // Test case 4 (Edge case)
        UnivariateRealFunction f4 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x;
            }
        };
        double solution4 = solver.solve(f4, -1, 1);
        Assert.assertEquals(0, solution4, 0);
    }
}