package org.apache.commons.math.linear;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.Solver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link SingularValueDecompositionImpl} class.
 */
public class SingularValueDecompositionImplTest {

    private static final double EPSILON = 1e-15;
    private RealMatrix matrix;
    private SingularValueDecomposition decomposable;
    private SingularValueDecompositionImpl svd;
    private RealMatrix U;
    private RealMatrix VT;

    @Before
    public void setUp() {
        matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0}, {1.0, 1.0}});
        decomposable = new SingularValueDecomposition(matrix);
        svd = new SingularValueDecompositionImpl(matrix);
    }

    /**
     * Test method getU().
     */
    @Test
    public void testGetU() {
        try {
            RealMatrix expected = decomposable.getU();
            RealMatrix actual = svd.getU();

            Assert.assertArrayEquals(expected.getData(), actual.getData(), EPSILON);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method getUT().
     */
    @Test
    public void testGetUT() {
        try {
            RealMatrix expected = decomposable.getUT();
            RealMatrix actual = svd.getUT();

            Assert.assertArrayEquals(expected.getData(), actual.getData(), EPSILON);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method getS().
     */
    @Test
    public void testGetS() {
        try {
            double[] expected = decomposable.getSingularValues().toArray();
            double[] actual = svd.getSingularValues();

            Assert.assertArrayEquals(expected, actual, EPSILON);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method getV().
     */
    @Test
    public void testGetV() {
        try {
            RealMatrix expected = decomposable.getV();
            RealMatrix actual = svd.getV();

            Assert.assertArrayEquals(expected.getData(), actual.getData(), EPSILON);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method getVT().
     */
    @Test
    public void testGetVT() {
        try {
            RealMatrix expected = decomposable.getVT();
            RealMatrix actual = svd.getVT();

            Assert.assertArrayEquals(expected.getData(), actual.getData(), EPSILON);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method getCovariance().
     */
    @Test
    public void testGetCovariance() {
        double minSingularValue = 1e-8;
        try {
            RealMatrix actual = svd.getCovariance(minSingularValue);
            RealMatrix expected = decomposable.getVT().multiply(
                    decomposable.getS().transpose()).multiply(
                            decomposable.getV().transpose()).multiply(
                                    new Array2DRowRealMatrix(new double[][]{
                                            {1.0, 0.0},
                                            {0.0, 1.0}}));

            Assert.assertEquals(expected, actual);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method getNorm().
     */
    @Test
    public void testGetNorm() {
        try {
            double expected = Math.sqrt(decomposable.getSingularValues()[0] *
                    decomposable.getSingularValues()[0] +
                    decomposable.getSingularValues()[1] *
                    decomposable.getSingularValues()[1]);
            double actual = svd.getNorm();

            Assert.assertEquals(expected, actual, EPSILON);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method getConditionNumber().
     */
    @Test
    public void testGetConditionNumber() {
        try {
            double expected = decomposable.getSingularValues()[0] /
                    decomposable.getSingularValues()[1];
            double actual = svd.getConditionNumber();

            Assert.assertEquals(expected, actual, EPSILON);
        } catch (InvalidMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method getRank().
     */
    @Test
    public void testGetRank() {
        try {
            int expected = decomposable.getRank();
            int actual = svd.getRank();

            Assert.assertEquals(expected, actual);
        } catch (IllegalStateException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method getSolver().
     */
    @Test
    public void testGetSolver() {
        double[] b = new double[]{1.0, 1.0};
        DecompositionSolver expected = decomposable.getSolver();
        Solver actual = svd.getSolver();

        try {
            RealVector x1 = expected.solve(new Array2DRowRealMatrix(b, false));
            RealVector x2 = actual.solve(new Array2DRowRealMatrix(b, false));

            Assert.assertEquals(x1.toArray(), x2.toArray(), EPSILON);
        } catch (IllegalArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        try {
            RealVector x1 = expected.solve(new Array2DRowRealMatrix(new double[][]{
                    {1.0, 1.0}, {1.0, 1.0}}, false));
            RealVector x2 = actual.solve(new Array2DRowRealMatrix(new double[][]{
                    {1.0, 1.0}, {1.0, 1.0}}, false));

            Assert.assertEquals(x1.toArray(), x2.toArray(), EPSILON);
        } catch (IllegalArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        try {
            RealMatrix x1 = expected.solve(matrix);
            RealMatrix x2 = actual.solve(matrix);

            Assert.assertEquals(x1.getData(), x2.getData(), EPSILON);
        } catch (IllegalArgumentException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test exception handling for getSolver() when the decomposed matrix is not non-singular.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testGetSolverException() {
        svd = new SingularValueDecompositionImpl(new Array2DRowRealMatrix(
                new double[][]{{0.0, 0.0}, {0.0, 0.0}}));
        svd.getSolver();
    }
}