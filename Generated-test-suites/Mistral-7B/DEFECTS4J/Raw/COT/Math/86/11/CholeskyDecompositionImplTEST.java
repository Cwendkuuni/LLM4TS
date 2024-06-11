import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {

    @Test
    public void testConstructorWithDefaultThreshold() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(3, 3, 1.0);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = decomposition.getL();
        assertTrue(l.isSymmetric());
        assertTrue(l.isPositiveDefinite());
        RealMatrix lt = decomposition.getLT();
        RealMatrix expectedLT = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.5, 0.3}, {0.5, 1.0, 0.5}, {0.3, 0.5, 1.0}});
        assertEquals(expectedLT, lt);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new int[]{3, 2}, new double[][]{{1.0, 1.0}, {1.0, 1.0}, {1.0, 1.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testConstructorWithNonSymmetricMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new int[]{3, 3}, new double[][]{{1.0, 1.0, 1.0}, {1.0, 1.1, 1.2}, {1.0, 1.2, 1.3}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrix() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(3, 3, -1.0);
        new CholeskyDecompositionImpl(matrix);
    }

    @Test
    public void testGetDeterminant() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(3, 3, 1.0);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        double determinant = decomposition.getDeterminant();
        assertEquals(determinant, 1.0 * 1.0 * 1.0, 1e-14);
    }

    @Test
    public void testGetSolver() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(3, 3, 1.0);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = decomposition.getSolver();
        RealVector b = MatrixUtils.createRealVector(3, new double[]{1.0, 2.0, 3.0});
        RealVector x = solver.solve(b);
        RealVector expectedX = MatrixUtils.createRealVector(3, new double[]{1.5, -0.5, 0.25});
        assertArrayEquals(expectedX.getDataRef(), x.getDataRef(), 1e-14);
    }

}