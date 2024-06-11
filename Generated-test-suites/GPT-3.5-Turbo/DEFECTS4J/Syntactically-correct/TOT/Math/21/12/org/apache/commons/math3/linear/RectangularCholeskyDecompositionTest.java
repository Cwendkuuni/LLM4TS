import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class RectangularCholeskyDecompositionTest {
  
  private final double EPSILON = 1e-10;
  private RealMatrix testMatrix;
  
  @Before
  public void setUp() {
    double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
    testMatrix = MatrixUtils.createRealMatrix(data);
  }
  
  @Test
  public void testGetRootMatrix() {
    RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(testMatrix, EPSILON);
    RealMatrix rootMatrix = decomposition.getRootMatrix();
    
    double[][] expectedData = {{2, 0, 0}, {6, 1, 0}, {-8, 5, 3}};
    RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(expectedData);
    
    assertTrue(expectedMatrix.subtract(rootMatrix).getNorm() < EPSILON);
  }
  
  @Test
  public void testGetRank() {
    RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(testMatrix, EPSILON);
    int rank = decomposition.getRank();
    
    assertEquals(3, rank);
  }
  
  @Test(expected = NonPositiveDefiniteMatrixException.class)
  public void testInvalidMatrix() {
    double[][] data = {{-5, 2}, {2, 1}};
    testMatrix = MatrixUtils.createRealMatrix(data);
    
    RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(testMatrix, EPSILON);   
  }
  
  @Test
  public void testSmallDiagonalElement() {
    double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 0.001}};
    testMatrix = MatrixUtils.createRealMatrix(data);
    
    RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(testMatrix, EPSILON);
    RealMatrix rootMatrix = decomposition.getRootMatrix();
    
    double[][] expectedData = {{2, 0, 0}, {6, 1, 0}, {-8, 5, 0.031624}};
    RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(expectedData);
    
    assertTrue(expectedMatrix.subtract(rootMatrix).getNorm() < EPSILON);
  }
  
}