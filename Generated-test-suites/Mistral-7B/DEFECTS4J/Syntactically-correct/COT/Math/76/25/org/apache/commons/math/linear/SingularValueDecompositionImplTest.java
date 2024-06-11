import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImplConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}, {3, 4}});
        try {
            SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
            assertNotNull(svd);
        } catch (InvalidMatrixException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = InvalidMatrixException.class)
    public void testSingularValueDecompositionImplConstructorWithInvalidMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}, {3, 3}});
        new SingularValueDecompositionImpl(matrix);
    }

    @Test
    public void testGetU() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix u = svd.getU();
        assertEquals(3, u.getRowDimension());
        assertEquals(3, u.getColumnDimension());
        RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{{0.612, 0.756, 0.408},
                {-0.756, 0.612, -0.408}, {-0.408, -0.408, 0.816}});
        assertArrayEquals(expectedU.getData(), u.getData(), 0.001);
    }

    @Test
    public void testGetUT() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix ut = svd.getUT();
        assertEquals(3, ut.getRowDimension());
        assertEquals(3, ut.getColumnDimension());
        RealMatrix expectedUt = new Array2DRowRealMatrix(new double[][]{{0.612, -0.756, -0.408},
                {0.756, 0.612, 0.408}, {-0.408, 0.408, 0.816}});
        assertArrayEquals(expectedUt.getData(), ut.getData(), 0.001);
    }

    @Test
    public void testGetS() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix s = svd.getS();
        assertEquals(3, s.getRowDimension());
        assertEquals(3, s.getColumnDimension());
        double[] expectedSingularValues = {1.1701714847283845, 1.151430270476811, 0.0};
        assertArrayEquals(expectedSingularValues, s.getData(), 0.001);
    }

    @Test
    public void testGetSingularValues() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double[] singularValues = svd.getSingularValues();
        assertArrayEquals(new double[]{1.1701714847283845, 1.151430270476811, 0.0}, singularValues, 0.001);
    }

    @Test
    public void testGetV() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix v = svd.getV();
        assertEquals(3, v.getRowDimension());
        assertEquals(3, v.getColumnDimension());
        RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{{-0.609, -0.781, -0.059},
                {-0.781, 0.609, 0.059}, {-0.059, 0.059, 0.707}});
        assertArrayEquals(expectedV.getData(), v.getData(), 0.001);
    }

    @Test
    public void testGetVT() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        RealMatrix vt = svd.getVT();
        assertEquals(3, vt.getRowDimension());
        assertEquals(3, vt.getColumnDimension());
        RealMatrix expectedVt = new Array2DRowRealMatrix(new double[][]{{-0.609, -0.781, -0.059},
                {-0.781, 0.609, 0.059}, {-0.059, 0.059, 0.707}});
        assertArrayEquals(expectedVt.getData(), vt.getData(), 0.001);
    }

    @Test
    public void testGetCovariance() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double minSingularValue = 1.1;
        RealMatrix covariance = svd.getCovariance(minSingularValue);
        assertEquals(3, covariance.getRowDimension());
        assertEquals(3, covariance.getColumnDimension());
        double[][] data = covariance.getData();
        assertEquals(0, data[0][0], 0.001);
        assertEquals(0, data[0][1], 0.001);
        assertEquals(1.4545454545454545, data[0][2], 0.001);
        assertEquals(1.4545454545454545, data[1][0], 0.001);
        assertEquals(0, data[1][1], 0.001);
        assertEquals(1.4545454545454545, data[1][2], 0.001);
        assertEquals(0, data[2][0], 0.001);
        assertEquals(0, data[2][1], 0.001);
        assertEquals(1.4545454545454545, data[2][2], 0.001);
    }

    @Test
    public void testGetNorm() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double norm = svd.getNorm();
        assertEquals(1.1701714847283845, norm, 0.001);
    }

    @Test
    public void testGetConditionNumber() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        double conditionNumber = svd.getConditionNumber();
        assertEquals(1.1701714847283845 / 0.0, conditionNumber, 0.001);
    }

    @Test
    public void testGetRank() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        int rank = svd.getRank();
        assertEquals(3, rank);
    }

    @Test
    public void testGetSolver() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1, 2}, {2, 3}, {3, 4}});
        SingularValueDecompositionImpl svd = new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = svd.getSolver();
        assertTrue(solver instanceof Solver);
        Solver solverCast = (Solver) solver;
        RealMatrix pseudoInverse = solverCast.getInverse();
        RealMatrix expectedPseudoInverse = new Array2DRowRealMatrix(new double[][]{{0.612, 0.756, 0.408},
                {-0.756, 0.612, -0.408}, {-0.408, -0.408, 0.816}});
        assertArrayEquals(expectedPseudoInverse.getData(), pseudoInverse.getData(), 0.001);
    }
}