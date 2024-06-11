import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

public class WeightTest {
    
    @Test
    public void testConstructor1() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}});
        RealMatrix actual = w.getWeight();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testConstructor2() {
        double[][] matrixData = {{1,2,3}, {4,5,6}, {7,8,9}};
        RealMatrix weight = new Array2DRowRealMatrix(matrixData);
        Weight w = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(matrixData);
        RealMatrix actual = w.getWeight();
        assertEquals(expected, actual);
    }
    
    @Test(expected = NonSquareMatrixException.class)
    public void testConstructor2_ThrowsNonSquareMatrixException() {
        double[][] matrixData = {{1,2,3}, {4,5,6}};
        RealMatrix weight = new Array2DRowRealMatrix(matrixData);
        Weight w = new Weight(weight);
    }
    
}