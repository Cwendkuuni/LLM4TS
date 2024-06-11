package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.special.MathUtils;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImpl() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            RealMatrix u = svd.getU();
            RealMatrix s = svd.getS();
            RealMatrix v = svd.getV();
            RealMatrix ut = svd.getUT();
            RealMatrix vt = svd.getVT();
            RealMatrix expectedUT = new Array2DRowRealMatrix(new double[][]{{0.573578854, -0.573578854},
                                                                                {0.816496581, 0.573578854}});
            RealMatrix expectedVT = new Array2DRowRealMatrix(new double[][]{{-0.573578854, 0.816496581},
                                                                               {0.573578854, -0.816496581}});
            RealMatrix expectedU = u.multiply(s.multiply(v));
            RealMatrix expectedS = MatrixUtils.createRealDiagonalMatrix(new double[]{1.414213562, 0});
            RealMatrix expectedV = vt.transpose();

            assertEquals(expectedU, matrix);
            assertEquals(expectedS, s);
            assertEquals(expectedV, v);
            assertEquals(expectedUT, ut);
            assertEquals(expectedVT, vt);
        } catch (InvalidMatrixException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testSingularValueDecompositionImplWithSingularValuesLessThanZero() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, -1}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            fail("InvalidMatrixException should be thrown");
        } catch (InvalidMatrixException e) {
            //expected
        }
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
        SingularValueDecompositionImpl svd;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
            int rank = svd.getRank();
            assertEquals(3, rank);
        } catch (InvalidMatrixException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testGetCovariance() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}});
        SingularValueDecompositionImpl svd;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
            RealMatrix covariance = svd.getCovariance(0.5);
            RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.573578854, -0.573578854},
                                                                               {-0.573578854, 0.573578854}});
            assertEquals(expected, covariance);
        } catch (InvalidMatrixException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testGetNorm() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}});
        SingularValueDecompositionImpl svd;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
            double norm = svd.getNorm();
            assertEquals(1.414213562, norm, 0.00001);
        } catch (InvalidMatrixException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testGetConditionNumber() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}});
        SingularValueDecompositionImpl svd;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
            double conditionNumber = svd.getConditionNumber();
            assertEquals(1.414213562 / 0, conditionNumber, 0.00001);
        } catch (InvalidMatrixException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testSolver() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3},
                                                                       {4, 5, 6},
                                                                       {7, 8, 9}});
        SingularValueDecompositionImpl svd;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
            DecompositionSolver solver = svd.getSolver();
            double[] b = {1, 2, 3};
            double[] x = solver.solve(b);
            RealVector expected = new ArrayRealVector(new double[]{1.110223025, 1.421085476, 1.353553391});
            assertArrayEquals(expected.toArray(), x, 0.00001);
        } catch (InvalidMatrixException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testSolverWithNonSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}});
        SingularValueDecompositionImpl svd;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
            DecompositionSolver solver = svd.getSolver();
            double[] b = {1, 2};
            double[] x = solver.solve(b);
            RealVector expected = new ArrayRealVector(new double[]{0.573578854, -0.573578854});
            assertArrayEquals(expected.toArray(), x, 0.00001);
        } catch (InvalidMatrixException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testSolverWithSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 0}, {0, 0}});
        SingularValueDecompositionImpl svd;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
            DecompositionSolver solver = svd.getSolver();
            double[] b = {1, 1};
            RealVector expected = new ArrayRealVector(new double[]{1, 1});
            try {
                solver.solve(b);
                fail("MoorePennockConditionNumberException should be thrown");
            } catch (MoorePennockConditionNumberException e) {
                //expected
            }
            assertEquals(expected, solver.getInverse().operate(b));
        } catch (InvalidMatrixException e) {
            fail("Exception should not be thrown");
        }
    }
}