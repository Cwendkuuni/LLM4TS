import static org.junit.Assert.*;
import org.junit.Test;

public class WeightTest {

    @Test
    public void testWeightWithDoubleArray() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObject = new Weight(weight);
        RealMatrix expectedWeight = new Array2DRowRealMatrix(3, 3, new double[][]{{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}});
        RealMatrix actualWeight = weightObject.getWeight();
        assertArrayEquals(expectedWeight.getData(), actualWeight.getData(), 0.0);
    }
}