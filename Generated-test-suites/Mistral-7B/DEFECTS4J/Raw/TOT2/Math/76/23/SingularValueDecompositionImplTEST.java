import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SingularValueDecompositionImpl;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.exception.SingularValueDecompositionException;
import org.apache.commons.math3.exception.InvalidMatrixException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test cases for SingularValueDecompositionImpl class.
 */
public class SingularValueDecompositionImplTest {

    /**
     * Test SingularValueDecompositionImpl constructor with valid input.
     * @throws InvalidMatrixException
     */
    @Test
    public void testSingularValueDecompositionImplValidInput()
        throws InvalidMatrixException {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix expectedU = RealMatrixUtils.createRealMatrix(new double[][]{{-0.7448, -0.4330},
                                                                             { 0.4330,  0.7448}});
        RealMatrix expectedVt = RealMatrixUtils.createRealMatrix(new double[][]{{-0.7448,  0.4330},
                                                                              {-0.4330,  0.7448}});
        RealMatrix expectedS = RealMatrixUtils.createRealDiagonalMatrix(new double[]{1.4142, 1.4142});
        RealMatrix expectedUT = expectedU.transpose();
        RealMatrix expectedVT = expectedVt.transpose();
        RealMatrix expectedVTU = expectedVT.multiply(expectedU);
        assertEquals(expectedUT, svd.getUT());
        assertEquals(expectedVT, svd.getVT());
        assertEquals(expectedS, svd.getS());
        assertEquals(expectedVTU, svd.getS().multiply(svd.getVT()));
        assertEquals(expectedS, svd.getS().multiply(svd.getU().transpose()));
    }

    /**
     * Test SingularValueDecompositionImpl constructor with invalid input.
     * @throws SingularValueDecompositionException
     * @throws InvalidMatrixException
     */
    @Test(expected=SingularValueDecompositionException.class)
    public void testSingularValueDecompositionImplInvalidInput()
        throws SingularValueDecompositionException, InvalidMatrixException {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 3}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
    }

    /**
     * Test SingularValueDecompositionImpl getRank method.
     * @throws SingularValueDecompositionException
     * @throws InvalidMatrixException
     */
    @Test
    public void testSingularValueDecompositionImplGetRank()
        throws SingularValueDecompositionException, InvalidMatrixException {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        assertEquals(2, svd.getRank());
    }

    /**
     * Test SingularValueDecompositionImpl getCovariance method.
     * @throws SingularValueDecompositionException
     * @throws InvalidMatrixException
     */
    @Test
    public void testSingularValueDecompositionImplGetCovariance()
        throws SingularValueDecompositionException, InvalidMatrixException {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6},
                                                                            {7, 8, 9}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
        double[] cutoffSingularValues = {1.0, 0.5};
        RealMatrix covarianceMatrix = svd.getCovariance(cutoffSingularValues);
        double[][] covarianceMatrixData = covarianceMatrix.getData();
        double[] expectedCovarianceData1 = {0.5642, 0.4531, 0};
        double[] expectedCovarianceData2 = {0.4531, 0.5642, 0};
        assertArrayEquals(expectedCovarianceData1, covarianceMatrixData[0]);
        assertArrayEquals(expectedCovarianceData2, covarianceMatrixData[1]);
    }

    /**
     * Test SingularValueDecompositionImpl getNorm method.
     * @throws SingularValueDecompositionException
     * @throws InvalidMatrixException
     */
    @Test
    public void testSingularValueDecompositionImplGetNorm()
        throws SingularValueDecompositionException, InvalidMatrixException {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double expectedNorm = 4.4721;
        assertEquals(expectedNorm, svd.getNorm(), 0.001);
    }

    /**
     * Test SingularValueDecompositionImpl getConditionNumber method.
     * @throws SingularValueDecompositionException
     * @throws InvalidMatrixException
     */
    @Test
    public void testSingularValueDecompositionImplGetConditionNumber()
        throws SingularValueDecompositionException, InvalidMatrixException {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double expectedConditionNumber = 4.4721 / 0.0;
        assertEquals(expectedConditionNumber, svd.getConditionNumber(), 0.001);
    }

    /**
     * Test SingularValueDecompositionImpl solve method with valid input.
     * @throws SingularValueDecompositionException
     * @throws InvalidMatrixException
     */
    @Test
    public void testSingularValueDecompositionImplSolveValidInput()
        throws SingularValueDecompositionException, InvalidMatrixException {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double[] b = {1, 2};
        double[] x = svd.getSolver().solve(b);
        double[] expectedX = {-1.4142, 1.4142};
        assertArrayEquals(expectedX, x);
    }

    /**
     * Test SingularValueDecompositionImpl solve method with invalid input.
     * @throws SingularValueDecompositionException
     * @throws InvalidMatrixException
     */
    @Test(expected=IllegalArgumentException.class)
    public void testSingularValueDecompositionImplSolveInvalidInput()
        throws SingularValueDecompositionException, InvalidMatrixException {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealVector b = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}}).columnVector(0);
        svd.getSolver().solve(b);
    }

}