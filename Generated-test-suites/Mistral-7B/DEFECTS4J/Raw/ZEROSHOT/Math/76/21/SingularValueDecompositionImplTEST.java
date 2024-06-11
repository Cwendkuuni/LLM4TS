import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.Solver;
import org.apache.commons.math3.linear.SingularValueDecompositionImpl;
import org.junit.Assert;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecomposition() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {2.0, 1.0},
                {1.0, 2.0}
        });
        SingularValueDecomposition decomposition = new SingularValueDecompositionImpl(matrix);
        RealMatrix U = decomposition.getU();
        RealMatrix S = decomposition.getS();
        RealMatrix VT = decomposition.getVT();
        Assert.assertEquals(U.getRowDimension(), U.getColumnDimension(), 2);
        Assert.assertEquals(S.getRowDimension(), S.getColumnDimension(), 2);
        Assert.assertEquals(VT.getRowDimension(), VT.getColumnDimension(), 2);
        Assert.assertEquals(U.getColumnDimension(), S.getColumnDimension(), VT.getColumnDimension(), 2);
        Assert.assertArrayEquals(new double[][]{
                {-0.244948974278317, 0.8164965809277261},
                {0.8164965809277261, -0.244948974278317}
        }, U.getData(), 0.000001);
        Assert.assertEquals(new double[]{1.4142135625, 0}, S.getData(), 0.000001);
        Assert.assertArrayEquals(new double[][]{
                {-0.8164965809277261, 0.244948974278317},
                {0.244948974278317, -0.8164965809277261}
        }, VT.getData(), 0.000001);
        RealMatrix X = S.getSolver().solve(new Array2DRowRealMatrix(new double[][]{
                {2.0, 1.0},
                {1.0, 2.0}
        }));
        RealMatrix A = U.transpose().multiply(S).multiply(VT);
        Assert.assertArrayEquals(new double[][]{
                {0.244948974278317, -0.244948974278317},
                {-0.244948974278317, 0.244948974278317}
        }, X.getData(), 0.000001);
        Assert.assertEquals(new double[]{1.4142135625, 1.4142135625}, A.multiply(X.getData()).getData(), 0.000001);
    }

    @Test
    public void testSingularValueDecompositionWithZeroDeterminant() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.0},
                {0.0, 1.0}
        });
        Assert.assertThrows(InvalidMatrixException.class, () -> {
            new SingularValueDecompositionImpl(matrix);
        });
    }

    @Test
    public void testSingularValueDecompositionWithSingularValuesLessThanZero() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.0},
                {0.0, -1.0}
        });
        Assert.assertThrows(InvalidMatrixException.class, () -> {
            new SingularValueDecompositionImpl(matrix);
        });
    }

    @Test
    public void testSolve() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {2.0, 1.0},
                {1.0, 2.0}
        });
        SingularValueDecomposition decomposition = new SingularValueDecompositionImpl(matrix);
        RealMatrix X = decomposition.getSolver().solve(new Array2DRowRealMatrix(new double[][]{
                {3.0, 4.0}
        }));
        Assert.assertEquals(X.getRowDimension(), 1);
        Assert.assertEquals(X.getColumnDimension(), 2);
        Assert.assertArrayEquals(new double[]{
                {-0.1320755051667298},
                {0.8679244948974274}
        }, X.getData(), 0.000001);
    }

    @Test
    public void testSolveWithNonSquareMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {2.0, 1.0},
                {1.0, 2.0},
                {1.0, 1.0}
        });
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            new SingularValueDecompositionImpl(matrix).getSolver().solve(new Array2DRowRealMatrix(new double[][]{
                    {3.0, 4.0}
            }));
        });
    }

    @Test
    public void testSolveWithNonSingularMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {2.0, 1.0},
                {1.0, 2.0}
        });
        SingularValueDecomposition decomposition = new SingularValueDecompositionImpl(matrix);
        Assert.assertTrue(decomposition.isNonSingular());
    }

    @Test
    public void testSolveWithSingularMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.0},
                {0.0, 1.0}
        });
        SingularValueDecomposition decomposition = new SingularValueDecompositionImpl(matrix);
        Assert.assertFalse(decomposition.isNonSingular());
    }
}