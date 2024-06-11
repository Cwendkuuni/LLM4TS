import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {

    // Test case for constructor Weight(double[])
    @Test
    public void testConstructor_withDoubleArray() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        
        RealMatrix weightMatrix = weight.getWeight();
        
        assertEquals(1.0, weightMatrix.getEntry(0, 0), 0);
        assertEquals(2.0, weightMatrix.getEntry(1, 1), 0);
        assertEquals(3.0, weightMatrix.getEntry(2, 2), 0);
    }
    
    // Test case for constructor Weight(double[]) with empty array
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testConstructor_withEmptyDoubleArray() {
        double[] weights = {};
        Weight weight = new Weight(weights);
    }
    
    // Test case for constructor Weight(RealMatrix)
    @Test
    public void testConstructor_withRealMatrix() {
        double[][] data = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        RealMatrix inputMatrix = new Array2DRowRealMatrix(data);
        
        Weight weight = new Weight(inputMatrix);
        
        RealMatrix weightMatrix = weight.getWeight();
        
        assertEquals(1.0, weightMatrix.getEntry(0, 0), 0);
        assertEquals(1.0, weightMatrix.getEntry(1, 1), 0);
        assertEquals(1.0, weightMatrix.getEntry(2, 2), 0);
    }
    
    // Test case for constructor Weight(RealMatrix) with non-square matrix
    @Test(expected = NonSquareMatrixException.class)
    public void testConstructor_withNonSquareRealMatrix() {
        double[][] data = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}};
        RealMatrix inputMatrix = new Array2DRowRealMatrix(data);
        
        Weight weight = new Weight(inputMatrix);
    }
    
    // Test case for getWeight() method
    @Test
    public void testGetWeight() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        
        RealMatrix weightMatrix = weight.getWeight();
        
        assertNotNull(weightMatrix);
    }

}