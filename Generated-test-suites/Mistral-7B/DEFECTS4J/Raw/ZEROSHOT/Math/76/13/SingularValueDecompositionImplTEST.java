import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Assert;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecomposition() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);

        RealMatrix u = svd.getU();
        Assert.assertEquals(2, u.getRowDimension());
        Assert.assertEquals(2, u.getColumnDimension());

        RealMatrix s = svd.getS();
        Assert.assertEquals(2, s.getRowDimension());
        Assert.assertEquals(2, s.getColumnDimension());
        Assert.assertEquals(3.0, s.getEntry(0, 0), 1e-6);
        Assert.assertEquals(5.0, s.getEntry(1, 1), 1e-6);

        RealMatrix v = svd.getV();
        Assert.assertEquals(2, v.getRowDimension());
        Assert.assertEquals(2, v.getColumnDimension());

        RealMatrix ut = svd.getUT();
        Assert.assertEquals(2, ut.getRowDimension());
        Assert.assertEquals(2, ut.getColumnDimension());
        Assert.assertEquals(1.0 / 3.0, ut.getEntry(0, 0), 1e-6);
        Assert.assertEquals(1.0 / 5.0, ut.getEntry(1, 0), 1e-6);
        Assert.assertEquals(1.0 / 3.0, ut.getEntry(0, 1), 1e-6);
        Assert.assertEquals(1.0 / 5.0, ut.getEntry(1, 1), 1e-6);

        RealMatrix vt = svd.getVT();
        Assert.assertEquals(2, vt.getRowDimension());
        Assert.assertEquals(2, vt.getColumnDimension());
        Assert.assertEquals(1.0 / 3.0, vt.getEntry(0, 0), 1e-6);
        Assert.assertEquals(1.0 / 5.0, vt.getEntry(1, 0), 1e-6);
        Assert.assertEquals(1.0 / 3.0, vt.getEntry(0, 1), 1e-6);
        Assert.assertEquals(1.0 / 5.0, vt.getEntry(1, 1), 1e-6);

        double[] singularValues = svd.getSingularValues();
        Assert.assertArrayEquals(new double[]{3.0, 5.0}, singularValues, 1e-6);

        DecompositionSolver solver = svd.getSolver();

        double[] b = new double[]{1.0, 1.0};
        double[] x = solver.solve(b);
        Assert.assertArrayEquals(new double[]{1.3888888888888887, 0.6666666666666667}, x, 1e-6);

        RealVector rb = new Array2DRowRealVector(new double[][]{b});
        RealVector rx = solver.solve(rb);
        Assert.assertEquals(1.3888888888888887, rx.getEntry(0), 1e-6);
        Assert.assertEquals(0.6666666666666667, rx.getEntry(1), 1e-6);

        RealMatrix rbMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0}});
        RealMatrix rxMatrix = solver.solve(rbMatrix);
        Assert.assertEquals(1.3888888888888887, rxMatrix.getEntry(0, 0), 1e-6);
        Assert.assertEquals(0.6666666666666667, rxMatrix.getEntry(1, 0), 1e-6);

        Assert.assertTrue(solver.isNonSingular());
        Assert.assertNull(solver.getInverse());

        matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {3.0, 4.0, 5.0}});
        svd = new SingularValueDecompositionImpl(matrix, 2);

        u = svd.getU();
        Assert.assertEquals(3, u.getRowDimension());
        Assert.assertEquals(2, u.getColumnDimension());

        s = svd.getS();
        Assert.assertEquals(2, s.getRowDimension());
        Assert.assertEquals(2, s.getColumnDimension());
        Assert.assertEquals(3.0, s.getEntry(0, 0), 1e-6);
        Assert.assertEquals(6.0, s.getEntry(1, 1), 1e-6);

        v = svd.getV();
        Assert.assertEquals(3, v.getRowDimension());
        Assert.assertEquals(2, v.getColumnDimension());

        ut = svd.getUT();
        Assert.assertEquals(2, ut.getRowDimension());
        Assert.assertEquals(3, ut.getColumnDimension());
        Assert.assertEquals(1.0 / 3.0, ut.getEntry(0, 0), 1e-6);
        Assert.assertEquals(1.0 / 6.0, ut.getEntry(0, 1), 1e-6);
        Assert.assertEquals(1.0 / 3.0, ut.getEntry(0, 2), 1e-6);
        Assert.assertEquals(1.0 / 6.0, ut.getEntry(1, 1), 1e-6);
        Assert.assertEquals(1.0 / 6.0, ut.getEntry(1, 2), 1e-6);

        vt = svd.getVT();
        Assert.assertEquals(2, vt.getRowDimension());
        Assert.assertEquals(3, vt.getColumnDimension());
        Assert.assertEquals(1.0 / 3.0, vt.getEntry(0, 0), 1e-6);
        Assert.assertEquals(1.0 / 6.0, vt.getEntry(0, 1), 1e-6);
        Assert.assertEquals(1.0 / 3.0, vt.getEntry(0, 2), 1e-6);
        Assert.assertEquals(1.0 / 6.0, vt.getEntry(1, 1), 1e-6);
        Assert.assertEquals(1.0 / 6.0, vt.getEntry(1, 2), 1e-6);

        singularValues = svd.getSingularValues();
        Assert.assertArrayEquals(new double[]{3.0, 6.0}, singularValues, 1e-6);

        solver = svd.getSolver();

        b = new double[]{1.0, 1.0, 1.0};
        x = solver.solve(b);
        Assert.assertArrayEquals(new double[]{1.111111111111111, 0.5555555555555556, 0.0}, x, 1e-6);

        rb = new Array2DRowRealVector(new double[][]{b});
        rx = solver.solve(rb);
        Assert.assertEquals(1.111111111111111, rx.getEntry(0), 1e-6);
        Assert.assertEquals(0.5555555555555556, rx.getEntry(1), 1e-6);
        Assert.assertEquals(0.0, rx.getEntry(2), 1e-6);

        rbMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 1.0, 1.0}});
        rxMatrix = solver.solve(rbMatrix);
        Assert.assertEquals(1.111111111111111, rxMatrix.getEntry(0, 0), 1e-6);
        Assert.assertEquals(0.5555555555555556, rxMatrix.getEntry(1, 0), 1e-6);
        Assert.assertEquals(0.0, rxMatrix.getEntry(2, 0), 1e-6);

        Assert.assertFalse(solver.isNonSingular());
        Assert.assertNotNull(solver.getInverse());
    }

}