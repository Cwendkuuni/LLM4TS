import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

public class WeightTest {

    @Test
    public void testConstructorArray() {
        double[] weightValues = {2.0, 3.0, 4.0};
        Weight weight = new Weight(weightValues);
        RealMatrix weightMatrix = weight.getWeight();
        assertEquals(2.0, weightMatrix.getEntry(0, 0), 0.0);
        assertEquals(3.0, weightMatrix.getEntry(1, 1), 0.0);
        assertEquals(4.0, weightMatrix.getEntry(2, 2), 0.0);
    }

    @Test
    public void testConstructorMatrix() {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        Weight weight = new Weight(inputMatrix);
        RealMatrix weightMatrix = weight.getWeight();
        assertEquals(1.0, weightMatrix.getEntry(0, 0), 0.0);
        assertEquals(1.0, weightMatrix.getEntry(1, 1), 0.0);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}});
        new Weight(inputMatrix);
    }
}