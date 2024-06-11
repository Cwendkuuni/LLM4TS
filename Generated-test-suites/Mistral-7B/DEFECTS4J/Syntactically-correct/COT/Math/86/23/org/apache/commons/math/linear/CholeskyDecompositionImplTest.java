package org.apache.commons.math.linear;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
 import org.apache.commons.math3.exception.MatrixIsSingularException;
 import org.apache.commons.math3.exception.NotPositiveDefiniteMatrixException;
 import org.apache.commons.math3.exception.NotSymmetricMatrixException;
 import org.apache.commons.math3.exception.NonSquareMatrixException;
 import org.apache.commons.math3.exception.NumberIsTooSmallException;
 import org.apache.commons.math3.exception.SingularMatrixException;
 import org.junit.Test;

/**
 * Test class for CholeskyDecompositionImpl.
 */
public class CholeskyDecompositionImplTest {

    /**
     * Test method for {@link CholeskyDecompositionImpl#CholeskyDecompositionImpl(RealMatrix)} constructor.
     */
    @Test
    public void testCholeskyDecompositionImplConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
             {1.0, 0.5, 0.5},
             {0.5, 2.0, 0.5},
             {0.5, 0.5, 3.0}
        });
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        assertNotNull(choleskyDecomposition);
    }

    /**
     * Test method for {@link CholeskyDecompositionImpl#CholeskyDecompositionImpl(RealMatrix, double, double)} constructor.
     */
    @Test
    public void testCholeskyDecompositionImplConstructorWithThresholds() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
             {1.0, 0.5, 0.5},
             {0.5, 2.0, 0.5},
             {0.5, 0.5, 3.0}
        });
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix, 0.1, 1.0e-10);
        assertNotNull(choleskyDecomposition);
    }

    /**
     * Test method for {@link CholeskyDecompositionImpl#CholeskyDecompositionImpl(RealMatrix, double, double)} constructor with invalid inputs.
     */
    @Test
    public void testCholeskyDecompositionImplConstructorWithInvalidInputs() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
                {1.0, 0.5, 0.5},
                {0.5, 2.0, 0.5},
                {0.5, 0.5, 3.0}
        });
        assertThrows(NotPositiveDefiniteMatrixException.class, () -> new CholeskyDecompositionImpl(MatrixUtils.createRealMatrix(new double[][] {
                {-1.0, 0.5, 0.5},
                {0.5, 2.0, 0.5},
                {0.5, 0.5, 3.0}
        })));
        assertThrows(NotSymmetricMatrixException.class, () -> new CholeskyDecompositionImpl(MatrixUtils.createRealMatrix(new double[][] {
                {1.0, 0.5, 0.5},
                {0.5, 2.0, 0.5},
                {0.5, 0.5, 3.0},
                {0.5, 0.5, 3.0}
        })));
        assertThrows(NonSquareMatrixException.class, () -> new CholeskyDecompositionImpl(MatrixUtils.createRealMatrix(new double[][] {
                {1.0, 0.5},
                {0.5, 2.0},
                {0.5, 0.5},
                {0.5, 0.5}
        })));
    }

    /**
     * Test method for {@link CholeskyDecompositionImpl#getL()} method.
     */
    @Test
    public void testGetL() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
                {1.0, 0.5, 0.5},
                {0.5, 2.0, 0.5},
                {0.5, 0.5, 3.0}
        });
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = choleskyDecomposition.getL();
        assertNotNull(l);
        RealMatrix lExpected = MatrixUtils.createRealMatrix(new double[][] {
                {1.0, 0.0, 0.0},
                {0.7071067811865475, 0.408248290463863, 0.0},
                {0.7071067811865475, 0.408248290463863, 1.0}
        });
        assertArrayEquals(lExpected.getData(), l.getData(), 1e-14);
    }

    /**
     * Test method for {@link CholeskyDecompositionImpl#getLT()} method.
     */
    @Test
    public void testGetLT() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
                {1.0, 0.5, 0.5},
                {0.5, 2.0, 0.5},
                {0.5, 0.5, 3.0}
        });
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix lt = choleskyDecomposition.getLT();
        assertNotNull(lt);
        RealMatrix ltExpected = MatrixUtils.createRealMatrix(new double[][] {
                {1.0, 0.5, 0.5},
                {0.7071067811865475, 0.408248290463863, 0.408248290463863},
                {0.7071067811865475, 0.408248290463863, 1.0}
        });
        assertArrayEquals(ltExpected.getData(), lt.getData(), 1e-14);
    }

    /**
     * Test method for {@link CholeskyDecompositionImpl#getDeterminant()} method.
     */
    @Test
    public void testGetDeterminant() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
                {1.0, 0.5, 0.5},
                {0.5, 2.0, 0.5},
                {0.5, 0.5, 3.0}
        });
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        assertEquals(1.0 * 2.0 * 3.0, choleskyDecomposition.getDeterminant(), 1e-14);
    }

    /**
     * Test method for {@link CholeskyDecompositionImpl#getSolver()} method.
     */
    @Test
    public void testGetSolver() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
                {1.0, 0.5, 0.5},
                {0.5, 2.0, 0.5},
                {0.5, 0.5, 3.0}
        });
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = choleskyDecomposition.getSolver();
        assertNotNull(solver);
    }

    /**
     * Test method for {@link CholeskyDecompositionImpl#getSolver().isNonSingular()} method.
     */
    @Test
    public void testGetSolverIsNonSingular() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
                {1.0, 0.5, 0.5},
                {0.5, 2.0, 0.5},
                {0.5, 0.5, 3.0}
        });
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        assertTrue(choleskyDecomposition.getSolver().isNonSingular());
    }

    /**
     * Test method for {@link CholeskyDecompositionImpl#getSolver().solve(RealVector)} method.
     */
    @Test
    public void testGetSolverSolveRealVector() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
                {1.0, 0.5, 0.5},
                {0.5, 2.0, 0.5},
                {0.5, 0.5, 3.0}
        });
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealVector b = new ArrayRealVector(new double[] {1.0, 2.0, 3.0});
        RealVector x = choleskyDecomposition.getSolver().solve(b);
        RealVector expected = new ArrayRealVector(new double[] {-0.0321465313344538, 0.6180339887498949, -0.6180339887498949});
        assertArrayEquals(expected.getData(), x.getData(), 1e-14);
    }

    /**
     * Test method for {@link CholeskyDecompositionImpl#getSolver().solve(RealMatrix)} method.
     */
    @Test
    public void testGetSolverSolveRealMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
                {1.0, 0.5, 0.5},
                {0.5, 2.0, 0.5},
                {0.5, 0.5, 3.0}
        });
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix b = MatrixUtils.createRealMatrix(new double[][] {{1.0}, {2.0}, {3.0}});
        RealMatrix x = choleskyDecomposition.getSolver().solve(b);
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][] {
                {-0.03214653, 0.61803399},
                {0.61803399, -0.61803399}
        });
        assertArrayEquals(expected.getData(), x.getData(), 1e-14);
    }

    /**
     * Test method for {@link CholeskyDecompositionImpl#getSolver().getInverse()} method.
     */
    @Test
    public void testGetSolverGetInverse() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
                {1.0, 0.5, 0.5},
                {0.5, 2.0, 0.5},
                {0.5, 0.5, 3.0}
        });
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix inverse = choleskyDecomposition.getSolver().getInverse();
        assertNull(inverse);
    }

    /**
     * Test edge cases for {@link CholeskyDecompositionImpl#CholeskyDecompositionImpl(RealMatrix, double, double)} constructor.
     */
    @Test
    public void testCholeskyDecompositionImplConstructorEdgeCases() {
        // Test identity matrix
        RealMatrix identityMatrix = MatrixUtils.createRealIdentityMatrix(3);
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(identityMatrix);
        RealMatrix l = choleskyDecomposition.getL();
        assertArrayEquals(identityMatrix.getData(), l.getData(), 1e-14);

        // Test singular value decomposition
        RealMatrix svd = SingularValueDecomposition.compute(MatrixUtils.createRealMatrix(new double[][] {{1.0, 0.0}, {0.0, 0.0}}));
        RealVector singularValues = svd.getSingularValues();
        double[] singularValuesArray = singularValues.toArray();
        assertFalse(singularValuesArray[1] > 0.0);
        try {
            new CholeskyDecompositionImpl(svd.getMatrixA(), 1.0e-15, 1.0e-15);
            fail("Exception should be thrown");
        } catch (NotPositiveDefiniteMatrixException e) {
            // expected
        }
    }

}