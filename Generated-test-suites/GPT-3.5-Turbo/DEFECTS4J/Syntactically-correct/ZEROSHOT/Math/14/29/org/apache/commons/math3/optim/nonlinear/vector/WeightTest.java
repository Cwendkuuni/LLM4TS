import org.junit.Test;
import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import static org.junit.Assert.assertEquals;

public class WeightTest {

    @Test
    public void testDiagonalWeightMatrix() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expectedWeightMatrix = new Array2DRowRealMatrix(new double[][] {{1.0, 0.0, 0.0},
                                                                                    {0.0, 2.0, 0.0},
                                                                                    {0.0, 0.0, 3.0}});
        assertEquals("Incorrect weight matrix", expectedWeightMatrix, weightObj.getWeight());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareWeightMatrix() {
        double[][] weightData = {{1.0, 2.0, 3.0},
                                 {4.0, 5.0, 6.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weightData);
        new Weight(weightMatrix);
    }
}