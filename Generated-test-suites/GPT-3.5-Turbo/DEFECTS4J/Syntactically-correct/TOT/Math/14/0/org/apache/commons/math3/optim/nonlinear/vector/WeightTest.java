import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Assert;
import org.junit.Test;

public class WeightTest {

    @Test
    public void testConstructorWithDoubleArray() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        
        Assert.assertArrayEquals(weight, w.getWeight().getData()[0], 0.0);
        Assert.assertArrayEquals(weight, w.getWeight().getData()[1], 0.0);
        Assert.assertArrayEquals(weight, w.getWeight().getData()[2], 0.0);
    }

    @Test
    public void testConstructorWithRealMatrix() {
        double[][] data = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}};
        Array2DRowRealMatrix matrix = new Array2DRowRealMatrix(data);
        
        Weight w = new Weight(matrix);
        
        Assert.assertArrayEquals(data[0], w.getWeight().getData()[0], 0.0);
        Assert.assertArrayEquals(data[1], w.getWeight().getData()[1], 0.0);
        Assert.assertArrayEquals(data[2], w.getWeight().getData()[2], 0.0);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithRealMatrixNonSquare() {
        double[][] data = {{1.0, 0.0}, {0.0, 2.0}, {0.0, 0.0}};
        Array2DRowRealMatrix matrix = new Array2DRowRealMatrix(data);
        
        new Weight(matrix);
    }
}