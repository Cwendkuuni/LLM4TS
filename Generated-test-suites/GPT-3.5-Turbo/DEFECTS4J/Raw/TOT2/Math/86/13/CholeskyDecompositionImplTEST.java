import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.linear.*;

public class CholeskyDecompositionImplTest {

    private RealMatrix matrix;
    private CholeskyDecompositionImpl decomposition;

    @Before
    public void setUp() {
        matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
        decomposition = new CholeskyDecompositionImpl(matrix);
    }

    @Test
    public void testGetL() {
        RealMatrix L = decomposition.getL();
        assertEquals(L.getEntry(0, 0), 2.0, 1e-15);
        assertEquals(L.getEntry(1, 0), 6.0, 1e-15);
        assertEquals(L.getEntry(1, 1), 1.0, 1e-15);
        assertEquals(L.getEntry(2, 0), -8.0, 1e-15);
        assertEquals(L.getEntry(2, 1), -5.0, 1e-15);
        assertEquals(L.getEntry(2, 2), 3.0, 1e-15);
    }

    @Test
    public void testGetDeterminant() {
        assertEquals(decomposition.getDeterminant(), 36.0, 1e-15);
    }

    @Test
    public void testSolver() {
        DecompositionSolver solver = decomposition.getSolver();
        double[] b = {1, 2, 3};
        double[] x = solver.solve(b);
        assertEquals(x[0], 1.0, 1e-15);
        assertEquals(x[1], 2.0, 1e-15);
        assertEquals(x[2], 3.0, 1e-15);
    }

    // Add more test methods for edge cases and exception scenarios

}