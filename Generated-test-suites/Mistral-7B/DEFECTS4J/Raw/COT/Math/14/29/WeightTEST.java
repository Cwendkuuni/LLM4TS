package org.apache.commons.math3.optimization.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.optimization.WeightTest;

import org.junit.Test;

/**
 * Test class for Weight class.
 */
public class WeightTest {

    /**
     * Test constructor with array argument.
     */
    @Test
    public void testConstructorWithArrayArgument() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightInstance = new Weight(weight);
        RealMatrix expectedWeightMatrix = RealMatrixUtils.createIdentityMatrix(3);
        RealMatrix actualWeightMatrix = weightInstance.getWeight();
        assertEquals(expectedWeightMatrix, actualWeightMatrix);
    }

    /**
     * Test constructor with matrix argument.
     */
    @Test
    public void testConstructorWithMatrixArgument() {
        double[][] weightMatrixData = {{1.0, 0.0}, {0.0, 2.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weightMatrixData);
        Weight weightInstance = new Weight(weightMatrix);
        RealMatrix expectedWeightMatrix = weightMatrix;
        RealMatrix actualWeightMatrix = weightInstance.getWeight();
        assertEquals(expectedWeightMatrix, actualWeightMatrix);
    }

    /**
     * Test NonSquareMatrixException when constructor with matrix argument is called with non-square matrix.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrixArgument() {
        double[][] weightMatrixData = {{1.0, 0.0}, {0.0, 2.0, 3.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weightMatrixData);
        new Weight(weightMatrix);
    }

    /**
     * Test getWeight method with diagonal weight matrix.
     */
    @Test
    public void testGetWeightWithDiagonalWeightMatrix() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightInstance = new Weight(weight);
        RealMatrix expectedWeightMatrix = RealMatrixUtils.createIdentityMatrix(3);
        RealMatrix actualWeightMatrix = weightInstance.getWeight();
        assertEquals(expectedWeightMatrix, actualWeightMatrix);
    }

    /**
     * Test getWeight method with non-diagonal weight matrix.
     */
    @Test
    public void testGetWeightWithNonDiagonalWeightMatrix() {
        double[][] weightMatrixData = {{1.0, 0.5}, {0.5, 2.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weightMatrixData);
        Weight weightInstance = new Weight(weightMatrix);
        RealMatrix expectedWeightMatrix = weightMatrix;
        RealMatrix actualWeightMatrix = weightInstance.getWeight();
        assertEquals(expectedWeightMatrix, actualWeightMatrix);
    }

    /**
     * Test getWeight method returns null if Weight instance is null.
     */
    @Test
    public void testGetWeightWithNullWeightInstance() {
        Weight weightInstance = null;
        assertNull(weightInstance.getWeight());
    }

    /**
     * Test getWeight method returns null if weight matrix is null.
     */
    @Test
    public void testGetWeightWithNullWeightMatrix() {
        Weight weightInstance = new Weight(new Array2DRowRealMatrix(new double[0][0]));
        assertNull(weightInstance.getWeight());
    }
}