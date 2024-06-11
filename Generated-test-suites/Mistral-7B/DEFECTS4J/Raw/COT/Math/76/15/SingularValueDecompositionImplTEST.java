import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SingularValueDecompositionImpl;
import org.apache.commons.math3.linear.Solver;
import org.apache.commons.math3.linear.UtilEigendecompositions;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Before;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    private RealMatrix matrix;
    private SingularValueDecompositionImpl svd;

    @Before
    public void setUp() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        matrix = new Array2DRowRealMatrix(data, false);
    }

    @Test
    public void testSingularValueDecompositionImplConstructorWithMatrix() throws Exception {
        svd = new SingularValueDecompositionImpl(matrix);
        assertEquals(3, svd.getRank());
    }

    @Test
    public void testSingularValueDecompositionImplConstructorWithMaxSingularValues() throws Exception {
        svd = new SingularValueDecompositionImpl(matrix, 2);
        assertEquals(2, svd.getRank());
    }

    @Test
    public void testGetU() throws Exception {
        svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix u = svd.getU();
        RealMatrix expectedU = UtilEigendecompositions. computeEigenvectors(matrix);
        assertEquals(expectedU, u);
    }

    @Test
    public void testGetUT() throws Exception {
        svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix ut = svd.getUT();
        RealMatrix expectedUT = expectedU.transpose();
        assertEquals(expectedUT, ut);
    }

    @Test
    public void testGetS() throws Exception {
        svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix s = svd.getS();
        double[] singularValues = svd.getSingularValues();
        double[] expectedSingularValues = {Math.sqrt(14.0), Math.sqrt(10.5), 0.0};
        assertArrayEquals(expectedSingularValues, singularValues, 0.0);
        assertEquals(3, s.getRowDimension());
        assertEquals(3, s.getColumnDimension());
        for (int i = 0; i < 3; ++i) {
            assertEquals(Math.sqrt(Math.pow(expectedSingularValues[i], 2) + Math.pow(Math.ulp(singularValues[i]), 2)),
                    s.getEntry(i, i), Math.ulp(singularValues[i]));
        }
    }

    @Test
    public void testGetSingularValues() throws Exception {
        svd = new SingularValueDecompositionImpl(matrix);
        double[] singularValues = svd.getSingularValues();
        double[] expectedSingularValues = {Math.sqrt(14.0), Math.sqrt(10.5), 0.0};
        assertArrayEquals(expectedSingularValues, singularValues, 0.0);
    }

    @Test
    public void testGetV() throws Exception {
        svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix v = svd.getV();
        RealMatrix expectedV = UtilEigendecompositions. computeEigenvectors(matrix);
        assertEquals(expectedV, v);
    }

    @Test
    public void testGetVT() throws Exception {
        svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix vt = svd.getVT();
        RealMatrix expectedVT = expectedV.transpose();
        assertEquals(expectedVT, vt);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCovarianceWithNegativeMinSingularValue() throws Exception {
        svd = new SingularValueDecompositionImpl(matrix);
        svd.getCovariance(-1.0);
    }

    @Test
    public void testGetNorm() throws Exception {
        svd = new SingularValueDecompositionImpl(matrix);
        assertEquals(Math.sqrt(14.0), svd.getNorm(), 0.0);
    }

    @Test
    public void testGetConditionNumber() throws Exception {
        svd = new SingularValueDecompositionImpl(matrix);
        assertEquals(Math.sqrt(14.0) / Math.sqrt(0.0), svd.getConditionNumber(), 0.0);
    }

    @Test
    public void testGetRank() throws Exception {
        svd = new SingularValueDecompositionImpl(matrix);
        assertEquals(3, svd.getRank());
    }

    @Test
    public void testGetSolver() throws Exception {
        svd = new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = svd.getSolver();
        assertTrue(solver instanceof Solver);
    }

    @Test
    public void testSolverSolveVector() throws Exception {
        svd = new SingularValueDecompositionImpl(matrix);
        Solver solver = svd.getSolver();
        RealVector b = new Array2DRowRealMatrix(new double[][]{{1.0}, {1.0}}).getColumnVector(0);
        RealVector x = solver.solve(b);
        double[] expectedX = {-0.0464, 0.5294};
        assertArrayEquals(expectedX, x.toArray(), 0.0);
    }

    @Test
    public void testSolverSolveMatrix() throws Exception {
        svd = new SingularValueDecompositionImpl(matrix);
        Solver solver = svd.getSolver();
        RealMatrix b = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        RealMatrix x = solver.solve(b);
        double[][] expectedX = {{1.2632, -0.5294}, {-0.5294, 0.8211}};
        assertArrayEquals(expectedX, x.getData(), 0.0);
    }

    @Test
    public void testIsNonSingular() throws Exception {
        svd = new SingularValueDecompositionImpl(matrix);
        Solver solver = svd.getSolver();
        assertTrue(solver.isNonSingular());
    }

    @Test
    public void testGetInverse() throws Exception {
        svd = new SingularValueDecompositionImpl(matrix);
        Solver solver = svd.getSolver();
        RealMatrix inverse = solver.getInverse();
        assertEquals(matrix.getRowDimension(), inverse.getRowDimension());
        assertEquals(matrix.getColumnDimension(), inverse.getColumnDimension());
        assertEquals(matrix, inverse.multiply(matrix));
    }

}