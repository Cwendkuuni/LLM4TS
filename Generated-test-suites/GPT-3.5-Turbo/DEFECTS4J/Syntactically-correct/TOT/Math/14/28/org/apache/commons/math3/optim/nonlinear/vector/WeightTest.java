import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {

    private double[] weightArray;
    private RealMatrix weightMatrix;

    @Before
    public void setUp() {
        weightArray = new double[]{1, 2, 3};
        weightMatrix = new Array2DRowRealMatrix(new double[][]{{1, 0, 0}, {0, 2, 0}, {0, 0, 3}});
    }

    @Test
    public void testWeightArrayConstructor() {
        Weight weight = new Weight(weightArray);
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(new double[][]{{1, 0, 0}, {0, 2, 0}, {0, 0, 3}});
        assertTrue(weight.getWeight().equals(expectedMatrix));
    }

    @Test
    public void testWeightArrayConstructorInvalidInput() {
        double[] invalidWeightArray = new double[]{};
        try {
            Weight weight = new Weight(invalidWeightArray);
            fail("Expected NonSquareMatrixException to be thrown");
        } catch (NonSquareMatrixException e) {
            // Expected exception
        }
    }

    @Test
    public void testWeightMatrixConstructor() {
        Weight weight = new Weight(weightMatrix);
        assertTrue(weight.getWeight().equals(weightMatrix));
    }

    @Test
    public void testWeightMatrixConstructorInvalidInput() {
        RealMatrix invalidWeightMatrix = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 2}});
        try {
            Weight weight = new Weight(invalidWeightMatrix);
            fail("Expected NonSquareMatrixException to be thrown");
        } catch (NonSquareMatrixException e) {
            // Expected exception
        }
    }
}