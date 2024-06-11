import org.apache.commons.math.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {

    private static final double EPSILON = 1e-10;

    @Test
    public void testGetL() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {
            { 4, 12, -16 },
            { 12, 37, -43 },
            { -16, -43, 98 }
        });
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix actualL = choleskyDecomposition.getL();
        RealMatrix expectedL = MatrixUtils.createRealMatrix(new double[][] {
            { 2, 0, 0 },
            { 6, 1, 0 },
            { -8, 5, 3 }
        });
        assertTrue(actualL.subtract(expectedL).getNorm() < EPSILON);
    }

    @Test
    public void testGetLT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {
            { 4, 12, -16 },
            { 12, 37, -43 },
            { -16, -43, 98 }
        });
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix actualLT = choleskyDecomposition.getLT();
        RealMatrix expectedLT = MatrixUtils.createRealMatrix(new double[][] {
            { 2, 6, -8 },
            { 0, 1, 5 },
            { 0, 0, 3 }
        });
        assertTrue(actualLT.subtract(expectedLT).getNorm() < EPSILON);
    }

    @Test
    public void testGetDeterminant() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {
            { 4, 12, -16 },
            { 12, 37, -43 },
            { -16, -43, 98 }
        });
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        double actualDeterminant = choleskyDecomposition.getDeterminant();
        double expectedDeterminant = 36;
        assertTrue(Math.abs(actualDeterminant - expectedDeterminant) < EPSILON);
    }

    @Test
    public void testGetSolver() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {
            { 4, 12, -16 },
            { 12, 37, -43 },
            { -16, -43, 98 }
        });
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = choleskyDecomposition.getSolver();
        RealVector b = new ArrayRealVector(new double[] { 1, 2, 3 });
        RealVector x = solver.solve(b);
        RealVector expectedX = new ArrayRealVector(new double[] { 0.25, 0.5, 1 });
        assertTrue(x.subtract(expectedX).getNorm() < EPSILON);
    }
}