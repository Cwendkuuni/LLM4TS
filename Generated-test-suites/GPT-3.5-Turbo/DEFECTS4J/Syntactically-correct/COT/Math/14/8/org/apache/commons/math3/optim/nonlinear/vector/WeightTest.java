import org.apache.commons.math3.optimization.Weight;
import org.junit.Assert;
import org.junit.Test;

public class WeightTest {
    
    @Test
    public void testWeightConstructor() {
        double[] weight = {1.0, 2.0, 3.0};
        
        Weight w = new Weight(weight);
        
        Assert.assertEquals(3, w.getWeight().getRowDimension());
        Assert.assertEquals(3, w.getWeight().getColumnDimension());
        Assert.assertEquals(1.0, w.getWeight().getEntry(0, 0), 0.001);
        Assert.assertEquals(2.0, w.getWeight().getEntry(1, 1), 0.001);
        Assert.assertEquals(3.0, w.getWeight().getEntry(2, 2), 0.001);
    }
    
    @Test(expected = NonSquareMatrixException.class)
    public void testWeightConstructorNonSquareMatrixException() {
        double[][] weight = {{1.0, 2.0}, {3.0, 4.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weight);
        
        Weight w = new Weight(weightMatrix);
    }
    
    @Test
    public void testWeightConstructorSquareMatrix() {
        double[][] weight = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weight);
        
        Weight w = new Weight(weightMatrix);
        
        Assert.assertEquals(3, w.getWeight().getRowDimension());
        Assert.assertEquals(3, w.getWeight().getColumnDimension());
        Assert.assertEquals(1.0, w.getWeight().getEntry(0, 0), 0.001);
        Assert.assertEquals(2.0, w.getWeight().getEntry(0, 1), 0.001);
        Assert.assertEquals(3.0, w.getWeight().getEntry(0, 2), 0.001);
        Assert.assertEquals(4.0, w.getWeight().getEntry(1, 0), 0.001);
        Assert.assertEquals(5.0, w.getWeight().getEntry(1, 1), 0.001);
        Assert.assertEquals(6.0, w.getWeight().getEntry(1, 2), 0.001);
        Assert.assertEquals(7.0, w.getWeight().getEntry(2, 0), 0.001);
        Assert.assertEquals(8.0, w.getWeight().getEntry(2, 1), 0.001);
        Assert.assertEquals(9.0, w.getWeight().getEntry(2, 2), 0.001);
    }

}