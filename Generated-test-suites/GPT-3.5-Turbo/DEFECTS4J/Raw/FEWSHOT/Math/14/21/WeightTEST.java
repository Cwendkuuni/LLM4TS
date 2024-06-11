import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Test;

public class WeightTest {

    @Test
    public void testWeightConstructorWithDoubleArray() {
        double[] weightValues = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weightValues);

        assertNotNull(weight.getWeight());
        assertEquals(3, weight.getWeight().getColumnDimension());
        assertEquals(3, weight.getWeight().getRowDimension());

        assertEquals(1.0, weight.getWeight().getEntry(0, 0), 0.0);
        assertEquals(2.0, weight.getWeight().getEntry(1, 1), 0.0);
        assertEquals(3.0, weight.getWeight().getEntry(2, 2), 0.0);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightConstructorWithNonSquareMatrix() {
        double[][] weightValues = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        RealMatrix weight = new Array2DRowRealMatrix(weightValues);
        new Weight(weight);
    }

    @Test
    public void testWeightConstructorWithSquareMatrix() {
        double[][] weightValues = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        RealMatrix weight = new Array2DRowRealMatrix(weightValues);
        Weight weightObject = new Weight(weight);

        assertNotNull(weightObject.getWeight());
        assertEquals(3, weightObject.getWeight().getColumnDimension());
        assertEquals(3, weightObject.getWeight().getRowDimension());

        assertEquals(1.0, weightObject.getWeight().getEntry(0, 0), 0.0);
        assertEquals(5.0, weightObject.getWeight().getEntry(1, 1), 0.0);
        assertEquals(9.0, weightObject.getWeight().getEntry(2, 2), 0.0);
    }

    @Test
    public void testGetWeight() {
        double[] weightValues = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weightValues);

        assertNotNull(weight.getWeight());
        assertEquals(3, weight.getWeight().getColumnDimension());
        assertEquals(3, weight.getWeight().getRowDimension());

        assertEquals(1.0, weight.getWeight().getEntry(0, 0), 0.0);
        assertEquals(2.0, weight.getWeight().getEntry(1, 1), 0.0);
        assertEquals(3.0, weight.getWeight().getEntry(2, 2), 0.0);
    }

}