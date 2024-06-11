package org.apache.commons.math.linear;

import org.apache.commons.math3.exception.SingularValueDecompositionException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.Solver;
import org.apache.commons.math3.linear.VectorUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the SingularValueDecompositionImpl class.
 */
public class SingularValueDecompositionImplTest {

    /**
     * Test the SingularValueDecompositionImpl constructor with a valid matrix.
     */
    @Test
    public void testSingularValueDecompositionImplValidMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            { 1, 2, 3 },
            { 2, 3, 4 },
            { 3, 4, 5 }
        });
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            RealMatrix u = svd.getU();
            RealMatrix s = svd.getS();
            RealMatrix v = svd.getV();
            RealMatrix expectedU = new Array2DRowRealMatrix(new double[][] {
                {-0.4999999999999999, -0.894427190999916, 0.44948974278317},
                { 0.4999999999999999, 0.4472345474252248, -0.894427190999916},
                { 0, 0.894427190999916, 0.44948974278317}
            });
            RealMatrix expectedS = new Array2DRowRealMatrix(new double[][] {
                { 5.15812965541424, 0.0, 0.0 },
                { 0.0, 4.82842712474619, 0.0 },
                { 0.0, 0.0, 2.30611429107622 }
            });
            RealMatrix expectedV = new Array2DRowRealMatrix(new double[][] {
                {-0.4226182623769815, 0.7353981635964539, -0.4999999999999984},
                { 0.8164965809277264, 0.4886025270873157, 0.4999999999999984},
                { 0.4082482904638632, -0.4886025270873157, 0.4999999999999984}
            });
            Assert.assertEquals(expectedU, u);
            Assert.assertEquals(expectedS, s);
            Assert.assertEquals(expectedV, v);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test the SingularValueDecompositionImpl constructor with a singular matrix.
     */
    @Test
    public void testSingularValueDecompositionImplSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            { 1, 1, 0 },
            { 1, 1, 0 },
            { 0, 0, 0 }
        });
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            RealMatrix u = svd.getU();
            RealMatrix s = svd.getS();
            RealMatrix v = svd.getV();
            RealMatrix expectedU = new Array2DRowRealMatrix(new double[][] {
                { 0.6708203256774651, 0.5384602613640793, 0.5384602613640793 },
                {-0.5384602613640793,  0.6708203256774651, 0.5384602613640793 },
                {-0.5384602613640793, -0.5384602613640793, 0.6708203256774651 }
            });
            RealMatrix expectedS = new Array2DRowRealMatrix(new double[][] {
                { 0.0, 0.0, 0.0 },
                { 0.0, 0.0, 0.0 },
                { 0.0, 0.0, 0.0 }
            });
            RealMatrix expectedV = new Array2DRowRealMatrix(new double[][] {
                {-0.7071067811865475,  0.2928932188134524, -0.4082482904638632 },
                { 0.2928932188134524, -0.7071067811865475,  0.4082482904638632 },
                {-0.4082482904638632,  0.4082482904638632,  0.7071067811865475 }
            });
            Assert.assertEquals(expectedU, u);
            Assert.assertEquals(expectedS, s);
            Assert.assertEquals(expectedV, v);
        } catch (SingularValueDecompositionException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test the SingularValueDecompositionImpl constructor with a 1x1 matrix.
     */
    @Test
    public void testSingularValueDecompositionImplOneByOneMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            { 2 }
        });
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            RealMatrix u = svd.getU();
            RealMatrix s = svd.getS();
            RealMatrix v = svd.getV();
            Assert.assertEquals(VectorUtils.createRealVector(new double[] { 1.0 }), u);
            Assert.assertEquals(VectorUtils.createRealVector(new double[] { 2.0 }), s);
            Assert.assertEquals(VectorUtils.createRealVector(new double[] { 1.0 / Math.sqrt(2.0) }), v);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test the SingularValueDecompositionImpl constructor with an empty matrix.
     */
    @Test
    public void testSingularValueDecompositionImplEmptyMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            RealMatrix u = svd.getU();
            RealMatrix s = svd.getS();
            RealMatrix v = svd.getV();
            Assert.assertEquals(new Array2DRowRealMatrix(new double[][] {}), u);
            Assert.assertEquals(new Array2DRowRealMatrix(new double[][] {}), s);
            Assert.assertEquals(new Array2DRowRealMatrix(new double[][] {}), v);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test the getCovariance method.
     */
    @Test
    public void testSingularValueDecompositionImplGetCovariance() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            { 1, 2, 3 },
            { 2, 3, 4 },
            { 3, 4, 5 }
        });
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
            RealMatrix expectedCovariance = new Array2DRowRealMatrix(new double[][] {
                { 1.778113735244016, 1.5384560688263243 },
                { 1.5384560688263243, 1.778113735244016 }
            });
            RealMatrix covariance = svd.getCovariance(0.01);
            Assert.assertEquals(expectedCovariance, covariance);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test the getNorm method.
     */
    @Test
    public void testSingularValueDecompositionImplGetNorm() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            { 1, 2, 3 },
            { 2, 3, 4 },
            { 3, 4, 5 }
        });
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            double expectedNorm = Math.sqrt(14.0) * 5.0;
            double norm = svd.getNorm();
            Assert.assertEquals(expectedNorm, norm);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test the getConditionNumber method.
     */
    @Test
    public void testSingularValueDecompositionImplGetConditionNumber() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            { 1, 2, 3 },
            { 2, 3, 4 },
            { 3, 4, 5 }
        });
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            double expectedConditionNumber = Math.sqrt(14.0) * 5.0 / 2.0;
            double conditionNumber = svd.getConditionNumber();
            Assert.assertEquals(expectedConditionNumber, conditionNumber, 1e-12);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test the getRank method.
     */
    @Test
    public void testSingularValueDecompositionImplGetRank() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            { 1, 2, 3 },
            { 2, 3, 4 },
            { 3, 4, 5 }
        });
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            int expectedRank = 3;
            int rank = svd.getRank();
            Assert.assertEquals(expectedRank, rank);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        RealMatrix matrix2 = new Array2DRowRealMatrix(new double[][] {
            { 1, 2, 3 },
            { 2, 3, 0 },
            { 3, 4, 5 }
        });
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix2);
            int expectedRank = 2;
            int rank = svd.getRank();
            Assert.assertEquals(expectedRank, rank);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test the getSolver method.
     */
    @Test
    public void testSingularValueDecompositionImplGetSolver() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            { 1, 2, 3 },
            { 2, 3, 4 },
            { 3, 4, 5 }
        });
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            DecompositionSolver solver = svd.getSolver();
            Solver expectedSolver = new Solver(svd.getSingularValues().toArray(), svd.getUT(), svd.getV(), true);
            Assert.assertEquals(expectedSolver, solver);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }
}