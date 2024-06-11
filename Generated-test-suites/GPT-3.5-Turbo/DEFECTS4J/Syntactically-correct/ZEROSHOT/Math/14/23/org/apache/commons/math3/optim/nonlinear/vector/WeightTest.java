import org.junit.Test;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

public class WeightTest {
    @Test
    public void testWeightConstructorWithArray() {
        double[] weight = {1, 2, 3};
        Weight w = new Weight(weight);

        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1, 0, 0},
                                                                     {0, 2, 0},
                                                                     {0, 0, 3}});
        RealMatrix actual = w.getWeight();
        assertMatrixEquals(expected, actual);
    }

    @Test
    public void testWeightConstructorWithMatrix() {
        RealMatrix weight = new Array2DRowRealMatrix(new double[][]{{1, 0, 0},
                                                                   {0, 2, 0},
                                                                   {0, 0, 3}});
        Weight w = new Weight(weight);

        RealMatrix expected = weight.copy();
        RealMatrix actual = w.getWeight();
        assertMatrixEquals(expected, actual);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightConstructorWithNonSquareMatrix() {
        RealMatrix weight = new Array2DRowRealMatrix(new double[][]{{1, 0},
                                                                   {0, 2},
                                                                   {0, 0}});
        Weight w = new Weight(weight);
    }

    private void assertMatrixEquals(RealMatrix m1, RealMatrix m2) {
        double[][] array1 = m1.getData();
        double[][] array2 = m2.getData();

        for (int i = 0; i < array1.length; i++) {
            assertArrayEquals(array1[i], array2[i]);
        }
    }

    private void assertArrayEquals(double[] a1, double[] a2) {
        assert a1.length == a2.length;

        for (int i = 0; i < a1.length; i++) {
            assert Math.abs(a1[i] - a2[i]) < 1e-10;
        }
    }
}