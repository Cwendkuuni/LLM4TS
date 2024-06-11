package org.apache.commons.math3.optimization.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.optimization.WeightDependence;
import org.junit.Test;

/**
 * Test cases for Weight class.
 */
public class WeightTest {

    /**
     * Test constructor with double array.
     */
    @Test
    public void testConstructorWithDoubleArray() {
        double[] weights = new double[]{1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expected = RealMatrixUtils.diagonalMatrix(weights);
        RealMatrix actual = weight.getWeight();
        assertEquals(expected, actual);
    }

    /**
     * Test constructor with RealMatrix.
     */
    @Test
    public void testConstructorWithRealMatrix() {
        double[][] weights = new double[][]{{1.0, 0.0}, {0.0, 2.0}};
        RealMatrix weightMatrix = RealMatrixUtils.createMatrix(weights);
        Weight weight = new Weight(weightMatrix);
        RealMatrix expected = weightMatrix.copy();
        RealMatrix actual = weight.getWeight();
        assertEquals(expected, actual);
    }

    /**
     * Test getWeight method with diagonal matrix.
     */
    @Test
    public void testGetWeightWithDiagonalMatrix() {
        double[] weights = new double[]{1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expected = RealMatrixUtils.diagonalMatrix(weights);
        RealMatrix actual = weight.getWeight();
        assertEquals(expected, actual);
    }

    /**
     * Test getWeight method with non-square matrix.
     */
    @Test(expected = org.apache.commons.math3.linear.NonSquareMatrixException.class)
    public void testGetWeightWithNonSquareMatrix() {
        double[][] weights = new double[][]{{1.0, 2.0}, {3.0, 4.0}};
        RealMatrix weightMatrix = RealMatrixUtils.createMatrix(weights);
        Weight weight = new Weight(weightMatrix);
        weight.getWeight();
    }

    /**
     * Test edge case with empty diagonal matrix.
     */
    @Test(expected = org.apache.commons.math3.linear.NonSquareMatrixException.class)
    public void testConstructorWithEmptyDiagonalMatrix() {
        double[] emptyDiagonal = new double[0];
        new Weight(emptyDiagonal);
    }

    /**
     * Test edge case with null double array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullDoubleArray() {
        new Weight(null);
    }

    /**
     * Test edge case with null RealMatrix.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullRealMatrix() {
        new Weight(null);
    }

    /**
     * Test edge case with non-square RealMatrix.
     */
    @Test(expected = org.apache.commons.math3.linear.NonSquareMatrixException.class)
    public void testConstructorWithNonSquareRealMatrix() {
        double[][] nonSquareMatrix = new double[][]{{1.0, 2.0}, {0.0, 3.0}};
        RealMatrix nonSquareRealMatrix = RealMatrixUtils.createMatrix(nonSquareMatrix);
        new Weight(nonSquareRealMatrix);
    }
}