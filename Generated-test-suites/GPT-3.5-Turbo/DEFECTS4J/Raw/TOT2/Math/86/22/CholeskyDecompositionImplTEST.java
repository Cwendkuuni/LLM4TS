import org.junit.Test;
import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {

    @Test
    public void testGetDetermiant() {
        // Test when matrix is 2x2
        double[][] matrixData = {{4.0, 1.0}, {1.0, 3.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        assertEquals(11.0, decomposition.getDeterminant(), 1e-15);
    }

    @Test
    public void testGetInverse() {
        // Test when matrix is 3x3
        double[][] matrixData = {{4.0, 1.0, 2.0}, {1.0, 3.0, -1.0}, {2.0, -1.0, 6.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix inverse = decomposition.getSolver().getInverse();
        RealMatrix expectedInverse = MatrixUtils.createRealMatrix(new double[][]{
                {0.236111, -0.069444, -0.055556},
                {-0.069444, 0.513889, 0.027778},
                {-0.055556, 0.027778, 0.194444}
        });
        assertEquals(expectedInverse, inverse);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        double[][] matrixData = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrixException() {
        double[][] matrixData = {{1.0, 2.0}, {3.0, 4.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrixException() {
        double[][] matrixData = {{-1.0, 0.0}, {0.0, -1.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
    }
}