
import org.junit.Assert;
import org.junit.Test;

public class WeightTest {

    //Test for Weight(double[] weight)

    @Test
    public void testWeightDoubleArray() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        Assert.assertEquals(1.0, w.getWeight().getEntry(0, 0), 0.0);
        Assert.assertEquals(2.0, w.getWeight().getEntry(1, 1), 0.0);
        Assert.assertEquals(3.0, w.getWeight().getEntry(2, 2), 0.0);
    }

    // Test for Weight(RealMatrix weight)

    @Test
    public void testWeightRealMatrix() {
        double[][] weightData = {{1.0, 0.0}, {0.0, 2.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weightData);
        Weight w = new Weight(weightMatrix);
        Assert.assertEquals(1.0, w.getWeight().getEntry(0, 0), 0.0);
        Assert.assertEquals(0.0, w.getWeight().getEntry(0, 1), 0.0);
        Assert.assertEquals(0.0, w.getWeight().getEntry(1, 0), 0.0);
        Assert.assertEquals(2.0, w.getWeight().getEntry(1, 1), 0.0);
    }

    // Edge case test for Weight(RealMatrix weight)
  
    @Test(expected = NonSquareMatrixException.class)
    public void testWeightRealMatrixNonSquare() {
        double[][] weightData = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weightData);
        Weight w = new Weight(weightMatrix);
    }

    // Additional test for getWeight()
    
    @Test
    public void testGetWeight() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        RealMatrix weightMatrix = w.getWeight();
        Assert.assertEquals(1.0, weightMatrix.getEntry(0, 0), 0.0);
        Assert.assertEquals(2.0, weightMatrix.getEntry(1, 1), 0.0);
        Assert.assertEquals(3.0, weightMatrix.getEntry(2, 2), 0.0);
    }
}

