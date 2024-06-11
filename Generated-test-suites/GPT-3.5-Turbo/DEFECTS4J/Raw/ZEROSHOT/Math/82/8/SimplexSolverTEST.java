import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.optimization.linear.SimplexSolver;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.linear.AbstractLinearOptimizer;
import org.apache.commons.math.optimization.linear.SimplexTableau;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.NoFeasibleSolutionException;
import org.apache.commons.math.optimization.UnboundedSolutionException;

import static org.junit.Assert.*;

public class SimplexSolverTest {

    private SimplexSolver solver;
    private double[] f;
    private double[][] constraints;
    private GoalType goalType;
    private boolean restrictToNonNegative;
    private double epsilon;

    @Before
    public void setUp() {
        solver = new SimplexSolver();
        f = new double[]{1.0, -2.0};
        constraints = new double[][]{{1.0, 1.0}, {-1.0, 1.0}};
        goalType = GoalType.MAXIMIZE;
        restrictToNonNegative = false;
        epsilon = 1.0e-6;
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        Integer pivotColumn = solver.getPivotColumn(tableau);
        assertEquals(Integer.valueOf(1), pivotColumn);
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        Integer pivotRow = solver.getPivotRow(1, tableau);
        assertEquals(Integer.valueOf(0), pivotRow);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoIterationUnboundedSolution() throws OptimizationException {
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        solver.doIteration(tableau);
    }

    @Test
    public void testDoIteration() throws OptimizationException {
        f = new double[]{1.0, -1.0};
        constraints = new double[][]{{1.0, 1.0}, {-1.0, 1.0}};
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        solver.doIteration(tableau);
        double[] expectedRow1 = new double[]{0.5, -0.5, 0.0};
        double[] expectedRow2 = new double[]{0.5, 0.5, 1.0};
        assertArrayEquals(expectedRow1, tableau.getRow(1), epsilon);
        assertArrayEquals(expectedRow2, tableau.getRow(2), epsilon);
    }

    @Test
    public void testIsPhase1Solved() {
        f = new double[]{-1.0, 2.0, 0.0};
        constraints = new double[][]{{-1.0, 1.0, 1.0}, {1.0, -1.0, 1.0}, {1.0, 1.0, 2.0}};
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        boolean phase1Solved = solver.isPhase1Solved(tableau);
        assertFalse(phase1Solved);
    }

    @Test
    public void testIsOptimal() {
        f = new double[]{-1.0, 2.0, 0.0};
        constraints = new double[][]{{-1.0, 1.0, 1.0}, {1.0, -1.0, 1.0}, {1.0, 1.0, 2.0}};
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        boolean optimal = solver.isOptimal(tableau);
        assertFalse(optimal);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1NoFeasibleSolution() throws OptimizationException {
        f = new double[]{-1.0, -2.0};
        constraints = new double[][]{{1.0, 1.0}, {-1.0, 1.0}};
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        solver.solvePhase1(tableau);
    }

    @Test
    public void testSolvePhase1() throws OptimizationException {
        f = new double[]{-1.0, -2.0};
        constraints = new double[][]{{1.0, 1.0}, {-1.0, 1.0}};
        SimplexTableau tableau = new SimplexTableau(f, constraints, goalType, restrictToNonNegative, epsilon);
        solver.solvePhase1(tableau);
        double[] expectedRow1 = new double[]{1.0, 1.0, 1.0};
        double[] expectedRow2 = new double[]{0.0, 1.0, 1.0};
        assertArrayEquals(expectedRow1, tableau.getRow(1), epsilon);
        assertArrayEquals(expectedRow2, tableau.getRow(2), epsilon);
    }

    @Test
    public void testDoOptimize() throws OptimizationException {
        RealPointValuePair solution = solver.doOptimize();
        double[] expectedPoint = new double[]{1.0, 0.0};
        assertArrayEquals(expectedPoint, solution.getPoint(), epsilon);
        assertEquals(-1.0, solution.getValue(), epsilon);
    }

}