
package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Test;

public class WeightTest {
    @Test
    public void testWeightConstructor() {
        double[] weightValues = {1.0, 2.0, 3.0};

        Weight weight = new Weight(weightValues);

        RealMatrix expectedMatrix = new Array2DRowRealMatrix(3, 3);
        expectedMatrix.setEntry(0, 0, 1.0);
        expectedMatrix.setEntry(1, 1, 2.0);
        expectedMatrix.setEntry(2, 2, 3.0);

        assertEquals(expectedMatrix, weight.getWeight());
    }

    @Test
    public void testWeightConstructorWithNonSquareMatrix() {
        RealMatrix weightMatrix = new Array2DRowRealMatrix(2, 3);

        try {
            Weight weight = new Weight(weightMatrix);

            fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            // Test passed
        }
    }

    @Test
    public void testGetWeight() {
        double[] weightValues = {1.0, 2.0, 3.0};

        Weight weight = new Weight(weightValues);

        RealMatrix expectedMatrix = new Array2DRowRealMatrix(3, 3);
        expectedMatrix.setEntry(0, 0, 1.0);
        expectedMatrix.setEntry(1, 1, 2.0);
        expectedMatrix.setEntry(2, 2, 3.0);

        assertEquals(expectedMatrix, weight.getWeight());
    }
}
