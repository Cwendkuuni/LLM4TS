import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

public class WeightTest {
  
    @Test
    public void testConstructorWithArray() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix result = weightObj.getWeight();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}});
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testConstructorWithMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 2.0}});
        Weight weightObj = new Weight(matrix);
        RealMatrix result = weightObj.getWeight();
        Assert.assertEquals(matrix, result);
    }
    
    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 2.0}, {1.0, 0.0}});
        Weight weightObj = new Weight(matrix);
    }

}