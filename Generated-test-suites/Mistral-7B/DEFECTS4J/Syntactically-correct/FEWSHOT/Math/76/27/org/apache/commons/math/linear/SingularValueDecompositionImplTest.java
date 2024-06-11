package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.EigenDecompositionImpl;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SingularValueDecompositionImpl;
import org.apache.commons.math3.linear.Transformer;
import org.apache.commons.math3.linear.TransformerFactory;
import org.apache.commons.math3.linear.util.MatrixUtils;
import org.apache.commons.math3.special.MathUtils;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImpl() {
        // Test empty matrix
        assertThrows(InvalidMatrixException.class, () -> new SingularValueDecompositionImpl(RealMatrix.IDENTITY_2X2));

        // Test identity matrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);

        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        RealMatrix ut = svd.getUT();
        RealMatrix vt = svd.getVT();

        assertEquals(matrix, u.multiply(s).multiply(v));
        assertEquals(matrix, ut.multiply(v));
        assertEquals(matrix, v.multiply(s).transpose().multiply(ut));

        // Test non-square matrix
        matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}, {1.0, 1.0}});
        svd = new SingularValueDecompositionImpl(matrix);

        u = svd.getU();
        s = svd.getS();
        v = svd.getV();
        ut = svd.getUT();
        vt = svd.getVT();

        assertEquals(matrix, u.multiply(s).multiply(v));
        assertEquals(MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 1.0}, {0.0, 1.0, 1.0}}),
                ut.multiply(v));
        assertEquals(MatrixUtils.createRealMatrix(new double[][]{{1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0)},
                        {-1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0)}}), v.transpose());

        // Test singular values
        matrix = MatrixUtils.createRealMatrix(new double[][]{{4.0, 2.0}, {2.0, 4.0}});
        svd = new SingularValueDecompositionImpl(matrix, 2);

        double[] singularValues = svd.getSingularValues();
        assertEquals(2.0, singularValues[0], 0.0);
        assertEquals(2.0, singularValues[1], 0.0);

        // Test singular values and U, VT
        matrix = MatrixUtils.createRealMatrix(new double[][]{{2.0, 1.0}, {1.0, 2.0}});
        svd = new SingularValueDecompositionImpl(matrix, 1);

        u = svd.getU();
        s = svd.getS();
        v = svd.getV();
        ut = svd.getUT();
        vt = svd.getVT();

        assertEquals(matrix, u.multiply(s).multiply(v));
        assertEquals(MatrixUtils.createRealMatrix(new double[][]{{1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0)},
                {-1.0 / Math.sqrt(2.0), 1.0 / Math.sqrt(2.0)}}), vt);
        assertEquals(1.0, singularValues[0], 0.0);

        // Test solver
        matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 1.0, 1.0}, {1.0, 0.0, 1.0}, {1.0, 1.0, 1.0}});
        svd = new SingularValueDecompositionImpl(matrix, 3);

        double[] b = {1.0, 0.0, 1.0};
        double[] x = svd.getSolver().solve(b);

        assertEquals(3, x.length);
        assertArrayEquals(new double[]{0.5, 0.5, 0.5}, x, 0.001);

        // Test getRank
        matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 1.0, 1.0}, {1.0, 0.0, 1.0}, {1.0, 1.0, 1.0}});
        svd = new SingularValueDecompositionImpl(matrix, 3);
        assertEquals(3, svd.getRank());

        matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 1.0, 1.0}, {1.0, 0.0, 1.0}, {1.0, 1.0, 0.0}});
        svd = new SingularValueDecompositionImpl(matrix, 3);
        assertEquals(2, svd.getRank());

        // Test getNorm and getConditionNumber
        matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        svd = new SingularValueDecompositionImpl(matrix, 2);
        assertEquals(1.0, svd.getNorm(), 0.0);
        assertEquals(1.0 / Math.sqrt(2.0), svd.getConditionNumber(), 0.0);

        // Test getCovariance
        matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 1.0, 0.0}, {1.0, 1.0, 0.0}, {0.0, 0.0, 1.0},
                {0.0, 0.0, 1.0}});
        svd = new SingularValueDecompositionImpl(matrix, 2);
        RealMatrix covariance = svd.getCovariance(0.1);

        assertEquals(2, covariance.getRowDimension());
        assertEquals(2, covariance.getColumnDimension());
        assertEquals(1.0, covariance.getEntry(0, 0), 0.01);
        assertEquals(1.0, covariance.getEntry(1, 1), 0.01);
        assertEquals(0.0, covariance.getEntry(0, 1), 0.01);
        assertEquals(0.0, covariance.getEntry(1, 0), 0.01);
    }

    @Test
    public void testBiDiagonalTransformer() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        Transformer transformer = new BiDiagonalTransformer(matrix);

        double[] mainBidiagonal = transformer.getMainDiagonalRef();
        double[] secondaryBidiagonal = transformer.getSecondaryDiagonalRef();

        assertEquals(2.0, mainBidiagonal[0], 0.0);
        assertEquals(2.0, mainBidiagonal[1], 0.0);
        assertEquals(0.0, secondaryBidiagonal[0], 0.0);
        assertEquals(3.0, secondaryBidiagonal[1], 0.0);
    }

}