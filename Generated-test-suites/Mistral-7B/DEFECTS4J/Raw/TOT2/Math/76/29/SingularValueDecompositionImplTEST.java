import org.apache.commons.math3.linear.*;
import org.junit.Assert;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImpl_IdentityMatrix() {
        RealMatrix matrix = MatrixUtils.createIdentityMatrix(3);
        try {
            SingularValueDecompositionImpl singularValueDecomposition = new SingularValueDecompositionImpl(matrix);
            RealMatrix u = singularValueDecomposition.getU();
            RealMatrix s = singularValueDecomposition.getS();
            RealMatrix v = singularValueDecomposition.getV();
            RealMatrix ut = singularValueDecomposition.getUT();
            RealMatrix st = singularValueDecomposition.getS().transpose();
            RealMatrix vt = singularValueDecomposition.getVT();

            // Check if U * S * V^T is equal to the original matrix
            RealMatrix product = u.multiply(s).multiply(vt);
            Assert.assertEquals("U * S * V^T should be equal to the original matrix", matrix, product);

            // Check if U^T * U is equal to the identity matrix
            RealMatrix utu = ut.multiply(u);
            Assert.assertEquals("U^T * U should be equal to the identity matrix", MatrixUtils.createIdentityMatrix(3), utu);

            // Check if V * V^T is equal to the identity matrix
            RealMatrix vtv = v.multiply(vt);
            Assert.assertEquals("V * V^T should be equal to the identity matrix", MatrixUtils.createIdentityMatrix(3), vtv);

            // Check if the singular values are correct
            Assert.assertArrayEquals("Singular values should be correct", new double[]{1.0, 1.0, 1.0}, singularValueDecomposition.getSingularValues(), 0.001);

        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSingularValueDecompositionImpl_RandomMatrix() {
        RealMatrix matrix = MatrixUtils.createRandomMatrix(3, 3);
        try {
            SingularValueDecompositionImpl singularValueDecomposition = new SingularValueDecompositionImpl(matrix);
            RealMatrix u = singularValueDecomposition.getU();
            RealMatrix s = singularValueDecomposition.getS();
            RealMatrix v = singularValueDecomposition.getV();
            RealMatrix ut = singularValueDecomposition.getUT();
            RealMatrix st = singularValueDecomposition.getS().transpose();
            RealMatrix vt = singularValueDecomposition.getVT();

            // Check if U * S * V^T is equal to the original matrix
            RealMatrix product = u.multiply(s).multiply(vt);
            Assert.assertEquals("U * S * V^T should be equal to the original matrix", matrix, product, 0.001);

            // Check if U^T * U and V * V^T are close to the identity matrix
            RealMatrix utu = ut.multiply(u);
            RealMatrix vtv = v.multiply(vt);
            Assert.assertTrue("U^T * U and V * V^T should be close to the identity matrix",
                    MatrixUtils.isIdentityMatrixWithinTolerance(utu, MatrixUtils.createIdentityMatrix(3), 0.001) &&
                    MatrixUtils.isIdentityMatrixWithinTolerance(vtv, MatrixUtils.createIdentityMatrix(3), 0.001));

            // Check if the singular values are correct (approximately)
            double[] singularValues = singularValueDecomposition.getSingularValues();
            for (int i = 0; i < singularValues.length; i++) {
                Assert.assertTrue("Singular values should be positive", singularValues[i] > 0);
            }
            Assert.assertArrayEquals("Singular values should be correct (approximately)",
                    singularValues, s.getData(), 0.001);

        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = InvalidMatrixException.class)
    public void testSingularValueDecompositionImpl_SingularMatrix() {
        RealMatrix matrix = MatrixUtils.createSingularMatrix(3, 3);
        new SingularValueDecompositionImpl(matrix);
    }

    @Test(expected = InvalidMatrixException.class)
    public void testSingularValueDecompositionImpl_ZeroMatrix() {
        RealMatrix matrix = MatrixUtils.createZeroMatrix(3, 3);
        new SingularValueDecompositionImpl(matrix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSingularValueDecompositionImpl_NegativeMinSingularValue() {
        SingularValueDecompositionImpl singularValueDecomposition = new SingularValueDecompositionImpl(MatrixUtils.createIdentityMatrix(3));
        singularValueDecomposition.getCovariance(-1);
    }
}