import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Test;

public class WeightTest {
  
    @Test
    public void testConstructor() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        Array2DRowRealMatrix weightMatrix = new Array2DRowRealMatrix(3, 3);
        weightMatrix.setEntry(0, 0, 1.0);
        weightMatrix.setEntry(1, 1, 2.0);
        weightMatrix.setEntry(2, 2, 3.0);
        assertEquals(weightMatrix, w.getWeight());  
    }
  
    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorNonSquareMatrix() {
        Array2DRowRealMatrix weightMatrix = new Array2DRowRealMatrix(2, 3);
        Weight w = new Weight(weightMatrix);
    }
  
    @Test
    public void testGetWeight() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        Array2DRowRealMatrix weightMatrix = new Array2DRowRealMatrix(3, 3);
        weightMatrix.setEntry(0, 0, 1.0);
        weightMatrix.setEntry(1, 1, 2.0);
        weightMatrix.setEntry(2, 2, 3.0);
        assertEquals(weightMatrix, w.getWeight());
    }
}