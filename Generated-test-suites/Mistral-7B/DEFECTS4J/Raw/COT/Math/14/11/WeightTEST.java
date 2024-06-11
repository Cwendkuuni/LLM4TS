import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Test;

public class WeightTest {

    @Test
    public void testWeightConstructorDiagonal() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expectedWeight = new Array2DRowRealMatrix(3, 3);
        expectedWeight.setEntry(0, 0, 1.0);
        expectedWeight.setEntry(1, 1, 2.0);
        expectedWeight.setEntry(2, 2, 3.0);
        RealMatrix actualWeight = weightObj.getWeight();
        Assert.assertArrayEquals(expectedWeight.getData(), actualWeight.getData(), 0.0);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightConstructorNonSquareMatrix() {
        double[][] weight = {{1.0, 2.0}, {3.0, 4.0}};
        Weight weightObj = new Weight(new Array2DRowRealMatrix(weight));
    }

    @Test
    public void testWeightConstructorSquareMatrix() {
        double[][] weight = {{1.0, 0.0}, {0.0, 1.0}};
        Weight weightObj = new Weight(new Array2DRowRealMatrix(weight));
        RealMatrix expectedWeight = new Array2DRowRealMatrix(2, 2).transpose().multiply(weight);
        RealMatrix actualWeight = weightObj.getWeight();
        Assert.assertEquals(expectedWeight, actualWeight);
    }
}