import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.linear.*;

public class CholeskyDecompositionImplTest {

    @Test
    public void testGetL() {
        // Test normal case
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = decomposition.getL();
        RealMatrix expectedL = new Array2DRowRealMatrix(new double[][]{{2, 0, 0}, {6, 1, 0}, {-8, 5, 3}});
        assertEquals(expectedL, l);
    }

    @Test
    public void testGetLT() {
        // Test normal case
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix lt = decomposition.getLT();
        RealMatrix expectedLT = new Array2DRowRealMatrix(new double[][]{{2, 6, -8}, {0, 1, 5}, {0, 0, 3}});
        assertEquals(expectedLT, lt);
    }

    @Test
    public void testGetDeterminant() {
        // Test normal case
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        double determinant = decomposition.getDeterminant();
        double expectedDeterminant = 576;
        assertEquals(expectedDeterminant, determinant, 1e-15);
    }

    @Test
    public void testGetSolver() {
        // Test normal case
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = decomposition.getSolver();
        assertNotNull(solver);
    }

}