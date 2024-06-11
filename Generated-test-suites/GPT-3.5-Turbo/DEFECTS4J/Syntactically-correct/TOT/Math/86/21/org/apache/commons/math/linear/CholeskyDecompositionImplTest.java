import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.linear.CholeskyDecompositionImpl;
import org.apache.commons.math.linear.MatrixUtils;
import org.apache.commons.math.linear.RealMatrix;
import org.junit.Assert;

public class CholeskyDecompositionImplTest {

    private double[][] testData;
    private CholeskyDecompositionImpl choleskyDecomposition;

    @Before
    public void setUp() {
        testData = new double[][]{
                {4.0, 12.0, -16.0},
                {12.0, 37.0, -43.0},
                {-16.0, -43.0, 98.0}
        };
        RealMatrix matrix = MatrixUtils.createRealMatrix(testData);
        choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
    }

    @Test
    public void testGetL() {
        RealMatrix L = choleskyDecomposition.getL();
        Assert.assertEquals(3, L.getRowDimension());
        Assert.assertEquals(3, L.getColumnDimension());
        Assert.assertEquals(2, L.getEntry(0, 0), 0.001);
        Assert.assertEquals(0, L.getEntry(1, 0), 0.001);
        Assert.assertEquals(0, L.getEntry(2, 0), 0.001);
        Assert.assertEquals(6, L.getEntry(1, 1), 0.001);
        Assert.assertEquals(0, L.getEntry(2, 1), 0.001);
        Assert.assertEquals(-2, L.getEntry(2, 2), 0.001);
    }

    @Test
    public void testGetLT() {
        RealMatrix LT = choleskyDecomposition.getLT();
        Assert.assertEquals(3, LT.getRowDimension());
        Assert.assertEquals(3, LT.getColumnDimension());
        Assert.assertEquals(2, LT.getEntry(0, 0), 0.001);
        Assert.assertEquals(6, LT.getEntry(0, 1), 0.001);
        Assert.assertEquals(-2, LT.getEntry(0, 2), 0.001);
        Assert.assertEquals(0, LT.getEntry(1, 0), 0.001);
        Assert.assertEquals(6, LT.getEntry(1, 1), 0.001);
        Assert.assertEquals(-3.833, LT.getEntry(1, 2), 0.001);
        Assert.assertEquals(0, LT.getEntry(2, 0), 0.001);
        Assert.assertEquals(0, LT.getEntry(2, 1), 0.001);
        Assert.assertEquals(7.211, LT.getEntry(2, 2), 0.001);
    }

    @Test
    public void testGetDeterminant() {
        double determinant = choleskyDecomposition.getDeterminant();
        Assert.assertEquals(36, determinant, 0.001);
    }

    @Test
    public void testGetSolver() {
        CholeskyDecompositionImpl.DecompositionSolver solver = choleskyDecomposition.getSolver();
        Assert.assertNotNull(solver);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrix() {
        double[][] nonSquareData = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        RealMatrix nonSquareMatrix = MatrixUtils.createRealMatrix(nonSquareData);
        CholeskyDecompositionImpl nonSquareDecomposition = new CholeskyDecompositionImpl(nonSquareMatrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrix() {
        double[][] notSymmetricData = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        RealMatrix notSymmetricMatrix = MatrixUtils.createRealMatrix(notSymmetricData);
        CholeskyDecompositionImpl notSymmetricDecomposition = new CholeskyDecompositionImpl(notSymmetricMatrix);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrix() {
        double[][] notPositiveDefiniteData = new double[][]{{-1.0, 0.0, 0.0}, {0.0, -1.0, 0.0}, {0.0, 0.0, -1.0}};
        RealMatrix notPositiveDefiniteMatrix = MatrixUtils.createRealMatrix(notPositiveDefiniteData);
        CholeskyDecompositionImpl notPositiveDefiniteDecomposition =
                new CholeskyDecompositionImpl(notPositiveDefiniteMatrix);
    }
}