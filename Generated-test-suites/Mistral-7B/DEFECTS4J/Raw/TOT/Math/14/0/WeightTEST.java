package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.unconstrained.GoalType;
import org.apache.commons.math3.optim.unconstrained.LeastSquaresProblem;
import org.apache.commons.math3.optim.unconstrained.NonlinearRegression;
import org.apache.commons.math3.optim.unconstrained.NonlinearRegressionResult;
import org.apache.commons.math3.optim.unconstrained.RealVector;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class WeightTest {

    @Test
    public void testWeightDiagonalConstruction() {
        double[] weights = new double[]{1.0, 2.0, 3.0};
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
        RealMatrix weights = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 2.0}, {0.0, 0.0}, {0.0, 0.0}});
        Weight weight = new Weight(weights);
        RealMatrix expected = weights.copy();

        RealMatrix actual = weight.getWeight();
        assertEquals(expected, actual);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightNonSquareMatrixConstruction() {
        RealMatrix weights = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 2.0}, {0.0, 0.0}});
        new Weight(new Array2DRowRealMatrix(2, 3, weights.getData())); // Using a non-square matrix
    }

    @Test
    public void testGetWeight() {
        double[] weights = new double[]{1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(1, 1, 2.0);
        expected.setEntry(2, 2, 3.0);

        RealMatrix actual = weight.getWeight();
        assertNotNull(actual);
        assertEquals(expected, actual);
    }
}