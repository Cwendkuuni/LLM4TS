package org.apache.commons.math.linear;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class OpenMapRealMatrixTest {
    private OpenMapRealMatrix matrix;
    private final double epsilon = 1e-10;

    @Before
    public void setUp() {
        matrix = new OpenMapRealMatrix(3, 3);
    }

    @Test
    public void testConstructor() {
        assertEquals(3, matrix.getRowDimension());
        assertEquals(3, matrix.getColumnDimension());
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix copyMatrix = matrix.copy();

        assertEquals(matrix.getRowDimension(), copyMatrix.getRowDimension());
        assertEquals(matrix.getColumnDimension(), copyMatrix.getColumnDimension());

        for (int i = 0; i < copyMatrix.getRowDimension(); i++) {
            for (int j = 0; j < copyMatrix.getColumnDimension(); j++) {
                assertEquals(matrix.getEntry(i, j), copyMatrix.getEntry(i, j), epsilon);
            }
        }
    }

    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix newMatrix = matrix.createMatrix(2, 2);

        assertEquals(2, newMatrix.getRowDimension());
        assertEquals(2, newMatrix.getColumnDimension());
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(3, 3);
        for (int i = 0; i < m.getRowDimension(); i++) {
            for (int j = 0; j < m.getColumnDimension(); j++) {
                m.setEntry(i, j, i + j);
            }
        }

        OpenMapRealMatrix resultMatrix = matrix.add(m);

        for (int i = 0; i < resultMatrix.getRowDimension(); i++) {
            for (int j = 0; j < resultMatrix.getColumnDimension(); j++) {
                assertEquals(0 + i + j, resultMatrix.getEntry(i, j), epsilon);
            }
        }
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(3, 3);
        for (int i = 0; i < m.getRowDimension(); i++) {
            for (int j = 0; j < m.getColumnDimension(); j++) {
                m.setEntry(i, j, i + j);
            }
        }

        OpenMapRealMatrix resultMatrix = matrix.subtract(m);

        for (int i = 0; i < resultMatrix.getRowDimension(); i++) {
            for (int j = 0; j < resultMatrix.getColumnDimension(); j++) {
                assertEquals(0 - (i + j), resultMatrix.getEntry(i, j), epsilon);
            }
        }
    }

    @Test
    public void testMultiply() {
        BlockRealMatrix m = new BlockRealMatrix(3, 3);
        for (int i = 0; i < m.getRowDimension(); i++) {
            for (int j = 0; j < m.getColumnDimension(); j++) {
                m.setEntry(i, j, i + j);
            }
        }

        RealMatrix resultMatrix = matrix.multiply(m);

        for (int i = 0; i < resultMatrix.getRowDimension(); i++) {
            for (int j = 0; j < resultMatrix.getColumnDimension(); j++) {
                assertEquals(0, resultMatrix.getEntry(i, j), epsilon);
            }
        }
    }

    @Test
    public void testGetEntry() {
        matrix.setEntry(1, 1, 10);

        assertEquals(10, matrix.getEntry(1, 1), epsilon);
        assertNotEquals(0, matrix.getEntry(1, 0), epsilon);
        assertNotEquals(0, matrix.getEntry(0, 1), epsilon);
    }

    @Test(expected = MatrixIndexOutOfBoundsException.class)
    public void testGetEntryOutOfBounds() {
        matrix.getEntry(4, 4);
    }

    @Test
    public void testSetEntry() {
        matrix.setEntry(1, 1, 10);

        assertEquals(10, matrix.getEntry(1, 1), epsilon);
    }

    @Test(expected = MatrixIndexOutOfBoundsException.class)
    public void testSetEntryOutOfBounds() {
        matrix.setEntry(4, 4, 1);
    }

    @Test
    public void testAddToEntry() {
        matrix.addToEntry(1, 1, 10);

        assertEquals(10, matrix.getEntry(1, 1), epsilon);
    }

    @Test(expected = MatrixIndexOutOfBoundsException.class)
    public void testAddToEntryOutOfBounds() {
        matrix.addToEntry(4, 4, 1);
    }

    @Test
    public void testMultiplyEntry() {
        matrix.setEntry(1, 1, 10);
        matrix.multiplyEntry(1, 1, 10);

        assertEquals(100, matrix.getEntry(1, 1), epsilon);
    }

    @Test(expected = MatrixIndexOutOfBoundsException.class)
    public void testMultiplyEntryOutOfBounds() {
        matrix.multiplyEntry(4, 4, 1);
    }
}