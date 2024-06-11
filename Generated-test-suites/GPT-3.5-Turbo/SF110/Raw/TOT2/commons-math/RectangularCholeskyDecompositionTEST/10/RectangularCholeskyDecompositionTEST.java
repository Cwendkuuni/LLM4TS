import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;

public class RectangularCholeskyDecompositionTest {

   @Test
   public void testConstructor() {
       RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 0}, {0, 1}});
       RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
       Assert.assertNotNull(decomposition);
   }

   @Test
   public void testGetRootMatrix() {
       RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 0}, {0, 1}});
       RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
       RealMatrix root = decomposition.getRootMatrix();
       Assert.assertEquals(1.0, root.getEntry(0, 0), 0.001);
       Assert.assertEquals(0.0, root.getEntry(0, 1), 0.001);
       Assert.assertEquals(0.0, root.getEntry(1, 0), 0.001);
       Assert.assertEquals(1.0, root.getEntry(1, 1), 0.001);
   }

   @Test
   public void testGetRank() {
       RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 0}, {0, 1}});
       RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
       int rank = decomposition.getRank();
       Assert.assertEquals(2, rank);
   }

   @Test(expected = NonPositiveDefiniteMatrixException.class)
   public void testConstructorNonPositiveDefiniteMatrixException() {
       RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{-1, 0}, {0, 1}});
       RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
   }

}