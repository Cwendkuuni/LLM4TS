import static org.junit.Assert.*;
import org.junit.Test;

public class WeightTest {

    @Test
    public void testWeightArrayConstructor() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                             {0.0, 2.0, 0.0},
                                                                             {0.0, 0.0, 3.0}});
        assertEquals(expectedMatrix, weight.getWeight());
    }

    @Test
    public void testWeightRealMatrixConstructor() {
        double[][] data = {{1.0, 0.0, 0.0},
                           {0.0, 2.0, 0.0},
                           {0.0, 0.0, 3.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        Weight weight = new Weight(matrix);
        assertEquals(matrix, weight.getWeight());
    }

    @Test
    public void testWeightNonSquareMatrixException() {
        double[][] data = {{1.0, 0.0},
                           {0.0, 2.0},
                           {0.0, 0.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        try {
            Weight weight = new Weight(matrix);
            fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            // Expected exception
        }
    }
}