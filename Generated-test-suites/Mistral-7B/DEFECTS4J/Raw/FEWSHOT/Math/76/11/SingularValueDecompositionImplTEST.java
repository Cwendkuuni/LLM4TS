package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrixFactory;
import org.apache.commons.math3.linear.RealVectorFactory;
import org.apache.commons.math3.exception.SingularValueException;
import org.apache.commons.math3.exception.DecompositionFailedException;
import org.apache.commons.math3.exception.InvalidMatrixException;
import org.apache.commons.math3.exception.MathRuntimeException;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecomposition() throws InvalidMatrixException, SingularValueException, DecompositionFailedException {
        RealMatrix matrix = RealMatrixFactory.symmetric(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix U = svd.getU();
        RealMatrix S = svd.getS();
        RealMatrix V = svd.getV();
        RealMatrix expectedU = MatrixUtils.createRealMatrix(new double[][]{{0.342020143538012, -0.923553353543868}, {-0.923553353543868, 0.342020143538012}});
        RealMatrix expectedS = MatrixUtils.createRealDiagonalMatrix(new double[]{2.44948974278317, 0.0});
        RealMatrix expectedV = MatrixUtils.createRealMatrix(new double[][]{{-0.759552123151619, -0.488602512363746}, {0.488602512363746, 0.759552123151619}});
        assertEquals(expectedU, U);
        assertEquals(expectedS, S);
        assertEquals(expectedV, V.transpose());
    }

    @Test
    public void testSingularValueDecompositionWithDifferentDimensions() throws InvalidMatrixException, SingularValueException, DecompositionFailedException {
        RealMatrix matrix = RealMatrixFactory.identity(3);
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        assertEquals(3, svd.getRank());
    }

    @Test
    public void testSingularValueDecompositionWithIdentityMatrix() throws InvalidMatrixException, SingularValueException, DecompositionFailedException {
        RealMatrix matrix = RealMatrixFactory.identity(3);
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix U = svd.getU();
        RealMatrix S = svd.getS();
        RealMatrix V = svd.getV();
        RealMatrix expectedU = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        RealMatrix expectedS = MatrixUtils.createRealDiagonalMatrix(new double[]{1.0, 1.0, 1.0});
        RealMatrix expectedV = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
        assertEquals(expectedU, U);
        assertEquals(expectedS, S);
        assertEquals(expectedV, V.transpose());
    }

    @Test
    public void testSingularValueDecompositionWithSymmetricMatrix() throws InvalidMatrixException, SingularValueException, DecompositionFailedException {
        RealMatrix matrix = RealMatrixFactory.symmetric(new double[][]{{2.0, 1.0}, {1.0, 2.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix U = svd.getU();
        RealMatrix S = svd.getS();
        RealMatrix V = svd.getV();
        RealMatrix expectedU = MatrixUtils.createRealMatrix(new double[][]{{0.666666681556292, 0.666666681556292}, {-0.666666681556292, 0.666666681556292}});
        RealMatrix expectedS = MatrixUtils.createRealDiagonalMatrix(new double[]{2.44948974278317, 0.0});
        RealMatrix expectedV = MatrixUtils.createRealMatrix(new double[][]{{-0.759552123151619, -0.488602512363746}, {0.488602512363746, 0.759552123151619}});
        assertEquals(expectedU, U);
        assertEquals(expectedS, S);
        assertEquals(expectedV, V.transpose());
    }

    @Test
    public void testSolver() throws InvalidMatrixException, SingularValueException, DecompositionFailedException {
        RealMatrix matrix = RealMatrixFactory.symmetric(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = svd.getSolver();
        RealVector b = RealVectorFactory.createDenseVector(new double[]{1.0, 2.0});
        RealVector x = solver.solve(b);
        RealVector expectedX = RealVectorFactory.createDenseVector(new double[]{2.16364636422544, -1.09813011130317});
        assertEquals(expectedX, x);
    }

    @Test
    public void testSolverWithIdentityMatrix() throws InvalidMatrixException, SingularValueException, DecompositionFailedException {
        RealMatrix matrix = RealMatrixFactory.identity(3);
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = svd.getSolver();
        RealVector b = RealVectorFactory.createDenseVector(new double[]{1.0, 0.0, 0.0});
        RealVector x = solver.solve(b);
        RealVector expectedX = b;
        assertEquals(expectedX, x);
    }

    @Test(expected = MathRuntimeException.class)
    public void testSolverWithNonSquareMatrix() throws InvalidMatrixException, SingularValueException, DecompositionFailedException {
        RealMatrix matrix = RealMatrixFactory.identity(3).addColumn(RealMatrixFactory.createColumnVector(new double[]{1.0}));
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = svd.getSolver();
        RealVector b = RealVectorFactory.createDenseVector(new double[]{1.0, 0.0, 0.0});
        solver.solve(b);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolverWithInvalidShapeMatrix() throws InvalidMatrixException, SingularValueException, DecompositionFailedException {
        RealMatrix matrix = RealMatrixFactory.createColumnVector(new double[]{1.0});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = svd.getSolver();
        RealVector b = RealVectorFactory.createDenseVector(new double[]{1.0, 0.0});
        solver.solve(b);
    }

    @Test
    public void testGetConditionNumber() throws InvalidMatrixException, SingularValueException, DecompositionFailedException {
        RealMatrix matrix = RealMatrixFactory.symmetric(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double conditionNumber = svd.getConditionNumber();
        assertEquals(3.0 / 2.0, conditionNumber, 0.001);
    }

    @Test
    public void testGetRank() throws InvalidMatrixException, SingularValueException, DecompositionFailedException {
        RealMatrix matrix = RealMatrixFactory.symmetric(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        int rank = svd.getRank();
        assertEquals(2, rank);
    }

    @Test
    public void testGetNorm() throws InvalidMatrixException, SingularValueException, DecompositionFailedException {
        RealMatrix matrix = RealMatrixFactory.symmetric(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double norm = svd.getNorm();
        assertEquals(Math.sqrt(15.0), norm, 0.001);
    }

    @Test
    public void testGetCovariance() throws InvalidMatrixException, SingularValueException, DecompositionFailedException {
        RealMatrix matrix = RealMatrixFactory.symmetric(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double minSingularValue = 1.0;
        RealMatrix covariance = svd.getCovariance(minSingularValue);
        RealMatrix expectedCovariance = new Array2DRowRealMatrix(new double[][]{{0.101256531489026, 0.0}, {0.0, 0.186528062978052}});
        assertEquals(expectedCovariance, covariance);
    }

    @Test
    public void testGetCovarianceWithIdentityMatrix() throws InvalidMatrixException, SingularValueException, DecompositionFailedException {
        RealMatrix matrix = RealMatrixFactory.identity(3);
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double minSingularValue = 1.0;
        RealMatrix covariance = svd.getCovariance(minSingularValue);
        RealMatrix expectedCovariance = RealMatrixFactory.identity(3);
        assertEquals(expectedCovariance, covariance);
    }

    @Test
    public void testGetCovarianceWithNonSquareMatrix() throws InvalidMatrixException, SingularValueException, DecompositionFailedException {
        RealMatrix matrix = RealMatrixFactory.identity(3).addColumn(RealMatrixFactory.createColumnVector(new double[]{1.0}));
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double minSingularValue = 1.0;
        try {
            svd.getCovariance(minSingularValue);
            fail("Exception expected");
        } catch (IllegalStateException e) {
            // Expected
        }
    }
}