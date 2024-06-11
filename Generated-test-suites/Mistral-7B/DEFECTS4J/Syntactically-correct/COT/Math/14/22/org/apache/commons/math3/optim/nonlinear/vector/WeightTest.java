import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class WeightTest {

    @Test
    public void testWeightDiagonalConstructor() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expected = RealMatrixUtils.diagonalMatrix(weight);
        RealMatrix actual = weightObj.getWeight();
        assertEquals(expected, actual);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightSquareMatrixConstructorThrowsException() {
        RealMatrix weight = RealMatrixUtils.create(new double[][]{
                {1.0, 2.0},
                {3.0, 4.0}
        });
        new Weight(weight);
    }

    @Test
    public void testWeightSquareMatrixConstructor() {
        RealMatrix weight = RealMatrixUtils.create(new double[][]{
                {1.0, 0.0},
                {0.0, 2.0}
        });
        Weight weightObj = new Weight(weight);
        RealMatrix expected = weight.copy();
        RealMatrix actual = weightObj.getWeight();
        assertEquals(expected, actual);
    }
}