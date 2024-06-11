import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

public class WeightTest {

    @Test
    public void testWeightConstructorArray() {
        double[] weights = {2.0, 3.0, 4.0};
        Weight weight = new Weight(weights);
        RealMatrix result = weight.getWeight();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
            {2.0, 0.0, 0.0},
            {0.0, 3.0, 0.0},
            {0.0, 0.0, 4.0}
        });

        assertArrayEquals(expected.getData(), result.getData());
    }

    @Test
    public void testWeightConstructorMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.0},
            {0.0, 2.0}
        });
        Weight weight = new Weight(matrix);
        RealMatrix result = weight.getWeight();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.0},
            {0.0, 2.0}
        });

        assertArrayEquals(expected.getData(), result.getData());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 0.0},
            {0.0, 2.0},
            {0.0, 3.0}
        });
        Weight weight = new Weight(matrix);
    }
}