import static org.junit.Assert.assertArrayEquals;

import org.apache.commons.math3.optimization.*;
import org.junit.Test;

public class WeightTest {
    @Test
    public void testWeightConstructor1() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        assertArrayEquals(weight, w.getWeight().getData()[0], 0.0);
        assertArrayEquals(weight, w.getWeight().getData()[1], 0.0);
        assertArrayEquals(weight, w.getWeight().getData()[2], 0.0);
    }
    
    @Test
    public void testWeightConstructor2() {
        double[][] weightData = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weightData);
        Weight w = new Weight(weightMatrix);
        assertArrayEquals(weightData[0], w.getWeight().getData()[0], 0.0);
        assertArrayEquals(weightData[1], w.getWeight().getData()[1], 0.0);
        assertArrayEquals(weightData[2], w.getWeight().getData()[2], 0.0);
    }
    
    @Test(expected = NonSquareMatrixException.class)
    public void testWeightConstructor2NonSquare() {
        double[][] weightData = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weightData);
        new Weight(weightMatrix);
    }
}