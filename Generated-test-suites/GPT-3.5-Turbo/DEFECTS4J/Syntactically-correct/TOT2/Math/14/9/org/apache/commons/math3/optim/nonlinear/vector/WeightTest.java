import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {

    @Test
    public void testWeightConstructorDoubleArray() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix result = weight.getWeight();
        assertEquals(1.0, result.getEntry(0, 0), 0.0);
        assertEquals(2.0, result.getEntry(1, 1), 0.0);
        assertEquals(3.0, result.getEntry(2, 2), 0.0);
    }

    @Test
    public void testWeightConstructorRealMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        Weight weight = new Weight(matrix);
        RealMatrix result = weight.getWeight();
        assertEquals(1.0, result.getEntry(0, 0), 0.0);
        assertEquals(1.0, result.getEntry(1, 1), 0.0);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightConstructorNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}});
        Weight weight = new Weight(matrix);
    }
}