package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class RectangularCholeskyDecompositionTest {
    @Test
    public void testConstructor() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);

        assertNotNull(decomposition.getRootMatrix());
        assertEquals(2, decomposition.getRank());
    }
}