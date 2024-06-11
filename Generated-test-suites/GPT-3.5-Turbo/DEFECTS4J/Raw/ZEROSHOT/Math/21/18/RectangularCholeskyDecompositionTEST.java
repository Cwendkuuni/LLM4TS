import org.junit.*;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor() {
        // Test case 1
        RealMatrix matrix1 = MatrixUtils.createRealMatrix(new double[][]{{4, 2}, {2, 3}});
        double small1 = 1e-8;
        RectangularCholeskyDecomposition decomposition1 = new RectangularCholeskyDecomposition(matrix1, small1);
        RealMatrix root1 = decomposition1.getRootMatrix();
        int rank1 = decomposition1.getRank();
        assertEquals(2, rank1);
        assertEquals(4.0, root1.getEntry(0, 0), 1e-8);
        assertEquals(2.0, root1.getEntry(1, 0), 1e-8);
        assertEquals(root1.getEntry(1, 0), root1.getEntry(0, 1), 1e-8);

        // Test case 2
        RealMatrix matrix2 = MatrixUtils.createRealMatrix(new double[][]{{1, 0}, {0, 0}});
        double small2 = 1e-8;
        RectangularCholeskyDecomposition decomposition2 = new RectangularCholeskyDecomposition(matrix2, small2);
        RealMatrix root2 = decomposition2.getRootMatrix();
        int rank2 = decomposition2.getRank();
        assertEquals(1, rank2);
        assertEquals(1.0, root2.getEntry(0, 0), 1e-8);
        assertEquals(0.0, root2.getEntry(1, 0), 1e-8);

        // Test case 3
        RealMatrix matrix3 = MatrixUtils.createRealMatrix(new double[][]{{3, 0}, {0, 0}});
        double small3 = 1e-8;
        try {
            RectangularCholeskyDecomposition decomposition3 = new RectangularCholeskyDecomposition(matrix3, small3);
            fail("Exception not thrown");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertEquals(0.0, e.getWrongNumber(), 1e-8);
            assertEquals(1, e.getWrongIndex());
            assertEquals(small3, e.getThreshold(), 1e-8);
        }
    }

}