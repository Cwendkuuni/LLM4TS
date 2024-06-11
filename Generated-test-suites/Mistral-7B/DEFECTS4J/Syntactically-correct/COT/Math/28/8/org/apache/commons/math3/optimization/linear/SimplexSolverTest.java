package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleConstraints;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.apache.commons.math3.util.Precision;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for {@link SimplexSolver}.
 */
public class SimplexSolverTest {

    /**
     * Test method for {@link SimplexSolver#SimplexSolver()}.
     */
    @org.junit.Test
    public void testConstructor() {
        final SimplexSolver solver = new SimplexSolver();
        assertEquals(1.0e-6, solver.epsilon, 0);
        assertEquals(10, solver.maxUlps);
    }

    /**
     * Test method for {@link SimplexSolver#SimplexSolver(double, int)}.
     */
    @org.junit.Test
    public void testConstructorWithArguments() {
        final double epsilon = 1.0e-10;
        final int maxUlps = 5;
        final SimplexSolver solver = new SimplexSolver(epsilon, maxUlps);
        assertEquals(epsilon, solver.epsilon, 0);
        assertEquals(maxUlps, solver.maxUlps);
    }

    /**
     * Test method for {@link SimplexSolver#getPivotColumn(SimplexTableau)}.
     */
    @org.junit.Test
    public void testGetPivotColumn() {
        final SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 3, 2}),
                                                          new SimpleConstraints(new SimpleConstraints.Constraint[]{}),
                                                          GoalType.MINIMIZE, true, 1.0e-6, 10);

        final Integer pivotColumn = SimplexSolver.getInstance().getPivotColumn(tableau);
        assertEquals(1, pivotColumn.intValue());
    }

    /**
     * Test method for {@link SimplexSolver#getPivotRow(SimplexTableau, int)}.
     */
    @org.junit.Test
    public void testGetPivotRow() {
        final SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 3, 2}),
                                                          new SimpleConstraints(new SimpleConstraints.Constraint[]{}),
                                                          GoalType.MINIMIZE, true, 1.0e-6, 10);

        final Integer pivotRow = SimplexSolver.getInstance().getPivotRow(tableau, 1);
        assertEquals(1, pivotRow.intValue());
    }

    /**
     * Test method for {@link SimplexSolver#doIteration(SimplexTableau)}.
     */
    @org.junit.Test
    public void testDoIteration() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        final SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 3, 2}),
                                                          new SimpleConstraints(new SimpleConstraints.Constraint[]{}),
                                                          GoalType.MINIMIZE, true, 1.0e-6, 10);
        SimplexSolver solver = new SimplexSolver();

        final Integer pivotCol = solver.getPivotColumn(tableau);
        final Integer pivotRow = solver.getPivotRow(tableau, pivotCol.intValue());
        solver.doIteration(tableau);

        final double entryZeroOne = tableau.getEntry(0, 1);
        assertEquals(1.0, entryZeroOne, 0);

        final double entryOneZero = tableau.getEntry(1, 0);
        assertEquals(-entryZeroOne / (tableau.getHeight() - 1), entryOneZero, 0);

        final double entryOneOne = tableau.getEntry(1, 1);
        assertEquals(1.0, entryOneOne, 0);
    }

    /**
     * Test method for {@link SimplexSolver#solvePhase1(SimplexTableau)}.
     */
    @org.junit.Test
    public void testSolvePhase1() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        final LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{-1, 3, 2});
        final SimplexTableau tableau = new SimplexTableau(objectiveFunction,
                                                          new SimpleConstraints(new SimpleConstraints.Constraint[]{}),
                                                          GoalType.MINIMIZE, true, 1.0e-6, 10);

        SimplexSolver solver = new SimplexSolver();
        solver.solvePhase1(tableau);

        final double entryZeroOne = tableau.getEntry(0, 1);
        assertEquals(1.0, entryZeroOne, 0);

        final double entryOneZero = tableau.getEntry(1, 0);
        assertEquals(-entryZeroOne / (tableau.getHeight() - 1), entryOneZero, 0);

        final double entryOneOne = tableau.getEntry(1, 1);
        assertEquals(1.0, entryOneOne, 0);

        assertArrayEquals(new double[]{0.0, 0.0, 1.0}, tableau.getSolution(), 1e-6);
    }

    /**
     * Test method for {@link SimplexSolver#doOptimize()}.
     */
    @org.junit.Test
    public void testDoOptimize() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        final LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{-1, 3, 2});
        final SimplexTableau tableau = new SimplexTableau(objectiveFunction,
                                                          new SimpleConstraints(new SimpleConstraints.Constraint[]{}),
                                                          GoalType.MINIMIZE, true, 1.0e-6, 10);

        SimplexSolver solver = new SimplexSolver();
        final PointValuePair solution = solver.doOptimize();

        assertArrayEquals(new double[]{0.0, 0.0, 1.0}, solution.getPoint(), 1e-6);
        assertEquals(-1.0, solution.getValue(), 1e-6);
    }

    /**
     * Test method for {@link SimplexSolver#doOptimize()} with unbounded solution.
     */
    @org.junit.Test(expected = UnboundedSolutionException.class)
    public void testDoOptimizeUnboundedSolution() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        final LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{-1, 3, 2});
        final SimplexTableau tableau = new SimplexTableau(objectiveFunction,
                                                          new SimpleConstraints(new SimpleConstraints.Constraint[]{new SimpleConstraints.Constraint(3, SimpleConstraints.Type.LEQ, 0.0)}),
                                                          GoalType.MINIMIZE, true, 1.0e-6, 10);

        SimplexSolver solver = new SimplexSolver();
        solver.doOptimize();
    }

    /**
     * Test method for {@link SimplexSolver#doOptimize()} with no feasible solution.
     */
    @org.junit.Test(expected = NoFeasibleSolutionException.class)
    public void testDoOptimizeNoFeasibleSolution() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        final LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{-1, 3, 2});
        final SimplexTableau tableau = new SimplexTableau(objectiveFunction,
                                                          new SimpleConstraints(new SimpleConstraints.Constraint[]{new SimpleConstraints.Constraint(0, SimpleConstraints.Type.EQ, 0.0)}),
                                                          GoalType.MINIMIZE, true, 1.0e-6, 10);

        SimplexSolver solver = new SimplexSolver();
        solver.doOptimize();
    }

    /**
     * Test method for {@link SimplexSolver#doOptimize()} with maximum iteration count exceeded.
     */
    @org.junit.Test(expected = MaxCountExceededException.class)
    public void testDoOptimizeMaxIterations() throws MaxCountExceededException, UnboundedSolutionException, NoFeasibleSolutionException {
        final LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{-1, 3, 2});
        final SimplexTableau tableau = new SimplexTableau(objectiveFunction,
                                                          new SimpleConstraints(new SimpleConstraints.Constraint[]{new SimpleConstraints.Constraint(0, SimpleConstraints.Type.EQ, 0.0)}),
                                                          GoalType.MINIMIZE, true, 1.0e-6, 0);

        SimplexSolver solver = new SimplexSolver();
        solver.doOptimize();
    }
}