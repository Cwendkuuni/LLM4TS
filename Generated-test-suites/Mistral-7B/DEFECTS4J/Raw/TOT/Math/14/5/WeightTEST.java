import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {

    @Test
    public void testDiagonalWeightCreation() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expectedWeight = new Array2DRowRealMatrix(3, 3);
        expectedWeight.setEntry(0, 0, weights[0]);
        expectedWeight.setEntry(1, 1, weights[1]);
        expectedWeight.setEntry(2, 2, weights[2]);
        RealMatrix actualWeight = weight.getWeight();
        assertEquals("Weights are not equal", expectedWeight, actualWeight);
    }

    @Test
    public void testNonSquareWeightCreation() {
        double[] weights = {1.0, 2.0, 3.0};
        RealMatrix nonSquareMatrix = new Array2DRowRealMatrix(2, 3);
        nonSquareMatrix.setEntry(0, 0, weights[0]);
        nonSquareMatrix.setEntry(0, 1, weights[1]);
        nonSquareMatrix.setEntry(1, 2, weights[2]);
        try {
            new Weight(nonSquareMatrix);
            fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            // Expected exception
        }
    }
}