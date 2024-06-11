package org.apache.commons.math3.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Precision;

import java.util.Random;

public class RectangularCholeskyDecompositionTest {

    @org.junit.Test
    public void testConstructor_ValidMatrix_Success() {
        double small = 1e-10;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 0.0}, {1.0, 2.0, 1.0}, {0.0, 1.0, 3.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.5430806348152426}, {0.0, 0.5430806348152426}});
        RealMatrix actualRoot = decomposition.getRootMatrix();
        assertEquals("Root matrix is not correct.", expectedRoot, actualRoot);
        assertEquals("Rank is not correct.", 3, decomposition.getRank());
    }

    @org.junit.Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor_NonPositiveDefiniteMatrix_Exception() {
        double small = 1e-10;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{-1.0, 0.0, 0.0}, {0.0, -1.0, 0.0}, {0.0, 0.0, -1.0}});
        new RectangularCholeskyDecomposition(matrix, small);
    }

    @org.junit.Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor_DiagonalBelowThreshold_Exception() {
        double small = 1e-10;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 0.0}, {1.0, 1.0, 0.0}, {0.0, 0.0, 0.0}});
        new RectangularCholeskyDecomposition(matrix, small);
    }

    @org.junit.Test
    public void testConstructor_RandomMatrix_Success() {
        double small = 1e-10;
        int size = 5;
        RealMatrix matrix = new Array2DRowRealMatrix(size, size);
        Random random = new Random();
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                matrix.setEntry(i, j, random.nextGaussian() / 2.0);
            }
        }
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix expectedRoot = matrix.copyRows(0, decomposition.getRank());
        RealMatrix actualRoot = decomposition.getRootMatrix();
        assertEquals("Root matrix is not correct.", expectedRoot, actualRoot);
        assertEquals("Rank is not correct.", decomposition.getRank(), size);
    }

    @org.junit.Test
    public void testGetRank_ValidMatrix_Success() {
        double small = 1e-10;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 0.0}, {1.0, 2.0, 1.0}, {0.0, 1.0, 3.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        int expectedRank = 3;
        int actualRank = decomposition.getRank();
        assertEquals("Rank is not correct.", expectedRank, actualRank);
    }

    @org.junit.Test
    public void testGetRank_SingularMatrix_Success() {
        double small = 1e-10;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 0.0}, {1.0, 1.0, 0.0}, {0.0, 0.0, 0.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        int expectedRank = 2;
        int actualRank = decomposition.getRank();
        assertEquals("Rank is not correct.", expectedRank, actualRank);
    }

    @org.junit.Test
    public void testGetRank_ZeroMatrix_ZeroRank() {
        double small = 1e-10;
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{}, {}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        int expectedRank = 0;
        int actualRank = decomposition.getRank();
        assertEquals("Rank is not zero.", expectedRank, actualRank);
    }
}