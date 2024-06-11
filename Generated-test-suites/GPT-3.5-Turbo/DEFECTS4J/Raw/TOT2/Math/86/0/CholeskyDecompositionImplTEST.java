import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class CholeskyDecompositionImplTest {

    @Test
    public void testCholeskyDecompositionImplDefaultConstructor() {
        // Test default constructor
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4, 2}, {2, 9}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);

        assertEquals(cholesky.getL(), new Array2DRowRealMatrix(new double[][]{{2.0, 0.0}, {1.0, 3.0}}));
    }

    @Test
    public void testGetDeterminant() {
        // Test getDeterminant
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{4, 2}, {2, 9}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);

        assertEquals(cholesky.getDeterminant(), 18.0, 0.0001);
    }

    @Test
    public void testSolverSingularMatrix() {
        // Test solver with singular matrix
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {1, 2}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);

        assertFalse(cholesky.getSolver().isNonSingular());
    }

}