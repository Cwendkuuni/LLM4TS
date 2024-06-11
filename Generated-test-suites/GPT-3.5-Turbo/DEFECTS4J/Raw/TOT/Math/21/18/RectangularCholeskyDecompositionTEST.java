import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {
  
  @Test
  public void testGetRootMatrix() {
    RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {
      { 4, 12, -16 },
      { 12, 37, -43 },
      { -16, -43, 98 }
    });
    
    RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0000001);
    
    RealMatrix expectedRoot = MatrixUtils.createRealMatrix(new double[][] {
      { 2, 6, -8 },
      { 0, 1, -5 },
      { 0, 0, 3 }
    });
    
    Assert.assertTrue(decomposition.getRootMatrix().equals(expectedRoot));
  }
  
  @Test
  public void testGetRank() {
    RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {
      { 4, 12, -16 },
      { 12, 37, -43 },
      { -16, -43, 98 }
    });
    
    RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0000001);
    
    Assert.assertEquals(3, decomposition.getRank());
  }
  
  @Test(expected = NonPositiveDefiniteMatrixException.class)
  public void testNonPositiveDefiniteMatrix() {
    RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {
      { 4, 2, 0 },
      { 2, 1, 1 },
      { 0, 1, 1 }
    });
    
    RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0000001);
  }
  
  @Test(expected = NonPositiveDefiniteMatrixException.class)
  public void testSmallDiagonalElementsThreshold() {
    RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {
      { 4, 2, 0 },
      { 2, 1, 1 },
      { 0, 1, 1 }
    });
    
    RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0001);
  }
}