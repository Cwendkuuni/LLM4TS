package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test cases for SingularValueDecompositionImpl class.
 */
public class SingularValueDecompositionImplTest {

    /**
     * Test constructor with valid input.
     */
    @Test
    public void testSingularValueDecompositionImplConstructionWithValidInput() {
        RealMatrix matrix = new Array2DRowRealMatrix(
            new double[][] { {1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0} });
        SingularValueDecompositionImpl svd = null;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            // should not be thrown
            fail("InvalidMatrixException should not be thrown");
        }
        assertNotNull(svd);
    }

    /**
     * Test constructor with invalid input.
     */
    @Test(expected = InvalidMatrixException.class)
    public void testSingularValueDecompositionImplConstructionWithInvalidInput() {
        RealMatrix matrix = new Array2DRowRealMatrix(
            new double[][] { {1.0, 2.0}, {4.0, 5.0} });
        new SingularValueDecompositionImpl(matrix);
    }

    /**
     * Test getU() method with valid input.
     */
    @Test
    public void testGetUWithValidInput() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(
            new double[][] { {1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0} });
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix expectedU = new Array2DRowRealMatrix(new double[][] {
            {-0.57394584, -0.81649663, -0.22293374},
            {-0.81649663, -0.57394584, -0.22293374},
            {-0.22293374, -0.22293374, 0.82555295}
        });
        RealMatrix actualU = svd.getU();
        assertEquals(expectedU, actualU);
    }

    /**
     * Test getUT() method with valid input.
     */
    @Test
    public void testGetUTWithValidInput() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(
            new double[][] { {1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0} });
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix expectedUt = new Array2DRowRealMatrix(new double[][] {
            {-0.57394584, -0.81649663, -0.22293374},
            {-0.81649663, -0.57394584, -0.22293374},
            {-0.22293374, -0.22293374, 0.82555295}
        });
        RealMatrix actualUt = svd.getUT();
        assertEquals(expectedUt, actualUt.transpose());
    }

    /**
     * Test getS() method with valid input.
     */
    @Test
    public void testGetSWithValidInput() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(
            new double[][] { {1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0} });
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double[] expectedSingularValues = {8.48529646, 6.48074070, 0.0};
        double[] actualSingularValues = svd.getSingularValues();
        assertArrayEquals(expectedSingularValues, actualSingularValues, 0.001);
    }

    /**
     * Test getV() method with valid input.
     */
    @Test
    public void testGetVWithValidInput() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(
            new double[][] { {1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0} });
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix expectedV = new Array2DRowRealMatrix(new double[][] {
            {-0.08921361, -0.97146844, 0.24390243},
            {-0.97146844, -0.08921361, -0.24390243},
            {0.24390243, 0.24390243, 0.86595871}
        });
        RealMatrix actualV = svd.getV();
        assertEquals(expectedV, actualV);
    }

    /**
     * Test getVT() method with valid input.
     */
    @Test
    public void testGetVTWithValidInput() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(
            new double[][] { {1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0} });
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix expectedVt = new Array2DRowRealMatrix(new double[][] {
            {-0.08921361, -0.97146844, 0.24390243},
            {-0.97146844, -0.08921361, 0.24390243},
            {0.24390243, 0.24390243, 0.86595871}
        });
        RealMatrix actualVt = svd.getVT();
        assertEquals(expectedVt, actualVt.transpose());
    }

    /**
     * Test getCovariance() method with valid input.
     */
    @Test
    public void testGetCovarianceWithValidInput() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(
            new double[][] { {1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0} });
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix covariance = svd.getCovariance(5);
        RealMatrix expectedCovariance = new Array2DRowRealMatrix(
            new double[][] { {0.06301402, 0.06226045, 0.06184975},
                              {0.06226045, 0.06301402, 0.06184975},
                              {0.06184975, 0.06184975, 0.10237593} });
        assertEquals(expectedCovariance, covariance);
    }

    /**
     * Test getNorm() method with valid input.
     */
    @Test
    public void testGetNormWithValidInput() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(
            new double[][] { {1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0} });
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double expectedNorm = 8.48529646;
        double actualNorm = svd.getNorm();
        assertEquals(expectedNorm, actualNorm, 0.001);
    }

    /**
     * Test getConditionNumber() method with valid input.
     */
    @Test
    public void testGetConditionNumberWithValidInput() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(
            new double[][] { {1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0} });
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double expectedConditionNumber = 8.48529646 / 0.0;
        double actualConditionNumber = svd.getConditionNumber();
        assertEquals(expectedConditionNumber, actualConditionNumber, 0.001);
    }

    /**
     * Test getRank() method with valid input.
     */
    @Test
    public void testGetRankWithValidInput() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(
            new double[][] { {1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0} });
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        int expectedRank = 3;
        int actualRank = svd.getRank();
        assertEquals(expectedRank, actualRank);
    }

    /**
     * Test getSolver() method with valid input.
     */
    @Test
    public void testGetSolverWithValidInput() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(
            new double[][] { {1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0} });
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = svd.getSolver();
        // test pseudo inverse
        double[] b = new double[] {1.0, 2.0, 3.0};
        double[] x = solver.solve(b);
        double[] expectedX = new double[] {1.45642338, 0.83942822, 0.32941176};
        assertArrayEquals(expectedX, x, 0.001);
        // test non-singularity
        RealMatrix m = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}, {11.0, 12.0, 13.0} });
        SingularValueDecompositionImpl svd2 = new SingularValueDecompositionImpl(m);
        DecompositionSolver solver2 = svd2.getSolver();
        boolean nonSingular = solver2.isNonSingular();
        assertTrue(nonSingular);
        // test inverse
        RealMatrix inverse = solver2.getInverse();
        RealMatrix expectedInverse = new Array2DRowRealMatrix(new double[][] {
            {0.31244931, -0.26844449, 0.05248651},
            {-0.26844449, 0.31244931, -0.05248651},
            {-0.05248651, -0.05248651, 0.72345154}
        });
        assertEquals(expectedInverse, inverse);
    }
}