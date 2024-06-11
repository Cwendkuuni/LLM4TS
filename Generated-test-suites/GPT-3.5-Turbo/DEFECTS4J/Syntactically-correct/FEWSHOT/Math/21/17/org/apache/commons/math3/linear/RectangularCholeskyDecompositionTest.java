package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {
    private RectangularCholeskyDecomposition rcd;

    @Before
    public void setup() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2, 3}, {2, 5, 4}, {3, 4, 6}});
        double small = 0.01;
        rcd = new RectangularCholeskyDecomposition(matrix, small);
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0}, {2.0, 1.0}, {3.0, -0.625}});
        assertEquals(expected, rcd.getRootMatrix());
    }

    @Test
    public void testGetRank() {
        assertEquals(2, rcd.getRank());
    }
}