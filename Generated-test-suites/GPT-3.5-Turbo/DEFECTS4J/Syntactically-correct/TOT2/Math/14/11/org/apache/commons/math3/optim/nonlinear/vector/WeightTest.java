import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

public class WeightTest {

    @Test
    public void testWeightConstructorWithArray() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1, 0, 0}, {0, 2, 0}, {0, 0, 3}});
        assertTrue(weight.getWeight().equals(expected));
    }

    @Test
    public void testWeightConstructorWithMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        Weight weight = new Weight(matrix);
        assertTrue(weight.getWeight().equals(matrix));
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}});
        Weight weight = new Weight(matrix);
    }
}