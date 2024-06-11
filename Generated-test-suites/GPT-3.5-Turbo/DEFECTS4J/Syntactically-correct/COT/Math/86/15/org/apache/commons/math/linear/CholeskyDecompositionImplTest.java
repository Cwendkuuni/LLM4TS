import org.apache.commons.math.linear.*;
import org.junit.*;
import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {

    // Helper method to compare two matrices
    private void assertMatrixEquals(double[][] expected, RealMatrix actual) {
        assertArrayEquals(expected.length, actual.getRowDimension());
        assertArrayEquals(expected[0].length, actual.getColumnDimension());
        for (int i = 0; i < expected.length; i++) {
            for (int j = 0; j < expected[i].length; j++) {
                assertEquals(expected[i][j], actual.getEntry(i, j), 0.001);
            }
        }
    }
    
    
    // Test getL() method
    @Test
    public void testGetL() {
        double[][] input = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        double[][] expected = {{2, 0, 0}, {6, 1, 0}, {-8, 5, 3}};
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(input);
        CholeskyDecompositionImpl cd = new CholeskyDecompositionImpl(inputMatrix);
        assertMatrixEquals(expected, cd.getL());  
    }
    
    // Test getLT() method
    @Test
    public void testGetLT() {
        double[][] input = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        double[][] expected = {{2, 6, -8}, {0, 1, 5}, {0, 0, 3}};
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(input);
        CholeskyDecompositionImpl cd = new CholeskyDecompositionImpl(inputMatrix);
        assertMatrixEquals(expected, cd.getLT());  
    }
    
    // Test getDeterminant() method
    @Test
    public void testGetDeterminant() {
        double[][] input = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(input);
        CholeskyDecompositionImpl cd = new CholeskyDecompositionImpl(inputMatrix);
        assertEquals(36, cd.getDeterminant(), 0.001);
    }
    
    // Test getSolver() method
    @Test
    public void testGetSolver() {
        double[][] input = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        double[] b = {1, 2, 3};
        double[] expected = {0.25, -0.5, 1.0};
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(input);
        CholeskyDecompositionImpl cd = new CholeskyDecompositionImpl(inputMatrix);
        DecompositionSolver solver = cd.getSolver();
        double[] result = solver.solve(b);
        assertArrayEquals(expected, result, 0.001);
    }
    
}