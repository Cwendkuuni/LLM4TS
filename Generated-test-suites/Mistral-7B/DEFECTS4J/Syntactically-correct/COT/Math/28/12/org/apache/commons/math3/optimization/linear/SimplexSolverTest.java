import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.SimplexTableau;
import org.apache.commons.math3.optim.nonlinear.SimpleGoalType;
import org.apache.commons.math3.optim.nonlinear.Constraint;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SimplexSolverTest {

    @Test
    public void testDefaultConstructor() {
        SimplexSolver solver = new SimplexSolver();
        Assert.assertEquals(1.0e-6, solver.epsilon, 0);
        Assert.assertEquals(10, solver.maxUlps);
    }

    @Test
    public void testCustomConstructor() {
        SimplexSolver solver = new SimplexSolver(0.001, 5);
        Assert.assertEquals(0.001, solver.epsilon, 0);
        Assert.assertEquals(5, solver.maxUlps);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testGetPivotColumnWithEmptyTableau() {
        SimplexTableau tableau = mock(SimplexTableau.class);
        when(tableau.getNumObjectiveFunctions()).thenReturn(0);
        SimplexSolver solver = new SimplexSolver();
        solver.getPivotColumn(tableau);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testGetPivotRowWithInvalidPivotColumn() {
        LinearObjectiveFunction objFunction = mock(LinearObjectiveFunction.class);
        LinearConstraint[] constraints = new LinearConstraint[0];
        SimplexTableau tableau = new SimplexTableau(objFunction, constraints, SimpleGoalType.MINIMIZE, false, 1e-6, 10);
        when(tableau.getEntry(0, 1)).thenReturn(-1);
        SimplexSolver solver = new SimplexSolver();
        solver.getPivotRow(tableau, 1);
    }

    @Test
    public void testGetPivotRowWithValidPivotColumn() {
        LinearObjectiveFunction objFunction = mock(LinearObjectiveFunction.class);
        LinearConstraint[] constraints = new LinearConstraint[0];
        SimplexTableau tableau = new SimplexTableau(objFunction, constraints, SimpleGoalType.MINIMIZE, false, 1e-6, 10);
        when(tableau.getEntry(0, 1)).thenReturn(-10);
        when(tableau.getEntry(1, 1)).thenReturn(-5);
        when(tableau.getEntry(1, 2)).thenReturn(1);
        when(tableau.getEntry(2, 1)).thenReturn(3);
        when(tableau.getEntry(2, 2)).thenReturn(2);
        SimplexSolver solver = new SimplexSolver();
        Integer result = solver.getPivotRow(tableau, 1);
        Assert.assertEquals(1, result.intValue());
    }

    @Test(expected = MaxCountExceededException.class)
    public void testDoIterationWithMaxIterationsExceeded() {
        SimplexTableau tableau = mock(SimplexTableau.class);
        when(tableau.getNumObjectiveFunctions()).thenReturn(1);
        when(tableau.getWidth()).thenReturn(3);
        when(tableau.getHeight()).thenReturn(3);
        SimplexSolver solver = new SimplexSolver();
        solver.maxIterations = 1;
        solver.doIteration(tableau);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testSolvePhase1WithUnboundedSolution() {
        LinearObjectiveFunction objFunction = mock(LinearObjectiveFunction.class);
        LinearConstraint[] constraints = new LinearConstraint[0];
        SimplexTableau tableau = new SimplexTableau(objFunction, constraints, SimpleGoalType.MINIMIZE, false, 1e-6, 10);
        when(tableau.getNumArtificialVariables()).thenReturn(0);
        when(tableau.isOptimal()).thenReturn(false);
        when(tableau.getEntry(0, tableau.getRhsOffset())).thenReturn(Double.POSITIVE_INFINITY);
        SimplexSolver solver = new SimplexSolver();
        solver.solvePhase1(tableau);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1WithNoFeasibleSolution() {
        LinearObjectiveFunction objFunction = mock(LinearObjectiveFunction.class);
        LinearConstraint[] constraints = new LinearConstraint[0];
        SimplexTableau tableau = new SimplexTableau(objFunction, constraints, SimpleGoalType.MINIMIZE, false, 1e-6, 10);
        when(tableau.getNumArtificialVariables()).thenReturn(1);
        when(tableau.isOptimal()).thenReturn(false);
        when(tableau.getEntry(0, tableau.getRhsOffset())).thenReturn(0);
        when(tableau.getEntry(1, tableau.getRhsOffset())).thenReturn(Double.POSITIVE_INFINITY);
        SimplexSolver solver = new SimplexSolver();
        solver.solvePhase1(tableau);
    }

    @Test
    public void testDoOptimizeWithBoundedSolution() {
        LinearObjectiveFunction objFunction = mock(LinearObjectiveFunction.class);
        LinearConstraint[] constraints = new LinearConstraint[0];
        SimplexTableau tableau = new SimplexTableau(objFunction, constraints, SimpleGoalType.MINIMIZE, false, 1e-6, 10);
        when(tableau.getNumArtificialVariables()).thenReturn(1);
        when(tableau.isOptimal()).thenReturn(false);
        when(tableau.getEntry(0, tableau.getRhsOffset())).thenReturn(0);
        when(tableau.getEntry(1, tableau.getRhsOffset())).thenReturn(0);
        when(tableau.getEntry(1, tableau.getWidth() - 1)).thenReturn(2);
        when(tableau.getEntry(1, 1)).thenReturn(-1);
        when(tableau.getEntry(1, 2)).thenReturn(1);
        when(tableau.getEntry(2, tableau.getWidth() - 1)).thenReturn(3);
        when(tableau.getEntry(2, 1)).thenReturn(1);
        when(tableau.getEntry(2, 2)).thenReturn(1);
        SimplexSolver solver = new SimplexSolver();
        PointValuePair result = solver.doOptimize();
        Assert.assertEquals(-1.5, result.getValue(), 0);
        Assert.assertArrayEquals(new double[]{2.0, 1.0}, result.getPoint(), 0);
    }

}