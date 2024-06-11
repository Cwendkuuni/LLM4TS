package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.WeightedSumFunction;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for Weight class.
 */
public class WeightTest {

    /**
     * Test constructor with diagonal weights.
     */
    @Test
    public void testWeightDiagonal() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                        {0.0, 2.0, 0.0},
                                                                        {0.0, 0.0, 3.0}});
        RealMatrix actual = weight.getWeight();
        assertEquals(expected, actual);
    }

    /**
     * Test constructor with non-diagonal weights.
     */
    @Test
    public void testWeightNonDiagonal() {
        double[][] weights = {{1.0, 0.5}, {0.5, 2.0}};
        Weight weight = new Weight(new Array2DRowRealMatrix(weights));
        RealMatrix expected = new Array2DRowRealMatrix(weights);
        RealMatrix actual = weight.getWeight();
        assertEquals(expected, actual);
    }

    /**
     * Test copy constructor with null weightMatrix.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWeightNull() {
        Weight weight = new Weight(null);
    }

    /**
     * Test copy constructor with non-square weightMatrix.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testWeightNonSquare() {
        double[][] weights = {{1.0, 0.5}, {0.5, 2.0}, {0.0, 3.0}};
        Weight weight = new Weight(new Array2DRowRealMatrix(weights));
    }

    /**
     * Test getWeight() method with diagonal weights.
     */
    @Test
    public void testGetWeightDiagonal() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                        {0.0, 2.0, 0.0},
                                                                        {0.0, 0.0, 3.0}});
        RealMatrix actual = weight.getWeight();
        assertEquals(expected, actual);
    }

    /**
     * Test getWeight() method with non-diagonal weights.
     */
    @Test
    public void testGetWeightNonDiagonal() {
        double[][] weights = {{1.0, 0.5}, {0.5, 2.0}};
        Weight weight = new Weight(new Array2DRowRealMatrix(weights));
        RealMatrix expected = new Array2DRowRealMatrix(weights);
        RealMatrix actual = weight.getWeight();
        assertEquals(expected, actual);
    }

    /**
     * Test getting weight matrix with singular values.
     */
    @Test
    public void testGetWeightSingularValues() {
        double[][] weights = {{1.0, 0.5}, {0.5, 2.0}};
        Weight weight = new Weight(new Array2DRowRealMatrix(weights));
        SingularValueDecomposition svd = new SingularValueDecomposition(weight.getWeight());
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5},
                                                                      {0.5, 1.5707963267948966}});
        RealMatrix actual = weight.getWeight();
        assertEquals(expected, actual);
    }
}