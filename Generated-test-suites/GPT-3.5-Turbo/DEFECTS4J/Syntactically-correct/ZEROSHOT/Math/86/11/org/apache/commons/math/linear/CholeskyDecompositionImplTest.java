import org.apache.commons.math.linear.RealMatrixImpl;
import org.junit.Assert;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

    @Test
    public void testGetL() {
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrixImpl matrix = new RealMatrixImpl(matrixData);

        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        double[][] lData = choleskyDecomposition.getL().getData();

        double[][] expectedLData = {{2, 0, 0}, {6, 1, 0}, {-8, 5, 3}};
        RealMatrixImpl expectedL = new RealMatrixImpl(expectedLData);

        Assert.assertArrayEquals(expectedL.getData(), lData);
    }

    @Test
    public void testGetLT() {
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrixImpl matrix = new RealMatrixImpl(matrixData);

        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        double[][] ltData = choleskyDecomposition.getLT().getData();

        double[][] expectedLTData = {{2, 6, -8}, {0, 1, 5}, {0, 0, 3}};
        RealMatrixImpl expectedLT = new RealMatrixImpl(expectedLTData);

        Assert.assertArrayEquals(expectedLT.getData(), ltData);
    }

    @Test
    public void testGetDeterminant() {
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrixImpl matrix = new RealMatrixImpl(matrixData);

        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        double determinant = choleskyDecomposition.getDeterminant();

        double expectedDeterminant = 36;
        Assert.assertEquals(expectedDeterminant, determinant, 0.0001);
    }

    @Test
    public void testGetSolver() {
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrixImpl matrix = new RealMatrixImpl(matrixData);

        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = choleskyDecomposition.getSolver();

        Assert.assertNotNull(solver);
    }

}