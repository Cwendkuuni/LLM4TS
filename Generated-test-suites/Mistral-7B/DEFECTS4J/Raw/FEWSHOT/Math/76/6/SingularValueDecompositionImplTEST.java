package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math3.linear.*;

import org.junit.Test;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImpl() throws Exception {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        RealMatrix expectedU = MatrixUtils.createRealMatrix(new double[][]{{0.61232344, 0.78740155},
                {-0.78740155, 0.61232344}});
        RealMatrix expectedV = MatrixUtils.createRealMatrix(new double[][]{{0.73210334, -0.69282032},
                {-0.69282032, 0.73210334}});
        RealMatrix expectedS = MatrixUtils.createRealDiagonalMatrix(new double[]{2.44948974, 0.0});
        assertEquals(expectedU, u);
        assertEquals(expectedS, s);
        assertEquals(expectedV, v);
        RealMatrix product = u.multiply(s).multiply(v.transpose());
        RealMatrix expectedProduct = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        assertEquals(expectedProduct, product);
    }

    @Test(expected = InvalidMatrixException.class)
    public void testSingularValueDecompositionInvalidMatrixException() throws Exception {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 2.0}, {3.0, 4.0}});
        new SingularValueDecompositionImpl(matrix);
    }

    @Test
    public void testSingularValueDecompositionTruncated() throws Exception {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        RealMatrix expectedU = MatrixUtils.createRealMatrix(new double[][]{{0.62539968, 0.75680249, -0.33581821},
                {-0.75680249, 0.62539968, 0.33581821}});
        RealMatrix expectedS = MatrixUtils.createRealDiagonalMatrix(new double[]{2.44948974, 1.58113883});
        RealMatrix expectedV = MatrixUtils.createRealMatrix(new double[][]{{-0.58118828, 0.82233396},
                {-0.82233396, -0.58118828}});
        assertEquals(expectedU, u);
        assertEquals(expectedS, s);
        assertEquals(expectedV, v);
        RealMatrix product = u.multiply(s).multiply(v.transpose());
        RealMatrix expectedProduct = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        assertEquals(expectedProduct, product);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSingularValueDecompositionInvalidCutoffSingularValue() throws Exception {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        new SingularValueDecompositionImpl(matrix).getCovariance(-1);
    }

}