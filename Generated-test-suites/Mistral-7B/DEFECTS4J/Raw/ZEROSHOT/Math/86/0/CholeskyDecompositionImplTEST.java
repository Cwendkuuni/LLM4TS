import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {

    @Test
    public void testCholeskyDecomposition() {
        double[][] matrixData = {
                {4.0, 1.0, 1.0},
                {1.0, 4.0, 1.0},
                {1.0, 1.0, 4.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);

        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = cholesky.getL();
        RealMatrix lT = cholesky.getLT();

        assertEquals("L is not lower triangular", l.isLowerTriangular(), true);
        assertEquals("L is not equal to L^T", l.transpose().equals(lT), true);
        assertEquals("Determinant not equal to 1", cholesky.getDeterminant(), 1.0, 1e-12);

        RealVector b = new ArrayRealVector(new double[]{1.0, -2.0, 3.0});
        RealVector x = cholesky.getSolver().solve(b);
        RealVector xExpected = new ArrayRealVector(new double[]{0.31622776601683795,
                0.7124537624236759, 1.0});
        assertEquals("Solution is not correct", x, xExpected);

        RealMatrix A = new Array2DRowRealMatrix(new double[][]{
                {4.0, 1.0, 1.0},
                {1.0, 4.0, 1.0},
                {1.0, 1.0, 4.0}
        });
        RealMatrix AInv = cholesky.getSolver().getInverse();
        RealMatrix product = A.multiply(AInv);
        RealMatrix identity = MatrixUtils.createIdentityMatrix(A.getRowDimension());

        // Check the product of A and its inverse is the identity matrix
        assertTrue("Product of A and its inverse is not the identity matrix",
                Precision.equals(product, identity, 1e-12));
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        double[][] matrixData = {
                {4.0, 1.0},
                {1.0, 4.0},
                {1.0, 1.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrixException() {
        double[][] matrixData = {
                {4.0, 1.0, 1.0},
                {1.0, 4.0, 1.0},
                {1.0, 1.0, 3.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        new CholeskyDecompositionImpl(matrix, 0.001, 0.001);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrixException() {
        double[][] matrixData = {
                {1.0, 1.0},
                {1.0, 1.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        new CholeskyDecompositionImpl(matrix, 0.001, 0.001);
    }

}