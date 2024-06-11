package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.EigenDecompositionImpl;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SingularValueDecompositionImpl;
import org.apache.commons.math3.linear.TridiagonalMatrix;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

/**
 * Test cases for {@link SingularValueDecompositionImpl}.
 */
@RunWith(Theories.class)
public class SingularValueDecompositionImplTest {

    /**
     * Data provider for testSingularValueDecomposition method.
     * @return test cases
     */
    @DataPoints
    public static Object[][] dataForTestSingularValueDecomposition() {
        return new Object[][] {
            // Test case 1: Identity matrix
            new Object[] { new Array2DRowRealMatrix(new double[][] { {1, 0}, {0, 1}}) },
            // Test case 2: Diagonal matrix
            new Object[] { new Array2DRowRealMatrix(new double[][] { {1, 0}, {0, 2}}) },
            // Test case 3: Non-square matrix
            new Object[] { new Array2DRowRealMatrix(new double[][] { {1, 1}, {0, 1}}) },
            // Test case 4: Singular matrix (rank < min(rows, columns))
            new Object[] { new Array2DRowRealMatrix(new double[][] { {1, 1, 0}, {1, 1, 1}}) }
        };
    }

    /**
     * Test SingularValueDecompositionImpl construction with valid input.
     * @param matrix Input matrix
     */
    @Test
    public void testSingularValueDecomposition(RealMatrix matrix) {
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            RealMatrix u = svd.getU();
            RealMatrix vt = svd.getVT();
            RealMatrix s = svd.getS();
            RealVector singularValues = svd.getSingularValues();
            RealMatrix expectedU = matrix.getMatrixNorm(0.001).getInverse();
            RealMatrix expectedVt = expectedU.transpose();
            RealMatrix expectedS = MatrixUtils.createRealDiagonalMatrix(singularValues.toVector());
            assertEquals("U is not the expected U", expectedU, u);
            assertEquals("VT is not the expected VT", expectedVt, vt);
            assertEquals("S is not the expected S", expectedS, s);
        } catch (InvalidMatrixException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test SingularValueDecompositionImpl construction with an invalid input.
     */
    @Test
    public void testSingularValueDecompositionInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> new SingularValueDecompositionImpl(null));
        assertThrows(IllegalArgumentException.class, () -> new SingularValueDecompositionImpl(new Array2DRowRealMatrix(new double[][] { {1, 1}, {1, 1}, {1, 1}})));
    }

    /**
     * Test getRank method.
     * @param matrix Input matrix
     */
    @Test
    public void testGetRank(RealMatrix matrix) {
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            int expectedRank = matrix.getRowDimension() < matrix.getColumnDimension() ? matrix.getRowDimension() : matrix.getColumnDimension();
            int rank = svd.getRank();
            assertTrue("Rank is not the expected rank: " + rank, rank == expectedRank);
        } catch (InvalidMatrixException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test getNorm method.
     * @param matrix Input matrix
     */
    @Test
    public void testGetNorm(RealMatrix matrix) {
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            double expectedNorm = matrix.getSingularValues()[0];
            double norm = svd.getNorm();
            assertEquals("Norm is not the expected norm: " + norm, expectedNorm, norm);
        } catch (InvalidMatrixException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test getConditionNumber method.
     * @param matrix Input matrix
     */
    @Test
    public void testGetConditionNumber(RealMatrix matrix) {
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            double expectedConditionNumber = expectedSingularValue(matrix) / expectedSingularValue(matrix.getMatrixNorm(0.001));
            double conditionNumber = svd.getConditionNumber();
            assertEquals("Condition number is not the expected condition number: " + conditionNumber, expectedConditionNumber, conditionNumber);
        } catch (InvalidMatrixException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test getSingularValues method.
     * @param matrix Input matrix
     */
    @Test
    public void testGetSingularValues(RealMatrix matrix) {
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            double[] singularValues = svd.getSingularValues();
            assertEquals("Number of singular values is not the expected number",
                         matrix.getRowDimension() < matrix.getColumnDimension() ? matrix.getRowDimension() : matrix.getColumnDimension(),
                         singularValues.length);
            double expectedSingularValue = matrix.getSingularValues()[0];
            assertEquals("Singular values are not the expected singular values", expectedSingularValue, singularValues[0]);
        } catch (InvalidMatrixException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test getCovariance method.
     * @param matrix Input matrix
     */
    @Test
    public void testGetCovariance(RealMatrix matrix) {
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            double minSingularValue = Math.min(matrix.getRowDimension(), matrix.getColumnDimension()) * 1e-6;
            RealMatrix covariance = svd.getCovariance(minSingularValue);
            assertTrue("Covariance matrix is not symmetric", covariance.isSymmetric());
            RealVector singularValues = svd.getSingularValues();
            double expectedRank = matrix.getRowDimension() < matrix.getColumnDimension() ? matrix.getRowDimension() : matrix.getColumnDimension();
            int rank = svd.getRank();
            for (int i = 0; i < rank; ++i) {
                for (int j = 0; j <= i; ++j) {
                    double expectedValue = singularValues.getEntry(i) * singularValues.getEntry(j);
                    double value = covariance.getEntry(i, j);
                    assertEquals("Covariance is not the expected covariance", expectedValue, value);
                }
            }
            for (int i = rank; i < singularValues.size(); ++i) {
                for (int j = 0; j <= i; ++j) {
                    double value = covariance.getEntry(i, j);
                    assertEquals("Covariance is not zero for non-significant singular values", 0.0, value);
                }
            }
        } catch (InvalidMatrixException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test solve method with valid input.
     * @param matrix Input matrix
     * @param b Right-hand side of the equation
     */
    @Test
    public void testSolve(RealMatrix matrix, RealVector b) {
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            RealVector x = svd.getSolver().solve(b);
            RealVector expectedX = MatrixUtils.createColumnVector(new double[] { 1, 1 });
            assertEquals("X is not the expected X", expectedX, x);
        } catch (InvalidMatrixException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test solve method with an invalid input.
     * @param matrix Input matrix
     */
    @Test
    public void testSolveInvalidInput(RealMatrix matrix) {
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            RealVector b = new ArrayRealVector(new double[] { 1, 2, 3 });
            DecompositionSolver solver = svd.getSolver();
            solver.solve(b);
            assertFalse("Solver should throw an exception with this input", true);
        } catch (IllegalArgumentException e) {
            // Expected
        } catch (InvalidMatrixException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test solve method with a non-square matrix.
     * @param matrix Input matrix
     * @param b Right-hand side of the equation
     */
    @Test
    public void testSolveNonSquare(RealMatrix matrix, RealVector b) {
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            RealVector x = svd.getSolver().solve(b);
            RealVector expectedX = new ArrayRealVector(new double[] { 1 / (matrix.getSingularValues()[0]), 1 / (matrix.getSingularValues()[1]) });
            assertEquals("X is not the expected X", expectedX, x);
        } catch (InvalidMatrixException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test isNonSingular method.
     * @param matrix Input matrix
     */
    @Test
    public void testIsNonSingular(RealMatrix matrix) {
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            boolean isNonSingular = svd.getSolver().isNonSingular();
            assertTrue("Matrix is not non-singular", isNonSingular);
        } catch (InvalidMatrixException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test getInverse method with a square and non-singular matrix.
     * @param matrix Input matrix
     */
    @Test
    public void testGetInverseSquareAndNonSingular(RealMatrix matrix) {
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            RealMatrix inverse = svd.getSolver().getInverse();
            RealMatrix expectedInverse = matrix.getInverse();
            assertEquals("Inverse is not the expected inverse", expectedInverse, inverse);
        } catch (InvalidMatrixException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test getInverse method with a singular matrix.
     * @param matrix Input matrix
     */
    @Test
    public void testGetInverseSingular(RealMatrix matrix) {
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            RealMatrix inverse = svd.getSolver().getInverse();
            assertNull("Inverse should be null for a singular matrix", inverse);
        } catch (InvalidMatrixException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test getInverse method with a non-square matrix.
     * @param matrix Input matrix
     */
    @Test
    public void testGetInverseNonSquare(RealMatrix matrix) {
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            RealMatrix inverse = svd.getSolver().getInverse();
            assertNull("Inverse should be null for a non-square matrix", inverse);
        } catch (InvalidMatrixException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test construct EigenDecomposition from TridiagonalMatrix.
     * @param matrix Input matrix
     */
    @Test
    public void testEigenDecompositionFromTridiagonalMatrix(RealMatrix matrix) {
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            EigenDecomposition eigenDecomposition = svd.eigenDecomposition;
            EigenDecompositionImpl expectedEigenDecomposition = new EigenDecompositionImpl(eigenDecomposition.getData());
            assertEquals("EigenDecomposition is not the expected eigenDecomposition", expectedEigenDecomposition, eigenDecomposition);
        } catch (InvalidMatrixException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the largest singular value of a matrix.
     * @param matrix Input matrix
     * @return the largest singular value of the matrix
     */
    private double expectedSingularValue(RealMatrix matrix) {
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            double[] singularValues = svd.getSingularValues();
            return singularValues[0];
        } catch (InvalidMatrixException e) {
            throw new RuntimeException(e);
        }
    }
}