import org.apache.commons.math3.linear.*;
import org.junit.Assert;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecomposition() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        SingularValueDecompositionImpl singularValueDecomposition = new SingularValueDecompositionImpl(matrix);

        RealMatrix U = singularValueDecomposition.getU();
        RealMatrix S = singularValueDecomposition.getS();
        RealMatrix V = singularValueDecomposition.getV();

        // Check U * S * V^T equals original matrix
        Assert.assertEquals(matrix, U.multiply(S).multiply(V.transpose()));
    }

    @Test
    public void testSingularValueDecompositionDiagonalMatrix() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealDiagonalMatrix(new double[]{1.0, 1.0});
        SingularValueDecompositionImpl singularValueDecomposition = new SingularValueDecompositionImpl(matrix);

        RealMatrix U = singularValueDecomposition.getU();
        RealMatrix S = singularValueDecomposition.getS();
        RealMatrix V = singularValueDecomposition.getV();

        // Check U * S * V^T equals original matrix
        Assert.assertEquals(matrix, U.multiply(S).multiply(V.transpose()));
    }

    @Test
    public void testSingularValueDecompositionIdentityMatrix() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createIdentityMatrix(3);
        SingularValueDecompositionImpl singularValueDecomposition = new SingularValueDecompositionImpl(matrix);

        RealMatrix U = singularValueDecomposition.getU();
        RealMatrix S = singularValueDecomposition.getS();
        RealMatrix V = singularValueDecomposition.getV();

        // Check U * S * V^T equals original matrix
        Assert.assertEquals(MatrixUtils.createIdentityMatrix(3), U.multiply(S).multiply(V.transpose()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSingularValueDecompositionWithInvalidMatrix() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 1.0}});
        new SingularValueDecompositionImpl(matrix); // The matrix has rank 2, but we're trying to compute the SVD for a rank 3 matrix.
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSingularValueDecompositionWithEmptyMatrix() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{}, {}});
        new SingularValueDecompositionImpl(matrix); // The matrix is empty.
    }

    @Test(expected = InvalidMatrixException.class)
    public void testSingularValueDecompositionWithInvalidDimension() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}, {1.0, 1.0}});
        new SingularValueDecompositionImpl(matrix, 1); // The number of singular values requested is less than the actual number of singular values.
    }

    @Test
    public void testGetSingularValues() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        SingularValueDecompositionImpl singularValueDecomposition = new SingularValueDecompositionImpl(matrix);

        double[] singularValues = singularValueDecomposition.getSingularValues();

        Assert.assertArrayEquals(new double[]{Math.sqrt(5.0), 0.0}, singularValues, 0.0);
    }

    @Test
    public void testGetNorm() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        SingularValueDecompositionImpl singularValueDecomposition = new SingularValueDecompositionImpl(matrix);

        double norm = singularValueDecomposition.getNorm();

        Assert.assertEquals(Math.sqrt(5.0), norm, 0.0);
    }

    @Test
    public void testGetConditionNumber() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        SingularValueDecompositionImpl singularValueDecomposition = new SingularValueDecompositionImpl(matrix);

        double conditionNumber = singularValueDecomposition.getConditionNumber();

        Assert.assertEquals(Math.sqrt(5.0) / Math.sqrt(1.0), conditionNumber, 0.0);
    }

    @Test
    public void testGetRank() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}});
        SingularValueDecompositionImpl singularValueDecomposition = new SingularValueDecompositionImpl(matrix);

        int rank = singularValueDecomposition.getRank();

        Assert.assertEquals(2, rank);
    }

    @Test
    public void testGetCovariance() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 1.0}});
        SingularValueDecompositionImpl singularValueDecomposition = new SingularValueDecompositionImpl(matrix);

        RealMatrix covariance = singularValueDecomposition.getCovariance(0.1);

        // Singular value decomposition of a 2x2 matrix has 2 singular values, so the covariance matrix should be 2x2 as well.
        Assert.assertEquals(2, covariance.getRowDimension());
        Assert.assertEquals(2, covariance.getColumnDimension());
    }

}