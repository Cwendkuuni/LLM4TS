import org.apache.commons.math.linear.CholeskyDecompositionImpl;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealVector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

  private CholeskyDecompositionImpl choleskyDecomposition;

  @Before
  public void setUp() {
    // create a symmetric positive-definite matrix for testing
    double[][] matrixData = {
        {4, 12, -16},
        {12, 37, -43},
        {-16, -43, 98}
    };
    RealMatrix matrix = new RealMatrixImpl(matrixData);
    choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
  }

  @Test
  public void testGetL() {
    RealMatrix L = choleskyDecomposition.getL();

    double[][] expectedData = {
        {2, 0, 0},
        {6, 1, 0},
        {-8, 5, 3}
    };

    Assert.assertArrayEquals(expectedData, L.getData());
  }

  @Test
  public void testGetLT() {
    RealMatrix LT = choleskyDecomposition.getLT();

    double[][] expectedData = {
        {2, 6, -8},
        {0, 1, 5},
        {0, 0, 3}
    };

    Assert.assertArrayEquals(expectedData, LT.getData());
  }

  @Test
  public void testGetDeterminant() {
    double determinant = choleskyDecomposition.getDeterminant();

    double expectedDeterminant = 36;

    Assert.assertEquals(expectedDeterminant, determinant, 0.0001);
  }

  @Test
  public void testSolver() {
    double[] b = {3, 4, 5};
    RealVector expectedSolution = new RealVectorImpl(new double[]{1, 1, 1});

    RealVector solution = choleskyDecomposition.getSolver().solve(b);

    Assert.assertArrayEquals(expectedSolution.toArray(), solution.toArray(), 0.0001);
  }

  @Test
  public void testIsNonSingular() {
    boolean isNonSingular = choleskyDecomposition.getSolver().isNonSingular();

    Assert.assertTrue(isNonSingular);
  }

  @Test
  public void testSolve() {
    double[] b = {3, 4, 5};
    RealVector expectedSolution = new RealVectorImpl(new double[]{1, 1, 1});

    RealVector solution = choleskyDecomposition.getSolver().solve(b);

    Assert.assertArrayEquals(expectedSolution.toArray(), solution.toArray(), 0.0001);
  }

  @Test
  public void testGetInverse() {
    RealMatrix inverse = choleskyDecomposition.getSolver().getInverse();

    double[][] expectedData = {
        {0.25, -0.5, 0.125},
        {-0.5, 1.0, -0.25},
        {0.125, -0.25, 0.15625}
    };

    Assert.assertArrayEquals(expectedData, inverse.getData());
  }

}