package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.special.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for SingularValueDecompositionImpl.
 */
public class SingularValueDecompositionImplTest {

    private static final int ROWS = 3;
    private static final int COLUMNS = 3;
    private static final double EPSILON = 1e-12;

    private RealMatrix matrix;
    private SingularValueDecompositionImpl decomposition;

    @Before
    public void setUp() {
        double[][] data = {
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        };
        matrix = new Array2DRowRealMatrix(data, false);
        try {
            decomposition = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test constructor with valid input.
     */
    @Test
    public void testConstructor() {
        Assert.assertNotNull(decomposition);
    }

    /**
     * Test getU() with valid input.
     */
    @Test
    public void testGetU() throws InvalidMatrixException {
        RealMatrix expected = new Array2DRowRealMatrix(
                new double[][]{{0.3333333333333333, 0.6666666666666666, -0.6666666666666666},
                              {-0.857416365538773, 0.4285714285714286, 0.4285714285714286},
                              {-0.4898979591836735, -0.8164965809277263, 0.408248290463863}},
                false);
        RealMatrix result = decomposition.getU();
        Assert.assertEquals(expected, result);
    }

    /**
     * Test getUT() with valid input.
     */
    @Test
    public void testGetUT() throws InvalidMatrixException {
        RealMatrix expected = new Array2DRowRealMatrix(
                new double[][]{{0.3333333333333333, -0.857416365538773, -0.4898979591836735},
                {0.6666666666666666, 0.4285714285714286, -0.8164965809277263},
                {-0.6666666666666666, 0.4285714285714286, 0.408248290463863}},
                false);
        RealMatrix result = decomposition.getUT();
        Assert.assertEquals(expected, result);
    }

    /**
     * Test getS() with valid input.
     */
    @Test
    public void testGetS() throws InvalidMatrixException {
        double[] expected = {3.7416573867739413, 2.4494897427831775, 0.0};
        double[] result = decomposition.getSingularValues();
        Assert.assertArrayEquals(expected, result, EPSILON);
    }

    /**
     * Test getVT() with valid input.
     */
    @Test
    public void testGetVT() throws InvalidMatrixException {
        RealMatrix expected = new Array2DRowRealMatrix(
                new double[][]{{0.574526812636823, 0.5385166393020224, 0.502245816283154},
                {-0.5385166393020224, 0.8164965809277263, 0.408248290463863},
                {-0.502245816283154, -0.408248290463863, 0.4285714285714286}},
                false);
        RealMatrix result = decomposition.getVT();
        Assert.assertEquals(expected, result);
    }

    /**
     * Test getV() with valid input.
     */
    @Test
    public void testGetV() throws InvalidMatrixException {
        RealMatrix expected = new Array2DRowRealMatrix(
                new double[][]{{-0.574526812636823, 0.5385166393020224, -0.502245816283154},
                {-0.5385166393020224, -0.8164965809277263, 0.408248290463863},
                {0.502245816283154, 0.408248290463863, 0.4285714285714286}},
                false);
        RealMatrix result = decomposition.getV();
        Assert.assertEquals(expected, result);
    }

    /**
     * Test getCovariance() with valid input.
     */
    @Test
    public void testGetCovariance() throws InvalidMatrixException {
        RealMatrix result = decomposition.getCovariance(1.0);
        double[][] expectedData = {{0.282842712474619, 0.219332135433772, 0.15625},
                                    {0.219332135433772, 0.282842712474619, 0.15625},
                                    {0.15625, 0.15625, 0.175981645606411}};
        RealMatrix expected = new Array2DRowRealMatrix(expectedData, false);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test getNorm() with valid input.
     */
    @Test
    public void testGetNorm() throws InvalidMatrixException {
        Assert.assertEquals(3.7416573867739413, decomposition.getNorm(), EPSILON);
    }

    /**
     * Test getConditionNumber() with valid input.
     */
    @Test
    public void testGetConditionNumber() throws InvalidMatrixException {
        Assert.assertEquals(12.42541464512233, decomposition.getConditionNumber(), EPSILON);
    }

    /**
     * Test getRank() with valid input.
     */
    @Test
    public void testGetRank() throws InvalidMatrixException {
        Assert.assertEquals(3, decomposition.getRank());
    }

    /**
     * Test getSolver() with valid input.
     */
    @Test
    public void testGetSolver() throws InvalidMatrixException {
        DecompositionSolver solver = decomposition.getSolver();
        RealVector b = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector x = solver.solve(b);
        double[] expected = {-2.9237730938948965E-5, -2.7557575555727698E-5, 1.0000000000000001};
        Assert.assertArrayEquals(expected, x.toArray(), EPSILON);
    }
}