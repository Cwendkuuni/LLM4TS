import static org.junit.Assert.*;
import org.junit.Test;

public class WeightTest {

    @Test
    public void testConstructorArray() {
        double[] weightValues = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weightValues);
        RealMatrix result = weight.getWeight();
        double[][] expectedValues = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}};
        RealMatrix expected = new Array2DRowRealMatrix(expectedValues);
        assertArrayEquals(expected.getData(), result.getData());
    }

    @Test
    public void testConstructorMatrix() {
        double[][] values = {{1.0, 0.0}, {0.0, 2.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(values);
        Weight weight = new Weight(matrix);
        RealMatrix result = weight.getWeight();
        assertArrayEquals(matrix.getData(), result.getData());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixConstructor() {
        double[][] values = {{1.0, 0.0}, {0.0, 2.0}, {1.0, 2.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(values);
        Weight weight = new Weight(matrix);
    }
}