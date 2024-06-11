package org.apache.commons.math.optimization.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.exception.NoFeasibleSolutionException;
import org.apache.commons.math3.exception.UnboundedSolutionException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

public class SimplexSolverTest {

    @Test
    public void testConstructorWithDefaultEpsilon() {
        SimplexSolver solver = new SimplexSolver();
        assertEquals(1.0e-6, solver.epsilon, 0.0);
    }

    @Test
    public void testConstructorWithCustomEpsilon() {
        double epsilon = 1.0e-8;
        SimplexSolver solver = new SimplexSolver(epsilon);
        assertEquals(epsilon, solver.epsilon, 0.0);
    }

    @Test
    public void testGetPivotColumn_AllCoefsNegative() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, -2.0}, new double[]{0.0}),
                new LinearConstraint[]{}, LinearObjectiveFunction.Type.MINIMIZE, true, 1.0e-6);
        Integer expected = 0;
        Integer actual = new SimplexSolver().getPivotColumn(tableau);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPivotColumn_CoefsWithMinValue() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0}),
                new LinearConstraint[]{}, LinearObjectiveFunction.Type.MINIMIZE, true, 1.0e-6);
        Integer expected = 1;
        Integer actual = new SimplexSolver().getPivotColumn(tableau);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPivotRow_MinRatio() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0}),
                new LinearConstraint[]{new LinearConstraint(1.0, 0.0, Double.POSITIVE_INFINITY),
                        new LinearConstraint(0.5, 1.0, 3.0),
                        new LinearConstraint(-1.0, 1.0, Double.NEGATIVE_INFINITY)},
                LinearObjectiveFunction.Type.MINIMIZE, true, 1.0e-6);
        Integer expected = 1;
        Integer actual = new SimplexSolver().getPivotRow(0, tableau);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPivotRow_MinRatio_SameRatio() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0}),
                new LinearConstraint[]{new LinearConstraint(1.0, 0.0, Double.POSITIVE_INFINITY),
                        new LinearConstraint(0.5, 1.0, 3.0),
                        new LinearConstraint(0.5, 1.0, 3.0)},
                LinearObjectiveFunction.Type.MINIMIZE, true, 1.0e-6);
        Integer expected = null;
        Integer actual = new SimplexSolver().getPivotRow(0, tableau);
        assertEquals(expected, actual);
    }

    @Test
    public void testDoIteration_PivotElementsSetCorrectly() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0}),
                new LinearConstraint[]{new LinearConstraint(1.0, 0.0, Double.POSITIVE_INFINITY),
                        new LinearConstraint(0.5, 1.0, 3.0),
                        new LinearConstraint(-1.0, 1.0, Double.NEGATIVE_INFINITY)},
                LinearObjectiveFunction.Type.MINIMIZE, true, 1.0e-6);
        Integer pivotCol = new SimplexSolver().getPivotColumn(tableau);
        Integer pivotRow = new SimplexSolver().getPivotRow(pivotCol, tableau);
        new SimplexSolver().doIteration(tableau);

        double pivotVal = tableau.getEntry(pivotRow, pivotCol);
        double dividedRow = tableau.getRow(pivotRow);
        for (int i = 0; i < tableau.getHeight(); i++) {
            if (i != pivotRow) {
                double multiplier = tableau.getEntry(i, pivotCol);
                double subtractedRow = tableau.getRow(i);
                assertEquals("Row " + i + " should be subtracted from row " + pivotRow + " by multiplier " + multiplier,
                        subtractedRow, MathUtils.subtract(dividedRow, multiplier * dividedRow), 0.0);
            }
        }
        assertArrayEquals("Pivot element should be set to 1", new double[]{1.0}, tableau.getRow(pivotRow), 0.01);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoIteration_UnboundedSolutionException() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{0.0}, new double[]{0.0}),
                new LinearConstraint[]{new LinearConstraint(Double.POSITIVE_INFINITY, 0, Double.POSITIVE_INFINITY)},
                LinearObjectiveFunction.Type.MINIMIZE, true, 1.0e-6);
        new SimplexSolver().doIteration(tableau);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1_NoFeasibleSolutionException() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0}),
                new LinearConstraint[]{new LinearConstraint(1.0, 0.0, Double.POSITIVE_INFINITY),
                        new LinearConstraint(0.5, 1.0, 3.0),
                        new LinearConstraint(-1.0, 1.0, Double.NEGATIVE_INFINITY)},
                LinearObjectiveFunction.Type.MINIMIZE, true, 1.0e-6);
        new SimplexSolver().solvePhase1(tableau);
        assertTrue("W should be zero", MathUtils.equals(tableau.getEntry(0, tableau.getRhsOffset()), 0, 1.0e-6));

        LinearConstraint constraint = tableau.getConstraints()[2];
        constraint.setConstant(Double.NaN);

        try {
            new SimplexSolver().solvePhase1(tableau);
            fail("Should throw NoFeasibleSolutionException");
        } catch (NoFeasibleSolutionException e) {
            // expected
        }
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolvePhase1_MaxIterationsExceededException() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0}),
                new LinearConstraint[]{new LinearConstraint(1.0, 0.0, Double.POSITIVE_INFINITY),
                        new LinearConstraint(0.5, 1.0, 3.0),
                        new LinearConstraint(-1.0, 1.0, Double.NEGATIVE_INFINITY)},
                LinearObjectiveFunction.Type.MINIMIZE, true, 1.0e-6);

        for (int i = 0; i < 100; i++) {
            new SimplexSolver().solvePhase1(tableau);
        }

        try {
            new SimplexSolver().solvePhase1(tableau);
            fail("Should throw MaxIterationsExceededException");
        } catch (MaxIterationsExceededException e) {
            // expected
        }
    }

    @Test
    public void testIsOptimal_OptimalSolution() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0}),
                new LinearConstraint[]{new LinearConstraint(1.0, 0.0, Double.POSITIVE_INFINITY),
                        new LinearConstraint(0.5, 1.0, 3.0),
                        new LinearConstraint(-1.0, 1.0, Double.NEGATIVE_INFINITY)},
                LinearObjectiveFunction.Type.MINIMIZE, true, 1.0e-6);
        new SimplexSolver().solvePhase1(tableau);
        new SimplexSolver().discardArtificialVariables(tableau);
        assertTrue("Solution should be optimal", new SimplexSolver().isOptimal(tableau));
    }

    @Test
    public void testIsOptimal_NonOptimalSolution() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0}),
                new LinearConstraint[]{new LinearConstraint(1.0, 0.0, Double.POSITIVE_INFINITY),
                        new LinearConstraint(0.5, 1.0, 3.0),
                        new LinearConstraint(-1.0, 1.0, Double.NEGATIVE_INFINITY)},
                LinearObjectiveFunction.Type.MINIMIZE, true, 1.0e-6);
        new SimplexSolver().solvePhase1(tableau);
        new SimplexSolver().discardArtificialVariables(tableau);
        LinearConstraint constraint = tableau.getConstraints()[1];
        constraint.setConstant(constraint.getConstant() + 1.0);
        assertFalse("Solution should not be optimal", new SimplexSolver().isOptimal(tableau));
    }

    @Test
    public void testDoOptimize_SolveLinearProblem() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1.0, 2.0}, new double[]{0.0}),
                new LinearConstraint[]{new LinearConstraint(1.0, 0.0, Double.POSITIVE_INFINITY),
                        new LinearConstraint(0.5, 1.0, 3.0),
                        new LinearConstraint(-1.0, 1.0, Double.NEGATIVE_INFINITY)},
                LinearObjectiveFunction.Type.MINIMIZE, true, 1.0e-6);
        RealPointValuePair solution = new SimplexSolver().doOptimize();
        double[] expectedX = {2.0 / 3.0, 1.0};
        assertArrayEquals("Solution x should be " + Arrays.toString(expectedX), expectedX, solution.getPoint(), 0.01);
        double expectedF = 1.0 / 3.0;
        assertEquals("Solution f should be " + expectedF, expectedF, solution.getValue(), 0.01);
    }

}