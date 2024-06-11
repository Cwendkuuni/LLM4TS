import org.apache.commons.math3.linear.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class RectangularCholeskyDecompositionTest {

    // Test case for constructor with small parameter

    @Test
    public void testConstructorWithSmall() {
        double[][] testData = {{1, 0.5, 0.5}, {0.5, 1, 0.5}, {0.5, 0.5, 1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.1);
            assertEquals(matrix, decomposition.getRootMatrix());
            assertEquals(3, decomposition.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Exception should not be thrown");
        }
    }

    // Test case for constructor without small parameter

    @Test
    public void testConstructorWithoutSmall() {
        double[][] testData = {{1, 0.5, 0.5}, {0.5, 1, 0.5}, {0.5, 0.5, 1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            assertEquals(matrix, decomposition.getRootMatrix());
            assertEquals(3, decomposition.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Exception should not be thrown");
        }
    }

    // Additional test case for constructor with non-positive definite matrix

    @Test
    public void testConstructorWithNonPositiveDefiniteMatrix() {
        double[][] testData = {{1, 0.5, 0.5}, {0.5, -1, 0.5}, {0.5, 0.5, 1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.1);
            fail("Exception should be thrown due to non-positive definite matrix");
        } catch (NonPositiveDefiniteMatrixException e) {
            assertTrue(e.getMessage().contains("Non-positive definite matrix"));
        }
    }

    // Test case for getRootMatrix()

    @Test
    public void testGetRootMatrix() {
        double[][] testData = {{1, 0.5, 0.5}, {0.5, 1, 0.5}, {0.5, 0.5, 1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            assertEquals(matrix, decomposition.getRootMatrix());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Exception should not be thrown");
        }
    }

    // Test case for getRank()

    @Test
    public void testGetRank() {
        double[][] testData = {{1, 0.5, 0.5}, {0.5, 1, 0.5}, {0.5, 0.5, 1}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);

        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            assertEquals(3, decomposition.getRank());
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Exception should not be thrown");
        }
    }

}