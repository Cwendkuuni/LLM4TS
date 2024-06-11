import org.junit.Test;
import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {

    @Test
    public void testCholeskyDecompositionImplConstructor() {
        // Test creating CholeskyDecompositionImpl with valid matrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 2}, {2, 5}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        assertNotNull(cholesky);
        
        // Test creating CholeskyDecompositionImpl with invalid matrix
        matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 1}, {1, 1}});
        try {
            cholesky = new CholeskyDecompositionImpl(matrix);
            fail("Expected NotPositiveDefiniteMatrixException");
        } catch (NotPositiveDefiniteMatrixException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetL() {
        // Test getting L matrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 2}, {2, 5}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = cholesky.getL();
        assertNotNull(l);
        
        // Test getting L matrix after calling getLT()
        RealMatrix lT = cholesky.getLT();
        RealMatrix l2 = cholesky.getL();
        assertEquals(lT, l2.transpose());
    }

    @Test
    public void testGetLT() {
        // Test getting LT matrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 2}, {2, 5}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix lT = cholesky.getLT();
        assertNotNull(lT);
    }

    @Test
    public void testGetDeterminant() {
        // Test getting determinant
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 2}, {2, 5}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        double determinant = cholesky.getDeterminant();
        assertEquals(16.0, determinant, 0.0);
    }

    @Test
    public void testGetSolver() {
        // Test getting solver
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 2}, {2, 5}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = cholesky.getSolver();
        assertNotNull(solver);
    }

    @Test
    public void testSolverSolve() {
        // Test solving linear equation
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 2}, {2, 5}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = cholesky.getSolver();
        double[] b = {1, 2};
        double[] x = solver.solve(b);
        assertArrayEquals(new double[]{0.6, 0.2}, x, 0.0);
    }

}