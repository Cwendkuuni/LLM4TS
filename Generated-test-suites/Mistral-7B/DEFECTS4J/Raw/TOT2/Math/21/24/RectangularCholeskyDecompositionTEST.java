package org.example;

import org.apache.commons.math3.linear.*;
import org.junit.*;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor() {
        // Test the constructor with a valid matrix and small threshold
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
                {4.0, 3.0, 2.0},
                {3.0, 5.0, 1.0},
                {2.0, 1.0, 2.0}
        });

        RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, 0.0);

        // Test the constructor with a non-positive definite matrix and small threshold
        expectThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(new Array2DRowRealMatrix(new double[][] {
                {-1.0, 0.0, 0.0},
                {0.0, 0.0, 0.0},
                {0.0, 0.0, -1.0}
        }), 0.0));
    }

    @Test
    public void testGetRootMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
                {4.0, 3.0, 2.0},
                {3.0, 5.0, 1.0},
                {2.0, 1.0, 2.0}
        });

        RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, 0.0);

        RealMatrix expected = new Array2DRowRealMatrix(new double[][] {
                {1.0 / FastMath.sqrt(4.0), 0.0, 0.0},
                {-0.5 / FastMath.sqrt(5.0), 1.0 / FastMath.sqrt(5.0), 0.0},
                {-0.5 * FastMath.sqrt(2.0) / FastMath.sqrt(5.0), -0.5 * FastMath.sqrt(2.0) / FastMath.sqrt(5.0), 1.0 / FastMath.sqrt(2.0)}
        });

        assertEquals("Root matrix is not as expected", expected, decomposer.getRootMatrix());
    }

    @Test
    public void testGetRank() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
                {4.0, 3.0, 2.0},
                {3.0, 5.0, 1.0},
                {2.0, 1.0, 2.0}
        });

        RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, 0.0);

        assertEquals("Rank is not as expected", 3, decomposer.getRank());
    }

    @Test
    public void testEmptyMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {});

        expectThrows(IllegalArgumentException.class, () -> new RectangularCholeskyDecomposition(matrix, 0.0));
    }

    @Test
    public void testNullMatrix() {
        RealMatrix matrix = null;

        expectThrows(NullPointerException.class, () -> new RectangularCholeskyDecomposition(matrix, 0.0));
    }

    @Test
    public void testNonSymmetricMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
                {4.0, 3.0},
                {1.0, 5.0}
        });

        expectThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix, 0.0));
    }

    @Test
    public void testNonPositiveSemidefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
                {-1.0, 3.0},
                {3.0, -1.0}
        });

        expectThrows(NonPositiveDefiniteMatrixException.class, () -> new RectangularCholeskyDecomposition(matrix, 0.0));
    }

    @Test
    public void testNegativeSmallThreshold() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
                {4.0, 3.0},
                {3.0, 5.0}
        });

        expectThrows(IllegalArgumentException.class, () -> new RectangularCholeskyDecomposition(matrix, -0.01));
    }
}