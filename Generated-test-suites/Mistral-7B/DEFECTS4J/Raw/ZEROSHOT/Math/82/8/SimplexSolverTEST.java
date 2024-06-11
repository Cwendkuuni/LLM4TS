import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.junit.Test;
import org.junit.Assert.*;

import static org.mockito.Mockito.*;

public class SimplexSolverTest {

    @Test
    public void testDoOptimize_SolvesLinearProblem() {
        // Given
        LinearObjectiveFunction objectiveFunction = mock(LinearObjectiveFunction.class);
        LinearConstraintSet constraints = new LinearConstraintSet();
        GoalType goalType = GoalType.MINIMIZE;
        boolean restrictToNonNegative = false;
        double epsilon = 0.001;
        SimplexSolver solver = new SimplexSolver(epsilon);

        // When
        PointValuePair solution = solver.doOptimize();

        // Then
        assertEquals(PointValuePair.class, solution.getClass());
        double[] x = solution.getPoint()[0];
        double fx = solution.getValue();
        verify(objectiveFunction, atLeastOnce()).value(x);
        assertEquals(fx, objectiveFunction.value(x), epsilon);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoOptimize_ThrowsUnboundedSolutionException() {
        // Given
        LinearObjectiveFunction objectiveFunction = mock(LinearObjectiveFunction.class);
        LinearConstraintSet constraints = new LinearConstraintSet();
        GoalType goalType = GoalType.MINIMIZE;
        boolean restrictToNonNegative = false;
        double epsilon = 0.001;
        SimplexSolver solver = new SimplexSolver(epsilon);

        SimplexTableau tableau = mock(SimplexTableau.class);
        when(tableau.getNumArtificialVariables()).thenReturn(0);
        when(tableau.getNumObjectiveFunctions()).thenReturn(1);
        when(tableau.getWidth()).thenReturn(3);
        when(tableau.getHeight()).thenReturn(2);
        when(tableau.getEntry(0, 0)).thenReturn(-1.0);
        when(tableau.getEntry(0, 1)).thenReturn(-2.0);
        when(tableau.getEntry(0, 2)).thenReturn(-3.0);
        when(tableau.getEntry(1, 0)).thenReturn(-4.0);
        when(tableau.getEntry(1, 1)).thenReturn(0.0);
        when(tableau.getEntry(1, 2)).thenReturn(1.0);
        when(tableau.getEntry(0, tableau.getWidth() - 1)).thenReturn(Double.POSITIVE_INFINITY);

        when(objectiveFunction.value(anyDoubleArray())).thenReturn(Double.NEGATIVE_INFINITY);
        when(objectiveFunction.gradient(anyDoubleArray())).thenReturn(new double[]{ -1.0, -2.0, -3.0 });

        doCallRealMethod().when(tableau).discardArtificialVariables();

        // When
        solver.doOptimize();

        // Then
        // Unbounded solution exception should be thrown
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testDoOptimize_ThrowsNoFeasibleSolutionException() {
        // Given
        LinearObjectiveFunction objectiveFunction = mock(LinearObjectiveFunction.class);
        LinearConstraintSet constraints = new LinearConstraintSet();
        GoalType goalType = GoalType.MINIMIZE;
        boolean restrictToNonNegative = false;
        double epsilon = 0.001;
        SimplexSolver solver = new SimplexSolver(epsilon);

        SimplexTableau tableau = mock(SimplexTableau.class);
        when(tableau.getNumArtificialVariables()).thenReturn(0);
        when(tableau.getNumObjectiveFunctions()).thenReturn(1);
        when(tableau.getWidth()).thenReturn(3);
        when(tableau.getHeight()).thenReturn(2);
        when(tableau.getEntry(0, 0)).thenReturn(-1.0);
        when(tableau.getEntry(0, 1)).thenReturn(-2.0);
        when(tableau.getEntry(0, 2)).thenReturn(-3.0);
        when(tableau.getEntry(1, 0)).thenReturn(-4.0);
        when(tableau.getEntry(1, 1)).thenReturn(0.0);
        when(tableau.getEntry(1, 2)).thenReturn(1.0);
        when(tableau.getEntry(0, tableau.getWidth() - 1)).thenReturn(0.0);

        when(objectiveFunction.value(anyDoubleArray())).thenReturn(Double.NEGATIVE_INFINITY);
        when(objectiveFunction.gradient(anyDoubleArray())).thenReturn(new double[]{ -1.0, -2.0, -3.0 });

        // When
        solver.doOptimize();

        // Then
        // No feasible solution exception should be thrown
    }

    @Test
    public void testGetPivotColumn() {
        // Given
        SimplexTableau tableau = new SimplexTableau(5, 5, 1, false, 0.001);

        // When
        int pivotColumn = SimplexSolver.getInstance().getPivotColumn(tableau);

        // Then
        int expectedPivotColumn = 0;
        assertEquals(expectedPivotColumn, pivotColumn);
    }

    @Test
    public void testGetPivotRow() {
        // Given
        SimplexTableau tableau = new SimplexTableau(5, 5, 1, false, 0.001);

        // When
        int pivotRow = SimplexSolver.getInstance().getPivotRow(0, tableau);

        // Then
        int expectedPivotRow = 1;
        assertEquals(expectedPivotRow, pivotRow);
    }

    @Test
    public void testDoIteration() {
        // Given
        SimplexTableau tableau = new SimplexTableau(5, 5, 1, false, 0.001);

        // When
        SimplexSolver solver = new SimplexSolver(0.001);
        solver.doIteration(tableau);

        // Then
        // Check tableau entries after one iteration
        double[] expectedTableau = {-1.23456789, -2.34567891, -3.45678912,
                0.0, 1.23456789, 1.0,
                1.23456789, -5.45678912, 0.0, 1.0,
                1.56789123, 2.56789123, 0.0, 0.0, 1.0,
                0.0, 1.0, 0.0, 0.0, 0.0, 0.0};
        assertArrayEquals(expectedTableau, tableau.toArray(), 0.001);
    }

    @Test
    public void testIsPhase1Solved() {
        // Given
        SimplexTableau tableau = new SimplexTableau(5, 5, 1, false, 0.001);

        // When
        boolean isPhase1Solved = SimplexSolver.getInstance().isPhase1Solved(tableau);

        // Then
        boolean expectedIsPhase1Solved = false;
        assertEquals(expectedIsPhase1Solved, isPhase1Solved);

        // Given
        for (int i = 0; i < tableau.getNumObjectiveFunctions(); i++) {
            tableau.setEntry(0, i, tableau.getEntry(0, i) + 1.0);
        }

        // When
        isPhase1Solved = SimplexSolver.getInstance().isPhase1Solved(tableau);

        // Then
        expectedIsPhase1Solved = true;
        assertEquals(expectedIsPhase1Solved, isPhase1Solved);
    }

    @Test
    public void testIsOptimal() {
        // Given
        SimplexTableau tableau = new SimplexTableau(5, 5, 1, false, 0.001);

        // When
        boolean isOptimal = SimplexSolver.getInstance().isOptimal(tableau);

        // Then
        boolean expectedIsOptimal = false;
        assertEquals(expectedIsOptimal, isOptimal);

        // Given
        for (int i = 0; i < tableau.getNumObjectiveFunctions(); i++) {
            tableau.setEntry(0, i, tableau.getEntry(0, i) + 1.0);
        }

        for (int i = 0; i < tableau.getWidth() - 1; i++) {
            tableau.setEntry(0, i + tableau.getWidth(), tableau.getEntry(0, i + tableau.getWidth()) + 1.0);
        }

        // When
        isOptimal = SimplexSolver.getInstance().isOptimal(tableau);

        // Then
        expectedIsOptimal = true;
        assertEquals(expectedIsOptimal, isOptimal);
    }
}