package org.apache.commons.math.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Precision;

import org.junit.Test;

/**
 * Tests for SingularValueDecompositionImpl class.
 */
public class SingularValueDecompositionImplTest {

    /**
     * Test SingularValueDecompositionImpl constructor with a valid matrix.
     */
    @Test
    public void testSingularValueDecompositionImplWithValidMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}});
        SingularValueDecompositionImpl svd = null;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            fail("Constructor should not throw InvalidMatrixException with a valid matrix.");
        }
        assertNotNull(svd.getU());
        assertNotNull(svd.getS());
        assertNotNull(svd.getV());
        assertNotNull(svd.getCachedU());
        assertNotNull(svd.getCachedUt());
        assertNotNull(svd.getCachedS());
        assertNotNull(svd.getCachedV());
        assertNotNull(svd.getCachedVt());
        assertEquals(2, svd.getCachedU().getRowDimension());
        assertEquals(2, svd.getCachedU().getColumnDimension());
        assertEquals(2, svd.getCachedS().getRowDimension());
        assertEquals(2, svd.getCachedS().getColumnDimension());
        assertEquals(2, svd.getCachedV().getRowDimension());
        assertEquals(2, svd.getCachedV().getColumnDimension());
        assertEquals(2, svd.getCachedVt().getRowDimension());
        assertEquals(2, svd.getCachedVt().getColumnDimension());
    }

    /**
     * Test SingularValueDecompositionImpl constructor with a singular matrix.
     */
    @Test(expected = InvalidMatrixException.class)
    public void testSingularValueDecompositionImplWithSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 0}});
        new SingularValueDecompositionImpl(matrix);
    }

    /**
     * Test SingularValueDecompositionImpl constructor with a zero matrix.
     */
    @Test(expected = InvalidMatrixException.class)
    public void testSingularValueDecompositionImplWithZeroMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{0, 0}, {0, 0}});
        new SingularValueDecompositionImpl(matrix);
    }

    /**
     * Test getU() method with a valid SingularValueDecompositionImpl object.
     */
    @Test
    public void testGetU() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}});
        SingularValueDecompositionImpl svd = null;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            fail("Constructor should not throw InvalidMatrixException with a valid matrix.");
        }
        RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{{0.632455532233624, 0.766044435027022},
                {-0.766044435027022, 0.632455532233624}});
        assertArrayEquals("U matrix should be correct.", expectedU.getData(), svd.getU().getData(), 0.0001);
    }

    /**
     * Test getUT() method with a valid SingularValueDecompositionImpl object.
     */
    @Test
    public void testGetUT() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}});
        SingularValueDecompositionImpl svd = null;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            fail("Constructor should not throw InvalidMatrixException with a valid matrix.");
        }
        RealMatrix expectedUt = new Array2DRowRealMatrix(new double[][]{{0.632455532233624, -0.766044435027022},
                {0.766044435027022, 0.632455532233624}});
        assertArrayEquals("UT matrix should be correct.", expectedUt.getData(), svd.getUT().getData(), 0.0001);
    }

    /**
     * Test getS() method with a valid SingularValueDecompositionImpl object.
     */
    @Test
    public void testGetS() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}});
        SingularValueDecompositionImpl svd = null;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            fail("Constructor should not throw InvalidMatrixException with a valid matrix.");
        }
        RealMatrix expectedS = new Array2DRowRealMatrix(new double[][]{{4.359563961229833, 0}, {0, 5.47722557506103}});
        assertArrayEquals("S matrix should be correct.", expectedS.getData(), svd.getS().getData(), 0.0001);
    }

    /**
     * Test getSingularValues() method with a valid SingularValueDecompositionImpl object.
     */
    @Test
    public void testGetSingularValues() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}});
        SingularValueDecompositionImpl svd = null;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            fail("Constructor should not throw InvalidMatrixException with a valid matrix.");
        }
        double[] expectedSingularValues = {4.359563961229833, 5.47722557506103};
        assertArrayEquals("Singular values should be correct.", expectedSingularValues, svd.getSingularValues(), 0.0001);
    }

    /**
     * Test getV() method with a valid SingularValueDecompositionImpl object.
     */
    @Test
    public void testGetV() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}});
        SingularValueDecompositionImpl svd = null;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            fail("Constructor should not throw InvalidMatrixException with a valid matrix.");
        }
        RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{{-0.408248290463863, 0.816496580927726},
                {0.816496580927726, -0.408248290463863}});
        assertArrayEquals("V matrix should be correct.", expectedV.getData(), svd.getV().getData(), 0.0001);
    }

    /**
     * Test getVT() method with a valid SingularValueDecompositionImpl object.
     */
    @Test
    public void testGetVT() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}});
        SingularValueDecompositionImpl svd = null;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            fail("Constructor should not throw InvalidMatrixException with a valid matrix.");
        }
        RealMatrix expectedVt = new Array2DRowRealMatrix(new double[][]{{-0.408248290463863, 0.816496580927726},
                {0.816496580927726, -0.408248290463863}});
        assertArrayEquals("VT matrix should be correct.", expectedVt.getData(), svd.getVT().getData(), 0.0001);
    }

    /**
     * Test getCovariance() method with a valid SingularValueDecompositionImpl object and a given singular value.
     */
    @Test
    public void testGetCovariance() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}});
        SingularValueDecompositionImpl svd = null;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            fail("Constructor should not throw InvalidMatrixException with a valid matrix.");
        }
        double minSingularValue = 2;
        RealMatrix expectedCovariance = new Array2DRowRealMatrix(new double[][]{{0.376991178033612, 0.376991178033612},
                {0.376991178033612, 0.376991178033612}});
        RealMatrix covariance = svd.getCovariance(minSingularValue);
        assertArrayEquals("Covariance matrix should be correct.", expectedCovariance.getData(), covariance.getData(), 0.0001);
    }

    /**
     * Test getNorm() method with a valid SingularValueDecompositionImpl object.
     */
    @Test
    public void testGetNorm() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}});
        SingularValueDecompositionImpl svd = null;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            fail("Constructor should not throw InvalidMatrixException with a valid matrix.");
        }
        double expectedNorm = 4.359563961229833;
        double norm = svd.getNorm();
        assertEquals("Norm should be correct.", expectedNorm, norm, 0.0001);
    }

    /**
     * Test getConditionNumber() method with a valid SingularValueDecompositionImpl object.
     */
    @Test
    public void testGetConditionNumber() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}});
        SingularValueDecompositionImpl svd = null;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            fail("Constructor should not throw InvalidMatrixException with a valid matrix.");
        }
        double expectedConditionNumber = 4.359563961229833 / 5.47722557506103;
        double conditionNumber = svd.getConditionNumber();
        assertEquals("Condition number should be correct.", expectedConditionNumber, conditionNumber, 0.0001);
    }

    /**
     * Test getRank() method with a valid SingularValueDecompositionImpl object.
     */
    @Test
    public void testGetRank() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}});
        SingularValueDecompositionImpl svd = null;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            fail("Constructor should not throw InvalidMatrixException with a valid matrix.");
        }
        int expectedRank = 2;
        int rank = svd.getRank();
        assertEquals("Rank should be correct.", expectedRank, rank);
    }

    /**
     * Test solve() method with a valid SingularValueDecompositionImpl object and a valid right-hand side.
     */
    @Test
    public void testSolveWithValidRightHandSide() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}});
        SingularValueDecompositionImpl svd = null;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            fail("Constructor should not throw InvalidMatrixException with a valid matrix.");
        }
        double[] rightHandSide = {2, 3};
        double[] expectedSolution = {-0.166666666666667, 0.166666666666667};
        double[] solution = svd.getSolver().solve(rightHandSide);
        assertArrayEquals("Solution should be correct.", expectedSolution, solution, 0.0001);
    }

    /**
     * Test solve() method with a valid SingularValueDecompositionImpl object and a singular right-hand side.
     */
    @Test
    public void testSolveWithSingularRightHandSide() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}});
        SingularValueDecompositionImpl svd = null;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            fail("Constructor should not throw InvalidMatrixException with a valid matrix.");
        }
        double[] rightHandSide = {0, 0};
        double[] expectedSolution = {Double.NaN, Double.NaN};
        double[] solution = svd.getSolver().solve(rightHandSide);
        assertArrayEquals("Solution should be correct.", expectedSolution, solution, 0.0001);
    }

}