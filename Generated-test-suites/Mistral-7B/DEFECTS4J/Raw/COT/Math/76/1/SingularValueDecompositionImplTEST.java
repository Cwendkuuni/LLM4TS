package org.apache.commons.math.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.EigenDecompositionImpl;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SingularValueDecompositionImpl;
import org.apache.commons.math3.linear.BiDiagonalTransformer;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.special.Epsilon;
import org.apache.commons.math3.util.MathUtils;

import org.junit.Test;

/**
 * SingularValueDecompositionImplTest class for testing SingularValueDecompositionImpl class.
 */
public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImpl() {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            RealMatrix U = svd.getU();
            RealMatrix S = svd.getS();
            RealMatrix V = svd.getV();
            RealVector singularValues = svd.getSingularValues();
            assertEquals(2, U.getRowDimension());
            assertEquals(2, U.getColumnDimension());
            assertEquals(2, S.getRowDimension());
            assertEquals(2, S.getColumnDimension());
            assertEquals(2, V.getRowDimension());
            assertEquals(2, V.getColumnDimension());
            assertEquals(2, singularValues.getDimension());
            assertArrayEquals(new double[]{Math.sqrt(5.0), Math.sqrt(3.0)}, singularValues.toArray(), 0.0);
            RealMatrix productUSV = U.multiply(S).multiply(V.transpose());
            assertTrue(productUSV.equals(matrix, Epsilon.getMachineEpsilon() * 10));
            RealMatrix productVtUS = V.transpose().multiply(U);
            assertTrue(productVtUS.equals(MatrixUtils.createIdentityMatrix(2), Epsilon.getMachineEpsilon() * 10));
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSingularValueDecompositionImplWithIdentityMatrix() {
        RealMatrix matrix = RealMatrixUtils.createIdentityMatrix(3);
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            RealMatrix U = svd.getU();
            RealMatrix S = svd.getS();
            RealMatrix V = svd.getV();
            RealVector singularValues = svd.getSingularValues();
            assertEquals(3, U.getRowDimension());
            assertEquals(3, U.getColumnDimension());
            assertEquals(3, S.getRowDimension());
            assertEquals(3, S.getColumnDimension());
            assertEquals(3, V.getRowDimension());
            assertEquals(3, V.getColumnDimension());
            assertEquals(3, singularValues.getDimension());
            assertArrayEquals(singularValues.toArray(), new double[]{1.0, 1.0, 1.0}, 0.0);
            RealMatrix productUSV = U.multiply(S).multiply(V.transpose());
            assertTrue(productUSV.equals(matrix, Epsilon.getMachineEpsilon() * 10));
            RealMatrix productVtUS = V.transpose().multiply(U);
            assertTrue(productVtUS.equals(MatrixUtils.createIdentityMatrix(3), Epsilon.getMachineEpsilon() * 10));
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSingularValueDecompositionImplWithEmptyMatrix() {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            fail("Unexpected exception not thrown.");
        } catch (InvalidMatrixException e) {
            assertTrue(e.getMessage().contains("empty matrix"));
        }
    }

    @Test
    public void testSingularValueDecompositionImplWithSquareSingularMatrix() {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            RealMatrix U = svd.getU();
            RealMatrix S = svd.getS();
            RealMatrix V = svd.getV();
            RealVector singularValues = svd.getSingularValues();
            assertEquals(2, U.getRowDimension());
            assertEquals(2, U.getColumnDimension());
            assertEquals(2, S.getRowDimension());
            assertEquals(2, S.getColumnDimension());
            assertEquals(2, V.getRowDimension());
            assertEquals(2, V.getColumnDimension());
            assertEquals(2, singularValues.getDimension());
            assertEquals(0.0, singularValues.getEntry(0), 0.0);
            assertEquals(0.0, singularValues.getEntry(1), 0.0);
            RealMatrix productUSV = U.multiply(S).multiply(V.transpose());
            assertTrue(productUSV.equals(matrix, Epsilon.getMachineEpsilon() * 10));
            RealMatrix productVtUS = V.transpose().multiply(U);
            assertTrue(productVtUS.equals(MatrixUtils.createIdentityMatrix(2), Epsilon.getMachineEpsilon() * 10));
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSingularValueDecompositionImplWithRowNorColumnVector() {
        int row = 3;
        int column = 2;
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(row, column);
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            fail("Unexpected exception not thrown.");
        } catch (InvalidMatrixException e) {
            assertTrue(e.getMessage().contains("row dimension") ||
                       e.getMessage().contains("column dimension"));
        }
    }

    @Test
    public void testSingularValueDecompositionImplWithRectangularMatrix() {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            fail("Unexpected exception not thrown.");
        } catch (InvalidMatrixException e) {
            assertTrue(e.getMessage().contains("singular value decomposition is not supported for rectangular matrices"));
        }
    }

    @Test
    public void testSingularValueDecompositionImplWithSingularMatrix() {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{0.0, 0.0}, {0.0, 0.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            fail("Unexpected exception not thrown.");
        } catch (InvalidMatrixException e) {
            assertTrue(e.getMessage().contains("singular matrix"));
        }
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            assertEquals(2, svd.getRank());
            RealMatrix singularValueDecompositionMatrix = new SingularValueDecomposition(matrix).getMatrix();
            assertEquals(2, singularValueDecompositionMatrix.getRank());
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetSolver() {
        RealMatrix matrix = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            DecompositionSolver solver = svd.getSolver();
            assertTrue(solver instanceof SingularValueDecompositionImpl.Solver);
            RealVector b = new ArrayRealVector(new double[]{1.0, 2.0});
            RealVector x = solver.solve(b);
            double[] xData = x.getData();
            assertEquals(2, x.getDimension());
            assertEquals(1.0 / Math.sqrt(5.0), xData[0], 0.0);
            assertEquals(2.0 / Math.sqrt(5.0), xData[1], 0.0);
            RealVector bExpected = new ArrayRealVector(new double[]{1.5, 2.5});
            RealVector xExpected = solver.solve(bExpected);
            double[] xExpectedData = xExpected.getData();
            assertEquals(2, xExpected.getDimension());
            assertEquals(1.5 / Math.sqrt(5.0), xExpectedData[0], 0.0);
            assertEquals(2.5 / Math.sqrt(5.0), xExpectedData[1], 0.0);
            RealMatrix B = RealMatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
            RealMatrix X = solver.solve(B);
            assertArrayEquals(new double[][]{
                    {1.0 / Math.sqrt(5.0) / Math.sqrt(3.0 + 4.0 * Math.sqrt(5.0)), 2.0 / Math.sqrt(5.0) / Math.sqrt(3.0 + 4.0 * Math.sqrt(5.0))},
                    {-3.0 / Math.sqrt(5.0) / Math.sqrt(3.0 + 4.0 * Math.sqrt(5.0)), 4.0 / Math.sqrt(5.0) / Math.sqrt(3.0 + 4.0 * Math.sqrt(5.0))}},
                    X.getData(), 0.0);
            assertTrue(solver.isNonSingular());
            RealMatrix pseudoInverse = solver.getInverse();
            RealMatrix A = RealMatrixUtils.createRealMatrix(new double[][]{{1.0}, {2.0}});
            RealMatrix X_Expected = RealMatrixUtils.createRealMatrix(new double[][]{{1.0 / Math.sqrt(5.0)}, {2.0 / Math.sqrt(5.0)}});
            assertEquals(1, pseudoInverse.getRowDimension());
            assertEquals(2, pseudoInverse.getColumnDimension());
            assertTrue(X_Expected.equals(pseudoInverse.multiply(A), Epsilon.getMachineEpsilon() * 10));
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}