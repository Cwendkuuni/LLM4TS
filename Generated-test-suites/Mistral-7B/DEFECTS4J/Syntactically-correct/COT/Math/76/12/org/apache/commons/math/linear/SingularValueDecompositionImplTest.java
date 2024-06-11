package org.apache.commons.math.linear;

import org.apache.commons.math3.exception.SingularValueException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SingularValueException;
import org.apache.commons.math3.linear.Solver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for SingularValueDecompositionImpl.
 */
public class SingularValueDecompositionImplTest {

    private SingularValueDecompositionImpl singularValueDecomposition;
    private RealMatrix matrix;

    @Before
    public void setUp() throws SingularValueException, InvalidMatrixException {
        double[][] data = { {1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0} };
        matrix = new Array2DRowRealMatrix(data, false);
        singularValueDecomposition = new SingularValueDecompositionImpl(matrix);
    }

    @Test
    public void testGetU() throws SingularValueException, InvalidMatrixException {
        RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{{0.54654932, -0.81649660, 0.25886025},
                {-0.81649660, 0.54654932, 0.25886025}, {0.25886025, 0.25886025, 0.94140846}}, false);
        RealMatrix actualU = singularValueDecomposition.getU();
        Assert.assertArrayEquals(expectedU.getData(), actualU.getData(), 0.001);
    }

    @Test
    public void testGetUT() throws SingularValueException, InvalidMatrixException {
        RealMatrix expectedUT = new Array2DRowRealMatrix(new double[][]{{0.54654932, -0.81649660, 0.25886025},
                {-0.81649660, 0.54654932, 0.25886025}, {0.25886025, 0.25886025, 0.94140846}}, false);
        RealMatrix actualUT = singularValueDecomposition.getUT();
        Assert.assertArrayEquals(expectedUT.getData(), actualUT.getData(), 0.001);
    }

    @Test
    public void testGetS() throws SingularValueException, InvalidMatrixException {
        double[] expectedSingularValues = {11.180339887558179, 5.712243602654339, 0.0};
        double[] actualSingularValues = singularValueDecomposition.getSingularValues();
        Assert.assertArrayEquals(expectedSingularValues, actualSingularValues, 0.001);
    }

    @Test
    public void testGetSingularValues() throws SingularValueException, InvalidMatrixException {
        double[] expectedSingularValues = {11.180339887558179, 5.712243602654339, 0.0};
        double[] actualSingularValues = singularValueDecomposition.getSingularValues();
        Assert.assertArrayEquals(expectedSingularValues, actualSingularValues, 0.001);
    }

    @Test
    public void testGetV() throws SingularValueException, InvalidMatrixException {
        RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{{-0.05132061, 0.83208803, -0.56268474},
                {-0.83208803, -0.05132061, -0.56268474}, {-0.56268474, 0.56268474, 0.83208803}}, false);
        RealMatrix actualV = singularValueDecomposition.getV();
        Assert.assertArrayEquals(expectedV.getData(), actualV.getData(), 0.001);
    }

    @Test
    public void testGetVT() throws SingularValueException, InvalidMatrixException {
        RealMatrix expectedVT = new Array2DRowRealMatrix(new double[][]{{-0.05132061, -0.83208803, -0.56268474},
                {0.83208803, -0.05132061, -0.56268474}, {0.56268474, 0.56268474, 0.83208803}}, false);
        RealMatrix actualVT = singularValueDecomposition.getVT();
        Assert.assertArrayEquals(expectedVT.getData(), actualVT.getData(), 0.001);
    }

    @Test(expected = SingularValueException.class)
    public void testGetCovarianceWithNegativeSingularValue()
            throws SingularValueException, InvalidMatrixException {
        singularValueDecomposition.getSingularValues()[1] *= -1;
        singularValueDecomposition.getCovariance(0.5);
    }

    @Test
    public void testGetNorm() throws SingularValueException, InvalidMatrixException {
        Assert.assertEquals(11.180339887558179, singularValueDecomposition.getNorm(), 0.001);
    }

    @Test
    public void testGetConditionNumber() throws SingularValueException, InvalidMatrixException {
        Assert.assertEquals(21.236419775116358, singularValueDecomposition.getConditionNumber(), 0.001);
    }

    @Test
    public void testGetRank() throws SingularValueException, InvalidMatrixException {
        Assert.assertEquals(3, singularValueDecomposition.getRank());
    }

    @Test
    public void testGetSolver() throws SingularValueException, InvalidMatrixException {
        DecompositionSolver solver = singularValueDecomposition.getSolver();
        Assert.assertTrue(solver instanceof Solver);
    }
}