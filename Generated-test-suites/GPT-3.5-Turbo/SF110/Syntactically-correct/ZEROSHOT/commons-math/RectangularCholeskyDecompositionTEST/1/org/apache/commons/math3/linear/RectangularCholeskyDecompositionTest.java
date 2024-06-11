import org.apache.commons.math3.linear.*;
import org.junit.*;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    // Test case for constructor with matrix and small value
    @Test
    public void testConstructorWithMatrixAndSmallValue() {
        // Create a test matrix
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(3, 3);
        testMatrix.setEntry(0, 0, 4.0);
        testMatrix.setEntry(0, 1, 2.0);
        testMatrix.setEntry(0, 2, 1.0);
        testMatrix.setEntry(1, 0, 2.0);
        testMatrix.setEntry(1, 1, 5.0);
        testMatrix.setEntry(1, 2, 3.0);
        testMatrix.setEntry(2, 0, 1.0);
        testMatrix.setEntry(2, 1, 3.0);
        testMatrix.setEntry(2, 2, 6.0);

        // Create an instance of RectangularCholeskyDecomposition using the test matrix and small value
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(testMatrix, 0.1);

        // Test the getRootMatrix method
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        assertEquals(3, rootMatrix.getRowDimension());
        assertEquals(3, rootMatrix.getColumnDimension());
        assertEquals(2.0, rootMatrix.getEntry(0, 0), 0.001);
        assertEquals(0.0, rootMatrix.getEntry(0, 1), 0.001);
        assertEquals(0.0, rootMatrix.getEntry(0, 2), 0.001);
        assertEquals(1.0, rootMatrix.getEntry(1, 0), 0.001);
        assertEquals(2.0, rootMatrix.getEntry(1, 1), 0.001);
        assertEquals(0.0, rootMatrix.getEntry(1, 2), 0.001);
        assertEquals(0.5, rootMatrix.getEntry(2, 0), 0.001);
        assertEquals(1.0, rootMatrix.getEntry(2, 1), 0.001);
        assertEquals(1.0, rootMatrix.getEntry(2, 2), 0.001);

        // Test the getRank method
        assertEquals(3, decomposition.getRank());
    }

    // Test case for constructor with matrix only
    @Test
    public void testConstructorWithMatrixOnly() {
        // Create a test matrix
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(3, 3);
        testMatrix.setEntry(0, 0, 4.0);
        testMatrix.setEntry(0, 1, 2.0);
        testMatrix.setEntry(0, 2, 1.0);
        testMatrix.setEntry(1, 0, 2.0);
        testMatrix.setEntry(1, 1, 5.0);
        testMatrix.setEntry(1, 2, 3.0);
        testMatrix.setEntry(2, 0, 1.0);
        testMatrix.setEntry(2, 1, 3.0);
        testMatrix.setEntry(2, 2, 6.0);

        // Create an instance of RectangularCholeskyDecomposition using the test matrix only
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(testMatrix);

        // Test the getRootMatrix method
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        assertEquals(3, rootMatrix.getRowDimension());
        assertEquals(3, rootMatrix.getColumnDimension());
        assertEquals(2.0, rootMatrix.getEntry(0, 0), 0.001);
        assertEquals(0.0, rootMatrix.getEntry(0, 1), 0.001);
        assertEquals(0.0, rootMatrix.getEntry(0, 2), 0.001);
        assertEquals(1.0, rootMatrix.getEntry(1, 0), 0.001);
        assertEquals(2.0, rootMatrix.getEntry(1, 1), 0.001);
        assertEquals(0.0, rootMatrix.getEntry(1, 2), 0.001);
        assertEquals(0.5, rootMatrix.getEntry(2, 0), 0.001);
        assertEquals(1.0, rootMatrix.getEntry(2, 1), 0.001);
        assertEquals(1.0, rootMatrix.getEntry(2, 2), 0.001);

        // Test the getRank method
        assertEquals(3, decomposition.getRank());
    }

}