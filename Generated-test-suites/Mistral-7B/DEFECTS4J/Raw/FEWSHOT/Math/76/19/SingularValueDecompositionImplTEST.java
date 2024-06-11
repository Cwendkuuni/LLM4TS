package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.Solver;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImpl() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                      {4.0, 5.0, 6.0},
                                                                      {7.0, 8.0, 9.0}});

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);

        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        RealMatrix ut = svd.getUT();
        RealMatrix vt = svd.getVT();

        assertEquals(matrix.getRowDimension(), u.getRowDimension());
        assertEquals(matrix.getColumnDimension(), u.getColumnDimension());
        assertEquals(matrix.getRowDimension(), s.getRowDimension());
        assertEquals(matrix.getColumnDimension(), s.getColumnDimension());
        assertEquals(matrix.getColumnDimension(), v.getRowDimension());
        assertEquals(matrix.getRowDimension(), v.getColumnDimension());
        assertEquals(matrix.getRowDimension(), ut.getRowDimension());
        assertEquals(matrix.getColumnDimension(), ut.getColumnDimension());
        assertEquals(matrix.getColumnDimension(), vt.getRowDimension());
        assertEquals(matrix.getRowDimension(), vt.getColumnDimension());

        assertArrayEquals(new double[][]{
                {0.574596654382222, -0.538460413261198, -0.546745465283479},
                {-0.538460413261198, 0.574596654382222, -0.536552066882623},
                {-0.546745465283479, -0.536552066882623, 0.553508328233257}}, u.getData(), 0.0001);

        assertArrayEquals(new double[][]{
                {5.19615280576993, 0.0, 0.0},
                {0.0, 5.19615280576993, 0.0},
                {0.0, 0.0, 0.0}}, s.getData(), 0.0001);

        assertArrayEquals(new double[][]{
                {-0.538460413261198, 0.538460413261198, -0.536552066882623},
                {-0.536552066882623, 0.536552066882623, 0.546745465283479},
                {0.538460413261198, -0.538460413261198, 0.546745465283479}}, v.getData(), 0.0001);

        assertArrayEquals(new double[][]{
                {-0.574596654382222, -0.538460413261198, -0.546745465283479},
                {-0.538460413261198, 0.574596654382222, -0.536552066882623},
                {-0.546745465283479, -0.536552066882623, 0.553508328233257}}, ut.getData(), 0.0001);

        assertArrayEquals(new double[][]{
                {-0.538460413261198, -0.536552066882623, 0.538460413261198},
                {0.536552066882623, 0.546745465283479, -0.538460413261198},
                {-0.536552066882623, 0.546745465283479, 0.538460413261198}}, vt.getData(), 0.0001);

        RealMatrix productUMS = u.multiply(s.transpose());
        RealMatrix productVMT = v.transpose().multiply(m);
        RealMatrix productUMSV = u.multiply(s.transpose()).multiply(v);

        assertArrayEquals(productUMS.getData(), productUMSV.getData(), 0.0001);
        assertArrayEquals(productUMS.getData(), matrix.getData(), 0.0001);

        RealMatrix inverseU = new SingularValueDecompositionImpl(u.transpose()).getV();
        RealMatrix inverseVT = new SingularValueDecompositionImpl(v.transpose()).getU();

        RealMatrix inverseProductUV = inverseU.multiply(inverseVT);

        assertArrayEquals(inverseProductUV.getData(), MatrixUtils.INVERSE.getInverse(matrix).getData(), 0.0001);

        Solver solver = svd.getSolver();

        RealVector b = new Array2DRowRealVector(new double[][]{
                {1.0}, {2.0}, {3.0}});

        RealVector x = solver.solve(b);

        RealVector expected = new Array2DRowRealVector(new double[]{1.0 / 5.19615280576993,
                                                                     1.0 / 5.19615280576993,
                                                                     1.0 / 5.19615280576993});

        assertArrayEquals(expected.getData(), x.getData(), 0.0001);

        RealMatrix xx = solver.solve(MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}}));

        assertArrayEquals(expected.getData(), xx.getData(0, 0), 0.0001);
        assertArrayEquals(expected.getData(), xx.getData(1, 0), 0.0001);
        assertArrayEquals(expected.getData(), xx.getData(2, 0), 0.0001);

        assertNull(svd.getSolver().isNonSingular());
    }

    @Test
    public void testSingularValueDecompositionImplMaxSingularValues() throws Exception {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                      {4.0, 5.0, 6.0},
                                                                      {7.0, 8.0, 9.0}});

        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix, 2);

        RealMatrix u = svd.getU();
        RealMatrix s = svd.getS();
        RealMatrix v = svd.getV();
        RealMatrix ut = svd.getUT();
        RealMatrix vt = svd.getVT();

        assertEquals(matrix.getRowDimension(), u.getRowDimension());
        assertEquals(matrix.getColumnDimension(), u.getColumnDimension());
        assertEquals(matrix.getRowDimension(), s.getRowDimension());
        assertEquals(2, s.getColumnDimension());
        assertEquals(matrix.getColumnDimension(), v.getRowDimension());
        assertEquals(matrix.getRowDimension(), v.getColumnDimension());
        assertEquals(matrix.getRowDimension(), ut.getRowDimension());
        assertEquals(2, ut.getColumnDimension());
        assertEquals(matrix.getColumnDimension(), vt.getRowDimension());
        assertEquals(2, vt.getColumnDimension());

        assertArrayEquals(new double[][]{
                {0.489897949845228, -0.316227766016838, -0.788426422843844},
                {0.316227766016838, 0.489897949845228, 0.788426422843844}}, u.getData(), 0.0001);

        assertArrayEquals(new double[][]{
                {8.94427190888651, 0.0},
                {0.0, 3.31520527543165}}, s.getData(), 0.0001);

        assertArrayEquals(new double[][]{
                {0.489897949845228, 0.316227766016838, 0.788426422843844},
                {0.873491538586992, 0.437718354545743, 0.408254593931901},
                {-0.408254593931901, 0.437718354545743, 0.873491538586992}}, v.getData(), 0.0001);

        assertArrayEquals(new double[][]{
                {0.489897949845228, 0.316227766016838},
                {-0.316227766016838, 0.489897949845228}}, ut.getData(), 0.0001);

        assertArrayEquals(new double[][]{
                {0.489897949845228, 0.873491538586992},
                {0.316227766016838, -0.437718354545743}}, vt.getData(), 0.0001);

        RealMatrix productUMS = u.multiply(s.transpose());
        RealMatrix productVMT = v.transpose().multiply(m);
        RealMatrix productUMSV = u.multiply(s.transpose()).multiply(v);

        assertArrayEquals(productUMS.getData(), productUMSV.getData(), 0.0001);
        assertArrayEquals(productUMS.getData(), matrix.getData(), 0.0001);

        RealMatrix inverseU = new SingularValueDecompositionImpl(u.transpose(), 2).getV();
        RealMatrix inverseVT = new SingularValueDecompositionImpl(v.transpose(), 2).getU();

        RealMatrix inverseProductUV = inverseU.multiply(inverseVT);

        assertArrayEquals(inverseProductUV.getData(), MatrixUtils.INVERSE.getInverse(matrix).getData(), 0.0001);

        Solver solver = svd.getSolver();

        RealVector b = new Array2DRowRealVector(new double[][]{
                {1.0}, {2.0}, {3.0}});

        RealVector x = solver.solve(b);

        RealVector expected = new Array2DRowRealVector(new double[]{1.0 / (8.94427190888651),
                                                                     1.0 / (8.94427190888651)});

        assertArrayEquals(expected.getData(), x.getData(), 0.0001);

        RealMatrix xx = solver.solve(MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0, 3.0}}));

        assertArrayEquals(expected.getData(), xx.getData(0, 0), 0.0001);
        assertArrayEquals(expected.getData(), xx.getData(1, 0), 0.0001);

        assertNotNull(svd.getSolver().isNonSingular());
    }

}