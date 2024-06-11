import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

public class WeightTest {

    @Test
    public void testWeightConstructorWithArray() {
        double[] weightsArray = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weightsArray);
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}});
        assertEquals(expectedMatrix, weight.getWeight());
    }

    @Test
    public void testWeightConstructorWithMatrix() {
        RealMatrix weightsMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 2.0}});
        Weight weight = new Weight(weightsMatrix);
        assertEquals(weightsMatrix, weight.getWeight());
    }

    @Test
    public void testWeightConstructorWithNonSquareMatrixException() {
        RealMatrix weightsMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}});
        try {
            Weight weight = new Weight(weightsMatrix);
            fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            // Expected exception
        }
    }
}