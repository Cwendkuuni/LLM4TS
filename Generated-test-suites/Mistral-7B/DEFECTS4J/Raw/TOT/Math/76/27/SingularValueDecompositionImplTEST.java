package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.Solver;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link SingularValueDecompositionImpl}.
 */
public class SingularValueDecompositionImplTest {

    /**
     * Test method for {@link SingularValueDecompositionImpl#SingularValueDecompositionImpl(RealMatrix)}
     * with valid input.
     */
    @Test
    public void testValidInput() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { { 1, 2 }, { 3, 4 } });
        SingularValueDecompositionImpl svd = null;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        RealMatrix expectedU = new Array2DRowRealMatrix(new double[][] { { 0.31622776601683796, 0.9132439413965213 },
            { -0.9132439413965213, 0.31622776601683796 } });
        RealMatrix expectedV = new Array2DRowRealMatrix(new double[][] { { 0.4494897427831726, 0.8944271804621473 },
            { -0.8944271804621473, 0.4494897427831726 } });
        Assert.assertEquals("U is not as expected.", expectedU, u);
        Assert.assertEquals("S is not as expected.", new Array2DRowRealMatrix(new double[][] { { 0.0, 1.4142137410599656 },
            { 1.4142137410599656, 0.0 } }), s);
        Assert.assertEquals("V is not as expected.", expectedV, v);
        Solver solver = svd.getSolver();
        RealVector b = new Array2DRowRealVector(new double[] { 1, 1 });
        RealVector x = solver.solve(b);
        Assert.assertEquals("Solution is not as expected.", new Array2DRowRealVector(new double[] { 0.8944271804621473,
            0.4494897427831726 }), x);
    }

    /**
     * Test method for {@link SingularValueDecompositionImpl#SingularValueDecompositionImpl(RealMatrix, int)}
     * with valid input.
     */
    @Test
    public void testValidInputWithMax() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { { 1, 2 }, { 3, 4 } });
        SingularValueDecompositionImpl svd = null;
        try {
            svd = new SingularValueDecompositionImpl(matrix, 1);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        RealMatrix expectedU = new Array2DRowRealMatrix(new double[][] { { 0.31622776601683796, 0.9132439413965213 },
            { -0.9132439413965213, 0.31622776601683796 } });
        RealMatrix expectedS = new Array2DRowRealMatrix(new double[][] { { 0.0 } });
        RealMatrix expectedV = new Array2DRowRealMatrix(new double[][] { { 0.4494897427831726 } });
        Assert.assertEquals("U is not as expected.", expectedU, u);
        Assert.assertEquals("S is not as expected.", expectedS, s);
        Assert.assertEquals("V is not as expected.", expectedV, v);
        Solver solver = svd.getSolver();
        RealVector b = new Array2DRowRealVector(new double[] { 1, 1 });
        RealVector x = solver.solve(b);
        Assert.assertEquals("Solution is not as expected.", new Array2DRowRealVector(new double[] { 0.4494897427831726 }), x);
    }

    /**
     * Test method for {@link SingularValueDecompositionImpl#SingularValueDecompositionImpl(RealMatrix, int)}
     * with invalid input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInputWithMax() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { { 1, 2 }, { 3, 4 } });
        SingularValueDecompositionImpl svd = null;
        try {
            svd = new SingularValueDecompositionImpl(matrix, -1);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link SingularValueDecompositionImpl#getSolver()} with valid input.
     */
    @Test
    public void testGetSolver() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { { 1, 2 }, { 3, 4 } });
        SingularValueDecompositionImpl svd = null;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        Solver solver = svd.getSolver();
        RealVector b = new Array2DRowRealVector(new double[] { 1, 1 });
        RealVector x = solver.solve(b);
        Assert.assertEquals("Solution is not as expected.", new Array2DRowRealVector(new double[] { 0.8944271804621473,
            0.4494897427831726 }), x);
        Assert.assertTrue("Solver is not non-singular.", solver.isNonSingular());
        RealMatrix inv = solver.getInverse();
        RealMatrix expectedInv = new Array2DRowRealMatrix(new double[][] { { 0.5735999999999999, -0.8181818181818181 },
            { 0.8181818181818181, 0.5735999999999999 } });
        Assert.assertEquals("Inverse is not as expected.", expectedInv, inv);
    }

    /**
     * Test method for {@link SingularValueDecompositionImpl#getSolver()} with singular matrix.
     */
    @Test
    public void testGetSolverSingular() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { { 0, 0 } });
        SingularValueDecompositionImpl svd = null;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        Solver solver = svd.getSolver();
        RealVector b = new Array2DRowRealVector(new double[] { 1, 1 });
        RealVector x = solver.solve(b);
        Assert.assertNull("Solution is not null.", x);
        Assert.assertFalse("Solver is non-singular.", solver.isNonSingular());
    }

    /**
     * Test method for {@link SingularValueDecompositionImpl#getRank()} with valid input.
     */
    @Test
    public void testGetRank() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { { 1, 2 }, { 3, 4 } });
        SingularValueDecompositionImpl svd = null;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
        int rank = svd.getRank();
        Assert.assertEquals("Rank is not expected.", 2, rank);
        matrix = new Array2DRowRealMatrix(new double[][] { { 1, 2 }, { 3, 0 } });
        SingularValueDecompositionImpl svd2 = null;
        try {
            svd2 = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
        rank = svd2.getRank();
        Assert.assertEquals("Rank is not expected.", 1, rank);
    }

    /**
     * Test method for {@link SingularValueDecompositionImpl#getCovariance(double)} with valid input.
     */
    @Test
    public void testGetCovariance() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] { { 1, 2 }, { 3, 4 } });
        SingularValueDecompositionImpl svd = null;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
        RealMatrix covariance = svd.getCovariance(1e-6);
        RealMatrix expectedCovariance = new Array2DRowRealMatrix(new double[][] { { 1.7320508075688772, 1.0 },
            { 1.0, 1.7320508075688772 } });
        Assert.assertEquals("Covariance is not as expected.", expectedCovariance, covariance);
    }

}