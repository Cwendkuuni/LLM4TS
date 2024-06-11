package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SingularValueDecompositionImpl;
import org.apache.commons.math3.linear.Solver;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;

public class CholeskyDecompositionImplTest {

    @Test
    public void testConstructorWithRealMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0},
                                                                      {2.0, 1.0, 2.0},
                                                                      {2.0, 2.0, 1.0}});

        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = choleskyDecomposition.getL();
        RealMatrix lt = choleskyDecomposition.getLT();

        // verify Cholesky decomposition
        RealMatrix expectedLT = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                            {0.0, 1.0, 0.0},
                                                                            {0.0, 0.0, Math.sqrt(5.0)}});
        assertEquals(expectedLT, lt);
        RealMatrix expectedL = lt.transpose();
        assertEquals(expectedL, l);

        // verify determinant
        assertEquals(Math.pow(Math.sqrt(5.0), 3.0), choleskyDecomposition.getDeterminant(), 1e-10);

        // verify solver
        RealVector b = new RealVectorImpl(new double[]{1.0, 2.0, 3.0});
        RealVector x = choleskyDecomposition.getSolver().solve(b);
        assertEquals(new RealVectorImpl(new double[]{1.3872699262258065, 0.3872699262258065, -0.2121648771522537}, false), x, 1e-5);

        // verify inverse
        RealMatrix inverse = choleskyDecomposition.getSolver().getInverse();
        assertNotNull(inverse);
        RealVector x2 = inverse.operate(b);
        assertEquals(new RealVectorImpl(new double[]{1.0/3.0, 1.0/3.0, 1.0/3.0}, false), x2, 1e-5);

    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0},
                                                                      {2.0, 1.0, 2.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testConstructorWithNonSymmetricMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0},
                                                                      {1.0, 3.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0},
                                                                      {2.0, -1.0}});
        try {
            new CholeskyDecompositionImpl(matrix);
            fail("Expected NotPositiveDefiniteMatrixException");
        } catch (NotPositiveDefiniteMatrixException e) {
            // Expected
        }

    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolverWithVectorOfWrongSize() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0},
                                                                      {2.0, 1.0, 2.0},
                                                                      {2.0, 2.0, 1.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        Solver solver = choleskyDecomposition.getSolver();
        RealVector b = new RealVectorImpl(new double[]{1.0, 2.0, 3.0, 4.0});
        try {
            solver.solve(b);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolverWithMatrixOfWrongSize() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 2.0},
                                                                      {2.0, 1.0, 2.0},
                                                                      {2.0, 2.0, 1.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        Solver solver = choleskyDecomposition.getSolver();
        RealMatrix b = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0},
                                                                    {3.0, 4.0}});
        try {
            solver.solve(b);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSolverWithSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0},
                                                                      {0.0, 1.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        Solver solver = choleskyDecomposition.getSolver();
        RealVector b = new RealVectorImpl(new double[]{1.0, 1.0});
        try {
            solver.solve(b);
            fail("Expected SingularValueDecomposition needed instead");
        } catch (InvocationTargetException e) {
            SingularValueDecompositionImpl svd = (SingularValueDecompositionImpl) e.getCause();
            RealMatrix u = svd.getU();
            RealMatrix s = MatrixUtils.createRealDiagonalMatrix(svd.getS());
            RealMatrix v = svd.getV();
            assertEquals(u.transpose().operate(b), v.operate(s.operate(new RealVectorImpl(new double[]{1.0, 0.0}, false))), 1e-5);
        }
    }

}