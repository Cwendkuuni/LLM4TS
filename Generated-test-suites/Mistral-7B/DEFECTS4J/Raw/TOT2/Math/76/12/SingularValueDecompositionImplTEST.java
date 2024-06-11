package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.Solver;
import org.junit.Assert;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    /**
     * Test constructor with valid input.
     */
    @Test
    public void testValidInput() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl svd;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            throw new RuntimeException(e);
        }
        Assert.assertNotNull(svd);
    }

    /**
     * Test constructor with invalid input (singular matrix).
     */
    @Test(expected = InvalidMatrixException.class)
    public void testSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        new SingularValueDecompositionImpl(matrix);
    }

    /**
     * Test getU() method for valid input.
     */
    @Test
    public void testGetU() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl svd;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            throw new RuntimeException(e);
        }
        RealMatrix u = svd.getU();
        Assert.assertNotNull(u);
        RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{{0.5413024965, 0.8314696133},
                {-0.8314696133, 0.5413024965}});
        Assert.assertEquals(expectedU, u);
    }

    /**
     * Test getUT() method for valid input.
     */
    @Test
    public void testGetUT() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl svd;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            throw new RuntimeException(e);
        }
        RealMatrix ut = svd.getUT();
        Assert.assertNotNull(ut);
        RealMatrix expectedUt = new Array2DRowRealMatrix(new double[][]{{0.5413024965, -0.8314696133},
                {0.8314696133, 0.5413024965}});
        Assert.assertEquals(expectedUt, ut);
    }

    /**
     * Test getS() method for valid input.
     */
    @Test
    public void testGetS() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl svd;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            throw new RuntimeException(e);
        }
        RealMatrix s = svd.getS();
        Assert.assertNotNull(s);
        RealMatrix expectedS = new Array2DRowRealMatrix(new double[][]{{2.828427125}, {0.0}, {0.0}});
        Assert.assertEquals(expectedS, s);
    }

    /**
     * Test getSingularValues() method for valid input.
     */
    @Test
    public void testGetSingularValues() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl svd;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            throw new RuntimeException(e);
        }
        double[] singularValues = svd.getSingularValues();
        Assert.assertNotNull(singularValues);
        Assert.assertEquals(2, singularValues.length);
        Assert.assertEquals(2.828427125, singularValues[0], 0.001);
        Assert.assertEquals(0.0, singularValues[1], 0.001);
    }

    /**
     * Test getV() method for valid input.
     */
    @Test
    public void testGetV() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl svd;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            throw new RuntimeException(e);
        }
        RealMatrix v = svd.getV();
        Assert.assertNotNull(v);
        RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{{-0.2755908577, -0.9396926228},
                {-0.9396926228, 0.2755908577}});
        Assert.assertEquals(expectedV, v);
    }

    /**
     * Test getVT() method for valid input.
     */
    @Test
    public void testGetVT() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl svd;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            throw new RuntimeException(e);
        }
        RealMatrix vt = svd.getVT();
        Assert.assertNotNull(vt);
        RealMatrix expectedVt = new Array2DRowRealMatrix(new double[][]{{-0.2755908577, -0.9396926228},
                {-0.9396926228, 0.2755908577}});
        Assert.assertEquals(expectedVt, vt);
    }

    /**
     * Test getCovariance() method for valid input.
     */
    @Test
    public void testGetCovariance() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl svd;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            throw new RuntimeException(e);
        }
        RealMatrix covariance = svd.getCovariance(0.1);
        Assert.assertNotNull(covariance);
        RealMatrix expectedCovariance = new Array2DRowRealMatrix(new double[][]{{0.8090168945, 0.6219458348},
                {0.6219458348, 0.8090168945}});
        Assert.assertEquals(expectedCovariance, covariance);
    }

    /**
     * Test getNorm() method for valid input.
     */
    @Test
    public void testGetNorm() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl svd;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            throw new RuntimeException(e);
        }
        double norm = svd.getNorm();
        Assert.assertEquals(2.828427125, norm, 0.001);
    }

    /**
     * Test getConditionNumber() method for valid input.
     */
    @Test
    public void testGetConditionNumber() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl svd;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            throw new RuntimeException(e);
        }
        double conditionNumber = svd.getConditionNumber();
        Assert.assertEquals(2.828427125 / 0.0, conditionNumber, 0.001);
    }

    /**
     * Test getRank() method for valid input.
     */
    @Test
    public void testGetRank() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl svd;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            throw new RuntimeException(e);
        }
        int rank = svd.getRank();
        Assert.assertEquals(2, rank);
    }

    /**
     * Test getSolver() method for valid input.
     */
    @Test
    public void testGetSolver() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl svd;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            throw new RuntimeException(e);
        }
        Solver solver = svd.getSolver();
        Assert.assertNotNull(solver);
    }

    /**
     * Test solve() method for valid input.
     */
    @Test
    public void testSolve() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl svd;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            throw new RuntimeException(e);
        }
        double[] b = new double[]{2.0, 3.0};
        double[] x = svd.getSolver().solve(b);
        double[] expectedX = new double[]{2.644875433, 3.115238532};
        Assert.assertArrayEquals(expectedX, x, 0.001);
    }

    /**
     * Test solve() method for singular matrix.
     */
    @Test
    public void testSolveSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        SingularValueDecompositionImpl svd;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            throw new RuntimeException(e);
        }
        double[] b = new double[]{1.0, 1.0};
        try {
            svd.getSolver().solve(b);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Test getInverse() method for valid input.
     */
    @Test
    public void testGetInverse() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl svd;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            throw new RuntimeException(e);
        }
        RealMatrix inverse = svd.getSolver().getInverse();
        Assert.assertNotNull(inverse);
        RealMatrix expectedInverse = new Array2DRowRealMatrix(new double[][]{{0.373583797, -0.491234523},
                {-0.491234523, 0.373583797}});
        Assert.assertEquals(expectedInverse, inverse);
    }

    /**
     * Test getInverse() method for singular matrix.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetInverseSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        SingularValueDecompositionImpl svd;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            throw new RuntimeException(e);
        }
        svd.getSolver().getInverse();
    }
}