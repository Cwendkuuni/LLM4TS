package org.apache.commons.math3.optimization;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WeightTest {

    private static final double EPSILON = 1e-10;
    private double[] diagonal;
    private Weight weight;
    private Array2DRowRealMatrix diagMatrix;


    @Before
    public void setUp() {
        diagonal = new double[]{1.0, 2.0, 3.0};
        diagMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                               {0.0, 2.0, 0.0},
                                                               {0.0, 0.0, 3.0}});
    }

    @Test
    public void testWeightConstructor_WithValidParameters_ShouldCreateDiagonalMatrix() {
        weight = new Weight(diagonal);
        assertEquals(diagMatrix, weight.getWeight());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightConstructor_WithNonSquareMatrix_ShouldThrowNonSquareMatrixException() {
        Array2DRowRealMatrix nonSquareMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0},
                                                                                       {3.0, 4.0},
                                                                                       {5.0, 6.0}});
        weight = new Weight(nonSquareMatrix);
    }

    @Test
    public void testWeightConstructor_WithSquareMatrix_ShouldCreateCopyOfMatrix() {
        weight = new Weight(diagMatrix);
        assertNotSame(diagMatrix, weight.getWeight());
        assertEquals(diagMatrix, weight.getWeight());
    }
}