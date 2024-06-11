import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.linear.*;

public class WeightTest {

    @Test
    public void testConstructorArray() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                      {0.0, 2.0, 0.0},
                                                                      {0.0, 0.0, 3.0}});
        assertTrue(expected.equals(weight.getWeight()));
    }

    @Test
    public void testConstructorMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0},
                                                                    {0.0, 2.0}});
        Weight weight = new Weight(matrix);
        assertTrue(matrix.equals(weight.getWeight()));
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorMatrixNonSquare() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0},
                                                                    {3.0, 4.0},
                                                                    {5.0, 6.0}});
        Weight weight = new Weight(matrix);
    }

    @Test
    public void testGetWeight() {
        double[] weights = {2.0, 3.0, 4.0};
        Weight weight = new Weight(weights);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{2.0, 0.0, 0.0},
                                                                      {0.0, 3.0, 0.0},
                                                                      {0.0, 0.0, 4.0}});
        assertTrue(expected.equals(weight.getWeight()));
    }

}