package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.special.E;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullMatrix() {
        new RectangularCholeskyDecomposition(null, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNegativeMatrix() {
        double[][] data = new double[][] { { -1.0, 0.0 }, { 0.0, -1.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        new RectangularCholeskyDecomposition(matrix, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNegativeSmall() {
        double[][] data = new double[][] { { 1.0, 0.0 }, { 0.0, -1.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        new RectangularCholeskyDecomposition(matrix, -0.0);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorNonPositiveDefinite() {
        double[][] data = new double[][] { { 0.0, 0.0 }, { 0.0, -1.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        new RectangularCholeskyDecomposition(matrix, 0.0);
    }

    @Test
    public void testConstructorIdentity() {
        double small = 1e-15;
        double[][] data = new double[][] { { 1.0, 0.0, 0.0 }, { 0.0, 1.0, 0.0 }, { 0.0, 0.0, 1.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.0, 0.0 }, { 0.0, 1.0, 0.0 }, { 0.0, 0.0, 1.0 } }, false);
        assertEquals(expected, decomposition.getRootMatrix());
        assertEquals(3, decomposition.getRank());
    }

    @Test
    public void testConstructorTriangular() {
        double small = 1e-15;
        double[][] data = new double[][] { { 1.0, 0.0, 0.0 }, { 1.0, 1.0, 0.0 }, { 0.0, 0.0, 1.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.0, 0.0 }, { 1.0, 1.0, 0.0 }, { 0.0, 0.0, E.EPSILON } }, false);
        assertEquals(expected, decomposition.getRootMatrix());
        assertEquals(3, decomposition.getRank());
    }

    @Test
    public void testConstructorSingular() {
        double small = 1e-15;
        double[][] data = new double[][] { { 1.0, 0.0, 0.0 }, { 1.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        assertNull(decomposition.getRootMatrix());
        assertEquals(0, decomposition.getRank());
    }

    @Test
    public void testConstructorDiagonal() {
        double small = 1e-15;
        double[][] data = new double[][] { { 1.0, 0.0 }, { 0.0, 1.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.0 }, { 0.0, 1.0 } }, false);
        assertEquals(expected, decomposition.getRootMatrix());
        assertEquals(2, decomposition.getRank());
    }

    @Test
    public void testConstructorZeroDiagonal() {
        double small = 1e-15;
        double[][] data = new double[][] { { 0.0, 0.0 }, { 0.0, 0.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        assertNull(decomposition.getRootMatrix());
        assertEquals(0, decomposition.getRank());
    }

    @Test
    public void testGetRank() {
        double small = 1e-15;
        double[][] data = new double[][] { { 1.0, 0.0, 0.0 }, { 0.0, 1.0, 0.0 }, { 0.0, 0.0, 1.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        assertEquals(3, decomposition.getRank());
    }

    @Test
    public void testGetRootMatrix() {
        double small = 1e-15;
        double[][] data = new double[][] { { 1.0, 0.0, 0.0 }, { 0.0, 1.0, 0.0 }, { 0.0, 0.0, 1.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.0, 0.0 }, { 0.0, 1.0, 0.0 }, { 0.0, 0.0, 1.0 } }, false);
        assertEquals(expected, decomposition.getRootMatrix());
    }

    @Test
    public void testGetRootMatrixIdentity() {
        double[][] data = new double[][] { { 1.0, 0.0, 0.0 }, { 0.0, 1.0, 0.0 }, { 0.0, 0.0, 1.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.0, 0.0 }, { 0.0, 1.0, 0.0 }, { 0.0, 0.0, 1.0 } }, false);
        assertEquals(expected, decomposition.getRootMatrix());
    }

    @Test
    public void testGetRootMatrixSingular() {
        double small = 1e-15;
        double[][] data = new double[][] { { 1.0, 0.0, 0.0 }, { 1.0, 0.0, 0.0 }, { 0.0, 0.0, 0.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        assertNull(decomposition.getRootMatrix());
    }

    @Test
    public void testGetRootMatrixZeroDiagonal() {
        double small = 1e-15;
        double[][] data = new double[][] { { 0.0, 0.0 }, { 0.0, 0.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        assertNull(decomposition.getRootMatrix());
    }

    @Test
    public void testGetRootMatrixZero() {
        double[][] data = new double[][] { { 0.0, 0.0 }, { 0.0, 0.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        assertNull(decomposition.getRootMatrix());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRootMatrixAfterSetRank() {
        double small = 1e-15;
        double[][] data = new double[][] { { 1.0, 0.0, 0.0 }, { 0.0, 1.0, 0.0 }, { 0.0, 0.0, 1.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        decomposition.setRank(1);
        decomposition.getRootMatrix();
    }

    @Test
    public void testSetRank() {
        double small = 1e-15;
        double[][] data = new double[][] { { 1.0, 0.0, 0.0 }, { 0.0, 1.0, 0.0 }, { 0.0, 0.0, 1.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        decomposition.setRank(1);
        assertEquals(1, decomposition.getRank());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetRankNegative() {
        double small = 1e-15;
        double[][] data = new double[][] { { 1.0, 0.0, 0.0 }, { 0.0, 1.0, 0.0 }, { 0.0, 0.0, 1.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        decomposition.setRank(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetRankZero() {
        double small = 1e-15;
        double[][] data = new double[][] { { 1.0, 0.0, 0.0 }, { 0.0, 1.0, 0.0 }, { 0.0, 0.0, 1.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        decomposition.setRank(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetRankGreaterThanMatrixSize() {
        double small = 1e-15;
        double[][] data = new double[][] { { 1.0, 0.0, 0.0 }, { 0.0, 1.0, 0.0 }, { 0.0, 0.0, 1.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        decomposition.setRank(4);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetRankAfterGetRootMatrix() {
        double small = 1e-15;
        double[][] data = new double[][] { { 1.0, 0.0, 0.0 }, { 0.0, 1.0, 0.0 }, { 0.0, 0.0, 1.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        decomposition.getRootMatrix();
        decomposition.setRank(1);
    }

    @Test
    public void testToString() {
        double small = 1e-15;
        double[][] data = new double[][] { { 1.0, 0.0, 0.0 }, { 0.0, 1.0, 0.0 }, { 0.0, 0.0, 1.0 } };
        RealMatrix matrix = new Array2DRowRealMatrix(data, false);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        String result = decomposition.toString();
        assertTrue(result.contains("class RectangularCholeskyDecomposition"));
        assertTrue(result.contains("Permuted Cholesky root of the symmetric positive semidefinite matrix."));
        assertTrue(result.contains("Rank of the symmetric positive semidefinite matrix."));
        assertTrue(result.contains("root: "));
        assertTrue(result.contains("rank: "));
        assertFalse(result.contains("symmetric positive semidefinite matrix: "));
        assertFalse(result.contains("decompose: "));
    }

}