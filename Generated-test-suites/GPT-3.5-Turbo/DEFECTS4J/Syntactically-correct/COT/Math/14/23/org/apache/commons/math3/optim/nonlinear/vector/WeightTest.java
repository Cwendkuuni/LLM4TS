import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {

    @Test
    public void testWeightDoubleArray() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(1, 1, 2.0);
        expected.setEntry(2, 2, 3.0);
        RealMatrix actual = w.getWeight();
        assertArrayEquals(expected.getData(), actual.getData());
    }

    @Test
    public void testWeightRealMatrix() {
        double[][] data = {{0.5, 0.0}, {0.0, 1.5}};
        RealMatrix weight = new Array2DRowRealMatrix(data);
        Weight w = new Weight(weight);
        RealMatrix actual = w.getWeight();
        assertEquals(weight, actual);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightNonSquareMatrixException() {
        double[][] data = {{0.5, 0.0}, {0.0, 1.5}, {1.0, 0.5}};
        RealMatrix weight = new Array2DRowRealMatrix(data);
        new Weight(weight);
    }
}