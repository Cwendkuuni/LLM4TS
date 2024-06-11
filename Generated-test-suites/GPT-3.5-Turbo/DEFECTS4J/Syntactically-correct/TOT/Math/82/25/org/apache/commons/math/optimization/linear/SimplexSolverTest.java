import org.junit.Test;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.apache.commons.math.optimization.linear.SimplexSolver;
import org.apache.commons.math.optimization.linear.SimplexTableau;
import org.apache.commons.math.optimization.linear.UnboundedSolutionException;
import org.apache.commons.math.optimization.linear.NoFeasibleSolutionException;
import org.apache.commons.math.optimization.OptimizationException;

import static org.junit.Assert.*;

public class SimplexSolverTest {

    @Test
    public void testGetPivotColumn() {
        double[][] coefficients = {{1, 2, 3}, {4, 5, 6}};
        double[] constants = {7, 8};

        SimplexTableau tableau = new SimplexTableau(coefficients, constants);
        int pivotColumn = new SimplexSolver().getPivotColumn(tableau);

        assertEquals(0, pivotColumn);
    }

    @Test
    public void testGetPivotRow() {
        double[][] coefficients = {{1, 2, 3}, {4, 5, 6}};
        double[] constants = {7, 8};

        SimplexTableau tableau = new SimplexTableau(coefficients, constants);
        int pivotColumn = new SimplexSolver().getPivotColumn(tableau);
        int pivotRow = new SimplexSolver().getPivotRow(pivotColumn, tableau);

        assertEquals(0, pivotRow);
    }

    @Test
    public void testDoIteration() throws OptimizationException {
        double[][] coefficients = {{1, 2, 3}, {4, 5, 6}};
        double[] constants = {7, 8};

        SimplexTableau tableau = new SimplexTableau(coefficients, constants);
        SimplexSolver solver = new SimplexSolver();

        solver.doIteration(tableau);

        double[][] expectedCoefficients = {{-0.111, 2, 3}, {0.888, 5, 6}};
        double[] expectedConstants = {0.777, 8};

        assertArrayEquals(expectedCoefficients, tableau.getCoefficients(), 0.001);
        assertArrayEquals(expectedConstants, tableau.getConstants(), 0.001);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoIteration_UnboundedSolutionException() throws OptimizationException {
        double[][] coefficients = {{-1, -2, -3}, {4, 5, 6}};
        double[] constants = {7, 8};

        SimplexTableau tableau = new SimplexTableau(coefficients, constants);
        SimplexSolver solver = new SimplexSolver();

        solver.doIteration(tableau);
    }

    @Test
    public void testIsPhase1Solved() {
        double[][] coefficients = {{1, 2, 3}, {4, 5, 6}};
        double[] constants = {7, 8};

        SimplexTableau tableau = new SimplexTableau(coefficients, constants);
        SimplexSolver solver = new SimplexSolver();

        boolean phase1Solved = solver.isPhase1Solved(tableau);

        assertFalse(phase1Solved);
    }

    @Test
    public void testIsOptimal() {
        double[][] coefficients = {{1, 2, 3}, {4, 5, 6}};
        double[] constants = {7, 8};

        SimplexTableau tableau = new SimplexTableau(coefficients, constants);
        SimplexSolver solver = new SimplexSolver();

        boolean optimal = solver.isOptimal(tableau);

        assertFalse(optimal);
    }

    @Test
    public void testSolvePhase1() throws OptimizationException {
        double[][] coefficients = {{1, 2, 3}, {4, 5, 6}};
        double[] constants = {7, 8};

        SimplexTableau tableau = new SimplexTableau(coefficients, constants);
        SimplexSolver solver = new SimplexSolver();

        solver.solvePhase1(tableau);

        double[][] expectedCoefficients = {{1, 0, 1}, {0, 1, 2}};
        double[] expectedConstants = {0, 0};

        assertArrayEquals(expectedCoefficients, tableau.getCoefficients(), 0.001);
        assertArrayEquals(expectedConstants, tableau.getConstants(), 0.001);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1_NoFeasibleSolutionException() throws OptimizationException {
        double[][] coefficients = {{-1, -2, -3}, {4, 5, 6}};
        double[] constants = {7, 8};

        SimplexTableau tableau = new SimplexTableau(coefficients, constants);
        SimplexSolver solver = new SimplexSolver();

        solver.solvePhase1(tableau);
    }

    @Test
    public void testDoOptimize() throws OptimizationException {
        double[][] coefficients = {{1, 2, 3}, {4, 5, 6}};
        double[] constants = {7, 8};

        SimplexTableau tableau = new SimplexTableau(coefficients, constants);
        SimplexSolver solver = new SimplexSolver();

        RealPointValuePair solution = solver.doOptimize();

        double[] expectedPoint = {0, 0, 0};
        double expectedValue = 0.0;

        assertArrayEquals(expectedPoint, solution.getPoint(), 0.001);
        assertEquals(expectedValue, solution.getValue(), 0.001);
    }

}