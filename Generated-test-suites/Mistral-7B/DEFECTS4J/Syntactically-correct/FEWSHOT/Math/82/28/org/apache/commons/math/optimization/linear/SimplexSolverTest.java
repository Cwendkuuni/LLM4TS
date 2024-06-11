package org.apache.commons.math.optimization.linear;

import static org.junit.Assert.*;

import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.SimplexTableau;
import org.apache.commons.math.optimization.goals.MaximizationGoal;
import org.apache.commons.math.optimization.linear.LinearConstraint;
import org.apache.commons.math.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math.optimization.linear.SimplexSolver;
import org.apache.commons.math.util.MathUtils;

public class SimplexSolverTest {

    @Test
    public void testDoOptimize() throws OptimizationException {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-3, 2}, 3);
        LinearConstraint[] constraints = new LinearConstraint[] {
            new LinearConstraint(new double[]{1, 2}, 2),
            new LinearConstraint(new double[]{1, 1}, 3),
            new LinearConstraint(new double[]{1, -1}, 0),
            new LinearConstraint(new double[]{-1, 1}, 1),
            new LinearConstraint(new double[]{-1, -1}, 1)
        };
        SimplexSolver solver = new SimplexSolver(1.0e-6);
        RealPointValuePair solution = solver.doOptimize();
        assertEquals(0, solution.getPoint()[0], 1.0e-6);
        assertEquals(1, solution.getPoint()[1], 1.0e-6);
        assertEquals(3, solution.getValue(), 1.0e-6);
    }

    @Test
    public void testDoOptimize_Minimization() throws OptimizationException {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{3, 2}, -3);
        LinearConstraint[] constraints = new LinearConstraint[] {
            new LinearConstraint(new double[]{1, 2}, 2),
            new LinearConstraint(new double[]{1, 1}, 3),
            new LinearConstraint(new double[]{1, -1}, 0),
            new LinearConstraint(new double[]{-1, 1}, 1),
            new LinearConstraint(new double[]{-1, -1}, 1)
        };
        SimplexSolver solver = new SimplexSolver(1.0e-6);
        solver.setGoalType(MaximizationGoal.MINIMIZATION_GOAL);
        RealPointValuePair solution = solver.doOptimize();
        assertEquals(0, solution.getPoint()[0], 1.0e-6);
        assertEquals(1, solution.getPoint()[1], 1.0e-6);
        assertEquals(-3, solution.getValue(), 1.0e-6);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testDoOptimize_NoFeasibleSolution() throws OptimizationException {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{3, 2}, 3);
        LinearConstraint[] constraints = new LinearConstraint[] {
            new LinearConstraint(new double[]{1, 2}, 1),
            new LinearConstraint(new double[]{1, 1}, 3),
            new LinearConstraint(new double[]{1, -1}, 0),
            new LinearConstraint(new double[]{-1, 1}, 1),
            new LinearConstraint(new double[]{-1, -1}, 1)
        };
        SimplexSolver solver = new SimplexSolver(1.0e-6);
        solver.doOptimize();
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoOptimize_UnboundedSolution() throws OptimizationException {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{3, 2}, 3);
        LinearConstraint[] constraints = new LinearConstraint[] {
            new LinearConstraint(new double[]{1, 2}, 2),
            new LinearConstraint(new double[]{1, 1}, 1),
            new LinearConstraint(new double[]{1, -1}, 0),
            new LinearConstraint(new double[]{-1, 1}, 1),
            new LinearConstraint(new double[]{-1, -1}, 1)
        };
        SimplexSolver solver = new SimplexSolver(1.0e-6);
        solver.doOptimize();
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{
            {-3, 2, 1, 1, 1},
            {2, -1, 1, 2, -1},
            {1, 1, 0, 0, 1},
            {1, -1, 0, 1, -1},
            {1, 1, -1, -1, 1},
            {0, 0, 2, 3, 0},
            {0, 0, 0, 0, 1}
        }, new LinearConstraint[0], MaximizationGoal.MAXIMIZATION_GOAL,
            false, 1.0e-6);
        Integer pivotCol = SimplexSolver.this.getPivotColumn(tableau);
        assertEquals(5, pivotCol.intValue());
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{
            {-3, 2, 1, 1, 1},
            {2, -1, 1, 2, -1},
            {1, 1, 0, 0, 1},
            {1, -1, 0, 1, -1},
            {1, 1, -1, -1, 1},
            {0, 0, 2, 3, 0},
            {0, 0, 0, 0, 1}
        }, new LinearConstraint[0], MaximizationGoal.MAXIMIZATION_GOAL,
            false, 1.0e-6);
        Integer pivotRow = SimplexSolver.this.getPivotRow(5, tableau);
        assertEquals(5, pivotRow.intValue());
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{
            {-3, 2, 1, 1, 1},
            {2, -1, 1, 2, -1},
            {1, 1, 0, 0, 1},
            {1, -1, 0, 1, -1},
            {1, 1, -1, -1, 1},
            {0, 0, 2, 3, 0},
            {0, 0, 0, 0, 1}
        }, new LinearConstraint[0], MaximizationGoal.MAXIMIZATION_GOAL,
            false, 1.0e-6);
        boolean result = SimplexSolver.this.isPhase1Solved(tableau);
        assertFalse(result);

        // add a constraint to make the problem feasible
        LinearConstraint constraint = new LinearConstraint(new double[]{1, 1}, 0);
        SimplexTableau tableau2 = new SimplexTableau(tableau.getTable(), tableau.getConstraints(),
            tableau.getGoalType(), tableau.isRestrictToNonNegative(), tableau.getEpsilon());
        tableau2.addConstraint(constraint);
        result = SimplexSolver.this.isPhase1Solved(tableau2);
        assertTrue(result);
    }

    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau(new double[][]{
            {-3, 2, 1, 1, 1},
            {2, -1, 1, 2, -1},
            {1, 1, 0, 0, 1},
            {1, -1, 0, 1, -1},
            {1, 1, -1, -1, 1},
            {0, 0, 2, 3, 0},
            {0, 0, 0, 0, 1}
        }, new LinearConstraint[0], MaximizationGoal.MAXIMIZATION_GOAL,
            false, 1.0e-6);
        boolean result = SimplexSolver.this.isOptimal(tableau);
        assertFalse(result);

        // add a constraint to make the problem feasible
        LinearConstraint constraint = new LinearConstraint(new double[]{1, 1}, 0);
        SimplexTableau tableau2 = new SimplexTableau(tableau.getTable(), tableau.getConstraints(),
            tableau.getGoalType(), tableau.isRestrictToNonNegative(), tableau.getEpsilon());
        tableau2.addConstraint(constraint);

        // run Phase 1
        SimplexSolver solver = new SimplexSolver(1.0e-6);
        solver.solvePhase1(tableau2);

        // check if Phase 1 is solved
        result = SimplexSolver.this.isPhase1Solved(tableau2);
        assertTrue(result);

        // run one more iteration to reach the optimal solution
        solver.doIteration(tableau2);

        // check if the problem is at an optimal state
        result = SimplexSolver.this.isOptimal(tableau2);
        assertTrue(result);
    }
}