package org.apache.commons.math3.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor_positiveDefiniteMatrix_smallThreshold() {
        double[][] matrixData = {
                {1.0, 2.0, 3.0},
                {2.0, 5.0, 6.0},
                {3.0, 6.0, 10.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        double smallThreshold = 0.0;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, smallThreshold);

        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();

        Assert.assertNotNull(root);
        Assert.assertEquals(3, rank);

        double[][] rootData = root.getData();
        assertEquals(3.0, Precision.doubleEquals(rootData[0][0], 1.0, 0.0001), 0.0);
        assertEquals(2.0 / 3.0, Precision.doubleEquals(rootData[0][1], 2.0 / 3.0, 0.0001), 0.0);
        assertEquals(-1.0 / 3.0, Precision.doubleEquals(rootData[0][2], -1.0 / 3.0, 0.0001), 0.0);
        assertEquals(Math.sqrt(5.0 / 3.0), Precision.doubleEquals(rootData[1][1], Math.sqrt(5.0 / 3.0), 0.0001), 0.0);
        assertEquals(1.0 / Math.sqrt(5.0), Precision.doubleEquals(rootData[1][2], 1.0 / Math.sqrt(5.0), 0.0001), 0.0);
        assertEquals(Math.sqrt(10.0 / 3.0), Precision.doubleEquals(rootData[2][2], Math.sqrt(10.0 / 3.0), 0.0001), 0.0);

        assertNull(decomposition.getRootMatrix().getEntry(2, 2));
        assertNull(decomposition.getRootMatrix().getEntry(2, 3));
        assertNull(decomposition.getRootMatrix().getEntry(3, 1));
        assertNull(decomposition.getRootMatrix().getEntry(3, 2));
    }

    @Test
    public void testConstructor_positiveDefiniteMatrix_largeThreshold() {
        double[][] matrixData = {
                {1.0, 2.0, 3.0},
                {2.0, 5.0, 6.0},
                {3.0, 6.0, 10.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        double smallThreshold = 1.0;
        try {
            new RectangularCholeskyDecomposition(matrix, smallThreshold);
            Assert.fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testConstructor_singularMatrix() {
        double[][] matrixData = {
                {1.0, 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {0.0, 0.0, 0.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        double smallThreshold = 0.0;
        try {
            new RectangularCholeskyDecomposition(matrix, smallThreshold);
            Assert.fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testConstructor_negativeDefiniteMatrix() {
        double[][] matrixData = {
                {1.0, 2.0, 3.0},
                {2.0, -5.0, 6.0},
                {3.0, 6.0, -10.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        double smallThreshold = 0.0;
        try {
            new RectangularCholeskyDecomposition(matrix, smallThreshold);
            Assert.fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testConstructor_symmetricMatrix() {
        double[][] matrixData = {
                {1.0, 2.0},
                {2.0, 1.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        double smallThreshold = 0.0;
        try {
            new RectangularCholeskyDecomposition(matrix, smallThreshold);
            Assert.fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testConstructor_identityMatrix() {
        double[][] matrixData = {
                {1.0, 0.0},
                {0.0, 1.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        double smallThreshold = 0.0;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, smallThreshold);

        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();

        Assert.assertNotNull(root);
        Assert.assertEquals(2, rank);

        double[][] rootData = root.getData();
        assertEquals(1.0, Precision.doubleEquals(rootData[0][0], 1.0, 0.0001), 0.0);
        assertEquals(0.0, Precision.doubleEquals(rootData[0][1], 0.0, 0.0001), 0.0);
        assertEquals(0.0, Precision.doubleEquals(rootData[1][0], 0.0, 0.0001), 0.0);
        assertEquals(1.0, Precision.doubleEquals(rootData[1][1], 1.0, 0.0001), 0.0);
    }

    @Test
    public void testConstructor_diagonalMatrix() {
        double[][] matrixData = {
                {4.0, 0.0},
                {0.0, 9.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        double smallThreshold = 0.0;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, smallThreshold);

        RealMatrix root = decomposition.getRootMatrix();
        int rank = decomposition.getRank();

        Assert.assertNotNull(root);
        Assert.assertEquals(2, rank);

        double[][] rootData = root.getData();
        assertEquals(Math.sqrt(4.0), Precision.doubleEquals(rootData[0][0], Math.sqrt(4.0), 0.0001), 0.0);
        assertEquals(0.0, Precision.doubleEquals(rootData[0][1], 0.0, 0.0001), 0.0);
        assertEquals(0.0, Precision.doubleEquals(rootData[1][0], 0.0, 0.0001), 0.0);
        assertEquals(3.0, Precision.doubleEquals(rootData[1][1], 3.0, 0.0001), 0.0);
    }

    @Test
    public void testConstructor_triangularMatrix() {
        double[][] matrixData = {
                {1.0, 2.0, 0.0},
                {2.0, 5.0, 3.0},
                {0.0, 3.0, 4.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        double smallThreshold = 0.0;
        try {
            new RectangularCholeskyDecomposition(matrix, smallThreshold);
            Assert.fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }
}