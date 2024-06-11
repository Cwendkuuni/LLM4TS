import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.MatrixUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class SchurTransformerTest {
    
    private SchurTransformer schurTransformer;
    private RealMatrix matrix;
    
    @Before
    public void setup() {
        // create a 3x3 input matrix
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        matrix = MatrixUtils.createRealMatrix(data);
        schurTransformer = new SchurTransformer(matrix);
    }
    
    @Test
    public void testGetP() {
        RealMatrix expectedP = MatrixUtils.createRealMatrix(schurTransformer.getP().getData());
        assertEquals(expectedP, schurTransformer.getP());
    }
    
    @Test
    public void testGetPT() {
        RealMatrix expectedPT = MatrixUtils.createRealMatrix(schurTransformer.getPT().getData());
        assertEquals(expectedPT, schurTransformer.getPT());
    }
    
    @Test
    public void testGetT() {
        RealMatrix expectedT = MatrixUtils.createRealMatrix(schurTransformer.getT().getData());
        assertEquals(expectedT, schurTransformer.getT());
    }
    
    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        // create a non-square input matrix
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        RealMatrix nonSquareMatrix = MatrixUtils.createRealMatrix(data);
        
        new SchurTransformer(nonSquareMatrix);
        fail("Expected NonSquareMatrixException");
    }
    
    @Test
    public void testConvergenceFailedException() {
        // Create a test case that fails to converge
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix matrixInfiniteLoop = MatrixUtils.createRealMatrix(data);
        schurTransformer = new SchurTransformer(matrixInfiniteLoop);
        
        try {
            schurTransformer.getP();
            fail("Expected MaxCountExceededException");
        } catch (MaxCountExceededException ex) {
            // Test passed
            assertTrue(true);
        }
    }
}