package org.apache.commons.math3.optimization.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.optimization.Weightable;
import org.junit.Test;

/**
 * Test cases for Weight class.
 */
public class WeightTest {

    /**
     * Test constructor with diagonal weight.
     */
    @Test
    public void testDiagonalWeightConstruction() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expectedWeight = new Array2DRowRealMatrix(3, 3);
        for (int i = 0; i < 3; i++) {
            expectedWeight.setEntry(i, i, weight[i]);
        }
        RealMatrix actualWeight = weightObj.getWeight();
        assertEquals(expectedWeight, actualWeight);
    }

    /**
     * Test constructor with non-square weight.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareWeightConstruction() {
        RealMatrix weight = new Array2DRowRealMatrix(3, 2);
        new Weight(weight);
    }

    /**
     * Test constructor with square weight.
     */
    @Test
    public void testSquareWeightConstruction() {
        double[][] weightData = {{1.0, 0.0}, {0.0, 1.0}};
        RealMatrix weight = new Array2DRowRealMatrix(weightData);
        Weight weightObj = new Weight(weight);
        RealMatrix expectedWeight = weight.copy();
        RealMatrix actualWeight = weightObj.getWeight();
        assertEquals(expectedWeight, actualWeight);
    }

    /**
     * Test getWeight method with null Weightable object.
     */
    @Test
    public void testGetWeightNullWeightable() {
        assertNull(new Weight(null).getWeight());
    }

    /**
     * Test getWeight method with Weightable object without weight.
     */
    @Test
    public void testGetWeightEmptyWeightable() {
        Weightable weightable = () -> new RealVector();
        assertNull(new Weight(weightable).getWeight());
    }

    /**
     * Test getWeight method with Weightable object having weight.
     */
    @Test
    public void testGetWeightWithWeightable() {
        double[] weightData = {1.0, 2.0, 3.0};
        Weightable weightable = () -> new Array2DRowRealMatrix(weightData);
        Weight weightObj = new Weight(weightable);
        RealMatrix expectedWeight = new Array2DRowRealMatrix(3, 3);
        for (int i = 0; i < 3; i++) {
            expectedWeight.setEntry(i, i, weightData[i]);
        }
        RealMatrix actualWeight = weightObj.getWeight();
        assertEquals(expectedWeight, actualWeight);
    }

    /**
     * Test getWeight method with Singular Value Decomposition object.
     */
    @Test
    public void testGetWeightSvd() {
        double[][] data = {{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        SingularValueDecomposition svd = new SingularValueDecomposition(matrix);
        Weight weightObj = new Weight(svd.getU());
        RealMatrix expectedWeight = svd.getS();
        RealMatrix actualWeight = weightObj.getWeight();
        assertEquals(expectedWeight, actualWeight);
    }

    /**
     * Test constructor with null weight array.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testDiagonalWeightConstructionNullArray() {
        new Weight(null);
    }

    /**
     * Test constructor with empty weight array.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testDiagonalWeightConstructionEmptyArray() {
        new Weight(new double[0]);
    }

    /**
     * Test constructor with negative weight.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testDiagonalWeightConstructionNegativeWeight() {
        double[] weight = {-1.0, 2.0, 3.0};
        new Weight(weight);
    }
}