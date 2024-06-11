
package org.apache.commons.math3.linear;

import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;

import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor_typicalCase() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4, 1}, {1, 2}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        assertNotNull(decomposition.getRootMatrix());
        assertEquals(2, decomposition.getRank());
    }

    @Test
    public void testConstructor_edgeCase() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1e-15, 0}, {0, 1}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-14);
        assertNotNull(decomposition.getRootMatrix());
        assertEquals(2, decomposition.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor_errorCase() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{-1, 2}, {2, -1}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4, 1}, {1, 2}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        assertNotNull(decomposition.getRootMatrix());
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4, 1}, {1, 2}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        assertEquals(2, decomposition.getRank());
    }

}
