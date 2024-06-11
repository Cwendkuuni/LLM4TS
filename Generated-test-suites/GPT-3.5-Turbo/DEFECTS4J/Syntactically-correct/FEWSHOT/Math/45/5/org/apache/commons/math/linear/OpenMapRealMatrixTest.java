
package org.apache.commons.math.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

public class OpenMapRealMatrixTest {
    private OpenMapRealMatrix matrix;

    @Before
    public void setUp() {
        matrix = new OpenMapRealMatrix(3, 3);
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix copy = matrix.copy();
        assertEquals(matrix.getRowDimension(), copy.getRowDimension());
        assertEquals(matrix.getColumnDimension(), copy.getColumnDimension());
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                assertEquals(matrix.getEntry(i, j), copy.getEntry(i, j), 0.0);
            }
        }
    }

    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix createdMatrix = matrix.createMatrix(2, 2);
        assertEquals(2, createdMatrix.getRowDimension());
        assertEquals(2, createdMatrix.getColumnDimension());
    }

    @Test
    public void testGetColumnDimension() {
        assertEquals(3, matrix.getColumnDimension());
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(3, 3);
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                m.setEntry(i, j, i + j);
            }
        }
        
        OpenMapRealMatrix result = matrix.add(m);
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                assertEquals(matrix.getEntry(i, j) + m.getEntry(i, j), result.getEntry(i, j), 0.0);
            }
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testAddWithMismatchedDimension() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(2, 2);
        matrix.add(m);
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(3, 3);
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                m.setEntry(i, j, i + j);
            }
        }
        
        OpenMapRealMatrix result = matrix.subtract(m);
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                assertEquals(matrix.getEntry(i, j) - m.getEntry(i, j), result.getEntry(i, j), 0.0);
            }
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testSubtractWithMismatchedDimension() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(2, 2);
        matrix.subtract(m);
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(3, 3);
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                m.setEntry(i, j, i + j);
            }
        }
        
        RealMatrix result = matrix.multiply(m);
        assertEquals(matrix.getRowDimension(), result.getRowDimension());
        assertEquals(m.getColumnDimension(), result.getColumnDimension());
        for (int i = 0; i < matrix.getRowDimension(); i++) {
            for (int j = 0; j < m.getColumnDimension(); j++) {
                double expected = 0.0;
                for (int k = 0; k < matrix.getColumnDimension(); k++) {
                    expected += matrix.getEntry(i, k) * m.getEntry(k, j);
                }
                assertEquals(expected, result.getEntry(i, j), 0.0);
            }
        }
    }
    
    @Test(expected = DimensionMismatchException.class)
    public void testMultiplyWithMismatchedDimension() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(2, 2);
        matrix.multiply(m);
    }

    @Test
    public void testGetEntry() {
        matrix.setEntry(1, 2, 5.0);
        assertEquals(5.0, matrix.getEntry(1, 2), 0.0);
    }

    @Test(expected = MatrixIndexOutOfBoundsException.class)
    public void testGetEntryWithInvalidRow() {
        matrix.getEntry(-1, 2);
    }

    @Test(expected = MatrixIndexOutOfBoundsException.class)
    public void testGetEntryWithInvalidColumn() {
        matrix.getEntry(1, 3);
    }

    @Test
    public void testGetRowDimension() {
        assertEquals(3, matrix.getRowDimension());
    }

    @Test
    public void testSetEntry() {
        matrix.setEntry(1, 2, 5.0);
        assertEquals(5.0, matrix.getEntry(1, 2), 0.0);
    }

    @Test(expected = MatrixIndexOutOfBoundsException.class)
    public void testSetEntryWithInvalidRow() {
        matrix.setEntry(-1, 2, 5.0);
    }

    @Test(expected = MatrixIndexOutOfBoundsException.class)
    public void testSetEntryWithInvalidColumn() {
        matrix.setEntry(1, 3, 5.0);
    }

    @Test
    public void testAddToEntry() {
        matrix.addToEntry(1, 2, 5.0);
        assertEquals(5.0, matrix.getEntry(1, 2), 0.0);
        
        matrix.addToEntry(1, 2, -1.0);
        assertEquals(4.0, matrix.getEntry(1, 2), 0.0);
    }

    @Test(expected = MatrixIndexOutOfBoundsException.class)
    public void testAddToEntryWithInvalidRow() {
        matrix.addToEntry(-1, 2, 5.0);
    }

    @Test(expected = MatrixIndexOutOfBoundsException.class)
    public void testAddToEntryWithInvalidColumn() {
        matrix.addToEntry(1, 3, 5.0);
    }

    @Test
    public void testMultiplyEntry() {
        matrix.multiplyEntry(1, 2, 5.0);
        assertEquals(0.0, matrix.getEntry(1, 2), 0.0);
        
        matrix.setEntry(1, 2, 1.0);
        matrix.multiplyEntry(1, 2, 5.0);
        assertEquals(5.0, matrix.getEntry(1, 2), 0.0);
    }

    @Test(expected = MatrixIndexOutOfBoundsException.class)
    public void testMultiplyEntryWithInvalidRow() {
        matrix.multiplyEntry(-1, 2, 5.0);
    }

    @Test(expected = MatrixIndexOutOfBoundsException.class)
    public void testMultiplyEntryWithInvalidColumn() {
        matrix.multiplyEntry(1, 3, 5.0);
    }
}
