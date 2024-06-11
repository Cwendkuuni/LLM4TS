import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.linear.RealMatrix;

public class WeightTest {

    @Test
    public void testWeightConstructorDoubleArray() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix result = weight.getWeight();
        
        assertEquals(1.0, result.getEntry(0, 0), 0.0001);
        assertEquals(2.0, result.getEntry(1, 1), 0.0001);
        assertEquals(3.0, result.getEntry(2, 2), 0.0001);
    }

    @Test
    public void testWeightConstructorRealMatrix() {
        double[][] data = {{1.0, 0.0}, {0.0, 1.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        Weight weight = new Weight(matrix);
        RealMatrix result = weight.getWeight();
        
        assertEquals(1.0, result.getEntry(0, 0), 0.0001);
        assertEquals(1.0, result.getEntry(1, 1), 0.0001);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        double[][] data = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        Weight weight = new Weight(matrix);
    }

}