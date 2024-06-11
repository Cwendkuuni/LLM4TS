package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class WeightTest {

    @Test
    public void testWeightDiagonalConstructor() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);

        RealMatrix expected = new Array2DRowRealMatrix(3, 3);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(1, 1, 2.0);
        expected.setEntry(2, 2, 3.0);

        RealMatrix actual = weight.getWeight();
        Assert.assertArrayEquals(expected.getData(), actual.getData(), 0.0);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightConstructorWithNonSquareMatrix() {
        double[][] weight = {{1.0, 2.0}, {3.0, 4.0}};
        Weight weight = new Weight(new Array2DRowRealMatrix(weight));
    }

    @Test
    public void testWeightSquareMatrixConstructor() {
        double[][] weight = {{1.0, 2.0}, {2.0, 3.0}};
        Weight weight = new Weight(new Array2DRowRealMatrix(weight));

        RealMatrix expected = new Array2DRowRealMatrix(2, 2);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(0, 1, 2.0);
        expected.setEntry(1, 0, 2.0);
        expected.setEntry(1, 1, 3.0);

        RealMatrix actual = weight.getWeight();
        Assert.assertArrayEquals(expected.getData(), actual.getData(), 0.0);
    }

    @Test
    public void testWeightCopyConstructor() {
        RealMatrix weight = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        Weight weightInstance = new Weight(weight);

        RealMatrix expected = new Array2DRowRealMatrix(2, 2);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(0, 1, 2.0);
        expected.setEntry(1, 0, 2.0);
        expected.setEntry(1, 1, 3.0);

        RealMatrix actual = weightInstance.getWeight();
        Assert.assertArrayEquals(expected.getData(), actual.getData(), 0.0);
    }

    @Test
    public void testWeightGetWeight() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);

        RealMatrix expected = new Array2DRowRealMatrix(3, 3);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(1, 1, 2.0);
        expected.setEntry(2, 2, 3.0);

        RealMatrix actual = weight.getWeight();
        Assert.assertArrayEquals(expected.getData(), actual.getData(), 0.0);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightGetWeightFromNonSquareMatrix() {
        Weight weight = new Weight(new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}}));
        weight.getWeight();
    }
}