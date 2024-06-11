import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SingularValueDecompositionImpl;
import org.apache.commons.math3.linear.TransformerFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests {@link SingularValueDecompositionImpl} class.
 */
public class SingularValueDecompositionImplTest {

    private RealMatrix matrix;
    private SingularValueDecomposition<RealMatrix> svd;

    @Before
    public void setUp() throws Exception {
        matrix = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0, 3.0},
            {2.0, -1.0, 4.0},
            {3.0, 4.0, 5.0}
        });
        svd = new SingularValueDecompositionImpl(matrix);
    }

    /**
     * Test getU() method.
     */
    @Test
    public void testGetU() throws Exception {
        RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{
            {0.4491652, -0.5612624, 0.5724193},
            {-0.5264852, 0.4283757, -0.5180942},
            {0.6281359, 0.3554225, 0.4176284}
        });
        RealMatrix actualU = svd.getU();
        assertNotNull(actualU);
        assertEquals(expectedU, actualU);
    }

    /**
     * Test getUT() method.
     */
    @Test
    public void testGetUT() throws Exception {
        RealMatrix expectedUT = new Array2DRowRealMatrix(new double[][]{
            {0.4491652, -0.5264852, 0.6281359},
            {-0.5612624, 0.4283757, 0.3554225},
            {0.5724193, -0.5180942, 0.4176284}
        });
        RealMatrix actualUT = svd.getUT();
        assertNotNull(actualUT);
        assertEquals(expectedUT, actualUT);
    }

    /**
     * Test getS() method.
     */
    @Test
    public void testGetS() throws Exception {
        double[] expectedSingularValues = {5.1833353, 3.5479481, 0.0};
        double[] actualSingularValues = svd.getSingularValues();
        assertNotNull(actualSingularValues);
        assertEquals(expectedSingularValues, actualSingularValues);
    }

    /**
     * Test getV() method.
     */
    @Test
    public void testGetV() throws Exception {
        RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{
            {0.4082483, 0.7840157, -0.4225991},
            {-0.7417374, 0.1245856, 0.532886},
            {0.4472532, -0.1819417, 0.7064013}
        });
        RealMatrix actualV = svd.getV();
        assertNotNull(actualV);
        assertEquals(expectedV, actualV);
    }

    /**
     * Test getVT() method.
     */
    @Test
    public void testGetVT() throws Exception {
        RealMatrix expectedVT = new Array2DRowRealMatrix(new double[][]{
            {0.4082483, -0.7417374, 0.4472532},
            {0.7840157, 0.1245856, -0.1819417},
            {-0.4225991, 0.532886, 0.7064013}
        });
        RealMatrix actualVT = svd.getVT();
        assertNotNull(actualVT);
        assertEquals(expectedVT, actualVT);
    }

    /**
     * Test getCovariance() method.
     */
    @Test
    public void testGetCovariance() throws Exception {
        double minSingularValue = 1.0;
        RealMatrix actualCovariance = svd.getCovariance(minSingularValue);
        double[][] expectedCovarianceData = new double[][]{
            {0.0235807, 0.0139529, 0.0},
            {0.0139529, 0.0107825, 0.0},
            {0.0, 0.0, 0.0}
        };
        RealMatrix expectedCovariance = new Array2DRowRealMatrix(expectedCovarianceData, false);
        assertNotNull(actualCovariance);
        assertEquals(expectedCovariance, actualCovariance);
    }

    /**
     * Test getNorm() method.
     */
    @Test
    public void testGetNorm() throws Exception {
        assertEquals(3.5479481, svd.getNorm(), 1e-8);
    }

    /**
     * Test getConditionNumber() method.
     */
    @Test
    public void testGetConditionNumber() throws Exception {
        assertEquals(12.0, svd.getConditionNumber(), 1e-8);
    }

    /**
     * Test getRank() method.
     */
    @Test
    public void testGetRank() throws Exception {
        assertEquals(3, svd.getRank());
    }

    /**
     * Test getSolver() method.
     */
    @Test
    public void testGetSolver() throws Exception {
        DecompositionSolver solver = svd.getSolver();
        RealMatrix b = new Array2DRowRealMatrix(new double[][]{
            {1.0, 2.0},
            {3.0, 4.0}
        });
        RealVector expectedSolution = new ArrayRealVector(new double[]{0.3350517, 0.9025422});
        RealVector actualSolution = solver.solve(b);
        assertNotNull(actualSolution);
        assertEquals(expectedSolution, actualSolution);
    }

}