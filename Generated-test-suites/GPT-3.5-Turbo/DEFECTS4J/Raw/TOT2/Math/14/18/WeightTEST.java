import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public class WeightTest {

    @Test
    public void testConstructorWithArray() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix result = weight.getWeight();
        assertEquals(1.0, result.getEntry(0, 0), 0);
        assertEquals(2.0, result.getEntry(1, 1), 0);
        assertEquals(3.0, result.getEntry(2, 2), 0);
    }

    @Test
    public void testConstructorWithArrayEdgeCase() {
        double[] weights = {0.0};
        Weight weight = new Weight(weights);
        RealMatrix result = weight.getWeight();
        assertEquals(0.0, result.getEntry(0, 0), 0);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithMatrixNonSquare() {
        double[][] values = {{1.0, 2.0}, {3.0, 4.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(values);
        Weight weight = new Weight(matrix);
    }

    @Test
    public void testConstructorWithMatrix() {
        double[][] values = {{1.0, 0.0}, {0.0, 1.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(values);
        Weight weight = new Weight(matrix);
        RealMatrix result = weight.getWeight();
        assertEquals(1.0, result.getEntry(0, 0), 0);
        assertEquals(1.0, result.getEntry(1, 1), 0);
    }

    @Test
    public void testGetWeight() {
        double[] weights = {2.0, 4.0, 6.0};
        Weight weight = new Weight(weights);
        RealMatrix result = weight.getWeight();
        assertEquals(2.0, result.getEntry(0, 0), 0);
        assertEquals(4.0, result.getEntry(1, 1), 0);
        assertEquals(6.0, result.getEntry(2, 2), 0);
    }

}