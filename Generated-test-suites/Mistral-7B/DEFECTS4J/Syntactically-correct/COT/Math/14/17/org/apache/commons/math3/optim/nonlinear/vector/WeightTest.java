package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math3.optimization.Weight;

public class WeightTest {
    @Test
    public void testWeightDiagonalConstructor() {
        double[] weights = new double[] {1.0, 2.0};
        Weight weight = new Weight(weights);
        RealMatrix expected = new Array2DRowRealMatrix(2, 2);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(1, 1, 2.0);
        RealMatrix actual = weight.getWeight();
        assertEquals(expected, actual);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightNonSquareConstructorThrowException() {
        double[] weights = new double[] {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
    }

    @Test
    public void testWeightSquareConstructor() {
        RealMatrix weight = new Array2DRowRealMatrix(new double[][] {{1.0, 0.0}, {0.0, 2.0}});
        Weight weightInstance = new Weight(weight);
        RealMatrix expected = weight.copy();
        RealMatrix actual = weightInstance.getWeight();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetWeight() {
        double[] weights = new double[] {1.0, 2.0};
        Weight weight = new Weight(weights);
        RealMatrix expected = new Array2DRowRealMatrix(2, 2);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(1, 1, 2.0);
        RealMatrix actual = weight.getWeight();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetWeightCopy() {
        double[] weights = new double[] {1.0, 2.0};
        Weight weight = new Weight(weights);
        RealMatrix expected = new Array2DRowRealMatrix(2, 2);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(1, 1, 2.0);
        RealMatrix actual = weight.getWeight().copy();
        assertEquals(expected, actual);
    }
}