import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

public class WeightTest {

    @Test
    public void testConstructor1() {
        double[] weightValues = {1.0, 2.0, 3.0, 4.0};
        Weight weight = new Weight(weightValues);
        RealMatrix expectedWeightMatrix = new Array2DRowRealMatrix(4, 4);
        expectedWeightMatrix.setEntry(0, 0, 1.0);
        expectedWeightMatrix.setEntry(1, 1, 2.0);
        expectedWeightMatrix.setEntry(2, 2, 3.0);
        expectedWeightMatrix.setEntry(3, 3, 4.0);
        RealMatrix actualWeightMatrix = weight.getWeight();
        assertTrue(actualWeightMatrix.equals(expectedWeightMatrix));
    }

    @Test
    public void testConstructor2() {
        double[][] weightData = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weightData);
        Weight weight = new Weight(weightMatrix);
        RealMatrix actualWeightMatrix = weight.getWeight();
        assertTrue(actualWeightMatrix.equals(weightMatrix));
    }

    @Test(expected=NonSquareMatrixException.class)
    public void testConstructor2WithNonSquareMatrix() {
        double[][] weightData = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weightData);
        Weight weight = new Weight(weightMatrix);
    }
}