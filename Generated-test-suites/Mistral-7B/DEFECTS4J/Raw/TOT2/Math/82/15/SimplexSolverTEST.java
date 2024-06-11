import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleOptimizationException;
import org.apache.commons.math3.optim.linear.GoalType;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunctionSet;
import org.apache.commons.math3.optim.linear.NonNegativeConstraint;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testDoOptimize_GivenSimpleLinearProblem_OptimalSolutionIsReturned() throws Exception {
        // Given a simple linear problem
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-3.0, -2.0, -1.0}, 0.0);
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{1.0, 1.0}, Relation.LEQUAL, 5.0));
        LinearConstraintSet nonNegativeConstraints = new LinearConstraintSet();
        nonNegativeConstraints.addConstraint(new NonNegativeConstraint(new int[]{0, 1, 2}));

        // When using SimplexSolver to solve the problem
        SimplexSolver solver = new SimplexSolver(1e-6);
        PointValuePair solution = solver.optimize(new LinearObjectiveFunctionSet(f),
                new LinearConstraintSet[]{constraints}, null,
                new LinearConstraintSet[]{nonNegativeConstraints}, GoalType.MINIMIZE);

        // Then the optimal solution is returned
        assertEquals(0.0, solution.getValue(), 1e-6);
        assertArrayEquals(new double[]{1.0 / 3.0, 2.0 / 3.0, 1.0}, solution.getPoint(), 1e-6);
    }

    @Test
    public void testDoOptimize_GivenUnboundedLinearProblem_UnboundedSolutionExceptionIsThrown() throws Exception {
        // Given an unbounded linear problem
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-3.0, -2.0, -1.0}, 0.0);
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{-1.0, -1.0}, Relation.LEQUAL, 0.0));

        // When using SimplexSolver to solve the problem
        SimplexSolver solver = new SimplexSolver(1e-6);

        // Then an UnboundedSolutionException is thrown
        try {
            solver.optimize(new LinearObjectiveFunctionSet(f),
                    new LinearConstraintSet[]{constraints}, null,
                    new LinearConstraintSet[]{new LinearConstraintSet()}, GoalType.MINIMIZE);
            fail();
        } catch (UnboundedSolutionException e) {
            // expected
        }
    }

    @Test
    public void testDoOptimize_GivenNoFeasibleLinearProblem_NoFeasibleSolutionExceptionIsThrown() throws Exception {
        // Given a no feasible linear problem
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-3.0, -2.0, -1.0}, 0.0);
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{1.0, 1.0}, Relation.LEQUAL, 5.0));
        LinearConstraintSet nonNegativeConstraints = new LinearConstraintSet();
        nonNegativeConstraints.addConstraint(new NonNegativeConstraint(new int[]{1, 2}));

        // When using SimplexSolver to solve the problem
        SimplexSolver solver = new SimplexSolver(1e-6);

        // Then a NoFeasibleSolutionException is thrown
        try {
            solver.optimize(new LinearObjectiveFunctionSet(f),
                    new LinearConstraintSet[]{constraints}, null,
                    new LinearConstraintSet[]{nonNegativeConstraints}, GoalType.MINIMIZE);
            fail();
        } catch (NoFeasibleSolutionException e) {
            // expected
        }
    }

    @Test(expected = OptimizationException.class)
    public void testDoOptimize_GivenMaxIterationsReached_OptimizationExceptionIsThrown() throws Exception {
        // Given a linear problem with maximum iterations reached
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-3.0, -2.0, -1.0}, 0.0);
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{1.0, 1.0}, Relation.LEQUAL, 5.0));
        LinearConstraintSet nonNegativeConstraints = new LinearConstraintSet();
        nonNegativeConstraints.addConstraint(new NonNegativeConstraint(new int[]{0, 1, 2}));

        // When using SimplexSolver to solve the problem with maximum iterations reached
        SimplexSolver solver = new SimplexSolver(1e-6, 10);

        // Then an OptimizationException is thrown
        solver.optimize(new LinearObjectiveFunctionSet(f),
                new LinearConstraintSet[]{constraints}, null,
                new LinearConstraintSet[]{nonNegativeConstraints}, GoalType.MINIMIZE);
    }
}