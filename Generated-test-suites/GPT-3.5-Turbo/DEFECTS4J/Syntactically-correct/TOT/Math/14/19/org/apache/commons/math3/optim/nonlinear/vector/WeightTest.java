import org.junit.Test;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import static org.junit.Assert.*;

public class WeightTest {

    @Test
    public void testWeightConstructorWithDoubleArray() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix weightMatrix = weight.getWeight();
        assertEquals(1.0, weightMatrix.getEntry(0, 0), 0.0);
        assertEquals(2.0, weightMatrix.getEntry(1, 1), 0.0);
        assertEquals(3.0, weightMatrix.getEntry(2, 2), 0.0);
    }

    @Test
    public void testWeightConstructorWithRealMatrix() {
        double[][] matrixData = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        Weight weight = new Weight(matrix);
        RealMatrix weightMatrix = weight.getWeight();
        assertEquals(1.0, weightMatrix.getEntry(0, 0), 0.0);
        assertEquals(2.0, weightMatrix.getEntry(1, 1), 0.0);
        assertEquals(3.0, weightMatrix.getEntry(2, 2), 0.0);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightConstructorWithInvalidMatrix() {
        double[][] matrixData = {{1.0, 0.0}, {0.0, 2.0}, {0.0, 0.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        Weight weight = new Weight(matrix);
    }

}