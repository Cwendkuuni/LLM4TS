import org.apache.commons.math.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {

    @Test
    public void testGetL() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        CholeskyDecomposition choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix lMatrix = choleskyDecomposition.getL();
        assertEquals(2.0, lMatrix.getEntry(0, 0), 0);
        assertEquals(6.0, lMatrix.getEntry(1, 0), 0);
        assertEquals(-8.0, lMatrix.getEntry(2, 0), 0);
        assertEquals(3.0, lMatrix.getEntry(1, 1), 0);
        assertEquals(-1.0, lMatrix.getEntry(2, 1), 0);
        assertEquals(7.0, lMatrix.getEntry(2, 2), 0);
    }

    @Test
    public void testGetLT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        CholeskyDecomposition choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix lTMatrix = choleskyDecomposition.getLT();
        assertEquals(2.0, lTMatrix.getEntry(0, 0), 0);
        assertEquals(3.0, lTMatrix.getEntry(1, 0), 0);
        assertEquals(7.0, lTMatrix.getEntry(2, 0), 0);
        assertEquals(6.0, lTMatrix.getEntry(1, 1), 0);
        assertEquals(-1.0, lTMatrix.getEntry(2, 1), 0);
        assertEquals(-8.0, lTMatrix.getEntry(2, 2), 0);
    }

    @Test
    public void testGetDeterminant() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        CholeskyDecomposition choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        double determinant = choleskyDecomposition.getDeterminant();
        assertEquals(396.0, determinant, 0);
    }

    @Test
    public void testGetInverse() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        CholeskyDecomposition choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix inverse = choleskyDecomposition.getInverse();
        RealMatrix identity = MatrixUtils.createRealIdentityMatrix(matrix.getRowDimension());
        RealMatrix result = matrix.multiply(inverse);
        double epsilon = 1e-15;
        for (int i = 0; i < result.getRowDimension(); i++) {
            for (int j = 0; j < result.getColumnDimension(); j++) {
                if (i == j) {
                    assertEquals(1.0, result.getEntry(i, j), epsilon);
                } else {
                    assertEquals(0.0, result.getEntry(i, j), epsilon);
                }
            }
        }
    }
}

