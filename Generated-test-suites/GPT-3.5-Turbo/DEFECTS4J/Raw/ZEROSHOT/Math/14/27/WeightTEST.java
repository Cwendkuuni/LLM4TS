import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {

    @Test
    public void testConstructor1() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(1, 1, 2.0);
        expected.setEntry(2, 2, 3.0);
        assertEquals(expected, w.getWeight());
    }

    @Test
    public void testConstructor2() {
        double[][] data = {
                {1.0, 0.0, 0.0},
                {0.0, 2.0, 0.0},
                {0.0, 0.0, 3.0}
        };
        RealMatrix weight = new Array2DRowRealMatrix(data);
        Weight w = new Weight(weight);
        assertEquals(weight, w.getWeight());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructor2NonSquareMatrix() {
        double[][] data = {
                {1.0, 2.0},
                {3.0, 4.0},
                {5.0, 6.0}
        };
        RealMatrix weight = new Array2DRowRealMatrix(data);
        new Weight(weight);
    }
}