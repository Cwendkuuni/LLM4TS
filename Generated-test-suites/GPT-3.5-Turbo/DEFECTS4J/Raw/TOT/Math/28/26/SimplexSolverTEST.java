import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SimplexSolverTest {

    private static final double EPSILON = 1e-6;
    private static final int MAX_ULPS = 10;

    private SimplexSolver simplexSolver;

    @Before
    public void setUp() {
        simplexSolver = new SimplexSolver(EPSILON, MAX_ULPS);
    }

    @Test
    public void testGetPivotColumn() {
        SimplexTableau tableau = createTableau();
        Integer pivotColumn = simplexSolver.getPivotColumn(tableau);
        Assert.assertEquals(Integer.valueOf(2), pivotColumn);
    }

    @Test
    public void testGetPivotRow() {
        SimplexTableau tableau = createTableau();
        Integer pivotRow = simplexSolver.getPivotRow(tableau, 2);
        Assert.assertEquals(Integer.valueOf(2), pivotRow);
    }

    @Test
    public void testDoIteration() throws Exception {
        SimplexTableau tableau = createTableau();
        simplexSolver.doIteration(tableau);

        double[][] expectedTableau = {
                {1, -1, -1, 0, 1},
                {0, 1, 1, 1, 0},
                {0, 0.5, 1, 0, 1.5}
        };
        assertTableauEquals(expectedTableau, tableau);
    }

    @Test
    public void testSolvePhase1() throws Exception {
        SimplexTableau tableau = createTableau();
        simplexSolver.solvePhase1(tableau);

        double[][] expectedTableau = {
                {1, -1, 0, 0, 1, -1, 1},
                {0, 1, 0, 1, 1, 2, 0},
                {0, 0.5, 1, 0, 0, -0.5, 1}
        };
        assertTableauEquals(expectedTableau, tableau);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testNoFeasibleSolution() throws Exception {
        SimplexTableau tableau = createInfeasibleTableau();
        simplexSolver.solvePhase1(tableau);
    }

    @Test
    public void testDoOptimize() throws Exception {
        PointValuePair solution = simplexSolver.doOptimize();
        double[] expectedPoint = {0, 0};
        Assert.assertArrayEquals(expectedPoint, solution.getPoint(), EPSILON);
        Assert.assertEquals(Double.NEGATIVE_INFINITY, solution.getValue(), EPSILON);
    }

    private SimplexTableau createTableau() {
        double[][] tableauData = {
                {1, -1, -1, 0, 1},
                {0, 1, 1, 2, 0},
                {0, 2, -1, 1, 3}
        };
        return new SimplexTableau(tableauData);
    }

    private SimplexTableau createInfeasibleTableau() {
        double[][] tableauData = {
                {1, -1, -1, 0, 1},
                {0, 1, 1, 2, 0},
                {0, 2, -1, 1, 4}
        };
        return new SimplexTableau(tableauData);
    }

    private void assertTableauEquals(double[][] expected, SimplexTableau actual) {
        double[][] actualData = actual.getData();
        Assert.assertEquals(expected.length, actualData.length);
        for (int i = 0; i < expected.length; i++) {
            Assert.assertArrayEquals(expected[i], actualData[i], EPSILON);
        }
    }
}