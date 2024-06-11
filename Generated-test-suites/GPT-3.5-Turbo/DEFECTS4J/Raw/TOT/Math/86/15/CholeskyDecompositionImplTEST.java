import org.apache.commons.math.linear.DecompositionSolver;
import org.apache.commons.math.linear.MatrixUtils;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealMatrixImpl;
import org.apache.commons.math.linear.RealVector;
import org.apache.commons.math.linear.RealVectorImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

    private CholeskyDecomposition choleskyDecomposition;

    @Before
    public void setUp() {
        double[][] data = {{25, 15, -5}, {15, 18, 0}, {-5, 0, 11}};
        RealMatrix matrix = new RealMatrixImpl(data);
        choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
    }

    @Test
    public void testGetL() {
        double[][] expectedData = {{5, 0, 0}, {3, 3, 0}, {-1, 1, 3}};
        RealMatrix expectedL = new RealMatrixImpl(expectedData);
        RealMatrix actualL = choleskyDecomposition.getL();
        Assert.assertTrue(expectedL.equals(actualL));
    }

    @Test
    public void testGetLT() {
        double[][] expectedData = {{5, 3, -1}, {0, 3, 1}, {0, 0, 3}};
        RealMatrix expectedLT = new RealMatrixImpl(expectedData);
        RealMatrix actualLT = choleskyDecomposition.getLT();
        Assert.assertTrue(expectedLT.equals(actualLT));
    }

    @Test
    public void testGetDeterminant() {
        double expectedDeterminant = 495.0;
        double actualDeterminant = choleskyDecomposition.getDeterminant();
        Assert.assertEquals(expectedDeterminant, actualDeterminant, 0.0);
    }

    @Test
    public void testGetSolver() {
        DecompositionSolver solver = choleskyDecomposition.getSolver();
        Assert.assertNotNull(solver);
    }

    @Test
    public void testIsNonSingular() {
        boolean expectedNonSingular = true;
        boolean actualNonSingular = choleskyDecomposition.getSolver().isNonSingular();
        Assert.assertEquals(expectedNonSingular, actualNonSingular);
    }

    @Test
    public void testSolveVector() {
        double[] b = {1, 2, 3};
        RealVector expectedX = new RealVectorImpl(new double[]{-0.2, 0.3, 1});
        RealVector actualX = choleskyDecomposition.getSolver().solve(new RealVectorImpl(b));
        Assert.assertTrue(expectedX.equals(actualX, 1e-10));
    }

    @Test
    public void testSolveMatrix() {
        double[][] bData = {{1, 2}, {3, 4}, {5, 6}};
        RealMatrix b = new RealMatrixImpl(bData);
        double[][] expectedXData = {{-0.2, 0.3}, {0.7, -0.8}, {1, -1}};
        RealMatrix expectedX = new RealMatrixImpl(expectedXData);
        RealMatrix actualX = choleskyDecomposition.getSolver().solve(b);
        Assert.assertTrue(expectedX.equals(actualX, 1e-10));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveMismatchedMatrix() {
        double[][] bData = {{1, 2, 3}, {4, 5, 6}};
        RealMatrix b = new RealMatrixImpl(bData);
        choleskyDecomposition.getSolver().solve(b);
    }

}