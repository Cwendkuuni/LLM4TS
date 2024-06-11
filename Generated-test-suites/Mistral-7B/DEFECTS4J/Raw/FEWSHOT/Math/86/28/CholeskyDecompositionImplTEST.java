package org.apache.commons.math.linear;

import static org.junit.Assert.*;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.special.Cholesky;

import java.util.Arrays;

public class CholeskyDecompositionImplTest {

    @Test
    public void testCholeskyDecomposition() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {2.0, 1.0, 1.0},
                {1.0, 2.0, 1.0},
                {1.0, 1.0, 2.0}
        });

        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix expectedLT = Cholesky.chol(MatrixUtils.createRealMatrix(new double[][]{
                {1.41421356, 0.0, 0.0},
                {0.70710678, 1.41421356, 0.0},
                {0.0, 0.70710678, 1.41421356}
        }));

        RealMatrix lt = decomposition.getLT();
        assertEquals(expectedLT, lt);

        RealMatrix l = decomposition.getL();
        RealMatrix expectedL = MatrixUtils.createRealMatrix(new double[][]{
                {1.41421356, 0.0, 0.0},
                {0.70710678, 1.41421356, 0.0},
                {0.0, 0.70710678, 1.41421356},
                {0.0, 0.0, 1.0}
        });

        assertEquals(expectedL, l);

        RealVector expectedSolution = new Array2DRowRealMatrix(new double[][]{
                {1.0, -1.0 / 3.0, 1.0 / 3.0},
                {-1.0 / 3.0, 1.0, -1.0 / 3.0},
                {1.0 / 3.0, -1.0 / 3.0, 1.0}
        }).columnVector(0);

        RealVector solution = decomposition.getSolver().solve(new RealVectorImpl(new double[]{1.0, 0.0, 1.0}));
        assertEquals(expectedSolution, solution);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNegativeDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {2.0, 1.0, 1.0},
                {1.0, 1.0, 1.0},
                {1.0, 1.0, -2.0}
        });
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {2.0, 1.0},
                {1.0, 2.0}
        });
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 1.0},
                {0.0, 2.0}
        });
        new CholeskyDecompositionImpl(matrix, 0.0, 0.0);
    }

    @Test
    public void testRelativeSymmetryThreshold() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {2.0, 1.0e-16, 0.0},
                {1.0e-16, 2.0, 1.0},
                {0.0, 1.0, 2.0}
        });

        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix, 1e-14, 1e-10);
        RealMatrix expectedLT = Cholesky.chol(MatrixUtils.createRealMatrix(new double[][]{
                {1.41421356, 0.0, 0.0},
                {0.70710678, 1.41421356, 0.0},
                {0.0, 0.70710678, 1.41421356}
        }));

        RealMatrix lt = decomposition.getLT();
        assertEquals(expectedLT, lt);

        RealMatrix l = decomposition.getL();
        RealMatrix expectedL = MatrixUtils.createRealMatrix(new double[][]{
                {1.41421356, 0.0, 0.0},
                {0.70710678, 1.41421356, 0.0},
                {0.0, 0.70710678, 1.41421356},
                {0.0, 0.0, 1.0}
        });

        assertEquals(expectedL, l);

        RealVector expectedSolution = new Array2DRowRealMatrix(new double[][]{
                {1.0, -1.0 / 3.0, 1.0 / 3.0},
                {-1.0 / 3.0, 1.0, -1.0 / 3.0},
                {1.0 / 3.0, -1.0 / 3.0, 1.0}
        }).columnVector(0);

        RealVector solution = decomposition.getSolver().solve(new RealVectorImpl(new double[]{1.0, 0.0, 1.0}));
        assertEquals(expectedSolution, solution);
    }

    @Test
    public void testAbsolutePositivityThreshold() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {2.0, 1.0e-16, 0.0},
                {1.0e-16, 1.9999999999999996e-10, 1.0},
                {0.0, 1.0, 2.0}
        });

        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix, 1e-14, 1e-10);
        RealMatrix expectedLT = Cholesky.chol(MatrixUtils.createRealMatrix(new double[][]{
                {1.41421356, 0.0, 0.0},
                {0.70710678, 1.41421356, 0.0},
                {0.0, 0.70710678, 1.41421356}
        }));

        RealMatrix lt = decomposition.getLT();
        assertEquals(expectedLT, lt);

        RealMatrix l = decomposition.getL();
        RealMatrix expectedL = MatrixUtils.createRealMatrix(new double[][]{
                {1.41421356, 0.0, 0.0},
                {0.70710678, 1.41421356, 0.0},
                {0.0, 0.70710678, 1.41421356},
                {0.0, 0.0, 1.0}
        });

        assertEquals(expectedL, l);

        RealVector expectedSolution = new Array2DRowRealMatrix(new double[][]{
                {1.0, -1.0 / 3.0, 1.0 / 3.0},
                {-1.0 / 3.0, 1.0, -1.0 / 3.0},
                {1.0 / 3.0, -1.0 / 3.0, 1.0}
        }).columnVector(0);

        RealVector solution = decomposition.getSolver().solve(new RealVectorImpl(new double[]{1.0, 0.0, 1.0}));
        assertEquals(expectedSolution, solution);
    }

    @Test
    public void testDeterminant() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {2.0, 1.0, 1.0},
                {1.0, 2.0, 1.0},
                {1.0, 1.0, 2.0}
        });

        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        double determinant = decomposition.getDeterminant();

        assertEquals(2.0 * 2.0 * 2.0, determinant, 1e-15);
    }
}