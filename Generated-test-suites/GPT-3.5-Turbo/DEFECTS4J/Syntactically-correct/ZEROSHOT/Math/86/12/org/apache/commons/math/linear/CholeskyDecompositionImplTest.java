import org.junit.Test;
import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {

    private static final double EPSILON = 1e-10;

    @Test
    public void testGetL() {
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix expectedL = MatrixUtils.createRealMatrix(new double[][]{{2, 0, 0}, {6, 1, 0}, {-8, 5, 3}});
        RealMatrix actualL = decomposition.getL();
        assertTrue(actualL.equals(expectedL, EPSILON));
    }

    @Test
    public void testGetLT() {
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix expectedLT = MatrixUtils.createRealMatrix(new double[][]{{2, 6, -8}, {0, 1, 5}, {0, 0, 3}});
        RealMatrix actualLT = decomposition.getLT();
        assertTrue(actualLT.equals(expectedLT, EPSILON));
    }

    @Test
    public void testGetDeterminant() {
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        double expectedDeterminant = 76.0;
        double actualDeterminant = decomposition.getDeterminant();
        assertEquals(expectedDeterminant, actualDeterminant, EPSILON);
    }

    @Test
    public void testGetSolver() {
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = decomposition.getSolver();
        double[] b = {1, 2, 3};
        double[] expectedSolution = {-0.5, 0.75, 0.5};
        double[] actualSolution = solver.solve(b);
        assertArrayEquals(expectedSolution, actualSolution, EPSILON);
    }

}