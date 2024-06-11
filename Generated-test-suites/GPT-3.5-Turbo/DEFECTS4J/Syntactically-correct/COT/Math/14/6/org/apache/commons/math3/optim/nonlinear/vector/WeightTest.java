import org.junit.Test;
import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

public class WeightTest {

    // Test case for Weight(double[] weight)
    @Test
    public void testWeight1() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        RealMatrix result = w.getWeight();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                      {0.0, 2.0, 0.0},
                                                                      {0.0, 0.0, 3.0}});
        assertArrayEquals(expected.getData(), result.getData());
    }

    // Test case for Weight(RealMatrix weight)
    @Test
    public void testWeight2() {
        RealMatrix weight = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0},
                                                                    {0.0, 2.0}});
        Weight w = new Weight(weight);
        RealMatrix result = w.getWeight();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0},
                                                                      {0.0, 2.0}});
        assertArrayEquals(expected.getData(), result.getData());
    }

    // Test case for Weight(RealMatrix weight) with non square matrix
    @Test(expected = NonSquareMatrixException.class)
    public void testWeight3() {
        RealMatrix weight = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0},
                                                                    {0.0, 2.0},
                                                                    {0.0, 0.0}});
        Weight w = new Weight(weight);
    }

}