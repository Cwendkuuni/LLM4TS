import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {

    @Test
    public void testWeightArray() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(1, 1, 2.0);
        expected.setEntry(2, 2, 3.0);
        assertArrayEquals(expected.getData(), w.getWeight().getData());
    }

    @Test
    public void testWeightMatrix() {
        double[][] data = {{1.0, 0.0}, {0.0, 1.0}};
        RealMatrix weight = new Array2DRowRealMatrix(data);
        Weight w = new Weight(weight);
        assertArrayEquals(data, w.getWeight().getData());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightMatrixNonSquare() {
        double[][] data = {{1.0, 0.0}, {0.0, 1.0}, {0.0, 0.0}};
        RealMatrix weight = new Array2DRowRealMatrix(data);
        Weight w = new Weight(weight);
    }
}