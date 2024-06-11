package org.apache.commons.math.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Precision;

import java.util.Random;

public class CholeskyDecompositionImplTest {

    @Test
    public void testCholeskyDecompositionImpl() throws Exception {
        // Test default constructor
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(3, 3, 1.0);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);

        // Test custom constructor
        double relativeSymmetryThreshold = 1.0e-8;
        double absolutePositivityThreshold = 1.0e-16;
        RealMatrix matrix2 = MatrixUtils.createRealSymmetricMatrix(3, 3, 1.0);
        CholeskyDecompositionImpl cholesky2 = new CholeskyDecompositionImpl(matrix2, relativeSymmetryThreshold, absolutePositivityThreshold);

        // Test exceptions
        try {
            CholeskyDecompositionImpl cholesky3 = new CholeskyDecompositionImpl(MatrixUtils.createRealDiagonalMatrix(3, 1.0, -1.0));
            fail();
        } catch (NotPositiveDefiniteMatrixException e) {
            // expected
        }

        try {
            CholeskyDecompositionImpl cholesky4 = new CholeskyDecompositionImpl(MatrixUtils.createRealDiagonalMatrix(3, 1.0, 0.0));
            fail();
        } catch (NotPositiveDefiniteMatrixException e) {
            // expected
        }

        try {
            CholeskyDecompositionImpl cholesky5 = new CholeskyDecompositionImpl(MatrixUtils.createRealSquareMatrix(3, 3, 1.0));
            fail();
        } catch (NonSquareMatrixException e) {
            // expected
        }

        try {
            CholeskyDecompositionImpl cholesky6 = new CholeskyDecompositionImpl(MatrixUtils.createRealSkewSymmetricMatrix(3, 3));
            fail();
        } catch (NotSymmetricMatrixException e) {
            // expected
        }

        // Test getL() and getLT()
        RealMatrix l = cholesky.getL();
        assertEquals(l.getRowDimension(), l.getColumnDimension());
        assertTrue(l.isSymmetric());
        RealMatrix lt = cholesky.getLT();
        assertEquals(lt.getRowDimension(), cholesky.getL().getColumnDimension());
        assertEquals(lt.getColumnDimension(), cholesky.getL().getRowDimension());
        assertTrue(lt.isSquare());

        // Test getDeterminant()
        double determinant = cholesky.getDeterminant();
        assertTrue(Precision.abs(determinant - 1.0) < 1.0e-12);

        // Test getSolver()
        DecompositionSolver solver = cholesky.getSolver();
        RealVector b = new RealVectorImpl(new double[]{1.0, 2.0, 3.0}, false);
        RealVector x = solver.solve(b);
        assertEquals(b.getDimension(), x.getDimension());
        for (int i = 0; i < b.getDimension(); i++) {
            assertEquals(i + 1, Precision.round(x.getEntry(i), 3));
        }

