import org.junit.Test;
import org.junit.Assert;

public class WeightTest {
    
    @Test
    public void testConstructorArray() {
        double[] weight = {1.0, 2.0, 3.0, 4.0};
        Weight w = new Weight(weight);
        
        double[][] expectedData = {{1.0, 0.0, 0.0, 0.0},
                                   {0.0, 2.0, 0.0, 0.0},
                                   {0.0, 0.0, 3.0, 0.0},
                                   {0.0, 0.0, 0.0, 4.0}};
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(expectedData);
        
        Assert.assertEquals(expectedMatrix, w.getWeight());
    }
    
    @Test
    public void testConstructorMatrix() {
        double[][] data = {{1.0, 0.0, 0.0},
                           {0.0, 2.0, 0.0},
                           {0.0, 0.0, 3.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(data);
        Weight w = new Weight(weightMatrix);
        
        Assert.assertEquals(weightMatrix, w.getWeight());
    }
    
    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorNonSquareMatrix() {
        double[][] data = {{1.0, 0.0},
                           {0.0, 2.0},
                           {0.0, 0.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(data);
        Weight w = new Weight(weightMatrix);
    }
}