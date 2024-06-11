package org.apache.commons.math.linear;
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    @Test
    public void testCopy() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(2, 2);
        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 3.0);
        matrix.setEntry(1, 1, 4.0);

        OpenMapRealMatrix copyMatrix = matrix.copy();

        assertEquals(matrix, copyMatrix);
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(2, 2);
        matrix1.setEntry(0, 0, 1.0);
        matrix1.setEntry(0, 1, 2.0);
        matrix1.setEntry(1, 0, 3.0);
        matrix1.setEntry(1, 1, 4.0);

        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(2, 2);
        matrix2.setEntry(0, 0, 2.0);
        matrix2.setEntry(0, 1, 3.0);
        matrix2.setEntry(1, 0, 4.0);
        matrix2.setEntry(1, 1, 5.0);

        OpenMapRealMatrix result = matrix1.add(matrix2);

        OpenMapRealMatrix expected = new OpenMapRealMatrix(2, 2);
        expected.setEntry(0, 0, 3.0);
        expected.setEntry(0, 1, 5.0);
        expected.setEntry(1, 0, 7.0);
        expected.setEntry(1, 1, 9.0);

        assertEquals(expected, result);
    }

    // Additional test cases for other methods

}