        // Test Cholesky decomposition of random matrices
        final int size = 5;
        RealMatrix randomMatrix = new RealMatrix(size, size);
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == j) {
                    randomMatrix.setEntry(i, j, random.nextGaussian() * 2.0 + 1.0);
                } else {
                    randomMatrix.setEntry(i, j, random.nextGaussian());
                }
            }
        }

        CholeskyDecompositionImpl choleskyRandom = new CholeskyDecompositionImpl(randomMatrix);
        RealMatrix lRandom = choleskyRandom.getL();
        assertEquals(lRandom.getRowDimension(), lRandom.getColumnDimension());
        assertTrue(lRandom.isSymmetric());
        RealMatrix ltRandom = choleskyRandom.getLT();
        assertEquals(ltRandom.getRowDimension(), choleskyRandom.getL().getColumnDimension());
        assertEquals(ltRandom.getColumnDimension(), choleskyRandom.getL().getRowDimension());
        assertTrue(ltRandom.isSquare());

        // Test Cholesky decomposition of a singular matrix
        RealMatrix singularMatrix = MatrixUtils.createRealSkewSymmetricMatrix(3, 3);
        try {
            new CholeskyDecompositionImpl(singularMatrix);
            fail();
        } catch (NotPositiveDefiniteMatrixException e) {
            // expected
        }

        // Test Cholesky decomposition of a non-symmetric matrix
        RealMatrix nonSymmetricMatrix = MatrixUtils.createRealDiagonalMatrix(3, new double[]{1.0, 2.0, 1.0});
        try {
            new CholeskyDecompositionImpl(nonSymmetricMatrix);
            fail();
        } catch (NotSymmetricMatrixException e) {
            // expected
        }

        // Test Cholesky decomposition of a non-square matrix
        RealMatrix nonSquareMatrix = MatrixUtils.createRealSquareMatrix(3, 3);
        nonSquareMatrix = MatrixUtils.createColumnPacket(nonSquareMatrix, new int[]{3, 3, 2});
        try {
            new CholeskyDecompositionImpl(nonSquareMatrix);
            fail();
        } catch (NonSquareMatrixException e) {
            // expected
        }

        // Test Cholesky decomposition of a matrix with small elements
        double epsilon = 1.0e-14;
        RealMatrix smallMatrix = MatrixUtils.createRealDiagonalMatrix(3, new double[]{epsilon, epsilon, epsilon});
        CholeskyDecompositionImpl choleskySmall = new CholeskyDecompositionImpl(smallMatrix, relativeSymmetryThreshold, absolutePositivityThreshold);
        RealMatrix lSmall = choleskySmall.getL();
        for (int i = 0; i < lSmall.getRowDimension(); i++) {
            for (int j = 0; j < lSmall.getColumnDimension(); j++) {
                assertTrue(Math.abs(lSmall.getEntry(i, j)) > epsilon);
            }
        }

        // Test Cholesky decomposition of a matrix with large elements
        double largeElement = 1.0e10;
        RealMatrix largeMatrix = MatrixUtils.createRealDiagonalMatrix(3, new double[]{largeElement, largeElement, largeElement});
        CholeskyDecompositionImpl choleskyLarge = new CholeskyDecompositionImpl(largeMatrix, relativeSymmetryThreshold, absolutePositivityThreshold);
        RealMatrix lLarge = choleskyLarge.getL();
        for (int i = 0; i < lLarge.getRowDimension(); i++) {
            for (int j = 0; j < lLarge.getColumnDimension(); j++) {
                assertTrue(Math.abs(lLarge.getEntry(i, j)) >= largeElement * (1.0 - relativeSymmetryThreshold));
            }
        }

        // Test Cholesky decomposition of a matrix with negative elements
        RealMatrix negativeMatrix = MatrixUtils.createRealDiagonalMatrix(3, new double[]{-1.0, -2.0, -3.0});
        try {
            new CholeskyDecompositionImpl(negativeMatrix);
            fail();
        } catch (NotPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testSolver() {
        // Test Cholesky decomposition and solving for a matrix with known solution
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(3, 3, 1.0);
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = cholesky.getL();

        RealVector b = new RealVectorImpl(new double[]{1.0, 2.0, 3.0}, false);
        RealVector x = cholesky.getSolver().solve(b);

        RealMatrix expectedX = MatrixUtils.createIdentityMatrix(3);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(Math.pow(i + 1, j + 1), x.getEntry(j * 3 + i), 1e-8);
            }
        }

        // Test Cholesky decomposition and solving for a random matrix
        Random random = new Random();
        int size = 5;
        RealMatrix randomMatrix = new RealMatrix(size, size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                randomMatrix.setEntry(i, j, random.nextGaussian());
            }
        }

        CholeskyDecompositionImpl choleskyRandom = new CholeskyDecompositionImpl(randomMatrix);
        RealMatrix lRandom = choleskyRandom.getL();

        RealVector bRandom = new RealVectorImpl(MathArrays.fill(new double[size * size], random.nextGaussian()), false);
        RealVector xRandom = choleskyRandom.getSolver().solve(bRandom);

        RealMatrix expectedXRandom = new RealMatrix(size, size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                expectedXRandom.setEntry(i, j, Math.pow(i + 1, j + 1) * Math.pow(2.0, random.nextGaussian()));
            }
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                assertEquals(expectedXRandom.getEntry(i, j), xRandom.getEntry(j * size + i), 1e-6);
            }
        }
    }
}