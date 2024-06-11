import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.*;

public class RectangularCholeskyDecompositionTest {
  
  @Test
  public void testConstructor() {
    RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
    RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
    assertNotNull(decomposition);
  }
  
  @Test
  public void testGetRootMatrix() {
    RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
    RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
    RealMatrix root = decomposition.getRootMatrix();
    assertNotNull(root);
  }
  
  @Test
  public void testGetRank() {
    RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
    RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
    int rank = decomposition.getRank();
    assertEquals(3, rank);
  }
  
  @Test(expected = NonPositiveDefiniteMatrixException.class)
  public void testNonPositiveDefiniteMatrixException() {
    RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{-4, -12, 16}, {-12, -37, 43}, {16, 43, -98}});
    RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
  }
  
  @Test
  public void testSmallParameterConstructor() {
    RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
    RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.1);
    assertNotNull(decomposition);
  }
  
  @Test(expected = NonPositiveDefiniteMatrixException.class)
  public void testSmallParameterNonPositiveDefiniteMatrixException() {
    RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{-4, -12, 16}, {-12, -37, 43}, {16, 43, -98}});
    RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.1);
  }
}