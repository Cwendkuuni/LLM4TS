import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class WeightTest {

    @Test
    public void testWeight_CreatesDiagonalWeightMatrix() {
        double[] weight = {2.0, 3.0, 4.0};
        Weight w = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{2, 0, 0}, {0, 3, 0}, {0, 0, 4}});
        assertEquals(expected, w.getWeight());
    }

    @Test
    public void testWeight_ThrowsNonSquareMatrixException() {
        RealMatrix weight = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}});
        try {
            Weight w = new Weight(weight);
            fail("Expected NonSquareMatrixException to be thrown.");
        } catch (NonSquareMatrixException e) {
            assertEquals(2, e.getColumnDimension());
            assertEquals(3, e.getRowDimension());
        }
    }

    @Test
    public void testGetWeight_ReturnsCopyOfWeightMatrix() {
        double[] weight = {2.0, 3.0, 4.0};
        Weight w = new Weight(weight);
        RealMatrix original = w.getWeight();
        original.setEntry(0, 0, 1000);
        RealMatrix updated = w.getWeight();
        assertEquals(2, updated.getEntry(0, 0), 0);
    }
}