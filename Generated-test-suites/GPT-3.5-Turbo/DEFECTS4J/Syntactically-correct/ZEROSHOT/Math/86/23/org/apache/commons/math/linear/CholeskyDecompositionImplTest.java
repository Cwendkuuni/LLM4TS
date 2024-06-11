import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

    private CholeskyDecompositionImpl choleskyDecomposition;

    @Before
    public void setUp() {
        // Create a test matrix
        double[][] testData = {
            {4,12,-16},
            {12,37,-43},
            {-16,-43,98}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);

        // Perform Cholesky decomposition
        try {
            choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        choleskyDecomposition = null;
    }

    @Test
    public void testGetL() {
        double[][] expectedData = {
            {2, 0, 0},
            {6, 1, 0},
            {-8, 5, 3}
        };
        RealMatrix expectedL = MatrixUtils.createRealMatrix(expectedData);

        assertEquals(expectedL, choleskyDecomposition.getL());
    }

    @Test
    public void testGetLT() {
        double[][] expectedData = {
            {2, 6, -8},
            {0, 1, 5},
            {0, 0, 3}
        };
        RealMatrix expectedLT = MatrixUtils.createRealMatrix(expectedData);

        assertEquals(expectedLT, choleskyDecomposition.getLT());
    }

    @Test
    public void testGetDeterminant() {
        double expectedDeterminant = -383;

        assertEquals(expectedDeterminant, choleskyDecomposition.getDeterminant(), 0.0001);
    }

    @Test
    public void testGetSolver() {
        DecompositionSolver solver = choleskyDecomposition.getSolver();

        // Test solver
        double[] b = {2, -1, 3};
        double[] expectedX = {1, -1, 2};
        assertArrayEquals(expectedX, solver.solve(b), 0.0001);

        // Test solver for RealVector
        double[] b2 = {2, -1, 3};
        RealVector bVector = new RealVectorImpl(b2);
        double[] expectedX2 = {1, -1, 2};
        RealVector expectedXVector = new RealVectorImpl(expectedX2);
        assertEquals(expectedXVector, solver.solve(bVector));

        // Test solver for RealMatrix
        double[][] b3 = {{2, 1}, {-1, 2}, {3, -2}};
        RealMatrix bMatrix = MatrixUtils.createRealMatrix(b3);
        double[][] expectedX3 = {{1, -1}, {-1, 1}, {2, -2}};
        RealMatrix expectedXMatrix = MatrixUtils.createRealMatrix(expectedX3);
        assertEquals(expectedXMatrix, solver.solve(bMatrix));
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        double[][] testData = {
            {1, 2, 3},
            {4, 5, 6}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);

        choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrixException() {
        double[][] testData = {
            {4, 12, -16},
            {12, 37, -43},
            {-16, -43, 100}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);

        choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrixException() {
        double[][] testData = {
            {1, -2, 3},
            {-2, 5, -6},
            {3, -6, 10}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);

        choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
    }

}