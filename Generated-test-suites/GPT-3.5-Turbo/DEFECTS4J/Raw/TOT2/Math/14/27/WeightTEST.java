import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

public class WeightTest {

    @Test
    public void testConstructorFromArray() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);

        RealMatrix result = weight.getWeight();
        double[][] expected = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}};

        assertArrayEquals(expected, result.getData());
    }

    @Test
    public void testConstructorFromMatrix() {
        double[][] values = {{1.0, 0.0}, {0.0, 2.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(values);
        Weight weight = new Weight(matrix);

        RealMatrix result = weight.getWeight();
        double[][] expected = {{1.0, 0.0}, {0.0, 2.0}};

        assertArrayEquals(expected, result.getData());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        double[][] values = {{1.0, 0.0}, {0.0, 2.0}, {0.0, 0.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(values);
        Weight weight = new Weight(matrix);
    }

    @Test
    public void testGetWeight() {
        double[] weights = {2.0, 4.0};
        Weight weight = new Weight(weights);

        RealMatrix result = weight.getWeight();
        double[][] expected = {{2.0, 0.0}, {0.0, 4.0}};

        assertArrayEquals(expected, result.getData());
    }
}