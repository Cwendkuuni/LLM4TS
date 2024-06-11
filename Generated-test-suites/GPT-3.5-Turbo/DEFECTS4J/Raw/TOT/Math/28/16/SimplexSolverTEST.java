import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexSolverTest {

    private SimplexSolver solver;

    @Before
    public void setUp() {
        solver = new SimplexSolver();
    }

    // Test getPivotColumn method

    @Test
    public void testGetPivotColumn() {
        double[][] coefficients = { {-2, -3, -4, 0, 0, 0},
                                    { 1,  0,  0, 1, 0, 4},
                                    { 0,  1,  0, 0, 1, 3},
                                    { 0,  0,  1, 0, 0, 1} };
        SimplexTableau tableau = new SimplexTableau(coefficients, 0, 2, 0);
        Integer pivotColumn = solver.getPivotColumn(tableau);
        assertEquals(Integer.valueOf(1), pivotColumn);
    }

    // Test getPivotRow method

    @Test
    public void testGetPivotRow() {
        double[][] coefficients = { {-2, -3, -4, 0, 0, 0},
                                    { 1,  0,  0, 1, 0, 4},
                                    { 0,  1,  0, 0, 1, 3},
                                    { 0,  0,  1, 0, 0, 1} };
        SimplexTableau tableau = new SimplexTableau(coefficients, 0, 2, 0);
        Integer pivotRow = solver.getPivotRow(tableau, 1);
        assertEquals(Integer.valueOf(2), pivotRow);
    }

    // Test doIteration method

    @Test
    public void testDoIteration() {
        double[][] coefficients = { {-2, -3, -4, 0, 0, 0},
                                    { 1,  0,  0, 1, 0, 4},
                                    { 0,  1,  0, 0, 1, 3},
                                    { 0,  0,  1, 0, 0, 1} };
        SimplexTableau tableau = new SimplexTableau(coefficients, 0, 2, 0);
        try {
            solver.doIteration(tableau);
        } catch (MaxCountExceededException | UnboundedSolutionException e) {
            e.printStackTrace();
        }
        double[][] expectedCoefficients = { {-4, -3,  0, 0, 0, -2},
                                            { 1,  0, -4, 1, 0,  4},
                                            { 0,  1,  0, 0, 1,  3},
                                            { 1,  0,  0, 0, 0,  1} };
        assertArrayEquals(expectedCoefficients, tableau.getCoefficients());
    }

    // Test solvePhase1 method

    @Test
    public void testSolvePhase1() {
        double[][] coefficients = { {-1, -2, -3, 0,  0,  0,  0},
                                    { 2,  1,  1, 1,  0,  0,  5},
                                    { 1,  3,  2, 0,  1,  0, 10},
                                    {-1, -1, -1, 0,  0, -1, -3} };
        SimplexTableau tableau = new SimplexTableau(coefficients, 3, 3, 2);
        try {
            solver.solvePhase1(tableau);
        } catch (MaxCountExceededException | UnboundedSolutionException | NoFeasibleSolutionException e) {
            e.printStackTrace();
        }
        double[][] expectedCoefficients = { { -9, -8, -5, 0, 0, 0 },
                                            { -1, -2, -3, 1, 0, 0 },
                                            { -6, -4, -1, 0, 1, 0 },
                                            {  1,  1,  1, 0, 0, 1 } };
        assertArrayEquals(expectedCoefficients, tableau.getCoefficients());
    }

    // Test doOptimize method

    @Test
    public void testDoOptimize() {
        double[][] coefficients = { {-1, -2, -3, 0,  0,  0,  0},
                                    { 2,  1,  1, 1,  0,  0,  5},
                                    { 1,  3,  2, 0,  1,  0, 10},
                                    {-1, -1, -1, 0,  0, -1, -3} };
        SimplexTableau tableau = new SimplexTableau(coefficients, 3, 3, 2);
        PointValuePair solution = null;
        try {
            solution = solver.doOptimize(tableau);
        } catch (MaxCountExceededException | UnboundedSolutionException | NoFeasibleSolutionException e) {
            e.printStackTrace();
        }
        double[] expectedSolution = { 0, 1, 3 };
        double[] actualSolution = solution.getPoint();
        assertArrayEquals(expectedSolution, actualSolution, 0.001);
    }
}