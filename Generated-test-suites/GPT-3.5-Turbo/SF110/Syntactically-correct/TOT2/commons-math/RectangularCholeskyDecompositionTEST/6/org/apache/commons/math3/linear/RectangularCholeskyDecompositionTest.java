package org.apache.commons.math3.linear;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.linear.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructorWithRegularMatrix() {
        double[][] testData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
        
        assertNotNull(decomp.getRootMatrix());
        assertEquals(3, decomp.getRank());
    }

    @Test
    public void testConstructorWithNonPositiveDefiniteMatrixException() {
        double[][] testData = {{-1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        
        assertThrows(NonPositiveDefiniteMatrixException.class, () -> {
            new RectangularCholeskyDecomposition(matrix);
        });
    }

    @Test
    public void testGetRootMatrix() {
        double[][] testData = {{25, 15, -5}, {15, 18, 0}, {-5, 0, 11}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
        
        RealMatrix rootMatrix = decomp.getRootMatrix();
        
        assertEquals(25.0, rootMatrix.getEntry(0, 0), 0.0001);
        assertEquals(22.5, rootMatrix.getEntry(1, 0), 0.0001);
        assertEquals(-2.5, rootMatrix.getEntry(2, 0), 0.0001);
    }

    // Add more test cases to cover other public methods and edge cases

}