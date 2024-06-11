package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testDoOptimize() throws Exception {
        SimplexSolver solver = new SimplexSolver();
        PointValuePair solution = solver.doOptimize();
        assertEquals("Expected optimal solution not found", true, solution.isOptimal());
        assertNull("Expected null rhs vector", solution.getRhs());
        assertArrayEquals("Expected empty constraint matrix", new double[0][], solution.getConstraints());
        assertArrayEquals("Expected empty objective function", new double[0], solution.getObjectiveFunction());
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testSolveUnbounded() throws Exception {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(new ObjectiveFunction() {
            @Override
            public double value(double[] parameters) {
                return Double.POSITIVE_INFINITY;
            }
        }, null, null, null, 0, 0);
        solver.solvePhase1(tableau);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testSolveMaxIterations() throws Exception {
        SimplexSolver solver = new SimplexSolver(1, 1);
        SimplexTableau tableau = new SimplexTableau(new ObjectiveFunction() {
            @Override
            public double value(double[] parameters) {
                return 0;
            }
        }, null, null, null, 0, 1);
        solver.solvePhase1(tableau);
        for (int i = 0; i < 1000; i++) {
            solver.doIteration(tableau);
        }
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolveNoFeasibleSolution() throws Exception {
        SimplexSolver solver = new SimplexSolver(1e-10, 1);
        SimplexTableau tableau = new SimplexTableau(new ObjectiveFunction() {
            @Override
            public double value(double[] parameters) {
                return -1;
            }
        }, new Constraint[] {
                new LinearConstraint(1, 1, 0),
                new LinearConstraint(1, -1, 1),
                new LinearConstraint(-1, 1, 1)
        }, null, null, 0, 0);
        solver.solvePhase1(tableau);
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new ObjectiveFunction() {
            @Override
            public double value(double[] parameters) {
                return -1;
            }
        }, null, null, null, 0, 3);
        Integer pivotCol = SimplexSolver.getInstance().getPivotColumn(tableau);
        assertTrue("Expected negative pivot column index", pivotCol < 0);
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(new ObjectiveFunction() {
            @Override
            public double value(double[] parameters) {
                return -1;
            }
        }, null, null, null, 0, 3);
        Integer pivotRow = SimplexSolver.getInstance().getPivotRow(tableau, 0);
        assertNull("Expected null pivot row", pivotRow);
    }

    @Test
    public void testDoIteration() throws Exception {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(new ObjectiveFunction() {
            @Override
            public double value(double[] parameters) {
                return -1;
            }
        }, null, null, null, 0, 3);
        Integer pivotCol = SimplexSolver.getInstance().getPivotColumn(tableau);
        Integer pivotRow = SimplexSolver.getInstance().getPivotRow(tableau, pivotCol.intValue());
        solver.doIteration(tableau);
        assertFalse("Expected non-optimal tableau after one iteration", tableau.isOptimal());
        assertEquals("Expected non-zero rhs after one iteration", -1, tableau.getEntry(pivotRow, tableau.getWidth() - 1), 1e-5);
    }

    @Test
    public void testSolvePhase1() throws Exception {
        SimplexSolver solver = new SimplexSolver();
        SimplexTableau tableau = new SimplexTableau(new ObjectiveFunction() {
            @Override
            public double value(double[] parameters) {
                return -1;
            }
        }, new Constraint[] {
            new LinearConstraint(1, 1, 0),
            new LinearConstraint(1, -1, 1),
            new LinearConstraint(-1, 1, 1)
        }, null, null, 0, 3);
        solver.solvePhase1(tableau);
        assertTrue("Expected optimal tableau after Phase 1", tableau.isOptimal());
        assertEquals("Expected zero rhs after Phase 1", 0, tableau.getEntry(0, tableau.getRhsOffset()), 1e-5);
    }

}