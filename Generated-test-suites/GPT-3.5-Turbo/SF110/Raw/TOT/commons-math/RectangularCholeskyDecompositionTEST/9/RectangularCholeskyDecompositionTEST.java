import static org.junit.Assert.*;
import org.apache.commons.math3.linear.*;
import org.junit.Before;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

  private RectangularCholeskyDecomposition decomposition;

  @Before
  public void setUp() {
    // Test matrix
    double[][] testMatrix = {
        {4, 12, -16},
        {12, 37, -43},
        {-16, -43, 98}
    };
    RealMatrix matrix = new Array2DRowRealMatrix(testMatrix);
    decomposition = new RectangularCholeskyDecomposition(matrix);
  }

  @Test
  public void testGetRootMatrix() {
    RealMatrix expectedMatrix = new Array2DRowRealMatrix(new double[][] {
        {2, 0, 0},
        {6, 1, 0},
        {-8, 5, 3}
    });

    assertTrue(decomposition.getRootMatrix().equals(expectedMatrix));
  }

  @Test
  public void testGetRank() {
    int expectedRank = 3;
    assertEquals(expectedRank, decomposition.getRank());
  }

  @Test(expected = NonPositiveDefiniteMatrixException.class)
  public void testNonPositiveDefiniteMatrixException() {
    // Test matrix with negative eigenvalue
    double[][] testMatrix = {
        {2, 0, 0},
        {0, -3, 0},
        {0, 0, 5}
    };
    RealMatrix matrix = new Array2DRowRealMatrix(testMatrix);
    new RectangularCholeskyDecomposition(matrix);
  }

  @Test
  public void testZeroMatrix() {
    // Test matrix with all zeros
    double[][] testMatrix = {
        {0, 0, 0},
        {0, 0, 0},
        {0, 0, 0}
    };
    RealMatrix matrix = new Array2DRowRealMatrix(testMatrix);
    RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
    RealMatrix expectedMatrix = new Array2DRowRealMatrix(new double[][] {
        {0, 0, 0},
        {0, 0, 0},
        {0, 0, 0}
    });
    assertTrue(decomposition.getRootMatrix().equals(expectedMatrix));
  }

  @Test(expected = NullPointerException.class)
  public void testNullMatrix() {
    RealMatrix matrix = null;
    new RectangularCholeskyDecomposition(matrix);
  }

}