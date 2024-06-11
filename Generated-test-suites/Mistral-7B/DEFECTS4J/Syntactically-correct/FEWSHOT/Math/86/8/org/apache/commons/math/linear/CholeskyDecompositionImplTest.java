package org.apache.commons.math.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

    /**
     * Test CholeskyDecompositionImpl constructor with a valid matrix.
     */
    @Test
    public void testCholeskyDecompositionImplValidMatrix() {
        double[][] matrixData = {
            {1.0, 0.5, 0.5},
            {0.5, 1.0, 0.5},
            {0.5, 0.5, 1.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        try {
            CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
            RealMatrix l = choleskyDecomposition.getL();
            assertEquals("L is not the expected L.", MatrixUtils.createRealMatrix(lTDataL), l);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test CholeskyDecompositionImpl constructor with a non-square matrix.
     */
    @Test(expected = NonSquareMatrixException.class)
    public void testCholeskyDecompositionImplNonSquareMatrix() {
        double[][] matrixData = {
            {1.0, 0.5},
            {0.5, 1.0},
            {0.5, 0.5}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        new CholeskyDecompositionImpl(matrix);
    }

    /**
     * Test CholeskyDecompositionImpl constructor with a not symmetric matrix.
     */
    @Test(expected = NotSymmetricMatrixException.class)
    public void testCholeskyDecompositionImplNotSymmetricMatrix() {
        double[][] matrixData = {
            {1.0, 0.5},
            {0.5, 1.5}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        new CholeskyDecompositionImpl(matrix, DEFAULT_RELATIVE_SYMMETRY_THRESHOLD, DEFAULT_ABSOLUTE_POSITIVITY_THRESHOLD);
    }

    /**
     * Test CholeskyDecompositionImpl constructor with a not positive definite matrix.
     */
    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testCholeskyDecompositionImplNotPositiveDefiniteMatrix() {
        double[][] matrixData = {
            {1.0, 0.5},
            {0.5, -1.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        new CholeskyDecompositionImpl(matrix, DEFAULT_RELATIVE_SYMMETRY_THRESHOLD, DEFAULT_ABSOLUTE_POSITIVITY_THRESHOLD);
    }

    /**
     * Test getL() method.
     */
    @Test
    public void testGetL() {
        double[][] matrixData = {
            {1.0, 0.5, 0.5},
            {0.5, 1.0, 0.5},
            {0.5, 0.5, 1.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        try {
            CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
            RealMatrix l = choleskyDecomposition.getL();
            assertEquals("L is not the expected L.", MatrixUtils.createRealMatrix(lTDataL), l);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test getLT() method.
     */
    @Test
    public void testGetLT() {
        double[][] matrixData = {
            {1.0, 0.5, 0.5},
            {0.5, 1.0, 0.5},
            {0.5, 0.5, 1.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        try {
            CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
            RealMatrix lt = choleskyDecomposition.getLT();
            assertEquals("LT is not the expected LT.", MatrixUtils.createRealMatrix(lTDataLT), lt);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test getDeterminant() method.
     */
    @Test
    public void testGetDeterminant() {
        double[][] matrixData = {
            {1.0, 0.5, 0.5},
            {0.5, 1.0, 0.5},
            {0.5, 0.5, 1.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        try {
            CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
            assertEquals("Determinant is not the expected determinant.", 1.0, choleskyDecomposition.getDeterminant(), 1e-16);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test getSolver() method.
     */
    @Test
    public void testGetSolver() {
        double[][] matrixData = {
            {1.0, 0.5, 0.5},
            {0.5, 1.0, 0.5},
            {0.5, 0.5, 1.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        try {
            CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
            DecompositionSolver solver = choleskyDecomposition.getSolver();
            assertTrue(solver instanceof CholeskyDecompositionImpl.Solver);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test Solver.isNonSingular() method.
     */
    @Test
    public void testSolverIsNonSingular() {
        double[][] matrixData = {
            {1.0, 0.5, 0.5},
            {0.5, 1.0, 0.5},
            {0.5, 0.5, 1.0}
        };
        double[][] bData = {
            {1.0},
            {2.0},
            {3.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        RealMatrix b = new Array2DRowRealMatrix(bData, false);

        try {
            CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
            CholeskyDecompositionImpl.Solver solver = choleskyDecomposition.getSolver();
            RealVector solution = solver.solve(new RealVectorImpl(b.getDataRef()));
            assertArrayEquals("Solution is not the expected solution.", new double[] {1.0, 1.5, 2.0}, solution.toArray(), 1e-16);
            assertTrue(solver.isNonSingular());
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test Solver.solve() method with a vector.
     */
    @Test
    public void testSolverSolveVector() {
        double[][] matrixData = {
            {1.0, 0.5, 0.5},
            {0.5, 1.0, 0.5},
            {0.5, 0.5, 1.0}
        };
        double[][] bData = {
            {1.0},
            {2.0},
            {3.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        RealMatrix b = new Array2DRowRealMatrix(bData, false);

        try {
            CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
            CholeskyDecompositionImpl.Solver solver = choleskyDecomposition.getSolver();
            RealVector solution = solver.solve(new RealVectorImpl(b.getDataRef()));
            assertArrayEquals("Solution is not the expected solution.", new double[] {1.0, 1.5, 2.0}, solution.toArray(), 1e-16);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test Solver.solve() method with a matrix.
     */
    @Test
    public void testSolverSolveMatrix() {
        double[][] matrixData = {
            {1.0, 0.5, 0.5},
            {0.5, 1.0, 0.5},
            {0.5, 0.5, 1.0}
        };
        double[][] bData = {
            {1.0, 2.0},
            {3.0, 4.0},
            {5.0, 6.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        RealMatrix b = new Array2DRowRealMatrix(bData, false);

        try {
            CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
            CholeskyDecompositionImpl.Solver solver = choleskyDecomposition.getSolver();
            RealMatrix solution = solver.solve(b);
            RealMatrix expectedSolution = new Array2DRowRealMatrix(new double[][] {{1.0, 2.0}, {0.5, 1.5}, {0.5, 1.0}}, false);
            assertEquals("Solution is not the expected solution.", expectedSolution, solution);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test Solver.getInverse() method.
     */
    @Test
    public void testSolverGetInverse() {
        double[][] matrixData = {
            {1.0, 0.5, 0.5},
            {0.5, 1.0, 0.5},
            {0.5, 0.5, 1.0}
        };
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        try {
            CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
            CholeskyDecompositionImpl.Solver solver = choleskyDecomposition.getSolver();
            RealMatrix inverse = solver.getInverse();
            RealMatrix expectedInverse = new Array2DRowRealMatrix(new double[][] {{1.6, 0.2, 0.2}, {0.2, 1.2, 0.2}, {0.2, 0.2, 1.6}}, false);
            assertEquals("Inverse is not the expected inverse.", expectedInverse, inverse);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AssertionError("Unexpected exception: " + e.getMessage());
        }
    }
}