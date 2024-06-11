import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

  @Test
  public void testConstructor() {
    // Typical use case
    RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4,12,-16},{12,37,-43},{-16,-43,98}});
    RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
    assertNotNull(decomposition);

    // Edge cases
    // Test with minimum order matrix
    matrix = MatrixUtils.createRealMatrix(new double[][]{{4}});
    decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
    assertNotNull(decomposition);

    // Error scenarios
    // Test with non-positive semidefinite matrix
    matrix = MatrixUtils.createRealMatrix(new double[][]{{-1,0,0},{0,-1,0},{0,0,1}});
    try {
      decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
      fail("Expected NonPositiveDefiniteMatrixException");
    } catch (NonPositiveDefiniteMatrixException e) {
      // success
    }
  }

  @Test
  public void testGetRootMatrix() {
    // Typical use case
    RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4,12,-16},{12,37,-43},{-16,-43,98}});
    RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
    RealMatrix root = decomposition.getRootMatrix();
    assertNotNull(root);

    // Edge cases
    // Test with minimum order matrix
    matrix = MatrixUtils.createRealMatrix(new double[][]{{4}});
    decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
    root = decomposition.getRootMatrix();
    assertNotNull(root);
  }

  @Test
  public void testGetRank() {
    // Typical use case
    RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4,12,-16},{12,37,-43},{-16,-43,98}});
    RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
    int rank = decomposition.getRank();
    assertEquals(3, rank);

    // Edge cases
    // Test with minimum order matrix
    matrix = MatrixUtils.createRealMatrix(new double[][]{{4}});
    decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
    rank = decomposition.getRank();
    assertEquals(1, rank);
  }

}