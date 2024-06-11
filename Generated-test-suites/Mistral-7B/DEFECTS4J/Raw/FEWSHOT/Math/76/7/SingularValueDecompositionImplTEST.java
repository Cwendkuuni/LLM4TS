package org.apache.commons.cli;

import org.apache.commons.math3.linear.*;
import org.junit.Assert.*;
import org.junit.Test;

import static org.apache.commons.math3.linear.MatrixUtils.createRealDiagonalMatrix;
import static org.apache.commons.math3.linear.MatrixUtils.createRealMatrix;
import static org.apache.commons.math3.linear.MatrixUtils.getIdentityMatrix;
import static org.apache.commons.math3.linear.RealVectorUtils.createRealVector;
import static org.apache.commons.math3.linear.RealVectorUtils.createRealVectorValue;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecomposition() {
        RealMatrix matrix = createMatrix(new double[][]{
            {1.0, 2.0},
            {3.0, 4.0}
        });

        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            RealMatrix u = svd.getU();
            RealMatrix s = svd.getS();
            RealMatrix v = svd.getV();
            RealMatrix ut = svd.getUT();
            RealMatrix st = svd.getS().transpose();
            RealMatrix vt = svd.getVT();

            RealMatrix expectedU = getIdentityMatrix(2);
            RealMatrix expectedV = getIdentityMatrix(2);
            RealMatrix expectedS = createRealDiagonalMatrix(new double[]{1.0, 1.54308063482086});

            Assert.assertArrayEquals(expectedU.getData(), u.getData(), 0.0);
            Assert.assertArrayEquals(expectedS.getData(), s.getData(), 0.0);
            Assert.assertArrayEquals(expectedV.getData(), v.getData(), 0.0);
            Assert.assertArrayEquals(expectedS.getData(), st.getData(), 0.0);
            Assert.assertArrayEquals(expectedV.getData(), vt.getData(), 0.0);

            RealMatrix productUSV = u.multiply(s).multiply(v);
            Assert.assertEquals(matrix, productUSV, 0.0);
            Assert.assertEquals(productUSV.transpose(), ut.multiply(st).multiply(vt), 0.0);

        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = createMatrix(new double[][]{
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0},
            {7.0, 8.0, 9.0}
        });

        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
            int rank = svd.getRank();
            assertEquals(2, rank);

        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetCovariance() {
        RealMatrix matrix = createMatrix(new double[][]{
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0},
            {7.0, 8.0, 9.0}
        });

        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 1.0);
            RealMatrix covariance = svd.getCovariance(1.0);

            RealMatrix expectedCovariance = new Array2DRowRealMatrix(
                new double[][]{
                    {5.0, 1.64843846153846},
                    {1.64843846153846, 2.96852535853035}
                }, false);

            Assert.assertArrayEquals(expectedCovariance.getData(), covariance.getData(), 0.0);

        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetNorm() {
        RealMatrix matrix = createMatrix(new double[][]{
            {1.0, 2.0},
            {3.0, 4.0}
        });

        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            double norm = svd.getNorm();
            assertEquals(Math.sqrt(15.0), norm, 0.0);

        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetConditionNumber() {
        RealMatrix matrix = createMatrix(new double[][]{
            {1.0, 2.0},
            {3.0, 4.0}
        });

        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            double conditionNumber = svd.getConditionNumber();
            assertEquals(15.0 / 1.0, conditionNumber, 0.0);

        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    private RealMatrix createMatrix(double[][] data) {
        return new Array2DRowRealMatrix(data, false);
    }
}