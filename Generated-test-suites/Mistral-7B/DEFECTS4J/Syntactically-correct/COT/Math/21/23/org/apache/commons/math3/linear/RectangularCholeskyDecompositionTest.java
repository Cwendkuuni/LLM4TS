package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;

import java.util.Random;

public class RectangularCholeskyDecompositionTest {

    @org.junit.Test
    public void testConstructor_PositiveDefiniteMatrix() {
        double[][] matrixData = {
                {2.0, 1.0, 1.0},
                {1.0, 2.0, 1.0},
                {1.0, 1.0, 2.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-12);
            assertEquals(3, decomposition.getRank());
            RealMatrix root = decomposition.getRootMatrix();
            assertArrayEquals(new double[][]{
                    {1.4142135625, 0.7071067811, 0.7071067811},
                    {0.7071067811, 1.4142135625, 0.7071067811},
                    {0.7071067811, 0.7071067811, 1.4142135625}
            }, root.getData());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @org.junit.Test
    public void testConstructor_SingularMatrix() {
        double[][] matrixData = {
                {2.0, 1.0, 1.0},
                {1.0, 2.0, 1.0},
                {1.0, 1.0, 0.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-12);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals("The given matrix is not positive semidefinite. Diagonal element [2][2] is zero.", e.getMessage());
        }
    }

    @org.junit.Test
    public void testConstructor_NegativeDefiniteMatrix() {
        double[][] matrixData = {
                {2.0, 1.0, 1.0},
                {1.0, 2.0, 1.0},
                {1.0, 1.0, -2.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-12);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected exception
        }
    }

    @org.junit.Test
    public void testConstructor_SmallMatrix() {
        double[][] matrixData = {
                {1.0, 1.0},
                {1.0, 1.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-14);
            assertEquals(2, decomposition.getRank());
            RealMatrix root = decomposition.getRootMatrix();
            assertArrayEquals(new double[][]{
                    {1.0, 1.0},
                    {1.0, 1.0}
            }, root.getData());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @org.junit.Test
    public void testConstructor_IdentityMatrix() {
        double[][] matrixData = {
                {1.0, 0.0},
                {0.0, 1.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-15);
            assertEquals(2, decomposition.getRank());
            RealMatrix root = decomposition.getRootMatrix();
            assertArrayEquals(new double[][]{
                    {1.0, 0.0},
                    {0.0, 1.0}
            }, root.getData());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @org.junit.Test
    public void testConstructor_RandomMatrix() {
        int size = 5;
        Random random = new Random(1);
        double[][] matrixData = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrixData[i][j] = random.nextGaussian();
            }
        }
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-5);
            int rank = decomposition.getRank();
            RealMatrix root = decomposition.getRootMatrix();
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < rank; j++) {
                    double expected = Precision.round(Math.sqrt(matrix.getEntry(i, i)), 5);
                    double actual = root.getEntry(i, j);
                    assertEquals(expected, actual, 1e-5);
                }
            }
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}