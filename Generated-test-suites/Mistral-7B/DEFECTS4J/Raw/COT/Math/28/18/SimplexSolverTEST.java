package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.junit.Test;

/**
 * Test class for SimplexSolver.
 */
public class SimplexSolverTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        SimplexSolver solver = new SimplexSolver();
        assertNotNull(solver);

        SimplexSolver solverWithArgs = new SimplexSolver(0.001d, 10);
        assertNotNull(solverWithArgs);
    }

    /**
     * Test getPivotColumn method.
     */
    @Test
    public void testGetPivotColumn() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[][]{{1.0, -2.0}, {-1.0, 3.0}}, 0);
        LinearConstraint[] constraints = new LinearConstraint[] {new LinearConstraint(1.0), new LinearConstraint(1.0, 1.0, 3.0)};

        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(obj, constraints, GoalType.MINIMIZE, null, 0.001d, 10);
        tableau.initTableau();

        int expected = 1;
        int actual = solver.getPivotColumn(tableau).intValue();
        assertEquals(expected, actual);
    }

    /**
     * Test getPivotRow method.
     */
    @Test
    public void testGetPivotRow() {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[][]{{1.0, -2.0}, {-1.0, 3.0}}, 0);
        LinearConstraint[] constraints = new LinearConstraint[] {new LinearConstraint(1.0), new LinearConstraint(1.0, 1.0, 3.0)};

        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(obj, constraints, GoalType.MINIMIZE, null, 0.001d, 10);
        tableau.initTableau();

        int expected = 0;
        int actual = solver.getPivotRow(tableau, 0).intValue();
        assertEquals(expected, actual);
    }

    /**
     * Test doIteration method.
     */
    @Test(expected = UnboundedSolutionException.class)
    public void testDoIteration_UnboundedSolutionException() throws Exception {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[][]{{1.0, -2.0}, {-1.0, 3.0}}, 0);
        LinearConstraint[] constraints = new LinearConstraint[] {new LinearConstraint(1.0), new LinearConstraint(1.0, 1.0, 3.0)};

        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(obj, constraints, GoalType.MINIMIZE, null, 0.001d, 10);
        tableau.initTableau();
        tableau.setWidth(tableau.getWidth() + 1);

        solver.doIteration(tableau);
    }

    /**
     * Test solvePhase1 method.
     */
    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1_NoFeasibleSolutionException() throws Exception {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[][]{{1.0, -2.0}, {-1.0, 3.0}}, 0);
        LinearConstraint[] constraints = new LinearConstraint[] {new LinearConstraint(1.0), new LinearConstraint(1.0, 1.0, 3.0)};

        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(obj, constraints, GoalType.MINIMIZE, null, 0.001d, 10);
        tableau.initTableau();
        tableau.setEntry(0, tableau.getWidth() - 1, 5.0);

        solver.solvePhase1(tableau);
    }

    /**
     * Test doOptimize method.
     */
    @Test
    public void testDoOptimize() throws Exception {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[][]{{1.0, -2.0}, {-1.0, 3.0}}, 0);
        LinearConstraint[] constraints = new LinearConstraint[] {new LinearConstraint(1.0), new LinearConstraint(1.0, 1.0, 3.0)};

        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(obj, constraints, GoalType.MINIMIZE, null, 0.001d, 10);

        PointValuePair expected = new PointValuePair(new double[]{1.0, 3.0}, -5.0);
        PointValuePair actual = solver.doOptimize();

        assertEquals(expected.getPoint().length, actual.getPoint().length);
        assertArrayEquals(expected.getPoint(), actual.getPoint(), 0.0001);
        assertEquals(expected.getValue(), actual.getValue(), 0.001);
    }

    /**
     * Test doOptimize with max count exceeded exception.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testDoOptimize_MaxCountExceededException() throws Exception {
        LinearObjectiveFunction obj = new LinearObjectiveFunction(new double[][]{{1.0, -2.0}, {-1.0, 3.0}}, 0);
        LinearConstraint[] constraints = new LinearConstraint[] {new LinearConstraint(1.0), new LinearConstraint(1.0, 1.0, 3.0)};

        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(obj, constraints, GoalType.MINIMIZE, null, 0.001d, 1);

        solver.doIteration(tableau);
        solver.doIteration(tableau);
        solver.doIteration(tableau);
    }

}