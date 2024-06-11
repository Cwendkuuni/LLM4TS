package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor() {
        // Test case 1
        RealMatrix matrix1 = MatrixUtils.createRealMatrix(3, 3);
        RectangularCholeskyDecomposition decomp1 = new RectangularCholeskyDecomposition(matrix1);
        assertNotNull(decomp1);
        assertEquals(0, decomp1.getRank());
        
        // Test case 2
        RealMatrix matrix2 = MatrixUtils.createRealMatrix(4, 4);
        RectangularCholeskyDecomposition decomp2 = new RectangularCholeskyDecomposition(matrix2, 0.5);
        assertNotNull(decomp2);
        assertEquals(0, decomp2.getRank());
        
        // Test case 3 - NonPositiveDefiniteMatrixException
        RealMatrix matrix3 = MatrixUtils.createRealMatrix(2, 2);
        double[][] data3 = matrix3.getData();
        data3[0][0] = 1;
        data3[0][1] = 2;
        data3[1][0] = 2;
        data3[1][1] = 5;
        try {
            RectangularCholeskyDecomposition decomp3 = new RectangularCholeskyDecomposition(matrix3);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals(1.0, e.getMainDiagonalElement(), 0.0);
            assertEquals(0, e.getIndex());
        }
        
        // Test case 4 - NonPositiveDefiniteMatrixException
        RealMatrix matrix4 = MatrixUtils.createRealMatrix(3, 3);
        double[][] data4 = matrix4.getData();
        data4[0][0] = 1;
        data4[0][1] = -0.5;
        data4[0][2] = 0.5;
        data4[1][0] = -0.5;
        data4[1][1] = -2;
        data4[1][2] = -3;
        data4[2][0] = 0.5;
        data4[2][1] = -3;
        data4[2][2] = -5;
        try {
            RectangularCholeskyDecomposition decomp4 = new RectangularCholeskyDecomposition(matrix4);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals(-2.0, e.getMainDiagonalElement(), 0.0);
            assertEquals(1, e.getIndex());
        }
    }
    
    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(3, 3);
        double[][] data = matrix.getData();
        data[0][0] = 1;
        data[0][1] = 2;
        data[0][2] = 3;
        data[1][0] = 2;
        data[1][1] = 5;
        data[1][2] = 6;
        data[2][0] = 3;
        data[2][1] = 6;
        data[2][2] = 9;
        
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
        RealMatrix rootMatrix = decomp.getRootMatrix();
        assertNotNull(rootMatrix);
        assertEquals(3, rootMatrix.getRowDimension());
        assertEquals(3, rootMatrix.getColumnDimension());
        assertEquals(1.0, rootMatrix.getEntry(0, 0), 0.0);
        assertEquals(2.0, rootMatrix.getEntry(0, 1), 0.0);
        assertEquals(3.0, rootMatrix.getEntry(0, 2), 0.0);
        assertEquals(2.0, rootMatrix.getEntry(1, 0), 0.0);
        assertEquals(1.0, rootMatrix.getEntry(1, 1), 0.0);
        assertEquals(1.0, rootMatrix.getEntry(1, 2), 0.0);
        assertEquals(3.0, rootMatrix.getEntry(2, 0), 0.0);
        assertEquals(1.0, rootMatrix.getEntry(2, 1), 0.0);
        assertEquals(0.0, rootMatrix.getEntry(2, 2), 0.0);
    }
    
    @Test
    public void testGetRank() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(3, 3);
        double[][] data = matrix.getData();
        data[0][0] = 1;
        data[0][1] = 2;
        data[0][2] = 3;
        data[1][0] = 2;
        data[1][1] = 5;
        data[1][2] = 6;
        data[2][0] = 3;
        data[2][1] = 6;
        data[2][2] = 9;
        
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
        assertEquals(3, decomp.getRank());
    }
}