import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        // Test with a positive definite matrix
        RealMatrix matrixPD = new Array2DRowRealMatrix(new double[][] {
                {1.0, 0.2, 0.1},
                {0.2, 1.0, 0.1},
                {0.1, 0.1, 1.0}
        });
        double small = 1e-8;

        try {
            RectangularCholeskyDecomposition decompositonPD = new RectangularCholeskyDecomposition(matrixPD, small);
            RealMatrix rootPD = decompositonPD.getRootMatrix();
            int rankPD = decompositonPD.getRank();

            assertEquals(3, rankPD);
            assertEquals(3, rootPD.getRowDimension());
            assertEquals(rankPD, rootPD.getColumnDimension());
            assertTrue(decompositonPD.getClass().isInstance(rootPD.getEntry(0, 0)));

            for (int i = 0; i < rankPD; ++i) {
                for (int j = 0; j < rankPD; ++j) {
                    double expected = MathUtils.sqrt(matrixPD.getData()[i][i]);
                    double actual = rootPD.getEntry(i, j);
                    assertEquals(expected, actual, small);
                }
            }

            RealMatrix productPD = rootPD.transpose().multiply(rootPD);
            for (int i = 0; i < rankPD; ++i) {
                for (int j = 0; j < rankPD; ++j) {
                    double expected = matrixPD.getData()[i][j];
                    double actual = productPD.getEntry(i, j);
                    assertEquals(expected, actual, small);
                }
            }

        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test with a positive semidefinite matrix
        RealMatrix matrixPSD = new Array2DRowRealMatrix(new double[][] {
                {1.0, 0.2, 0.1},
                {0.2, 1.0, 0.1},
                {0.1, 0.1, 0.0}
        });

        try {
            RectangularCholeskyDecomposition decompositonPSD = new RectangularCholeskyDecomposition(matrixPSD, 0.0);
            RealMatrix rootPSD = decompositonPSD.getRootMatrix();
            int rankPSD = decompositonPSD.getRank();

            assertEquals(3, rankPSD);
            assertEquals(3, rootPSD.getRowDimension());
            assertEquals(rankPSD, rootPSD.getColumnDimension());
            assertTrue(decompositonPSD.getClass().isInstance(rootPSD.getEntry(0, 0)));

            for (int i = 0; i < rankPSD; ++i) {
                for (int j = 0; j < rankPSD; ++j) {
                    double expected = MathUtils.sqrt(matrixPSD.getData()[i][i]);
                    double actual = rootPSD.getEntry(i, j);
                    assertEquals(expected, actual, 0.0);
                }
            }

            RealMatrix productPSD = rootPSD.transpose().multiply(rootPSD);
            for (int i = 0; i < rankPSD; ++i) {
                for (int j = 0; j < rankPSD; ++j) {
                    double expected = matrixPSD.getData()[i][j];
                    double actual = productPSD.getEntry(i, j);
                    assertEquals(expected, actual, 0.0);
                }
            }

            // Test with an invalid matrix
            try {
                new RectangularCholeskyDecomposition(new Array2DRowRealMatrix(new double[][] {}), 0.0);
                fail("Expected NonPositiveDefiniteMatrixException");
            } catch (NonPositiveDefiniteMatrixException e) {
                // Expected
            }

            // Test with a negative definite matrix
            RealMatrix matrixND = new Array2DRowRealMatrix(new double[][] {
                    {1.0, -0.1, 0.1},
                    {-0.1, 1.0, 0.1},
                    {0.1, 0.1, -1.0}
            });

            try {
                new RectangularCholeskyDecomposition(matrixND, 0.0);
                fail("Expected NonPositiveDefiniteMatrixException");
            } catch (NonPositiveDefiniteMatrixException e) {
                // Expected
            }

            // Test with a singular matrix
            RealMatrix matrixSingular = new Array2DRowRealMatrix(new double[][] {
                    {1.0, 0.2, 0.1},
                    {0.2, 1.0, 0.1},
                    {0.1, 0.1, 0.0}
            });

            try {
                new RectangularCholeskyDecomposition(MatrixUtils.singularValueDecomposition(matrixSingular)[0], 0.0);
                fail("Expected NonPositiveDefiniteMatrixException");
            } catch (NonPositiveDefiniteMatrixException e) {
                // Expected
            }

        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetRank() {
        // Test with a positive definite matrix
        RealMatrix matrixPD = new Array2DRowRealMatrix(new double[][] {
                {1.0, 0.2, 0.1},
                {0.2, 1.0, 0.1},
                {0.1, 0.1, 1.0}
        });

        try {
            RectangularCholeskyDecomposition decompositonPD = new RectangularCholeskyDecomposition(matrixPD, 1e-8);
            int rankPD = decompositonPD.getRank();
            assertEquals(3, rankPD);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test with a singular matrix
        RealMatrix matrixSingular = new Array2DRowRealMatrix(new double[][] {
                {1.0, 0.2, 0.1},
                {0.2, 1.0, 0.1},
                {0.1, 0.1, 0.0}
        });

        try {
            RectangularCholeskyDecomposition decompositonSingular = new RectangularCholeskyDecomposition(matrixSingular, 0.0);
            int rankSingular = decompositonSingular.getRank();
            assertEquals(2, rankSingular);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test with a negative definite matrix
        RealMatrix matrixND = new Array2DRowRealMatrix(new double[][] {
                {1.0, -0.1, 0.1},
                {-0.1, 1.0, 0.1},
                {0.1, 0.1, -1.0}
        });

        try {
            new RectangularCholeskyDecomposition(matrixND, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }

        // Test with an empty matrix
        try {
            new RectangularCholeskyDecomposition(new Array2DRowRealMatrix(new double[][] {}), 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

    @Test
    public void testGetRootMatrix() {
        // Test with a positive definite matrix
        RealMatrix matrixPD = new Array2DRowRealMatrix(new double[][] {
                {1.0, 0.2, 0.1},
                {0.2, 1.0, 0.1},
                {0.1, 0.1, 1.0}
        });

        try {
            RectangularCholeskyDecomposition decompositonPD = new RectangularCholeskyDecomposition(matrixPD, 1e-8);
            RealMatrix rootPD = decompositonPD.getRootMatrix();

            assertEquals(3, rootPD.getRowDimension());
            assertEquals(3, rootPD.getColumnDimension());
            assertNotNull(rootPD);

            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 3; ++j) {
                    double expected = MathUtils.sqrt(matrixPD.getData()[i][j]);
                    double actual = rootPD.getEntry(i, j);
                    assertEquals(expected, actual, 1e-8);
                }
            }

        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test with a singular matrix
        RealMatrix matrixSingular = new Array2DRowRealMatrix(new double[][] {
                {1.0, 0.2, 0.1},
                {0.2, 1.0, 0.1},
                {0.1, 0.1, 0.0}
        });

        try {
            RectangularCholeskyDecomposition decompositonSingular = new RectangularCholeskyDecomposition(matrixSingular, 0.0);
            RealMatrix rootSingular = decompositonSingular.getRootMatrix();

            assertEquals(3, rootSingular.getRowDimension());
            assertEquals(2, rootSingular.getColumnDimension());
            assertNotNull(rootSingular);

            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 2; ++j) {
                    double expected = Precision.sqrt(matrixSingular.getData()[i][i]);
                    double actual = rootSingular.getEntry(i, j);
                    assertEquals(expected, actual, 1e-8);
                }
            }

        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test with an empty matrix
        try {
            new RectangularCholeskyDecomposition(new Array2DRowRealMatrix(new double[][] {}), 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }

        // Test with a negative definite matrix
        RealMatrix matrixND = new Array2DRowRealMatrix(new double[][] {
                {1.0, -0.1, 0.1},
                {-0.1, 1.0, 0.1},
                {0.1, 0.1, -1.0}
        });

        try {
            new RectangularCholeskyDecomposition(matrixND, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Expected
        }
    }

}