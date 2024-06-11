package org.apache.commons.math3.linear;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor() {
        double[][] data = {{1, 0}, {0, 1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);

        assertNotNull(decomposition.getRootMatrix());
        assertEquals(2, decomposition.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor_NonPositiveDefiniteMatrixException() {
        double[][] data = {{-1, 0}, {0, 1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
    }
}