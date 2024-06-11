import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

public class WeightTest {

    @Test
    public void testWeightDiagonalConstructor() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                     {0.0, 2.0, 0.0},
                                                                     {0.0, 0.0, 3.0}});
        assertEquals(expected, weight.getWeight());
    }

    @Test
    public void testWeightMatrixConstructor() {
        double[][] data = {{1.0, 2.0},
                           {3.0, 4.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        Weight weight = new Weight(matrix);
        assertEquals(matrix, weight.getWeight());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0},
                                                                    {3.0, 4.0},
                                                                    {5.0, 6.0}});
        Weight weight = new Weight(matrix);
    }
}