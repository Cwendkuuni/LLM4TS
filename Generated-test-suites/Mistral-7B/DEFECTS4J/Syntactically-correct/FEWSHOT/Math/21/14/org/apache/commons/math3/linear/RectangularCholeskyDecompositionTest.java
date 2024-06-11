package org.apache.commons.math3.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testEmptyDecomposition() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[0][0]);
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            assertFalse("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testIdentityMatrixDecomposition() {
        RealMatrix matrix = MatrixUtils.createIdentityMatrix(3);
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
            assertEquals(matrix, decomposition.getRootMatrix().transposeMultiply(decomposition.getRootMatrix()));
            assertEquals(3, decomposition.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            assertFalse("Unexpected NonPositiveDefiniteMatrixException");
        }
    }

    @Test
    public void testSingularValueDecomposition() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 1.0},
            {1.0, 1.0}
        });
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            assertFalse("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testNonPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0},
            {2.0, -1.0}
        });
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            assertFalse("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
            assertEquals(2.0, e.getDiagonalElement(), Precision.EPSILON);
            assertEquals(1, e.getRow());
            assertEquals(0.0, e.getSmall());
        }
    }

    @Test
    public void testDecomposition() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0, 3.0},
            {2.0, 5.0, 7.0},
            {3.0, 7.0, 12.0}
        });
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1.0);
            RealMatrix root = decomposition.getRootMatrix();
            assertEquals(3, root.getRowDimension());
            assertEquals(3, root.getColumnDimension());
            assertArrayEquals(new double[][]{
                    {4.0449587362829393, 0.0, 0.0},
                    {1.4142137410599674, 2.645751312225806, 0.0},
                    {0.0, 1.5811388300841898, 3.3166247903554245}
            }, root.getData());
            assertEquals(3, decomposition.getRank());
            RealMatrix product = root.transposeMultiply(root);
            RealVector testVector = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
            RealVector resultVector = root.operate(testVector);
            RealVector expectedVector = MatrixUtils.createRealVector(product.operate(testVector));
            assertEquals(expectedVector, resultVector, 1e-12);
        } catch (NonPositiveDefiniteMatrixException e) {
            assertFalse("Unexpected NonPositiveDefiniteMatrixException");
        }
    }

}