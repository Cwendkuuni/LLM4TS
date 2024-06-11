import org.junit.Test;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;

public class RectangularCholeskyDecompositionTest {

  @Test
  public void testDecompositionSymmetricPositiveSemidefiniteMatrix() {

    double[][] matrixData = {{1, 0}, {0, 1}};
    RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);

    RectangularCholeskyDecomposition decomposition =
        new RectangularCholeskyDecomposition(matrix, FastMath.pow(10, -15));

    RealMatrix rootMatrix = decomposition.getRootMatrix();

    Assert.assertEquals(2, rootMatrix.getColumnDimension());
    Assert.assertEquals(2, rootMatrix.getRowDimension());

    double[][] expectedRootData = {{1, 0}, {0, 1}};
    RealMatrix expectedRoot = MatrixUtils.createRealMatrix(expectedRootData);

    for (int i = 0; i < expectedRoot.getRowDimension(); i++) {
      for (int j = 0; j < expectedRoot.getColumnDimension(); j++) {
        Assert.assertEquals(expectedRoot.getEntry(i, j), rootMatrix.getEntry(i, j), 1e-15);
      }
    }
  }

  @Test
  public void testDecompositionSymmetricPositiveSemidefiniteMatrixWithSmallDiagonalElements() {

    double[][] matrixData = {{10e-16, 0}, {0, 0}};
    RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);

    RectangularCholeskyDecomposition decomposition =
        new RectangularCholeskyDecomposition(matrix, FastMath.pow(10, -15));

    RealMatrix rootMatrix = decomposition.getRootMatrix();

    Assert.assertEquals(2, rootMatrix.getColumnDimension());
    Assert.assertEquals(2, rootMatrix.getRowDimension());

    double[][] expectedRootData = {{1e-8, 0}, {0, 0}};
    RealMatrix expectedRoot = MatrixUtils.createRealMatrix(expectedRootData);

    for (int i = 0; i < expectedRoot.getRowDimension(); i++) {
      for (int j = 0; j < expectedRoot.getColumnDimension(); j++) {
        Assert.assertEquals(expectedRoot.getEntry(i, j), rootMatrix.getEntry(i, j), 1e-15);
      }
    }
  }

  @Test(expected = NonPositiveDefiniteMatrixException.class)
  public void testDecompositionNonPositiveDefiniteMatrix() {

    double[][] matrixData = {{1, 0}, {0, -1}};
    RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);

    RectangularCholeskyDecomposition decomposition =
        new RectangularCholeskyDecomposition(matrix, FastMath.pow(10, -15));
  }

  @Test(expected = NonPositiveDefiniteMatrixException.class)
  public void testDecompositionNonPositiveDefiniteMatrixWithSmallDiagonalElements() {

    double[][] matrixData = {{1e-16, 0}, {0, -1e-16}};
    RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);

    RectangularCholeskyDecomposition decomposition =
        new RectangularCholeskyDecomposition(matrix, FastMath.pow(10, -15));
  }

  @Test
  public void testRankOfSymmetricPositiveSemidefiniteMatrix() {

    double[][] matrixData = {{1, 2}, {2, 3}};
    RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);

    RectangularCholeskyDecomposition decomposition =
        new RectangularCholeskyDecomposition(matrix, FastMath.pow(10, -15));

    int expectedRank = 2;
    int actualRank = decomposition.getRank();

    Assert.assertEquals(expectedRank, actualRank);
  }

  @Test
  public void testRankOfSymmetricPositiveSemidefiniteMatrixWithSmallDiagonalElements() {

    double[][] matrixData = {{10e-16, 0}, {0, 10e-16}};
    RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);

    RectangularCholeskyDecomposition decomposition =
        new RectangularCholeskyDecomposition(matrix, FastMath.pow(10, -15));

    int expectedRank = 1;
    int actualRank = decomposition.getRank();

    Assert.assertEquals(expectedRank, actualRank);
  }

}