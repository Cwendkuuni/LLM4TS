package org.apache.commons.math.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

    @Test
    public void testCholeskyDecomposition() {
        // Test default threshold values
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5, 0.5}, {0.5, 2.0, 1.5}, {0.5, 1.5, 3.0}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = decomposition.getL();
        RealMatrix lT = decomposition.getLT();
        RealMatrix expectedL = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5, 0.5}, {0.0, 1.4142135625, 0.7071067811}, {0.0, 0.0, 1.7320508076}});
        RealMatrix expectedLT = MatrixUtils.createRealMatrix(expectedL.getData());
        assertEquals(expectedL, l);
        assertEquals(expectedLT, lT);

        // Test custom threshold values
        double relativeSymmetryThreshold = 1.0e-8;
        double absolutePositivityThreshold = 1.0e-12;
        RealMatrix matrix2 = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5, 0.5}, {0.5, 2.0, 1.5}, {0.5, 1.5, 3.0 + 1.0e-15}});
        CholeskyDecompositionImpl decomposition2 = new CholeskyDecompositionImpl(matrix2, relativeSymmetryThreshold, absolutePositivityThreshold);
        RealMatrix l2 = decomposition2.getL();
        RealMatrix lT2 = decomposition2.getLT();
        RealMatrix expectedL2 = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5, 0.5}, {0.0, 1.4142135625, 0.7071067811}, {0.0, 0.0, 1.7320508076 + 1.0e-15}});
        RealMatrix expectedLT2 = MatrixUtils.createRealMatrix(expectedL2.getData());
        assertEquals(expectedL2, l2, 1.0e-12);
        assertEquals(expectedLT2, lT2, 1.0e-12);

        // Test exceptions
        try {
            CholeskyDecompositionImpl decomposition3 = new CholeskyDecompositionImpl(MatrixUtils.createRealIdentityMatrix(3));
            fail();
        } catch (NonSquareMatrixException e) {
            // expected
        }

        try {
            CholeskyDecompositionImpl decomposition4 = new CholeskyDecompositionImpl(MatrixUtils.createRealDiagonalMatrix(new double[]{0.0, 1.0, 2.0}), 1.0e-16, 1.0e-16);
            fail();
        } catch (NotPositiveDefiniteMatrixException e) {
            // expected
        }

        try {
            CholeskyDecompositionImpl decomposition5 = new CholeskyDecompositionImpl(MatrixUtils.createRealDiagonalMatrix(new double[]{1.0, 0.0, 2.0}), 1.0e-16, 1.0e-16);
            fail();
        } catch (NotSymmetricMatrixException e) {
            // expected
        }

        try {
            CholeskyDecompositionImpl decomposition6 = new CholeskyDecompositionImpl(MatrixUtils.createRealDiagonalMatrix(new double[]{1.0, 1.0, 2.0}), 1.0e-16, 1.0e-16);
            RealMatrix l6 = decomposition6.getL();
            RealMatrix lT6 = decomposition6.getLT();
            assertEquals(MatrixUtils.createRealIdentityMatrix(3), l6.transpose().multiply(lT6));
        } catch (Exception e) {
            fail();
        }

        // Test determinant and solve
        RealMatrix matrix3 = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5, 0.5}, {0.5, 2.0, 1.5}, {0.5, 1.5, 3.0}});
        CholeskyDecompositionImpl decomposition7 = new CholeskyDecompositionImpl(matrix3);
        double determinant = decomposition7.getDeterminant();
        assertEquals(determinant, 11.0, 1.0e-12);
        RealVector b = new Array2DRowRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector x = decomposition7.getSolver().solve(b);
        assertEquals(x.getDimension(), 3);
        assertEquals(b, matrix3.multiply(x), 1.0e-12);

        // Test singular value decomposition
        RealMatrix matrix4 = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 2.0}});
        SingularValueDecomposition svd = new SingularValueDecomposition(matrix4);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        RealMatrix lTExpected = MatrixUtils.createRealMatrix(u.transpose().getData());
        RealMatrix lExpected = new CholeskyDecompositionImpl(matrix4, 1.0e-16, 1.0e-16).getLT();
        assertEquals(lExpected, lTExpected);
    }

}