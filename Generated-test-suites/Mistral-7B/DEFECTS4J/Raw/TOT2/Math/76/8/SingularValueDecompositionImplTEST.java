package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.Array2DRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SingularValueDecompositionImpl;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the SingularValueDecompositionImpl class.
 */
public class SingularValueDecompositionImplTest {

    /**
     * Test the constructor with a valid matrix.
     * @throws Exception if an error occurs
     */
    @Test
    public void testValidConstruction()
        throws Exception {
        final RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{3.0, 2.0}, {2.0, 3.0}});
        final SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
    }

    /**
     * Test the constructor with an invalid matrix.
     * @throws Exception if an error occurs
     */
    @Test(expected = InvalidMatrixException.class)
    public void testInvalidConstruction()
        throws Exception {
        final RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{3.0, 2.0}, {2.0, 3.0}, {1.0, 3.0}});
        new SingularValueDecompositionImpl(matrix);
    }

    /**
     * Test the getU() method.
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetU()
        throws Exception {
        final RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{3.0, 2.0}, {2.0, 3.0}});
        final SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        final RealMatrix expectedU = MatrixUtils.createRealMatrix(new double[][]{{0.54302611, 0.85822882}, {-0.85822882, 0.54302611}});
        final RealMatrix u = svd.getU();
        Assert.assertEquals(expectedU, u);
    }

    /**
     * Test the getUT() method.
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetUT()
        throws Exception {
        final RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{3.0, 2.0}, {2.0, 3.0}});
        final SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        final RealMatrix expectedUT = MatrixUtils.createRealMatrix(new double[][]{{0.54302611, -0.85822882}, {0.85822882, 0.54302611}});
        final RealMatrix ut = svd.getUT();
        Assert.assertEquals(expectedUT, ut);
    }

    /**
     * Test the getS() method.
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetS()
        throws Exception {
        final RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{3.0, 2.0}, {2.0, 3.0}});
        final SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        final RealMatrix expectedS = MatrixUtils.createRealDiagonalMatrix(new double[]{3.74165739, 2.16224561});
        final RealMatrix s = svd.getS();
        Assert.assertEquals(expectedS, s);
    }

    /**
     * Test the getSingularValues() method.
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetSingularValues()
        throws Exception {
        final RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{3.0, 2.0}, {2.0, 3.0}});
        final SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        final double[] expectedSingularValues = new double[]{3.74165739, 2.16224561};
        final double[] singularValues = svd.getSingularValues();
        Assert.assertArrayEquals(expectedSingularValues, singularValues);
    }

    /**
     * Test the getV() method.
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetV()
        throws Exception {
        final RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{3.0, 2.0}, {2.0, 3.0}});
        final SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        final RealMatrix expectedV = MatrixUtils.createRealMatrix(new double[][]{{0.53846931, 0.85355339}, {-0.85355339, 0.53846931}});
        final RealMatrix v = svd.getV();
        Assert.assertEquals(expectedV, v);
    }

    /**
     * Test the getVT() method.
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetVT()
        throws Exception {
        final RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{3.0, 2.0}, {2.0, 3.0}});
        final SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        final RealMatrix expectedVT = MatrixUtils.createRealMatrix(new double[][]{{0.53846931, -0.85355339}, {-0.85355339, 0.53846931}});
        final RealMatrix vt = svd.getVT();
        Assert.assertEquals(expectedVT, vt);
    }

    /**
     * Test the getCovariance() method with a valid cutoff.
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetCovarianceValidCutoff()
        throws Exception {
        final RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        final SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        final double cutoff = 2.0;
        final RealMatrix expectedCovariance = MatrixUtils.createRealMatrix(new double[][]{{5.0, 5.0}, {5.0, 5.0}});
        final RealMatrix covariance = svd.getCovariance(cutoff);
        Assert.assertEquals(expectedCovariance, covariance);
    }

    /**
     * Test the getCovariance() method with an invalid cutoff.
     * @throws Exception if an error occurs
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetCovarianceInvalidCutoff()
        throws Exception {
        final RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        final SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        final double cutoff = 10.0;
        svd.getCovariance(cutoff);
    }

    /**
     * Test the getNorm() method.
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetNorm()
        throws Exception {
        final RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{3.0, 2.0}, {2.0, 3.0}});
        final SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        final double norm = svd.getNorm();
        Assert.assertEquals(3.74165739, norm, 0.001);
    }

    /**
     * Test the getConditionNumber() method.
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetConditionNumber()
        throws Exception {
        final RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{3.0, 2.0}, {2.0, 3.0}});
        final SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        final double conditionNumber = svd.getConditionNumber();
        Assert.assertEquals(3.74165739 / 2.16224561, conditionNumber, 0.001);
    }

    /**
     * Test the getRank() method.
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetRank()
        throws Exception {
        final RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{3.0, 2.0}, {2.0, 3.0}, {1.0, 3.0}});
        final SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        final int rank = svd.getRank();
        Assert.assertEquals(2, rank);
    }

    /**
     * Test the getSolver() method.
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetSolver()
        throws Exception {
        final RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{3.0, 2.0}, {2.0, 3.0}});
        final SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        final DecompositionSolver solver = svd.getSolver();
        Assert.assertNotNull(solver);
    }

}