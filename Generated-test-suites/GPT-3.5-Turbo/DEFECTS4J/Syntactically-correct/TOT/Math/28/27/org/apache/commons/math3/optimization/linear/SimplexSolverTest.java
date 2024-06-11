import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.Relationship;
import org.junit.Before;
import org.junit.Test;

public class SimplexSolverTest {

    private SimplexSolver solver;

    @Before
    public void setUp() {
        solver = new SimplexSolver();
    }

    @Test
    public void testDoOptimize() {

        // 1. Test typical use case
        LinearObjectiveFunction function = new LinearObjectiveFunction(new double[]{5, 4}, 0);
        LinearConstraint constraint1 = new LinearConstraint(new double[]{3, 2}, Relationship.LEQ, 7);
        LinearConstraint constraint2 = new LinearConstraint(new double[]{-1, 1}, Relationship.LEQ, 2);
        LinearConstraint[] constraints = new LinearConstraint[]{constraint1, constraint2};

        try {
            PointValuePair solution = solver.optimize(function, constraints, GoalType.MAXIMIZE, true);
            assertNotNull(solution);
            assertEquals(10.0, solution.getValue(), 1e-6);
            assertTrue(solution.getPoint().length == 2);
            assertEquals(1.0, solution.getPoint()[0], 1e-6);
            assertEquals(2.0, solution.getPoint()[1], 1e-6);
        } catch (MaxCountExceededException | UnboundedSolutionException | NoFeasibleSolutionException e) {
            fail("Unexpected exception " + e.getMessage());
        }

        // 2. Test edge case with equal constraint coefficients
        function = new LinearObjectiveFunction(new double[]{1, 1}, 0);
        constraint1 = new LinearConstraint(new double[]{1, 1}, Relationship.EQ, 1);
        constraints = new LinearConstraint[]{constraint1};

        try {
            PointValuePair solution = solver.optimize(function, constraints, GoalType.MAXIMIZE, true);
            assertNotNull(solution);
            assertEquals(0.0, solution.getValue(), 1e-6);
            assertTrue(solution.getPoint().length == 2);
            assertEquals(0.5, solution.getPoint()[0], 1e-6);
            assertEquals(0.5, solution.getPoint()[1], 1e-6);
        } catch (MaxCountExceededException | UnboundedSolutionException | NoFeasibleSolutionException e) {
            fail("Unexpected exception " + e.getMessage());
        }

        // 3. Test error scenario with unbounded solution
        function = new LinearObjectiveFunction(new double[]{1, -2}, 0);
        constraint1 = new LinearConstraint(new double[]{1, -1}, Relationship.GEQ, 0);
        constraint2 = new LinearConstraint(new double[]{2, -1}, Relationship.GEQ, 0);
        constraints = new LinearConstraint[]{constraint1, constraint2};

        try {
            solver.optimize(function, constraints, GoalType.MAXIMIZE, true);
            fail("Expected UnboundedSolutionException, but no exception was thrown");
        } catch (MaxCountExceededException | UnboundedSolutionException | NoFeasibleSolutionException e) {
            assertTrue(e instanceof UnboundedSolutionException);
        }

        // 4. Test error scenario with no feasible solution
        function = new LinearObjectiveFunction(new double[]{1, 1}, 0);
        constraint1 = new LinearConstraint(new double[]{-1, -1}, Relationship.LEQ, -1);
        constraints = new LinearConstraint[]{constraint1};

        try {
            solver.optimize(function, constraints, GoalType.MAXIMIZE, true);
            fail("Expected NoFeasibleSolutionException, but no exception was thrown");
        } catch (MaxCountExceededException | UnboundedSolutionException | NoFeasibleSolutionException e) {
            assertTrue(e instanceof NoFeasibleSolutionException);
        }
    }
}