import org.junit.Test;
import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {

    @Test
    public void testGetL() {
        // Create a symmetric positive definite matrix
        double[][] data = {{4, 2, 1}, {2, 9, 3}, {1, 3, 16}};
        RealMatrix matrix = new RealMatrixImpl(data);

        // Perform Cholesky decomposition
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = decomposition.getL();

        // Define expected L matrix
        double[][] expectedData = {{2, 0, 0}, {1, 2, 0}, {0.5, 1, 3}};
        RealMatrix expectedL = new RealMatrixImpl(expectedData);

        // Compare the computed L matrix with the expected L matrix
        assertTrue(l.equals(expectedL));
    }

    @Test
    public void testGetLT() {
        // Create a symmetric positive definite matrix
        double[][] data = {{4, 2, 1}, {2, 9, 3}, {1, 3, 16}};
        RealMatrix matrix = new RealMatrixImpl(data);

        // Perform Cholesky decomposition
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix lt = decomposition.getLT();

        // Define expected LT matrix
        double[][] expectedData = {{2, 1, 0.5}, {0, 2, 1}, {0, 0, 3}};
        RealMatrix expectedLT = new RealMatrixImpl(expectedData);

        // Compare the computed LT matrix with the expected LT matrix
        assertTrue(lt.equals(expectedLT));
    }

    @Test
    public void testGetDeterminant() {
        // Create a symmetric positive definite matrix
        double[][] data = {{4, 2, 1}, {2, 9, 3}, {1, 3, 16}};
        RealMatrix matrix = new RealMatrixImpl(data);

        // Perform Cholesky decomposition
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        double determinant = decomposition.getDeterminant();

        // Define expected determinant
        double expectedDeterminant = 36;

        // Compare the computed determinant with the expected determinant
        assertEquals(expectedDeterminant, determinant, 0.0001);
    }

    @Test
    public void testGetSolver() {
        // Create a symmetric positive definite matrix
        double[][] data = {{4, 2, 1}, {2, 9, 3}, {1, 3, 16}};
        RealMatrix matrix = new RealMatrixImpl(data);

        // Perform Cholesky decomposition
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);

        // Get the solver
        DecompositionSolver solver = decomposition.getSolver();

        // Verify that the solver is not null
        assertNotNull(solver);
    }

}