import org.junit.Assert;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

  @Test
  public void testGetL() {
    double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
    RealMatrix matrix = MatrixUtils.createRealMatrix(data);
    CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
    RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{2, 0, 0}, {6, 1, 0}, {-8, 5, 3}});
    RealMatrix actual = cholesky.getL();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetLT() {
    double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
    RealMatrix matrix = MatrixUtils.createRealMatrix(data);
    CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
    RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{2, 6, -8}, {0, 1, 5}, {0, 0, 3}});
    RealMatrix actual = cholesky.getLT();
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetDeterminant() {
    double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
    RealMatrix matrix = MatrixUtils.createRealMatrix(data);
    CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
    double expected = 36;
    double actual = cholesky.getDeterminant();
    Assert.assertEquals(expected, actual, 0);
  }

  @Test
  public void testGetSolver() {
    double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
    RealMatrix matrix = MatrixUtils.createRealMatrix(data);
    CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
    DecompositionSolver solver = cholesky.getSolver();
    Assert.assertNotNull(solver);
  }

  @Test
  public void testSolveDoubleArray() {
    double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
    RealMatrix matrix = MatrixUtils.createRealMatrix(data);
    CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
    double[] b = {2, 2, 2};
    double[] expected = {0.5, -0.0625, 0.16666666666666666};
    double[] actual = cholesky.getSolver().solve(b);
    Assert.assertArrayEquals(expected, actual, 0);
  }

  @Test
  public void testSolveRealVector() {
    double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
    RealMatrix matrix = MatrixUtils.createRealMatrix(data);
    CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
    RealVector b = new RealVectorImpl(new double[]{2, 2, 2});
    RealVector expected = new RealVectorImpl(new double[]{0.5, -0.0625, 0.16666666666666666});
    RealVector actual = cholesky.getSolver().solve(b);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testSolveRealMatrix() {
    double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
    RealMatrix matrix = MatrixUtils.createRealMatrix(data);
    CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
    RealMatrix b = MatrixUtils.createColumnRealMatrix(new double[]{2, 2, 2});
    RealMatrix expected = MatrixUtils.createColumnRealMatrix(new double[]{0.5, -0.0625, 0.16666666666666666});
    RealMatrix actual = cholesky.getSolver().solve(b);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testGetInverse() {
    double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
    RealMatrix matrix = MatrixUtils.createRealMatrix(data);
    CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
    RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{0.5, 0.5, -0.5}, {0.5, -0.0625, -0.5625}, {-0.5, -0.5625, 4.395833333333333}});
    RealMatrix actual = cholesky.getSolver().getInverse();
    Assert.assertEquals(expected, actual);
  }

}