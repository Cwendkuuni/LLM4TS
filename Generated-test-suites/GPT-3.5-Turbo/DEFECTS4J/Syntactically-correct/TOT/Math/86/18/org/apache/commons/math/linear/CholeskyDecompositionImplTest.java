import org.apache.commons.math.linear.CholeskyDecompositionImpl;
import org.apache.commons.math.linear.NotPositiveDefiniteMatrixException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {

    private CholeskyDecompositionImpl decomposition;
    
    @Before
    public void setUp() {
        double[][] matrixData = {{4, 2, 1}, {2, 5, 3}, {1, 3, 6}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        decomposition = new CholeskyDecompositionImpl(matrix);
    }
    
    @Test
    public void testGetL() {
        double[][] expectedData = {{2.0, 0.0, 0.0}, {1.0, 2.0, 0.0}, {0.5, 1.0, 1.5}};
        RealMatrix expectedL = MatrixUtils.createRealMatrix(expectedData);
        
        RealMatrix actualL = decomposition.getL();
        
        assertEquals(expectedL, actualL);
    }
    
    @Test
    public void testGetLT() {
        double[][] expectedData = {{2.0, 1.0, 0.5}, {0.0, 2.0, 1.0}, {0.0, 0.0, 1.5}};
        RealMatrix expectedLT = MatrixUtils.createRealMatrix(expectedData);
        
        RealMatrix actualLT = decomposition.getLT();
        
        assertEquals(expectedLT, actualLT);
    }
    
    @Test
    public void testGetDeterminant() {
        double expectedDeterminant = 40.0;
        
        double actualDeterminant = decomposition.getDeterminant();
        
        assertEquals(expectedDeterminant, actualDeterminant, 0.0001);
    }
    
    @Test
    public void testGetSolver() {
        DecompositionSolver solver = decomposition.getSolver();
        
        assertNotNull(solver);
    }
    
    @Test
    public void testSolve() {
        double[] b = {1.0, 2.0, 3.0};
        double[] expectedX = {-1.25, 1.0, 1.5};
        
        double[] actualX = decomposition.getSolver().solve(b);
        
        assertArrayEquals(expectedX, actualX, 0.0001);
    }
    
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrix() {
        double[][] matrixData = {{1, -1}, {-1, 1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        
        new CholeskyDecompositionImpl(matrix);
    }
    
    @Test
    public void testSolveWithInvalidMatrixException() {
        double[] b = {1.0, 2.0};
        double[] expectedX = {1.0, 2.0};
        
        try {
            double[] actualX = decomposition.getSolver().solve(b);
            assertEquals(expectedX, actualX);
        } catch (InvalidMatrixException e) {
            fail("Unexpected InvalidMatrixException");
        }
    }
}