import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SimplexSolverTest {

    @Test
    public void testDefaultConstructor() {
        SimplexSolver solver = new SimplexSolver();
        assertEquals(1.0e-6, solver.epsilon, 0);
    }

    @Test
    public void testCustomEpsilonConstructor() {
        double customEpsilon = 1.0e-8;
        SimplexSolver solver = new SimplexSolver(customEpsilon);
        assertEquals(customEpsilon, solver.epsilon, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeEpsilon() {
        new SimplexSolver(-1.0);
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 2}),
                Arrays.asList(new LinearConstraint[]{
                        new LinearConstraint(new double[]{-1, 1}, Relationship.LEQUAL, 0),
                        new LinearConstraint(new double[]{-1, 1}, Relationship.LEQUAL, 2)
                }),
                LinearObjectiveFunction.Type.MINIMIZE, true, 1.0e-6);

        Integer pivotColumn = new SimplexSolver().getPivotColumn(tableau);
        assertEquals(0, pivotColumn.intValue());
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 2}),
                Arrays.asList(new LinearConstraint[]{
                        new LinearConstraint(new double[]{-1, 1}, Relationship.LEQUAL, 0),
                        new LinearConstraint(new double[]{-1, 1}, Relationship.LEQUAL, 2)
                }),
                LinearObjectiveFunction.Type.MINIMIZE, true, 1.0e-6);

        Integer pivotRow = new SimplexSolver().getPivotRow(0, tableau);
        assertEquals(1, pivotRow.intValue());
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoIteration_UnboundedSolution() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 0}),
                Arrays.asList(new LinearConstraint[]{
                        new LinearConstraint(new double[]{-1, 1}, Relationship.LEQUAL, 0),
                        new LinearConstraint(new double[]{-1, 1}, Relationship.LEQUAL, 2)
                }),
                LinearObjectiveFunction.Type.MINIMIZE, true, 1.0e-6);
        new SimplexSolver().doIteration(tableau);
    }

    @Test
    public void testIterationsCounter() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 2}),
                Arrays.asList(new LinearConstraint[]{
                        new LinearConstraint(new double[]{-1, 1}, Relationship.LEQUAL, 0),
                        new LinearConstraint(new double[]{-1, 1}, Relationship.LEQUAL, 2)
                }),
                LinearObjectiveFunction.Type.MINIMIZE, true, 1.0e-6);

        int initialIterationsCounter = SimplexSolver.class.getField("iterationsCounter").getInt(null);
        new SimplexSolver().doIteration(tableau);
        int iterationsCounterAfterFirstIteration = SimplexSolver.class.getField("iterationsCounter").getInt(null);

        assertEquals(initialIterationsCounter + 1, iterationsCounterAfterFirstIteration);
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 2}),
                Arrays.asList(new LinearConstraint[]{
                        new LinearConstraint(new double[]{-1, 1}, Relationship.LEQUAL, 0),
                        new LinearConstraint(new double[]{-1, 1}, Relationship.LEQUAL, 2)
                }),
                LinearObjectiveFunction.Type.MINIMIZE, true, 1.0e-6);

        assertFalse(new SimplexSolver().isPhase1Solved(tableau));

        for (int i = 0; i < 10; i++) {
            new SimplexSolver().doIteration(tableau);
        }

        assertTrue(new SimplexSolver().isPhase1Solved(tableau));
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testIsOptimal_NoFeasibleSolution() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 2}),
                Arrays.asList(new LinearConstraint[]{
                        new LinearConstraint(new double[]{-1, 1}, Relationship.LEQUAL, 0),
                        new LinearConstraint(new double[]{-1, 1}, Relationship.LEQUAL, 2)
                }),
                LinearObjectiveFunction.Type.MINIMIZE, true, 1.0e-6);

        new SimplexSolver().solvePhase1(tableau);
        new SimplexSolver().discardArtificialVariables(tableau);

        for (int i = 0; i < 10; i++) {
            new SimplexSolver().doIteration(tableau);
        }

        assertFalse(new SimplexSolver().isOptimal(tableau));

        new SimplexSolver().doIteration(tableau);

        throw new AssertionError("The problem should now have an unbounded solution.");
    }

    @Test
    public void testIsOptimal_OptimalSolution() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 2}),
                Arrays.asList(new LinearConstraint[]{
                        new LinearConstraint(new double[]{-1, 1}, Relationship.LEQUAL, 0),
                        new LinearConstraint(new double[]{-1, 1}, Relationship.LEQUAL, 2)
                }),
                LinearObjectiveFunction.Type.MINIMIZE, true, 1.0e-6);

        new SimplexSolver().solvePhase1(tableau);
        new SimplexSolver().discardArtificialVariables(tableau);

        for (int i = 0; i < 10; i++) {
            new SimplexSolver().doIteration(tableau);
        }

        PointValuePair optimalSolution = new SimplexSolver().doOptimize();
        RealPointValuePair expectedSolution = new RealPointValuePair(new double[]{1.0 / 11, 1.0 / 11}, -1.0 / 11);

        assertEquals(expectedSolution, optimalSolution);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDiscardArtificialVariables_NoArtificialVariables() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 2}),
                Arrays.asList(new LinearConstraint[]{
                        new LinearConstraint(new double[]{-1, 1}, Relationship.LEQUAL, 0),
                        new LinearConstraint(new double[]{-1, 1}, Relationship.LEQUAL, 2)
                }),
                LinearObjectiveFunction.Type.MINIMIZE, true, 1.0e-6);

        new SimplexSolver().discardArtificialVariables(tableau);
    }

    @Test
    public void testDoOptimize() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 2}),
                Arrays.asList(new LinearConstraint[]{
                        new LinearConstraint(new double[]{-1, 1}, Relationship.LEQUAL, 0),
                        new LinearConstraint(new double[]{-1, 1}, Relationship.LEQUAL, 2)
                }),
                LinearObjectiveFunction.Type.MINIMIZE, true, 1.0e-6);

        RealPointValuePair optimalSolution = new SimplexSolver().doOptimize();
        RealPointValuePair expectedSolution = new RealPointValuePair(new double[]{1.0 / 11, 1.0 / 11}, -1.0 / 11);

        assertEquals(expectedSolution, optimalSolution);
    }

    @Test
    public void testDoOptimize_MultipleConstraints() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1, 3, 2}),
                Arrays.asList(
                        new LinearConstraint(new double[]{-1, 1, 1}, Relationship.LEQUAL, 0),
                        new LinearConstraint(new double[]{-1, 1, 1}, Relationship.LEQUAL, 2),
                        new LinearConstraint(new double[]{-1, 2, 1}, Relationship.LEQUAL, 1),
                        new LinearConstraint(new double[]{-1, 2, 1}, Relationship.LEQUAL, 3)
                ),
                LinearObjectiveFunction.Type.MINIMIZE, true, 1.0e-6);

        RealPointValuePair optimalSolution = new SimplexSolver().doOptimize();
        RealPointValuePair expectedSolution = new RealPointValuePair(new double[]{3.0 / 8, 1.0 / 4, 1.0 / 4}, -3.0 / 8);

        assertEquals(expectedSolution, optimalSolution);
    }
}