import org.junit.Test;
import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

import static org.junit.Assert.*;

public class WeightTest {

    // Test cases for Weight(double[] weight)

    @Test
    public void testWeightConstructorWithValidInput() {
        double[] weight = {1.0, 2.0, 3.0};

        Weight w = new Weight(weight);

        RealMatrix expectedMatrix = new Array2DRowRealMatrix(3, 3);
        expectedMatrix.setEntry(0, 0, 1.0);
        expectedMatrix.setEntry(1, 1, 2.0);
        expectedMatrix.setEntry(2, 2, 3.0);

        assertTrue(expectedMatrix.equals(w.getWeight()));
    }

    @Test
    public void testWeightConstructorWithEmptyInput() {
        double[] weight = {};

        Weight w = new Weight(weight);

        RealMatrix expectedMatrix = new Array2DRowRealMatrix(0, 0);

        assertTrue(expectedMatrix.equals(w.getWeight()));
    }

    @Test(expected = NullPointerException.class)
    public void testWeightConstructorWithNullInput() {
        double[] weight = null;

        Weight w = new Weight(weight);
    }

    // Test cases for Weight(RealMatrix weight)

    @Test
    public void testWeightConstructorWithValidSquareMatrix() {
        RealMatrix weight = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});

        Weight w = new Weight(weight);

        assertTrue(weight.equals(w.getWeight()));
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightConstructorWithNonSquareMatrix() {
        RealMatrix weight = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}});

        Weight w = new Weight(weight);
    }

    // General method test case

    @Test
    public void testGetWeight() {
        double[] weight = {1.0, 2.0, 3.0};

        Weight w = new Weight(weight);

        RealMatrix expectedMatrix = new Array2DRowRealMatrix(3, 3);
        expectedMatrix.setEntry(0, 0, 1.0);
        expectedMatrix.setEntry(1, 1, 2.0);
        expectedMatrix.setEntry(2, 2, 3.0);

        assertTrue(expectedMatrix.equals(w.getWeight()));
    }

}