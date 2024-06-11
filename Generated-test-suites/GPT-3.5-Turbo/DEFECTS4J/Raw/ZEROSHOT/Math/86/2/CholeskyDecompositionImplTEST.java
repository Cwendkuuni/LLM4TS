import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CholeskyDecompositionImplTest {
  
  private CholeskyDecompositionImpl cholesky;
  
  @Before
  public void setUp() throws Exception {
    double[][] testData = {{4, -1, 1}, {-1, 8, -2}, {1, -2, 6}};
    RealMatrix testMatrix = new RealMatrixImpl(testData);
    cholesky = new CholeskyDecompositionImpl(testMatrix);
  }
  
  @Test
  public void testGetL() {
    double[][] expectedData = {{2, 0, 0}, {-0.5, 2.8, 0}, {0.5, -0.6, 2.4}};
    RealMatrix expectedL = new RealMatrixImpl(expectedData);
    RealMatrix actualL = cholesky.getL();
    assertEquals(expectedL, actualL);
  }
  
  @Test
  public void testGetLT() {
    double[][] expectedData = {{2, -0.5, 0.5}, {0, 2.8, -0.6}, {0, 0, 2.4}};
    RealMatrix expectedLT = new RealMatrixImpl(expectedData);
    RealMatrix actualLT = cholesky.getLT();
    assertEquals(expectedLT, actualLT);
  }
  
  @Test
  public void testGetDeterminant() {
    double expectedDeterminant = 38.0;
    double actualDeterminant = cholesky.getDeterminant();
    assertEquals(expectedDeterminant, actualDeterminant, 0.0001);
  }
  
  @Test
  public void testGetSolver() {
    DecompositionSolver solver = cholesky.getSolver();
    assertNotNull(solver);
  }
  
  @Test
  public void testSolve() {
    double[] b = {3, 2, 1};
    double[] expectedSolution = {1, 1, 1};
    double[] actualSolution = cholesky.getSolver().solve(b);
    assertArrayEquals(expectedSolution, actualSolution, 0.0001);
  }
  
  @Test
  public void testSolveRealVector() {
    RealVector b = new RealVectorImpl(new double[]{3, 2, 1});
    RealVector expectedSolution = new RealVectorImpl(new double[]{1, 1, 1});
    RealVector actualSolution = cholesky.getSolver().solve(b);
    assertEquals(expectedSolution, actualSolution);
  }
  
  @Test
  public void testSolveRealMatrix() {
    double[][] bData = {{3, 1}, {2, 2}, {1, 3}};
    RealMatrix b = new RealMatrixImpl(bData);
    double[][] expectedSolutionData = {{1, 0}, {1, 1}, {1, 2}};
    RealMatrix expectedSolution = new RealMatrixImpl(expectedSolutionData);
    RealMatrix actualSolution = cholesky.getSolver().solve(b);
    assertEquals(expectedSolution, actualSolution);
  }
  
  @Test
  public void testGetInverse() {
    double[][] expectedInverseData = {{0.3, 0.1}, {0.1, 0.2}};
    RealMatrix expectedInverse = new RealMatrixImpl(expectedInverseData);
    RealMatrix actualInverse = cholesky.getSolver().getInverse();
    assertEquals(expectedInverse, actualInverse);
  }
  
}