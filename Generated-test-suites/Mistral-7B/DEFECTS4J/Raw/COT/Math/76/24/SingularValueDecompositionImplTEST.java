import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImplConstructor() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        try {
            SingularValueDecompositionImpl decomposition = new SingularValueDecompositionImpl(matrix);
            assertEquals(2, decomposition.getRank());
        } catch (InvalidMatrixException e) {
            // This exception should not be thrown
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSingularValueDecompositionImplConstructorWithInvalidMatrix() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {1.0, 2.0}, {3.0, 4.0}});
        new SingularValueDecompositionImpl(matrix);
    }

    @Test
    public void testGetU() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl decomposition = new SingularValueDecompositionImpl(matrix);
        RealMatrix expectedU = new Array2DRowRealMatrix(new double[][]{{0.3420201435517848, 0.6548761642482488},
                {-0.6548761642482488, 0.3420201435517848}});
        RealMatrix u = decomposition.getU();
        assertEquals(2, u.getRowDimension());
        assertEquals(2, u.getColumnDimension());
        assertTrue(u.isSimilarTo(expectedU, 1e-9));
    }

    @Test(expected = InvalidMatrixException.class)
    public void testGetUTWhenUIsNotFound() throws InvalidMatrixException {
        SingularValueDecompositionImpl decomposition = new SingularValueDecompositionImpl(new Array2DRowRealMatrix(new double[][]{{1.0}}));
        decomposition.getUT();
    }

    @Test
    public void testGetS() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl decomposition = new SingularValueDecompositionImpl(matrix);
        RealMatrix expectedS = new DiagonalMatrix(new double[]{2.4494897427831, 4.4194320915676});
        RealMatrix s = decomposition.getS();
        assertEquals(2, s.getRowDimension());
        assertEquals(2, s.getColumnDimension());
        assertTrue(s.isSimilarTo(expectedS, 1e-9));
    }

    @Test(expected = InvalidMatrixException.class)
    public void testGetSingularValuesWhenSingularValuesAreNotFound() throws InvalidMatrixException {
        SingularValueDecompositionImpl decomposition = new SingularValueDecompositionImpl(new Array2DRowRealMatrix(new double[][]{{1.0}}));
        decomposition.getSingularValues();
    }

    @Test
    public void testGetV() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl decomposition = new SingularValueDecompositionImpl(matrix);
        RealMatrix expectedV = new Array2DRowRealMatrix(new double[][]{{-0.433012701892219}, {0.86602540378442}});
        RealMatrix v = decomposition.getV();
        assertEquals(2, v.getRowDimension());
        assertEquals(2, v.getColumnDimension());
        assertTrue(v.isSimilarTo(expectedV, 1e-9));
    }

    @Test(expected = InvalidMatrixException.class)
    public void testGetVTWhenVIsNotFound() throws InvalidMatrixException {
        SingularValueDecompositionImpl decomposition = new SingularValueDecompositionImpl(new Array2DRowRealMatrix(new double[][]{{1.0}}));
        decomposition.getVT();
    }

    @Test
    public void testGetCovariance() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl decomposition = new SingularValueDecompositionImpl(matrix);
        RealMatrix covariance = decomposition.getCovariance(1.5);
        RealMatrix expectedCovariance = new Array2DRowRealMatrix(new double[][]{{0.4545454545454545, 0.2222222222222222},
                {0.2222222222222222, 0.1111111111111111}});
        assertTrue(covariance.isSimilarTo(expectedCovariance, 1e-9));
    }

    @Test(expected = MathRuntimeException.class)
    public void testGetCovarianceWithNegativeValue() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl decomposition = new SingularValueDecompositionImpl(matrix);
        decomposition.getCovariance(-1.5);
    }

    @Test
    public void testGetNorm() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl decomposition = new SingularValueDecompositionImpl(matrix);
        double norm = decomposition.getNorm();
        assertEquals(3.7416573867739413, norm, 1e-9);
    }

    @Test
    public void testGetConditionNumber() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl decomposition = new SingularValueDecompositionImpl(matrix);
        double conditionNumber = decomposition.getConditionNumber();
        assertEquals(3.7416573867739413 / 1.0, conditionNumber, 1e-9);
    }

    @Test
    public void testGetRank() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl decomposition = new SingularValueDecompositionImpl(matrix);
        int rank = decomposition.getRank();
        assertEquals(2, rank);
    }

    @Test
    public void testGetSolver() throws InvalidMatrixException {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0}, {2.0, 3.0}});
        SingularValueDecompositionImpl decomposition = new SingularValueDecompositionImpl(matrix);
        DecompositionSolver solver = decomposition.getSolver();
        RealVector b = new ArrayRealVector(new double[]{1.0, 2.0});
        RealVector x = solver.solve(b);
        assertEquals(0.0, x.getEntry(0) - 1.131839385376829, 1e-9);
        assertEquals(0.0, x.getEntry(1) - 1.2104149453123354, 1e-9);
    }

}