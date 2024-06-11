package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SingularValueDecompositionImpl;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.exception.NotPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.NotSymmetricMatrixException;
import org.junit.Test;
import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {

    @Test
    public void testIdentityMatrixDecomposition() throws Exception {
        final double[][] identityMatrixData = {
                {1.0, 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {0.0, 0.0, 1.0}
        };
        RealMatrix inputMatrix = new Array2DRowRealMatrix(identityMatrixData);
        CholeskyDecompositionImpl impl = new CholeskyDecompositionImpl(inputMatrix);
        RealMatrix expectedDecomposition = MatrixUtils.createRealIdentityMatrix(3);
        RealMatrix actualDecomposition = impl.getL();
        assertTrue(MatrixUtils.isEqual(expectedDecomposition, actualDecomposition));
    }

    @Test
    public void testSymmetricMatrixDecomposition() throws Exception {
        final double[][] symmetricMatrixData = {
                {1.1, 1.2, 1.2},
                {1.2, 1.3, 1.3},
                {1.2, 1.3, 1.4}
        };
        RealMatrix inputMatrix = new Array2DRowRealMatrix(symmetricMatrixData);
        CholeskyDecompositionImpl impl = new CholeskyDecompositionImpl(inputMatrix);
        RealMatrix expectedDecomposition = new SingularValueDecompositionImpl(inputMatrix).getL();
        RealMatrix actualDecomposition = impl.getL();
        assertTrue(MatrixUtils.isEqual(expectedDecomposition, actualDecomposition));
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNegativeDefiniteMatrix() throws Exception {
        final double[][] negativeDefiniteMatrixData = {
                {1.0, 0.0, 0.0},
                {0.0, -1.0, 0.0},
                {0.0, 0.0, -1.0}
        };
        RealMatrix inputMatrix = new Array2DRowRealMatrix(negativeDefiniteMatrixData);
        new CholeskyDecompositionImpl(inputMatrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testNonSymmetricMatrix() throws Exception {
        final double[][] nonSymmetricMatrixData = {
                {1.0, 1.1},
                {0.0, 1.0}
        };
        RealMatrix inputMatrix = new Array2DRowRealMatrix(nonSymmetricMatrixData);
        new CholeskyDecompositionImpl(inputMatrix);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrix() throws Exception {
        final double[][] nonSquareMatrixData = {
                {1.0, 0.0},
                {0.0, 1.0},
                {0.0, 1.0}
        };
        RealMatrix inputMatrix = new Array2DRowRealMatrix(nonSquareMatrixData);
        new CholeskyDecompositionImpl(inputMatrix);
    }

    @Test
    public void testSolve() throws Exception {
        final double[][] inputMatrixData = {
                {1.1, 1.2},
                {1.2, 1.3}
        };
        RealMatrix inputMatrix = new Array2DRowRealMatrix(inputMatrixData);
        CholeskyDecompositionImpl impl = new CholeskyDecompositionImpl(inputMatrix);
        RealVector b = new RealVectorImpl(new double[]{1.0, 2.0});
        RealVector x = impl.getSolver().solve(b);
        RealVector expectedX = new RealVectorImpl(new double[]{0.65312712, 0.6259922});
        assertTrue(x.equals(expectedX, 1e-10));
    }

}