package org.apache.commons.math3.optimization.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.LinearConstraint;
import org.apache.commons.math3.optimization.LinearObjectiveFunction;
import org.apache.commons.math3.optimization.LinearConstraintSet;
import org.apache.commons.math3.optimization.MaxCountExceededException;
import org.apache.commons.math3.optimization.NoFeasibleSolutionException;
import org.apache.commons.math3.optimization.UnboundedSolutionException;
import org.apache.commons.math3.optimization.linear.SimplexSolver;
import org.apache.commons.math3.optimization.linear.SimplexTableau;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testDoOptimize() throws Exception {
        // Test with an empty problem
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[]{}, 0.0);
        LinearConstraintSet constraints = new LinearConstraintSet();
        SimplexSolver solver = new SimplexSolver();
        PointValuePair result = solver.doOptimize();
        assertNull(result.getValue());
        assertArrayEquals(new double[]{}, result.getPoint(), 0.0);

        // Test with a simple problem (minimize x, x >= 0)
        f = new LinearObjectiveFunction(new double[]{1.0}, 0.0);
        LinearConstraint c1 = new LinearConstraint(new double[]{1.0}, 0.0, null, null, 'G', null);
        constraints.addConstraint(c1);
        solver = new SimplexSolver();
        result = solver.doOptimize();
        assertEquals(0.0, result.getValue(), solver.epsilon);
        assertArrayEquals(new double[]{0.0}, result.getPoint(), solver.epsilon);

        // Test with a problem with no solution
        f = new LinearObjectiveFunction(new double[]{1.0, 1.0}, 2.0);
        LinearConstraint c1 = new LinearConstraint(new double[]{1.0, -1.0}, 0.0, null, null, 'G', null);
        LinearConstraint c2 = new LinearConstraint(new double[]{1.0, -2.0}, 0.0, null, null, 'G', null);
        constraints.addConstraint(c1);
        constraints.addConstraint(c2);
        solver = new SimplexSolver();
        try {
            result = solver.doOptimize();
            fail("Expected a NoFeasibleSolutionException");
        } catch (NoFeasibleSolutionException e) {
            // Expected
        }

        // Test with a problem with an unbounded solution
        f = new LinearObjectiveFunction(new double[]{1.0, -1.0}, 0.0);
        LinearConstraint c1 = new LinearConstraint(new double[]{1.0, -1.0}, Double.POSITIVE_INFINITY, null, null, 'L', null);
        constraints.addConstraint(c1);
        solver = new SimplexSolver();
        try {
            result = solver.doOptimize();
            fail("Expected an UnboundedSolutionException");
        } catch (UnboundedSolutionException e) {
            // Expected
        }

        // Test with a problem with a maximum number of iterations
        f = new LinearObjectiveFunction(new double[]{1.0, 1.0}, 2.0);
        LinearConstraint c1 = new LinearConstraint(new double[]{1.0, -1.0}, 0.0, null, null, 'G', null);
        LinearConstraint c2 = new LinearConstraint(new double[]{1.0, -2.0}, 0.0, null, null, 'G', null);
        constraints.addConstraint(c1);
        constraints.addConstraint(c2);
        solver = new SimplexSolver(1);
        try {
            result = solver.doOptimize();
            fail("Expected a MaxCountExceededException");
        } catch (MaxCountExceededException e) {
            // Expected
        }
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{1.0}, 0.0),
                                                   new LinearConstraintSet(),
                                                   SimplexSolver.MINIMIZE,
                                                   false,
                                                   1.0e-6,
                                                   10);
        assertEquals(1, getPivotColumn(tableau).intValue());
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{1.0, 1.0}, 2.0),
                                                   new LinearConstraintSet(),
                                                   SimplexSolver.MINIMIZE,
                                                   false,
                                                   1.0e-6,
                                                   10);
        assertEquals(2, getPivotRow(tableau, 0).intValue());
    }

    @Test
    public void testDoIteration() throws Exception {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{1.0}, 0.0),
                                                   new LinearConstraintSet(),
                                                   SimplexSolver.MINIMIZE,
                                                   false,
                                                   1.0e-6,
                                                   10);
        SimplexSolver solver = new SimplexSolver();
        solver.doIteration(tableau);
        assertEquals(1, tableau.getNumBasicVariables());
        assertEquals(1, tableau.getNumNonBasicVariables());
        assertEquals(1, tableau.getHeight());
        assertEquals(2, tableau.getWidth());
        assertEquals(0.0, tableau.getEntry(0, 0), solver.epsilon);
        assertEquals(1.0, tableau.getEntry(0, 1), solver.epsilon);
        assertEquals(0.0, tableau.getEntry(0, 2), solver.epsilon);
        assertEquals(0.0, tableau.getEntry(1, 0), solver.epsilon);
        assertEquals(0.0, tableau.getEntry(1, 1), solver.epsilon);
        assertEquals(1.0, tableau.getEntry(1, 2), solver.epsilon);

        tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{1.0, 1.0}, 2.0),
                                     new LinearConstraintSet(),
                                     SimplexSolver.MINIMIZE,
                                     false,
                                     1.0e-6,
                                     10);
        solver.doIteration(tableau);
        assertEquals(1, tableau.getNumBasicVariables());
        assertEquals(1, tableau.getNumNonBasicVariables());
        assertEquals(1, tableau.getHeight());
        assertEquals(2, tableau.getWidth());
        assertEquals(1.0, tableau.getEntry(0, 0), solver.epsilon);
        assertEquals(0.0, tableau.getEntry(0, 1), solver.epsilon);
        assertEquals(-1.0, tableau.getEntry(0, 2), solver.epsilon);
        assertEquals(0.0, tableau.getEntry(1, 0), solver.epsilon);
        assertEquals(1.0, tableau.getEntry(1, 1), solver.epsilon);
        assertEquals(0.0, tableau.getEntry(1, 2), solver.epsilon);
    }

    @Test
    public void testSolvePhase1() throws Exception {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{1.0, 1.0}, 2.0),
                                                   new LinearConstraintSet(),
                                                   SimplexSolver.MINIMIZE,
                                                   false,
                                                   1.0e-6,
                                                   10);
        SimplexSolver solver = new SimplexSolver();
        solver.solvePhase1(tableau);
        assertEquals(0, tableau.getNumArtificialVariables());
        assertEquals(1, tableau.getNumBasicVariables());
        assertEquals(1, tableau.getNumNonBasicVariables());
        assertEquals(1, tableau.getHeight());
        assertEquals(2, tableau.getWidth());
        assertEquals(0.0, tableau.getEntry(0, 0), solver.epsilon);
        assertEquals(1.0, tableau.getEntry(0, 1), solver.epsilon);
        assertEquals(0.0, tableau.getEntry(0, 2), solver.epsilon);
        assertEquals(0.0, tableau.getEntry(1, 0), solver.epsilon);
        assertEquals(1.0, tableau.getEntry(1, 1), solver.epsilon);
        assertEquals(0.0, tableau.getEntry(1, 2), solver.epsilon);
    }

    private SimplexTableau newSimplexTableau(LinearObjectiveFunction f, LinearConstraintSet constraints,
                                            int goalType, boolean nonNegative, double epsilon, int maxUlps) {
        return new SimplexTableau(f,
                                 constraints,
                                 goalType,
                                 nonNegative,
                                 epsilon,
                                 maxUlps);
    }

    private Integer getPivotColumn(SimplexTableau tableau) {
        return SimplexSolver.this.getPivotColumn(tableau);
    }

    private Integer getPivotRow(SimplexTableau tableau, int col) {
        return SimplexSolver.this.getPivotRow(tableau, col);
    }
}