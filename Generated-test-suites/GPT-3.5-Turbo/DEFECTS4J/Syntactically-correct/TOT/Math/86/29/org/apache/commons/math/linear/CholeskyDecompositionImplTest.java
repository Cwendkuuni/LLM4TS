import org.junit.Assert;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

  @Test
  public void testGetL() {
    // Create a test matrix
    double[][] testData = { {4, 12, -16},
                            {12, 37, -43},
                            {-16, -43, 98} };
    RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
    
    // Create the Cholesky decomposition
    CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
    
    // Get the lower triangular matrix L
    RealMatrix L = cholesky.getL();
    
    // Verify the elements of L
    Assert.assertEquals(2, L.getRowDimension());
    Assert.assertEquals(2, L.getColumnDimension());
    Assert.assertEquals(2.0, L.getEntry(0, 0), 0.0);
    Assert.assertEquals(0.0, L.getEntry(0, 1), 0.0);
    Assert.assertEquals(6.0, L.getEntry(1, 0), 0.0);
    Assert.assertEquals(1.0, L.getEntry(1, 1), 0.0);
  }
  
  @Test
  public void testGetLT() {
    // Create a test matrix
    double[][] testData = { {4, 12, -16},
                            {12, 37, -43},
                            {-16, -43, 98} };
    RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
    
    // Create the Cholesky decomposition
    CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
    
    // Get the transposed lower triangular matrix LT
    RealMatrix LT = cholesky.getLT();
    
    // Verify the elements of LT
    Assert.assertEquals(2, LT.getRowDimension());
    Assert.assertEquals(2, LT.getColumnDimension());
    Assert.assertEquals(2.0, LT.getEntry(0, 0), 0.0);
    Assert.assertEquals(6.0, LT.getEntry(0, 1), 0.0);
    Assert.assertEquals(0.0, LT.getEntry(1, 0), 0.0);
    Assert.assertEquals(1.0, LT.getEntry(1, 1), 0.0);
  }
  
  @Test
  public void testGetDeterminant() {
    // Create a test matrix
    double[][] testData = { {4, 12, -16},
                            {12, 37, -43},
                            {-16, -43, 98} };
    RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
    
    // Create the Cholesky decomposition
    CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
    
    // Get the determinant of the matrix
    double determinant = cholesky.getDeterminant();
    
    // Verify the determinant
    Assert.assertEquals(2304.0, determinant, 0.0);
  }
  
  @Test
  public void testGetSolver() {
    // Create a test matrix
    double[][] testData = { {4, 12, -16},
                            {12, 37, -43},
                            {-16, -43, 98} };
    RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
    
    // Create the Cholesky decomposition
    CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
    
    // Get the solver
    DecompositionSolver solver = cholesky.getSolver();
    
    // Test solving a linear equation
    double[] b = {1, 2, 3};
    double[] x = solver.solve(b);
    double[] expectedX = {0.71, -0.07, 0.17};
    Assert.assertArrayEquals(expectedX, x, 0.01);
  }

}