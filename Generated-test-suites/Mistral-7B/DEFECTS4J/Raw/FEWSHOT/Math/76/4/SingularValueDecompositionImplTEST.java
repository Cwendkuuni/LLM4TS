package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math3.linear.*;

import org.junit.Test;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImpl() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix U = svd.getU();
        RealMatrix S = svd.getS();
        RealMatrix V = svd.getV();
        RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{{0.39295223, 0.33721233, 0.88859275}, {0.62078484, -0.74731544, -0.40828457}, {-0.78252585, 0.46023951, -0.38642274}});
        RealMatrix expectedS = new Array2DRowRealMatrix(new double[][]{ {15.430304, 0.0, 0.0}, {0.0, 8.164966, 0.0}, {0.0, 0.0, 1.414214} });
        RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{{0.40828457, -0.38642274, -0.78252585}, {-0.38642274, 0.81649662, 0.40828457}, {0.78252585, 0.46023951, 0.33721233}});
        RealMatrix ut = svd.getUT();
        RealMatrix st = svd.getS();
        RealMatrix vt = svd.getVT();

        assertEquals(expectedU, U);
        assertEquals(expectedS, S);
        assertEquals(expectedV, V);
        assertEquals(expectedU, ut.transpose());
        assertEquals(expectedS, st);
        assertEquals(expectedV, vt.transpose());
    }

    @Test
    public void testSingularValueDecompositionImpl_SingularMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix U = svd.getU();
        RealMatrix S = svd.getS();
        RealMatrix V = svd.getV();
        RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{{0.70710678, 0.70710678}, {-0.70710678, 0.70710678}});
        RealMatrix expectedS = new Array2DRowRealMatrix(new double[][]{ {1.4142136, 0.0}, {0.0, 0.0} });
        RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{{0.70710678, -0.70710678}, {-0.70710678, 0.70710678}});
        RealMatrix ut = svd.getUT();
        RealMatrix st = svd.getS();
        RealMatrix vt = svd.getVT();

        assertEquals(expectedU, U);
        assertEquals(expectedS, S);
        assertEquals(expectedV, V);
        assertEquals(expectedU, ut.transpose());
        assertEquals(expectedS, st);
        assertEquals(expectedV, vt.transpose());

        try {
            svd.getRank();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testSingularValueDecompositionImpl_ZeroMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[0][0]);
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        try {
            svd.getU();
            fail("Expected InvalidMatrixException");
        } catch (InvalidMatrixException e1) {
            try {
                svd.getS();
                fail("Expected InvalidMatrixException");
            } catch (InvalidMatrixException e2) {
                try {
                    svd.getV();
                    fail("Expected InvalidMatrixException");
                } catch (InvalidMatrixException e3) {
                    // Expected
                }
            }
        }
    }

    @Test
    public void testSingularValueDecompositionImpl_EmptyMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        try {
            svd.getU();
            fail("Expected InvalidMatrixException");
        } catch (InvalidMatrixException e1) {
            try {
                svd.getS();
                fail("Expected InvalidMatrixException");
            } catch (InvalidMatrixException e2) {
                try {
                    svd.getV();
                    fail("Expected InvalidMatrixException");
                } catch (InvalidMatrixException e3) {
                    // Expected
                }
            }
        }
    }

    @Test
    public void testGetCovariance() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix covariance = svd.getCovariance(5.0);
        RealMatrix expectedCovariance = new Array2DRowRealMatrix(new double[][]{{0.4989855, 0.34443836, 0.1163561}, {0.34443836, 0.46491215, 0.1065561}, {0.1163561, 0.1065561, 0.2142136}});
        assertEquals(expectedCovariance, covariance);
    }

    @Test
    public void testGetCovariance_EmptyMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        try {
            svd.getCovariance(5.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetCovariance_SingularMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        try {
            svd.getCovariance(5.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetNorm() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double norm = svd.getNorm();
        double expectedNorm = Math.sqrt(15.430304);
        assertEquals(expectedNorm, norm, 1e-14);
    }

    @Test
    public void testGetConditionNumber() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double conditionNumber = svd.getConditionNumber();
        double expectedConditionNumber = Math.sqrt(15.430304) / Math.sqrt(1.414214);
        assertEquals(expectedConditionNumber, conditionNumber, 1e-14);
    }

    @Test
    public void testGetRank() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        int rank = svd.getRank();
        assertEquals(3, rank);
    }
}