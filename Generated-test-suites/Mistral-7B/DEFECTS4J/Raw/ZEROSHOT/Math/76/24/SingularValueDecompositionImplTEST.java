package org.apache.commons.math.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecompositionImpl;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SingularValueDecompositionImpl;
import org.apache.commons.math3.linear.BiDiagonalTransformer;
import org.apache.commons.math3.special.MathUtils;

import java.lang.reflect.Method;

public class SingularValueDecompositionImplTest {

    @org.junit.Test
    public void testGetSingularValues() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double[] singularValues = svd.getSingularValues();
        assertEquals(2, singularValues.length);
        assertEquals(2.44948974278317, singularValues[0], 1e-10);
        assertEquals(0.0, singularValues[1], 1e-10);
    }

    @org.junit.Test
    public void testGetUT() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix ut = svd.getUT();
        RealMatrix u = svd.getU();
        RealMatrix expected = u.transpose();
        assertEquals(expected, ut);
    }

    @org.junit.Test
    public void testGetS() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix s = svd.getS();
        assertEquals(2, s.getRowDimension());
        assertEquals(2, s.getColumnDimension());
        assertEquals(Math.sqrt(5.0), s.getEntry(0, 0), 1e-10);
        assertEquals(0.0, s.getEntry(1, 0), 1e-10);
        assertEquals(0.0, s.getEntry(0, 1), 1e-10);
        assertEquals(Math.sqrt(13.0), s.getEntry(1, 1), 1e-10);
    }

    @org.junit.Test
    public void testGetV() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix v = svd.getV();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{-0.78255969, -0.41367006},
                                                                         {0.41367006,  0.78255969}});
        assertEquals(expected, v);
    }

    @org.junit.Test
    public void testGetVT() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix vt = svd.getVT();
        RealMatrix v = svd.getV();
        RealMatrix expected = v.transpose();
        assertEquals(expected, vt);
    }

    @org.junit.Test
    public void testGetU() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix u = svd.getU();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{-0.57459669, -0.81649661},
                                                                        {0.81649661,  0.57459669}});
        assertEquals(expected, u);
    }

    @org.junit.Test
    public void testGetSolver() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = svd.getSolver();
        assertEquals(4.0, solver.getRank(), 1e-10);
    }

    @org.junit.Test
    public void testGetCovariance() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix covariance = svd.getCovariance(1e-10);
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{-0.78255969, -0.41367006},
                                                                         {0.41367006,  0.78255969}});
        assertEquals(expected, covariance);
    }

    @org.junit.Test
    public void testGetNorm() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        assertEquals(2.44948974278317, svd.getNorm(), 1e-10);
    }

    @org.junit.Test
    public void testGetConditionNumber() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        assertEquals(10.332744375, svd.getConditionNumber(), 1e-10);
    }

    @org.junit.Test
    public void testGetRank() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        assertEquals(2, svd.getRank());
    }

    @org.junit.Test
    public void testGetUTTranspose() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix ut = svd.getUT();
        RealMatrix utTranspose = svd.getUT().transpose();
        assertEquals(ut, utTranspose);
    }

    @org.junit.Test
    public void testGetVTranspose() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix v = svd.getV();
        RealMatrix vt = svd.getVT();
        RealMatrix vtTranspose = svd.getVT().transpose();
        assertEquals(v, vtTranspose);
    }

    @org.junit.Test
    public void testSolveSingularValueDecompositionImplWithNonSquareMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}, {5, 6}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealVector b = new ArrayRealVector(new double[]{1.0, 0.0, 0.0});
        RealVector x = svd.getSolver().solve(b);
        RealVector expected = new ArrayRealVector(new double[]{0.10537315, 0.23284555, 0.28284271});
        assertEquals(expected, x, 1e-15);
    }

    @org.junit.Test
    public void testSolveSingularValueDecompositionImplWithSquareMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 1}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealVector b = new ArrayRealVector(new double[]{1.0, 1.0});
        RealVector x = svd.getSolver().solve(b);
        RealVector expected = new ArrayRealVector(new double[]{-0.25881904510252096, 0.5176380902050419});
        assertEquals(expected, x, 1e-15);
    }

    @org.junit.Test
    public void testGetInverse() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix inverse = svd.getSolver().getInverse();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.40824829, -0.04082483},
                                                                         {-0.04082483, 0.40824829}});
        assertEquals(expected, inverse);
    }

    @org.junit.Test
    public void testGetPseudoInverse() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix pseudoInverse = svd.getSolver();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.40824829, -0.04082483},
                                                                         {-0.04082483, 0.40824829}});
        assertEquals(expected, pseudoInverse.getInverse());
    }

    @org.junit.Test
    public void testGetRankOfSquareMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 1}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        assertEquals(2, svd.getRank());
    }

    @org.junit.Test
    public void testGetRankOfNonSquareMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}, {5, 6}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        assertEquals(2, svd.getRank());
    }

    @org.junit.Test
    public void testSolveSingularValueDecompositionImplWithZeroSingularValue() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}, {5, 6}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealVector b = new ArrayRealVector(new double[]{1.0, 0.0, 0.0});
        RealVector x = svd.getSolver().solve(b);
        RealVector expected = new ArrayRealVector(new double[]{0.10537315, 0.23284555, 0.28284271});
        assertEquals(expected, x, 1e-15);
    }

    @org.junit.Test
    public void testSolveSingularValueDecompositionImplWithZeroSingularValueInverse() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}, {5, 6}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix inverse = svd.getSolver().getInverse();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.40824829, -0.04082483, 0.13311212},
                                                                         {-0.04082483, 0.40824829, -0.13311212},
                                                                         {0.13311212, -0.13311212, 0.71721896}});
        assertEquals(expected, inverse);
    }

    @org.junit.Test
    public void testSolveSingularValueDecompositionImplWithZeroSingularValuePseudoInverse() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {3, 4}, {5, 6}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix pseudoInverse = svd.getSolver();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{0.40824829, -0.04082483, 0.13311212},
                                                                         {-0.04082483, 0.40824829, -0.13311212},
                                                                         {0.13311212, -0.13311212, 0.71721896}});
        assertEquals(expected, pseudoInverse.getInverse());
    }

}