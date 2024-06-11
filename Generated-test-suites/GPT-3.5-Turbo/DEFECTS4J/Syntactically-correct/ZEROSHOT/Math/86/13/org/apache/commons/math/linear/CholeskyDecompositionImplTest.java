import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

    private CholeskyDecompositionImpl decomposition;

    @Before
    public void setUp() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
            {4, 12, -16},
            {12, 37, -43},
            {-16, -43, 98}
        });
        try {
            decomposition = new CholeskyDecompositionImpl(matrix);
        } catch (NonSquareMatrixException | NotSymmetricMatrixException | NotPositiveDefiniteMatrixException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetL() {
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{
            {2, 0, 0},
            {6, 1, 0},
            {-8, 5, 3}
        });

        RealMatrix result = decomposition.getL();

        assertTrue(expected.equals(result));
    }

    @Test
    public void testGetLT() {
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{
            {2, 6, -8},
            {0, 1, 5},
            {0, 0, 3}
        });

        RealMatrix result = decomposition.getLT();

        assertTrue(expected.equals(result));
    }

    @Test
    public void testGetDeterminant() {
        double expected = 576;

        double result = decomposition.getDeterminant();

        assertEquals(expected, result, 0);
    }

    @Test
    public void testGetSolver() {
        DecompositionSolver solver = decomposition.getSolver();

        assertTrue(solver instanceof CholeskyDecompositionImpl.Solver);
    }

    @Test
    public void testSolverIsNonSingular() {
        DecompositionSolver solver = decomposition.getSolver();

        assertTrue(solver.isNonSingular());
    }

    @Test
    public void testSolve() {
        DecompositionSolver solver = decomposition.getSolver();
        double[] b = {1, 2, 3};
        double[] expected = {-3, 1, 2};

        double[] result = solver.solve(b);

        assertArrayEquals(expected, result, 0);
    }

    @Test
    public void testSolveRealVector() {
        DecompositionSolver solver = decomposition.getSolver();
        RealVector b = new RealVectorImpl(new double[]{1, 2, 3});
        RealVector expected = new RealVectorImpl(new double[]{-3, 1, 2});

        RealVector result = solver.solve(b);

        assertEquals(expected, result);
    }

    @Test
    public void testSolveRealMatrix() {
        DecompositionSolver solver = decomposition.getSolver();
        RealMatrix b = MatrixUtils.createRealMatrix(new double[][]{{1}, {2}, {3}});
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{-3}, {1}, {2}});

        RealMatrix result = solver.solve(b);

        assertTrue(expected.equals(result));
    }

    @Test
    public void testGetInverse() {
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{
            {0.375, -0.125, 0.0},
            {-0.125, 0.125, 0.25},
            {0.0, 0.25, 0.3333333333333333}
        });

        RealMatrix result = decomposition.getInverse();

        assertTrue(expected.equals(result));
    }
}