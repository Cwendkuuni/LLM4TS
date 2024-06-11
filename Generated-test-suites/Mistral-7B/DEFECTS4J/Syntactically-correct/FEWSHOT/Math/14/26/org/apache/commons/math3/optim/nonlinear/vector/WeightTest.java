package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.junit.Test;

public class WeightTest {
    @Test
    public void testWeightDiagonalConstruction() {
        double[] weights = new double[]{1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);

        RealMatrix expectedWeight = RealMatrixUtils.diagonalMatrix(weights);
        RealMatrix actualWeight = weight.getWeight();
        assertEquals(expectedWeight, actualWeight);
    }

    @Test
    public void testWeightMatrixConstruction() {
        RealMatrix weight = RealMatrixUtils.identityMatrix(3);
        Weight weightObj = new Weight(weight);

        RealMatrix expectedWeight = weight.copy();
        RealMatrix actualWeight = weightObj.getWeight();
        assertEquals(expectedWeight, actualWeight);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightMatrixNonSquareConstruction() {
        RealMatrix weight = RealMatrixUtils.identityMatrix(3);
        RealMatrix weightWithOneMoreColumn = RealMatrixUtils.appendColumn(weight, RealMatrixUtils.createColumnVector(1.0, 1.0, 1.0));
        new Weight(weightWithOneMoreColumn);
    }
}