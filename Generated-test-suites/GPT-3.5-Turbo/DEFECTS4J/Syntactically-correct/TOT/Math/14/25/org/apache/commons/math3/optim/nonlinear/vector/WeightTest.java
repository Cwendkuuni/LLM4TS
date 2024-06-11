import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

public class WeightTest {

    @Test
    public void testDiagonalWeightMatrix() {
        double[] weight = {1.0, 2.0, 3.0, 4.0};
        Weight w = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0, 0.0},
                                                                     {0.0, 2.0, 0.0, 0.0},
                                                                     {0.0, 0.0, 3.0, 0.0},
                                                                     {0.0, 0.0, 0.0, 4.0}});
        RealMatrix result = w.getWeight();
        assertArrayEquals(expected.getData(), result.getData());
    }

    @Test
    public void testSquareWeightMatrix() {
        double[][] data = {{1.0, 2.0, 3.0},
                           {4.0, 5.0, 6.0},
                           {7.0, 8.0, 9.0}};
        RealMatrix weight = new Array2DRowRealMatrix(data);
        Weight w = new Weight(weight);
        RealMatrix result = w.getWeight();
        assertArrayEquals(weight.getData(), result.getData());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareWeightMatrix() {
        double[][] data = {{1.0, 2.0, 3.0},
                           {4.0, 5.0, 6.0}};
        RealMatrix weight = new Array2DRowRealMatrix(data);
        Weight w = new Weight(weight);
    }

    @Test
    public void testImmutableWeightMatrix() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        RealMatrix weightMatrix = w.getWeight();
        weightMatrix.setEntry(0, 0, 5.0);
        RealMatrix result = w.getWeight();
        assertNotEquals(5.0, result.getEntry(0, 0), 0.0);
    }
}