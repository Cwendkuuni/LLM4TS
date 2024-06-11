import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructorWithValidMatrix() {
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);

        assertEquals(matrix, decomposition.getRootMatrix());
        assertEquals(3, decomposition.getRank());
    }

    @Test
    public void testConstructorWithInvalidMatrix() {
        double[][] data = {{4, 12, -16}, {12, -1, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            fail("NonPositiveDefiniteMatrixException should have been thrown");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertTrue(e.getMessage().contains("Element is not positive definite"));
        }
    }

    @Test
    public void testConstructorWithZeroSmallParameter() {
        double[][] data = {{4, 12, -16}, {12, 1, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);

        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);

        assertEquals(matrix, decomposition.getRootMatrix());
        assertEquals(3, decomposition.getRank());
    }

    @Test
    public void testConstructorWithNonZeroSmallParameter() {
        double[][] data = {{4, 12, -16}, {12, 1, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.1);
            fail("NonPositiveDefiniteMatrixException should have been thrown");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertTrue(e.getMessage().contains("Element is not positive definite"));
        }
    }
}