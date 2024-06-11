import static org.junit.Assert.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SingularValueDecompositionImpl;
import org.apache.commons.math3.linear.Solver;
import org.apache.commons.math3.linear.linalg.SingularValueDecompositionLU;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecomposition() throws Exception {
        RealMatrix matrix = new SingularValueDecompositionLU(new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        })).getSVD();

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();

        assertEquals(u.getRowDimension(), s.getRowDimension());
        assertEquals(u.getColumnDimension(), s.getColumnDimension());
        assertEquals(u.getRowDimension(), v.getRowDimension());
        assertEquals(u.getColumnDimension(), v.getColumnDimension());

        assertArrayEquals(new double[]{1.0, 0.0, 0.0}, u.getColumnVector(0).toArray(), 0.0);
        assertArrayEquals(new double[]{0.0, 1.0, 0.0}, u.getColumnVector(1).toArray(), 0.0);
        assertArrayEquals(new double[]{0.0, 0.0, 1.0}, u.getColumnVector(2).toArray(), 0.0);

        assertArrayEquals(new double[]{Math.sqrt(14.0), 0.0, 0.0}, s.getColumnVector(0).toArray(), 0.0);
        assertArrayEquals(new double[]{0.0, Math.sqrt(29.0), 0.0}, s.getColumnVector(1).toArray(), 0.0);
        assertArrayEquals(new double[]{0.0, 0.0, Math.sqrt(81.0)}, s.getColumnVector(2).toArray(), 0.0);

        assertArrayEquals(new double[]{0.2538579548597606, 0.5266698850521147, 0.8153136735225554},
                v.getColumnVector(0).toArray(), 0.0);
        assertArrayEquals(new double[]{-0.8082813843245535, -0.1407140878167385, 0.428390552524362},
                v.getColumnVector(1).toArray(), 0.0);
        assertArrayEquals(new double[]{0.4330127018922192, -0.4551024613460712, 0.60039663658471},
                v.getColumnVector(2).toArray(), 0.0);

        RealMatrix ut = svd.getUT();
        RealMatrix st = svd.getS();
        RealMatrix vt = svd.getVT();

        assertEquals(u.getTranspose(), ut);
        assertEquals(s.getTranspose(), st);
        assertEquals(v.getTranspose(), vt);

        RealMatrix productUSt = u.multiply(st);
        RealMatrix productUtVt = ut.transpose().multiply(vt);

        assertMatrixEquals(productUSt, productUtVt);

        testSingularValueDecompositionWithException();
        testRank();
        testGetSolver();
        testGetCovariance();
        testGetNorm();
        testGetConditionNumber();
    }

    @Test
    public void testSingularValueDecompositionWithException() throws Exception {
        RealMatrix invalidMatrix = new SingularValueDecompositionLU(new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0},
                {2.0, 2.0}
        })).getSVD();

        try {
            new SingularValueDecompositionImpl(invalidMatrix);
            fail("Expected InvalidMatrixException");
        } catch (InvalidMatrixException e) {
            // expected
        }
    }

    @Test
    public void testRank() throws Exception {
        RealMatrix matrix = new SingularValueDecompositionLU(new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        })).getSVD();

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);

        int rank = svd.getRank();
        assertEquals(rank, 3);

        try {
            svd = new SingularValueDecompositionImpl(new Array2DRowRealMatrix(new double[][]{
                    {1.0, 2.0, 3.0},
                    {4.0, 5.0, 6.0}
            }));
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @Test
    public void testGetSolver() throws Exception {
        RealMatrix matrix = new SingularValueDecompositionLU(new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        })).getSVD();

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);

        Solver solver = svd.getSolver();

        RealVector b = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0},
                {3.0, 4.0}
        }).getColumnVector(0);

        RealVector x = solver.solve(b);

        assertArrayEquals(new double[]{1.433188572877626, 1.839798258590596}, x.toArray(), 0.0);

        RealMatrix Ax = svd.getUT().multiply(svd.getS()).multiply(svd.getV());
        RealMatrix Bx = svd.getUT().multiply(b).add(RealMatrix.IDENTITY.multiply(new RealVector[]{b, b}));

        double tolerance = 1e-10;

        RealMatrix error = Ax.subtract(Bx);

        double maxError = 0.0;

        for (int i = 0; i < error.getRowDimension(); ++i) {
            for (int j = 0; j < error.getColumnDimension(); ++j) {
                double currentError = error.getEntry(i, j);
                if (Math.abs(currentError) > maxError) {
                    maxError = Math.abs(currentError);
                }
            }
        }

        assertTrue("Maximum error: " + maxError + " is greater than tolerance: " + tolerance,
                maxError <= tolerance);
    }

    @Test
    public void testGetCovariance() throws Exception {
        RealMatrix matrix = new SingularValueDecompositionLU(new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        })).getSVD();

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);

        double minSingularValue = svd.getSingularValues()[svd.getSingularValues().length - 1];

        RealMatrix covariance = svd.getCovariance(minSingularValue / 100.0);

        RealMatrix expectedCovariance = new Array2DRowRealMatrix(new double[][]{
                {1.265114949116938, 1.304866155965312},
                {1.304866155965312, 2.313631584104925}
        });

        assertMatrixEquals(expectedCovariance, covariance);
    }

    @Test
    public void testGetNorm() throws Exception {
        RealMatrix matrix = new SingularValueDecompositionLU(new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        })).getSVD();

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);

        double norm = svd.getNorm();

        assertEquals(norm, svd.singularValues[0]);
    }

    @Test
    public void testGetConditionNumber() throws Exception {
        RealMatrix matrix = new SingularValueDecompositionLU(new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        })).getSVD();

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);

        double conditionNumber = svd.getConditionNumber();

        assertEquals(conditionNumber, svd.singularValues[0] / svd.singularValues[svd.singularValues.length - 1]);
    }

    private static void assertMatrixEquals(RealMatrix a, RealMatrix b) {
        assertEquals(a.getRowDimension(), b.getRowDimension());
        assertEquals(a.getColumnDimension(), b.getColumnDimension());

        for (int i = 0; i < a.getRowDimension(); ++i) {
            for (int j = 0; j < a.getColumnDimension(); ++j) {
                assertEquals(a.getEntry(i, j), b.getEntry(i, j), 0.0);
            }
        }
    }

    private static void assertArrayEquals(double[] expected, double[] actual, double tolerance) {
        for (int i = 0; i < expected.length; ++i) {
            assertEquals(expected[i], actual[i], tolerance);
        }
    }

}