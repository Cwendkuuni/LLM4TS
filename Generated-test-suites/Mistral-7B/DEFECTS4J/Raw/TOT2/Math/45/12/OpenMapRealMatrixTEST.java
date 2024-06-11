import org.apache.commons.math3.linear.OpenMapRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link OpenMapRealMatrix}.
 */
public class OpenMapRealMatrixTest {
    private OpenMapRealMatrix matrix;

    /**
     * Initialize a 2x2 OpenMapRealMatrix.
     */
    @Before
    public void setUp() {
        matrix = new OpenMapRealMatrix(2, 2);
    }

    /**
     * Test constructor with correct dimensions.
     */
    @Test
    public void testConstructor() {
        Assert.assertEquals(2, matrix.getRowDimension());
        Assert.assertEquals(2, matrix.getColumnDimension());
    }

    /**
     * Test getEntry() with valid indices.
     */
    @Test
    public void testGetEntry() {
        matrix.setEntry(0, 0, 3.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 1.0);
        matrix.setEntry(1, 1, 4.0);

        Assert.assertEquals(3.0, matrix.getEntry(0, 0), 1e-14);
        Assert.assertEquals(2.0, matrix.getEntry(0, 1), 1e-14);
        Assert.assertEquals(1.0, matrix.getEntry(1, 0), 1e-14);
        Assert.assertEquals(4.0, matrix.getEntry(1, 1), 1e-14);
    }

    /**
     * Test getEntry() with invalid row index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetEntryInvalidRow() {
        matrix.getEntry(3, 0);
    }

    /**
     * Test getEntry() with invalid column index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetEntryInvalidColumn() {
        matrix.getEntry(0, 3);
    }

    /**
     * Test setEntry() with valid indices.
     */
    @Test
    public void testSetEntry() {
        matrix.setEntry(0, 0, 3.0);
        matrix.setEntry(0, 1, 2.0);
        matrix.setEntry(1, 0, 1.0);
        matrix.setEntry(1, 1, 4.0);

        matrix.setEntry(0, 0, 5.0);
        Assert.assertEquals(5.0, matrix.getEntry(0, 0), 1e-14);

        matrix.setEntry(1, 1, 6.0);
        Assert.assertEquals(6.0, matrix.getEntry(1, 1), 1e-14);
    }

    /**
     * Test setEntry() with invalid row index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetEntryInvalidRow() {
        matrix.setEntry(3, 0, 3.0);
    }

    /**
     * Test setEntry() with invalid column index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetEntryInvalidColumn() {
        matrix.setEntry(0, 3, 3.0);
    }

    /**
     * Test add() with valid matrices.
     */
    @Test
    public void testAdd() {
        OpenMapRealMatrix matrixA = createMatrix(1, 1, 1.0);
        OpenMapRealMatrix matrixB = createMatrix(1, 1, 2.0);
        OpenMapRealMatrix result = matrixA.add(matrixB);

        Assert.assertEquals(1, result.getRowDimension());
        Assert.assertEquals(1, result.getColumnDimension());
        Assert.assertEquals(3.0, result.getEntry(0, 0), 1e-14);
    }

    /**
     * Test add() with incompatible dimensions.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAddIncompatibleDimensions() {
        OpenMapRealMatrix matrixA = createMatrix(1, 1, 1.0);
        OpenMapRealMatrix matrixB = createMatrix(2, 1, 2.0);
        matrixA.add(matrixB);
    }

    /**
     * Test subtract() with valid matrices.
     */
    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrixA = createMatrix(1, 1, 1.0);
        OpenMapRealMatrix matrixB = createMatrix(1, 1, -1.0);
        OpenMapRealMatrix result = matrixA.subtract(matrixB);

