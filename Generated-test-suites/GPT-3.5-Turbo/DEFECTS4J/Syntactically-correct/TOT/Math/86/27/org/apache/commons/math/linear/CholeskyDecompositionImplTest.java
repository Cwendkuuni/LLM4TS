import org.apache.commons.math.linear.CholeskyDecompositionImpl;
import org.apache.commons.math.linear.DecompositionSolver;
import org.junit.Assert;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

    @Test
    public void testConstructor() {
        double[][] matrixData = { { 4, 12, -16 }, { 12, 37, -43 }, { -16, -43, 98 } };
        double[][] expectedLTData = { { 2, 6, -8 }, { 0, 1, 5 }, { 0, 0, 3 } };

        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);

        double[][] ltData = cholesky.getLT().getData();

        for (int i = 0; i < matrixData.length; i++) {
            for (int j = 0; j < matrixData[i].length; j++) {
                Assert.assertEquals(expectedLTData[i][j], ltData[i][j], 0.0001);
            }
        }
    }

    @Test
    public void testGetL() {
        double[][] matrixData = { { 16, 4, -8 }, { 4, 20, 10 }, { -8, 10, 71 } };
        double[][] expectedLData = { { 4, 0, 0 }, { 1, 4, 0 }, { -2, 2.5, 8.5 } };

        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);

        double[][] lData = cholesky.getL().getData();

        for (int i = 0; i < matrixData.length; i++) {
            for (int j = 0; j < matrixData[i].length; j++) {
                Assert.assertEquals(expectedLData[i][j], lData[i][j], 0.0001);
            }
        }
    }

    @Test
    public void testGetDeterminant() {
        double[][] matrixData = { { 1, 3, 2 }, { 3, 13, 7 }, { 2, 7, 17 } };
        double expectedDeterminant = 84;

        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);

        double determinant = cholesky.getDeterminant();

        Assert.assertEquals(expectedDeterminant, determinant, 0.0001);
    }

    @Test
    public void testGetSolver() {
        double[][] matrixData = { { 4, 12, -16 }, { 12, 37, -43 }, { -16, -43, 98 } };
        double[] vectorData = { 3, 2, 1 };
        double[] expectedSolution = { -2, 0, 1 };

        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = cholesky.getSolver();

        RealVector b = new RealVectorImpl(vectorData);

        RealVector solution = solver.solve(b);

        for (int i = 0; i < vectorData.length; i++) {
            Assert.assertEquals(expectedSolution[i], solution.getEntry(i), 0.0001);
        }
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorNonSquareMatrix() {
        double[][] matrixData = { { 1, 2, 3 }, { 4, 5, 6 } };

        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testConstructorNotSymmetricMatrix() {
        double[][] matrixData = { { 1, 2, 3 }, { 2, 5, 6 }, { 3, 6, 8 } };

        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testConstructorNotPositiveDefiniteMatrix() {
        double[][] matrixData = { { -1, 2, 3 }, { 2, 5, 6 }, { 3, 6, 8 } };

        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
    }

}