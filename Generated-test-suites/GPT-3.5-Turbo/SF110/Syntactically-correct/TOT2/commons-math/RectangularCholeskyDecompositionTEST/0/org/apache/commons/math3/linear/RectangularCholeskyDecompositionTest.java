package org.apache.commons.math3.linear;
import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);

        assertNotNull(rcd.getRootMatrix());
        assertEquals(3, rcd.getRank());
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);

        RealMatrix root = rcd.getRootMatrix();
        assertNotNull(root);
        assertEquals(3, root.getRowDimension());
        assertEquals(3, root.getColumnDimension());
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);

        assertEquals(3, rcd.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{-4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        RectangularCholeskyDecomposition rcd = new RectangularCholeskyDecomposition(matrix);
    }

}