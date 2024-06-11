import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {

    private CholeskyDecompositionImpl cholesky;

    @Before
    public void setUp() {
        // Create a symmetric positive-definite matrix for testing
        double[][] matrixData = {{4, 1, 1}, {1, 5, 3}, {1, 3, 9}};
        RealMatrix matrix = new RealMatrixImpl(matrixData);
        
        try {
            cholesky = new CholeskyDecompositionImpl(matrix);
        } catch (Exception e) {
            fail("Failed to create Cholesky decomposition: " + e.getMessage());
        }
    }

    @Test
    public void testGetL() {
        try {
            RealMatrix lMatrix = cholesky.getL();
            double[][] lData = lMatrix.getData();
            assertEquals(lData.length, 3);
            assertEquals(lData[0][0], 2.0, 0.000001);
            assertEquals(lData[1][0], 0.5, 0.000001);
            assertEquals(lData[1][1], 2.179449, 0.000001);
            assertEquals(lData[2][0], 0.5, 0.000001);
            assertEquals(lData[2][1], 1.369306, 0.000001);
            assertEquals(lData[2][2], 2.693498, 0.000001);
        } catch (Exception e) {
            fail("Failed to get L matrix: " + e.getMessage());
        }
    }

    @Test
    public void testGetLT() {
        try {
            RealMatrix ltMatrix = cholesky.getLT();
            double[][] ltData = ltMatrix.getData();
            assertEquals(ltData.length, 3);
            assertEquals(ltData[0][0], 2.0, 0.000001);
            assertEquals(ltData[0][1], 0.5, 0.000001);
            assertEquals(ltData[0][2], 0.5, 0.000001);
            assertEquals(ltData[1][1], 2.179449, 0.000001);
            assertEquals(ltData[1][2], 1.369306, 0.000001);
            assertEquals(ltData[2][2], 2.693498, 0.000001);
        } catch (Exception e) {
            fail("Failed to get LT matrix: " + e.getMessage());
        }
    }

    @Test
    public void testGetDeterminant() {
        try {
            double determinant = cholesky.getDeterminant();
            assertEquals(determinant, 58.0, 0.000001);
        } catch (Exception e) {
            fail("Failed to get determinant: " + e.getMessage());
        }
    }

    @Test
    public void testGetSolver() {
        try {
            DecompositionSolver solver = cholesky.getSolver();
            assertNotNull(solver);
        } catch (Exception e) {
            fail("Failed to get solver: " + e.getMessage());
        }
    }

    @Test
    public void testSolve() {
        try {
            double[] b = {8.3, 14.6, 30.8};
            double[] expectedSolution = {3.73205080756888, 4.03726779962486, 5.01229503298076};
            
            DecompositionSolver solver = cholesky.getSolver();
            double[] solution = solver.solve(b);
            
            assertArrayEquals(expectedSolution, solution, 0.000001);
        } catch (Exception e) {
            fail("Failed to solve linear equation: " + e.getMessage());
        }
    }
}