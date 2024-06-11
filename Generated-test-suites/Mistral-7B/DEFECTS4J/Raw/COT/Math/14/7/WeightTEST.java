package org.apache.commons.math3.optimization.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.optimization.WeightInterface;
import org.apache.commons.math3.optimization.test.WeightTests;

import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for Weight class.
 */
public class WeightTest {

    private Weight weight;
    private RealMatrix weightMatrix;
    private double[] initialGuess;

    @Before
    public void setUp() {
        initialGuess = new double[]{1.0, 2.0, 3.0};
        weight = new Weight(initialGuess);
        weightMatrix = weight.getWeight();
    }

    // Test the constructor with a diagonal weight matrix

    @Test
    public void testDiagonalConstructor() {
        Weight weight1 = new Weight(new double[]{1.0, 1.0, 1.0});
        Weight weight2 = new Weight(new double[]{2.0, 2.0, 2.0});
        RealMatrix expectedWeight1 = new Array2DRowRealMatrix(3, 3, new double[][]{
                {1.0, 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {0.0, 0.0, 1.0}
        });
        RealMatrix expectedWeight2 = new Array2DRowRealMatrix(3, 3, new double[][]{
                {2.0, 0.0, 0.0},
                {0.0, 2.0, 0.0},
                {0.0, 0.0, 2.0}
        });
        assertEquals(expectedWeight1, weight1.getWeight());
        assertEquals(expectedWeight2, weight2.getWeight());
    }

    @Test
    public void testNonDiagonalConstructor() {
        double[][] weightData = new double[][]{{1.0, 2.0}, {2.0, 3.0}};
        RealMatrix expectedWeight = new Array2DRowRealMatrix(2, 2, weightData);
        Weight weight = new Weight(expectedWeight);
        RealMatrix actualWeight = weight.getWeight();
        assertEquals(expectedWeight, actualWeight);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareConstructor() {
        RealMatrix weight = new Array2DRowRealMatrix(3, 2);
        new Weight(weight);
    }

    @Test
    public void testGetWeight() {
        assertEquals(weightMatrix, weight.getWeight());
    }

    // Test the getWeight method with a WeightInterface

    @Test
    public void testGetWeightFromWeightInterface() {
        WeightInterface weightInterface = new Weight() {
            @Override
            public RealMatrix getWeight() {
                return new Array2DRowRealMatrix(new double[][]{
                        {1.0, 0.0},
                        {0.0, 2.0},
                        {0.0, 0.5}
                });
            }
        };
        Weight weight = new Weight(weightInterface);
        RealMatrix expectedWeight = new Array2DRowRealMatrix(3, 3, new double[][]{
                {1.0, 0.0, 0.0},
                {0.0, 2.0, 0.0},
                {0.0, 0.0, 0.5}
        });
        assertEquals(expectedWeight, weight.getWeight());
    }

    // Test edge cases and exceptions

    @Test(expected = DimensionMismatchException.class)
    public void testGetWeightWithNonSquareWeightInterface() {
        WeightInterface weightInterface = new Weight() {
            @Override
            public RealMatrix getWeight() {
                return new Array2DRowRealMatrix(2, 3);
            }
        };
        new Weight(weightInterface);
    }

    @Test
    public void testSingularValueDecomposition() {
        SingularValueDecomposition svd = new SingularValueDecomposition(weightMatrix);
        RealMatrix diagonalMatrix = svd.getDiagonalMatrix();
        RealVector singularValues = svd.getSingularValues();
        double[] actualDiagonal = diagonalMatrix.getColumnVector(0).toArray();
        double[] expectedDiagonal = new double[]{1.0, 1.0, 1.0};
        assertArrayEquals(expectedDiagonal, actualDiagonal);
        assertFalse(singularValues.getEntry(1) < 0.0);
        assertFalse(singularValues.getEntry(2) < 0.0);
    }

    @Test
    public void testPointValuePair() {
        PointValuePair pvp = new PointValuePair(new double[]{1.0, 2.0, 3.0}, 1.0);
        Weight weight = new Weight(pvp);
        RealMatrix expectedWeight = new Array2DRowRealMatrix(3, 3, new double[][]{
                {1.0, 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {0.0, 0.0, 1.0}
        });
        assertEquals(expectedWeight, weight.getWeight());
    }

    @Test(expected = NullPointerException.class)
    public void testNullPointValuePair() {
        new Weight(null);
    }

    @Test
    public void testWeightWithZeroDeterminant() {
        double[][] weightData = new double[][]{{1.0, 0.0}, {0.0, 1.0}};
        RealMatrix expectedWeight = new Array2DRowRealMatrix(2, 2, weightData);
        Weight weight = new Weight(expectedWeight);
        RealMatrix actualWeight = weight.getWeight();
        assertEquals(expectedWeight, actualWeight);
    }
}