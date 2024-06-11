package org.apache.commons.math3.optimization;

import static org.junit.Assert.*;
import org.junit.Test;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.NonSquareMatrixException;

public class WeightTest {

    @Test
    public void testWeightDiagonal() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);

        RealMatrix weightMatrix = w.getWeight();

        RealVector v = weightMatrix.getColumnVector(0);
        assertEquals(3.0, v.getEntry(0), 0.0);
        assertEquals(0.0, v.getEntry(1), 0.0);
        assertEquals(0.0, v.getEntry(2), 0.0);

        v = weightMatrix.getColumnVector(1);
        assertEquals(0.0, v.getEntry(0), 0.0);
        assertEquals(2.0, v.getEntry(1), 0.0);
        assertEquals(0.0, v.getEntry(2), 0.0);

        v = weightMatrix.getColumnVector(2);
        assertEquals(0.0, v.getEntry(0), 0.0);
        assertEquals(0.0, v.getEntry(1), 0.0);
        assertEquals(3.0, v.getEntry(2), 0.0);

        RealMatrix weightInv = new Array2DRowRealMatrix(new double[][]{
                {1.0 / 1.0, 0.0, 0.0},
                {0.0, 1.0 / 2.0, 0.0},
                {0.0, 0.0, 1.0 / 3.0}
        });

        RealMatrix product = weightMatrix.multiply(weightInv);
        RealMatrix identity = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {0.0, 0.0, 1.0}
        });

        SingularValueDecomposition svd = new SingularValueDecomposition(identity);
        RealMatrix expected = svd.getT().multiply(svd.getS()).multiply(svd.getU());

        RealMatrix result = product.multiply(expected);

        assertTrue(result.determinant() > 0.99999999999);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightNonSquare() {
        double[] weight = {1.0, 2.0};
        new Weight(weight);
    }

    @Test
    public void testWeightCopy() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w1 = new Weight(weight);

        RealMatrix weightMatrix1 = w1.getWeight();

        Weight w2 = new Weight(weightMatrix1);
        RealMatrix weightMatrix2 = w2.getWeight();

        for (int i = 0; i < weightMatrix1.getRowDimension(); i++) {
            for (int j = 0; j < weightMatrix1.getColumnDimension(); j++) {
                assertEquals(weightMatrix1.getEntry(i, j), weightMatrix2.getEntry(i, j), 0.0);
            }
        }
    }
}