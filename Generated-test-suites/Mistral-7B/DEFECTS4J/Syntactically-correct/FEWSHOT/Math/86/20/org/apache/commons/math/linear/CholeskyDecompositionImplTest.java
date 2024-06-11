package org.apache.commons.math.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.MatrixUtils;

import java.util.Random;

public class CholeskyDecompositionImplTest {

    @Test
    public void testCholeskyDecompositionImpl() {
        // Test default constructor
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.2}, {0.1, 1.0, 0.3}, {0.2, 0.3, 1.0}});
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = cholesky.getL();
        RealMatrix lt = cholesky.getLT();
        RealMatrix expectedL = new Array2DRowRealMatrix(new double[][]{{Math.sqrt(1.0), 0.0, 0.0}, {0.0, Math.sqrt(1.0 + 0.1*0.1), 0.1}, {0.0, 0.1, Math.sqrt(1.0 + 0.2*0.2)}});
        RealMatrix expectedLT = MatrixUtils.createRealMatrix(expectedL.getData());
        assertEquals(expectedL, l);
        assertEquals(expectedLT, lt);

        // Test constructor with custom thresholds
        double relativeSymmetryThreshold = 1.0e-6;
        double absolutePositivityThreshold = 1.0e-8;
        RealMatrix matrix2 = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.2}, {0.1, 1.0 + 1.0e-10, 0.3}, {0.2, 0.3, 1.0}});
        CholeskyDecompositionImpl cholesky2 = new CholeskyDecompositionImpl(matrix2, relativeSymmetryThreshold, absolutePositivityThreshold);
        RealMatrix l2 = cholesky2.getL();
        RealMatrix lt2 = cholesky2.getLT();
        RealMatrix expectedL2 = new Array2DRowRealMatrix(new double[][]{{Math.sqrt(1.0), 0.0, 0.0}, {0.0, Math.sqrt(1.0 + 0.1*0.1), 0.1}, {0.0, 0.1, Math.sqrt(1.0 + 0.2*0.2)}});
        RealMatrix expectedLT2 = MatrixUtils.createRealMatrix(expectedL2.getData());
        assertEquals(expectedL2, l2, 1e-6);
        assertEquals(expectedLT2, lt2, 1e-6);

        // Test exceptions
        try {
            new CholeskyDecompositionImpl(MatrixUtils.createRealIdentityMatrix(3));
            fail("Expected NotSquareMatrixException");
        } catch (NotSquareMatrixException e) {
            // expected
        }

        try {
            new CholeskyDecompositionImpl(MatrixUtils.createRealDiagonalMatrix(3, new double[]{1.0, -1.0, 1.0}));
            fail("Expected NotPositiveDefiniteMatrixException");
        } catch (NotPositiveDefiniteMatrixException e) {
            // expected
        }

        try {
            new CholeskyDecompositionImpl(MatrixUtils.createRealDiagonalMatrix(3, new double[]{1.0, 1.0, 1.0}), 0.0, 0.0);
            fail("Expected NotSymmetricMatrixException");
        } catch (NotSymmetricMatrixException e) {
            // expected
        }

        // Test getDeterminant()
        double[][] determinantTestData = new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}};
        CholeskyDecompositionImpl choleskyDeterminant = new CholeskyDecompositionImpl(new Array2DRowRealMatrix(determinantTestData));
        double actualDeterminant = choleskyDeterminant.getDeterminant();
        double expectedDeterminant = 1.0;
        assertEquals(expectedDeterminant, actualDeterminant, 1e-12);

        // Test getSolver()
        double[] b = {1.0, 1.0, 1.0};
        RealVector x = cholesky.getSolver().solve(b);
        RealVector expectedX = new ArrayRealVector(new double[]{1.0, 1.0, 1.0});
        assertArrayEquals(expectedX.toArray(), x.toArray(), 1e-12);

        // Test getSolver() with RealVector
        RealVector realVector = new ArrayRealVector(b);
        RealVector result = cholesky.getSolver().solve(realVector);
        assertEquals(expectedX, result);

        // Test getSolver() with RealMatrix
        RealMatrix matrixB = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        RealMatrix result2 = cholesky.getSolver().solve(matrixB);
        RealMatrix expectedResult = new Array2DRowRealMatrix(new double[][]{{1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0)}, {-1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0)}});
        assertEquals(expectedResult, result2);

        // Test getSolver().isNonSingular()
        assertTrue(cholesky.getSolver().isNonSingular());

        // Test getSolver().solve(RealVector) with singular matrix
        double[][] singularData = new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 0.0}};
        CholeskyDecompositionImpl singularCholesky = new CholeskyDecompositionImpl(new Array2DRowRealMatrix(singularData));
        try {
            singularCholesky.getSolver().solve(new ArrayRealVector(new double[]{1.0, 1.0, 1.0}));
            fail("Expected InvalidMatrixException");
        } catch (InvalidMatrixException e) {
            // expected
        }
    }

    @Test
    public void testSolver() {
        // Test constructor
        double[][] data = new double[][]{{1.0, 0.1, 0.2}, {0.1, 1.0, 0.3}, {0.2, 0.3, 1.0}};
        Solver solver = new Solver(data);

        // Test isNonSingular()
        assertTrue(solver.isNonSingular());

        // Test solve(double[])
        double[] b = {1.0, 1.0, 1.0};
        double[] x = solver.solve(b);
        double[] expectedX = new double[]{1.0, 1.0, 1.0};
        assertArrayEquals(expectedX, x, 1e-12);

        // Test solve(RealVector)
        RealVector realVector = new ArrayRealVector(b);
        RealVector result = solver.solve(realVector);
        assertEquals(expectedX, result.toArray(), 1e-12);

        // Test solve(RealMatrix)
        RealMatrix matrixB = new Array2DRowRealMatrix(data);
        RealMatrix result2 = solver.solve(matrixB);
        RealMatrix expectedResult = MatrixUtils.createRealMatrix(expectedX);
        assertEquals(expectedResult, result2);

        // Test getInverse()
        RealMatrix inverse = solver.getInverse();
        RealMatrix expectedInverse = MatrixUtils.createRealMatrix(expectedX).transpose();
        assertEquals(expectedInverse, inverse);

        // Test solve(RealMatrix) with singular matrix
        double[][] singularData = new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 0.0}};
        Solver singularSolver = new Solver(singularData);
        try {
            singularSolver.solve(MatrixUtils.createRealIdentityMatrix(3));
            fail("Expected InvalidMatrixException");
        } catch (InvalidMatrixException e) {
            // expected
        }
    }

    @Test
    public void testSolver_Random() {
        int size = 10;
        Random random = new Random();
        double[][] data = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                data[i][j] = random.nextGaussian();
            }
        }
        RealMatrix randomMatrix = new Array2DRowRealMatrix(data);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(randomMatrix);
        Solver solver = cholesky.getSolver();

        double[] randomB = new double[size];
        for (int i = 0; i < size; i++) {
            randomB[i] = random.nextGaussian();
        }
        double[] x = solver.solve(randomB);
        RealVector expectedX = new ArrayRealVector(randomB);
        assertEquals(expectedX.toArray(), x, 1e-12);

        RealVector result = solver.solve(new ArrayRealVector(randomB));
        assertEquals(expectedX, result);

        RealMatrix matrixB = new Array2DRowRealMatrix(data);
        RealMatrix result2 = solver.solve(matrixB);
        RealMatrix expectedResult = MatrixUtils.createRealMatrix(x);
        assertEquals(expectedResult, result2);

        assertTrue(solver.isNonSingular());
    }

}