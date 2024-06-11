package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.Solver;
import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.junit.Assert;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

    @Test
    public void testCholeskyDecompositionImpl() {
        // Test empty matrix
        CholeskyDecomposition choleskyDecomposition = new CholeskyDecompositionImpl(new Array2DRowRealMatrix(new double[0][0]));
        Assert.assertNull(choleskyDecomposition.getL());
        Assert.assertNull(choleskyDecomposition.getLT());

        // Test identity matrix
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
        CholeskyDecomposition choleskyDecomposition = new CholeskyDecompositionImpl(inputMatrix);
        RealMatrix l = choleskyDecomposition.getL();
        RealMatrix lT = choleskyDecomposition.getLT();
        Assert.assertArrayEquals(new double[][]{{1.0, 0.0}, {0.0, 1.0}}, l.getData());
        Assert.assertArrayEquals(new double[][]{{1.0, 0.0}, {0.0, 1.0}}, lT.getData());

        // Test symmetric positive definite matrix
        RealMatrix inputMatrix1 = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, 3.0, 3.0}, {2.0, 3.0, 5.0}});
        CholeskyDecomposition choleskyDecomposition1 = new CholeskyDecompositionImpl(inputMatrix1, 0.0, 0.0);
        RealMatrix l1 = choleskyDecomposition1.getL();
        RealMatrix lT1 = choleskyDecomposition1.getLT();
        Solver solver = choleskyDecomposition1.getSolver();
        RealVector b = new RealVectorImpl(new double[]{1.0, 2.0, 2.0});
        RealVector x = solver.solve(b);
        Assert.assertArrayEquals(new double[]{1.0 / Math.sqrt(5.0), -1.0 / Math.sqrt(5.0), 1.0 / Math.sqrt(5.0)}, x.toArray());

        // Test matrix with absolute positivity threshold
        RealMatrix inputMatrix2 = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, 3.0, 3.0}, {2.0, 3.0, 5.0 + 1e-9}});
        CholeskyDecomposition choleskyDecomposition2 = null;
        try {
            choleskyDecomposition2 = new CholeskyDecompositionImpl(inputMatrix2, 0.0, 0.0);
            Assert.fail("Expected NotPositiveDefiniteMatrixException");
        } catch (NotPositiveDefiniteMatrixException e) {
            // expected
        }

        // Test matrix with relative symmetry threshold
        RealMatrix inputMatrix3 = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0 + 1e-16, 1.0}, {1.0 + 1e-16, 1.0, 1.0}, {1.0, 1.0, 1.0 + 1e-16}});
        CholeskyDecomposition choleskyDecomposition3 = null;
        try {
            choleskyDecomposition3 = new CholeskyDecompositionImpl(inputMatrix3, 1.0e-16, 0.0);
            Assert.fail("Expected NotSymmetricMatrixException");
        } catch (NotSymmetricMatrixException e) {
            // expected
        }

        // Test matrix with non-square dimensions
        RealMatrix inputMatrix4 = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0}, {2.0, 3.0, 3.0}});
        CholeskyDecomposition choleskyDecomposition4 = null;
        try {
            choleskyDecomposition4 = new CholeskyDecompositionImpl(inputMatrix4);
            Assert.fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            // expected
        }
    }

}