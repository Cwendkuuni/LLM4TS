import org.apache.commons.math3.optimization.*;
import org.apache.commons.math3.linear.*;
import org.junit.*;
import static org.junit.Assert.*;

public class WeightTest {

    @Test
    public void testConstructor_doubleArray() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        
        double[][] expected = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}};
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(expected);
        
        assertTrue(w.getWeight().equals(expectedMatrix));
    }
    
    @Test
    public void testConstructor_RealMatrix() {
        double[][] weight = {{1.0, 0.0}, {0.0, 2.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weight);
        
        Weight w = new Weight(weightMatrix);
        
        assertTrue(w.getWeight().equals(weightMatrix));
    }
    
    @Test(expected = NonSquareMatrixException.class)
    public void testConstructor_RealMatrix_NonSquareMatrixException() {
        double[][] weight = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weight);
        
        Weight w = new Weight(weightMatrix);
    }
}