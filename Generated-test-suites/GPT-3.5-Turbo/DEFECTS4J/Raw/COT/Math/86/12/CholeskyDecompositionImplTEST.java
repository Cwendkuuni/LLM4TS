import org.junit.Test;
import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {
  
  @Test
  public void testGetL() {
    RealMatrix matrix = new RealMatrixImpl(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
    CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
    RealMatrix expectedL = new RealMatrixImpl(new double[][]{{2, 0, 0}, {6, 1, 0}, {-8, 5, 3}});
    assertTrue(choleskyDecomposition.getL().equals(expectedL));
  }
  
  @Test
  public void testGetLT() {
    RealMatrix matrix = new RealMatrixImpl(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
    CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
    RealMatrix expectedLT = new RealMatrixImpl(new double[][]{{2, 6, -8}, {0, 1, 5}, {0, 0, 3}});
    assertTrue(choleskyDecomposition.getLT().equals(expectedLT));
  }
  
  @Test
  public void testGetDeterminant() {
    RealMatrix matrix = new RealMatrixImpl(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
    CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
    double expectedDeterminant = 576;
    assertEquals(expectedDeterminant, choleskyDecomposition.getDeterminant(), 0.0001);
  }
  
  @Test
  public void testGetSolver() {
    RealMatrix matrix = new RealMatrixImpl(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
    CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
    DecompositionSolver solver = choleskyDecomposition.getSolver();
    double[] b = {1, 2, 3};
    RealVector expectedSolution = new RealVectorImpl(new double[]{4.125, -8.25, 6.125});
    assertTrue(solver.solve(new RealVectorImpl(b)).equals(expectedSolution));
  }
  
  @Test
  public void testIsNonSingular() {
    RealMatrix matrix = new RealMatrixImpl(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
    CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
    DecompositionSolver solver = choleskyDecomposition.getSolver();
    assertTrue(solver.isNonSingular());
  }
  
  @Test
  public void testSolve() {
    RealMatrix matrix = new RealMatrixImpl(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
    CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
    DecompositionSolver solver = choleskyDecomposition.getSolver();
    double[] b = {1, 2, 3};
    RealVector expectedSolution = new RealVectorImpl(new double[]{4.125, -8.25, 6.125});
    assertTrue(solver.solve(new RealVectorImpl(b)).equals(expectedSolution));
  }
  
  @Test
  public void testGetInverse() {
    RealMatrix matrix = new RealMatrixImpl(new double[][]{{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}});
    CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
    RealMatrix expectedInverse = new RealMatrixImpl(new double[][]{{0.0833, -0.3333, 0.6667}, {-0.3333, 0.3333, 0}, {0.6667, 0, 0.3333}});
    assertTrue(choleskyDecomposition.getInverse().equals(expectedInverse));
  }

}