
package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Test;

public class WeightTest {

    @Test
    public void testConstructorWithArray() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        assertNotNull(weight.getWeight());
        assertTrue(weight.getWeight() instanceof Array2DRowRealMatrix);
        assertEquals(3, weight.getWeight().getRowDimension());
        assertEquals(3, weight.getWeight().getColumnDimension());
        for (int i = 0; i < weights.length; i++) {
            assertEquals(weights[i], weight.getWeight().getEntry(i, i), 0.0);
        }
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        double[][] weights = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        Weight weight = new Weight(new Array2DRowRealMatrix(weights));
    }

    @Test
    public void testConstructorWithSquareMatrix() {
        double[][] weights = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        Weight weight = new Weight(new Array2DRowRealMatrix(weights));
        assertNotNull(weight.getWeight());
        assertTrue(weight.getWeight() instanceof Array2DRowRealMatrix);
        assertEquals(3, weight.getWeight().getRowDimension());
        assertEquals(3, weight.getWeight().getColumnDimension());
        for (int i = 0; i < weights.length; i++) {
            assertArrayEquals(weights[i], weight.getWeight().getRow(i), 0.0);
        }
    }

}
