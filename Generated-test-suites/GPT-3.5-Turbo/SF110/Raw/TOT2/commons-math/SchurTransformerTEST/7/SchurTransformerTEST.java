package org.apache.commons.math3.linear;

import static org.junit.Assert.*;
import org.junit.Test;

public class SchurTransformerTest {

    @Test
    public void testConstructorNonSquareMatrixException() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}, {5, 6}});
        try {
            SchurTransformer transformer = new SchurTransformer(matrix);
            fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            // Exception expected
        }
    }

    @Test
    public void testGetP() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        double[][] expectedPData = new double[][]{{1, 2, 0}, {4, 5, 0}, {7, 8, 1}};
        RealMatrix expectedP = MatrixUtils.createRealMatrix(expectedPData);
        assertTrue(expectedP.equals(transformer.getP()));
    }

    // Add more test cases for other methods...

}