package org.apache.commons.math3.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4.0, 2.0}, {2.0, 3.0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        RealMatrix root = decomposition.getRootMatrix();

        assertEquals(2, decomposition.getRank());
        assertTrue(root.getEntry(0, 0) > 0);
        assertTrue(root.getEntry(1, 1) > 0);
    }

    @Test
    public void testConstructorWithNonPositiveDefiniteMatrixException() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4.0, 2.0}, {2.0, 1.0}});
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected exception
        }
    }
}