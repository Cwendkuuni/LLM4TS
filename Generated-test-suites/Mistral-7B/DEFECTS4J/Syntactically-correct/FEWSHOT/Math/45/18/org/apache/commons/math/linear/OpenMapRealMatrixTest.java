package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.linear.BlockRealMatrix;
import org.apache.commons.math.linear.OpenIntToDoubleHashMap;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.ArrayRealVector;
import org.apache.commons.math.linear.RealVector;

import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testOpenMapRealMatrixCreation() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 2);
        assertEquals(3, matrix.getRowDimension());
        assertEquals(2, matrix.getColumnDimension());
        assertEquals(0, matrix.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenMapRealMatrixNegativeSize() {
        new OpenMapRealMatrix(-1, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOpenMapRealMatrixZeroSize() {
        new OpenMapRealMatrix(0, 2);
    }

    @Test
    public void testCopyConstructor() {
        OpenMapRealMatrix original = new OpenMapRealMatrix(new OpenIntToDoubleHashMap() {{
            put(0, 1.0);
            put(1, 2.0);
            put(2, 3.0);
        }});
        OpenMapRealMatrix copy = new OpenMapRealMatrix(original);

        assertEquals(original.getRowDimension(), copy.getRowDimension());
        assertEquals(original.getColumnDimension(), copy.getColumnDimension());
        assertEquals(original.size(), copy.size());

        for (int i = 0; i < original.size(); i++) {
            assertEquals(original.getEntry(i / original.getColumnDimension(), i % original.getColumnDimension()), copy.getEntry(i / copy.getColumnDimension(), i % copy.getColumnDimension()));
        }
    }

    @Test
    public void testAddition() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenIntToDoubleHashMap() {{
            put(0, 1.0);
            put(1, 2.0);
            put(2, 3.0);
        }});
        OpenMapRealMatrix b = new OpenMapRealMatrix(new OpenIntToDoubleHashMap() {{
            put(0, 1.0);
            put(1, 2.0);
            put(2, 3.0);
            put(3, 4.0);
            put(4, 5.0);
        }});

        OpenMapRealMatrix result = a.add(b);

        assertEquals(a.getRowDimension(), result.getRowDimension());
        assertEquals(a.getColumnDimension(), result.getColumnDimension());
        assertEquals(a.size() + b.size(), result.size());

        for (int i = 0; i < a.size(); i++) {
            double aValue = a.getEntry(i / a.getColumnDimension(), i % a.getColumnDimension());
            double bValue = b.getEntry(i / b.getColumnDimension(), i % b.getColumnDimension());
            double expectedValue = aValue + bValue;
            assertEquals(expectedValue, result.getEntry(i / result.getColumnDimension(), i % result.getColumnDimension()));
        }

        for (int i = a.size(); i < result.size(); i++) {
            int row = i / result.getColumnDimension();
            int column = i % result.getColumnDimension();
            assertEquals(0.0, result.getEntry(row, column));
        }
    }

    @Test
    public void testSubtraction() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenIntToDoubleHashMap() {{
            put(0, 1.0);
            put(1, 2.0);
            put(2, 3.0);
        }});
        OpenMapRealMatrix b = new OpenMapRealMatrix(new OpenIntToDoubleHashMap() {{
            put(0, 1.0);
            put(1, 2.0);
            put(2, 3.0);
        }});

        OpenMapRealMatrix result = a.subtract(b);

        assertEquals(a.getRowDimension(), result.getRowDimension());
        assertEquals(a.getColumnDimension(), result.getColumnDimension());
        assertEquals(a.size(), result.size());

        for (int i = 0; i < a.size(); i++) {
            double aValue = a.getEntry(i / a.getColumnDimension(), i % a.getColumnDimension());
            double bValue = b.getEntry(i / b.getColumnDimension(), i % b.getColumnDimension());
            double expectedValue = aValue - bValue;
            assertEquals(expectedValue, result.getEntry(i / result.getColumnDimension(), i % result.getColumnDimension()));
        }
    }

    @Test
    public void testMultiplication() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenIntToDoubleHashMap() {{
            put(0, new OpenIntToDoubleHashMap() {{
                put(0, 1.0);
                put(1, 2.0);
            }});
            put(1, new OpenIntToDoubleHashMap() {{
                put(0, 3.0);
                put(1, 4.0);
            }});
            put(2, new OpenIntToDoubleHashMap() {{
                put(0, 5.0);
                put(1, 6.0);
            }});
        }});

        RealMatrix b = new ArrayRealVector(new double[][]{{1.0, 2.0}, {3.0, 4.0}});

        OpenMapRealMatrix result = a.multiply(b);

        assertEquals(a.getRowDimension(), result.getRowDimension());
        assertEquals(b.getColumnDimension(), result.getColumnDimension());
        assertEquals(a.getRowDimension() * b.getColumnDimension(), result.size());

        for (int i = 0; i < result.size(); i++) {
            double expectedValue = 0.0;
            for (int j = 0; j < b.getColumnDimension(); j++) {
                expectedValue += a.getEntry(i / a.getColumnDimension(), j) * b.getEntry(j, i % a.getColumnDimension());
            }
            assertEquals(expectedValue, result.getEntry(i));
        }
    }

    @Test
    public void testMultiplicationWithSameMatrix() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap() {{
            put(0, new OpenIntToDoubleHashMap() {{
                put(0, 1.0);
                put(1, 2.0);
            }});
            put(1, new OpenIntToDoubleHashMap() {{
                put(0, 3.0);
                put(1, 4.0);
            }});
            put(2, new OpenIntToDoubleHashMap() {{
                put(0, 5.0);
                put(1, 6.0);
            }});
        }});

        OpenMapRealMatrix result = matrix.multiply(matrix);

        assertEquals(matrix.getRowDimension(), result.getRowDimension());
        assertEquals(matrix.getColumnDimension(), result.getColumnDimension());
        assertEquals(matrix.getRowDimension() * matrix.getColumnDimension(), result.size());

        for (int i = 0; i < result.size(); i++) {
            double expectedValue = 0.0;
            for (int j = 0; j < matrix.getColumnDimension(); j++) {
                for (int k = 0; k < matrix.getColumnDimension(); k++) {
                    expectedValue += matrix.getEntry(i / matrix.getColumnDimension(), j) * matrix.getEntry(j, k) * matrix.getEntry(k, i % matrix.getColumnDimension());
                }
            }
            assertEquals(expectedValue, result.getEntry(i));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiplicationWithIncompatibleMatrix() {
        OpenMapRealMatrix a = new OpenMapRealMatrix(new OpenIntToDoubleHashMap() {{
            put(0, new OpenIntToDoubleHashMap() {{
                put(0, 1.0);
                put(1, 2.0);
            }});
            put(1, new OpenIntToDoubleHashMap() {{
                put(0, 3.0);
                put(1, 4.0);
            }});
            put(2, new OpenIntToDoubleHashMap() {{
                put(0, 5.0);
                put(1, 6.0);
            }});
        }});

        RealMatrix b = new BlockRealMatrix(new double[][]{{1.0}, {2.0}, {3.0}});

        a.multiply(b);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap() {{
            put(0, 0.0);
            put(1, 0.0);
            put(2, 0.0);
        }});

        matrix.setEntry(0, 0, 1.0);
        matrix.setEntry(1, 1, -2.0);
        matrix.setEntry(2, 2, 3.0);

        assertEquals(1.0, matrix.getEntry(0, 0));
        assertEquals(-2.0, matrix.getEntry(1, 1));
        assertEquals(3.0, matrix.getEntry(2, 2));
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap() {{
            put(0, 0.0);
            put(1, 0.0);
            put(2, 0.0);
        }});

        matrix.addToEntry(0, 0, 1.0);
        matrix.addToEntry(1, 1, -2.0);
        matrix.addToEntry(2, 2, 3.0);

        assertEquals(1.0, matrix.getEntry(0, 0));
        assertEquals(-2.0, matrix.getEntry(1, 1));
        assertEquals(3.0, matrix.getEntry(2, 2));
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap() {{
            put(0, 1.0);
            put(1, 2.0);
            put(2, 3.0);
        }});

        matrix.multiplyEntry(0, 0, 0.5);
        matrix.multiplyEntry(1, 1, 0.25);
        matrix.multiplyEntry(2, 2, 2.0);

        assertEquals(0.5, matrix.getEntry(0, 0));
        assertEquals(0.5 * 2.0, matrix.getEntry(0, 1), 0.001);
        assertEquals(0.5 * 3.0, matrix.getEntry(0, 2), 0.001);
        assertEquals(2.0 * 0.25, matrix.getEntry(1, 1), 0.001);
        assertEquals(2.0 * 3.0, matrix.getEntry(2, 2));
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testNumberIsTooLargeException() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenIntToDoubleHashMap() {{
            put(0, Long.MAX_VALUE);
        }});
    }

    @Test
    public void testComputeKey() {
        int row = 2;
        int column = 1;

        assertEquals(row * matrix.getColumnDimension() + column, matrix.computeKey(row, column));
    }
}