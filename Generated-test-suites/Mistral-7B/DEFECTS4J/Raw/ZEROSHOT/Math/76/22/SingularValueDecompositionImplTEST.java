import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SingularValueDecompositionImpl;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImpl() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        });

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);

        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();

        assertNotNull(u);
        assertNotNull(s);
        assertNotNull(v);

        Assert.assertEquals(3, u.getRowDimension());
        Assert.assertEquals(3, u.getColumnDimension());
        Assert.assertEquals(3, s.getRowDimension());
        Assert.assertEquals(3, s.getColumnDimension());
        Assert.assertEquals(3, v.getRowDimension());
        Assert.assertEquals(3, v.getColumnDimension());

        RealMatrix expectedUT = new Array2DRowRealMatrix(new double[][]{
                {-0.382821025, 0.740275191, 0.538460086},
                {0.740275191, 0.382821025, -0.538460086},
                {0.538460086, -0.538460086, 0.382821025}
        });
        RealMatrix expectedUTActual = svd.getUT();
        assertEquals(expectedUT, expectedUTActual);

        RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{
                {-0.408248291, 0.816496581, -0.408248291},
                {-0.816496581, -0.408248291, 0.816496581},
                {-0.408248291, 0.816496581, 0.408248291}
        });
        RealMatrix expectedVActual = svd.getV();
        assertEquals(expectedV, expectedVActual);

        RealMatrix expectedS = new Array2DRowRealMatrix(new double[][]{
                {11.8749793, 0.0},
                {0.0, 2.4494898},
                {0.0, 0.0}
        });
        RealMatrix expectedSActedual = svd.getS();
        assertEquals(expectedS, expectedSActedual);

        RealMatrix expectedPseudoInverse = new Array2DRowRealMatrix(new double[][]{
                {-0.132251522, 0.157804878, 0.861538467},
                {0.157804878, 0.923606798, -0.428657565},
                {0.861538467, -0.428657565, 0.28403323}
        });
        DecompositionSolver solver = svd.getSolver();
        RealMatrix pseudoInverse = solver.getInverse();
        assertEquals(expectedPseudoInverse, pseudoInverse);

        double[] b = new double[]{1.0, 2.0, 3.0};
        double[] expectedX = new double[]{
                1.020262634,
                0.979737868,
                1.005056623};
        double[] x = solver.solve(b);
        assertEquals(expectedX, x);

        RealVector rvb = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector rvx = solver.solve(rvb);
        Assert.assertArrayEquals(expectedX, rvx.toArray());

        RealMatrix rb = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0},
                {3.0, 4.0}});
        RealMatrix rx = solver.solve(rb);
        assertEquals(expectedPseudoInverse, rx);

        // Non-square matrix test
        matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0},
                {3.0}});
        svd = new SingularValueDecompositionImpl(matrix, 1);
        assertNotNull(svd.getSolver());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSingularValueDecompositionImplInvalidArg() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0},
                {3.0, 4.0},
                {5.0, 6.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 4);
    }

}