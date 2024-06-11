
import org.junit.Assert;
import org.junit.Test;

public class WeightTest {

    // Test case for Weight(double[] weight)
    @Test
    public void testWeightConstructorWithArray() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(3, 3);
        expectedMatrix.setEntry(0, 0, 1.0);
        expectedMatrix.setEntry(1, 1, 2.0);
        expectedMatrix.setEntry(2, 2, 3.0);
        Assert.assertEquals(expectedMatrix, w.getWeight());
    }

    // Test case for Weight(RealMatrix weight)
    @Test
    public void testWeightConstructorWithMatrix() {
        double[][] matrixData = {{1.0, 0.0}, {0.0, 1.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(matrixData);
        Weight w = new Weight(weightMatrix);
        Assert.assertEquals(weightMatrix, w.getWeight());
    }
}
