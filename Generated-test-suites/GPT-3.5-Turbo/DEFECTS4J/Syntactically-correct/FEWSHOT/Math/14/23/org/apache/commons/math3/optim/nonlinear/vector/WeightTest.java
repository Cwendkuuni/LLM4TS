package org.apache.commons.math3.optimization;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Test;

public class WeightTest {

    @Test
    public void testWeight() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expectedWeightMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                                {0.0, 2.0, 0.0},
                                                                                {0.0, 0.0, 3.0}});
        RealMatrix actualWeightMatrix = weight.getWeight();
        assertEquals(expectedWeightMatrix, actualWeightMatrix);
    }

    @Test
    public void testWeightWithNonSquareMatrix() {
        RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                          {4.0, 5.0, 6.0}});
        try {
            Weight weight = new Weight(weightMatrix);
            fail("Expected NonSquareMatrixException to be thrown");
        } catch (NonSquareMatrixException ex) {
            // Exception expected
        }
    }

    @Test
    public void testGetWeight() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        RealMatrix expectedWeightMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                                {0.0, 2.0, 0.0},
                                                                                {0.0, 0.0, 3.0}});
        RealMatrix actualWeightMatrix = weight.getWeight();
        assertEquals(expectedWeightMatrix, actualWeightMatrix);
    }
}