import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public class WeightTest {
    
    @Test
    public void testWeightConstructorWithArray() {
        double[] weightValues = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weightValues);
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(new double[][] {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}});
        assertArrayEquals(expectedMatrix.getData(), weight.getWeight().getData());
    }
    
    @Test
    public void testWeightConstructorWithSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {{1.0, 0.0}, {0.0, 2.0}});
        Weight weight = new Weight(matrix);
        assertArrayEquals(matrix.getData(), weight.getWeight().getData());
    }
    
    @Test(expected = NonSquareMatrixException.class)
    public void testWeightConstructorWithNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}});
        new Weight(matrix);
    }
}