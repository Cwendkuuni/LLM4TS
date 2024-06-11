package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.apache.commons.math.linear.*;

import org.junit.Test;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImpl() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);

        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();

        RealMatrix productUvs = u.multiply(s).multiply(v.transpose());
        assertTrue(MatrixUtils.isEqual(matrix, productUvs, 1e-9));
    }

    @Test
    public void testSingularValueDecompositionImplWithZeroDeterminant() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        assertThrows(InvalidMatrixException.class, () -> new SingularValueDecompositionImpl(matrix));
    }

    @Test
    public void testSingularValueDecompositionImplWithZeroSingularValues() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{0.0, 0.0}, {0.0, 0.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);

        assertTrue(svd.getRank() == 0);
    }

    @Test
    public void testGetSolver() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 3);

        DecompositionSolver solver = svd.getSolver();

        RealMatrix a = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        RealVector b = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});

        RealVector x = solver.solve(b);

        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{2.206378682373783, 1.431280621882438, 0.713147381552165},
                {-0.734388595711851, -0.451589609161176, -0.21432184435066},
                {0.449408963257323, 0.288360348149861, 0.130265492156328}});

        assertTrue(MatrixUtils.isEqual(expected, MatrixUtils.createRealMatrix(new double[][]{{x.getEntry(0), x.getEntry(1), x.getEntry(2)},
                {x.getEntry(3), x.getEntry(4), x.getEntry(5)},
                {x.getEntry(6), x.getEntry(7), x.getEntry(8)}}), 1e-9));
    }

    @Test
    public void testGetCovariance() throws InvalidMatrixException {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);

        RealMatrix covariance = svd.getCovariance(0.1);

        assertTrue(covariance.getRowDimension() == 2 && covariance.getColumnDimension() == 2);
        assertTrue(covariance.getEntry(0, 0) > 0);
        assertTrue(covariance.getEntry(1, 1) > 0);
        assertTrue(covariance.getEntry(1, 0) == 0);
        assertTrue(covariance.getEntry(0, 1) == 0);
    }
}