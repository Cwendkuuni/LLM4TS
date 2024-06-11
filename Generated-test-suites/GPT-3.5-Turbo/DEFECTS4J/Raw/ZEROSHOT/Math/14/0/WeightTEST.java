import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

public class WeightTest {

    @Test
    public void testWeightWithArrayConstructor() {
        double[] weight = {1, 2, 3, 4};
        Weight w = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(4, 4);
        for (int i = 0; i < weight.length; i++) {
            expected.setEntry(i, i, weight[i]);
        }

        assertEquals(expected, w.getWeight());
    }

    @Test
    public void testWeightWithMatrixConstructor() {
        double[][] weightData = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        RealMatrix weight = new Array2DRowRealMatrix(weightData);
        Weight w = new Weight(weight);

        assertEquals(weight, w.getWeight());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightWithMatrixConstructorThrowsException() {
        double[][] weightData = {{1, 0}, {0, 1}, {0, 0}};
        RealMatrix weight = new Array2DRowRealMatrix(weightData);
        Weight w = new Weight(weight);
    }
  
}