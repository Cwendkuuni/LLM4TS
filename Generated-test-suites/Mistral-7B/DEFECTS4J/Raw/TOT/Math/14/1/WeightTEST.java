package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.math3.optimization.Weight;

public class WeightTest {

    @Test
    public void testDiagonalWeight() {
        // Create a diagonal weight matrix
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);

        // Test getWeight method
        RealMatrix expectedWeight = new Array2DRowRealMatrix(weight);
        RealMatrix actualWeight = weightObj.getWeight();
        assertEquals(expectedWeight.toString(), actualWeight.toString());

        // Test with invalid input
        try {
            new Weight(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new Weight(new double[0]);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testNonDiagonalWeight() {
        // Create a non-diagonal weight matrix
        double[][] weight = {{1.0, 0.5}, {0.5, 1.0}};
        Weight weightObj = new Weight(new Array2DRowRealMatrix(weight));

        // Test getWeight method
        RealMatrix expectedWeight = new Array2DRowRealMatrix(weight);
        RealMatrix actualWeight = weightObj.getWeight();
        assertEquals(expectedWeight.toString(), actualWeight.toString());

        // Test with invalid input
        try {
            new Weight(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new Weight(new Array2DRowRealMatrix(new int[]{3, 2}));
            fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testSingularValueDecomposition() {
        // Create a weight matrix with rank < n
        double[][] weight = {{1.0, 0.5}, {0.5, 0.5}};
        Weight weightObj = new Weight(new Array2DRowRealMatrix(weight));

        // Test SVD
        SingularValueDecomposition svd = new SingularValueDecomposition(weightObj.getWeight());
        RealMatrix expectedS = svd.getS();
        RealMatrix actualS = weightObj.getWeight().operate(weightObj.getWeight().transpose());
        assertEquals(expectedS.toString(), actualS.toString());

        // Test with invalid input
        try {
            new Weight(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new Weight(new Array2DRowRealMatrix(new int[]{3, 2}));
            fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testNegativeWeight() {
        // Create a weight matrix with negative values
        double[] weight = {1.0, -2.0, 3.0};
        Weight weightObj = new Weight(weight);

        // Test getWeight method
        RealMatrix expectedWeight = new Array2DRowRealMatrix(weight);
        RealMatrix actualWeight = weightObj.getWeight();
        assertEquals(expectedWeight.toString(), actualWeight.toString());

        // Test with invalid input
        try {
            new Weight(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            new Weight(new double[0]);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}