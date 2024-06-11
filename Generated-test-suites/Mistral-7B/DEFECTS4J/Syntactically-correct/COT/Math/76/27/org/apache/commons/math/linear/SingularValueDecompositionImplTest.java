package org.apache.commons.math.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.EigenvalueDecomposition;
import org.apache.commons.math3.linear.FieldMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImplConstructor() {
        RealMatrix matrix = new FieldMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
            assertTrue(svd.getRank() >= 2);
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSingularValueDecompositionImplInvalidInputMatrix() {
        RealMatrix matrix = new FieldMatrix(new double[][]{{1.0, 2.0}, {3.0, 0.0}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
            fail("Expected InvalidMatrixException");
        } catch (InvalidMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testGetU() throws InvalidMatrixException {
        RealMatrix matrix = new FieldMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
        RealMatrix u = svd.getU();
        assertTrue(u.getRowDimension() == 2 && u.getColumnDimension() == 2);
    }

    @Test
    public void testGetUT() throws InvalidMatrixException {
        RealMatrix matrix = new FieldMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
        RealMatrix ut = svd.getUT();
        assertTrue(ut.getRowDimension() == 2 && ut.getColumnDimension() == 2);
        assertEquals(u.transpose(), ut);
    }

    @Test
    public void testGetS() throws InvalidMatrixException {
        RealMatrix matrix = new FieldMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
        RealMatrix s = svd.getS();
        assertTrue(s.getRowDimension() == 2 && s.getColumnDimension() == 1);
        assertTrue(s.getEntry(0, 0) > 0 && s.getEntry(1, 0) > 0);
    }

    @Test
    public void testGetSingularValues() throws InvalidMatrixException {
        RealMatrix matrix = new FieldMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
        double[] singularValues = svd.getSingularValues();
        assertTrue(singularValues.length == 2);
        assertTrue(singularValues[0] > 0 && singularValues[1] > 0);
    }

    @Test
    public void testGetV() throws InvalidMatrixException {
        RealMatrix matrix = new FieldMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
        RealMatrix v = svd.getV();
        assertTrue(v.getRowDimension() == 2 && v.getColumnDimension() == 2);
    }

    @Test
    public void testGetVT() throws InvalidMatrixException {
        RealMatrix matrix = new FieldMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
        RealMatrix vt = svd.getVT();
        assertTrue(vt.getRowDimension() == 2 && vt.getColumnDimension() == 2);
        assertEquals(v.transpose(), vt);
    }

    @Test
    public void testGetCovariance() throws InvalidMatrixException {
        RealMatrix matrix = new FieldMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
        double minSingularValue = 1.0;
        RealMatrix covariance = svd.getCovariance(minSingularValue);
        assertTrue(covariance.getRowDimension() == 2 && covariance.getColumnDimension() == 2);
    }

    @Test
    public void testGetNorm() throws InvalidMatrixException {
        RealMatrix matrix = new FieldMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
        double expectedNorm = svd.getSingularValues()[0];
        double norm = svd.getNorm();
        assertEquals(expectedNorm, norm, 1e-12);
    }

    @Test
    public void testGetConditionNumber() throws InvalidMatrixException {
        RealMatrix matrix = new FieldMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
        double expectedConditionNumber = svd.getSingularValues()[0] / svd.getSingularValues()[1];
        double conditionNumber = svd.getConditionNumber();
        assertEquals(expectedConditionNumber, conditionNumber, 1e-12);
    }

    @Test
    public void testGetRank() throws InvalidMatrixException {
        RealMatrix matrix = new FieldMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
        int expectedRank = 2;
        int rank = svd.getRank();
        assertEquals(expectedRank, rank);
    }

    @Test
    public void testGetSolver() throws InvalidMatrixException {
        RealMatrix matrix = new FieldMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
        DecompositionSolver solver = svd.getSolver();
        assertTrue(solver instanceof SingularValueDecompositionImpl.Solver);
    }

    @Test
    public void testSolverSimple() {
        RealMatrix matrix = new FieldMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
        DecompositionSolver solver = svd.getSolver();
        RealVector b = new ArrayRealVector(new double[]{2.0, 3.0});
        RealVector x = solver.solve(b);
        double tolerance = 1e-9;
        assertEquals(new ArrayRealVector(new double[]{-0.53846037, 0.84792349}), x, tolerance);
    }

    @Test
    public void testSolverComplex() {
        RealMatrix matrix = new FieldMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 3);
        DecompositionSolver solver = svd.getSolver();
        RealVector b = new ArrayRealVector(new double[]{2.0, 3.0, 4.0});
        RealVector x = solver.solve(b);
        double tolerance = 1e-9;
        assertEquals(new ArrayRealVector(new double[]{-0.28830127, 0.35259317, 0.81595503}), x, tolerance);
    }

    @Test
    public void testIsNonSingular() throws InvalidMatrixException {
        RealMatrix matrix = new FieldMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
        assertTrue(svd.getSolver().isNonSingular());
    }

    @Test
    public void testGetInverse() throws InvalidMatrixException {
        RealMatrix matrix = new FieldMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
        RealMatrix inverse = svd.getSolver().getInverse();
        assertTrue(inverse.getRowDimension() == 2 && inverse.getColumnDimension() == 2);
        RealMatrix product = matrix.multiply(inverse);
        assertTrue(product.isIdentityMatrix());
    }

    @Test
    public void testConstructEigenDecomposition() {
        double[][] bidiagonalMatrix = {{1.0, 2.0}, {3.0, 5.0}, {5.0, 7.0}};
        double[] eigenvalues = {4.0, 2.0, 1.0};
        EigenvalueDecomposition eigenvalueDecomposition = new EigenvalueDecompositionImpl(
                bidiagonalMatrix, eigenvalues);
        EigenDecomposition eigenDecomposition = new EigenDecompositionImpl(
                eigenvalueDecomposition.getEigenvectors(), eigenvalueDecomposition.getRealEigenvalues());
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(
                new Array2DRowRealMatrix(bidiagonalMatrix), eigenDecomposition.getEigenvectors().getColumnDimension());
        assertEquals(svd.getEigenDecomposition(), eigenDecomposition);
    }

}