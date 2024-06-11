import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

public class WeightTest {

    @Test
    public void testWeightArrayConstructor() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}});
        RealMatrix actual = weight.getWeight();

        assertEquals(expected, actual);
    }

    @Test
    public void testWeightRealMatrixConstructor() {
        double[][] data = {{1.0, 0.0}, {0.0, 2.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        
        Weight weight = new Weight(matrix);
        
        RealMatrix actual = weight.getWeight();

        assertEquals(matrix, actual);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        double[][] data = {{1.0, 0.0}, {1.0, 2.0}, {1.0, 2.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        
        Weight weight = new Weight(matrix);
    }
}