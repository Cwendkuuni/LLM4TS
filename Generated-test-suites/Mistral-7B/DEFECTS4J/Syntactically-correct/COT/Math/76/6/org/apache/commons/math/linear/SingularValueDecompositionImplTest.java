package org.apache.commons.math.linear;

import org.apache.commons.math3.exception.SingularValueDecompositionException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.special.EigenvalueSolver;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for SingularValueDecompositionImpl class.
 */
public class SingularValueDecompositionImplTest {

    private RealMatrix testMatrix;
    private SingularValueDecompositionImpl svd;

    @Before
    public void setup() {
        double[][] data = {
                {2.0, 1.0, 1.0},
                {1.0, 2.0, 1.0},
                {1.0, 1.0, 2.0}
        };
        testMatrix = new Array2DRowRealMatrix(data, false);
    }

    @Test
    public void testSingularValueDecompositionImpl_Init() throws SingularValueDecompositionException {
        svd = new SingularValueDecompositionImpl(testMatrix);
    }

    @Test
    public void testGetU() throws SingularValueDecompositionException {
        svd = new SingularValueDecompositionImpl(testMatrix);
        RealMatrix u = svd.getU();
        Assert.assertNotNull(u);
        int rows = u.getRowDimension();
        int cols = u.getColumnDimension();
        Assert.assertEquals(rows, cols);
        Assert.assertEquals(rows, testMatrix.getRowDimension());
        Assert.assertEquals(cols, testMatrix.getColumnDimension());
    }

    @Test
    public void testGetUT() throws SingularValueDecompositionException {
        svd = new SingularValueDecompositionImpl(testMatrix);
        RealMatrix ut = svd.getUT();
        Assert.assertNotNull(ut);
        int rows = ut.getRowDimension();
        int cols = ut.getColumnDimension();
        Assert.assertEquals(cols, testMatrix.getRowDimension());
        Assert.assertEquals(rows, testMatrix.getColumnDimension());
    }

    @Test
    public void testGetS() throws SingularValueDecompositionException {
        svd = new SingularValueDecompositionImpl(testMatrix);
        RealMatrix s = svd.getS();
        Assert.assertNotNull(s);
        Assert.assertTrue(s instanceof RealDiagonalMatrix);
        int rows = s.getRowDimension();
        int cols = s.getColumnDimension();
        Assert.assertEquals(rows, 1);
        Assert.assertEquals(cols, testMatrix.getRowDimension());
    }

    @Test
    public void testGetSingularValues() throws SingularValueDecompositionException {
        svd = new SingularValueDecompositionImpl(testMatrix);
        double[] singularValues = svd.getSingularValues();
        Assert.assertNotNull(singularValues);
        Assert.assertEquals(singularValues.length, testMatrix.getRowDimension());
        Assert.assertNotSame(singularValues[0], 0.0);
    }

    @Test
    public void testGetV() throws SingularValueDecompositionException {
        svd = new SingularValueDecompositionImpl(testMatrix);
        RealMatrix v = svd.getV();
        Assert.assertNotNull(v);
        int rows = v.getRowDimension();
        int cols = v.getColumnDimension();
        Assert.assertEquals(rows, testMatrix.getRowDimension());
        Assert.assertEquals(cols, testMatrix.getColumnDimension());
    }

    @Test
    public void testGetVT() throws SingularValueDecompositionException {
        svd = new SingularValueDecompositionImpl(testMatrix);
        RealMatrix vt = svd.getVT();
        Assert.assertNotNull(vt);
        int rows = vt.getRowDimension();
        int cols = vt.getColumnDimension();
        Assert.assertEquals(cols, testMatrix.getRowDimension());
        Assert.assertEquals(rows, testMatrix.getColumnDimension());
    }

