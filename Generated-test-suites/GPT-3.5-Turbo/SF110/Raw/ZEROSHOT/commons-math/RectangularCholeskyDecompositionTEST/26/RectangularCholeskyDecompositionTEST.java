import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.linear.*;

public class RectangularCholeskyDecompositionTest {

  @Test
  public void testConstructorWithPositiveDefiniteMatrix() {
    double[][] testData = {{4, 2, 2}, {2, 9, 7}, {2, 7, 21}};
    RealMatrix testMatrix = MatrixUtils.createRealMatrix(testData);
    RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(testMatrix);
    RealMatrix rootMatrix = decomposition.getRootMatrix();

    assertArrayEquals(testData, rootMatrix.transpose().multiply(rootMatrix).getData());
    assertEquals(3, decomposition.getRank());
  }

  @Test(expected = NonPositiveDefiniteMatrixException.class)
  public void testConstructorWithNonPositiveDefiniteMatrix() {
    double[][] testData = {{1, 2, 3}, {2, 5, 8}, {3, 8, 14}};
    RealMatrix testMatrix = MatrixUtils.createRealMatrix(testData);
    new RectangularCholeskyDecomposition(testMatrix);
  }

  @Test
  public void testGetRootMatrix() {
    double[][] testData = {{4, 2, 2}, {2, 9, 7}, {2, 7, 21}};
    RealMatrix testMatrix = MatrixUtils.createRealMatrix(testData);
    RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(testMatrix);
    RealMatrix rootMatrix = decomposition.getRootMatrix();

    double[][] expectedData = {
      {2, 1, 1},
      {0, 2, 3},
      {0, 0, 4}
    };
    RealMatrix expectedMatrix = MatrixUtils.createRealMatrix(expectedData);
    assertArrayEquals(expectedMatrix.getData(), rootMatrix.getData());
  }

  @Test
  public void testGetRank() {
    double[][] testData = {{4, 2, 2}, {2, 9, 7}, {2, 7, 21}};
    RealMatrix testMatrix = MatrixUtils.createRealMatrix(testData);
    RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(testMatrix);

    assertEquals(3, decomposition.getRank());
  }

}