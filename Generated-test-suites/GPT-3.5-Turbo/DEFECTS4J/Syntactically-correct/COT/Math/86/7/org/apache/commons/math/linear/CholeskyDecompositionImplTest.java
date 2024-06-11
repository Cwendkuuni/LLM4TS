import org.junit.Test;
import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {

    private CholeskyDecompositionImpl cholesky;

    // Test constructor
    @Test
    public void testConstructor() {
        double[][] matrixData = {{4, 12, -16},
                                 {12, 37, -43},
                                 {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);

        cholesky = new CholeskyDecompositionImpl(matrix);

        assertNotNull(cholesky.getL());
        assertNotNull(cholesky.getLT());
        assertEquals(matrix, cholesky.getL().multiply(cholesky.getLT()));
    }

    // Test getL method
    @Test
    public void testGetL() {
        double[][] matrixData = {{4, 12, -16},
                                 {12, 37, -43},
                                 {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);

        cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = cholesky.getL();

        assertEquals(3, l.getRowDimension());
        assertEquals(3, l.getColumnDimension());
        assertEquals(2, l.getEntry(0, 0), 0.0);
        assertEquals(3, l.getEntry(1, 0), 0.0);
        assertEquals(-4, l.getEntry(2, 0), 0.0);
        assertEquals(0, l.getEntry(0, 1), 0.0);
        assertEquals(4, l.getEntry(1, 1), 0.0);
        assertEquals(-5, l.getEntry(2, 1), 0.0);
        assertEquals(0, l.getEntry(0, 2), 0.0);
        assertEquals(0, l.getEntry(1, 2), 0.0);
        assertEquals(7, l.getEntry(2, 2), 0.0);
    }

    // Test getLT method
    @Test
    public void testGetLT() {
        double[][] matrixData = {{4, 12, -16},
                                 {12, 37, -43},
                                 {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);

        cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix lt = cholesky.getLT();

        assertEquals(3, lt.getRowDimension());
        assertEquals(3, lt.getColumnDimension());
        assertEquals(2, lt.getEntry(0, 0), 0.0);
        assertEquals(0, lt.getEntry(1, 0), 0.0);
        assertEquals(0, lt.getEntry(2, 0), 0.0);
        assertEquals(3, lt.getEntry(0, 1), 0.0);
        assertEquals(4, lt.getEntry(1, 1), 0.0);
        assertEquals(0, lt.getEntry(2, 1), 0.0);
        assertEquals(-4, lt.getEntry(0, 2), 0.0);
        assertEquals(-5, lt.getEntry(1, 2), 0.0);
        assertEquals(7, lt.getEntry(2, 2), 0.0);
    }

    // Test getDeterminant method
    @Test
    public void testGetDeterminant() {
        double[][] matrixData = {{4, 12, -16},
                                 {12, 37, -43},
                                 {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);

        cholesky = new CholeskyDecompositionImpl(matrix);
        double determinant = cholesky.getDeterminant();

        assertEquals(324, determinant, 0.0);
    }

    // Test getSolver method
    @Test
    public void testGetSolver() {
        double[][] matrixData = {{4, 12, -16},
                                 {12, 37, -43},
                                 {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);

        cholesky = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = cholesky.getSolver();

        assertNotNull(solver);
    }

}