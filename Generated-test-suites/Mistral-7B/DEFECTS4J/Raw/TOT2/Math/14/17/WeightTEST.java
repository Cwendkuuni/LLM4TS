import static org.junit.Assert.*;
import org.apache.commons.math3.LinearAlgebraTest;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.optimization.Weight;

public class WeightTest {
    @org.junit.Test
    public void testWeightDiagonalConstructor() {
        double[] weight = new double[]{1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expected = RealMatrixFactory.identity(3);
        RealMatrix actual = weight.getWeight();
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testWeightDiagonalConstructorNullInput() {
        Weight weightObj = new Weight(null);
        assertFalse(weightObj.getWeight() != null);
    }

    @org.junit.Test
    public void testWeightMatrixConstructor() {
        RealMatrix weight = RealMatrixFactory.identity(3);
        Weight weightObj = new Weight(weight);
        RealMatrix expected = weight;
        RealMatrix actual = weightObj.getWeight();
        assertEquals(expected, actual);
    }

    @org.junit.Test(expected = org.apache.commons.math3.linear.NonSquareMatrixException.class)
    public void testWeightMatrixConstructorNonSquare() {
        RealMatrix weight = RealMatrixFactory.identity(3).transpose();
        new Weight(weight);
    }

    @org.junit.Test
    public void testGetWeight() {
        double[] weight = new double[]{1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expected = RealMatrixFactory.identity(3);
        RealMatrix actual = weightObj.getWeight();
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testGetWeightNullWeightMatrix() {
        Weight weightObj = new Weight(null);
        assertNull(weightObj.getWeight());
    }

    @org.junit.Test
    public void testNegativeWeights() {
        double[] weight = new double[]{-1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3)
                .setEntry(0, 0, -1.0)
                .setEntry(1, 1, -1.0)
                .setEntry(2, 2, -1.0);
        RealMatrix actual = weightObj.getWeight();
        assertEquals(expected, actual);
    }

    @org.junit.Test
    public void testZeroSizeArray() {
        double[] weight = new double[0];
        assertThrows(IllegalArgumentException.class, () -> new Weight(weight));
    }

    @org.junit.Test
    public void testZeroSizedMatrix() {
        RealMatrix weight = RealMatrixFactory.EMPTY;
        assertThrows(IllegalArgumentException.class, () -> new Weight(weight));
    }
}