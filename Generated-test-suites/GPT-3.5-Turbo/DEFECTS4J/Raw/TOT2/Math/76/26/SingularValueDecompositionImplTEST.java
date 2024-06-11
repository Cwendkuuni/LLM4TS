import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SingularValueDecompositionImplTest {

    private SingularValueDecompositionImpl svdImpl;

    @Before
    public void setUp() {
        double[][] matrixData = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        svdImpl = new SingularValueDecompositionImpl(matrix);
    }

    @Test
    public void testGetU() {
        // Test typical case
        RealMatrix uMatrix = svdImpl.getU();
        assertNotNull(uMatrix);
        assertEquals(3, uMatrix.getRowDimension());
        assertEquals(3, uMatrix.getColumnDimension());

        // Test edge case with empty matrix
        double[][] emptyData = {};
        RealMatrix emptyMatrix = MatrixUtils.createRealMatrix(emptyData);
        svdImpl = new SingularValueDecompositionImpl(emptyMatrix);
        uMatrix = svdImpl.getU();
        assertNotNull(uMatrix);
        assertEquals(0, uMatrix.getRowDimension());
        assertEquals(0, uMatrix.getColumnDimension());
    }

    @Test
    public void testGetUT() {
        RealMatrix utMatrix = svdImpl.getUT();
        assertNotNull(utMatrix);
        assertEquals(3, utMatrix.getRowDimension());
        assertEquals(3, utMatrix.getColumnDimension());
    }

    @Test
    public void testGetS() {
        RealMatrix sMatrix = svdImpl.getS();
        assertNotNull(sMatrix);
        assertEquals(3, sMatrix.getRowDimension());
        assertEquals(3, sMatrix.getColumnDimension());
    }

    // Add more test cases for other methods

}