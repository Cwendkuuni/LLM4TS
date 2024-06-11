import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

public class WeightTest {

    @Test
    public void testConstructorWithArray() {
        // Typical use case
        double[] weight = {1, 2, 3};
        Weight w = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1, 0, 0}, {0, 2, 0}, {0, 0, 3}});
        Assert.assertEquals(expected, w.getWeight());

        // Edge case
        double[] zeroWeight = new double[0];
        w = new Weight(zeroWeight);
        expected = new Array2DRowRealMatrix(new double[][]{});
        Assert.assertEquals(expected, w.getWeight());

        // Error scenario
        double[] nonSquareWeight = {1, 2};
        try {
            w = new Weight(nonSquareWeight);
            Assert.fail("Expected NonSquareMatrixException to be thrown");
        } catch (NonSquareMatrixException e) {
            // Test passes if NonSquareMatrixException is thrown
        }
    }

    @Test
    public void testConstructorWithMatrix() {
        // Typical use case
        RealMatrix weight = new Array2DRowRealMatrix(new double[][]{{1, 0, 0}, {0, 2, 0}, {0, 0, 3}});
        Weight w = new Weight(weight);
        Assert.assertEquals(weight, w.getWeight());

        // Edge case
        weight = new Array2DRowRealMatrix(new double[][]{});
        w = new Weight(weight);
        Assert.assertEquals(weight, w.getWeight());

        // Error scenario
        weight = new Array2DRowRealMatrix(new double[][]{{1, 2}});
        try {
            w = new Weight(weight);
            Assert.fail("Expected NonSquareMatrixException to be thrown");
        } catch (NonSquareMatrixException e) {
            // Test passes if NonSquareMatrixException is thrown
        }
    }
}