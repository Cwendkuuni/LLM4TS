package org.apache.commons.math3.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathConstants;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor() {
        double small = 1e-8;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0, 0.0},
                                                                    {1.0, 2.0, 1.0},
                                                                    {0.0, 1.0, 2.0}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomposition.getRootMatrix();
            assertEquals(3, root.getRowDimension());
            assertEquals(3, root.getColumnDimension());
            double[][] rootData = root.getData();
            assertEquals(1.4142135625, rootData[0][0], Precision.SAFE_EPSILON);
            assertEquals(-0.7071067811, rootData[0][1], Precision.SAFE_EPSILON);
            assertEquals(0.0, rootData[0][2], Precision.SAFE_EPSILON);
            assertEquals(-0.7071067811, rootData[1][0], Precision.SAFE_EPSILON);
            assertEquals(1.4142135625, rootData[1][1], Precision.SAFE_EPSILON);
            assertEquals(0.7071067811, rootData[1][2], Precision.SAFE_EPSILON);
            assertEquals(0.0, rootData[2][0], Precision.SAFE_EPSILON);
            assertEquals(0.0, rootData[2][1], Precision.SAFE_EPSILON);
            assertEquals(1.4142135625, rootData[2][2], Precision.SAFE_EPSILON);
            int rank = decomposition.getRank();
            assertEquals(3, rank);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testConstructorWithNegativeSmall() {
        double small = -1e-8;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0, 0.0},
                                                                    {1.0, 2.0, 1.0},
                                                                    {0.0, 1.0, 2.0}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            fail("Expected NonPositiveDefiniteMatrixException but no exception was thrown.");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testConstructorWithZeroMatrix() {
        double small = 1e-8;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{0.0},
                                                                    {0.0}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            fail("Expected NonPositiveDefiniteMatrixException but no exception was thrown.");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testConstructorWithIdentityMatrix() {
        double small = 1e-8;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0},
                                                                    {0.0, 1.0}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomposition.getRootMatrix();
            assertEquals(2, root.getRowDimension());
            assertEquals(2, root.getColumnDimension());
            double[][] rootData = root.getData();
            assertEquals(1.0, rootData[0][0], Precision.SAFE_EPSILON);
            assertEquals(0.0, rootData[0][1], Precision.SAFE_EPSILON);
            assertEquals(0.0, rootData[1][0], Precision.SAFE_EPSILON);
            assertEquals(1.0, rootData[1][1], Precision.SAFE_EPSILON);
            int rank = decomposition.getRank();
            assertEquals(2, rank);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testConstructorWithSingularMatrix() {
        double small = 1e-8;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0},
                                                                    {1.0, 0.0}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            fail("Expected NonPositiveDefiniteMatrixException but no exception was thrown.");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testConstructorWithNonSymmetricMatrix() {
        double small = 1e-8;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0},
                                                                    {1.0, 1.0}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            fail("Expected NonPositiveDefiniteMatrixException but no exception was thrown.");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testConstructorWithSmallMatrix() {
        double small = MathConstants.EPSILON;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0},
                                                                    {0.0, 1.0}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomposition.getRootMatrix();
            assertEquals(2, root.getRowDimension());
            assertEquals(2, root.getColumnDimension());
            double[][] rootData = root.getData();
            assertEquals(1.0, rootData[0][0], Precision.SAFE_EPSILON);
            assertEquals(0.0, rootData[0][1], Precision.SAFE_EPSILON);
            assertEquals(0.0, rootData[1][0], Precision.SAFE_EPSILON);
            assertEquals(1.0, rootData[1][1], Precision.SAFE_EPSILON);
            int rank = decomposition.getRank();
            assertEquals(2, rank);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testConstructorWithSymmetricZeroDiagonalMatrix() {
        double small = 1e-8;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0},
                                                                    {0.0, 1.0},
                                                                    {0.0, 0.0}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomposition.getRootMatrix();
            assertEquals(3, root.getRowDimension());
            assertEquals(3, root.getColumnDimension());
            double[][] rootData = root.getData();
            assertEquals(1.0, rootData[0][0], Precision.SAFE_EPSILON);
            assertEquals(0.0, rootData[0][1], Precision.SAFE_EPSILON);
            assertEquals(0.0, rootData[0][2], Precision.SAFE_EPSILON);
            assertEquals(0.0, rootData[1][0], Precision.SAFE_EPSILON);
            assertEquals(1.0, rootData[1][1], Precision.SAFE_EPSILON);
            assertEquals(0.0, rootData[1][2], Precision.SAFE_EPSILON);
            assertEquals(0.0, rootData[2][0], Precision.SAFE_EPSILON);
            assertEquals(0.0, rootData[2][1], Precision.SAFE_EPSILON);
            assertEquals(1.0, rootData[2][2], Precision.SAFE_EPSILON);
            int rank = decomposition.getRank();
            assertEquals(3, rank);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testConstructorWithSymmetricPositiveDefiniteMatrix() {
        double small = 1e-8;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0},
                                                                    {1.0, 2.0}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomposition.getRootMatrix();
            assertEquals(2, root.getRowDimension());
            assertEquals(2, root.getColumnDimension());
            double[][] rootData = root.getData();
            assertEquals(1.4142135625, rootData[0][0], Precision.SAFE_EPSILON);
            assertEquals(-0.7071067811, rootData[0][1], Precision.SAFE_EPSILON);
            assertEquals(-0.7071067811, rootData[1][0], Precision.SAFE_EPSILON);
            assertEquals(1.4142135625, rootData[1][1], Precision.SAFE_EPSILON);
            int rank = decomposition.getRank();
            assertEquals(2, rank);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testConstructorWithSymmetricPositiveSemidefiniteMatrix() {
        double small = 1e-8;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{2.0, 1.0, 0.0},
                                                                    {1.0, 2.0, 1.0},
                                                                    {0.0, 1.0, 2.0}});
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomposition.getRootMatrix();
            assertEquals(3, root.getRowDimension());
            assertEquals(3, root.getColumnDimension());
            double[][] rootData = root.getData();
            assertEquals(1.4142135625, rootData[0][0], Precision.SAFE_EPSILON);
            assertEquals(-0.7071067811, rootData[0][1], Precision.SAFE_EPSILON);
            assertEquals(0.0, rootData[0][2], Precision.SAFE_EPSILON);
            assertEquals(-0.7071067811, rootData[1][0], Precision.SAFE_EPSILON);
            assertEquals(1.4142135625, rootData[1][1], Precision.SAFE_EPSILON);
            assertEquals(0.7071067811, rootData[1][2], Precision.SAFE_EPSILON);
            assertEquals(0.0, rootData[2][0], Precision.SAFE_EPSILON);
            assertEquals(0.0, rootData[2][1], Precision.SAFE_EPSILON);
            assertEquals(1.4142135625, rootData[2][2], Precision.SAFE_EPSILON);
            int rank = decomposition.getRank();
            assertEquals(3, rank);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}