import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

  private static final double EPSILON = 1e-10;
  
  @Test
  public void testGetRootMatrix() {
    double[][] data = {
      {2.0, -1.0},
      {-1.0, 2.0}
    };
    RealMatrix matrix = MatrixUtils.createRealMatrix(data);
    
    RectangularCholeskyDecomposition cholesky = new RectangularCholeskyDecomposition(matrix, EPSILON);
    
    RealMatrix rootMatrix = cholesky.getRootMatrix();
    
    assertEquals(rootMatrix.getEntry(0, 0), 1.4142135623, EPSILON);
    assertEquals(rootMatrix.getEntry(1, 0), -0.7071067811, EPSILON);
    assertEquals(rootMatrix.getEntry(0, 1), 0.0000000000, EPSILON);
    assertEquals(rootMatrix.getEntry(1, 1), 1.2247448714, EPSILON);
  }

  @Test
  public void testGetRank() {
    double[][] data = {
      {2.0, -1.0},
      {-1.0, 2.0}
    };
    RealMatrix matrix = MatrixUtils.createRealMatrix(data);
    
    RectangularCholeskyDecomposition cholesky = new RectangularCholeskyDecomposition(matrix, EPSILON);
    
    int rank = cholesky.getRank();
    
    assertEquals(rank, 2);
  }
  
  @Test
  public void testNonPositiveDefiniteMatrixException() {
    double[][] data = {
      {1.0, -1.0},
      {-1.0, 2.0}
    };
    RealMatrix matrix = MatrixUtils.createRealMatrix(data);
    
    try {
      RectangularCholeskyDecomposition cholesky = new RectangularCholeskyDecomposition(matrix, EPSILON);
      fail("Expected NonPositiveDefiniteMatrixException");
    } catch (NonPositiveDefiniteMatrixException e) {
      assertEquals(e.getMain(), 1.0, EPSILON);
      assertEquals(e.getRow(), 1);
      assertEquals(e.getThreshold(), EPSILON);
    }
  }

}