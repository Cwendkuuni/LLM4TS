
import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {

    @Test
    public void testConstructorWithArray() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(1, 1, 2.0);
        expected.setEntry(2, 2, 3.0);
        assertTrue(expected.equals(w.getWeight()));
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithMatrixInvalid() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        RealMatrix weight = new Array2DRowRealMatrix(data);
        Weight w = new Weight(weight);
    }

    @Test
    public void testConstructorWithMatrixValid() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        RealMatrix weight = new Array2DRowRealMatrix(data);
        Weight w = new Weight(weight);
        assertTrue(weight.equals(w.getWeight()));
    }
}
