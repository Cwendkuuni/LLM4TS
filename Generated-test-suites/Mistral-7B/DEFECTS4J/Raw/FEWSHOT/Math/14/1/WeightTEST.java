package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.junit.Test;

public class WeightTest {

    @Test
    public void testWeightDiagonalConstruction() {
        double[] weights = {1.0, 2.0, 3.0};
        RealMatrix expected = new Array2DRowRealMatrix(3, 3, weights, false);
        Weight weight = new Weight(weights);
        RealMatrix actual = weight.getWeight();
        assertEquals(expected.getRowDimension(), actual.getRowDimension());
        assertEquals(expected.getColumnDimension(), actual.getColumnDimension());
        assertArrayEquals(expected.getData(), actual.getData(), 0.0);
    }

    @Test
    public void testWeightMatrixConstruction() {
        RealMatrix weight = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.0},
                {0.0, 2.0},
                {0.0, 0.0}
        });
        Weight weightObj = new Weight(weight);
        RealMatrix actual = weightObj.getWeight();
        RealMatrix expected = weight.copy();
        assertEquals(expected.getRowDimension(), actual.getRowDimension());
        assertEquals(expected.getColumnDimension(), actual.getColumnDimension());
        assertArrayEquals(expected.getData(), actual.getData(), 0.0);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightMatrixConstructionException() {
        RealMatrix weight = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.0},
                {0.0, 2.0},
                {0.0, 3.0}
        });
        new Weight(weight);
    }

    @Test
    public void testGetWeight() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        assertNotNull(weight.getWeight());
        RealMatrix actual = weight.getWeight();
        RealMatrix expected = new Array2DRowRealMatrix(3, 3, weights, false);
        assertArrayEquals(expected.getData(), actual.getData(), 0.0);
    }

    @Test
    public void testWeightSingularValueDecomposition() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix actual = weight.getWeight();
        SingularValueDecomposition svd = new SingularValueDecomposition(actual);
        RealMatrix diagonal = svd.getDiagonalMatrix();
        assertEquals(3, diagonal.getRowDimension());
        assertEquals(3, diagonal.getColumnDimension());
        assertEquals(1.0, diagonal.getEntry(0, 0), 0.0);
        assertEquals(1.5811388300841898, diagonal.getEntry(1, 1), 0.0);
        assertEquals(2.165063502755015, diagonal.getEntry(2, 2), 0.0);
    }
}