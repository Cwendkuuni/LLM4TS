import static org.junit.Assert.*;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

  @Test
  public void testGetRootMatrix() {
    double[][] data = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
    RealMatrix matrix = new Array2DRowRealMatrix(data);
    double small = 1e-9;
    RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
    
    RealMatrix expected = new Array2DRowRealMatrix(data);
    RealMatrix result = decomp.getRootMatrix();
    
    assertEquals(expected, result);
  }

  @Test
  public void testGetRank() {
    double[][] data = {{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
    RealMatrix matrix = new Array2DRowRealMatrix(data);
    double small = 1e-9;
    RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix, small);
    
    int expected = 3;
    int result = decomp.getRank();
    
    assertEquals(expected, result);
  }
}