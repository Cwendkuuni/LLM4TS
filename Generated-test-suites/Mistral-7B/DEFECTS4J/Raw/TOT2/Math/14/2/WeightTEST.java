import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {

    @Test
    public void testWeightDiagonal() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expectedWeight = new Array2DRowRealMatrix(3, 3);
        expectedWeight.setEntry(0, 0, 1.0);
        expectedWeight.setEntry(1, 1, 2.0);
        expectedWeight.setEntry(2, 2, 3.0);
        RealMatrix actualWeight = weightObj.getWeight();
        assertEquals(expectedWeight, actualWeight);
    }

    @Test
    public void testWeightSquareMatrix() {
        double[][] weight = {{1.0, 0.0}, {0.0, 2.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weight);
        Weight weightObj = new Weight(weightMatrix);
        RealMatrix expectedWeight = weightMatrix.copy();
        RealMatrix actualWeight = weightObj.getWeight();
        assertEquals(expectedWeight, actualWeight);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWeightNullInputDiagonal() {
        double[] weight = null;
        Weight weightObj = new Weight(weight);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWeightNullInputSquareMatrix() {
        RealMatrix weightMatrix = null;
        Weight weightObj = new Weight(weightMatrix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWeightEmptyInputSquareMatrix() {
        RealMatrix weightMatrix = new Array2DRowRealMatrix(0, 0);
        Weight weightObj = new Weight(weightMatrix);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightNonSquareMatrix() {
        double[][] weight = {{1.0, 0.0}, {0.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weight);
        Weight weightObj = new Weight(weightMatrix);
    }

    @Test
    public void testWeightNullOutput() {
        Weight weightObj = new Weight(new double[]{1.0, 2.0, 3.0});
        RealMatrix actualWeight = weightObj.getWeight();
        assertNull(actualWeight);
    }
}