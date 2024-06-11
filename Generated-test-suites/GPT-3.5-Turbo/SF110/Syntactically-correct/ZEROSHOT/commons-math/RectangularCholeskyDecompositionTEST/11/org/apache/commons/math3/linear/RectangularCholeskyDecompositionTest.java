package org.apache.commons.math3.linear;

import org.junit.Test;
import org.apache.commons.math3.util.*;

import static org.junit.Assert.assertEquals;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor1() {
        double[][] data = {{4, 1, -1}, {1, 2, 0}, {-1, 0, 3}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);

        assertEquals(matrix, decomposition.getRootMatrix());
        assertEquals(3, decomposition.getRank());
    }

    @Test
    public void testConstructor2() {
        double[][] data = {{4, 1, -1}, {1, 2, 0}, {-1, 0, 3}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = 0.1;
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);

        assertEquals(matrix, decomposition.getRootMatrix());
        assertEquals(3, decomposition.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor1NonPositiveDefiniteMatrixException() {
        double[][] data = {{4, 1, -1}, {1, -2, 0}, {-1, 0, 3}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        new RectangularCholeskyDecomposition(matrix);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor2NonPositiveDefiniteMatrixException() {
        double[][] data = {{4, 1, -1}, {1, -2, 0}, {-1, 0, 3}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        double small = 0.1;
        new RectangularCholeskyDecomposition(matrix, small);
    }
}