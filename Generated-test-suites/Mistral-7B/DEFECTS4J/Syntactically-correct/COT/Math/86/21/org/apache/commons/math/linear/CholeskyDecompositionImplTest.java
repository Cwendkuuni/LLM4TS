import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {

    @Test
    public void testConstructorWithDefaultThreshold() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.1}, {0.1, 1.0, 0.1}, {0.1, 0.1, 1.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix expectedL = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5, 0.25}, {0.5, 1.0, 0.5}, {0.25, 0.5, 1.0}});
        RealMatrix actualL = choleskyDecomposition.getL();
        MatrixUtils.equalToIdentity(expectedL, actualL, 1e-15);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1}, {0.1}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testConstructorWithNonSymmetricMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.1}, {0.1, 1.0, 0.1}, {0.1, 0.1, 1.0}, {0.1, 0.1, 0.1, 1.1}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.1}, {0.1, -1.0, 0.1}, {0.1, 0.1, -1.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetLWithNullArgument() {
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(MatrixUtils.createRealIdentityMatrix(1));
        choleskyDecomposition.getL(null);
    }

    @Test
    public void testGetLT() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.1}, {0.1, 1.0, 0.1}, {0.1, 0.1, 1.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix expectedLT = new Array2DRowRealMatrix(new double[][]{{1.0, 0.5, 0.3125}, {0.5, 1.0, 0.625}, {0.3125, 0.625, 1.0}});
        RealMatrix actualLT = choleskyDecomposition.getLT();
        MatrixUtils.equalToIdentity(expectedLT, actualLT, 1e-15);
    }

    @Test
    public void testGetDeterminant() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.1}, {0.1, 1.0, 0.1}, {0.1, 0.1, 1.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        double expectedDeterminant = 0.375;
        double actualDeterminant = choleskyDecomposition.getDeterminant();
        assertEquals(expectedDeterminant, actualDeterminant, 1e-15);
    }

    @Test
    public void testGetSolver() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.1, 0.1}, {0.1, 1.0, 0.1}, {0.1, 0.1, 1.0}});
        CholeskyDecompositionImpl choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
        RealVector vector = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector solution = choleskyDecomposition.getSolver().solve(vector);
        double[] expectedSolution = new double[]{0.3333333333333333, 0.6666666666666667, 1.0};
        assertArrayEquals(expectedSolution, solution.toArray(), 1e-15);
    }

}