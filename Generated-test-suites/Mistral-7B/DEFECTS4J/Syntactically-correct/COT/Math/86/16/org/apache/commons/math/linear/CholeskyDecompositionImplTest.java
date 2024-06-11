import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CholeskyDecompositionImplTest {

 private static final double[][] data = {
         {1.0, 2.0, 3.0},
         {2.0, 4.0, 5.0},
         {3.0, 5.0, 6.0}
 };

 @Test
 public void testConstructor() throws Exception {
     RealMatrix matrix = new MatrixUtils.Dense2DMatrix(data);
     CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
     assertNotNull(cholesky.getL());
     assertNotNull(cholesky.getLT());
 }

 @Test(expected = NonSquareMatrixException.class)
 public void testNonSquareMatrixException() throws Exception {
     RealMatrix matrix = new MatrixUtils.Dense2DMatrix(new double[][]{{1.0}});
     new CholeskyDecompositionImpl(matrix);
 }

 @Test(expected = NotSymmetricMatrixException.class)
 public void testNotSymmetricMatrixException() throws Exception {
     RealMatrix matrix = new MatrixUtils.Dense2DMatrix(new double[][]{{1.0, 1.1}, {1.1, 1.0}});
     new CholeskyDecompositionImpl(matrix, DEFAULT_RELATIVE_SYMMETRY_THRESHOLD, DEFAULT_ABSOLUTE_POSITIVITY_THRESHOLD);
 }

 @Test(expected = NotPositiveDefiniteMatrixException.class)
 public void testNotPositiveDefiniteMatrixException() throws Exception {
     RealMatrix matrix = MatrixUtils.createRealDiagonalMatrix(new double[]{-1.0, 4.0, 9.0});
     new CholeskyDecompositionImpl(matrix, DEFAULT_RELATIVE_SYMMETRY_THRESHOLD, DEFAULT_ABSOLUTE_POSITIVITY_THRESHOLD);
 }

 @Test
 public void testGetL() throws Exception {
     RealMatrix matrix = new MatrixUtils.Dense2DMatrix(data);
     CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
     RealMatrix expectedL = MatrixUtils.createRealDiagonalMatrix(new double[]{1.0, Math.sqrt(5.0), Math.sqrt(13.0)});
     assertEquals(expectedL, cholesky.getL());
 }

 @Test
 public void testGetLT() throws Exception {
     RealMatrix matrix = new MatrixUtils.Dense2DMatrix(data);
     CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
     RealMatrix expectedLT = MatrixUtils.createRealMatrix(new double[][]{{1.0 / Math.sqrt(1.0), 2.0 / Math.sqrt(1.0), 3.0 / Math.sqrt(1.0)},
             {2.0 / Math.sqrt(5.0), 1.0, 1.5},
             {3.0 / Math.sqrt(13.0), 1.5, 1.0}});
     assertEquals(expectedLT, cholesky.getLT());
 }

 @Test
 public void testGetDeterminant() throws Exception {
     RealMatrix matrix = new MatrixUtils.Dense2DMatrix(data);
     CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
     assertEquals(2.0 * Math.sqrt(13.0), cholesky.getDeterminant(), 1e-14);
 }

 @Test
 public void testSolver() throws Exception {
     RealMatrix matrix = new MatrixUtils.Dense2DMatrix(data);
     CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
     double[] vector = {1.0, 2.0, 3.0};
     double[] solution = {1.0, (1.0 - Math.sqrt(5.0)) / 2.0, (1.0 - 3.0 * Math.sqrt(13.0)) / 2.0};
     assertEquals(solution, cholesky.getSolver().solve(vector), 1e-14);
 }

}