import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CholeskyDecompositionImplTest {

    private CholeskyDecomposition decomposition;

    @Before
    public void setUp() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 5.0, 2.0}, {1.0, 2.0, 7.0}});
        decomposition = new CholeskyDecompositionImpl(matrix);
    }

    @Test
    public void testGetL() {
        RealMatrix expectedL = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.7071067811865476, 1.0, 0.0}, {-0.4226187729418671, -0.4226187729418671, 1.0}});
        RealMatrix l = decomposition.getL();
        Assert.assertArrayEquals(expectedL.getData(), l.getData(), 0.0);
    }

    @Test
    public void testGetLT() {
        RealMatrix expectedLT = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 1.0}, {0.7071067811865476, 5.0, 2.0}, {0.4226187729418671, 2.0, 7.0}});
        RealMatrix lt = decomposition.getLT();
        Assert.assertArrayEquals(expectedLT.getData(), lt.getData(), 0.0);
    }

    @Test
    public void testGetDeterminant() {
        double expectedDeterminant = 35.172413793103346;
        double determinant = decomposition.getDeterminant();
        Assert.assertEquals(expectedDeterminant, determinant, 0.0);
    }

    @Test
    public void testSolver() {
        RealVector b = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector x = decomposition.getSolver().solve(b);
        RealVector expectedX = new ArrayRealVector(new double[]{2.660194821312552, -0.3316340626309263, 0.5773502691896258});
        Assert.assertArrayEquals(expectedX.toArray(), x.toArray(), 0.0);
    }

    @Test
    public void testSolve() {
        RealVector b = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector x = decomposition.solve(b);
        RealVector expectedX = new ArrayRealVector(new double[]{2.660194821312552, -0.3316340626309263, 0.5773502691896258});
        Assert.assertArrayEquals(expectedX.toArray(), x.toArray(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveWithInvalidVector() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 5.0, 2.0}, {1.0, 2.0, 7.0}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        RealVector b = new ArrayRealVector(new double[]{1.0, 2.0, 3.0, 4.0});
        decomposition.solve(b);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolverWithInvalidVector() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 5.0, 2.0}, {1.0, 2.0, 7.0}});
        CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
        DecompositionSolver solver = decomposition.getSolver();
        RealVector b = new ArrayRealVector(new double[]{1.0, 2.0, 3.0, 4.0});
        solver.solve(b);
    }

    @Test
    public void testInverse() {
        RealMatrix expectedInverse = new Array2DRowRealMatrix(new double[][]{{0.348339693357875, 0.223585726273144, -0.074598974152845},
                {0.223585726273144, 1.96185214519272, 0.652145151628191},
                {-0.074598974152845, 0.652145151628191, 1.73122956216314}});
        RealMatrix inverse = decomposition.getInverse();
        Assert.assertArrayEquals(expectedInverse.getData(), inverse.getData(), 0.0);
    }

}