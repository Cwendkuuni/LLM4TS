package org.apache.commons.math.optimization.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testDoOptimize_SimpleExample() {
        double[][] f = {{-1.0, 2.0}, {-3.0, 1.0}};
        SimplexConstraint[] constraints = new SimplexConstraint[]{
                new LinearConstraint(2.0, 1.0, 3.0, 1), // x1 + 2x2 <= 3
                new LinearConstraint(1.0, 1.0, 1.0, 0) // x1 >= 0
        };
        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair solution = solver.doOptimize();
        assertArrayEquals(new double[]{0.0, 1.5}, solution.getPoint(), 0.001);
        assertEquals(2.25, solution.getValue(), 0.001);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testDoOptimize_MaxIterationsExceeded() {
        double[][] f = {{-1.0, 2.0}, {-3.0, 1.0}};
        SimplexConstraint[] constraints = new SimplexConstraint[]{
                new LinearConstraint(2.0, 1.0, 3.0, 1), // x1 + 2x2 <= 3
                new LinearConstraint(1.0, 1.0, 1.0, 0) // x1 >= 0
        };
        SimplexSolver solver = new SimplexSolver(0);
        solver.doOptimize();
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testDoOptimize_NoFeasibleSolution() {
        double[][] f = {{-1.0, 2.0}, {-3.0, 1.0}};
        SimplexConstraint[] constraints = new SimplexConstraint[]{
                new LinearConstraint(1.0, 1.0, 1.0, 1), // x1 + x2 > 1
                new LinearConstraint(1.0, 1.0, 1.0, 0) // x1 >= 0
        };
        SimplexSolver solver = new SimplexSolver();
        solver.doOptimize();
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoOptimize_UnboundedSolution() {
        double[][] f = {{-1.0, 2.0}, {-3.0, 1.0}};
        SimplexConstraint[] constraints = new SimplexConstraint[]{
                new LinearConstraint(0.0, 1.0, Double.POSITIVE_INFINITY, 1), // x1 >= 0
                new LinearConstraint(1.0, 1.0, 1.0, 0) // x2 >= 0
        };
        SimplexSolver solver = new SimplexSolver();
        solver.doOptimize();
    }

    @Test
    public void testGetPivotColumn() {
        double[][] f = {{-1.0, 2.0}, {-3.0, 1.0}};
        SimplexTableau tableau = new SimplexTableau(f, null, null, null, 0.001);
        Integer pivotCol = new SimplexSolver().getPivotColumn(tableau);
        assertEquals(1, pivotCol.intValue());
    }

    @Test
    public void testGetPivotRow() {
        double[][] f = {{-1.0, 2.0}, {-3.0, 1.0}};
        SimplexTableau tableau = new SimplexTableau(f, null, null, null, 0.001);
        Integer pivotRow = new SimplexSolver().getPivotRow(1, tableau);
        assertEquals(0, pivotRow.intValue());
    }

    @Test
    public void testIsPhase1Solved() {
        double[][] f = {{-1.0, 2.0}, {-3.0, 1.0}};
        SimplexTableau tableau = new SimplexTableau(f, null, null, null, 0.001);
        assertTrue(new SimplexSolver().isPhase1Solved(tableau));

        tableau.setEntry(0, 1, 1.1);
        assertFalse(new SimplexSolver().isPhase1Solved(tableau));
    }

    @Test
    public void testIsOptimal() {
        double[][] f = {{-1.0, 2.0}, {-3.0, 1.0}};
        SimplexTableau tableau = new SimplexTableau(f, null, null, null, 0.001);
        tableau.discardArtificialVariables();
        assertTrue(new SimplexSolver().isOptimal(tableau));

        tableau.setEntry(0, 1, 1.1);
        assertFalse(new SimplexSolver().isOptimal(tableau));
    }

    @Test
    public void testSolvePhase1() {
        double[][] f = {{-1.0, 2.0}, {-3.0, 1.0}};
        SimplexTableau tableau = new SimplexTableau(f, null, null, null, 0.001);
        new SimplexSolver().solvePhase1(tableau);

        // check that all artificial variables have zero values
        for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getWidth() - 1; i++) {
            assertEquals(0, tableau.getEntry(0, i), 0.001);
        }

        // check that the tableau is feasible
        for (int i = 0; i < tableau.getHeight(); i++) {
            for (int j = 0; j < tableau.getWidth(); j++) {
                if (j < tableau.getNumObjectiveFunctions()) {
                    assertEquals(MathUtils.add(MathUtils.mul(tableau.getEntry(i, j), tableau.getCoefficients()[j]), tableau.getRhs()[i]), tableau.getEntry(i, j), 0.001);
                } else {
                    assertTrue(MathUtils.compareTo(tableau.getEntry(i, j), 0, 0.001) >= 0);
                }
            }
        }
    }
}