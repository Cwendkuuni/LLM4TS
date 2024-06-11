
import org.apache.commons.math3.optimization.*;
import org.apache.commons.math3.linear.*;
import org.junit.*;

public class WeightTest {

    @Test
    public void testConstructorWithArray() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(1, 1, 2.0);
        expected.setEntry(2, 2, 3.0);
        Assert.assertTrue(w.getWeight().equals(expected));
    }

    @Test
    public void testConstructorWithMatrix() {
        double[][] data = {{1.0, 0.0}, {0.0, 2.0}};
        RealMatrix weight = new Array2DRowRealMatrix(data);
        Weight w = new Weight(weight);
        Assert.assertTrue(w.getWeight().equals(weight));
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        double[][] data = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}};
        RealMatrix weight = new Array2DRowRealMatrix(data);
        Weight w = new Weight(weight);
    }
}