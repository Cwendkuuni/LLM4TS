import org.apache.commons.math3.optim.initialization.Weight;
import org.apache.commons.math3.optim.initialization.WeightImpl;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

public class WeightTest {

    @Test
    public void testWeightDiagonalConstruction() {
        double[] weights = new double[]{1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expected = RealMatrixUtils.diagonalMatrix(weights);
        RealMatrix actual = weight.getWeight();
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = org.apache.commons.math3.linear.NonSquareMatrixException.class)
    public void testWeightNonSquareConstruction() {
        double[] weights = new double[]{1.0, 2.0};
        new Weight(weights);
    }

    @Test
    public void testWeightMatrixConstruction() {
        double[][] matrixData = new double[][]{{1.0, 0.0}, {0.0, 2.0}};
        Weight weight = new Weight(new Array2DRowRealMatrix(matrixData));
        RealMatrix expected = new Array2DRowRealMatrix(matrixData);
        RealMatrix actual = weight.getWeight();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetNullWeight() {
        Weight weight = new Weight(null);
        assertNull(weight.getWeight());
    }
}