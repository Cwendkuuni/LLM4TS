import org.apache.commons.math3.linear.*;
import org.apache.commons.math3. exceptional.InvalidMatrixException;
import org.apache.commons.math3.exception.SingularValueDecompositionException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SingularValueDecompositionImplTest {

 @Test
 public void testSingularValueDecompositionImplValid() throws InvalidMatrixException, SingularValueDecompositionException {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
 SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);

 // Test getters
 assertEquals(2, svd.getRowDimension());
 assertEquals(2, svd.getColumnDimension());
 assertNotNull(svd.getCachedU());
 assertNull(svd.getCachedS());
 assertNull(svd.getCachedV());
 assertNull(svd.getCachedVt());

 // Test methods
 RealMatrix u = svd.getU();
 assertEquals(2, u.getRowDimension());
 assertEquals(2, u.getColumnDimension());
 assertEquals(1.0, u.getEntry(0, 0), 1e-14);
 assertEquals(0.707106781186548, u.getEntry(0, 1), 1e-14);
 assertEquals(-0.707106781186548, u.getEntry(1, 0), 1e-14);
 assertEquals(0.707106781186548, u.getEntry(1, 1), 1e-14);
 RealMatrix s = svd.getS();
 assertEquals(2, s.getRowDimension());
 assertEquals(2, s.getColumnDimension());
 assertEquals(3.0, s.getEntry(0, 0), 1e-14);
 assertEquals(0.0, s.getEntry(1, 0), 1e-14);
 assertEquals(1.7320508075688772, s.getEntry(1, 1), 1e-14);
 RealMatrix v = svd.getV();
 assertEquals(2, v.getRowDimension());
 assertEquals(2, v.getColumnDimension());
 assertEquals(1.0 / Math.sqrt(3.0), v.getEntry(0, 0), 1e-14);
 assertEquals(-1.0 / Math.sqrt(2.0), v.getEntry(0, 1), 1e-14);
 assertEquals(1.0 / Math.sqrt(2.0), v.getEntry(1, 0), 1e-14);
 assertEquals(1.0 / Math.sqrt(3.0), v.getEntry(1, 1), 1e-14);
 RealMatrix vt = svd.getVT();
 assertEquals(2, vt.getRowDimension());
 assertEquals(2, vt.getColumnDimension());
 assertEquals(1.0 / Math.sqrt(3.0), vt.getEntry(0, 0), 1e-14);
 assertEquals(1.0 / Math.sqrt(2.0), vt.getEntry(0, 1), 1e-14);
 assertEquals(-1.0 / Math.sqrt(2.0), vt.getEntry(1, 0), 1e-14);
 assertEquals(1.0 / Math.sqrt(3.0), vt.getEntry(1, 1), 1e-14);
 RealMatrix ut = svd.getUT();
 assertEquals(2, ut.getRowDimension());
 assertEquals(2, ut.getColumnDimension());
 assertTrue(ut.isIdentityMatrix());
 RealMatrix sD = svd.getS();
 assertEquals(2, sD.getRowDimension());
 assertEquals(2, sD.getColumnDimension());
 assertEquals(3.0, sD.getEntry(0, 0), 1e-14);
 assertEquals(0.0, sD.getEntry(1, 0), 1e-14);
 assertEquals(1.7320508075688772, sD.getEntry(1, 1), 1e-14);
 double[] singularValues = svd.getSingularValues();
 assertEquals(2, singularValues.length);
 assertEquals(3.0, singularValues[0], 1e-14);
 assertEquals(1.7320508075688772, singularValues[1], 1e-14);
 RealMatrix uT = svd.getUT();
 RealMatrix vT = svd.getVT();
 RealMatrix product = uT.multiply(vT);
 RealMatrix matrixOrigin = matrix.transpose().multiply(matrix);
 assertTrue(MatrixUtils.isEquals(product, matrixOrigin, 1e-14));
 }

 @Test(expected = InvalidMatrixException.class)
 public void testSingularValueDecompositionImplInvalidParameters() {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}});
 SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
 }

 @Test
 public void testSingularValueDecompositionImplRankDeficiency() throws InvalidMatrixException, SingularValueDecompositionException {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
 SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);

 // Test getters
 assertEquals(3, svd.getRowDimension());
 assertEquals(3, svd.getColumnDimension());
 assertNotNull(svd.getCachedU());
 assertNull(svd.getCachedS());
 assertNull(svd.getCachedV());
 assertNull(svd.getCachedVt());

 // Test methods
 RealMatrix u = svd.getU();
 assertEquals(3, u.getRowDimension());
 assertEquals(2, u.getColumnDimension());
 RealMatrix s = svd.getS();
 assertEquals(2, s.getRowDimension());
 assertEquals(2, s.getColumnDimension());
 RealMatrix v = svd.getV();
 assertEquals(3, v.getRowDimension());
 assertEquals(2, v.getColumnDimension());
 RealMatrix vT = svd.getVT();
 assertEquals(2, vT.getRowDimension());
 assertEquals(3, vT.getColumnDimension());
 RealMatrix uT = svd.getUT();
 assertEquals(2, uT.getRowDimension());
 assertEquals(3, uT.getColumnDimension());
 RealMatrix product = uT.multiply(vT);
 RealMatrix matrixOrigin = matrix.transpose().multiply(matrix);
 assertTrue(MatrixUtils.isEquals(product, matrixOrigin, 1e-14));
 }

 @Test
 public void testSingularValueDecompositionImplMaximumRank() throws InvalidMatrixException, SingularValueDecompositionException {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
 SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 3);

 // Test getters
 assertEquals(3, svd.getRowDimension());
 assertEquals(3, svd.getColumnDimension());
 assertNotNull(svd.getCachedU());
 assertNotNull(svd.getCachedS());
 assertNotNull(svd.getCachedV());
 assertNotNull(svd.getCachedVt());

 // Test methods
 RealMatrix u = svd.getU();
 assertEquals(3, u.getRowDimension());
 assertEquals(3, u.getColumnDimension());
 RealMatrix s = svd.getS();
 assertEquals(3, s.getRowDimension());
 assertEquals(3, s.getColumnDimension());
 RealMatrix v = svd.getV();
 assertEquals(3, v.getRowDimension());
 assertEquals(3, v.getColumnDimension());
 RealMatrix vT = svd.getVT();
 assertEquals(3, vT.getRowDimension());
 assertEquals(3, vT.getColumnDimension());
 RealMatrix uT = svd.getUT();
 assertEquals(3, uT.getRowDimension());
 assertEquals(3, uT.getColumnDimension());
 RealMatrix product = uT.multiply(vT);
 RealMatrix matrixOrigin = matrix.transpose().multiply(matrix);
 assertTrue(MatrixUtils.isEquals(product, matrixOrigin, 1e-14));
 }

 @Test
 public void testSingularValueDecompositionImplSingular() throws InvalidMatrixException, SingularValueDecompositionException {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 0.0}});
 SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);

 // Test getters
 assertEquals(2, svd.getRowDimension());
 assertEquals(2, svd.getColumnDimension());
 assertNotNull(svd.getCachedU());
 assertNull(svd.getCachedS());
 assertNull(svd.getCachedV());
 assertNull(svd.getCachedVt());

 // Test methods
 assertEquals(0, svd.getRank());
 assertFalse(svd.isNonSingular());
 }

 @Test
 public void testSingularValueDecompositionImplNonSingular() throws InvalidMatrixException, SingularValueDecompositionException {
 RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0}, {0.0, 1.0}});
 SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);

 // Test getters
 assertEquals(2, svd.getRowDimension());
 assertEquals(2, svd.getColumnDimension());
 assertNotNull(svd.getCachedU());
 assertNotNull(svd.getCachedS());
 assertNotNull(svd.getCachedV());
 assertNotNull(svd.getCachedVt());

 // Test methods
 assertEquals(2, svd.getRank());
 assertTrue(svd.isNonSingular());
 }

}