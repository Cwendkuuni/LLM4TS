package org.apache.commons.math3.linear;

import org.junit.Test;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.exception.util.*;

public class SchurTransformerTest {

    @Test
    public void testGetP() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        assertEqualMatrix(schurTransformer.getP(), new double[][]{{1, 0}, {0, 1}});
    }

    @Test
    public void testGetPT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        assertEqualMatrix(schurTransformer.getPT(), new double[][]{{1, 3}, {2, 4}});
    }

    @Test
    public void testGetT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
        assertEqualMatrix(schurTransformer.getT(), new double[][]{{1, 2}, {0, -2}});
    }

    // Exception handling: NonSquareMatrixException
    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}});
        SchurTransformer schurTransformer = new SchurTransformer(matrix);
    }

    private void assertEqualMatrix(RealMatrix matrix1, double[][] matrix2) {
        double[][] data = matrix1.getData();
        for (int i = 0; i < matrix2.length; i++) {
            for (int j = 0; j < matrix2[i].length; j++) {
                assertEquals(data[i][j], matrix2[i][j]);
            }
        }
    }

    private void assertEquals(double value1, double value2) {
        double epsilon = 1e-6;
        assertEquals(value1, value2, epsilon);
    }

    private void assertEquals(double value1, double value2, double epsilon) {
        if (Math.abs(value1 - value2) > epsilon) {
            throw new AssertionError("Values are not equal: " + value1 + ", " + value2);
        }
    }
}