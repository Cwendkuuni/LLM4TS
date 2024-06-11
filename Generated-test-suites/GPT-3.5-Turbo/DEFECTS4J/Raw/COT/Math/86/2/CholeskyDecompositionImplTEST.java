import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CholeskyDecompositionImplTest {
  
  private CholeskyDecompositionImpl cholesky;
  
  @Before
  public void setup() {
    // create a test matrix
    double[][] testData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
    RealMatrix testMatrix = MatrixUtils.createRealMatrix(testData);
    // initialize the CholeskyDecompositionImpl object
    cholesky = new CholeskyDecompositionImpl(testMatrix);
  }
  
  @Test
  public void testGetL() {
    // perform some assertions on the L matrix
    Assert.assertEquals(2.0, cholesky.getL().getEntry(0, 0), 0.0);
    Assert.assertEquals(0.0, cholesky.getL().getEntry(0, 1), 0.0);
    Assert.assertEquals(-8.0, cholesky.getL().getEntry(1, 0), 0.0);
    Assert.assertEquals(3.0, cholesky.getL().getEntry(1, 1), 0.0);
  }
  
  @Test
  public void testGetLT() {
    // perform some assertions on the LT matrix
    Assert.assertEquals(2.0, cholesky.getLT().getEntry(0, 0), 0.0);
    Assert.assertEquals(-8.0, cholesky.getLT().getEntry(0, 1), 0.0);
    Assert.assertEquals(0.0, cholesky.getLT().getEntry(1, 0), 0.0);
    Assert.assertEquals(3.0, cholesky.getLT().getEntry(1, 1), 0.0);
  }
  
  @Test
  public void testGetDeterminant() {
    // perform an assertion on the determinant value
    Assert.assertEquals(4.0, cholesky.getDeterminant(), 0.0);
  }
  
  @Test
  public void testGetSolver() {
    // perform assertions on the solver object
    DecompositionSolver solver = cholesky.getSolver();
    Assert.assertTrue(solver.isNonSingular());
  }
}