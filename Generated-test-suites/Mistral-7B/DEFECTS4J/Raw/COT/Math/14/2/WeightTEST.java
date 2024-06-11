package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.optim.AbstractOptimizationTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class WeightTest extends AbstractOptimizationTest {

    private Weight weight;
    private double[] weights;
    private RealMatrix weightMatrix;

    public WeightTest(double[] weights) {
        this.weights = weights;
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new double[]{1.0, 1.0}},
                {new double[]{0.5, 0.5, 0.5}},
                {new double[]{1.0, 0.0}, {0.0, 1.0}},
                {null}
        });
    }

    @Test
    public void testConstructorWithArray() {
        weight = new Weight(weights);
        weightMatrix = weight.getWeight();
        assertEquals(weights.length, weightMatrix.getRowDimension());
        assertEquals(weights.length, weightMatrix.getColumnDimension());
        assertNotNull(weightMatrix);
        for (int i = 0; i < weights.length; i++) {
            assertEquals(weights[i], weightMatrix.getEntry(i, i), 1e-12);
        }
    }

    @Test
    public void testConstructorWithMatrix() throws NonSquareMatrixException {
        weightMatrix = RealMatrixUtils.createMatrix(weights.length, weights.length);
        for (int i = 0; i < weights.length; i++) {
            weightMatrix.setEntry(i, i, weights[i]);
        }
        weight = new Weight(weightMatrix);
        assertEquals(weightMatrix, weight.getWeight());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() throws NonSquareMatrixException {
        RealMatrix matrix = RealMatrixUtils.createMatrix(2, 3);
        new Weight(matrix);
    }

    @Test
    public void testGetWeight() {
        weight = new Weight(weights);
        assertNotNull(weight.getWeight());
        assertEquals(weights.length, weight.getWeight().getRowDimension());
        assertEquals(weights.length, weight.getWeight().getColumnDimension());
    }

    @Test
    public void testEquals() {
        double[] weights1 = {1.0, 1.0};
        double[] weights2 = {1.0, 1.0};
        Weight weight1 = new Weight(weights1);
        Weight weight2 = new Weight(weights2);
        Weight weight3 = new Weight(new double[]{2.0, 2.0});
        assertTrue(weight1.equals(weight1));
        assertTrue(weight1.equals(weight2));
        assertFalse(weight1.equals(weight3));
        assertFalse(weight1.equals(null));
        assertFalse(weight1.equals(""));
    }

    @Test
    public void testHashCode() {
        double[] weights1 = {1.0, 1.0};
        double[] weights2 = {1.0, 1.0};
        Weight weight1 = new Weight(weights1);
        Weight weight2 = new Weight(weights2);
        assertEquals(weight1.hashCode(), weight1.hashCode());
        assertEquals(weight1.hashCode(), weight2.hashCode());
        assertNotEquals(weight1.hashCode(), new Object().hashCode());
    }

    @Test
    public void testToString() {
        double[] weights1 = {1.0, 1.0};
        Weight weight1 = new Weight(weights1);
        assertEquals("Weight(matrix=[[1.0, 1.0], [1.0, 1.0]])", weight1.toString());
    }
}