import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {
  
  RectangularCholeskyDecomposition decomposition;
  double[][] matrixData;
  RealMatrix matrix;

  @Before
  public void setUp() {
    matrixData = new double[][]{
      {1, 0, 0},
      {0, 1, 0},
      {0, 0, 1}
    };
    matrix = MatrixUtils.createRealMatrix(matrixData);
    decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
  }

  @Test
  public void testGetRootMatrix() {
    double[][] expectedRootData = new double[][]{
      {1, 0, 0},
      {0, 1, 0},
      {0, 0, 1}
    };
    RealMatrix expectedRoot = MatrixUtils.createRealMatrix(expectedRootData);
    assertEquals(expectedRoot, decomposition.getRootMatrix());
  }

  @Test
  public void testGetRank() {
    assertEquals(3, decomposition.getRank());
  }
  
  @Test
  public void testNonPositiveDefiniteMatrixException() {
    double[][] matrixData = new double[][]{
      {1, 0, 0},
      {0, -1, 0},
      {0, 0, 1}
    };
    RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
    try {
      decomposition = new RectangularCholeskyDecomposition(matrix, 1e-10);
      fail("Exception not thrown");
    } catch (NonPositiveDefiniteMatrixException e) {
      // Exception expected
    }
  }
  
  @Test
  public void testDependentColumn() {
    double[][] matrixData = new double[][]{
      {1, 0, 0},
      {0, 1, 0},
      {0, 1, 1}
    };
    RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
    int rank = new RectangularCholeskyDecomposition(matrix, 1e-10).getRank();
    assertEquals(2, rank);
  }

}