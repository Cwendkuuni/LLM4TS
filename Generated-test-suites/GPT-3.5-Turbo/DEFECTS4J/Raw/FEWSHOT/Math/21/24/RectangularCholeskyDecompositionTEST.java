package org.apache.commons.math3.linear;

import org.apache.commons.math3.util.FastMath;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    private RectangularCholeskyDecomposition choleskyDecomposition;

    private RealMatrix matrix;
    private double small;

    @Before
    public void setUp() {
        int order = 3;
        double[][] data = {{4, 1, -1}, {1, 2, 0}, {-1, 0, 3}};
        matrix = MatrixUtils.createRealMatrix(data);
        small = 1e-10;
        choleskyDecomposition = new RectangularCholeskyDecomposition(matrix, small);
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix root = choleskyDecomposition.getRootMatrix();
        double[][] expectedData = {{2, 0, 0}, {0.5, 1.3228756555322954, 0}, {-0.5, 0.5163977794943222, 1.7320508075688772}};
        RealMatrix expected = MatrixUtils.createRealMatrix(expectedData);
        assertTrue(MatrixUtils.isEqual(root, expected, small));
    }

    @Test
    public void testGetRank() {
        int rank = choleskyDecomposition.getRank();
        assertEquals(3, rank);
    }

}