import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Assert;
import org.junit.Test;

public class WeightTest {

    @Test
    public void testWeightDiagonalConstructor() {
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
    public void testWeightNonSquareConstructorException() {
        double[][] weight = {{1.0, 2.0}, {3.0, 4.0}};
        Weight weightObj = new Weight(new Array2DRowRealMatrix(2, 3, weight));
    }

    @Test
    public void testWeightSquareConstructor() {
        double[][] weight = {{1.0, 0.0}, {0.0, 1.0}};
        Weight weightObj = new Weight(new Array2DRowRealMatrix(2, 2, weight));
        RealMatrix expectedWeight = new Array2DRowRealMatrix(2, 2, weight);

        RealMatrix actualWeight = weightObj.getWeight();
        Assert.assertArrayEquals(expectedWeight.getData(), actualWeight.getData(), 0.0);
    }

    @Test
    public void testWeightGetDiagonalElement() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);

        RealVector diagonal = weightObj.getWeight().getColumnVector(0);
        Assert.assertEquals(weight[0], diagonal.getEntry(0), 0.0);
        Assert.assertEquals(weight[1], diagonal.getEntry(1), 0.0);
        Assert.assertEquals(weight[2], diagonal.getEntry(2), 0.0);
    }

    @Test
    public void testWeightGetNonExistentElement() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);

        try {
            weightObj.getWeight().getEntry(3, 3);
            Assert.fail();
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }
}