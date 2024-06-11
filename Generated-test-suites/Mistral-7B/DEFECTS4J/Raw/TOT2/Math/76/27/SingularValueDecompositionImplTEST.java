import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SingularValueDecompositionImpl;
import org.apache.commons.math3.linear.Solver;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test for SingularValueDecompositionImpl.
 */
public class SingularValueDecompositionImplTest {

    /**
     * Test method getU() for positive dimension.
     */
    @Test
    public void testGetUWithPositiveDimension() {
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        try {
            final SingularValueDecompositionImpl decomposition =
                    new SingularValueDecompositionImpl(matrix);
            final RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{{0.6595508142031241, -0.7557811832727018},
                                                                           {-0.7557811832727018, 0.6595508142031241}});
            RealMatrix actualU = decomposition.getU();
            assertEquals(expectedU, actualU);
        } catch (InvalidMatrixException e) {
            // should not be thrown
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method getUT() for positive dimension.
     */
    @Test
    public void testGetUTWithPositiveDimension() {
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        try {
            final SingularValueDecompositionImpl decomposition =
                    new SingularValueDecompositionImpl(matrix);
            final RealMatrix expectedUT = new Array2DRowRealMatrix(new double[][]{{0.6595508142031241, -0.7557811832727018},
                                                                             {-0.7557811832727018, 0.6595508142031241}});
            RealMatrix actualUT = decomposition.getUT();
            assertEquals(expectedUT, actualUT);
        } catch (InvalidMatrixException e) {
            // should not be thrown
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method getS() for positive dimension.
     */
    @Test
    public void testGetSWithPositiveDimension() {
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        try {
            final SingularValueDecompositionImpl decomposition =
                    new SingularValueDecompositionImpl(matrix, 2);
            double[] expectedSingularValues = {1.4142137410599636, 0.7071067811865476};
            double[] actualSingularValues = decomposition.getSingularValues();
            assertArrayEquals(expectedSingularValues, actualSingularValues);
        } catch (InvalidMatrixException e) {
            // should not be thrown
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method getV() for positive dimension.
     */
    @Test
    public void testGetVWithPositiveDimension() {
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        try {
            final SingularValueDecompositionImpl decomposition =
                    new SingularValueDecompositionImpl(matrix);
            final RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{{-0.7557811832727018, 0.6595508142031241},
                                                                            {0.6595508142031241, 0.7557811832727018}});
            RealMatrix actualV = decomposition.getV();
            assertEquals(expectedV, actualV);
        } catch (InvalidMatrixException e) {
            // should not be thrown
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method getVT() for positive dimension.
     */
    @Test
    public void testGetVTWithPositiveDimension() {
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        try {
            final SingularValueDecompositionImpl decomposition =
                    new SingularValueDecompositionImpl(matrix);
            final RealMatrix expectedVT = new Array2DRowRealMatrix(new double[][]{{-0.7557811832727018, 0.6595508142031241},
                                                                               {0.6595508142031241, -0.7557811832727018}});
            RealMatrix actualVT = decomposition.getVT();
            assertEquals(expectedVT, actualVT);
        } catch (InvalidMatrixException e) {
            // should not be thrown
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method getSolver() for positive dimension.
     */
    @Test
    public void testGetSolverWithPositiveDimension() {
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        try {
            final SingularValueDecompositionImpl decomposition =
                    new SingularValueDecompositionImpl(matrix);
            final Solver solver = decomposition.getSolver();
            assertNotNull(solver);
        } catch (InvalidMatrixException e) {
            // should not be thrown
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method getCovariance() for positive dimension.
     */
    @Test
    public void testGetCovarianceWithPositiveDimension() {
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        try {
            final SingularValueDecompositionImpl decomposition =
                    new SingularValueDecompositionImpl(matrix);
            final RealMatrix covariance = decomposition.getCovariance(0.5);
            final double[][] covarianceData = covariance.getData();
            for (double[] row : covarianceData) {
                assertArrayEquals(new double[]{0.3826834323658256, 0.3826834323658256}, row);
            }
        } catch (InvalidMatrixException e) {
            // should not be thrown
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method getNorm() for positive dimension.
     */
    @Test
    public void testGetNormWithPositiveDimension() {
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        try {
            final SingularValueDecompositionImpl decomposition =
                    new SingularValueDecompositionImpl(matrix);
            double actualNorm = decomposition.getNorm();
            assertEquals(1.4142137410599636, actualNorm, 1e-14);
        } catch (InvalidMatrixException e) {
            // should not be thrown
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method getConditionNumber() for positive dimension.
     */
    @Test
    public void testGetConditionNumberWithPositiveDimension() {
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        try {
            final SingularValueDecompositionImpl decomposition =
                    new SingularValueDecompositionImpl(matrix);
            double actualConditionNumber = decomposition.getConditionNumber();
            assertEquals(1.4142137410599636 / 0.7071067811865476, actualConditionNumber, 1e-14);
        } catch (InvalidMatrixException e) {
            // should not be thrown
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method getRank() for positive dimension.
     */
    @Test
    public void testGetRankWithPositiveDimension() {
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        try {
            final SingularValueDecompositionImpl decomposition =
                    new SingularValueDecompositionImpl(matrix);
            int actualRank = decomposition.getRank();
            assertEquals(2, actualRank);
        } catch (InvalidMatrixException e) {
            // should not be thrown
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method getCovariance() for singular value decomposition with less columns than rows.
     */
    @Test(expected = InvalidMatrixException.class)
    public void testGetCovarianceWithLessColumnsThanRows() {
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}, {5, 6}});
        try {
            final SingularValueDecompositionImpl decomposition =
                    new SingularValueDecompositionImpl(matrix);
            decomposition.getCovariance(0.5);
            fail("Expected InvalidMatrixException");
        } catch (InvalidMatrixException e) {
            // expected
        }
    }

    /**
     * Test method getCovariance() for singular value decomposition with less rows than columns.
     */
    @Test(expected = InvalidMatrixException.class)
    public void testGetCovarianceWithLessRowsThanColumns() {
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}, {5}});
        try {
            final SingularValueDecompositionImpl decomposition =
                    new SingularValueDecompositionImpl(matrix);
            decomposition.getCovariance(0.5);
            fail("Expected InvalidMatrixException");
        } catch (InvalidMatrixException e) {
            // expected
        }
    }

}