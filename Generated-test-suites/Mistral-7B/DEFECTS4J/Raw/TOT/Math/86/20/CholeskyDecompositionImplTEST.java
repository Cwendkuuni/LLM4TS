package org.apache.commons.math.linear;

import org.apache.commons.math3.exception.NotPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.NotSymmetricMatrixException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for CholeskyDecompositionImpl class.
 */
public class CholeskyDecompositionImplTest {

    private RealMatrix testMatrix;
    private RealMatrix testSymmetricMatrix;
    private RealMatrix testNonPositiveDefiniteMatrix;
    private RealMatrix testNonSquareMatrix;
    private CholeskyDecompositionImpl choleskyDecomposition;

    @Before
    public void setUp() {
        double[][] testMatrixData = {
            {2.0, 1.0, 1.0},
            {1.0, 2.0, 1.0},
            {1.0, 1.0, 2.0}
        };

        double[][] testSymmetricMatrixData = {
            {1.0, 1.0, 0.0},
            {1.0, 1.0, 1.0},
            {0.0, 1.0, 1.0}
        };

        double[][] testNonPositiveDefiniteMatrixData = {
            {-1.0, 1.0, 1.0},
            {1.0, -1.0, 1.0},
            {1.0, 1.0, -1.0}
        };

        double[][] testNonSquareMatrixData = {
            {1.0, 1.0},
            {1.0, 1.0, 1.0}
        };

        testMatrix = new Array2DRowRealMatrix(testMatrixData);
        testSymmetricMatrix = new Array2DRowRealMatrix(testSymmetricMatrixData);
        testNonPositiveDefiniteMatrix = new Array2DRowRealMatrix(testNonPositiveDefiniteMatrixData);
        testNonSquareMatrix = new Array2DRowRealMatrix(testNonSquareMatrixData);

        try {
            choleskyDecomposition = new CholeskyDecompositionImpl(testMatrix);
        } catch (NotPositiveDefiniteMatrixException | NotSymmetricMatrixException | NonSquareMatrixException e) {
            fail("Unexpected error during test setup: " + e.getMessage());
        }
    }

    @Test
    public void testGetL() {
        RealMatrix expectedL = new Array2DRowRealMatrix(new double[][]{
            {1.4142135625, -0.7071067811, -0.7071067811},
            {-0.7071067811, 1.4142135625, -0.7071067811},
            {-0.7071067811, -0.7071067811, 1.4142135625}
        });
        RealMatrix actualL = choleskyDecomposition.getL();
        assertTrue(expectedL.equals(actualL, 1e-6));
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testGetLWithNonPositiveDefiniteMatrix() {
        choleskyDecomposition = new CholeskyDecompositionImpl(testNonPositiveDefiniteMatrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testGetLWithNonSymmetricMatrix() {
        choleskyDecomposition = new CholeskyDecompositionImpl(testSymmetricMatrix);
        choleskyDecomposition.setRelativeSymmetryThreshold(0.0);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testGetLWithNonSquareMatrix() {
        choleskyDecomposition = new CholeskyDecompositionImpl(testNonSquareMatrix);
    }

    @Test
    public void testGetLT() {
        RealMatrix expectedLT = new Array2DRowRealMatrix(new double[][]{
            {1.4142135625, 1.4142135625, 1.4142135625},
            {-0.7071067811, 0.7071067811, -0.7071067811},
            {-0.7071067811, -0.7071067811, 0.7071067811}
        });
        RealMatrix actualLT = choleskyDecomposition.getLT();
        assertTrue(expectedLT.equals(actualLT, 1e-6));
    }

    @Test
    public void testGetDeterminant() {
        double expectedDeterminant = 2.0 * Math.sqrt(2.0);
        double actualDeterminant = choleskyDecomposition.getDeterminant();
        assertEquals(expectedDeterminant, actualDeterminant, 1e-6);
    }

    @Test
    public void testSolve() {
        double[] expectedSolution = {-0.08209451, 0.41614684, -0.41614684};
        double[] actualSolution = choleskyDecomposition.getSolver().solve(new double[]{1.0, -1.0, 1.0});
        assertArrayEquals(expectedSolution, actualSolution, 1e-6);
    }

    @Test
    public void testSolveWithNonPositiveDefiniteMatrix() {
        choleskyDecomposition = new CholeskyDecompositionImpl(testNonPositiveDefiniteMatrix);
        try {
            choleskyDecomposition.getSolver().solve(new double[]{1.0, 1.0, 1.0});
            fail("Expected NotPositiveDefiniteMatrixException");
        } catch (NotPositiveDefiniteMatrixException e) {
            // expected
        }
    }
}