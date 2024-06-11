import org.junit.Test;
import static org.junit.Assert.*;

public class SchurTransformerTest {

    @Test
    public void testGetP() {
        double[][] matrix = {{1, 2}, {3, 4}};
        RealMatrix realMatrix = MatrixUtils.createRealMatrix(matrix);
        SchurTransformer schurTransformer = new SchurTransformer(realMatrix);
        
        RealMatrix expected = MatrixUtils.createRealMatrix(matrix);
        RealMatrix result = schurTransformer.getP();
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testGetPT() {
        double[][] matrix = {{1, 2}, {3, 4}};
        RealMatrix realMatrix = MatrixUtils.createRealMatrix(matrix);
        SchurTransformer schurTransformer = new SchurTransformer(realMatrix);
        
        RealMatrix expected = realMatrix.transpose();
        RealMatrix result = schurTransformer.getPT();
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testGetT() {
        double[][] matrix = {{1, 2}, {3, 4}};
        RealMatrix realMatrix = MatrixUtils.createRealMatrix(matrix);
        SchurTransformer schurTransformer = new SchurTransformer(realMatrix);
        
        RealMatrix expected = MatrixUtils.createRealMatrix(matrix);
        RealMatrix result = schurTransformer.getT();
        
        assertEquals(expected, result);
    }

    // Additional test cases for potential edge cases and exception handling scenarios

    @Test
    public void testNonSquareMatrixException() {
        double[][] matrix = {{1, 2, 3}, {4, 5, 6}};
        RealMatrix realMatrix = MatrixUtils.createRealMatrix(matrix);
        
        try {
            SchurTransformer schurTransformer = new SchurTransformer(realMatrix);
            fail("Expected NonSquareMatrixException to be thrown");
        } catch (NonSquareMatrixException e) {
            // Exception expected
        }
    }

    @Test
    public void testConvergenceFailedException() {
        double[][] matrix = {{1, 2}, {3, 4}};
        RealMatrix realMatrix = MatrixUtils.createRealMatrix(matrix);
        SchurTransformer schurTransformer = new SchurTransformer(realMatrix);
        
        // Modify private variable MAX_ITERATIONS to force convergence failure
        try {
            Field field = SchurTransformer.class.getDeclaredField("MAX_ITERATIONS");
            field.setAccessible(true);
            field.setInt(schurTransformer, 10);

            schurTransformer.transform();
            fail("Expected MaxCountExceededException to be thrown");
        } catch (MaxCountExceededException e) {
            // Exception expected
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}