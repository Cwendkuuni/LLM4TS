import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {

    @Test
    public void testConstructorWithDefaultThresholds() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{
                {1.0, 0.1, 0.1},
                {0.1, 1.0, 0.1},
                {0.1, 0.1, 1.0}
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        assertNotNull(cholesky.getL());
        assertNotNull(cholesky.getLT());
    }

    @Test(expected = NotSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 0.1},
                {0.1, 1.0},
                {0.1, 0.1}
        });
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testConstructorWithNonSymmetricMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 0.1},
                {0.1, 0.1},
                {0.1, 0.2}
        });
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 0.1},
                {0.1, -1.0}
        });
        new CholeskyDecompositionImpl(matrix);
    }

    @Test
    public void testConstructorWithCustomThresholds() {
        RealMatrix matrix = MatrixUtils.createRealSymmetricMatrix(new double[][]{
                {1.0e-10, 0.1, 0.1},
                {0.1, 1.0e-10, 0.1},
                {0.1, 0.1, 1.0}
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix, 1.0e-12, 1.0e-12);
        assertNotNull(cholesky.getL());
        assertNotNull(cholesky.getLT());
    }

    @Test
    public void testGetL() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 0.1, 0.1},
                {0.1, 1.0, 0.1},
                {0.1, 0.1, 1.0}
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix expectedL = MatrixUtils.createRealMatrix(new double[][]{
                {0.1, 0.0, 0.0},
                {0.0, 0.70710678118654755, 0.40824829046386304},
                {0.0, 0.40824829046386304, 0.57735026918962576}
        });
        RealMatrix l = cholesky.getL();
        assertEquals(expectedL, l);
    }

    @Test
    public void testGetLT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 0.1, 0.1},
                {0.1, 1.0, 0.1},
                {0.1, 0.1, 1.0}
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        RealMatrix expectedLT = MatrixUtils.createRealMatrix(new double[][]{
                {0.1, 0.1, 0.1},
                {0.1, 0.70710678118654755, 0.40824829046386304},
                {0.1, 0.40824829046386304, 0.57735026918962576}
        });
        RealMatrix lt = cholesky.getLT();
        assertEquals(expectedLT, lt);
    }

    @Test
    public void testGetDeterminant() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 0.1, 0.1},
                {0.1, 1.0, 0.1},
                {0.1, 0.1, 1.0}
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        double expectedDeterminant = 0.5465122501465171;
        assertEquals(expectedDeterminant, cholesky.getDeterminant(), 1e-12);
    }

    @Test
    public void testGetSolver() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
                {1.0, 0.1, 0.1},
                {0.1, 1.0, 0.1},
                {0.1, 0.1, 1.0}
        });
        CholeskyDecompositionImpl cholesky = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = cholesky.getSolver();
        double[] b = new double[]{1.0, 2.0, 3.0};
        double[] expectedX = new double[]{2.3464726862213545, 0.6324555366576095, 1.0};
        RealVector x = solver.solve(new RealVectorImpl(b));
        assertArrayEquals(expectedX, x.toArray(), 1e-12);
    }

}