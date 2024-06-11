
import org.apache.commons.math3.optimization.*;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {
    
    // Test constructor with double array
    @Test
    public void testConstructorWithDoubleArray() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        
        double[][] expectedMatrixData = {
            {1.0, 0.0, 0.0},
            {0.0, 2.0, 0.0},
            {0.0, 0.0, 3.0}
        };
        Array2DRowRealMatrix expectedMatrix = new Array2DRowRealMatrix(expectedMatrixData);
        
        assertTrue(weight.getWeight().equals(expectedMatrix));
    }
    
    // Test constructor with RealMatrix
    @Test
    public void testConstructorWithRealMatrix() {
        double[][] matrixData = {
            {1.0, 2.0},
            {3.0, 4.0}
        };
        Array2DRowRealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        
        Weight weight = new Weight(matrix);
        
        assertTrue(weight.getWeight().equals(matrix));
    }
    
    // Test constructor with NonSquareMatrixException
    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrixException() {
        double[][] matrixData = {
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0}
        };
        Array2DRowRealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        
        Weight weight = new Weight(matrix);
    }
}