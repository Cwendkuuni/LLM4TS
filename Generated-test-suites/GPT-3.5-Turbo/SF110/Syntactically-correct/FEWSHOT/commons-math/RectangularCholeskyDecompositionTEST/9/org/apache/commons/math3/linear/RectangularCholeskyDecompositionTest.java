
package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        assertEquals(MatrixUtils.createRealMatrix(new double[][]{{2, 0, 0}, {6, 1, 0}, {-8, 5, 3}}), decomposition.getRootMatrix());
        assertEquals(3, decomposition.getRank());
    }

    @Test
    public void testConstructorWithSmall() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.1);
        assertEquals(MatrixUtils.createRealMatrix(new double[][]{{2, 0, 0}, {6, 1, 0}, {-8, 5, 3}}), decomposition.getRootMatrix());
        assertEquals(3, decomposition.getRank());
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrix() throws NonPositiveDefiniteMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{-4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        new RectangularCholeskyDecomposition(matrix);
    }
}
