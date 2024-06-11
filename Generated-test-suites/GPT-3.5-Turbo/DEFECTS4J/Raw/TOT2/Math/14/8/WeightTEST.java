import static org.junit.Assert.*;
import org.junit.Test;

public class WeightTest {

    @Test
    public void testWeightDiagonalConstructor() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1, 0, 0}, {0, 2, 0}, {0, 0, 3}});
        assertTrue(expected.equals(weight.getWeight()));
    }

    @Test
    public void testWeightMatrixConstructor() {
        double[][] data = {{1, 0, 0}, {0, 2, 0}, {0, 0, 3}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        Weight weight = new Weight(matrix);
        assertTrue(matrix.equals(weight.getWeight()));
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixConstructor() {
        double[][] data = {{1, 0}, {0, 2}, {0, 0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        Weight weight = new Weight(matrix);
    }
}