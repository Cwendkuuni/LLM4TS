import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

   private RealMatrix matrix;

   @Before
   public void setUp() {
      double[][] data = {
            { 1.0, 0.5, 0.5 },
            { 0.5, 1.0, 0.5 },
            { 0.5, 0.5, 1.0 }
      };
      matrix = MatrixUtils.createRealMatrix(data);
   }

   @Test
   public void testGetRootMatrix() {
      RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1.0e-10);
      RealMatrix rootMatrix = decomposition.getRootMatrix();

      double[][] expectedData = {
            { 1.0, 0.0 },
            { 0.5, 0.8660254038 },
            { 0.5, -0.2886751346 }
      };
      RealMatrix expected = MatrixUtils.createRealMatrix(expectedData);

      Assert.assertEquals(expected, rootMatrix);
   }

   @Test
   public void testGetRank() {
      RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1.0e-10);
      int rank = decomposition.getRank();

      Assert.assertEquals(2, rank);
   }

}