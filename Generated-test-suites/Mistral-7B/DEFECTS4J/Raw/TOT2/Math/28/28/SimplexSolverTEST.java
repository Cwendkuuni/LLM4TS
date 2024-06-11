package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.LinearConstraintSet;
import org.apache.commons.math3.optimization.NonNegativeConstraint;

import java.util.Arrays;

public class SimplexSolverTest {

    /**
     * Test method for {@link SimplexSolver#SimplexSolver()}.
     */
    @Test
    public void testDefaultConstructor() {
        SimplexSolver solver = new SimplexSolver();
        assertEquals(1.0e-6, solver.getEpsilon(), 0);
        assertEquals(10, solver.getMaxUlps());
    }

    /**
     * Test method for {@link SimplexSolver#SimplexSolver(double, int)}.
     */
    @Test
    public void testCustomConstructor() {
        SimplexSolver solver = new SimplexSolver(0.001, 5);
        assertEquals(0.001, solver.getEpsilon(), 0);
        assertEquals(5, solver.getMaxUlps());
    }

    /**
     * Test method for {@link SimplexSolver#getPivotColumn(SimplexTableau)}.
     */
    @Test
    public void testGetPivotColumn() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1, 2}, new double[]{1, 1});
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{1}, 2));
        constraints.addConstraint(new LinearConstraint(new double[]{-1, 1}, 1));
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, new NonNegativeConstraint[0], 1e-6, 10);
        solver.solvePhase1(tableau);
        Integer pivotColumn = solver.getPivotColumn(tableau);
        assertTrue(pivotColumn.intValue() >= 0 && pivotColumn.intValue() < tableau.getWidth() - 1);
    }

    /**
     * Test method for {@link SimplexSolver#getPivotRow(SimplexTableau, int)}.
     */
    @Test
    public void testGetPivotRow() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1, 2}, new double[]{1, 1});
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{1}, 2));
        constraints.addConstraint(new LinearConstraint(new double[]{-1, 1}, 1));
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, new NonNegativeConstraint[0], 1e-6, 10);
        solver.solvePhase1(tableau);
        Integer pivotRow = solver.getPivotRow(tableau, solver.getPivotColumn(tableau).intValue());
        if (pivotRow != null) {
            assertTrue(pivotRow.intValue() >= 0 && pivotRow.intValue() < tableau.getHeight());
        }
    }

    /**
     * Test method for {@link SimplexSolver#doIteration(SimplexTableau)}.
     */
    @Test
    public void testDoIteration() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{1, -1}, new double[]{2, 1});
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{1, -1}, 1));
        constraints.addConstraint(new LinearConstraint(new double[]{1, 1}, 1));
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, new NonNegativeConstraint[0], 1e-6, 10);
        solver.solvePhase1(tableau);
        Integer pivotColumn = solver.getPivotColumn(tableau);
        Integer pivotRow = solver.getPivotRow(tableau, pivotColumn.intValue());
        solver.doIteration(tableau);
        double entry = tableau.getEntry(pivotRow, pivotColumn);
        assertTrue(entry > 0);
    }

    /**
     * Test method for {@link SimplexSolver#solvePhase1(SimplexTableau)}.
     */
    @Test
    public void testSolvePhase1() {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{1, -1}, new double[]{2, 1});
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{1, -1}, 1));
        constraints.addConstraint(new LinearConstraint(new double[]{1, 1}, 1));
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, new NonNegativeConstraint[0], 1e-6, 10);
        solver.solvePhase1(tableau);

        // Check if the tableau is optimal
        for (int i = 0; i < tableau.getHeight(); i++) {
            if (i != tableau.getNumObjectiveFunctions()) {
                double rhs = tableau.getEntry(i, tableau.getWidth() - 1);
                for (int j = 0; j < tableau.getWidth(); j++) {
                    if (j != tableau.getNumObjectiveFunctions()) {
                        double entry = tableau.getEntry(i, j);
                        assertTrue(entry <= 0);
                    }
                }
                // The RHS should be zero for an optimal tableau
                assertEquals(0, rhs, 1e-6);
            }
        }
    }

    /**
     * Test method for {@link SimplexSolver#doOptimize()}.
     */
    @Test
    public void testDoOptimize() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1, 2}, new double[]{1, 1});
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{1}, 2));
        constraints.addConstraint(new LinearConstraint(new double[]{-1, 1}, 1));
        SimplexSolver solver = new SimplexSolver();
        PointValuePair result = solver.doOptimize();
        assertEquals(-1, result.getPoint()[0], 1e-6);
        assertEquals(-3, result.getValue(), 1e-6);
    }

    /**
     * Test method for {@link SimplexSolver#doOptimize()}.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testDoOptimizeMaxIterations() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1, 2}, new double[]{1, 1});
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{1}, 2));
        constraints.addConstraint(new LinearConstraint(new double[]{-1, 1}, 1));
        SimplexSolver solver = new SimplexSolver();
        solver.setMaxIterations(1);
        solver.doOptimize();
    }

    /**
     * Test method for {@link SimplexSolver#doOptimize()}.
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testDoOptimizeUnboundedSolution() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1, 2}, new double[]{1, 1});
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{1}, 0));
        LinearObjectiveFunction g = new LinearObjectiveFunction(new double[]{1, -1}, new double[]{1, 1});
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, new NonNegativeConstraint[0], 1e-6, 10);
        solver.solvePhase1(tableau);
        solver.doOptimize();
    }

    /**
     * Test method for {@link SimplexSolver#doOptimize()}.
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testDoOptimizeNoFeasibleSolution() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-1, 2}, new double[]{1, 1});
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{1, -1}, 0));
        LinearObjectiveFunction g = new LinearObjectiveFunction(new double[]{1, -1}, new double[]{1, 1});
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(f, constraints, GoalType.MINIMIZE, new NonNegativeConstraint[0], 1e-6, 10);
        solver.solvePhase1(tableau);
        solver.doOptimize();
    }

    /**
     * Test method for {@link SimplexSolver#doOptimize()}.
     */
    @Test
    public void testDoOptimizeCustomConstraintSet() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{-3, 1}, new double[]{1, 1});
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{1, -1}, 1));
        constraints.addConstraint(new LinearConstraint(new double[]{1, 1}, 2));
        constraints.addConstraint(new LinearConstraint(new double[]{1, 1}, 3));
        SimplexSolver solver = new SimplexSolver();
        PointValuePair result = solver.doOptimize();
        assertEquals(-3, result.getPoint()[0], 1e-6);
        assertEquals(-2, result.getValue(), 1e-6);
    }

    /**
     * Test method for {@link SimplexSolver#doOptimize()}.
     */
    @Test
    public void testDoOptimizeNonNegativeConstraints() throws Exception {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{1, -1}, new double[]{1, 1});
        LinearConstraintSet constraints = new LinearConstraintSet();
        constraints.addConstraint(new LinearConstraint(new double[]{1, -1}, 1));
        constraints.addConstraint(new LinearConstraint(new double[]{1, 1}, 1));
        LinearConstraintSet nonNegativeConstraints = new LinearConstraintSet();
        nonNegativeConstraints.addConstraint(new NonNegativeConstraint(new int[]{1, 2}));
        SimplexSolver solver = new SimplexSolver();
        PointValuePair result = solver.doOptimize(f, constraints, GoalType.MINIMIZE, nonNegativeConstraints, 1e-6, 10);
        assertEquals(-1, result.getPoint()[0], 1e-6);
        assertEquals(-3, result.getValue(), 1e-6);
    }
}