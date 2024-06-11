import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

public class WeightTest {

    @Test
    public void testConstructor_Diagonal() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(3, 3);
        expectedMatrix.setEntry(0, 0, 1.0);
        expectedMatrix.setEntry(1, 1, 2.0);
        expectedMatrix.setEntry(2, 2, 3.0);
        RealMatrix actualMatrix = w.getWeight();
        assertArrayEquals(expectedMatrix.getData(), actualMatrix.getData());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructor_NonSquare() {
        RealMatrix weight = new Array2DRowRealMatrix(3, 2);
        Weight w = new Weight(weight);
    }

    @Test
    public void testGetWeight() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(3, 3);
        expectedMatrix.setEntry(0, 0, 1.0);
        expectedMatrix.setEntry(1, 1, 2.0);
        expectedMatrix.setEntry(2, 2, 3.0);
        RealMatrix actualMatrix = w.getWeight();
        assertArrayEquals(expectedMatrix.getData(), actualMatrix.getData());
    }
}