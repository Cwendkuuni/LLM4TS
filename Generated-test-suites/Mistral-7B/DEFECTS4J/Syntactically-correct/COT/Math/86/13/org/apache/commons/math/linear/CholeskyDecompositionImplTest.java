import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CholeskyDecompositionImplTest {

 @Test
 public void testDefaultConstructor() {
 CholeskyDecompositionImpl decomposition;
 try {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
 decomposition = new CholeskyDecompositionImpl(matrix);
 } catch (Exception e) {
 // expected exception
 }
 assertEquals(1.0, decomposition.getDeterminant(), 1.0e-10);
 }

 @Test
 public void testConstructorWithMatrix() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5, 0.5}, {0.5, 1.0, 0.5}, {0.5, 0.5, 1.0}});
 CholeskyDecompositionImpl decomposition;
 try {
 decomposition = new CholeskyDecompositionImpl(matrix, CholeskyDecompositionImpl.DEFAULT_RELATIVE_SYMMETRY_THRESHOLD,
 CholeskyDecompositionImpl.DEFAULT_ABSOLUTE_POSITIVITY_THRESHOLD);
 } catch (Exception e) {
 // expected exception
 }
 RealMatrix l = decomposition.getL();
 RealMatrix lt = decomposition.getLT();
 assertNotNull(l);
 assertNotNull(lt);
 assertEquals(matrix, lt.multiply(l));
 }

 @Test(expected = NotPositiveDefiniteMatrixException.class)
 public void testConstructorWithNotPositiveDefiniteMatrix() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.5, -1.0}});
 CholeskyDecompositionImpl decomposition;
 try {
 decomposition = new CholeskyDecompositionImpl(matrix, CholeskyDecompositionImpl.DEFAULT_RELATIVE_SYMMETRY_THRESHOLD,
 CholeskyDecompositionImpl.DEFAULT_ABSOLUTE_POSITIVITY_THRESHOLD);
 } catch (Exception e) {
 // expected exception
 }
 }

 @Test(expected = NotSymmetricMatrixException.class)
 public void testConstructorWithNotSymmetricMatrix() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5}, {0.5, 1.5}});
 CholeskyDecompositionImpl decomposition;
 try {
 decomposition = new CholeskyDecompositionImpl(matrix, CholeskyDecompositionImpl.DEFAULT_RELATIVE_SYMMETRY_THRESHOLD,
 CholeskyDecompositionImpl.DEFAULT_ABSOLUTE_POSITIVITY_THRESHOLD);
 } catch (Exception e) {
 // expected exception
 }
 }

 @Test
 public void testGetL() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5, 0.5}, {0.5, 1.0, 0.5}, {0.5, 0.5, 1.0}});
 CholeskyDecompositionImpl decomposition;
 try {
 decomposition = new CholeskyDecompositionImpl(matrix, CholeskyDecompositionImpl.DEFAULT_RELATIVE_SYMMETRY_THRESHOLD,
 CholeskyDecompositionImpl.DEFAULT_ABSOLUTE_POSITIVITY_THRESHOLD);
 } catch (Exception e) {
 // expected exception
 }
 RealMatrix l = decomposition.getL();
 assertNotNull(l);
 }

 @Test
 public void testGetLT() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5, 0.5}, {0.5, 1.0, 0.5}, {0.5, 0.5, 1.0}});
 CholeskyDecompositionImpl decomposition;
 try {
 decomposition = new CholeskyDecompositionImpl(matrix, CholeskyDecompositionImpl.DEFAULT_RELATIVE_SYMMETRY_THRESHOLD,
 CholeskyDecompositionImpl.DEFAULT_ABSOLUTE_POSITIVITY_THRESHOLD);
 } catch (Exception e) {
 // expected exception
 }
 RealMatrix lt = decomposition.getLT();
 assertNotNull(lt);
 }

 @Test
 public void testGetDeterminant() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5, 0.5}, {0.5, 1.0, 0.5}, {0.5, 0.5, 1.0}});
 CholeskyDecompositionImpl decomposition;
 try {
 decomposition = new CholeskyDecompositionImpl(matrix, CholeskyDecompositionImpl.DEFAULT_RELATIVE_SYMMETRY_THRESHOLD,
 CholeskyDecompositionImpl.DEFAULT_ABSOLUTE_POSITIVITY_THRESHOLD);
 } catch (Exception e) {
 // expected exception
 }
 assertEquals(1.0, decomposition.getDeterminant(), 1.0e-10);
 }

 @Test
 public void testGetSolver() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5, 0.5}, {0.5, 1.0, 0.5}, {0.5, 0.5, 1.0}});
 CholeskyDecompositionImpl decomposition;
 try {
 decomposition = new CholeskyDecompositionImpl(matrix, CholeskyDecompositionImpl.DEFAULT_RELATIVE_SYMMETRY_THRESHOLD,
 CholeskyDecompositionImpl.DEFAULT_ABSOLUTE_POSITIVITY_THRESHOLD);
 } catch (Exception e) {
 // expected exception
 }
 DecompositionSolver solver = decomposition.getSolver();
 assertNotNull(solver);
 }

 @Test
 public void testSolverWithDenseVector() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5, 0.5}, {0.5, 1.0, 0.5}, {0.5, 0.5, 1.0}});
 CholeskyDecompositionImpl decomposition;
 try {
 decomposition = new CholeskyDecompositionImpl(matrix, CholeskyDecompositionImpl.DEFAULT_RELATIVE_SYMMETRY_THRESHOLD,
 CholeskyDecompositionImpl.DEFAULT_ABSOLUTE_POSITIVITY_THRESHOLD);
 } catch (Exception e) {
 // expected exception
 }
 RealVector b = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
 RealVector x = decomposition.getSolver().solve(b);
 assertArrayEquals(new double[]{1.5, 0.0, 0.0}, x.toArray(), 1.0e-10);
 }

 @Test
 public void testSolverWithSparseVector() {
 RealMatrix matrix = new SparseMatrix(new int[][]{
 {0, 0, 0}, {0, 1, 1}, {0, 1, 2}, {1, 0, 1}, {1, 1, 2}, {1, 1, 2}
 }, new double[][]{
 {1.0, 0.5, 0.0}, {0.5, 1.0, 0.5}, {0.0, 0.5, 1.0}
 });
 CholeskyDecompositionImpl decomposition;
 try {
 decomposition = new CholeskyDecompositionImpl(matrix, CholeskyDecompositionImpl.DEFAULT_RELATIVE_SYMMETRY_THRESHOLD,
 CholeskyDecompositionImpl.DEFAULT_ABSOLUTE_POSITIVITY_THRESHOLD);
 } catch (Exception e) {
 // expected exception
 }
 RealVector b = new SparseRealVector(new int[]{0, 1, 2}, new double[]{1.0, 2.0, 3.0});
 RealVector x = decomposition.getSolver().solve(b);
 assertArrayEquals(new double[]{1.5, 0.0, 0.0}, x.toArray(), 1.0e-10);
 }

 @Test
 public void testSolverWithDenseMatrix() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5, 0.5}, {0.5, 1.0, 0.5}, {0.5, 0.5, 1.0}});
 CholeskyDecompositionImpl decomposition;
 try {
 decomposition = new CholeskyDecompositionImpl(matrix, CholeskyDecompositionImpl.DEFAULT_RELATIVE_SYMMETRY_THRESHOLD,
 CholeskyDecompositionImpl.DEFAULT_ABSOLUTE_POSITIVITY_THRESHOLD);
 } catch (Exception e) {
 // expected exception
 }
 RealMatrix b = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
 RealMatrix x = decomposition.getSolver().solve(b);
 assertEquals(MatrixUtils.createRealMatrix(new double[][]{{2.5, -0.5}, {-1.0, 0.5}}), x);
 }

 @Test
 public void testSolverWithSparseMatrix() {
 RealMatrix matrix = new SparseMatrix(new int[][]{
 {0, 0, 0}, {0, 1, 1}, {0, 1, 2}, {1, 0, 1}, {1, 1, 2}, {1, 1, 2}
 }, new double[][]{
 {1.0, 0.5, 0.0}, {0.5, 1.0, 0.5}, {0.0, 0.5, 1.0}
 });
 CholeskyDecompositionImpl decomposition;
 try {
 decomposition = new CholeskyDecompositionImpl(matrix, CholeskyDecompositionImpl.DEFAULT_RELATIVE_SYMMETRY_THRESHOLD,
 CholeskyDecompositionImpl.DEFAULT_ABSOLUTE_POSITIVITY_THRESHOLD);
 } catch (Exception e) {
 // expected exception
 }
 RealMatrix b = new SparseMatrix(new int[][]{
 {0, 1}, {1, 1}, {1, 1}
 }, new double[][]{
 {1.0, 2.0}, {3.0, 4.0}
 });
 RealMatrix x = decomposition.getSolver().solve(b);
 assertEquals(MatrixUtils.createRealMatrix(new double[][]{{1.5, -0.5}, {0.0, 0.5}}), x);
 }

 @Test
 public void testInverse() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5, 0.5}, {0.5, 1.0, 0.5}, {0.5, 0.5, 1.0}});
 CholeskyDecompositionImpl decomposition;
 try {
 decomposition = new CholeskyDecompositionImpl(matrix, CholeskyDecompositionImpl.DEFAULT_RELATIVE_SYMMETRY_THRESHOLD,
 CholeskyDecompositionImpl.DEFAULT_ABSOLUTE_POSITIVITY_THRESHOLD);
 } catch (Exception e) {
 // expected exception
 }
 RealMatrix inverse = decomposition.getSolver().getInverse();
 assertEquals(MatrixUtils.createRealMatrix(new double[][]{{2.333333333333334, -0.3333333333333335, 0.0},
 {-0.3333333333333335, 1.333333333333334, -0.3333333333333335},
 {0.0, -0.3333333333333335, 1.0}}), inverse);
 }

}