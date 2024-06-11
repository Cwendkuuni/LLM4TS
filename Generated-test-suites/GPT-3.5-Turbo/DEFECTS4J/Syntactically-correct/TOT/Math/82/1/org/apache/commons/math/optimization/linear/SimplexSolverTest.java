import org.apache.commons.math.optimization.linear.SimplexSolver;
import org.apache.commons.math.optimization.linear.SimplexTableau;
import org.apache.commons.math.optimization.linear.UnboundedSolutionException;
import org.apache.commons.math.optimization.linear.NoFeasibleSolutionException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class SimplexSolverTest {

    private static final double EPSILON = 1e-6;

    private static SimplexSolver solver;
    private static SimplexTableau tableau;

    @BeforeClass
    public static void setUp() {
        solver = new SimplexSolver();
    }

    @Test
    public void testGetPivotColumn() {
        tableau = new SimplexTableau(new double[][]{{1, -1, 0},
                                                    {0, -2, 1},
                                                    {-1, 0, 0}},
                                    new double[]{1, 1, 0},
                                    GoalType.MAXIMIZE,
                                    true,
                                    EPSILON);

        Integer pivotCol = solver.getPivotColumn(tableau);

        Assert.assertEquals(Integer.valueOf(1), pivotCol);
    }

    @Test
    public void testGetPivotRow() {
        tableau = new SimplexTableau(new double[][]{{1, -2, 1},
                                                    {3, -1, 1},
                                                    {1, 1, 0}},
                                    new double[]{0, 4, 2},
                                    GoalType.MINIMIZE,
                                    false,
                                    EPSILON);

        Integer pivotRow = solver.getPivotRow(1, tableau);

        Assert.assertEquals(Integer.valueOf(2), pivotRow);
    }

    @Test(expected = UnboundedSolutionException.class)
    public void testDoIterationUnbounded() throws Exception {
        tableau = new SimplexTableau(new double[][]{{2, 1, 5},
                                                    {4, -1, -5},
                                                    {1, -1, 0}},
                                    new double[]{5, -10, 1},
                                    GoalType.MAXIMIZE,
                                    true,
                                    EPSILON);

        solver.doIteration(tableau);
    }

    @Test
    public void testDoIteration() throws Exception {
        tableau = new SimplexTableau(new double[][]{{1, 2, -1},
                                                    {1, -1, 1},
                                                    {1, -2, 2}},
                                    new double[]{2, 3, 1},
                                    GoalType.MINIMIZE,
                                    false,
                                    EPSILON);

        solver.doIteration(tableau);

        double[][] expectedTableauData = {{0, 1, -1},
                                          {1, -3, 4},
                                          {1, -4, 5}};
        double[] expectedRhsCoefficients = {1, 5, 1};

        assertTableauData(expectedTableauData, expectedRhsCoefficients, tableau);
    }

    @Test
    public void testIsPhase1Solved() {
        tableau = new SimplexTableau(new double[][]{{-1, 1, -1, 1, 0},
                                                    {-2, 1, 1, 0, -1},
                                                    {-1, -1, 1, 0 , 0},
                                                    {1, 1, 1, 0, 0}},
                                    new double[]{1, 2, 0, -1},
                                    GoalType.MINIMIZE,
                                    true,
                                    EPSILON);

        boolean isPhase1Solved = solver.isPhase1Solved(tableau);

        Assert.assertFalse(isPhase1Solved);
    }

    @Test
    public void testIsOptimal() {
        tableau = new SimplexTableau(new double[][]{{2, -1, 1},
                                                    {1, -1, 1},
                                                    {1, 2, -2}},
                                    new double[]{1, 2, 0},
                                    GoalType.MAXIMIZE,
                                    false,
                                    EPSILON);

        boolean isOptimal = solver.isOptimal(tableau);

        Assert.assertFalse(isOptimal);
    }

    @Test(expected = NoFeasibleSolutionException.class)
    public void testSolvePhase1NoFeasibleSolution() throws Exception {
        tableau = new SimplexTableau(new double[][]{{1, 2, -1},
                                                    {1, -1, -2},
                                                    {-2, 1, -1}},
                                    new double[]{1, -2, 2},
                                    GoalType.MAXIMIZE,
                                    true,
                                    EPSILON);

        solver.solvePhase1(tableau);
    }

    @Test
    public void testSolvePhase1() throws Exception {
        tableau = new SimplexTableau(new double[][]{{-1, 1, -1},
                                                    {-2, 1, 1},
                                                    {-1, -1, 1}},
                                    new double[]{1, 1, 0},
                                    GoalType.MAXIMIZE,
                                    true,
                                    EPSILON);

        solver.solvePhase1(tableau);

        double[][] expectedTableauData = {{1, -1, 1},
                                          {0, 0, -2},
                                          {0, 0, 0}};
        double[] expectedRhsCoefficients = {1, 0, 0};

        assertTableauData(expectedTableauData, expectedRhsCoefficients, tableau);
    }

    @Test
    public void testDoOptimize() throws Exception {
        tableau = new SimplexTableau(new double[][]{{1, -1, 1},
                                                    {2, -1, -1}},
                                    new double[]{2, 4},
                                    GoalType.MINIMIZE,
                                    false,
                                    EPSILON);

        RealPointValuePair solution = solver.doOptimize(tableau);

        double[] expectedPoint = {2, 0};

        Assert.assertArrayEquals(expectedPoint, solution.getPoint(), EPSILON);
    }

    private void assertTableauData(double[][] expectedTableauData,
                                   double[] expectedRhsCoefficients,
                                   SimplexTableau actualTableau) {
        for (int i = 0; i < expectedTableauData.length; i++) {
            for (int j = 0; j < expectedTableauData[i].length; j++) {
                Assert.assertEquals(expectedTableauData[i][j],
                                    actualTableau.getEntry(i, j),
                                    EPSILON);
            }
        }

        for (int i = 0; i < expectedRhsCoefficients.length; i++) {
            Assert.assertEquals(expectedRhsCoefficients[i],
                                actualTableau.getEntry(i, actualTableau.getWidth() - 1),
                                EPSILON);
        }
    }
}