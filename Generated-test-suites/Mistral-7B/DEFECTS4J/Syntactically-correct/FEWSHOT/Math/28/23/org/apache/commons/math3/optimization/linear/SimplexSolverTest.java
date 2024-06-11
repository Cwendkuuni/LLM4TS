package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimplexTableau;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.junit.Test;

public class SimplexSolverTest {
    @Test
    public void testDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        // Test with default settings
        SimplexSolver solver = new SimplexSolver();
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{1.0, 3.0});
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(new double[]{-1.0, 1.0}, 0.0),
            new LinearConstraint(new double[]{1.0, 1.0}, 0.0),
            new LinearConstraint(new double[]{2.0, 1.0}, 4.0),
            new LinearConstraint(new double[]{2.0, 2.0}, 0.0)
        };
        GoalType goalType = GoalType.MINIMIZE;
        boolean restrictToNonNegative = false;
        PointValuePair result = solver.doOptimize();
        assertEquals(0.0, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{1.0 / 3.0, 2.0 / 3.0}, result.getPoint(), 1e-6);

        // Test with custom settings
        SimplexSolver solverCustom = new SimplexSolver(1e-8, 5);
        PointValuePair resultCustom = solverCustom.doOptimize();
        assertEquals(0.0, resultCustom.getValue(), 1e-8);
        assertArrayEquals(new double[]{1.0 / 3.0, 2.0 / 3.0}, resultCustom.getPoint(), 1e-8);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testMaxIterations() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexSolver solver = new SimplexSolver();
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{1.0, 3.0});
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(new double[]{-1.0, 1.0}, 0.0),
            new LinearConstraint(new double[]{1.0, 1.0}, 0.0),
            new LinearConstraint(new double[]{2.0, 1.0}, 4.0),
            new LinearConstraint(new double[]{2.0, 2.0}, 0.0)
        };
        GoalType goalType = GoalType.MINIMIZE;
        boolean restrictToNonNegative = false;
        SimplexTableau tableau = new SimplexTableau(obj, constraints, goalType, restrictToNonNegative, 1e-6, 10);
        int iterationsCounter = SimplexSolver.class.getDeclaredField("iterationsCounter");
        iterationsCounter.setAccessible(true);
        iterationsCounter.setInt(solver, 1000);
        solver.doOptimize();
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testUnboundedSolution() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexSolver solver = new SimplexSolver();
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{2.0}, new double[]{0.0});
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(new double[]{}, 0.0)
        };
        GoalType goalType = GoalType.MINIMIZE;
        boolean restrictToNonNegative = false;
        solver.doOptimize();
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        SimplexSolver solver = new SimplexSolver();
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{1.0, 3.0});
        LinearConstraint[] constraints = new LinearConstraint[]{
            new LinearConstraint(new double[]{-1.0, 1.0}, 1.0),
            new LinearConstraint(new double[]{1.0, 1.0}, 0.0),
            new LinearConstraint(new double[]{2.0, 1.0}, 4.0),
            new LinearConstraint(new double[]{2.0, 2.0}, 0.0)
        };
        GoalType goalType = GoalType.MINIMIZE;
        boolean restrictToNonNegative = false;
        solver.doOptimize();
    }
}