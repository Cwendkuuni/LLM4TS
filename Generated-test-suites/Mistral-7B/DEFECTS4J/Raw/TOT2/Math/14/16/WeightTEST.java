import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public class WeightTest {
    @Test
    public void testWeightConstructionWithArray() {
        double[] weights = {};
        Weight weightObject = new Weight(weights);
        RealMatrix expected = new Array2DRowRealMatrix(0, 0);
        RealMatrix actual = weightObject.getWeight();
        assertEquals(expected, actual);
    }

    @Test
    public void testWeightConstructionWithNonSquareRealMatrix() {
        double[][] weightMatrixData = {{1.0, 0.0}, {0.0, 2.0}, {0.0, 0.0}, {3.0, 1.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weightMatrixData);
        Weight weightObject = new Weight(weightMatrix);
        assertThrows(NonSquareMatrixException.class, () -> weightObject.getWeight());
    }

    @Test
    public void testWeightConstructionWithNegativeWeights() {
        double[] weights = {-1.0, 2.0, -3.0};
        Weight weightObject = new Weight(weights);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3, -1.0, 0.0, 0.0, 0.0, 0.0, -3.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 2.0);
        RealMatrix actual = weightObject.getWeight();
        assertEquals(expected, actual);
    }

    @Test
    public void testWeightConstructionWithArray() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weightObject = new Weight(weights);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 3.0);
        RealMatrix actual = weightObject.getWeight();
        assertEquals(expected, actual);
    }

    @Test
    public void testWeightConstructionWithRealMatrix() {
        double[][] weightMatrixData = {{1.0, 0.0}, {0.0, 2.0}, {0.0, 0.0}, {3.0, 0.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weightMatrixData);
        Weight weightObject = new Weight(weightMatrix);
        RealMatrix expected = new Array2DRowRealMatrix(weightMatrixData);
        RealMatrix actual = weightObject.getWeight();
        assertEquals(expected, actual);
    }
}