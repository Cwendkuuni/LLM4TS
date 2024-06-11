import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleTableau;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearOptimizationFactory;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SimplexSolverTest {

    @Test
    public void testSimplexSolverConstructorWithDefaultEpsilon() {
        SimplexSolver simplexSolver = new SimplexSolver();
        assertEquals(1.0e-6, simplexSolver.epsilon, 0.0);
    }

    @Test
    public void testSimplexSolverConstructorWithCustomEpsilon() {
        double customEpsilon = 1.0e-7;
        SimplexSolver simplexSolver = new SimplexSolver(customEpsilon);
        assertEquals(customEpsilon, simplexSolver.epsilon, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimplexSolverNegativeEpsilon() {
        new SimplexSolver(-1.0);
    }

    @Test
    public void testGetPivotColumn() {
        SimpleTableau tableau = new SimpleTableau(new LinearObjectiveFunction(new double[]{-1.0, 1.0}, null),
                new LinearConstraint[]{}, new LinearConstraint[]{new LinearConstraint(1.0, 1.0, 2.0)}, null, 1.0e-6);

        Integer pivotColumn = new SimplexSolver().getPivotColumn(tableau);
        assertEquals(1, pivotColumn.intValue());
    }

    @Test
    public void testGetPivotRowWithNegativeEntry() {
        SimpleTableau tableau = new SimpleTableau(new LinearObjectiveFunction(new double[]{-1.0, 1.0}, null),
                new LinearConstraint[]{}, new LinearConstraint[]{new LinearConstraint(1.0, 1.0, 2.0)}, null, 1.0e-6);

        Integer pivotRow = new SimplexSolver().getPivotRow(0, tableau);
        assertNull(pivotRow);
    }

    @Test
    public void testGetPivotRowWithZeroEntry() {
        SimpleTableau tableau = new SimpleTableau(new LinearObjectiveFunction(new double[]{-1.0, 1.0}, null),
                new LinearConstraint[]{}, new LinearConstraint[]{new LinearConstraint(1.0, 1.0, 2.0)}, null, 1.0e-6);

        Integer pivotRow = new SimplexSolver().getPivotRow(1, tableau);
        assertEquals(1, pivotRow.intValue());
    }

    @Test
    public void testDoIterationThrowsUnboundedSolutionException() {
        SimpleTableau tableau = new SimpleTableau(new LinearObjectiveFunction(new double[]{-1.0, 1.0}, null),
                new LinearConstraint[]{}, new LinearConstraint[]{new LinearConstraint(1.0, 1.0, 2.0)}, null, 1.0e-6);

        SimplexSolver simplexSolver = new SimplexSolver();

        // Set the tableau in an unbounded state
        simplexSolver.doIteration(tableau);
        simplexSolver.doIteration(tableau);
        simplexSolver.doIteration(tableau);

        try {
            simplexSolver.doIteration(tableau);
            fail("Expected UnboundedSolutionException");
        } catch (OptimizationException e) {
            assertTrue(e instanceof UnboundedSolutionException);
        }
    }

    @Test
    public void testDoIteration() {
        SimpleTableau tableau = new SimpleTableau(new LinearObjectiveFunction(new double[]{-1.0, 1.0}, null),
                new LinearConstraint[]{}, new LinearConstraint[]{new LinearConstraint(1.0, 1.0, 2.0)}, null, 1.0e-6);

        SimplexSolver simplexSolver = new SimplexSolver();

        simplexSolver.doIteration(tableau);

        // Check if the pivot element in the pivot row was set to 1
        double pivotVal = tableau.getEntry(0, 0);
        assertEquals(1.0, pivotVal, 1.0e-6);

        // Check if the rest of the pivot column was set to 0
        for (int i = 0; i < tableau.getHeight(); i++) {
            if (i != 0) {
                double entry = tableau.getEntry(i, 0);
                assertEquals(0.0, entry, 1.0e-6);
            }
        }
    }

    @Test
    public void testIsPhase1Solved() {
        SimpleTableau tableau = new SimpleTableau(new LinearObjectiveFunction(new double[]{-1.0, 1.0}, null),
                new LinearConstraint[]{}, new LinearConstraint[]{new LinearConstraint(1.0, 1.0, 2.0)}, null, 1.0e-6);

        SimplexSolver simplexSolver = new SimplexSolver();

        // Phase 1 is not solved
        assertFalse(simplexSolver.isPhase1Solved(tableau));

        // Do an iteration to move to Phase 1
        simplexSolver.doIteration(tableau);

        // Phase 1 is solved
        assertTrue(simplexSolver.isPhase1Solved(tableau));
    }

    @Test
    public void testIsOptimal() {
        SimpleTableau tableau = new SimpleTableau(new LinearObjectiveFunction(new double[]{-1.0, 1.0}, null),
                new LinearConstraint[]{}, new LinearConstraint[]{new LinearConstraint(1.0, 1.0, 2.0)}, null, 1.0e-6);

        SimplexSolver simplexSolver = new SimplexSolver();

        // Do Phase 1
        simplexSolver.doIteration(tableau);

        // Problem is not optimal
        assertFalse(simplexSolver.isOptimal(tableau));

        // Do an iteration to reach optimality
        simplexSolver.doIteration(tableau);

        // Problem is optimal
        assertTrue(simplexSolver.isOptimal(tableau));
    }

    @Test(expected = OptimizationException.class)
    public void testSolvePhase1ThrowsNoFeasibleSolutionException() {
        SimpleTableau tableau = new SimpleTableau(new LinearObjectiveFunction(new double[]{-1.0, 1.0}, null),
                new LinearConstraint[]{}, new LinearConstraint[]{new LinearConstraint(1.0, 1.0, 2.0)}, null, 1.0e-6);

        SimplexSolver simplexSolver = new SimplexSolver();

        // Set the tableau in a non-feasible state
        simplexSolver.solvePhase1(tableau);

        try {
            simplexSolver.solvePhase1(tableau);
            fail("Expected NoFeasibleSolutionException");
        } catch (OptimizationException e) {
            assertTrue(e instanceof NoFeasibleSolutionException);
        }
    }

    @Test
    public void testSolvePhase1() {
        SimpleTableau tableau = new SimpleTableau(new LinearObjectiveFunction(new double[]{-1.0, 1.0}, null),
                new LinearConstraint[]{}, new LinearConstraint[]{new LinearConstraint(1.0, 1.0, 2.0)}, null, 1.0e-6);

        SimplexSolver simplexSolver = new SimplexSolver();

        simplexSolver.solvePhase1(tableau);

        // Check if all artificial variables have been discarded
        for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getWidth() - 1; i++) {
            assertEquals(0.0, tableau.getEntry(0, i), 1.0e-6);
        }
    }

    @Test
    public void testOptimize() {
        LinearObjectiveFunction objectiveFunction = new LinearObjectiveFunction(new double[]{1.0, 1.0}, null);
        LinearConstraint[] constraints = new LinearConstraint[]{
                new LinearConstraint(1.0, 1.0, 2.0),
                new LinearConstraint(2.0, 1.0, 3.0),
                new LinearConstraint(3.0, 0.0, 1.0),
                new LinearConstraint(5.0, 1.0, 4.0),
                new LinearConstraint(3.0, 1.0, 1.0)
        };
        PointValuePair solution = new SimplexSolver().doOptimize();

        assertEquals(5.0, solution.getPoint()[0], 1.0e-6);
        assertEquals(4.0, solution.getValue(), 1.0e-6);
    }
}