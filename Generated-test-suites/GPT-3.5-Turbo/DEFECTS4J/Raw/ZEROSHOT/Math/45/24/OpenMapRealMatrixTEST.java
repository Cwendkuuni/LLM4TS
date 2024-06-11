import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testConstructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        assert(matrix.getRowDimension() == 3);
        assert(matrix.getColumnDimension() == 3);
    }

    @Test
    public void testCopy() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix copy = matrix.copy();
        assert(copy.getRowDimension() == matrix.getRowDimension());
        assert(copy.getColumnDimension() == matrix.getColumnDimension());
    }

    @Test
    public void testAdd() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix result = matrix1.add(matrix2);
        assert(result.getRowDimension() == matrix1.getRowDimension());
        assert(result.getColumnDimension() == matrix1.getColumnDimension());
    }

    @Test
    public void testSubtract() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix result = matrix1.subtract(matrix2);
        assert(result.getRowDimension() == matrix1.getRowDimension());
        assert(result.getColumnDimension() == matrix1.getColumnDimension());
    }

    @Test
    public void testMultiply() {
        OpenMapRealMatrix matrix1 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix matrix2 = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix result = matrix1.multiply(matrix2);
        assert(result.getRowDimension() == matrix1.getRowDimension());
        assert(result.getColumnDimension() == matrix2.getColumnDimension());
    }

    @Test
    public void testGetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        double entry = matrix.getEntry(1, 1);
        assert(entry == 0.0);
    }

    @Test
    public void testSetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 1, 5.0);
        double entry = matrix.getEntry(1, 1);
        assert(entry == 5.0);
    }

    @Test
    public void testAddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 1, 5.0);
        matrix.addToEntry(1, 1, 2.0);
        double entry = matrix.getEntry(1, 1);
        assert(entry == 7.0);
    }

    @Test
    public void testMultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        matrix.setEntry(1, 1, 5.0);
        matrix.multiplyEntry(1, 1, 2.0);
        double entry = matrix.getEntry(1, 1);
        assert(entry == 10.0);
    }
}