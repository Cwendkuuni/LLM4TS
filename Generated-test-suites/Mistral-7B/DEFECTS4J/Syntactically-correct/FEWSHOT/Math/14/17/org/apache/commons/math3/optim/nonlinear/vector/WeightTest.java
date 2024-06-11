package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.junit.Test;

public class WeightTest {

    @Test
    public void testWeightDiagonalConstructor() {
        double[] weight = new double[]{1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3, new double[][]{
                {1.0, 0.0, 0.0},
                {0.0, 2.0, 0.0},
                {0.0, 0.0, 3.0}
        });
        RealMatrix actual = weightObj.getWeight();
        assertTrue(actual.equals(expected));
    }

    @Test
    public void testWeightNonSquareMatrixConstructor() {
        RealMatrix weight = new Array2DRowRealMatrix(3, 2, new double[][]{
                {1.0, 2.0},
                {3.0, 4.0},
                {5.0, 6.0}
        });
        Weight weightObj = new Weight(weight);
        assertFalse(weightObj instanceof Weight); // Throws NonSquareMatrixException
    }

    @Test
    public void testWeightCopyConstructor() {
        RealMatrix weight = new Array2DRowRealMatrix(3, 3, new double[][]{
                {1.0, 0.0, 0.0},
                {0.0, 2.0, 0.0},
                {0.0, 0.0, 3.0}
        });
        Weight weightObj = new Weight(weight);
        RealMatrix expected = weight.copy();
        RealMatrix actual = weightObj.getWeight();
        assertTrue(actual.equals(expected));
    }

    @Test
    public void testWeightGetWeight() {
        double[] weight = new double[]{1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3, new double[][]{
                {1.0, 0.0, 0.0},
                {0.0, 2.0, 0.0},
                {0.0, 0.0, 3.0}
        });
        RealMatrix actual = weightObj.getWeight();
        assertTrue(actual.equals(expected));

        // Test null weight
        Weight nullWeightObj = new Weight(null);
        assertNull(nullWeightObj.getWeight());

        // Test getting weight of already copied matrix
        Weight copyWeightObj = new Weight(weightObj.getWeight());
        RealMatrix copyWeight = copyWeightObj.getWeight();
        RealMatrix originalWeight = weightObj.getWeight();
        assertTrue(copyWeight.equals(originalWeight));
    }

    @Test
    public void testWeightSingularValueDecomposition() {
        double[] weight = new double[]{1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix actual = weightObj.getWeight();
        SingularValueDecomposition svd = new SingularValueDecomposition(actual);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3, svd.getS());
        assertTrue(actual.equals(expected));
    }
}