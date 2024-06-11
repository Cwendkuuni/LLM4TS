import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.apache.commons.math.linear.RealMatrixImpl;
import org.junit.Before;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

    private CholeskyDecompositionImpl choleskyDecomposition;

    @Before
    public void setUp() {
        double[][] data = {
            {4, 12, -16},
            {12, 37, -43},
            {-16, -43, 98}
        };
        RealMatrixImpl matrix = new RealMatrixImpl(data);
        choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
    }

    @Test
    public void testGetL() {
        RealMatrix l = choleskyDecomposition.getL();
        double[][] expectedData = {
            {2, 0, 0},
            {6, 1, 0},
            {-8, 5, 3}
        };
        RealMatrixImpl expected = new RealMatrixImpl(expectedData);
        assertEquals(expected, l);
    }

    @Test
    public void testGetLT() {
        RealMatrix lt = choleskyDecomposition.getLT();
        double[][] expectedData = {
            {2, 6, -8},
            {0, 1, 5},
            {0, 0, 3}
        };
        RealMatrixImpl expected = new RealMatrixImpl(expectedData);
        assertEquals(expected, lt);
    }

    @Test
    public void testGetDeterminant() {
        double determinant = choleskyDecomposition.getDeterminant();
        assertEquals(729.0, determinant, 0.0001);
    }

    @Test
    public void testGetSolver() {
        DecompositionSolver solver = choleskyDecomposition.getSolver();
        assertNotNull(solver);
    }

}