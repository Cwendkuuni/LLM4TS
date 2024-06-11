import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {

    @Test
    public void testConstructorWithArray() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix result = weight.getWeight();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}});
        assertArrayEquals(expected.getData(), result.getData());
    }

    @Test
    public void testConstructorWithMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 2.0}});
        Weight weight = new Weight(matrix);
        RealMatrix result = weight.getWeight();
        assertArrayEquals(matrix.getData(), result.getData());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}});
        Weight weight = new Weight(matrix);
    }
}