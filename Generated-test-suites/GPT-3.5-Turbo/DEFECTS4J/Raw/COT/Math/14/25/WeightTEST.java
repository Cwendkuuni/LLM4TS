import org.junit.Test;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

public class WeightTest {

    @Test
    public void testWeight_ConstructorWithDoubleArray() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}});
        RealMatrix actual = w.getWeight();
        assertEquals(expected, actual);
    }

    @Test
    public void testWeight_ConstructorWithRealMatrix() {
        RealMatrix weight = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 2.0}});
        Weight w = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 2.0}});
        RealMatrix actual = w.getWeight();
        assertEquals(expected, actual);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeight_ConstructorWithNonSquareMatrix() {
        RealMatrix weight = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}});
        Weight w = new Weight(weight);
    }
}