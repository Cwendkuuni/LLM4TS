import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {

    @Test
    public void testWeightConstructorWithPositiveWeights() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        assertNotNull(w);
        assertEquals(3, w.getWeight().getRowDimension());
        assertEquals(3, w.getWeight().getColumnDimension());
        assertEquals(1.0, w.getWeight().getEntry(0, 0), 0.0001);
        assertEquals(2.0, w.getWeight().getEntry(1, 1), 0.0001);
        assertEquals(3.0, w.getWeight().getEntry(2, 2), 0.0001);
    }

    @Test
    public void testWeightConstructorWithZeroWeights() {
        double[] weight = {0.0, 0.0, 0.0};
        Weight w = new Weight(weight);
        assertNotNull(w);
        assertEquals(3, w.getWeight().getRowDimension());
        assertEquals(3, w.getWeight().getColumnDimension());
        assertEquals(0.0, w.getWeight().getEntry(0, 0), 0.0001);
        assertEquals(0.0, w.getWeight().getEntry(1, 1), 0.0001);
        assertEquals(0.0, w.getWeight().getEntry(2, 2), 0.0001);
    }

    @Test
    public void testWeightConstructorWithNegativeWeights() {
        double[] weight = {-1.0, -2.0, -3.0};
        Weight w = new Weight(weight);
        assertNotNull(w);
        assertEquals(3, w.getWeight().getRowDimension());
        assertEquals(3, w.getWeight().getColumnDimension());
        assertEquals(-1.0, w.getWeight().getEntry(0, 0), 0.0001);
        assertEquals(-2.0, w.getWeight().getEntry(1, 1), 0.0001);
        assertEquals(-3.0, w.getWeight().getEntry(2, 2), 0.0001);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightConstructorWithNonSquareMatrix() {
        double[][] weight = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(weight);
        Weight w = new Weight(matrix);
    }

}