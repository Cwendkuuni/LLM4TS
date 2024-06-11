package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.SimpleBounds;
import org.apache.commons.math3.optimization.linear.LinearConstraint;
import org.apache.commons.math3.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.linear.LinearPointValuePair;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testSolveSimpleExample() {
        // Test a simple example with a single objective function and a single constraint
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}}, 3.0);
        LinearConstraint constraint = new LinearConstraint(new double[][]{{-1.0, 1.0}}, Relationship.LEQ, 0.0);
        SimplexSolver solver = new SimplexSolver();

        PointValuePair solution = solver.doOptimize();
        assertEquals(LinearPointValuePair.class, solution.getClass());
        assertEquals(3.0, solution.getPoint()[0], 1.0e-6);
        assertEquals(0.0, solution.getPoint()[1], 1.0e-6);
        assertEquals(3.0, solution.getValue(), 1.0e-6);
    }

    @Test
    public void testSolveMultipleObjectives() {
        // Test a multiple objective example with two objective functions and a single constraint
        LinearObjectiveFunction objFunction1 = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new double[]{-2.0, 5.0});
        LinearObjectiveFunction objFunction2 = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}, {3.0, 4.0}}, new double[]{5.0, -2.0});
        LinearConstraint constraint = new LinearConstraint(new double[][]{{-1.0, 1.0}}, Relationship.LEQ, 0.0);
        SimplexSolver solver = new SimplexSolver();

        PointValuePair solution = solver.doOptimize();
        assertEquals(LinearPointValuePair.class, solution.getClass());
        assertEquals(0.0, solution.getPoint()[0], 1.0e-6);
        assertEquals(1.0, solution.getPoint()[1], 1.0e-6);
        assertEquals(-2.0, solution.getValue()[0], 1.0e-6);
        assertEquals(5.0, solution.getValue()[1], 1.0e-6);
    }

    @Test
    public void testSolveWithBounds() {
        // Test a problem with bounds
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}}, 3.0);
        LinearConstraint constraint1 = new LinearConstraint(new double[][]{{-1.0, 1.0}}, Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[][]{{0.0, 1.0}}, Relationship.GEQ, 1.0);
        SimplexSolver solver = new SimplexSolver();

        PointValuePair solution = solver.doOptimize();
        assertEquals(LinearPointValuePair.class, solution.getClass());
        assertEquals(1.0, solution.getPoint()[0], 1.0e-6);
        assertEquals(1.0, solution.getPoint()[1], 1.0e-6);
        assertEquals(2.0, solution.getValue(), 1.0e-6);
    }

    @Test
    public void testUnboundedSolution() {
        // Test an unbounded solution
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}}, Double.POSITIVE_INFINITY);
        SimplexSolver solver = new SimplexSolver();

        assertThrows(UnboundedSolutionException.class, () -> solver.doOptimize());
    }

    @Test
    public void testMaxIterationsExceeded() {
        // Test the case where the maximum iteration count is exceeded
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}}, 3.0);
        LinearConstraint constraint = new LinearConstraint(new double[][]{{-1.0, 1.0}}, Relationship.LEQ, 0.0);
        SimplexSolver solver = new SimplexSolver(1, 1);

        assertThrows(MaxCountExceededException.class, () -> solver.doOptimize());
    }

    @Test
    public void testNoFeasibleSolution() {
        // Test the case where there is no feasible solution
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}}, 3.0);
        LinearConstraint constraint1 = new LinearConstraint(new double[][]{{-1.0, 1.0}}, Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[][]{{0.0, 1.0}}, Relationship.LEQ, 2.0);
        SimplexSolver solver = new SimplexSolver();

        assertThrows(NoFeasibleSolutionException.class, () -> solver.doOptimize());
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[][]{{1.0, 2.0}}, 3.0),
                new LinearConstraint[]{
                        new LinearConstraint(new double[][]{{-1.0, 1.0}}, Relationship.LEQ, 0.0),
                        new LinearConstraint(new double[][]{{0.0, 1.0}}, Relationship.LEQ, 2.0)
                },
                null, 1.0e-6, 10, new SimplexSolver());

        Integer pivotCol = SimplexSolver.this.getPivotColumn(tableau);
        assertEquals(0, pivotCol.intValue());
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[][]{{1.0, 2.0}}, 3.0),
                new LinearConstraint[]{
                        new LinearConstraint(new double[][]{{-1.0, 1.0}}, Relationship.LEQ, 0.0),
                        new LinearConstraint(new double[][]{{0.0, 1.0}}, Relationship.LEQ, 2.0)
                },
                null, 1.0e-6, 10, new SimplexSolver());

        Integer pivotRow = SimplexSolver.this.getPivotRow(tableau, 0);
        assertEquals(1, pivotRow.intValue());
    }

    @Test
    public void testSolvePhase1() {
        LinearObjectiveFunction objFunction = new LinearObjectiveFunction(new double[][]{{1.0, 2.0}}, 3.0);
        LinearConstraint constraint1 = new LinearConstraint(new double[][]{{-1.0, 1.0}}, Relationship.LEQ, 0.0);
        LinearConstraint constraint2 = new LinearConstraint(new double[][]{{0.0, 1.0}}, Relationship.LEQ, 2.0);
        SimplexSolver solver = new SimplexSolver();

        SimplexTableau tableau = new SimplexTableau(objFunction, new LinearConstraint[]{constraint1, constraint2}, null, 1.0e-6, 10, solver);

        solver.solvePhase1(tableau);

        assertEquals(1, tableau.getNumArtificialVariables());
        assertTrue(tableau.isOptimal());
        assertEquals(3.0, tableau.getEntry(0, 0), 1.0e-6);
        assertEquals(0.0, tableau.getEntry(0, 1), 1.0e-6);
        assertEquals(3.0, tableau.getEntry(0, 2), 1.0e-6);
        assertEquals(0.0, tableau.getEntry(1, 0), 1.0e-6);
        assertEquals(-0.5, tableau.getEntry(1, 1), 1.0e-6);
        assertEquals(-0.5, tableau.getEntry(1, 2), 1.0e-6);
    }
}