import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.linear.*;
import static org.junit.Assert.assertEquals;

public class RectangularCholeskyDecompositionTest {

  @Test
  public void testGetRootMatrix() {
    // Initialize the matrix
    double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
    RealMatrix matrix = MatrixUtils.createRealMatrix(data);

    // Calculate the Cholesky decomposition
    RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0001);

    // Get the root matrix
    RealMatrix root = decomposition.getRootMatrix();

    // Create the expected root matrix
    double[][] expectedData = {{2, 0, 0}, {6, 1, 0}, {-8, 5, 3}};
    RealMatrix expectedRoot = MatrixUtils.createRealMatrix(expectedData);

    // Compare the calculated root matrix with the expected root matrix
    Assert.assertEquals(expectedRoot, root);
  }

  @Test
  public void testGetRank() {
    // Initialize the matrix
    double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
    RealMatrix matrix = MatrixUtils.createRealMatrix(data);

    // Calculate the Cholesky decomposition
    RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0001);

    // Get the rank
    int rank = decomposition.getRank();

    // Compare the calculated rank with the expected rank
    Assert.assertEquals(3, rank);
  }

  @Test(expected = NonPositiveDefiniteMatrixException.class)
  public void testNonPositiveDefiniteMatrixException() {
    // Initialize the matrix
    double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 97}};
    RealMatrix matrix = MatrixUtils.createRealMatrix(data);

    // Calculate the Cholesky decomposition
    RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0001);
  }

  @Test(expected = NonPositiveDefiniteMatrixException.class)
  public void testSmallDiagonalElementsThresholdException() {
    // Initialize the matrix
    double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
    RealMatrix matrix = MatrixUtils.createRealMatrix(data);

    // Calculate the Cholesky decomposition with a small diagonal elements threshold
    RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.1);
  }

  @Test
  public void testEdgeCase() {
    // Initialize the matrix with all zero elements
    double[][] data = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
    RealMatrix matrix = MatrixUtils.createRealMatrix(data);

    // Calculate the Cholesky decomposition
    RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0001);

    // Get the root matrix
    RealMatrix root = decomposition.getRootMatrix();

    // Create the expected root matrix
    double[][] expectedData = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
    RealMatrix expectedRoot = MatrixUtils.createRealMatrix(expectedData);

    // Compare the calculated root matrix with the expected root matrix
    Assert.assertEquals(expectedRoot, root);
  }

}