    @Test
    public void testGetCovariance() throws SingularValueDecompositionException {
        svd = new SingularValueDecompositionImpl(testMatrix);
        double minSingularValue = singularValues[1];
        RealMatrix covariance = svd.getCovariance(minSingularValue);
        Assert.assertNotNull(covariance);
        int rows = covariance.getRowDimension();
        int cols = covariance.getColumnDimension();
        Assert.assertEquals(rows, testMatrix.getRowDimension());
        Assert.assertEquals(cols, testMatrix.getRowDimension());
    }

    @Test
    public void testGetNorm() throws SingularValueDecompositionException {
        svd = new SingularValueDecompositionImpl(testMatrix);
        double norm = svd.getNorm();
        Assert.assertNotNull(norm);
        Assert.assertEquals(norm, singularValues[0], 1e-6);
    }

    @Test
    public void testGetConditionNumber() throws SingularValueDecompositionException {
        svd = new SingularValueDecompositionImpl(testMatrix);
        double conditionNumber = svd.getConditionNumber();
        Assert.assertNotNull(conditionNumber);
        Assert.assertEquals(conditionNumber, singularValues[0] / singularValues[2], 1e-6);
    }

    @Test
    public void testGetRank() throws SingularValueDecompositionException {
        svd = new SingularValueDecompositionImpl(testMatrix);
        int rank = svd.getRank();
        Assert.assertEquals(rank, testMatrix.getRank());
    }

    @Test
    public void testGetSolver() throws SingularValueDecompositionException {
        svd = new SingularValueDecompositionImpl(testMatrix);
        DecompositionSolver solver = svd.getSolver();
        Assert.assertNotNull(solver);
    }

    @Test
    public void testSolver_SolveVector() throws SingularValueDecompositionException {
        svd = new SingularValueDecompositionImpl(testMatrix);
        RealVector b = new Array2DRowRealMatrix(new double[][]{{1.0}, {2.0}}, false).getColumnVector(0);
        RealVector x = svd.getSolver().solve(b);
        Assert.assertEquals(x.getDimension(), testMatrix.getRowDimension());
        Assert.assertEquals(x.getDimension(), x.getNumberElements(), 1);
    }

    @Test
    public void testSolver_SolveMatrix() throws SingularValueDecompositionException {
        svd = new SingularValueDecompositionImpl(testMatrix);
        RealMatrix b = MatrixUtils.createRealMatrix(new double[][]{{1.0, 2.0}, {3.0, 4.0}});
        RealMatrix x = svd.getSolver().solve(b);
        Assert.assertEquals(x.getRowDimension(), testMatrix.getRowDimension());
        Assert.assertEquals(x.getColumnDimension(), testMatrix.getColumnDimension());
    }

    @Test
    public void testSolver_IsNonSingular() throws SingularValueDecompositionException {
        svd = new SingularValueDecompositionImpl(testMatrix);
        DecompositionSolver solver = svd.getSolver();
        Assert.assertTrue(solver.isNonSingular());
    }

    @Test
    public void testSolver_GetInverse() throws SingularValueDecompositionException {
        svd = new SingularValueDecompositionImpl(testMatrix);
        DecompositionSolver solver = svd.getSolver();
        RealMatrix inverse = solver.getInverse();
        Assert.assertEquals(inverse.getRowDimension(), testMatrix.getRowDimension());
        Assert.assertEquals(inverse.getColumnDimension(), testMatrix.getColumnDimension());
    }

    @Test(expected = MathRuntimeException.class)
    public void testSolver_SolveSingularMatrix() throws SingularValueDecompositionException {
        double[][] data = {{0.0, 1.0}, {1.0, 0.0}};
        RealMatrix singularMatrix = new Array2DRowRealMatrix(data, false);
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(singularMatrix);
        DecompositionSolver solver = svd.getSolver();
        RealVector b = new Array2DRowRealMatrix(new double[][]{{1.0}, {2.0}}, false).getColumnVector(0);
        RealVector x = solver.solve(b);
    }

}