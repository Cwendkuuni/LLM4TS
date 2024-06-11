package org.apache.commons.math3.optimization;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class WeightTest {
    
    @Test
    public void testWeightConstructor1() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(1, 1, 2.0);
        expected.setEntry(2, 2, 3.0);
        assertEquals(expected, w.getWeight());
    }

    @Test
    public void testWeightConstructor2() {
        RealMatrix weight = new Array2DRowRealMatrix(2, 2);
        weight.setEntry(0, 0, 1.0);
        weight.setEntry(1, 1, 2.0);
        Weight w = new Weight(weight);
        assertEquals(weight, w.getWeight());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightConstructorNonSquareMatrixException() {
        RealMatrix weight = new Array2DRowRealMatrix(2, 3);
        new Weight(weight);
        fail("NonSquareMatrixException not thrown");
    }
}