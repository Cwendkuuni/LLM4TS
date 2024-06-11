import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optimization.*;
import org.apache.commons.math3.linear.*;
import org.junit.rules.ExpectedException;

public class WeightTest {

    @Test
    public void testDiagonalWeightMatrix() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                      {0.0, 2.0, 0.0},
                                                                      {0.0, 0.0, 3.0}});
        assertArrayEquals(expected.getData(), weight.getWeight().getData());
    }

    @Test
    public void testNonSquareMatrixException() {
        RealMatrix weight = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0},
                                                                    {3.0, 4.0},
                                                                    {5.0, 6.0}});
        ExpectedException exception = ExpectedException.none();
        Weight w = new Weight(weight);
        exception.expect(NonSquareMatrixException.class);
        RealMatrix invalidWeight = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0},
                                                                          {0.0, 1.0},
                                                                          {0.0, 0.0}});
        w = new Weight(invalidWeight);
    }
}