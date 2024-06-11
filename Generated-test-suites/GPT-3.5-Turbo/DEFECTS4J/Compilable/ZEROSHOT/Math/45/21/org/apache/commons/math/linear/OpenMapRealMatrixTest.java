package org.apache.commons.math.linear;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    private OpenMapRealMatrix matrix;

    @Before
    public void setUp() {
        matrix = new OpenMapRealMatrix(3, 3);
    }

    @Test
    public void testConstructor() {
        Assert.assertEquals(3, matrix.getRowDimension());
        Assert.assertEquals(3, matrix.getColumnDimension());
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix copy = matrix.copy();
        Assert.assertEquals(matrix.getRowDimension(), copy.getRowDimension());
        Assert.assertEquals(matrix.getColumnDimension(), copy.getColumnDimension());
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix result = matrix.add(m);
        double[][] data = result.getData();
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                Assert.assertEquals(matrix.getData()[i][j] + m.getData()[i][j], data[i][j], 0.0001);
            }
        }
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix result = matrix.subtract(m);
        double[][] data = result.getData();
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                Assert.assertEquals(matrix.getData()[i][j] - m.getData()[i][j], data[i][j], 0.0001);
            }
        }
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix m = new OpenMapRealMatrix(3, 3);
        RealMatrix result = matrix.multiply(m);
        double[][] data = result.getData();
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                double sum = 0;
                for (int k = 0; k < matrix.getColumnDimension(); k++) {
                    sum += matrix.getData()[i][k] * m.getData()[k][j];
                }
                Assert.assertEquals(sum, data[i][j], 0.0001);
            }
        }
    }

    @Test
    public void testGetEntry() {
        matrix.setEntry(1, 2, 5.6);
        double entry = matrix.getEntry(1, 2);
        Assert.assertEquals(5.6, entry, 0.0001);
    }

    @Test
    public void testSetEntry() {
        matrix.setEntry(1, 2, 3.4);
        double entry = matrix.getEntry(1, 2);
        Assert.assertEquals(3.4, entry, 0.0001);
    }

    @Test
    public void testAddToEntry() {
        matrix.setEntry(1, 2, 3.4);
        matrix.addToEntry(1, 2, 1.2);
        double entry = matrix.getEntry(1, 2);
        Assert.assertEquals(4.6, entry, 0.0001);
    }

    @Test
    public void testMultiplyEntry() {
        matrix.setEntry(1, 2, 3.4);
        matrix.multiplyEntry(1, 2, 2);
        double entry = matrix.getEntry(1, 2);
        Assert.assertEquals(6.8, entry, 0.0001);
    }
}