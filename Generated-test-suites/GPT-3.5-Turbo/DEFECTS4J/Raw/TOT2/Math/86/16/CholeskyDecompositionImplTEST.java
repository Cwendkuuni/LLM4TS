import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class CholeskyDecompositionImplTest {

    private CholeskyDecompositionImpl choleskyDecomp;

    @Before
    public void setUp() {
        double[][] matrixData = {
            {4.0, 12.0, -16.0},
            {12.0, 37.0, -43.0},
            {-16.0, -43.0, 98.0}
        };
        RealMatrix matrix = new RealMatrixImpl(matrixData);
        choleskyDecomp = new CholeskyDecompositionImpl(matrix);
    }

    @Test
    public void testGetL() {
        RealMatrix L = choleskyDecomp.getL();
        double[][] expectedData = {
            {2.0, 0.0, 0.0},
            {6.0, 1.0, 0.0},
            {-8.0, 5.0, 3.0}
        };
        Assert.assertArrayEquals(expectedData, L.getData());
    }

    @Test
    public void testGetLT() {
        RealMatrix LT = choleskyDecomp.getLT();
        double[][] expectedData = {
            {2.0, 6.0, -8.0},
            {0.0, 1.0, 5.0},
            {0.0, 0.0, 3.0}
        };
        Assert.assertArrayEquals(expectedData, LT.getData());
    }

    @Test
    public void testGetDeterminant() {
        double determinant = choleskyDecomp.getDeterminant();
        double expectedDeterminant = 36.0;
        Assert.assertEquals(expectedDeterminant, determinant, 0.0001);
    }

    @Test
    public void testSolver() {
        DecompositionSolver solver = choleskyDecomp.getSolver();
      
        double[] b = {1.0, 2.0, 3.0};
        double[] x = solver.solve(b);
      
        double[] expectedX = {0.5, 0.0, 1.0};
        Assert.assertArrayEquals(expectedX, x, 0.0001);
    }

    // Additional tests for edge cases and error scenarios can be added here

}