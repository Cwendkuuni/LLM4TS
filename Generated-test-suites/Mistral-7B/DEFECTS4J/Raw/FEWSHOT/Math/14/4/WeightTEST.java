package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;

public class WeightTest {

    @Test
    public void testWeightDiagonalConstruction() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);

        RealMatrix expected = new Array2DRowRealMatrix(3, 3);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(1, 1, 2.0);
        expected.setEntry(2, 2, 3.0);

        RealMatrix actual = weight.getWeight();
        assertEquals(expected, actual);
    }

    @Test
    public void testWeightMatrixConstruction() {
        RealMatrix weight = new Array2DRowRealMatrix(new double[][]{
                {2.0, 0.0},
                {0.0, 1.5},
                {-1.0, -1.0}
        });

        Weight weightObj = new Weight(weight);
        RealMatrix expected = weight.copy();

        RealMatrix actual = weightObj.getWeight();
        assertEquals(expected, actual);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightMatrixNonSquareException() {
        RealMatrix weight = new Array2DRowRealMatrix(new double[][]{
                {2.0},
                {0.0, 1.5},
                {-1.0, -1.0}
        });

        new Weight(weight);
    }

    @Test
    public void testGetWeightNull() {
        Weight weight = new Weight(null);
        assertNull(weight.getWeight());
    }
}