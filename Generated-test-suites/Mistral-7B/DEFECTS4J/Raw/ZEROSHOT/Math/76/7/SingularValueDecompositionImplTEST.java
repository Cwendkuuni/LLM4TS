import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.EigenvalueDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SingularValueDecompositionImpl;
import org.apache.commons.math3.linear.Solver;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.special.Epsilon;
import org.junit.Test;
import static org.junit.Assert.*;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImpl() throws Exception {
        double[][] input = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(input, false);
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);

        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        RealMatrix ut = svd.getUT();
        RealMatrix vt = svd.getVT();

        // Check U
        RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{{0.54496849, 0.81006421, -0.13220555},
                {-0.81006421, 0.54496849, 0.13220555}, {0.13220555, -0.13220555, 0.74175132}});
        assertEquals("U incorrect.", expectedU, u);

        // Check U^T
        RealMatrix expectedUt = new Array2DRowRealMatrix(new double[][]{{0.54496849, -0.81006421, 0.13220555},
                {0.81006421, 0.54496849, -0.13220555}, {-0.13220555, 0.13220555, 0.74175132}});
        assertEquals("U^T incorrect.", expectedUt, ut);

        // Check S
        double[] expectedSingularValues = {1.03162278, 1.41614685, 2.23606798};
        assertArrayEquals("Singular values incorrect.", expectedSingularValues, s.getData(), 0.0);

        // Check V
        RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{{0.64280821, -0.75680158, 0.13220555},
                {-0.75680158, 0.64280821, -0.13220555}, {0.13220555, 0.13220555, 0.94868329}});
        assertEquals("V incorrect.", expectedV, v);

        // Check V^T
        RealMatrix expectedVt = new Array2DRowRealMatrix(new double[][]{{0.64280821, -0.75680158, 0.13220555},
                {-0.75680158, 0.64280821, 0.13220555}, {0.13220555, 0.13220555, 0.94868329}});
        assertEquals("V^T incorrect.", expectedVt, vt);

        // Check getSingularValues()
        double[] singularValues = svd.getSingularValues();
        assertArrayEquals("getSingularValues() incorrect.", expectedSingularValues, singularValues, 0.0);

        // Check getRank()
        assertEquals("getRank() incorrect.", 3, svd.getRank());

        // Check getNorm()
        assertEquals("getNorm() incorrect.", 1.03162278, svd.getNorm(), Epsilon.DOUBLE_EPSILON);

        // Check getConditionNumber()
        assertEquals("getConditionNumber() incorrect.", 2.23606798 / 1.03162278, svd.getConditionNumber(), Epsilon.DOUBLE_EPSILON);

        // Check getCovariance()
        RealMatrix covariance = svd.getCovariance(1.0);
        RealMatrix expectedCovariance = new Array2DRowRealMatrix(new double[][]{{0.23849623, -0.13219815, 0.13219815},
                {-0.13219815, 0.23849623, 0.13219815}, {0.13219815, 0.13219815, 0.23849623}});
        assertEquals("getCovariance() incorrect.", expectedCovariance, covariance);

        // Check getSolver()
        Solver solver = svd.getSolver();
        RealVector b = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector x = solver.solve(b);
        RealVector expectedX = new ArrayRealVector(new double[]{-0.03745592, -0.14155038, 0.99999997});
        assertEquals("Solver solution incorrect.", expectedX, x);

        // Test non-singular matrix
        double[][] input2 = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        RealMatrix matrix2 = new Array2DRowRealMatrix(input2, false);
        SingularValueDecompositionImpl svd2 = new SingularValueDecompositionImpl(matrix2, 2);
        Solver solver2 = svd2.getSolver();
        RealMatrix b2 = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        RealMatrix x2 = solver2.solve(b2);
        assertEquals("Non-singular matrix solution incorrect.", MatrixUtils.createIdentityMatrix(2), x2);

        // Test singular matrix
        double[][] input3 = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 0.0}};
        RealMatrix matrix3 = new Array2DRowRealMatrix(input3, false);
        SingularValueDecompositionImpl svd3 = new SingularValueDecompositionImpl(matrix3, 3);
        Solver solver3 = svd3.getSolver();
        RealMatrix b3 = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}});
        RealMatrix x3 = solver3.solve(b3);
        // Singular value decomposition of a singular matrix does not uniquely determine X
        RealMatrix x3Expected1 = new Array2DRowRealMatrix(new double[][]{{0.0, 0.0, 1.0}, {0.0, 0.0, 1.0}});
        RealMatrix x3Expected2 = new Array2DRowRealMatrix(new double[][]{{1.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0},
                {1.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0}});
        assertTrue("Solver solution could be one of the following.",
                x3.equals(x3Expected1) || x3.equals(x3Expected2));
    }

}