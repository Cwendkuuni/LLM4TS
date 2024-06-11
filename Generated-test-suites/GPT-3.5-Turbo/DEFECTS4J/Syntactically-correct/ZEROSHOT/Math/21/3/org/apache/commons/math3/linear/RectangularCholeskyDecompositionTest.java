import static org.junit.Assert.*;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {
  
  @Test
  public void testGetRootMatrix() {
    // Create a symmetric positive semidefinite matrix
    double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
    RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
    
    // Perform rectangular Cholesky decomposition
    RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.000001);
    RealMatrix root = decomposition.getRootMatrix();
    
    // Expected root matrix
    double[][] expectedData = {{2, 0, 0}, {6, 1, 0}, {-8, 5, 3}};
    RealMatrix expected = MatrixUtils.createRealMatrix(expectedData);
    
    // Check if the actual root matrix is equal to the expected root matrix
    assertTrue(root.equals(expected));
  }
  
  @Test
  public void testGetRank() {
    // Create a symmetric positive semidefinite matrix
    double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
    RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
    
    // Perform rectangular Cholesky decomposition
    RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.000001);
    int rank = decomposition.getRank();
    
    // Expected rank
    int expected = 3;
    
    // Check if the actual rank is equal to the expected rank
    assertEquals(expected, rank);
  }
  
  @Test(expected = NonPositiveDefiniteMatrixException.class)
  public void testNonPositiveDefiniteMatrixException() {
    // Create a non-positive definite matrix
    double[][] matrixData = {{-4, 12, -16}, {12, -37, -43}, {-16, -43, 98}};
    RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
    
    // Perform rectangular Cholesky decomposition
    RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.000001);
  }
  
  @Test
  public void testSmallDiagonalElements() {
    // Create a symmetric positive semidefinite matrix with small diagonal elements
    double[][] matrixData = {{4, 0, 0}, {0, 37, 0}, {0, 0, 0.000001}};
    RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
    
    // Perform rectangular Cholesky decomposition
    RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0001);
    int rank = decomposition.getRank();
    
    // Expected rank
    int expected = 2;
    
    // Check if the actual rank is equal to the expected rank
    assertEquals(expected, rank);
  }

}