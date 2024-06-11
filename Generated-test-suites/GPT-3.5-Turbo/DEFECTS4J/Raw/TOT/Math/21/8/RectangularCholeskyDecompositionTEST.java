import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Before;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    private final double EPSILON = 1e-10;
    private RealMatrix matrix;
    private RectangularCholeskyDecomposition decomposition;

    @Before
    public void setUp() {
        matrix = MatrixUtils.createRealMatrix(new double[][] {
            {4, -2, 1},
            {-2, 2, -4},
            {1, -4, 6}
        });
        decomposition = new RectangularCholeskyDecomposition(matrix, EPSILON);
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(new double[][] {
            {2, 0, 0},
            {-1, 1, 0},
            {0.5, -1, 1}
        });
        RealMatrix root = decomposition.getRootMatrix();
        assertEquals(expectedRoot, root);
    }

    @Test
    public void testGetRank() {
        int expectedRank = 3;
        int rank = decomposition.getRank();
        assertEquals(expectedRank, rank);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        RealMatrix invalidMatrix = MatrixUtils.createRealMatrix(new double[][] {
            {1, -2, 1},
            {-2, 2, -4},
            {1, -4, 6}
        });
        RectangularCholeskyDecomposition invalidDecomposition =
            new RectangularCholeskyDecomposition(invalidMatrix, EPSILON);
    }

    @Test
    public void testSmallDiagonalElements() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {
            {4, -2, 1},
            {-2, 2, -4},
            {1, -4, EPSILON}
        });
        RectangularCholeskyDecomposition decomposition =
            new RectangularCholeskyDecomposition(matrix, EPSILON);
        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(new double[][] {
            {2, 0, 0},
            {-1, 1, 0},
            {0, -1, 1}
        });
        RealMatrix root = decomposition.getRootMatrix();
        assertEquals(expectedRoot, root);
    }

    @Test
    public void testLargeDiagonalElements() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {
            {4, -2, 1},
            {-2, 2, -4},
            {1, -4, 6 + EPSILON}
        });
        RectangularCholeskyDecomposition decomposition =
            new RectangularCholeskyDecomposition(matrix, EPSILON);
        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(new double[][] {
            {2, 0, 0},
            {-1, 1, 0},
            {0.5, -1, 1}
        });
        RealMatrix root = decomposition.getRootMatrix();
        assertEquals(expectedRoot, root);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testZeroDiagonalElement() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {
            {4, -2, 1},
            {-2, 2, -4},
            {1, -4, 0}
        });
        RectangularCholeskyDecomposition decomposition =
            new RectangularCholeskyDecomposition(matrix, EPSILON);
    }

    @Test
    public void testGetRootMatrixWithZeroRoot() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
        });
        RectangularCholeskyDecomposition decomposition =
            new RectangularCholeskyDecomposition(matrix, EPSILON);
        RealMatrix expectedRoot = MatrixUtils.createRealMatrix(new double[][] {
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
        });
        RealMatrix root = decomposition.getRootMatrix();
        assertEquals(expectedRoot, root);
    }

    @Test
    public void testGetRankWithZeroRank() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
        });
        RectangularCholeskyDecomposition decomposition =
            new RectangularCholeskyDecomposition(matrix, EPSILON);
        int expectedRank = 0;
        int rank = decomposition.getRank();
        assertEquals(expectedRank, rank);
    }

}