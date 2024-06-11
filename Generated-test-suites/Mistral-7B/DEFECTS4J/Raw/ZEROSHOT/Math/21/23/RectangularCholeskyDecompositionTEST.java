import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testDecomposition() {
        double small = 1e-6;

        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.25, 0.25, 0.25},
                                                                      {0.25, 1.0, 0.5, 0.5},
                                                                      {0.25, 0.5, 1.1, 0.8},
                                                                      {0.25, 0.5, 0.8, 2.0}});

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
            RealMatrix root = decomposition.getRootMatrix();
            int rank = decomposition.getRank();

            RealMatrix expectedRoot = new Array2DRowRealMatrix(new double[][]{{1.0, 0.25, 0.0, 0.0},
                                                                                   {0.0, 0.707106781186548, -0.422618380557096, 0.0},
                                                                                   {0.0, 0.422618380557096, 1.0, 0.408248290463863},
                                                                                   {0.0, 0.0, 0.408248290463863, 1.414213562373095}});

            assertEquals("The root matrix is not correct.", expectedRoot, root);
            assertEquals("The rank is not correct.", 3, rank);
        } catch (NonPositiveDefiniteMatrixException e) {
            // The matrix is not positive definite, the decomposition should fail.
            assertEquals("The matrix should not be positive definite.", "Matrix is not positive definite.", e.getMessage());
        }
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        double small = 1e-6;

        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.25, 0.25, 0.25},
                                                                      {0.25, 1.0, 0.5, 0.5},
                                                                      {0.25, 0.5, 1.1, 0.8},
                                                                      {0.25, 0.5, 0.8, 1.5}});

        new RectangularCholeskyDecomposition(matrix, small);
    }
}