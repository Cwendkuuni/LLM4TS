import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

public class WeightTest {

    @Test
    public void testWeightConstructorWithArray() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix result = weight.getWeight();
        assertEquals(1.0, result.getEntry(0, 0), 0.0);
        assertEquals(2.0, result.getEntry(1, 1), 0.0);
        assertEquals(3.0, result.getEntry(2, 2), 0.0);
    }

    @Test
    public void testWeightConstructorWithArrayEdgeCases() {
        double[] weights = {0.0};
        Weight weight = new Weight(weights);
        RealMatrix result = weight.getWeight();
        assertEquals(0.0, result.getEntry(0, 0), 0.0);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightConstructorWithArrayException() {
        double[] weights = {1.0, 2.0, 3.0};
        RealMatrix matrix = new Array2DRowRealMatrix(2, 3);
        new Weight(matrix);
    }

    @Test
    public void testWeightConstructorWithMatrix() {
        double[][] data = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        Weight weight = new Weight(matrix);
        RealMatrix result = weight.getWeight();
        assertEquals(1.0, result.getEntry(0, 0), 0.0);
        assertEquals(2.0, result.getEntry(1, 1), 0.0);
        assertEquals(3.0, result.getEntry(2, 2), 0.0);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightConstructorWithMatrixException() {
        double[][] data = {{1.0, 0.0}, {0.0, 2.0}, {0.0, 0.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        new Weight(matrix);
    }
}