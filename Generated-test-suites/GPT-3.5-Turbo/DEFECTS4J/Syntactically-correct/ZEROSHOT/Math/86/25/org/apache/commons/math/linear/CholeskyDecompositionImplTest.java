import org.apache.commons.math.linear.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

    private CholeskyDecompositionImpl cholesky;

    @Before
    public void setUp() {
        double[][] testData = {
            {4, 12, -16},
            {12, 37, -43},
            {-16, -43, 98}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        cholesky = new CholeskyDecompositionImpl(matrix);
    }

    @Test
    public void testGetL() {
        double[][] expectedData = {
            {2, 0, 0},
            {6, 1, 0},
            {-8, 5, 3}
        };
        RealMatrix expected = MatrixUtils.createRealMatrix(expectedData);
        RealMatrix actual = cholesky.getL();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLT() {
        double[][] expectedData = {
            {2, 6, -8},
            {0, 1, 5},
            {0, 0, 3}
        };
        RealMatrix expected = MatrixUtils.createRealMatrix(expectedData);
        RealMatrix actual = cholesky.getLT();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDeterminant() {
        double expected = 828;
        double actual = cholesky.getDeterminant();
        assertEquals(expected, actual, 1e-10);
    }

    @Test
    public void testGetSolver() {
        DecompositionSolver solver = cholesky.getSolver();

        double[] b = {1, 2, 3};
        double[] expected = {0.5, -0.5, 1};
        double[] actual = solver.solve(b);
        assertArrayEquals(expected, actual, 1e-10);

        RealVector bVector = new ArrayRealVector(b);
        RealVector expectedVector = new ArrayRealVector(expected);
        RealVector actualVector = solver.solve(bVector);
        assertEquals(expectedVector, actualVector);

        RealMatrix bMatrix = MatrixUtils.createColumnRealMatrix(b);
        RealMatrix expectedMatrix = MatrixUtils.createColumnRealMatrix(expected);
        RealMatrix actualMatrix = solver.solve(bMatrix);
        assertEquals(expectedMatrix, actualMatrix);

    }

}