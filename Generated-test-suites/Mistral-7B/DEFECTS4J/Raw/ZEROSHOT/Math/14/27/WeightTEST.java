import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Assert;
import org.junit.Test;

public class WeightTest {

    @Test
    public void testDiagonalWeightConstruction() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);

        RealMatrix expectedWeight = new Array2DRowRealMatrix(3, 3);
        expectedWeight.setEntry(0, 0, 1.0);
        expectedWeight.setEntry(1, 1, 2.0);
        expectedWeight.setEntry(2, 2, 3.0);

        RealMatrix actualWeight = weight.getWeight();
        Assert.assertArrayEquals(expectedWeight.getData(), actualWeight.getData(), 0.0);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareWeightConstruction() {
        double[] weights = {1.0, 2.0};
        new Weight(weights);
    }

    @Test
    public void testSquareWeightConstruction() {
        double[][] weights = {{1.0, 0.0}, {0.0, 2.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weights);
        Weight weight = new Weight(weightMatrix);

        RealMatrix expectedWeight = weightMatrix.copy();

        RealMatrix actualWeight = weight.getWeight();
        Assert.assertArrayEquals(expectedWeight.getData(), actualWeight.getData(), 0.0);
    }

    @Test
    public void testGetWeight() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);

        RealMatrix expectedWeight = new Array2DRowRealMatrix(3, 3);
        expectedWeight.setEntry(0, 0, 1.0);
        expectedWeight.setEntry(1, 1, 2.0);
        expectedWeight.setEntry(2, 2, 3.0);

        RealMatrix actualWeight = weight.getWeight();
        Assert.assertArrayEquals(expectedWeight.getData(), actualWeight.getData(), 0.0);
    }
}