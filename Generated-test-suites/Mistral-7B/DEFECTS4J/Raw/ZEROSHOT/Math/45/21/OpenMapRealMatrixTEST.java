import org.apache.commons.math3.linear.AbstractRealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.OpenIntToDoubleHashMap;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SparseRealMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

public class OpenMapRealMatrixTest {

    @Test
    public void testOpenMapRealMatrixCreation() {
        int rowDimension = 3;
        int columnDimension = 2;
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rowDimension, columnDimension);
        assertEquals(rowDimension, matrix.getRowDimension());
        assertEquals(columnDimension, matrix.getColumnDimension());
    }

    @Test
    public void testOpenMapRealMatrixCopy() {
        int rowDimension = 3;
        int columnDimension = 2;
        OpenMapRealMatrix originalMatrix = new OpenMapRealMatrix(rowDimension, columnDimension);
        OpenMapRealMatrix copiedMatrix = originalMatrix.copy();
        assertEquals(originalMatrix, copiedMatrix);
    }

    @Test
    public void testOpenMapRealMatrixAddition() {
        int rowDimension = 3;
        int columnDimension = 2;

        OpenMapRealMatrix firstMatrix = new OpenMapRealMatrix(rowDimension, columnDimension);
        firstMatrix.setEntry(1, 1, 1.0);
        firstMatrix.setEntry(1, 2, 2.0);
        firstMatrix.setEntry(2, 1, 3.0);
        firstMatrix.setEntry(2, 2, 4.0);

        OpenMapRealMatrix secondMatrix = new OpenMapRealMatrix(rowDimension, columnDimension);
        secondMatrix.setEntry(0, 0, 1.0);
        secondMatrix.setEntry(0, 1, 2.0);
        secondMatrix.setEntry(1, 1, 3.0);
        secondMatrix.setEntry(1, 2, 4.0);
        secondMatrix.setEntry(2, 0, 5.0);
        secondMatrix.setEntry(2, 1, 6.0);
        secondMatrix.setEntry(2, 2, 7.0);

        OpenMapRealMatrix sumMatrix = firstMatrix.add(secondMatrix);

        OpenMapRealMatrix expectedSumMatrix = new OpenMapRealMatrix(rowDimension, columnDimension);
        expectedSumMatrix.setEntry(1, 1, 2.0);
        expectedSumMatrix.setEntry(1, 2, 4.0);
        expectedSumMatrix.setEntry(2, 0, 6.0);
        expectedSumMatrix.setEntry(2, 1, 7.0);
        expectedSumMatrix.setEntry(2, 2, 11.0);

        assertEquals(expectedSumMatrix, sumMatrix);
    }

    @Test
    public void testOpenMapRealMatrixSubtraction() {
        int rowDimension = 3;
        int columnDimension = 2;

        OpenMapRealMatrix firstMatrix = new OpenMapRealMatrix(rowDimension, columnDimension);
        firstMatrix.setEntry(1, 1, 1.0);
        firstMatrix.setEntry(1, 2, 2.0);
        firstMatrix.setEntry(2, 1, 3.0);
        firstMatrix.setEntry(2, 2, 4.0);

        OpenMapRealMatrix secondMatrix = new OpenMapRealMatrix(rowDimension, columnDimension);
        secondMatrix.setEntry(0, 0, 1.0);
        secondMatrix.setEntry(0, 1, 2.0);
        secondMatrix.setEntry(1, 1, 3.0);
        secondMatrix.setEntry(1, 2, 4.0);
        secondMatrix.setEntry(2, 0, 5.0);
        secondMatrix.setEntry(2, 1, 6.0);
        secondMatrix.setEntry(2, 2, 7.0);

        OpenMapRealMatrix differenceMatrix = firstMatrix.subtract(secondMatrix);

        OpenMapRealMatrix expectedDifferenceMatrix = new OpenMapRealMatrix(rowDimension, columnDimension);
        expectedDifferenceMatrix.setEntry(1, 1, 0.0);
        expectedDifferenceMatrix.setEntry(1, 2, -2.0);
        expectedDifferenceMatrix.setEntry(2, 1, -2.0);
        expectedDifferenceMatrix.setEntry(2, 2, 3.0);

        assertEquals(expectedDifferenceMatrix, differenceMatrix);
    }

    @Test
    public void testOpenMapRealMatrixMultiplication() {
        int rowDimensionFirst = 3;
        int columnDimensionFirst = 2;
        int rowDimensionSecond = 2;
        int columnDimensionSecond = 3;

        OpenMapRealMatrix firstMatrix = new OpenMapRealMatrix(rowDimensionFirst, columnDimensionFirst);
        firstMatrix.setEntry(1, 1, 1.0);
        firstMatrix.setEntry(1, 2, 2.0);
        firstMatrix.setEntry(2, 1, 3.0);
        firstMatrix.setEntry(2, 2, 4.0);

        OpenMapRealMatrix secondMatrix = new OpenMapRealMatrix(rowDimensionSecond, columnDimensionSecond);
        secondMatrix.setEntry(0, 0, 1.0);
        secondMatrix.setEntry(0, 1, 2.0);
        secondMatrix.setEntry(0, 2, 3.0);
        secondMatrix.setEntry(1, 0, 4.0);
        secondMatrix.setEntry(1, 1, 5.0);
        secondMatrix.setEntry(1, 2, 6.0);

        OpenMapRealMatrix productMatrix = firstMatrix.multiply(secondMatrix);

        OpenMapRealMatrix expectedProductMatrix = new OpenMapRealMatrix(rowDimensionFirst, columnDimensionSecond);
        expectedProductMatrix.setEntry(1, 0, 4.0);
        expectedProductMatrix.setEntry(1, 1, 7.0);
        expectedProductMatrix.setEntry(1, 2, 10.0);
        expectedProductMatrix.setEntry(2, 0, 13.0);
        expectedProductMatrix.setEntry(2, 1, 18.0);
        expectedProductMatrix.setEntry(2, 2, 23.0);

        assertEquals(expectedProductMatrix, productMatrix);
    }

    @Test
    public void testOpenMapRealMatrixGetEntry() {
        int rowDimension = 3;
        int columnDimension = 2;

        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rowDimension, columnDimension);
        matrix.setEntry(1, 1, 1.0);
        matrix.setEntry(1, 2, 2.0);
        matrix.setEntry(2, 1, 3.0);
        matrix.setEntry(2, 2, 4.0);

        for (int i = 0; i < rowDimension; i++) {
            for (int j = 0; j < columnDimension; j++) {
                double expectedValue = matrix.getEntry(i, j);
                double actualValue = matrix.getEntry(i, j);
                assertEquals(expectedValue, actualValue);
            }
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testOpenMapRealMatrixSetEntryWithTooLargeValue() {
        int rowDimension = 3;
        int columnDimension = 2;

        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rowDimension, columnDimension);
        MatrixUtils.checkRowIndex(matrix, 3);
        matrix.setEntry(3, 1, Double.MAX_VALUE);
    }

    @Test
    public void testOpenMapRealMatrixSetEntry() {
        int rowDimension = 3;
        int columnDimension = 2;

        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rowDimension, columnDimension);
        matrix.setEntry(1, 1, 1.0);
        matrix.setEntry(1, 2, 2.0);
        matrix.setEntry(2, 1, 3.0);
        matrix.setEntry(2, 2, 4.0);

        for (int i = 0; i < rowDimension; i++) {
            for (int j = 0; j < columnDimension; j++) {
                double value = i + j;
                matrix.setEntry(i, j, value);
                assertEquals(value, matrix.getEntry(i, j));
            }
        }
    }

    @Test
    public void testOpenMapRealMatrixAddToEntry() {
        int rowDimension = 3;
        int columnDimension = 2;

        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rowDimension, columnDimension);
        matrix.setEntry(1, 1, 1.0);
        matrix.setEntry(1, 2, 2.0);
        matrix.setEntry(2, 1, 3.0);
        matrix.setEntry(2, 2, 4.0);

        for (int i = 0; i < rowDimension; i++) {
            for (int j = 0; j < columnDimension; j++) {
                double increment = i + j;
                matrix.addToEntry(i, j, increment);
                assertEquals(i + j + 1.0, matrix.getEntry(i, j), 0.0);
            }
        }
    }

    @Test
    public void testOpenMapRealMatrixMultiplyEntry() {
        int rowDimension = 3;
        int columnDimension = 2;

        OpenMapRealMatrix matrix = new OpenMapRealMatrix(rowDimension, columnDimension);
        matrix.setEntry(1, 1, 1.0);
        matrix.setEntry(1, 2, 2.0);
        matrix.setEntry(2, 1, 3.0);
        matrix.setEntry(2, 2, 4.0);

        for (int i = 0; i < rowDimension; i++) {
            for (int j = 0; j < columnDimension; j++) {
                double factor = i + j;
                matrix.multiplyEntry(i, j, factor);
                assertEquals((i + j) * matrix.getEntry(i, j), matrix.getEntry(i, j), 0.0);
            }
        }
    }

    @Test
    public void testOpenMapRealMatrixMultiply() {
        int rowDimensionFirst = 3;
        int columnDimensionFirst = 2;
        int rowDimensionSecond = 2;
        int columnDimensionSecond = 3;

        OpenMapRealMatrix firstMatrix = new OpenMapRealMatrix(rowDimensionFirst, columnDimensionFirst);
        firstMatrix.setEntry(1, 1, 1.0);
        firstMatrix.setEntry(1, 2, 2.0);
        firstMatrix.setEntry(2, 1, 3.0);
        firstMatrix.setEntry(2, 2, 4.0);

        OpenMapRealMatrix secondMatrix = new OpenMapRealMatrix(rowDimensionSecond, columnDimensionSecond);
        secondMatrix.setEntry(0, 0, 1.0);
        secondMatrix.setEntry(0, 1, 2.0);
        secondMatrix.setEntry(0, 2, 3.0);
        secondMatrix.setEntry(1, 0, 4.0);
        secondMatrix.setEntry(1, 1, 5.0);
        secondMatrix.setEntry(1, 2, 6.0);

        OpenMapRealMatrix productMatrix = firstMatrix.multiply(secondMatrix);

        OpenMapRealMatrix expectedProductMatrix = new OpenMapRealMatrix(rowDimensionFirst, columnDimensionSecond);
        expectedProductMatrix.setEntry(1, 0, 4.0);
        expectedProductMatrix.setEntry(1, 1, 7.0);
        expectedProductMatrix.setEntry(1, 2, 10.0);
        expectedProductMatrix.setEntry(2, 0, 13.0);
        expectedProductMatrix.setEntry(2, 1, 18.0);
        expectedProductMatrix.setEntry(2, 2, 23.0);

        assertEquals(expectedProductMatrix, productMatrix);
    }

    @Test
    public void testOpenMapRealMatrixDenseMatrixMultiplication() {
        int rowDimensionFirst = 3;
        int columnDimensionFirst = 2;
        int rowDimensionSecond = 2;
        int columnDimensionSecond = 3;

        OpenMapRealMatrix firstMatrix = new OpenMapRealMatrix(rowDimensionFirst, columnDimensionFirst);
        firstMatrix.setEntry(1, 1, 1.0);
        firstMatrix.setEntry(1, 2, 2.0);
        firstMatrix.setEntry(2, 1, 3.0);
        firstMatrix.setEntry(2, 2, 4.0);

        OpenMapRealMatrix secondMatrix = new OpenMapRealMatrix(rowDimensionSecond, columnDimensionSecond);
        secondMatrix.setEntry(0, 0, 1.0);
        secondMatrix.setEntry(0, 1, 2.0);
        secondMatrix.setEntry(0, 2, 3.0);
        secondMatrix.setEntry(1, 0, 4.0);
        secondMatrix.setEntry(1, 1, 5.0);
        secondMatrix.setEntry(1, 2, 6.0);

        OpenMapRealMatrix productMatrix = firstMatrix.multiply(secondMatrix);

        RealMatrix expectedProductMatrix = new Array2DRowRealMatrix(new double[][] {
            {4.0, 7.0, 10.0},
            {13.0, 18.0, 23.0}
        });

        assertEquals(expectedProductMatrix, productMatrix);

        RealMatrix denseProductMatrix = new BlockRealMatrix(new double[][] {
            {4.0, 7.0, 10.0},
            {13.0, 18.0, 23.0}
        });

        assertEquals(denseProductMatrix, productMatrix.transpose());
    }
}