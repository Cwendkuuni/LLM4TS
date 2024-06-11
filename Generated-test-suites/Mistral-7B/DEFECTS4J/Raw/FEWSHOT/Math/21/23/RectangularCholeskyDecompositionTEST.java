package org.apache.commons.math3.linear;

import org.apache.commons.math3.util.MatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.RealMatrix;
import org.apache.commons.math3.util.RealVector;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testDecomposition() {
        double small = 1e-7;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {2.0, 1.0, 0.0},
                {1.0, 2.0, 1.0},
                {0.0, 1.0, 1.0}
        });
        try {
            RectangularCholeskyDecomposition decomposed = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
                    {FastMath.sqrt(2.0), 0.0, 0.0},
                    {0.5, FastMath.sqrt(2.0), 1.0 / Math.sqrt(2.0)},
                    {0.0, -1.0 / Math.sqrt(2.0), FastMath.sqrt(2.0)}
            });
            Assert.assertEquals(expected, decomposed.getRootMatrix());
            Assert.assertEquals(expected.getRowDimension(), decomposed.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSmallEigenvalues() {
        double small = 1e-12;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0, 1.0},
                {1.0, 1.0, 1.0 + 1e-13, 1.0},
                {1.0, 1.0, 1.0, 1.0 + 1e-13}
        });
        try {
            RectangularCholeskyDecomposition decomposed = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{
                    {1.0, 0.0, 0.0, 0.0},
                    {0.7071067811865476, 0.7071067811865476, 0.0, 0.0},
                    {-0.7071067811865476, 0.7071067811865476, 1.0, 0.0},
                    {0.0, 0.0, -0.7071067811865476, 1.0}
            });
            Assert.assertEquals(expected, decomposed.getRootMatrix(), 1e-11);
            Assert.assertEquals(expected.getRowDimension(), decomposed.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSingularMatrix() {
        double small = 1e-6;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {1.0, 2.0, 3.0},
                {1.0, 2.0, 3.0}
        });
        try {
            RectangularCholeskyDecomposition decomposed = new RectangularCholeskyDecomposition(matrix, small);
            Assert.assertEquals(3, decomposed.getRank());
            RealVector v = new ArrayRealVector(new double[]{1.0, 1.0, 1.0});
            RealVector w = decomposed.getRootMatrix().operate(v);
            RealVector expected = new ArrayRealVector(new double[]{1.0, 1.0, 1.0});
            Assert.assertEquals(expected, w, 1e-6);
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testNonPositiveDefiniteMatrixException() {
        double small = 1e-6;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 1.0, 1.0},
                {1.0, -1.0, 1.0},
                {1.0, 1.0, -1.0}
        });
        try {
            new RectangularCholeskyDecomposition(matrix, small);
            Assert.fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.assertEquals(-1.0, Precision.round(e.getDiagonalValue(), 4));
            Assert.assertEquals(2, e.getRowIndex());
        }
    }
}