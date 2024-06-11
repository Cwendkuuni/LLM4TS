package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixTestCase;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.GradientDescent;
import org.apache.commons.math3.optim.nonlinear.LevenbergMarquardt;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link Weight}.
 */
public class WeightTest extends RealMatrixTestCase {

    /**
     * Test constructor with a diagonal weight matrix.
     */
    @Test
    public void testDiagonalWeightMatrix() {
        double[] weights = new double[]{1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3);
        expected.setEntry(0, 0, weights[0]);
        expected.setEntry(1, 1, weights[1]);
        expected.setEntry(2, 2, weights[2]);
        RealMatrix weightMatrix = weight.getWeight();
        assertEquals(expected, weightMatrix);
    }

    /**
     * Test constructor with a non-diagonal weight matrix.
     */
    @Test
    public void testNonDiagonalWeightMatrix() {
        double[][] weights = new double[][]{{1.0, 0.5}, {0.5, 2.0}};
        RealMatrix weight = new Array2DRowRealMatrix(weights);
        Weight weightObj = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(weights);
        RealMatrix weightMatrix = weightObj.getWeight();
        assertEquals(expected, weightMatrix);
    }

    /**
     * Test constructor with a null weight matrix.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullWeightMatrix() {
        RealMatrix weight = null;
        new Weight(weight);
    }

    /**
     * Test constructor with a non-square weight matrix.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareWeightMatrix() {
        double[][] weights = new double[][]{{1.0, 0.5}, {0.5}};
        RealMatrix weight = new Array2DRowRealMatrix(weights);
        new Weight(weight);
    }

    /**
     * Test {@link Weight#Weight(double[])}.
     */
    @Test
    public void testWeightDiagonal() {
        double[] weights = new double[]{1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3);
        expected.setEntry(0, 0, weights[0]);
        expected.setEntry(1, 1, weights[1]);
        expected.setEntry(2, 2, weights[2]);
        RealMatrix weightMatrix = weight.getWeight();
        assertEquals(expected, weightMatrix);
    }

    /**
     * Test {@link Weight#Weight(RealMatrix)}.
     */
    @Test
    public void testWeightNonDiagonal() {
        double[][] weights = new double[][]{{1.0, 0.5}, {0.5, 2.0}};
        RealMatrix weight = new Array2DRowRealMatrix(weights);
        Weight weightObj = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(weights);
        RealMatrix weightMatrix = weightObj.getWeight();
        assertEquals(expected, weightMatrix);
    }

    /**
     * Test {@link Weight#getWeight()}.
     */
    @Test
    public void testGetWeight() {
        double[][] weights = new double[][]{{1.0, 0.5}, {0.5, 2.0}};
        RealMatrix weight = new Array2DRowRealMatrix(weights);
        Weight weightObj = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(weights);
        RealMatrix weightMatrix = weightObj.getWeight();
        assertEquals(expected, weightMatrix);
    }

    /**
     * Test {@link Weight#Weight(double[])}.
     */
    @Test
    public void testWeightEmptyDiagonal() {
        double[] weights = new double[0];
        Weight weight = new Weight(weights);
        RealMatrix expected = new Array2DRowRealMatrix(0, 0);
        RealMatrix weightMatrix = weight.getWeight();
        assertEquals(expected, weightMatrix);
    }

    /**
     * Test {@link Weight#Weight(RealMatrix)}.
     */
    @Test
    public void testWeightEmptyNonDiagonal() {
        RealMatrix weight = new Array2DRowRealMatrix(0, 0);
        Weight weightObj = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(0, 0);
        RealMatrix weightMatrix = weightObj.getWeight();
        assertEquals(expected, weightMatrix);
    }

    /**
     * Test {@link Weight#Weight(double[])}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWeightNullDiagonal() {
        double[] weights = null;
        new Weight(weights);
    }

    /**
     * Test {@link Weight#Weight(RealMatrix)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWeightNullNonDiagonal() {
        RealMatrix weight = null;
        new Weight(weight);
    }

    /**
     * Test {@link Weight#Weight(double[])}.
     */
    @Test
    public void testWeightNegativeDiagonal() {
        double[] weights = new double[]{-1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3);
        expected.setEntry(0, 0, weights[0]);
        expected.setEntry(1, 1, weights[1]);
        expected.setEntry(2, 2, weights[2]);
        RealMatrix weightMatrix = weight.getWeight();
        assertEquals(expected, weightMatrix);
    }

    /**
     * Test {@link Weight#Weight(RealMatrix)}.
     */
    @Test
    public void testWeightNegativeNonDiagonal() {
        double[][] weights = new double[][]{{-1.0, 0.5}, {0.5, 2.0}};
        RealMatrix weight = new Array2DRowRealMatrix(weights);
        Weight weightObj = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(weights);
        RealMatrix weightMatrix = weightObj.getWeight();
        assertEquals(expected, weightMatrix);
    }

    /**
     * Test {@link Weight#getWeight()} with null argument.
     */
    @Test
    public void testGetWeightNull() {
        Weight weight = new Weight(new double[0]);
        assertNotNull(weight.getWeight());
    }

    /**
     * Test {@link Weight#getWeight()} with non-null argument.
     */
    @Test
    public void testGetWeightNotNull() {
        double[][] weights = new double[][]{{1.0, 0.5}, {0.5, 2.0}};
        Weight weight = new Weight(new Array2DRowRealMatrix(weights));
        RealMatrix expected = new Array2DRowRealMatrix(weights);
        RealMatrix weightMatrix = weight.getWeight();
        assertEquals(expected, weightMatrix);
    }
}