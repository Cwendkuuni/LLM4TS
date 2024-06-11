import org.junit.Assert;
import org.junit.Test;

public class CholeskyDecompositionImplTest {
  
  // Create the CholeskyDecompositionImpl object
  CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(new RealMatrixImpl(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}}));
  
  // Test the getL() method
  @Test
  public void testGetL() {
    double[][] actual = cholesky.getL().getData();
    double[][] expected = {{2, 0, 0}, {6, 1, 0}, {-8, 5, 3}};
    Assert.assertArrayEquals(expected, actual);
  }
  
  // Test the getLT() method
  @Test
  public void testGetLT() {
    double[][] actual = cholesky.getLT().getData();
    double[][] expected = {{2, 6, -8}, {0, 1, 5}, {0, 0, 3}};
    Assert.assertArrayEquals(expected, actual);
  }
  
  // Test the getDeterminant() method
  @Test
  public void testGetDeterminant() {
    double actual = cholesky.getDeterminant();
    double expected = 324;
    Assert.assertEquals(expected, actual, 0);
  }
  
  // Test the getSolver() method
  @Test
  public void testGetSolver() {
    DecompositionSolver solver = cholesky.getSolver();
    Assert.assertNotNull(solver);
  }
  
  // Test the solve(double[] b) method
  @Test
  public void testSolveDoubleArray() {
    double[] b = {1, 5, -2};
    double[] actual = cholesky.getSolver().solve(b);
    double[] expected = {0.5, -0.1, -0.7};
    Assert.assertArrayEquals(expected, actual, 0);
  }
  
  // Test the solve(RealVector b) method
  @Test
  public void testSolveRealVector() {
    RealVector b = new RealVectorImpl(new double[]{1, 5, -2});
    RealVector actual = cholesky.getSolver().solve(b);
    RealVector expected = new RealVectorImpl(new double[]{0.5, -0.1, -0.7});
    Assert.assertEquals(expected, actual);
  }
  
  // Test the solve(RealVectorImpl b) method
  @Test
  public void testSolveRealVectorImpl() {
    RealVectorImpl b = new RealVectorImpl(new double[]{1, 5, -2});
    RealVectorImpl actual = cholesky.getSolver().solve(b);
    RealVectorImpl expected = new RealVectorImpl(new double[]{0.5, -0.1, -0.7});
    Assert.assertEquals(expected, actual);
  }
  
  // Test the solve(RealMatrix b) method
  @Test
  public void testSolveRealMatrix() {
    RealMatrix b = new RealMatrixImpl(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
    RealMatrix actual = cholesky.getSolver().solve(b);
    RealMatrix expected = new RealMatrixImpl(new double[][]{{-3, -4, -5}, {3, 2, 1}, {-1, 0, 1}});
    Assert.assertEquals(expected, actual);
  }
  
  // Test the getInverse() method
  @Test
  public void testGetInverse() {
    RealMatrix actual = cholesky.getSolver().getInverse();
    RealMatrix expected = new RealMatrixImpl(new double[][]{{0.5, -0.75, 0.5}, {-0.75, 1.75, -1.25}, {0.5, -1.25, 1}});
    Assert.assertEquals(expected, actual);
  }
  
}