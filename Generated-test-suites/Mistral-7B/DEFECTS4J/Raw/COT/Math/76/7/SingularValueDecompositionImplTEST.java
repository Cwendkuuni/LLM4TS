package org.apache.commons.math.linear;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImplConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                     {4.0, 5.0, 6.0},
                                                                     {7.0, 8.0, 9.0}});
        try {
            SingularValueDecompositionImpl singularValueDecompositionImpl =
                    new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            throw new TestFailureException("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testSingularValueDecompositionImplGetU() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                     {4.0, 5.0, 6.0},
                                                                     {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl singularValueDecompositionImpl =
                new SingularValueDecompositionImpl(matrix);
        RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{{0.53846068, -0.82704983, 0.11102003},
                                                                          {-0.82704983, 0.53846068, -0.11102003},
                                                                          {0.11102003, -0.11102003, 0.95734391}});
        RealMatrix u = singularValueDecompositionImpl.getU();
        assertEquals(expectedU, u);
    }

    @Test
    public void testSingularValueDecompositionImplGetUT() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                     {4.0, 5.0, 6.0},
                                                                     {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl singularValueDecompositionImpl =
                new SingularValueDecompositionImpl(matrix);
        RealMatrix expectedUt = new Array2DRowRealMatrix(new double[][]{{0.53846068, -0.82704983, 0.11102003},
                                                                          {-0.82704983, 0.53846068, -0.11102003},
                                                                          {0.11102003, -0.11102003, 0.95734391}});
        RealMatrix ut = singularValueDecompositionImpl.getUT();
        assertEquals(expectedUt, ut);
    }

    @Test
    public void testSingularValueDecompositionImplGetS() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                     {4.0, 5.0, 6.0},
                                                                     {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl singularValueDecompositionImpl =
                new SingularValueDecompositionImpl(matrix);
        RealMatrix expectedS = new Array2DRowRealMatrix(new double[][]{{15.2113188},
                                                                         {7.37568155},
                                                                         {2.44948974}});
        RealMatrix s = singularValueDecompositionImpl.getS();
        assertEquals(expectedS, s);
    }

    @Test
    public void testSingularValueDecompositionImplGetSingularValues() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                     {4.0, 5.0, 6.0},
                                                                     {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl singularValueDecompositionImpl =
                new SingularValueDecompositionImpl(matrix);
        double[] expectedSingularValues = {15.2113188, 7.37568155, 2.44948974};
        double[] singularValues = singularValueDecompositionImpl.getSingularValues();
        assertNotNull(singularValues);
        for (int i = 0; i < singularValues.length; ++i) {
            assertEquals(expectedSingularValues[i], singularValues[i], 1e-12);
        }
    }

    @Test
    public void testSingularValueDecompositionImplGetV() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                     {4.0, 5.0, 6.0},
                                                                     {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl singularValueDecompositionImpl =
                new SingularValueDecompositionImpl(matrix);
        RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{{-0.09250801, -0.42695203, -0.86586068},
                                                                          {-0.42695203, 0.86586068, 0.09250801},
                                                                          {0.86586068, 0.09250801, -0.42695203}});
        RealMatrix v = singularValueDecompositionImpl.getV();
        assertEquals(expectedV, v);
    }

    @Test
    public void testSingularValueDecompositionImplGetVT() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                     {4.0, 5.0, 6.0},
                                                                     {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl singularValueDecompositionImpl =
                new SingularValueDecompositionImpl(matrix);
        RealMatrix expectedVt = new Array2DRowRealMatrix(new double[][]{{-0.09250801, -0.42695203, 0.86586068},
                                                                          {-0.42695203, 0.86586068, -0.09250801},
                                                                          {0.86586068, 0.09250801, 0.42695203}});
        RealMatrix vt = singularValueDecompositionImpl.getVT();
        assertEquals(expectedVt, vt);
    }

    @Test
    public void testSingularValueDecompositionImplGetCovariance() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                     {4.0, 5.0, 6.0},
                                                                     {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl singularValueDecompositionImpl =
                new SingularValueDecompositionImpl(matrix, 2);
        RealMatrix expectedCovariance = new Array2DRowRealMatrix(new double[][]{{2.56113282, 1.91921457},
                                                                                 {1.91921457, 2.56113282}});
        RealMatrix covariance = singularValueDecompositionImpl.getCovariance(0);
        assertEquals(expectedCovariance, covariance);
    }

    @Test
    public void testSingularValueDecompositionImplGetNorm() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                     {4.0, 5.0, 6.0},
                                                                     {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl singularValueDecompositionImpl =
                new SingularValueDecompositionImpl(matrix);
        assertEquals(15.2113188, singularValueDecompositionImpl.getNorm(), 1e-12);
    }

    @Test
    public void testSingularValueDecompositionImplGetConditionNumber() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                     {4.0, 5.0, 6.0},
                                                                     {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl singularValueDecompositionImpl =
                new SingularValueDecompositionImpl(matrix);
        assertEquals(3.85433211, singularValueDecompositionImpl.getConditionNumber(), 1e-12);
    }

    @Test
    public void testSingularValueDecompositionImplGetRank() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                     {4.0, 5.0, 6.0},
                                                                     {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl singularValueDecompositionImpl =
                new SingularValueDecompositionImpl(matrix);
        assertEquals(3, singularValueDecompositionImpl.getRank());
    }

    @Test
    public void testSingularValueDecompositionImplGetSolver() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                     {4.0, 5.0, 6.0},
                                                                     {7.0, 8.0, 9.0}});
        SingularValueDecompositionImpl singularValueDecompositionImpl =
                new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = singularValueDecompositionImpl.getSolver();
        assertTrue(solver instanceof SingularValueDecompositionImpl.Solver);
    }

}