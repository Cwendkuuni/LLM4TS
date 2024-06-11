package org.apache.commons.math.linear;

import org.apache.commons.math3.exception.SingularValueDecompositionException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear. SingularValueDecomposition;
import org.apache.commons.math3.special.Epsilon;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SingularValueDecompositionImplTest {

    private static final double EPSILON = Epsilon.DOUBLE_EPSILON;

    private RealMatrix matrixA;
    private SingularValueDecompositionImpl svd;

    @Before
    public void setup() {
        double[][] dataA = new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        };
        matrixA = new Array2DRowRealMatrix(dataA, false);
    }

    @Test
    public void testSingularValueDecompositionImpl() throws SingularValueDecompositionException {
        svd = new SingularValueDecompositionImpl(matrixA);
        Assert.assertEquals(3, svd.getRank());
    }

    @Test
    public void testGetU() throws SingularValueDecompositionException {
        RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{
                {-0.53452254,  0.85705888, -0.08258683},
                {-0.84764262, -0.48489561,  0.44535843},
                { 0.0,         0.0,         1.0}
        });
        RealMatrix actualU = svd.getU();
        Assert.assertEquals(expectedU, actualU);
    }

    @Test
    public void testGetUT() throws SingularValueDecompositionException {
        RealMatrix expectedUT = new Array2DRowRealMatrix(new double[][]{
                {-0.53452254, -0.85705888,  0.0},
                {-0.84764262, -0.48489561,  0.0},
                { 0.0,         0.0,         1.0}
        });
        RealMatrix actualUT = svd.getUT();
        Assert.assertEquals(expectedUT, actualUT);
    }

    @Test
    public void testGetS() throws SingularValueDecompositionException {
        RealMatrix expectedS = MatrixUtils.createRealDiagonalMatrix(new double[]{1.0, 0.73438588, 0.0});
        RealMatrix actualS = svd.getS();
        Assert.assertEquals(expectedS, actualS);
    }

    @Test
    public void testGetSingularValues() throws SingularValueDecompositionException {
        double[] expectedSingularValues = new double[]{1.0, 0.73438588, 0.0};
        double[] actualSingularValues = svd.getSingularValues();
        Assert.assertArrayEquals(expectedSingularValues, actualSingularValues, EPSILON);
    }

    @Test
    public void testGetV() throws SingularValueDecompositionException {
        RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{
                {-0.01090412,  0.94868334, -0.27559055},
                {-0.38268343, -0.08258683, -0.94868334},
                { 0.91629265,  0.27559055,  0.0}
        });
        RealMatrix actualV = svd.getV();
        Assert.assertEquals(expectedV, actualV);
    }

    @Test
    public void testGetVT() throws SingularValueDecompositionException {
        RealMatrix expectedVT = new Array2DRowRealMatrix(new double[][]{
                {-0.01090412, -0.94868334,  0.27559055},
                {-0.38268343, -0.08258683, -0.94868334},
                { 0.91629265,  0.27559055,  0.0}
        });
        RealMatrix actualVT = svd.getVT();
        Assert.assertEquals(expectedVT, actualVT);
    }

    @Test(expected = InvalidMatrixException.class)
    public void testSingularValueDecompositionWithInvalidMatrix() throws SingularValueDecompositionException {
        SingularValueDecompositionImpl svd = null;
        try {
            svd = new SingularValueDecompositionImpl(new Array2DRowRealMatrix(new double[][]{
                    {1.0, 2.0},
                    {2.0, 1.0},
                    {0.0, 0.0}
            }));
        } catch (InvalidMatrixException e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    @Test
    public void testGetCovariance() throws SingularValueDecompositionException {
        double minSingularValue = 0.5;
        RealMatrix expectedCovariance = new Array2DRowRealMatrix(new double[][]{
                {0.00454826, 0.00909652},
                {0.00909652, 0.01819303}
        });
        RealMatrix actualCovariance = svd.getCovariance(minSingularValue);
        Assert.assertEquals(expectedCovariance, actualCovariance);
    }

    @Test
    public void testGetNorm() throws SingularValueDecompositionException {
        Assert.assertEquals(1.0, svd.getNorm(), EPSILON);
    }

    @Test
    public void testGetConditionNumber() throws SingularValueDecompositionException {
        Assert.assertEquals(13.55137528, svd.getConditionNumber(), EPSILON);
    }

    @Test
    public void testGetRank() throws SingularValueDecompositionException {
        Assert.assertEquals(3, svd.getRank());
    }

    @Test
    public void testGetSolver() throws SingularValueDecompositionException {
        DecompositionSolver solver = svd.getSolver();
        double[] b = new double[]{1.0, 2.0, 3.0};
        double[] solution = solver.solve(b);
        Assert.assertArrayEquals(new double[]{0.36488298, 0.57542028, 1.0}, solution, EPSILON);
    }
}