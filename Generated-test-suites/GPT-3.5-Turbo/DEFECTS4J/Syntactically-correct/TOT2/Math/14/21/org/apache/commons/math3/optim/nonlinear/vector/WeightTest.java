import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WeightTest {

    @Test
    public void testWeightDiagonalConstructor() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                           {0.0, 2.0, 0.0},
                                                                           {0.0, 0.0, 3.0}});
        assertEquals(expectedMatrix, weight.getWeight());
    }

    @Test
    public void testWeightMatrixConstructor() {
        double[][] data = {{1.0, 0.0, 0.0},
                           {0.0, 2.0, 0.0},
                           {0.0, 0.0, 3.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        Weight weight = new Weight(matrix);
        assertEquals(matrix, weight.getWeight());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        double[][] data = {{1.0, 0.0},
                           {0.0, 2.0},
                           {0.0, 0.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        Weight weight = new Weight(matrix);
    }
}