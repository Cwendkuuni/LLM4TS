import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SchurTransformerTest {
  
  @Test
  public void testGetP() {
    RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
    SchurTransformer transformer = new SchurTransformer(matrix);
    RealMatrix result = transformer.getP();
    RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{-0.87055, -0.49237}, {-0.49237, 0.87055}});
    assertArrayEquals(expected.getData(), result.getData());
  }
  
  @Test
  public void testGetPT() {
    RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
    SchurTransformer transformer = new SchurTransformer(matrix);
    RealMatrix result = transformer.getPT();
    RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{-0.87055, -0.49237}, {-0.49237, 0.87055}});
    assertArrayEquals(expected.getData(), result.getData());
  }
  
  @Test
  public void testGetT() {
    RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
    SchurTransformer transformer = new SchurTransformer(matrix);
    RealMatrix result = transformer.getT();
    RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{{1.75488, 1.26373}, {-0.73205, 0.24505}});
    assertArrayEquals(expected.getData(), result.getData());
  }
  
  @Test(expected = NonSquareMatrixException.class)
  public void testNonSquareMatrix() {
    RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}});
    SchurTransformer transformer = new SchurTransformer(matrix);
  }
  
  @Test
  public void testConvergenceFailed() {
    RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1}});
    try {
      SchurTransformer transformer = new SchurTransformer(matrix);
      fail("Expected MaxCountExceededException");
    } catch (MaxCountExceededException e) {
      // test passed
    }
  }
}