import org.junit.Test;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

public class WeightTest {

    // Test case for Weight(double[] weight)
    @Test
    public void testWeightDoubleArray() {
        Weight weight = new Weight(new double[]{1.0, 2.0, 3.0});
        
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}});
        RealMatrix actual = weight.getWeight();
        
        assertEquals(expected, actual);
    }
    
    // Test case for Weight(RealMatrix weight)
    @Test
    public void testWeightRealMatrix() {
        RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}});
        Weight weight = new Weight(weightMatrix);
        
        RealMatrix expected = weightMatrix.copy();
        RealMatrix actual = weight.getWeight();
        
        assertEquals(expected, actual);
    }
}