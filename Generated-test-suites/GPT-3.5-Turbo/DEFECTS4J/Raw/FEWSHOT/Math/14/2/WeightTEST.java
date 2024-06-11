package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Before;
import org.junit.Test;

public class WeightTest {
    private static final double[] WEIGHT_VALUES = {1.0, 2.0, 3.0};

    private Weight weight;

    @Before
    public void setUp() {
        weight = new Weight(WEIGHT_VALUES);
    }

    @Test
    public void testConstructorWithArray() {
        assertNotNull(weight);
    }

    @Test
    public void testConstructorWithRealMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(WEIGHT_VALUES.length, WEIGHT_VALUES.length);
        assertEquals(matrix, weight.getWeight());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithInvalidRealMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(WEIGHT_VALUES.length, WEIGHT_VALUES.length + 1);
        new Weight(matrix);
    }

    @Test
    public void testGetWeight() {
        assertNotNull(weight.getWeight());
    }
}