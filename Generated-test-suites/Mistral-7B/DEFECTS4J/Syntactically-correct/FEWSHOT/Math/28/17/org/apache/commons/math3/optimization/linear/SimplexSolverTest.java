package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.LinearConstraint;
import org.apache.commons.math3.optimization.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.SimplexTableau;
import org.apache.commons.math3.optimization.goaltype.Maximize;
import org.apache.commons.math3.optimization.linear.NonNegativeConstraint;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimplexSolverTest {
    @Test
    public void testDoOptimize() throws Exception {
        // Test simple case
        LinearObjectiveFunction func = new LinearObjectiveFunction(new double[]{-3.0, 2.0}, new double[]{1.0, 1.0});
        LinearConstraint[] constraints = new LinearConstraint[]{
                new LinearConstraint(new double[]{1.0, 1.0}, 3.0, Relationship.GREATER_EQUAL),
                new LinearConstraint(new double[]{1.0, 1.0}, 5.0, Relationship.LESS_EQUAL),
                new LinearConstraint(new double[]{-1.0, 1.0}, 2.0, Relationship.LESS_EQUAL),
                new LinearConstraint(new double[]{1.0, -1.0}, 4.0, Relationship.LESS_EQUAL)
        };
        SimplexSolver solver = new SimplexSolver();

        PointValuePair solution = solver.doOptimize();

        assertNotNull(solution);
        assertEquals(new double[]{0.0, 3.0}, solution.getPoint(), 0.0);
        assertEquals(-3.0, solution.getValue(), 0.0);

        // Test with non-negativity constraints
        LinearObjectiveFunction func2 = new LinearObjectiveFunction(new double[]{-3.0, 2.0}, new double[]{1.0, 1.0});
        LinearConstraint[] constraints2 = new LinearConstraint[]{
                new LinearConstraint(new double[]{1.0, 1.0}, 3.0, Relationship.GREATER_EQUAL),
                new LinearConstraint(new double[]{1.0, 1.0}, 5.0, Relationship.LESS_EQUAL),
                new NonNegativeConstraint(new double[]{1.0, 1.0}),
                new NonNegativeConstraint(new double[]{-1.0, 1.0}),
                new NonNegativeConstraint(new double[]{1.0, -1.0})
        };
        solver = new SimplexSolver();

        PointValuePair solution2 = solver.doOptimize();

        assertNotNull(solution2);
        assertEquals(new double[]{0.0, 3.0}, solution2.getPoint(), 0.0);
        assertEquals(-3.0, solution2.getValue(), 0.0);

        // Test with max goal type
        LinearObjectiveFunction func3 = new LinearObjectiveFunction(new double[]{3.0, 2.0}, new double[]{1.0, 1.0});
        LinearConstraint[] constraints3 = new LinearConstraint[]{
                new LinearConstraint(new double[]{1.0, 1.0}, 3.0, Relationship.GREATER_EQUAL),
                new LinearConstraint(new double[]{1.0, 1.0}, 5.0, Relationship.LESS_EQUAL),
                new LinearConstraint(new double[]{1.0, 1.0}, 2.0, Relationship.LESS_EQUAL),
                new LinearConstraint(new double[]{1.0, -1.0}, 4.0, Relationship.LESS_EQUAL)
        };
        SimplexSolver solver3 = new SimplexSolver(new Maximize());

        PointValuePair solution3 = solver3.doOptimize();

        assertNotNull(solution3);
        assertEquals(new double[]{3.0 / 2.0, 3.0 / 2.0}, solution3.getPoint(), 0.0);
        assertEquals(3.0, solution3.getValue(), 0.0);

        // Test with unbounded solution
        LinearObjectiveFunction func4 = new LinearObjectiveFunction(new double[]{-3.0, 2.0}, new double[]{1.0, 1.0});
        LinearConstraint[] constraints4 = new LinearConstraint[]{
                new LinearConstraint(new double[]{1.0, 1.0}, 3.0, Relationship.GREATER_EQUAL),
                new LinearConstraint(new double[]{1.0, 1.0}, 3.0, Relationship.LESS_EQUAL),
                new LinearConstraint(new double[]{1.0, 1.0}, 2.0, Relationship.LESS_EQUAL),
                new LinearConstraint(new double[]{1.0, -1.0}, 4.0, Relationship.LESS_EQUAL)
        };
        SimplexSolver solver4 = new SimplexSolver();

        try {
            solver4.doOptimize();
            fail("Expected UnboundedSolutionException");
        } catch (UnboundedSolutionException e) {
            // Expected
        }

        // Test with no feasible solution
        LinearObjectiveFunction func5 = new LinearObjectiveFunction(new double[]{3.0, 2.0}, new double[]{1.0, 1.0});
        LinearConstraint[] constraints5 = new LinearConstraint[]{
                new LinearConstraint(new double[]{1.0, 1.0}, 2.0, Relationship.LESS_EQUAL),
                new LinearConstraint(new double[]{1.0, 1.0}, 2.0, Relationship.GREATER_EQUAL),
                new LinearConstraint(new double[]{1.0, 1.0}, 2.0, Relationship.EQUAL)
        };
        SimplexSolver solver5 = new SimplexSolver();

        try {
            solver5.doOptimize();
            fail("Expected NoFeasibleSolutionException");
        } catch (NoFeasibleSolutionException e) {
            // Expected
        }

        // Test with max iteration count exceeded
        SimplexSolver solver6 = new SimplexSolver(1);

        try {
            solver6.doOptimize();
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // Expected
        }
    }
}