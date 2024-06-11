
package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.analysis.UnivariateFunction;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BracketingNthOrderBrentSolverTest {

    @Test
    public void testSolve() {
        // Create the solver
        BracketingNthOrderBrentSolver solver = new BracketingNthOrderBrentSolver();

        // Define the function
        UnivariateFunction f = new UnivariateFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        // Solve for the root within the given interval
        double root = solver.solve(100, f, 0, 10);

        // Check the result
        assertEquals(2.0, root, 1e-6);
    }
}
