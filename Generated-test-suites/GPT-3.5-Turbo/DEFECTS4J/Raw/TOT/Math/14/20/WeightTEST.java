import static org.junit.Assert.*;
import org.junit.Test;

public class WeightTest {

    @Test
    public void testWeightConstructorWithArray() {
        double[] weightArray = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weightArray);

        assertEquals(1.0, weight.getWeight().getEntry(0, 0), 0.0001);
        assertEquals(2.0, weight.getWeight().getEntry(1, 1), 0.0001);
        assertEquals(3.0, weight.getWeight().getEntry(2, 2), 0.0001);
    }

    @Test
    public void testWeightConstructorWithArray_zeroLength() {
        double[] weightArray = {};
        Weight weight = new Weight(weightArray);

        assertEquals(0, weight.getWeight().getColumnDimension());
        assertEquals(0, weight.getWeight().getRowDimension());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightConstructorWithMatrix_nonSquareMatrix() {
        double[][] weights = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weights);
        Weight weight = new Weight(weightMatrix);
    }

    @Test
    public void testWeightConstructorWithMatrix_squareMatrix() {
        double[][] weights = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weights);
        Weight weight = new Weight(weightMatrix);

        RealMatrix resultMatrix = weight.getWeight();

        assertEquals(1.0, resultMatrix.getEntry(0, 0), 0.0001);
        assertEquals(2.0, resultMatrix.getEntry(0, 1), 0.0001);
        assertEquals(3.0, resultMatrix.getEntry(0, 2), 0.0001);
        assertEquals(4.0, resultMatrix.getEntry(1, 0), 0.0001);
        assertEquals(5.0, resultMatrix.getEntry(1, 1), 0.0001);
        assertEquals(6.0, resultMatrix.getEntry(1, 2), 0.0001);
        assertEquals(7.0, resultMatrix.getEntry(2, 0), 0.0001);
        assertEquals(8.0, resultMatrix.getEntry(2, 1), 0.0001);
        assertEquals(9.0, resultMatrix.getEntry(2, 2), 0.0001);
    }
}