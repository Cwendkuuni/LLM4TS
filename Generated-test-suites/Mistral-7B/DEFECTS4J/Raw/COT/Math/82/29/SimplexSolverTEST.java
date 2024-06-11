import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearConstraints;
import org.apache.commons.math3.optim.linear.LinearOptimizationFactory;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SimplexSolverTest {

    @Test
    public void testSimplexSolver_DefaultConstructor() {
        SimplexSolver solver = new SimplexSolver();
        assertEquals(1.0e-6, solver.epsilon, 0.0);
    }

    @Test
    public void testSimplexSolver_CustomConstructor() {
        double customEpsilon = 1.0e-7;
        SimplexSolver solver = new SimplexSolver(customEpsilon);
        assertEquals(customEpsilon, solver.epsilon, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPivotColumn_EmptyTableau() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{-1}), new LinearConstraints());
        SimplexSolver solver = new SimplexSolver();
        solver.getPivotColumn(tableau);
    }

    @Test
    public void testGetPivotColumn_NegativeValues() {
        double[] coefficients = new double[]{-1.0, -2.0, 3.0};
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(coefficients), new LinearConstraints());
        SimplexSolver solver = new SimplexSolver();
        Integer pivotColumn = solver.getPivotColumn(tableau);
        assertEquals(0, pivotColumn.intValue());
    }

    @Test
    public void testGetPivotColumn_PositiveValues() {
        double[] coefficients = new double[]{1.0, -2.0, 3.0};
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(coefficients), new LinearConstraints());
        SimplexSolver solver = new SimplexSolver();
        Integer pivotColumn = solver.getPivotColumn(tableau);
        assertEquals(1, pivotColumn.intValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPivotRow_NullColumn() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{1.0, 1.0, 1.0}),
                new LinearConstraints(new LinearConstraint[]{new LinearConstraint(0, SimpleBounds.unbounded(), Double.POSITIVE_INFINITY),
                new LinearConstraint(1, SimpleBounds.unbounded(), Double.POSITIVE_INFINITY),
                new LinearConstraint(2, SimpleBounds.unbounded(), Double.POSITIVE_INFINITY)}));
        Integer pivotColumn = 3;
        SimplexSolver solver = new SimplexSolver();
        solver.getPivotRow(pivotColumn, tableau);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPivotRow_InvalidColumn() {
        double[] coefficients = new double[]{1.0, 1.0, 1.0};
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(coefficients), new LinearConstraints());
        Integer pivotColumn = 3;
        SimplexSolver solver = new SimplexSolver();
        solver.getPivotRow(pivotColumn, tableau);
    }

    @Test
    public void testGetPivotRow_ValidColumn() {
        double[] coefficients = new double[]{1.0, 1.0, 1.0};
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(coefficients), new LinearConstraints());
        Integer pivotColumn = 0;
        SimplexSolver solver = new SimplexSolver();
        Integer pivotRow = solver.getPivotRow(pivotColumn, tableau);
        assertEquals(1, pivotRow.intValue());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDoIteration_EmptyTableau() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{1.0, 1.0, 1.0}),
                new LinearConstraints());
        SimplexSolver solver = new SimplexSolver();
        solver.doIteration(tableau);
    }

    @Test
    public void testDoIteration_SimpleCase() {
        double[] objectiveFunctionCoefficients = new double[]{-1.0, 1.0, 1.0};
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(objectiveFunctionCoefficients),
                new LinearConstraints(new LinearConstraint[]{new LinearConstraint(0, SimpleBounds.unbounded(), Double.POSITIVE_INFINITY),
                        new LinearConstraint(1, SimpleBounds.unbounded(), Double.POSITIVE_INFINITY),
                        new LinearConstraint(2, SimpleBounds.unbounded(), Double.POSITIVE_INFINITY)}));
        SimplexSolver solver = new SimplexSolver();
        solver.doIteration(tableau);
        assertEquals(1, tableau.getNumArtificialVariables());
        assertEquals(0.0, tableau.getEntry(1, 0), 0.0);
        assertEquals(0.0, tableau.getEntry(1, 1), 0.0);
        assertEquals(-1.0 / 2.0, tableau.getEntry(1, 2), 0.0);
        assertEquals(0.5, tableau.getEntry(1, 3), 0.0);
        assertEquals(-0.5, tableau.getEntry(1, 4), 0.0);
        assertEquals(1.0 / 2.0, tableau.getEntry(1, 5), 0.0);
    }

    @Test
    public void testIsPhase1Solved_EmptyTableau() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[]{1.0, 1.0, 1.0}),
                new LinearConstraints());
        SimplexSolver solver = new SimplexSolver();
        assertFalse(solver.isPhase1Solved(tableau));
    }

    @Test
    public void testIsPhase1Solved_SimpleCase() {
        double[] objectiveFunctionCoefficients = new double[]{-1.0, 1.0, 1.0};
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(objectiveFunctionCoefficients),
                new LinearConstraints(new LinearConstraint[]{new LinearConstraint(0, SimpleBounds.unbounded(), Double.POSITIVE_INFINITY),
                        new LinearConstraint(1, SimpleBounds.unbounded(), Double.POSITIVE_INFINITY),
                        new LinearConstraint(2, SimpleBounds.unbounded(), Double.POSITIVE_INFINITY)}));
        SimplexSolver solver = new SimplexSolver();
        tableau.doIteration(solver);
        tableau.doIteration(solver);
        tableau.doIteration(solver);
        tableau.doIteration(solver);
        tableau.doIteration(solver);
        assertTrue(solver.isPhase1Solved(tableau));
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testSolvePhase1_UnboundedSolution() {
        double[] objectiveFunctionCoefficients = new double[]{-1.0, 1.0, 1.0};
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(objectiveFunctionCoefficients),
                new LinearConstraints(new LinearConstraint[]{new LinearConstraint(0, SimpleBounds.unbounded(), Double.POSITIVE_INFINITY),
                        new LinearConstraint(1, SimpleBounds.unbounded(), Double.POSITIVE_INFINITY),
                        new LinearConstraint(2, SimpleBounds.unbounded(), Double.POSITIVE_INFINITY)}));
        SimplexSolver solver = new SimplexSolver();
        solver.solvePhase1(tableau);
    }

    @Test
    public void testSolvePhase1_SimpleCase() {
        double[] objectiveFunctionCoefficients = new double[]{-1.0, 1.0, 1.0};
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(objectiveFunctionCoefficients),
                new LinearConstraints(new LinearConstraint[]{new LinearConstraint(0, SimpleBounds.unbounded(), Double.POSITIVE_INFINITY),
                        new LinearConstraint(1, SimpleBounds.unbounded(), Double.POSITIVE_INFINITY),
                        new LinearConstraint(2, SimpleBounds.unbounded(), Double.POSITIVE_INFINITY)}));
        SimplexSolver solver = new SimplexSolver();
        solver.solvePhase1(tableau);
        assertEquals(0, tableau.getNumArtificialVariables());
        assertEquals(0.0, tableau.getEntry(0, 0), 0.0);
        assertEquals(0.0, tableau.getEntry(0, 1), 0.0);
        assertEquals(0.0, tableau.getEntry(0, 2), 0.0);
        assertEquals(0.0, tableau.getEntry(0, 3), 0.0);
        assertEquals(0.0, tableau.getEntry(0, 4), 0.0);
        assertEquals(0.0, tableau.getEntry(0, 5), 0.0);
        assertEquals(0.0, tableau.getEntry(0, 6), 0.0);
        assertEquals(0.0, tableau.getEntry(0, 7), 0.0);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testDoOptimize_NoFeasibleSolution() {
        double[] objectiveFunctionCoefficients = new double[]{-1.0, 1.0, 1.0};
        LinearConstraint constraint = new LinearConstraint(0, SimpleBounds.unbounded(), Double.NEGATIVE_INFINITY);
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(objectiveFunctionCoefficients),
                new LinearConstraints(constraint));
        SimplexSolver solver = new SimplexSolver();
        solver.solvePhase1(tableau);
        solver.doIteration(tableau);
        solver.doIteration(tableau);
        solver.doIteration(tableau);
        solver.doIteration(tableau);
        solver.doIteration(tableau);
        solver.doIteration(tableau);
        solver.doIteration(tableau);
        solver.doIteration(tableau);
        PointValuePair solution = solver.doOptimize();
        assertNull(solution);
    }

    @Test
    public void testDoOptimize_SimpleCase() {
        double[] objectiveFunctionCoefficients = new double[]{1.0, 1.0, 1.0};
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(objectiveFunctionCoefficients),
                new LinearConstraints(new LinearConstraint[]{new LinearConstraint(0, SimpleBounds.unbounded(), Double.POSITIVE_INFINITY),
                        new LinearConstraint(1, SimpleBounds.unbounded(), Double.POSITIVE_INFINITY),
                        new LinearConstraint(2, SimpleBounds.unbounded(), Double.POSITIVE_INFINITY)}));
        SimplexSolver solver = new SimplexSolver();
        solver.solvePhase1(tableau);
        PointValuePair solution = solver.doOptimize();
        assertEquals(3, solution.getPoint().length);
        assertEquals(0.0, solution.getValue(), 0.0);
    }

}