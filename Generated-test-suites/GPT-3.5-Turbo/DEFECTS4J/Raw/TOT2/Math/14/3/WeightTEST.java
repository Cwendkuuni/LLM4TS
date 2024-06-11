import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.linear.RealMatrix;

public class WeightTest {

    @Test
    public void testWeightConstructorWithArray() {
        double[] weightArray = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weightArray);
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}});
        assertTrue(weight.getWeight().equals(expectedMatrix));
    }

    @Test
    public void testWeightConstructorWithMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 2.0}});
        Weight weight = new Weight(matrix);
        assertTrue(weight.getWeight().equals(matrix));
    }

    @Test
    public void testNonSquareMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}});
        try {
            Weight weight = new Weight(matrix);
            fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            assertTrue(true);
        }
    }
}