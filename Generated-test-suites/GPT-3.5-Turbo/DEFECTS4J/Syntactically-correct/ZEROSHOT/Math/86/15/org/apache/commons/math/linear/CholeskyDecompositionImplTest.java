import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;
import org.apache.commons.math.linear.NonSquareMatrixException;
import org.apache.commons.math.linear.NotSymmetricMatrixException;
import org.apache.commons.math.linear.NotPositiveDefiniteMatrixException;

public class CholeskyDecompositionImplTest {
    private CholeskyDecompositionImpl decomposition;
    private RealMatrix matrix;

    @Before
    public void setUp() {
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        matrix = new RealMatrixImpl(data);
        decomposition = new CholeskyDecompositionImpl(matrix);
    }

    @Test
    public void testGetL() {
        double[][] expectedData = {{2, 0, 0}, {6, 1, 0}, {-8, 5, 3}};
        RealMatrix expectedL = new RealMatrixImpl(expectedData);
        RealMatrix l = decomposition.getL();
        assertTrue(expectedL.equals(l));
    }

    @Test
    public void testGetLT() {
        double[][] expectedData = {{2, 6, -8}, {0, 1, 5}, {0, 0, 3}};
        RealMatrix expectedLT = new RealMatrixImpl(expectedData);
        RealMatrix lt = decomposition.getLT();
        assertTrue(expectedLT.equals(lt));
    }

    @Test
    public void testGetDeterminant() {
        double expectedDeterminant = 1088;
        double determinant = decomposition.getDeterminant();
        assertEquals(expectedDeterminant, determinant, 1e-10);
    }

    @Test
    public void testGetSolver() {
        DecompositionSolver solver = decomposition.getSolver();
        assertNotNull(solver);
    }
}