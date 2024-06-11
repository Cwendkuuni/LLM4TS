import org.junit.Test;

import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {
  
  private CholeskyDecompositionImpl decomposition;
  private RealMatrix matrix;
  
  @Test
  public void testGetL() {
    matrix = MatrixUtils.createRealMatrix(new double[][] {
        {1, 2, 3},
        {2, 5, 8},
        {3, 8, 14}
    });
    decomposition = new CholeskyDecompositionImpl(matrix);
    RealMatrix expectedL = MatrixUtils.createRealMatrix(new double[][] {
        {1, 0, 0},
        {2, 1, 0},
        {3, 2, 1}
    });
    assertEquals(expectedL, decomposition.getL());
  }
  
  @Test
  public void testGetLT() {
    matrix = MatrixUtils.createRealMatrix(new double[][] {
        {1, 2, 3},
        {2, 5, 8},
        {3, 8, 14}
    });
    decomposition = new CholeskyDecompositionImpl(matrix);
    RealMatrix expectedLT = MatrixUtils.createRealMatrix(new double[][] {
        {1, 2, 3},
        {0, 1, 2},
        {0, 0, 1}
    });
    assertEquals(expectedLT, decomposition.getLT());
  }
  
  @Test
  public void testGetDeterminant() {
    matrix = MatrixUtils.createRealMatrix(new double[][] {
        {4, 2, -5},
        {2, 9, 1},
        {-5, 1, 7}
    });
    decomposition = new CholeskyDecompositionImpl(matrix);
    double expectedDeterminant = 162.0;
    assertEquals(expectedDeterminant, decomposition.getDeterminant(), 0.0);
  }
  
  @Test
  public void testGetSolver() {
    matrix = MatrixUtils.createRealMatrix(new double[][] {
        {2, 1, -1},
        {1, -2, 1},
        {-1, 1, -2}
    });
    decomposition = new CholeskyDecompositionImpl(matrix);
    DecompositionSolver solver = decomposition.getSolver();
    double[] b = {3, -1, 0};
    double[] expectedSolution = {1, -1, 1};
    assertArrayEquals(expectedSolution, solver.solve(b), 0.0);
  }
  
  @Test(expected = NonSquareMatrixException.class)
  public void testNonSquareMatrixException() {
    matrix = MatrixUtils.createRealMatrix(new double[][] {
        {1, 2, 3},
        {2, 5, 8}
    });
    decomposition = new CholeskyDecompositionImpl(matrix);
  }
  
  @Test(expected = NotSymmetricMatrixException.class)
  public void testNotSymmetricMatrixException() {
    matrix = MatrixUtils.createRealMatrix(new double[][] {
        {1, 2, 3},
        {4, 5, 6},
        {7, 8, 9}
    });
    decomposition = new CholeskyDecompositionImpl(matrix);
  }
  
  @Test(expected = NotPositiveDefiniteMatrixException.class)
  public void testNotPositiveDefiniteMatrixException() {
    matrix = MatrixUtils.createRealMatrix(new double[][] {
        {-1, 2},
        {2, -3}
    });
    decomposition = new CholeskyDecompositionImpl(matrix);
  }
  
}