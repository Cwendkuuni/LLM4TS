import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Assert;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

    @Test
    public void testCholeskyDecomposition() {
        double[][] input = {
                {2.0, 1.0, 0.0},
                {1.0, 2.0, 1.0},
                {0.0, 1.0, 2.0}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(input);
        try {
            CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
            RealMatrix l = decomposition.getL();
            RealMatrix lt = decomposition.getLT();
            Assert.assertEquals(matrix, lt.multiply(l));
            Assert.assertEquals(MatrixUtils.createIdentityMatrix(3), l.transpose().multiply(l));
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCholeskyDecompositionSymmetric() {
        double[][] input = {
                {2.0, 1.0},
                {1.0, 2.0}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(input);
        try {
            CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix, 1e-12);
            RealMatrix l = decomposition.getL();
            RealMatrix lt = decomposition.getLT();
            Assert.assertEquals(matrix, lt.multiply(l));
            Assert.assertEquals(MatrixUtils.createIdentityMatrix(2), l.transpose().multiply(l));
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testCholeskyDecompositionNotPositiveDefinite() {
        double[][] input = {
                {2.0, 1.0},
                {1.0, -1.0}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(input);
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testCholeskyDecompositionNotSymmetric() {
        double[][] input = {
                {2.0, 1.0},
                {1.0, 3.0}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(input);
        new CholeskyDecompositionImpl(matrix, 1e-12, 1e-12);
    }

    @Test
    public void testSolver() {
        double[][] input = {
                {2.0, 1.0, 0.0},
                {1.0, 2.0, 1.0},
                {0.0, 1.0, 2.0}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(input);
        try {
            CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
            RealVector b = new RealVectorImpl(new double[]{1.0, 2.0, 3.0});
            RealVector x = decomposition.getSolver().solve(b);
            Assert.assertArrayEquals(new double[]{1.0 / Math.sqrt(2.0),
                    0.5, 1.0 / Math.sqrt(2.0)}, x.toArray(), 0.001);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetDeterminant() {
        double[][] input = {
                {2.0, 1.0, 0.0},
                {1.0, 2.0, 1.0},
                {0.0, 1.0, 2.0}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(input);
        try {
            CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
            double determinant = decomposition.getDeterminant();
            Assert.assertEquals(StatUtils.product(new double[]{2.0, 1.0, 1.0 / 2.0}), determinant, 0.001);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

}