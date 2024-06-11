import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testEmptyMatrix() {
        try {
            new RectangularCholeskyDecomposition(RealMatrixUtils.createRealMatrix(new double[0][0]), 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testIdentityMatrix() {
        RealMatrix matrix = RealMatrixUtils.createIdentityMatrix(5);
        RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrix, 0.0);
        RealMatrix root = decomposer.getRootMatrix();
        RealMatrix expected = RealMatrixUtils.createRealMatrix(5, 5);
        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 5; ++j) {
                if (i == j) {
                    assertEquals(1.0, root.getEntry(i, j), 1e-8);
                } else {
                    assertEquals(0.0, root.getEntry(i, j), 1e-8);
                }
                expected.setEntry(i, j, i == j ? 1.0 : 0.0);
            }
        }
        assertEquals(expected, root);
        assertEquals(5, decomposer.getRank());
    }

    @Test
    public void testSymmetricPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0, 2.0},
            {2.0, 1.0, 2.0},
            {2.0, 2.0, 3.0}
        });
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }

        RealMatrix matrixSmall = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0, 2.0},
            {2.0, 1.0, 1.5},
            {2.0, 1.5, 3.0}
        });
        RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrixSmall, 1.5);
        RealMatrix root = decomposer.getRootMatrix();
        RealMatrix expected = RealMatrixUtils.createRealMatrix(3, 3);
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                expected.setEntry(i, j, i == j ? 1.0 : (i < j ? Math.sqrt(2.0) : 0.0));
            }
        }
        assertEquals(expected, root);
        assertEquals(3, decomposer.getRank());
    }

    @Test
    public void testNonPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0, 2.0},
            {2.0, -1.0, 2.0},
            {2.0, 2.0, -3.0}
        });
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }

        RealMatrix matrixSmall = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0, 2.0},
            {2.0, -1.0, 2.0},
            {2.0, 2.0, -3.0}
        });
        try {
            new RectangularCholeskyDecomposition(matrixSmall, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }

        RealMatrix matrixNegative = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0, 2.0},
            {2.0, -1.0, 2.0},
            {2.0, 2.0, -3.0}
        });
        try {
            new RectangularCholeskyDecomposition(matrixNegative, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }

        RealMatrix matrixNegativeSmall = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2.0, 2.0},
            {2.0, -1.0, 2.0},
            {2.0, 2.0, -3.0}
        });
        try {
            new RectangularCholeskyDecomposition(matrixNegativeSmall, 1.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }

        RealMatrix matrixDiagonal = new Array2DRowRealMatrix(new double[][] {
            {1.0, 0.0, 0.0},
            {0.0, 2.0, 0.0},
            {0.0, 0.0, 3.0}
        });
        try {
            new RectangularCholeskyDecomposition(matrixDiagonal, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }

        RealMatrix matrixDiagonalSmall = new Array2DRowRealMatrix(new double[][] {
            {1.0, 0.0, 0.0},
            {0.0, 2.0, 1.5},
            {0.0, 1.5, 3.0}
        });
        try {
            new RectangularCholeskyDecomposition(matrixDiagonalSmall, 1.5);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testNaNMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0 / 0.0, 1.0 / 0.0, 1.0 / 0.0},
            {1.0 / 0.0, 1.0 / 0.0, 1.0 / 0.0},
            {1.0 / 0.0, 1.0 / 0.0, 1.0 / 0.0}
        });
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testNegativeMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {-1.0, 2.0, 2.0},
            {2.0, -1.0, 2.0},
            {2.0, 2.0, -3.0}
        });
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testZeroMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0}
        });
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testSingularMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 1.0, 0.0},
            {1.0, 1.0, 0.0},
            {0.0, 0.0, 0.0}
        });
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testSingularMatrixSmall() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 1.0, 0.0},
            {1.0, 1.0, 1.5},
            {0.0, 1.5, 3.0}
        });
        try {
            new RectangularCholeskyDecomposition(matrix, 1.5);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testSymmetricPositiveDefiniteMatrixWithSmallValues() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 1e-6, 1e-6},
            {1e-6, 1.0, 1e-6},
            {1e-6, 1e-6, 1.0}
        });
        try {
            new RectangularCholeskyDecomposition(matrix, 1e-6);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }

        RealMatrix matrixSmall = new Array2DRowRealMatrix(new double[][] {
            {1.0, 1e-8, 1e-8},
            {1e-8, 1.0, 1e-8},
            {1e-8, 1e-8, 1.0}
        });
        try {
            new RectangularCholeskyDecomposition(matrixSmall, 1e-8);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }

        RealMatrix matrixSmall2 = new Array2DRowRealMatrix(new double[][] {
            {1.0, 1e-12, 1e-12},
            {1e-12, 1.0, 1e-12},
            {1e-12, 1e-12, 1.0}
        });
        RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrixSmall2, 1e-12);
        RealMatrix root = decomposer.getRootMatrix();
        RealMatrix expected = RealMatrixUtils.createRealMatrix(3, 3);
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (i == j) {
                    assertEquals(1.0, root.getEntry(i, j), 1e-12);
                } else {
                    assertEquals(0.0, root.getEntry(i, j), 1e-12);
                }
                expected.setEntry(i, j, i == j ? 1.0 : 0.0);
            }
        }
        assertEquals(expected, root);
        assertEquals(3, decomposer.getRank());
    }

    @Test
    public void testSymmetricPositiveDefiniteMatrixWithSmallValues2() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2e-6, 1e-6},
            {2e-6, 1.0, 1e-6},
            {1e-6, 1e-6, 1.0}
        });
        try {
            new RectangularCholeskyDecomposition(matrix, 1e-6);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }

        RealMatrix matrixSmall = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2e-8, 1e-8},
            {2e-8, 1.0, 1e-8},
            {1e-8, 1e-8, 1.0}
        });
        try {
            new RectangularCholeskyDecomposition(matrixSmall, 1e-8);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }

        RealMatrix matrixSmall3 = new Array2DRowRealMatrix(new double[][] {
            {1.0, 2e-12, 1e-12},
            {2e-12, 1.0, 1e-12},
            {1e-12, 1e-12, 1.0}
        });
        RectangularCholeskyDecomposition decomposer = new RectangularCholeskyDecomposition(matrixSmall3, 1e-12);
        RealMatrix root = decomposer.getRootMatrix();
        RealMatrix expected = RealMatrixUtils.createRealMatrix(3, 3);
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (i == j) {
                    assertEquals(1.0, root.getEntry(i, j), 1e-12);
                } else {
                    assertEquals(0.0, root.getEntry(i, j), 1e-12);
                }
                expected.setEntry(i, j, i == j ? 1.0 : 0.0);
            }
        }
        assertEquals(expected, root);
        assertEquals(3, decomposer.getRank());
    }

}