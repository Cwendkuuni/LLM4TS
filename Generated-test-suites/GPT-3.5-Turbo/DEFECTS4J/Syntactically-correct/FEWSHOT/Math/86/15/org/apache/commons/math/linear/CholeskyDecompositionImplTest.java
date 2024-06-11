package org.apache.commons.math.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math.MatrixUtils;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

    @Test
    public void testGetL() {
        double[][] testData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        CholeskyDecomposition decomp = new CholeskyDecompositionImpl(matrix);

        double[][] expectedData = {{2, 0, 0}, {6, 1, 0}, {-8, 5, 3}};
        RealMatrix expected = MatrixUtils.createRealMatrix(expectedData);

        assertEquals(expected, decomp.getL());
    }

    @Test
    public void testGetLT() {
        double[][] testData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        CholeskyDecomposition decomp = new CholeskyDecompositionImpl(matrix);

        double[][] expectedData = {{2, 6, -8}, {0, 1, 5}, {0, 0, 3}};
        RealMatrix expected = MatrixUtils.createRealMatrix(expectedData);

        assertEquals(expected, decomp.getLT());
    }

    @Test
    public void testGetDeterminant() {
        double[][] testData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        CholeskyDecomposition decomp = new CholeskyDecompositionImpl(matrix);

        double expected = 576;
        assertEquals(expected, decomp.getDeterminant(), 1e-15);
    }

    @Test
    public void testGetSolver() {
        double[][] testData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        CholeskyDecomposition decomp = new CholeskyDecompositionImpl(matrix);

        DecompositionSolver solver = decomp.getSolver();
        assertArrayEquals(new double[]{2, -1, 3}, solver.solve(new double[]{2, -1, 3}), 1e-15);
    }
}