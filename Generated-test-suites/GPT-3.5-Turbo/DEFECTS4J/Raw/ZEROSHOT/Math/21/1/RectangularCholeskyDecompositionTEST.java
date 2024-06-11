import static org.junit.Assert.*;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

  @Test
  public void testGetRootMatrix() {
    // Define some test data
    double[][] data = {{4, -1, 1}, {-1, 5, 3}, {1, 3, 10}};
    RealMatrix matrix = MatrixUtils.createRealMatrix(data);

    // Create a RectangularCholeskyDecomposition instance
    RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);

    // Test getRootMatrix method
    RealMatrix rootMatrix = decomposition.getRootMatrix();
    assertEquals(3, rootMatrix.getRowDimension());
    assertEquals(3, rootMatrix.getColumnDimension());
    assertTrue(rootMatrix.getEntry(0, 0) > 0);
    assertTrue(rootMatrix.getEntry(1, 1) > 0);
    assertTrue(rootMatrix.getEntry(2, 2) > 0);
  }

  @Test
  public void testGetRank() {
    // Define some test data
    double[][] data = {{4, -1, 1}, {-1, 5, 3}, {1, 3, 10}};
    RealMatrix matrix = MatrixUtils.createRealMatrix(data);

    // Create a RectangularCholeskyDecomposition instance
    RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);

    // Test getRank method
    int rank = decomposition.getRank();
    assertEquals(3, rank);
  }

  @Test(expected = NonPositiveDefiniteMatrixException.class)
  public void testNegativeDefiniteMatrix() {
    // Define some test data
    double[][] data = {{-1, 0}, {0, -1}};
    RealMatrix matrix = MatrixUtils.createRealMatrix(data);

    // Create a RectangularCholeskyDecomposition instance
    RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
  }

}