package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrixFactory;
import org.apache.commons.math3.linear.RealVectorFactory;
import org.apache.commons.math3.special.Epsilon;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImpl() throws Exception {
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(
                MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {2, 1}}));

        RealMatrix ut = svd.getUT();
        RealMatrix s = svd.getS();
        RealMatrix vt = svd.getVT();

        SingularValueDecomposition expectedSvd = new SingularValueDecomposition(
                MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {2, 1}}));

        RealMatrix expectedU = expectedSvd.getMatrixA();
        RealMatrix expectedV = expectedSvd.getMatrixV();

        RealMatrix actualU = ut;
        RealMatrix actualV = vt;

        RealMatrix expectedSingularValues = expectedSvd.getSingularValues();
        double[] actualSingularValues = svd.getSingularValues();

        assertEquals(expectedU, actualU, Epsilon.MACHEP);
        assertEquals(expectedSingularValues, new ArrayRealVector(actualSingularValues), Epsilon.MACHEP);
        assertEquals(expectedV, actualV, Epsilon.MACHEP);

        RealMatrix expectedInvU = expectedU.inverse();
        RealMatrix expectedInvUTranspose = expectedInvU.transpose();
        RealMatrix expectedInvUSvd = expectedInvUTranspose.multiply(expectedSvd.getMatrixV());
        RealMatrix expectedInvSvd = expectedSvd.getInverse();

        DecompositionSolver solver = svd.getSolver();

        RealVector b = RealVectorFactory.getInstance().createVector(new double[]{1, 1});
        RealVector expectedSolution = expectedInvSvd.operate(b);
        RealVector actualSolution = solver.solve(b);

        assertEquals(expectedSolution.toArray(), actualSolution.toArray(), Epsilon.MACHEP);

        RealMatrix x = MatrixUtils.createRealMatrix(new double[][]{{1, 1}, {1, 1}});
        RealMatrix expectedProduct = expectedInvUSvd.multiply(x);
        RealMatrix actualProduct = svd.getSolver().solve(x);

        assertEquals(expectedProduct, actualProduct, Epsilon.MACHEP);
    }

    @Test
    public void testSingularValueDecompositionImplWithLargerMatrix() throws Exception {
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(
                MatrixUtils.createRealMatrix(new double[][]{{1, 2, 3}, {2, 1, -1}, {3, -1, 1}}));

        RealMatrix ut = svd.getUT();
        RealMatrix s = svd.getS();
        RealMatrix vt = svd.getVT();

        SingularValueDecomposition expectedSvd = new SingularValueDecomposition(
                MatrixUtils.createRealMatrix(new double[][]{{1, 2, 3}, {2, 1, -1}, {3, -1, 1}}));

        RealMatrix expectedU = expectedSvd.getMatrixA();
        RealMatrix expectedV = expectedSvd.getMatrixV();

        RealMatrix actualU = ut;
        RealMatrix actualV = vt;

        RealMatrix expectedSingularValues = expectedSvd.getSingularValues();
        double[] actualSingularValues = svd.getSingularValues();

        assertEquals(expectedU, actualU, Epsilon.MACHEP);
        assertEquals(expectedSingularValues, new ArrayRealVector(actualSingularValues), Epsilon.MACHEP);
        assertEquals(expectedV, actualV, Epsilon.MACHEP);

        RealMatrix expectedInvU = expectedU.inverse();
        RealMatrix expectedInvUTranspose = expectedInvU.transpose();
        RealMatrix expectedInvUSvd = expectedInvUTranspose.multiply(expectedSvd.getMatrixV());
        RealMatrix expectedInvSvd = expectedSvd.getInverse();

        DecompositionSolver solver = svd.getSolver();

        RealVector b = RealVectorFactory.getInstance().createVector(new double[]{1, 1, 1});
        RealVector expectedSolution = expectedInvSvd.operate(b);
        RealVector actualSolution = solver.solve(b);

        assertEquals(expectedSolution.toArray(), actualSolution.toArray(), Epsilon.MACHEP);

        RealMatrix x = MatrixUtils.createRealMatrix(new double[][]{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}});
        RealMatrix expectedProduct = expectedInvUSvd.multiply(x);
        RealMatrix actualProduct = svd.getSolver().solve(x);

        assertEquals(expectedProduct, actualProduct, Epsilon.MACHEP);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSingularValueDecompositionImplWithInvalidMatrix() throws Exception {
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(
                RealMatrixFactory.getInstance().getIdentityMatrix(2));
    }

    @Test
    public void testRank() throws Exception {
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(
                MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {2, 1}}));
        assertEquals(2, svd.getRank());

        SingularValueDecompositionImpl svd2 = new SingularValueDecompositionImpl(
                MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {2, 1}, {0, 0}}));
        assertEquals(2, svd2.getRank());

        SingularValueDecompositionImpl svd3 = new SingularValueDecompositionImpl(
                MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {2, 1}, {0, 0}, {0, 0}}));
        assertEquals(2, svd3.getRank());

        SingularValueDecompositionImpl svd4 = new SingularValueDecompositionImpl(
                MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {2, 1}, {0, 0}, {0, 0}, {0, 0}}));
        assertEquals(2, svd4.getRank());
    }

    @Test
    public void testGetConditionNumber() throws Exception {
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(
                MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {2, 1}}));
        assertEquals(Math.max(1, 2), svd.getConditionNumber(), Epsilon.MACHEP);

        SingularValueDecompositionImpl svd2 = new SingularValueDecompositionImpl(
                MatrixUtils.createRealMatrix(new double[][]{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}}));
        assertEquals(Math.max(1, 2), svd2.getConditionNumber(), Epsilon.MACHEP);

        SingularValueDecompositionImpl svd3 = new SingularValueDecompositionImpl(
                MatrixUtils.createRealMatrix(new double[][]{{1, 0}, {0, 1}, {1, 0}}));
        assertEquals(Math.max(1 / Math.sqrt(2), Math.sqrt(2)), svd3.getConditionNumber(), Epsilon.MACHEP);

        SingularValueDecompositionImpl svd4 = new SingularValueDecompositionImpl(
                MatrixUtils.createRealMatrix(new double[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}}));
        assertEquals(Math.max(1, Math.sqrt(2)), svd4.getConditionNumber(), Epsilon.MACHEP);
    }

    @Test
    public void testCovariance() throws Exception {
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(
                MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {2, 1}}));
        RealMatrix covariance = svd.getCovariance(0.1);
        RealMatrix expectedCovariance = MatrixUtils.createRealMatrix(new double[][]{{1, 0}, {0, 1}});
        assertEquals(expectedCovariance, covariance, Epsilon.MACHEP);

        SingularValueDecompositionImpl svd2 = new SingularValueDecompositionImpl(
                MatrixUtils.createRealMatrix(new double[][]{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}}));
        RealMatrix covariance2 = svd2.getCovariance(0.5);
        RealMatrix expectedCovariance2 = MatrixUtils.createRealMatrix(new double[][]{{0.5, 0.5, 0.5}, {0.5, 0.5, 0.5}, {0.5, 0.5, 0.5}});
        assertEquals(expectedCovariance2, covariance2, Epsilon.MACHEP);
    }

    @Test
    public void testNorm() throws Exception {
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(
                MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {2, 1}}));
        assertEquals(Math.sqrt(5), svd.getNorm(), Epsilon.MACHEP);

        SingularValueDecompositionImpl svd2 = new SingularValueDecompositionImpl(
                MatrixUtils.createRealMatrix(new double[][]{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}}));
        assertEquals(Math.sqrt(3), svd2.getNorm(), Epsilon.MACHEP);
    }

    @Test
    public void testSolver() throws Exception {
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(
                MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {2, 1}}));

        RealVector b = RealVectorFactory.getInstance().createVector(new double[]{1, 1});
        RealVector expectedSolution = new ArrayRealVector(new double[]{1, -1});
        RealVector actualSolution = svd.getSolver().solve(b);

        assertEquals(expectedSolution, actualSolution, Epsilon.MACHEP);

        RealMatrix x = MatrixUtils.createRealMatrix(new double[][]{{1, 1}, {1, 1}});
        RealMatrix expectedProduct = new Array2DRowRealMatrix(new double[][]{1, 1}, new double[]{1, -1});
        RealMatrix actualProduct = svd.getSolver().solve(x);

        assertEquals(expectedProduct, actualProduct, Epsilon.MACHEP);

        SingularValueDecompositionImpl svd2 = new SingularValueDecompositionImpl(
                MatrixUtils.createRealMatrix(new double[][]{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}}));

        RealVector b2 = RealVectorFactory.getInstance().createVector(new double[]{1, 1, 1});
        RealVector expectedSolution2 = new ArrayRealVector(new double[]{0.3333333333333333, 0.5, 0.5});
        RealVector actualSolution2 = svd2.getSolver().solve(b2);

        assertEquals(expectedSolution2, actualSolution2, Epsilon.MACHEP);

        RealMatrix x2 = MatrixUtils.createRealMatrix(new double[][]{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}});
        RealMatrix expectedProduct2 = MatrixUtils.createRealMatrix(new double[][]{{0.3333333333333333, 0.5, 0.5},
                {0.3333333333333333, 0.5, 0.5}, {0.3333333333333333, 0.5, 0.5}});
        RealMatrix actualProduct2 = svd2.getSolver().solve(x2);

        assertEquals(expectedProduct2, actualProduct2, Epsilon.MACHEP);
    }
}