import org.apache.commons.math3.linear.AbstractRealMatrix;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.OpenIntToDoubleHashMap;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SparseRealMatrix;
import org.apache.commons.math3.util.MatrixUtils;
import org.junit.Assert;
import org.junit.Test;

public class OpenMapRealMatrixTest {

    @Test
    public void testOpenMapRealMatrix_Constructor() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(3, 3);
        Assert.assertEquals(3, matrix.getRowDimension());
        Assert.assertEquals(3, matrix.getColumnDimension());
        Assert.assertNull(matrix.getEntry(0, 0));
    }

    @Test
    public void testOpenMapRealMatrix_CopyConstructor() {
        OpenMapRealMatrix originalMatrix = new OpenMapRealMatrix(3, 3);
        OpenMapRealMatrix copiedMatrix = new OpenMapRealMatrix(originalMatrix);
        Assert.assertEquals(originalMatrix.getRowDimension(), copiedMatrix.getRowDimension());
        Assert.assertEquals(originalMatrix.getColumnDimension(), copiedMatrix.getColumnDimension());
        Assert.assertNull(copiedMatrix.getEntry(0, 0));
    }

    @Test
    public void testOpenMapRealMatrix_Addition() {
        OpenMapRealMatrix leftMatrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3).add(new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3).identity())));
        OpenMapRealMatrix rightMatrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3).identity());
        OpenMapRealMatrix result = leftMatrix.add(rightMatrix);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Assert.assertEquals(i == j ? 2.0 : 0.0, result.getEntry(i, j), 0.001);
            }
        }
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testOpenMapRealMatrix_Addition_DimensionMismatch() {
        OpenMapRealMatrix leftMatrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        OpenMapRealMatrix rightMatrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3));
        leftMatrix.add(rightMatrix);
    }

    @Test
    public void testOpenMapRealMatrix_Subtraction() {
        OpenMapRealMatrix leftMatrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3).add(new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3).identity())));
        OpenMapRealMatrix rightMatrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3).identity());
        OpenMapRealMatrix result = leftMatrix.subtract(rightMatrix);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Assert.assertEquals(i == j ? 0.0 : 0.0, result.getEntry(i, j), 0.001);
            }
        }
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testOpenMapRealMatrix_Subtraction_DimensionMismatch() {
        OpenMapRealMatrix leftMatrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3));
        OpenMapRealMatrix rightMatrix = new OpenMapRealMatrix(new OpenMapRealMatrix(2, 3));
        leftMatrix.subtract(rightMatrix);
    }

    @Test
    public void testOpenMapRealMatrix_Multiplication() {
        OpenMapRealMatrix leftMatrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3).identity());
        OpenMapRealMatrix rightMatrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3).identity());
        RealMatrix result = leftMatrix.multiply(rightMatrix);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Assert.assertEquals(i == j ? 1.0 : 0.0, result.getEntry(i, j), 0.001);
            }
        }
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testOpenMapRealMatrix_Multiplication_DimensionMismatch() {
        OpenMapRealMatrix leftMatrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3).identity());
        OpenMapRealMatrix rightMatrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 2));
        leftMatrix.multiply(rightMatrix);
    }

    @Test
    public void testOpenMapRealMatrix_GetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3).identity());
        Assert.assertEquals(0.0, matrix.getEntry(0, 0), 0.0);
        matrix.setEntry(0, 0, 1.0);
        Assert.assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
    }

    @Test(expected = org.apache.commons.math3.exception.IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrix_GetEntry_InvalidIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3).identity());
        matrix.getEntry(3, 3);
    }

    @Test
    public void testOpenMapRealMatrix_SetEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3).identity());
        matrix.setEntry(0, 0, 1.0);
        Assert.assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
    }

    @Test(expected = org.apache.commons.math3.exception.IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrix_SetEntry_InvalidIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3).identity());
        matrix.setEntry(3, 3, 1.0);
    }

    @Test
    public void testOpenMapRealMatrix_AddToEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3).identity());
        matrix.addToEntry(0, 0, 1.0);
        Assert.assertEquals(1.0, matrix.getEntry(0, 0), 0.0);
    }

    @Test(expected = org.apache.commons.math3.exception.IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrix_AddToEntry_InvalidIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3).identity());
        matrix.addToEntry(3, 3, 1.0);
    }

    @Test
    public void testOpenMapRealMatrix_MultiplyEntry() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3).identity());
        matrix.multiplyEntry(0, 0, 2.0);
        Assert.assertEquals(2.0, matrix.getEntry(0, 0), 0.0);
    }

    @Test(expected = org.apache.commons.math3.exception.IndexOutOfBoundsException.class)
    public void testOpenMapRealMatrix_MultiplyEntry_InvalidIndex() {
        OpenMapRealMatrix matrix = new OpenMapRealMatrix(new OpenMapRealMatrix(3, 3).identity());
        matrix.multiplyEntry(3, 3, 2.0);
    }
}