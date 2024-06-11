package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.util.FastMath;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;

import org.junit.Test;

public class BaseSecantSolverTest {

    /**
     * Test method for {@link BaseSecantSolver#solve(int, UnivariateRealFunction, double, double, AllowedSolution)}.
     */
    @Test
    public void testSolve() {
        // Create a test function
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * 2 - 2;
            }
        };

        // Create an instance of BaseSecantSolver
        BaseSecantSolver solver = new BaseSecantSolver() {
            protected double computeObjectiveValue(double x) {
                return f.value(x);
            }
        };

        // Set the maximum number of evaluations
        int maxEval = 100;

        // Set the bounds
        double min = 0.0;
        double max = 10.0;

        // Set the allowed solution
        AllowedSolution allowed = AllowedSolution.ANY_SIDE;

        // Call the solve method
        double result = solver.solve(maxEval, f, min, max, allowed);

        // Check the result
        assertEquals(1.0, result, 1e-6);
    }
}