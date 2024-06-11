import org.junit.Test;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;


public class RectangularCholeskyDecompositionTest {

    @Test
    public void testDecomposition() {
        // Test cases for decomposition
        RealMatrix matrix1 = MatrixUtils.createRealMatrix(new double[][] {{1.0, 0.0}, {0.0, 1.0}});
        RectangularCholeskyDecomposition decom1 = new RectangularCholeskyDecomposition(matrix1, 0.0);
        RealMatrix root1 = decom1.getRootMatrix();
        int rank1 = decom1.getRank();
        assertEquals(2, rank1);
        assertEquals(1.0, root1.getEntry(0, 0), 0.0);
        assertEquals(0.0, root1.getEntry(0, 1), 0.0);
        assertEquals(0.0, root1.getEntry(1, 0), 0.0);
        assertEquals(1.0, root1.getEntry(1, 1), 0.0);

        RealMatrix matrix2 = MatrixUtils.createRealMatrix(new double[][] {{2.0, 1.0}, {1.0, 2.0}});
        RectangularCholeskyDecomposition decom2 = new RectangularCholeskyDecomposition(matrix2, 0.0);
        RealMatrix root2 = decom2.getRootMatrix();
        int rank2 = decom2.getRank();
        assertEquals(2, rank2);
        assertEquals(1.41421356, root2.getEntry(0, 0), 1e-5);
        assertEquals(0.70710678, root2.getEntry(0, 1), 1e-5);
        assertEquals(0.0, root2.getEntry(1, 0), 0.0);
        assertEquals(1.22474487, root2.getEntry(1, 1), 1e-5);

        RealMatrix matrix3 = MatrixUtils.createRealMatrix(new double[][] {{1.0, 2.0}, {2.0, 4.0}});
        RectangularCholeskyDecomposition decom3 = new RectangularCholeskyDecomposition(matrix3, 0.0);
        RealMatrix root3 = decom3.getRootMatrix();
        int rank3 = decom3.getRank();
        assertEquals(1, rank3);
        assertEquals(1.0, root3.getEntry(0, 0), 0.0);
        assertEquals(0.0, root3.getEntry(0, 1), 0.0);
        assertEquals(2.0, root3.getEntry(1, 0), 0.0);
        assertEquals(0.0, root3.getEntry(1, 1), 0.0);

        RealMatrix matrix4 = MatrixUtils.createRealMatrix(new double[][] {{1.0, 1.0, 1.0}, {1.0, 2.0, 2.0}, {1.0, 2.0, 3.0}});
        RectangularCholeskyDecomposition decom4 = new RectangularCholeskyDecomposition(matrix4, 0.0);
        RealMatrix root4 = decom4.getRootMatrix();
        int rank4 = decom4.getRank();
        assertEquals(2, rank4);
        assertEquals(1.0, root4.getEntry(0, 0), 0.0);
        assertEquals(0.0, root4.getEntry(0, 1), 0.0);
        assertEquals(-1.0, root4.getEntry(0, 2), 0.0);
        assertEquals(0.0, root4.getEntry(1, 0), 0.0);
        assertEquals(1.0, root4.getEntry(1, 1), 0.0);
        assertEquals(1.0, root4.getEntry(1, 2), 0.0);

        RealMatrix matrix5 = MatrixUtils.createRealMatrix(new double[][] {{1.0, 2.0, 3.0}, {2.0, 4.0, 6.0}, {3.0, 6.0, 9.0}});
        RectangularCholeskyDecomposition decom5 = new RectangularCholeskyDecomposition(matrix5, 0.0);
        RealMatrix root5 = decom5.getRootMatrix();
        int rank5 = decom5.getRank();
        assertEquals(1, rank5);
        assertEquals(1.0, root5.getEntry(0, 0), 0.0);
        assertEquals(0.0, root5.getEntry(0, 1), 0.0);
        assertEquals(2.0, root5.getEntry(0, 2), 0.0);
        assertEquals(2.0, root5.getEntry(1, 0), 0.0);
        assertEquals(0.0, root5.getEntry(1, 1), 0.0);
        assertEquals(0.0, root5.getEntry(1, 2), 0.0);
        
    }

}