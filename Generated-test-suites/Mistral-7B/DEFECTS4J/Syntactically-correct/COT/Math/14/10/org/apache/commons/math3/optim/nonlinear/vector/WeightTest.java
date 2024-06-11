package org.apache.commons.math3.optimization.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.optimization.WeightTest;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.junit.Test;

/**
 * Test class for Weight.
 */
public class WeightTest {

    /**
     * Test constructor with double array.
     */
    @Test
    public void testWeightConstructorWithDoubleArray() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightInstance = new Weight(weight);
        RealMatrix expectedWeight = new Array2DRowRealMatrix(weight);
        RealMatrix actualWeight = weightInstance.getWeight();
        assertEquals(expectedWeight, actualWeight);
    }

    /**
     * Test constructor with RealMatrix.
     */
    @Test
    public void testWeightConstructorWithRealMatrix() {
        double[][] weight = {{1.0, 0.0}, {0.0, 2.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weight);
        Weight weightInstance = new Weight(weightMatrix);
        RealMatrix expectedWeight = weightMatrix.copy();
        RealMatrix actualWeight = weightInstance.getWeight();
        assertEquals(expectedWeight, actualWeight);
    }

    /**
     * Test getWeight method with diagonal matrix.
     */
    @Test
    public void testGetWeightWithDiagonalMatrix() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightInstance = new Weight(weight);
        RealMatrix expectedWeight = new Array2DRowRealMatrix(weight);
        RealMatrix actualWeight = weightInstance.getWeight();
        assertEquals(expectedWeight, actualWeight);
    }

    /**
     * Test getWeight method with non-diagonal matrix.
     */
    @Test(expected = org.apache.commons.math3.linear.NonSquareMatrixException.class)
    public void testGetWeightWithNonDiagonalMatrix() {
        double[][] weight = {{1.0, 0.0}, {0.0, 2.0}, {3.0, 4.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weight);
        Weight weightInstance = new Weight(weightMatrix);
        RealMatrix actualWeight = weightInstance.getWeight();
        fail("Expected NonSquareMatrixException");
    }

    /**
     * Test getWeight method with null Weight object.
     */
    @Test(expected = NullPointerException.class)
    public void testGetWeightWithNullWeightObject() {
        Weight weightInstance = null;
        RealMatrix actualWeight = weightInstance.getWeight();
        fail("Expected NullPointerException");
    }

    /**
     * Test getWeight method with empty Weight object.
     */
    @Test
    public void testGetWeightWithEmptyWeightObject() {
        Weight weightInstance = new Weight(new Array2DRowRealMatrix(new double[0][0]));
        RealMatrix expectedWeight = new Array2DRowRealMatrix(new double[0][0]);
        RealMatrix actualWeight = weightInstance.getWeight();
        assertEquals(expectedWeight, actualWeight);
    }

    /**
     * Test exception handling when creating Weight instance with non-square matrix.
     */
    @Test(expected = org.apache.commons.math3.linear.NonSquareMatrixException.class)
    public void testWeightConstructorWithNonSquareMatrix() {
        double[][] weight = {{1.0, 0.0}, {0.0, 2.0}, {3.0, 4.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weight);
        new Weight(weightMatrix);
        fail("Expected NonSquareMatrixException");
    }

    /**
     * Test if Singular Value Decomposition (SVD) of Weight matrix preserves its diagonal structure.
     */
    @Test
    public void testSingularValueDecomposition() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightInstance = new Weight(weight);
        RealMatrix weightMatrix = weightInstance.getWeight();
        SingularValueDecomposition svd = new SingularValueDecomposition(weightMatrix);
        RealMatrix diagonalMatrix = svd.getDiagonalMatrix();
        RealVector diagonalVector = diagonalMatrix.getColumnVector(0);
        double[] diagonal = new double[diagonalVector.getDimension()];
        diagonalVector.toArray(diagonal);
        assertArrayEquals(weight, diagonal, 0.001);
    }

}