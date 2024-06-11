package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixIterator;
import org.junit.Test;

public class WeightTest {

    @Test
    public void testWeightDiagonalConstructor() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight weightObj = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3, new double[][]{{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}});
        RealMatrix actual = weightObj.getWeight();
        assertArrayEquals(expected.getData(), actual.getData(), 0.0);
    }

    @Test
    public void testWeightSquareMatrixConstructor() {
        double[][] weightMatrixData = {{1.0, 0.5}, {0.5, 1.0}};
        RealMatrix weight = new Array2DRowRealMatrix(weightMatrixData);
        Weight weightObj = new Weight(weight);
        RealMatrix expected = weight.copy();
        RealMatrix actual = weightObj.getWeight();
        assertNotNull(actual);
        assertTrue(expected.equals(actual));
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightSquareMatrixException() {
        double[][] weightMatrixData = {{1.0, 0.5}, {0.5, 1.0}, {1.1, 1.2}};
        RealMatrix weight = new Array2DRowRealMatrix(weightMatrixData);
        new Weight(weight);
    }

    @Test
    public void testWeightMatrixIteration() {
        double[][] weightMatrixData = {{1.0, 0.5}, {0.5, 1.0}};
        RealMatrix weight = new Array2DRowRealMatrix(weightMatrixData);
        Weight weightObj = new Weight(weight);
        RealMatrix weightMatrix = weightObj.getWeight();
        RealMatrixIterator iterator = weightMatrix.iterator();
        double[][] expectedValues = weightMatrixData;
        double[] entry;
        int row = 0, col = 0;
        while (iterator.hasNext()) {
            entry = iterator.nextEntry();
            assertTrue(expectedValues[row][col] == entry[0] && expectedValues[row][col] == entry[1]);
            col++;
            if (col == weightMatrix.getColumnDimension()) {
                row++;
                col = 0;
            }
        }
    }
}