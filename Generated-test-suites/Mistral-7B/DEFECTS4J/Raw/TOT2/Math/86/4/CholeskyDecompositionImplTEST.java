import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.CholeskyDecompositionImpl;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {

    @Test
    public void testIdentityMatrix() {
        RealMatrix matrix = MatrixUtils.createRealIdentityMatrix(3);
        CholeskyDecomposition decomposition = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = decomposition.getL();
        RealMatrix lt = decomposition.getLT();
        RealMatrix expectedL = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                          {0.0, 1.0, 0.0},
                                                                          {0.0, 0.0, 1.0}});
        RealMatrix expectedLT = lt.transpose();
        assertEquals("L is not equal to the expected identity matrix", expectedL, l);
        assertEquals("LT is not equal to the expected transpose of L", expectedLT, lt);
    }

    @Test(expected = NotSquareMatrixException.class)
    public void testNonSquareMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0},
                                                                       {3.0, 4.0, 5.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testNonSymmetricMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 1.0},
                                                                       {2.0, 3.0}});
        new CholeskyDecompositionImpl(matrix, DEFAULT_RELATIVE_SYMMETRY_THRESHOLD, DEFAULT_ABSOLUTE_POSITIVITY_THRESHOLD);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0},
                                                                       {2.0, -1.0}});
        new CholeskyDecompositionImpl(matrix, DEFAULT_RELATIVE_SYMMETRY_THRESHOLD, DEFAULT_ABSOLUTE_POSITIVITY_THRESHOLD);
    }

    @Test
    public void testSolveVector() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4.0, 1.0},
                                                                       {1.0, 9.0}});
        RealVector b = new RealVectorImpl(new double[]{1.0, 2.0}, false);
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix, DEFAULT_RELATIVE_SYMMETRY_THRESHOLD, DEFAULT_ABSOLUTE_POSITIVITY_THRESHOLD);
        RealVector x = decomposition.getSolver().solve(b);
        RealVector expectedX = new RealVectorImpl(new double[]{0.1295896561f, -0.2127660277f}, false);
        assertEquals("Solution is not equal to the expected solution", expectedX, x);
    }

    @Test
    public void testSolveMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4.0, 1.0, 0.0},
                                                                       {1.0, 9.0, 2.0},
                                                                       {0.0, 2.0, 3.0}});
        RealMatrix b = MatrixUtils.createRealMatrix(new double[][]{{1.0, 0.0},
                                                                    {0.0, 2.0},
                                                                    {-1.0, 3.0}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix, DEFAULT_RELATIVE_SYMMETRY_THRESHOLD, DEFAULT_ABSOLUTE_POSITIVITY_THRESHOLD);
        RealMatrix x = decomposition.getSolver().solve(b);
        RealMatrix expectedX = MatrixUtils.createRealMatrix(new double[][]{{0.1295896561f, 0.0637958281f},
                                                                           {-0.2127660277f, 0.1751631228f},
                                                                           {-0.1063975136f, -0.0375815613f}});
        assertEquals("Solution is not equal to the expected solution", expectedX, x);
    }

    @Test
    public void testDeterminant() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{2.0, 3.0},
                                                                       {3.0, 4.0}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix, DEFAULT_RELATIVE_SYMMETRY_THRESHOLD, DEFAULT_ABSOLUTE_POSITIVITY_THRESHOLD);
        double determinant = decomposition.getDeterminant();
        assertEquals("Determinant is not equal to the expected value", 15.0, determinant, 1.0e-14);
    }

    @Test
    public void testInverse() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{2.0, 3.0},
                                                                       {3.0, 4.0}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix, DEFAULT_RELATIVE_SYMMETRY_THRESHOLD, DEFAULT_ABSOLUTE_POSITIVITY_THRESHOLD);
        RealMatrix inverse = decomposition.getSolver().getInverse();
        RealMatrix expectedInverse = MatrixUtils.createRealMatrix(new double[][]{{0.2360679775f, -0.1557304751f},
                                                                                 {-0.1557304751f, 0.0828652377f}});
        assertEquals("Inverse is not equal to the expected inverse", expectedInverse, inverse);
    }

}