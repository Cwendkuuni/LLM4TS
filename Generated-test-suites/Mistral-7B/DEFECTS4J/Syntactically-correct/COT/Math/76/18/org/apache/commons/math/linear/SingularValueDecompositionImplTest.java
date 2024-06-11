import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.SingularValueDecompositionImpl;
import org.apache.commons.math3.linear.TransformerFactory;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SingularValueDecompositionImplTest {

    @Test
    public void testSingularValueDecompositionImpl() {
        // Test constructing SingularValueDecompositionImpl with valid input
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}});
        SingularValueDecompositionImpl svd = null;
        try {
            svd = new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            e.printStackTrace();
        }
        assertNotNull(svd);

        // Test getU, getUT, getS, and getSingularValues
        RealMatrix u = svd.getU();
        RealMatrix ut = svd.getUT();
        RealMatrix s = svd.getS();
        double[] singularValues = svd.getSingularValues();
        assertEquals(matrix.getRowDimension(), u.getRowDimension());
        assertEquals(matrix.getColumnDimension(), u.getColumnDimension());
        assertEquals(matrix.getRowDimension(), ut.getRowDimension());
        assertEquals(matrix.getColumnDimension(), ut.getColumnDimension());
        assertEquals(singularValues.length, s.getRowDimension());
        assertEquals(singularValues.length, s.getColumnDimension());
        assertEquals(3, u.getRowDimension());
        assertEquals(3, u.getColumnDimension());
        assertEquals(3, ut.getRowDimension());
        assertEquals(3, ut.getColumnDimension());
        assertEquals(3, s.getRowDimension());
        assertEquals(3, s.getColumnDimension());

        // Test getV and getVT
        RealMatrix v = svd.getV();
        RealMatrix vt = svd.getVT();
        assertEquals(matrix.getColumnDimension(), v.getRowDimension());
        assertEquals(matrix.getColumnDimension(), v.getColumnDimension());
        assertEquals(3, v.getRowDimension());
        assertEquals(3, v.getColumnDimension());
        assertEquals(3, vt.getRowDimension());
        assertEquals(matrix.getRowDimension(), vt.getColumnDimension());

        // Test getCovariance
        double minSingularValue = singularValues[singularValues.length - 1];
        RealMatrix covariance = svd.getCovariance(minSingularValue);
        assertEquals(minSingularValue, covariance.getMaxElement(), 0.0);

        // Test getNorm
        double norm = svd.getNorm();
        assertEquals(singularValues[0], norm, 0.0);

        // Test getConditionNumber
        double conditionNumber = svd.getConditionNumber();
        assertEquals(singularValues[0] / singularValues[2], conditionNumber, 0.0);

        // Test getRank
        int rank = svd.getRank();
        assertEquals(3, rank);

        // Test getSolver
        DecompositionSolver solver = svd.getSolver();
        assertTrue(solver instanceof SingularValueDecompositionImpl.Solver);

        // Test solve method with RealVector
        RealVector b = new Array2DRowRealMatrix(new double[][]{
                {1.0, 0.0, 0.0}
        }).operate(svd.getV());
        RealVector solution = solver.solve(b);
        assertArrayEquals(new double[]{1.0 / singularValues[0], 0.0, 0.0},
                solution.toArray(), 0.0);

        // Test solve method with double[]
        double[] x = new double[]{0.0, 0.0, 0.0};
        double[] solution2 = solver.solve(x);
        assertArrayEquals(new double[]{1.0 / singularValues[0], 0.0, 0.0},
                solution2, 0.0);

        // Test isNonSingular method
        assertTrue(solver.isNonSingular());

        // Test getInverse method
        RealMatrix inverse = solver.getInverse();
        assertNotNull(inverse);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSingularValueDecompositionImplInvalidInput() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0, 3.0}, {3.0, 4.0, 5.0}, {6.0, 7.0, 8.0}
        });
        try {
            new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            throw new IllegalArgumentException("Unexpected exception", e);
        }
    }

    @Test(expected = MathRuntimeException.class)
    public void testSingularValueDecompositionImplConvergenceException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}, {7.0, 8.0}, {9.0, 10.0}});
        try {
            new SingularValueDecompositionImpl(matrix, 10);
        } catch (InvalidMatrixException e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    @Test(expected = MathRuntimeException.class)
    public void testSingularValueDecompositionImplSingularityException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {0.0, 0.0}, {0.0, 0.0}, {0.0, 0.0}
        });
        try {
            new SingularValueDecompositionImpl(matrix);
        } catch (InvalidMatrixException e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    @Test
    public void testTransformerFactory() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{
                {1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}
        });
        BiDiagonalTransformer transformer = new BiDiagonalTransformer(matrix);
        assertTrue(transformer instanceof TransformerFactory.Transformer);
        assertTrue(transformer instanceof SingularValueDecompositionImpl.BiDiagonalTransformer);
    }
}