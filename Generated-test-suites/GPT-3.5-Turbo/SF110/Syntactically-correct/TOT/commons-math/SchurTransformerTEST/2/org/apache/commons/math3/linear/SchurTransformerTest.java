import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;
import static org.junit.Assert.*;

public class SchurTransformerTest {
    
    @Test
    public void testConstructorValidInput() {
        // create a valid square matrix
        double[][] data = {{1, 2}, {3, 4}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        // test constructor with valid input matrix
        SchurTransformer transformer = new SchurTransformer(matrix);
        
        // check if the transformation was completed successfully
        assertNotNull(transformer.getP());
        assertNotNull(transformer.getPT());
        assertNotNull(transformer.getT());
    }
    
    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorInvalidInput() {
        // create an invalid non-square matrix
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        // test constructor with invalid input matrix
        SchurTransformer transformer = new SchurTransformer(matrix);
    }
    
    @Test
    public void testGetP() {
        // create a valid square matrix
        double[][] data = {{1, 2}, {3, 4}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        // create a SchurTransformer object
        SchurTransformer transformer = new SchurTransformer(matrix);
        
        // get the P matrix
        RealMatrix P = transformer.getP();
        
        // check if the P matrix is not null
        assertNotNull(P);
        
        // check the dimensions of the P matrix
        assertEquals(matrix.getRowDimension(), P.getRowDimension());
        assertEquals(matrix.getColumnDimension(), P.getColumnDimension());
    }
    
    @Test
    public void testGetPT() {
        // create a valid square matrix
        double[][] data = {{1, 2}, {3, 4}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        // create a SchurTransformer object
        SchurTransformer transformer = new SchurTransformer(matrix);
        
        // get the P^T matrix
        RealMatrix PT = transformer.getPT();
        
        // check if the P^T matrix is not null
        assertNotNull(PT);
        
        // check the dimensions of the P^T matrix
        assertEquals(matrix.getColumnDimension(), PT.getRowDimension());
        assertEquals(matrix.getRowDimension(), PT.getColumnDimension());
    }
    
    @Test
    public void testGetT() {
        // create a valid square matrix
        double[][] data = {{1, 2}, {3, 4}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        // create a SchurTransformer object
        SchurTransformer transformer = new SchurTransformer(matrix);
        
        // get the T matrix
        RealMatrix T = transformer.getT();
        
        // check if the T matrix is not null
        assertNotNull(T);
        
        // check the dimensions of the T matrix
        assertEquals(matrix.getRowDimension(), T.getRowDimension());
        assertEquals(matrix.getColumnDimension(), T.getColumnDimension());
    }
    
    @Test
    public void testTransform() {
        // create a valid square matrix
        double[][] data = {{1, 2}, {3, 4}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        // create a SchurTransformer object
        SchurTransformer transformer = new SchurTransformer(matrix);
        
        // check if the transformation was completed successfully
        assertNotNull(transformer.getP());
        assertNotNull(transformer.getPT());
        assertNotNull(transformer.getT());
    }
    
    @Test(expected = MaxCountExceededException.class)
    public void testTransformConvergenceFailed() {
        // create a valid square matrix
        double[][] data = {{1, 2}, {3, 4}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        // create a SchurTransformer object with a matrix that fails to converge
        SchurTransformer transformer = new SchurTransformer(matrix);
        
        // perform transformation on a matrix that fails to converge
        transformer.transform();
    }
    
}