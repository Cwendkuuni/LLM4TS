import org.apache.commons.math.MathRuntimeException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

    private CholeskyDecompositionImpl cholesky;

    @Before
    public void setUp() {
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        cholesky = new CholeskyDecompositionImpl(matrix);
    }

    @Test
    public void testGetL() {
        double[][] expectedData = {{2, 6, -8}, {0, 1, 1}, {0, 0, 3}};
        RealMatrix expectedL = MatrixUtils.createRealMatrix(expectedData);
        RealMatrix actualL = cholesky.getL();
        Assert.assertEquals(expectedL, actualL);
    }

    @Test
    public void testGetLT() {
        double[][] expectedData = {{2, 0, 0}, {6, 1, 0}, {-8, 1, 3}};
        RealMatrix expectedLT = MatrixUtils.createRealMatrix(expectedData);
        RealMatrix actualLT = cholesky.getLT();
        Assert.assertEquals(expectedLT, actualLT);
    }

    @Test
    public void testGetDeterminant() {
        double expectedDeterminant = 432;
        double actualDeterminant = cholesky.getDeterminant();
        Assert.assertEquals(expectedDeterminant, actualDeterminant, 0.0001);
    }

    @Test
    public void testGetSolver() {
        DecompositionSolver solver = cholesky.getSolver();
        Assert.assertNotNull(solver);
    }

    @Test
    public void testIsNonSingular() {
        boolean expectedNonSingular = true;
        boolean actualNonSingular = cholesky.isNonSingular();
        Assert.assertEquals(expectedNonSingular, actualNonSingular);
    }

    @Test
    public void testSolve() throws InvalidMatrixException {
        double[] b = {1, 2, 3};
        double[] expectedX = {1.5, -2.0, 1.0};
        double[] actualX = cholesky.solve(b);
        Assert.assertArrayEquals(expectedX, actualX, 0.0001);
    }

    @Test
    public void testSolveRealVector() throws InvalidMatrixException {
        RealVector b = new ArrayRealVector(new double[] {1, 2, 3});
        RealVector expectedX = new ArrayRealVector(new double[] {1.5, -2.0, 1.0});
        RealVector actualX = cholesky.solve(b);
        Assert.assertEquals(expectedX, actualX);
    }

    @Test
    public void testSolveRealMatrix() throws InvalidMatrixException {
        double[][] bData = {{1}, {2}, {3}};
        RealMatrix b = MatrixUtils.createRealMatrix(bData);
        double[][] expectedXData = {{1.5}, {-2.0}, {1.0}};
        RealMatrix expectedX = MatrixUtils.createRealMatrix(expectedXData);
        RealMatrix actualX = cholesky.solve(b);
        Assert.assertEquals(expectedX, actualX);
    }

    @Test
    public void testGetInverse() throws InvalidMatrixException {
        double[][] expectedInverseData =
            {{0.25, -0.17, 0.74}, {-0.17, 0.16, -0.57}, {0.74, -0.57, 3.15}};
        RealMatrix expectedInverse = MatrixUtils.createRealMatrix(expectedInverseData);
        RealMatrix actualInverse = cholesky.getInverse();
        Assert.assertEquals(expectedInverse, actualInverse);
    }

}