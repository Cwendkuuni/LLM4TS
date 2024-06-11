import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

public class WeightTest {

    @Test
    public void testConstructorWithArray() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}});
        assertSame(expectedMatrix, weight.getWeight());
    }

    @Test
    public void testConstructorWithInvalidArray() {
        double[] weights = {1.0, 2.0, 3.0, 4.0};
        try {
            Weight weight = new Weight(weights);
            fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testConstructorWithMatrix() {
        RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}});
        Weight weight = new Weight(weightMatrix);
        assertSame(weightMatrix, weight.getWeight());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithInvalidMatrix() {
        RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 2.0}, {0.0, 0.0}});
        Weight weight = new Weight(weightMatrix);
    }

}