        Assert.assertEquals(1, result.getRowDimension());
        Assert.assertEquals(1, result.getColumnDimension());
        Assert.assertEquals(0.0, result.getEntry(0, 0), 1e-14);
    }

    /**
     * Test subtract() with incompatible dimensions.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSubtractIncompatibleDimensions() {
        OpenMapRealMatrix matrixA = createMatrix(1, 1, 1.0);
        OpenMapRealMatrix matrixB = createMatrix(2, 1, 2.0);
        matrixA.subtract(matrixB);
    }

    /**
     * Test multiply() with valid matrices.
     */
    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrixA = createMatrix(2, 1, 1.0, 2.0);
        OpenMapRealMatrix matrixB = createMatrix(1, 2, 3.0, 4.0);
        RealMatrix result = matrixA.multiply(matrixB);

        OpenMapRealMatrix expectedResult = createMatrix(2, 2,
                3.0 * 1.0 + 4.0 * 2.0,
                3.0 * 2.0 + 4.0 * 2.0);

        for (int i = 0; i < result.getRowDimension(); i++) {
            for (int j = 0; j < result.getColumnDimension(); j++) {
                Assert.assertEquals(expectedResult.getEntry(i, j),
                        result.getEntry(i, j), 1e-14);
            }
        }
    }

    /**
     * Test multiply() with incompatible dimensions.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMultiplyIncompatibleDimensions() {
        OpenMapRealMatrix matrixA = createMatrix(2, 1, 1.0, 2.0);
        OpenMapRealMatrix matrixB = createMatrix(1, 3, 3.0, 4.0, 5.0);
        matrixA.multiply(matrixB);
    }

    /**
     * Test copy() with a valid matrix.
     */
    @Test
    public void testCopy() {
        OpenMapRealMatrix matrixA = createMatrix(1, 1, 1.0);
        OpenMapRealMatrix matrixB = matrixA.copy();

        Assert.assertEquals(1, matrixB.getRowDimension());
        Assert.assertEquals(1, matrixB.getColumnDimension());
        Assert.assertEquals(1.0, matrixB.getEntry(0, 0), 1e-14);
    }

    /**
     * Test createMatrix() with a valid number of rows and columns.
     */
    @Test
    public void testCreateMatrix() {
        OpenMapRealMatrix matrix = OpenMapRealMatrix.createMatrix(2, 2);
        Assert.assertEquals(2, matrix.getRowDimension());
        Assert.assertEquals(2, matrix.getColumnDimension());
    }

    /**
     * Test createMatrix() with negative number of rows.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateMatrixNegativeRows() {
        OpenMapRealMatrix.createMatrix(-1, 1);
    }

    /**
     * Test createMatrix() with negative number of columns.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateMatrixNegativeColumns() {
        OpenMapRealMatrix.createMatrix(1, -1);
    }

    /**
     * Test getColumnDimension() with a valid matrix.
     */
    @Test
    public void testGetColumnDimension() {
        OpenMapRealMatrix matrix = createMatrix(2, 2);
        Assert.assertEquals(2, matrix.getColumnDimension());
    }

    /**
     * Test getColumnDimension() with an empty matrix.
     */
    @Test
    public void testGetColumnDimensionEmpty() {
        OpenMapRealMatrix matrix = createMatrix(1, 0);
        Assert.assertEquals(0, matrix.getColumnDimension());
    }

    /**
     * Test getRowDimension() with a valid matrix.
     */
    @Test
    public void testGetRowDimension() {
        OpenMapRealMatrix matrix = createMatrix(2, 2);
        Assert.assertEquals(2, matrix.getRowDimension());
    }

    /**
     * Test getRowDimension() with an empty matrix.
     */
    @Test
    public void testGetRowDimensionEmpty() {
        OpenMapRealMatrix matrix = createMatrix(0, 1);
        Assert.assertEquals(0, matrix.getRowDimension());
    }

    /**
     * Test addToEntry() with valid indices and value.
     */
    @Test
    public void testAddToEntry() {
        matrix.addToEntry(0, 0, 1.0);
        Assert.assertEquals(4.0, matrix.getEntry(0, 0), 1e-14);
    }

    /**
     * Test addToEntry() with invalid row index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddToEntryInvalidRow() {
        matrix.addToEntry(3, 0, 1.0);
    }

    /**
     * Test addToEntry() with invalid column index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddToEntryInvalidColumn() {
        matrix.addToEntry(0, 3, 1.0);
    }

    /**
     * Test multiplyEntry() with valid indices and value.
     */
    @Test
    public void testMultiplyEntry() {
        matrix.multiplyEntry(0, 0, 2.0);
        Assert.assertEquals(2.0 * 3.0, matrix.getEntry(0, 0), 1e-14);
    }

    /**
     * Test multiplyEntry() with invalid row index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiplyEntryInvalidRow() {
        matrix.multiplyEntry(3, 0, 2.0);
    }

    /**
     * Test multiplyEntry() with invalid column index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testMultiplyEntryInvalidColumn() {
        matrix.multiplyEntry(0, 3, 2.0);
    }

    private OpenMapRealMatrix createMatrix(int rows, int columns, double... values) {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rows, columns);
        for (int i = 0; i < values.length; i++) {
            matrix.setEntry(i / columns, i % columns, values[i]);
        }
        return matrix;
    }

    private OpenMapRealMatrix createMatrix(int rows, int columns) {
        return createMatrix(rows, columns, (double) 0);
    }
}