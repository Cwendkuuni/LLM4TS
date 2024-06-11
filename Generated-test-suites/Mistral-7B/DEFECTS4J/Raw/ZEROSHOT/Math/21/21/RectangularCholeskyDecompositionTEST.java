import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.special.SpecialFunctions;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testEmptyMatrix() {
        try {
            new RectangularCholeskyDecomposition(new Array2DRowRealMatrix(new double[0][0]), 0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testDiagonalMatrix() {
        double[][] data = {{1.0, 0.0}, {0.0, 1.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 0.0);
        RealMatrix root = decomp.getRootMatrix();
        RealMatrix expected = MatrixUtils.createRealMatrix(2, 2, x -> x[0] > 0.0 ? x[0] : 0.0);
        assertEquals(expected, root);
        assertEquals(2, decomp.getRank());
    }

    @Test
    public void testIdentityMatrix() {
        double[][] data = {{1.0, 0.0}, {0.0, 1.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, 1.0e-12);
        RealMatrix root = decomp.getRootMatrix();
        RealMatrix expected = MatrixUtils.createIdentityMatrix(2);
        assertEquals(expected, root);
        assertEquals(2, decomp.getRank());
    }

    @Test
    public void testSingularMatrix() {
        double[][] data = {{1.0, 0.0}, {0.0, 0.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testNegativeMatrix() {
        double[][] data = {{1.0, 0.0}, {0.0, -1.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testNonSymmetricMatrix() {
        double[][] data = {{1.0, 0.0}, {0.0, 2.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testNearlySingularMatrix() {
        double[][] data = {{1.0, 0.0}, {0.0, 1.0e-12}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testSingleVector() {
        double[][] data = {{1.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        try {
            new RectangularCholeskyDecomposition(matrix, 0.0);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // expected
        }
    }

    @Test
    public void testDecomposition() {
        double eps = 1.0e-12;
        double[][] data = {{3.0, 2.0, 1.0}, {2.0, 5.0, 4.0}, {1.0, 4.0, 6.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(data);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, eps);
            RealMatrix root = decomp.getRootMatrix();
            RealMatrix expected = new SingularValueDecomposition(matrix).getV();
            for (int i = 0; i < 3; i++) {
                RealVector v = root.getColumnVector(i);
                RealVector u = expected.getColumnVector(i);
                for (int j = 0; j < 3; j++) {
                    assertEquals(SpecialFunctions.sqrt(data[i][i]), v.getEntry(j), eps);
                    assertEquals(u.getEntry(j), v.getEntry(j), eps);
                }
            }
            assertEquals(3, decomp.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Unexpected NonPositiveDefiniteMatrixException: " + e.getMessage());
        }
    }

}