package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.EigenvalueDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SingularValueDecompositionImpl;
import org.apache.commons.math3.linear.Solver;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.special.Epsilon;

import java.math.BigDecimal;
import java.util.Arrays;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImpl() throws Exception {
        final RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        });

        final SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();

        RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{
                {0.4771849, -0.7221694, 0.4512829},
                {-0.7383876, 0.6616132, -0.4135761},
                {0.4082483, 0.7496129, 0.5324421}
        });
        RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{
                {0.4472136, 0.7873438, -0.4082483},
                {-0.7745968, -0.0822926, 0.7383876},
                {0.4082483, 0.5555112, 0.4082483}
        });
        RealMatrix expectedS = MatrixUtils.createRealDiagonalMatrix(new double[]{1.0, 1.5811388, 2.1653045});

        assertEquals(expectedU.getRowDimension(), u.getRowDimension());
        assertEquals(expectedU.getColumnDimension(), u.getColumnDimension());
        assertEquals(expectedV.getRowDimension(), v.getRowDimension());
        assertEquals(expectedV.getColumnDimension(), v.getColumnDimension());
        assertEquals(expectedS.getRowDimension(), s.getRowDimension());
        assertEquals(expectedS.getColumnDimension(), s.getColumnDimension());
        assertArrayEquals(expectedU.getData(), u.getData(), 1e-12);
        assertArrayEquals(expectedV.getData(), v.getData(), 1e-12);
        assertArrayEquals(expectedS.getData(), s.getData(), 1e-12);

        RealMatrix expectedUSV = expectedU.multiply(expectedS).multiply(expectedV);
        RealMatrix computedUSV = u.multiply(s).multiply(v);
        assertEquals(expectedUSV.getRowDimension(), computedUSV.getRowDimension());
        assertEquals(expectedUSV.getColumnDimension(), computedUSV.getColumnDimension());
        assertArrayEquals(expectedUSV.getData(), computedUSV.getData(), 1e-12);
    }

    @Test
    public void testSingularValueDecompositionImpl_getRank() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 0.0},
                {4.0, 5.0, 0.0},
                {7.0, 8.0, 0.0}
        });

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        assertEquals(2, svd.getRank());
    }

    @Test
    public void testSingularValueDecompositionImpl_getRank_singularMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0}
        });

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        assertEquals(0, svd.getRank());
    }

    @Test
    public void testSingularValueDecompositionImpl_getSingularValues() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        });

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double[] expectedSingularValues = new double[]{1.0, 1.5811388, 2.1653045};
        assertArrayEquals(expectedSingularValues, svd.getSingularValues(), 1e-12);
    }

    @Test
    public void testSingularValueDecompositionImpl_getCovariance() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        });

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix covariance = svd.getCovariance(0.5);

        RealMatrix expectedCovariance = new Array2DRowRealMatrix(new double[][]{
                {0.3824799, 0.3217463, 0.2597819},
                {0.3217463, 0.3824799, 0.2597819},
                {0.2597819, 0.2597819, 0.2221898}
        });
        assertArrayEquals(expectedCovariance.getData(), covariance.getData(), 1e-12);
    }

    @Test
    public void testSingularValueDecompositionImpl_getNorm() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        });

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        assertEquals(1.0, svd.getNorm(), 1e-12);
    }

    @Test
    public void testSingularValueDecompositionImpl_getConditionNumber() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        });

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        assertEquals(2.1653045 / 1.0, svd.getConditionNumber(), 1e-12);
    }

    @Test
    public void testSingularValueDecompositionImpl_getSolver() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        });

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = svd.getSolver();

        RealVector b = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector x = solver.solve(b);

        RealVector expectedX = new ArrayRealVector(new double[]{1.3661410, 0.3524343, -0.8164966});
        assertEquals(expectedX.getDimension(), x.getDimension());
        assertArrayEquals(expectedX.toArray(), x.toArray(), 1e-12);

        RealVector xExpected = new ArrayRealVector(new double[]{1.3661410, 0.3524343, -0.8164966});
        assertEquals(xExpected, solver.getInverse().solve(b), 1e-12);

        assertThrows(IllegalArgumentException.class, () -> svd.getSolver().solve(null));
    }

    @Test
    public void testSingularValueDecompositionImpl_getSolver_singularMatrix() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0},
                {0.0, 0.0, 0.0}
        });

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = svd.getSolver();

        RealVector b = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        assertThrows(IllegalArgumentException.class, () -> solver.solve(b));
    }
}