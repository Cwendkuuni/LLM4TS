import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.optimization.linear.SimplexSolver;
import org.apache.commons.math.optimization.linear.SimplexTableau;
import org.apache.commons.math.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math.optimization.linear.Relationship;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.linear.ArrayRealVector;
import org.apache.commons.math.linear.RealVector;

import static org.junit.Assert.*;

public class SimplexSolverTest {

    private SimplexSolver solver;

    @Before
    public void setUp() {
        solver = new SimplexSolver();
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[] {1, -2}, 0), 
                                                    Relationship.LEQ,
                                                    new double[][]{{1, -2}, {2, 1}, {1, 1}}, 
                                                    new double[]{1, 2, 3},
                                                    GoalType.MINIMIZE, true);
        Integer pivotColumn = solver.getPivotColumn(tableau);
        assertEquals(Integer.valueOf(1), pivotColumn);
    }

    @Test
    public void testGetPivotColumnNoNegativeCoefficient() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[] {1, 2}, 0), 
                                                    Relationship.LEQ,
                                                    new double[][]{{1, 2}, {2, 1}, {1, 1}}, 
                                                    new double[]{1, 2, 3},
                                                    GoalType.MINIMIZE, true);
        Integer pivotColumn = solver.getPivotColumn(tableau);
        assertNull(pivotColumn);
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[] {1, -2}, 0), 
                                                    Relationship.LEQ,
                                                    new double[][]{{1, -2}, {2, 1}, {1, 1}}, 
                                                    new double[]{1, 2, 3},
                                                    GoalType.MINIMIZE, true);
        Integer pivotRow = solver.getPivotRow(1, tableau);
        assertEquals(Integer.valueOf(2), pivotRow);
    }

    @Test
    public void testGetPivotRowNoPositiveCoefficient() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[] {1, -2}, 0), 
                                                    Relationship.LEQ,
                                                    new double[][]{{1, -2}, {2, -1}, {1, -1}}, 
                                                    new double[]{1, 2, 3},
                                                    GoalType.MINIMIZE, true);
        Integer pivotRow = solver.getPivotRow(1, tableau);
        assertNull(pivotRow);
    }

    @Test
    public void testDoIteration() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[] {1, -2}, 0), 
                                                    Relationship.LEQ,
                                                    new double[][]{{1, -2}, {2, 1}, {1, 1}}, 
                                                    new double[]{1, 2, 3},
                                                    GoalType.MINIMIZE, true);
        solver.doIteration(tableau);
        RealVector expected = new ArrayRealVector(new double[] {0, 1, 1});
        assertEquals(expected, tableau.getSolution().getPoint());
    }

    @Test
    public void testIsPhase1Solved() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[] {1, -2}, 0), 
                                                    Relationship.LEQ,
                                                    new double[][]{{1, -2}, {2, 1}, {1, 1}}, 
                                                    new double[]{1, 2, 3},
                                                    GoalType.MINIMIZE, true);
        boolean isPhase1Solved = solver.isPhase1Solved(tableau);
        assertFalse(isPhase1Solved);
    }

    @Test
    public void testIsPhase1SolvedAllPositiveCoefficients() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[] {1, 2}, 0), 
                                                    Relationship.LEQ,
                                                    new double[][]{{1, 2}, {2, 1}, {1, 1}}, 
                                                    new double[]{1, 2, 3},
                                                    GoalType.MINIMIZE, true);
        boolean isPhase1Solved = solver.isPhase1Solved(tableau);
        assertTrue(isPhase1Solved);
    }

    @Test
    public void testIsOptimal() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[] {1, -2}, 0), 
                                                    Relationship.LEQ,
                                                    new double[][]{{1, -2}, {2, 1}, {1, 1}}, 
                                                    new double[]{1, 2, 3},
                                                    GoalType.MINIMIZE, true);
        boolean isOptimal = solver.isOptimal(tableau);
        assertFalse(isOptimal);
    }

    @Test
    public void testIsOptimalAllPositiveCoefficients() {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[] {1, 2}, 0), 
                                                    Relationship.LEQ,
                                                    new double[][]{{1, 2}, {2, 1}, {1, 1}}, 
                                                    new double[]{1, 2, 3},
                                                    GoalType.MINIMIZE, true);
        boolean isOptimal = solver.isOptimal(tableau);
        assertTrue(isOptimal);
    }

    @Test
    public void testSolvePhase1() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[] {1, -2, 3}, 0), 
                                                    Relationship.LEQ,
                                                    new double[][]{{1, -2, 1}, {2, 1, 1}, {-1, 2, -1}}, 
                                                    new double[]{1, 2, 3},
                                                    GoalType.MINIMIZE, true);
        solver.solvePhase1(tableau);
        RealVector expected = new ArrayRealVector(new double[] {1, 0, 1});
        assertEquals(expected, tableau.getSolution().getPoint());
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1NoFeasibleSolution() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[] {1, -2, 3}, 0), 
                                                    Relationship.LEQ,
                                                    new double[][]{{1, -2, 1}, {2, 1, 1}, {-1, 2, 1}}, 
                                                    new double[]{1, 2, 3},
                                                    GoalType.MINIMIZE, true);
        solver.solvePhase1(tableau);
    }

    @Test
    public void testDoOptimize() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(new LinearObjectiveFunction(new double[] {1, -2, 3}, 0), 
                                                    Relationship.LEQ,
                                                    new double[][]{{1, -2, 1}, {2, 1, 1}, {-1, 2, -1}}, 
                                                    new double[]{1, 2, 3},
                                                    GoalType.MINIMIZE, true);
        RealPointValuePair solution = solver.doOptimize(tableau);
        RealVector expected = new ArrayRealVector(new double[] {1, 0, 1});
        assertEquals(expected, solution);
    }
}