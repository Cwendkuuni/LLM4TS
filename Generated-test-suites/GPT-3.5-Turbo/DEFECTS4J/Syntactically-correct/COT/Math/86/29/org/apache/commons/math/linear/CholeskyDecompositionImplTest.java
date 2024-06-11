import org.apache.commons.math.linear.*;
import org.junit.*;
import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {

    private CholeskyDecompositionImpl cholesky;

    @Before
    public void setUp() {
        double[][] data = {
            {4, 12, -16},
            {12, 37, -43},
            {-16, -43, 98}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        cholesky = new CholeskyDecompositionImpl(matrix);
    }

    @After
    public void tearDown() {
        cholesky = null;
    }

    @Test
    public void testGetL() {
        double[][] expectedData = {
            {2.0, 0.0, 0.0},
            {6.0, 1.0, 0.0},
            {-8.0, 5.0, 3.0}
        };
        RealMatrix expected = MatrixUtils.createRealMatrix(expectedData);
        RealMatrix actual = cholesky.getL();
        assertTrue(expected.equals(actual));
    }

    @Test
    public void testGetLT() {
        double[][] expectedData = {
            {2.0, 6.0, -8.0},
            {0.0, 1.0, 5.0},
            {0.0, 0.0, 3.0}
        };
        RealMatrix expected = MatrixUtils.createRealMatrix(expectedData);
        RealMatrix actual = cholesky.getLT();
        assertTrue(expected.equals(actual));
    }

    @Test
    public void testGetDeterminant() {
        double expected = 36.0;
        double actual = cholesky.getDeterminant();
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testGetSolver() {
        DecompositionSolver solver = cholesky.getSolver();
        assertNotNull(solver);
    }

    @Test
    public void testIsNonSingular() {
        assertTrue(cholesky.getSolver().isNonSingular());
    }

    @Test
    public void testSolve() {
        double[] b = {1.0, 2.0, 3.0};
        double[] expected = {-1.0, 2.0, 2.0 / 3.0};
        double[] actual = cholesky.getSolver().solve(b);
        assertArrayEquals(expected, actual, 0.0);
    }

    @Test
    public void testSolveRealVector() {
        RealVector b = new RealVectorImpl(new double[]{1.0, 2.0, 3.0});
        RealVector expected = new RealVectorImpl(new double[]{-1.0, 2.0, 2.0 / 3.0});
        RealVector actual = cholesky.getSolver().solve(b);
        assertTrue(expected.equals(actual));
    }

    @Test
    public void testSolveRealMatrix() {
        double[][] bData = {{1.0, 4.0}, {2.0, 5.0}, {3.0, 6.0}};
        RealMatrix b = MatrixUtils.createRealMatrix(bData);
        double[][] expectedData = {{-1.0, 16.0}, {2.0, -7.0}, {2.0 / 3.0, 2.0 / 3.0}};
        RealMatrix expected = MatrixUtils.createRealMatrix(expectedData);
        RealMatrix actual = cholesky.getSolver().solve(b);
        assertTrue(expected.equals(actual));
    }

    @Test
    public void testGetInverse() {
        double[][] expectedData = {
            {0.25, 0.08333333333333333, -0.045454545454545456},
            {0.08333333333333333, 0.027777777777777776, -0.015151515151515152},
            {-0.045454545454545456, -0.015151515151515152, 0.3333333333333333}
        };
        RealMatrix expected = MatrixUtils.createRealMatrix(expectedData);
        RealMatrix actual = cholesky.getSolver().getInverse();
        assertTrue(expected.equals(actual));
    }

}