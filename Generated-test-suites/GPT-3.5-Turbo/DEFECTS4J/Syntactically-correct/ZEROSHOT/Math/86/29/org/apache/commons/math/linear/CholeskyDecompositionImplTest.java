import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CholeskyDecompositionImplTest {

    @Test
    public void testGetL() {
        double[][] data = {
            {4, -1, 2},
            {-1, 3, -1},
            {2, -1, 2}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        CholeskyDecompositionImpl decomp = new CholeskyDecompositionImpl(matrix);
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{
            {2.0, 0.0, 0.0},
            {-0.5, 1.36602540378, 0.0},
            {1.0, -0.73029674334, 0.4472135955}
        });
        RealMatrix actual = decomp.getL();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLT() {
        double[][] data = {
            {4, -1, 2},
            {-1, 3, -1},
            {2, -1, 2}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        CholeskyDecompositionImpl decomp = new CholeskyDecompositionImpl(matrix);
        RealMatrix expected = MatrixUtils.createRealMatrix(new double[][]{
            {2.0, -0.5, 1.0},
            {0.0, 1.36602540378, -0.73029674334},
            {0.0, 0.0, 0.4472135955}
        });
        RealMatrix actual = decomp.getLT();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDeterminant() {
        double[][] data = {
            {4, -1, 2},
            {-1, 3, -1},
            {2, -1, 2}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        CholeskyDecompositionImpl decomp = new CholeskyDecompositionImpl(matrix);
        double expected = 8.0;
        double actual = decomp.getDeterminant();
        assertEquals(expected, actual, 1e-12);
    }

    @Test
    public void testGetSolver() {
        double[][] data = {
            {4, -1, 2},
            {-1, 3, -1},
            {2, -1, 2}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        CholeskyDecompositionImpl decomp = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = decomp.getSolver();
        RealVector b = new ArrayRealVector(new double[] {1, 2, 3});
        RealVector x = solver.solve(b);
        RealVector expected = new ArrayRealVector(new double[] {0.5, 1.0, 1.5});
        assertEquals(expected, x);
    }

}