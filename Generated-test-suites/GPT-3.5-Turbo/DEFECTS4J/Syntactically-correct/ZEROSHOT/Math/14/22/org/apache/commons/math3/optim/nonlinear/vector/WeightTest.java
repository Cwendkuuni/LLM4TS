import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {

    @Test
    public void testConstructor() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);

        RealMatrix expectedMatrix = new Array2DRowRealMatrix(3, 3);
        expectedMatrix.setEntry(0, 0, 1.0);
        expectedMatrix.setEntry(1, 1, 2.0);
        expectedMatrix.setEntry(2, 2, 3.0);

        assertEquals(expectedMatrix, w.getWeight());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixConstructor() {
        double[][] matrixData = { {1,2,3}, {4,5,6}, {7,8,9} };
        RealMatrix weight = new Array2DRowRealMatrix(matrixData);
        Weight w = new Weight(weight);
    }

    @Test
    public void testGetWeight() {
        double[] weight = {2.0, 3.0, 4.0};
        Weight w = new Weight(weight);

        RealMatrix expectedMatrix = new Array2DRowRealMatrix(3, 3);
        expectedMatrix.setEntry(0, 0, 2.0);
        expectedMatrix.setEntry(1, 1, 3.0);
        expectedMatrix.setEntry(2, 2, 4.0);

        assertEquals(expectedMatrix, w.getWeight());
    }
}