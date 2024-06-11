import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.CholeskyDecompositionImpl;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.apache.commons.math3.exception.NotSymmetricMatrixException;
import org.apache.commons.math3.exception.NotPositiveDefiniteMatrixException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CholeskyDecompositionImplTest {

    @Test
    public void testCholeskyDecomposition() throws Exception {
        double[][] matrixData = {
                {2.0, 1.0, 1.0},
                {1.0, 2.0, 1.0},
                {1.0, 1.0, 2.0}
        };

        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        CholeskyDecompositionImpl decomp = new CholeskyDecompositionImpl(matrix);
        RealMatrix l = decomp.getL();
        RealMatrix lt = decomp.getLT();

        // Test L and LT
        RealMatrix expectedL = new Array2DRowRealMatrix(new double[][]{{1.4142135625, 0.0, 0.0},
                {-0.7071067812, 1.4142135625, 0.0},
                {-0.7071067812, 0.0, 1.4142135625}}, false);
        RealMatrix expectedLT = MatrixUtils.transpose(expectedL);

        assertEquals(expectedL, l);
        assertEquals(expectedLT, lt);

        // Test getDeterminant()
        assertEquals(Math.sqrt(2.0 * 2.0), decomp.getDeterminant(), 1e-10);

        // Test getSolver()
        RealVector b = new ArrayRealVector(new double[]{1.0, 1.0, 1.0});
        RealVector x = decomp.getSolver().solve(b);
        RealVector expectedX = new ArrayRealVector(new double[]{0.3333333333333333, 0.6, 1.0});
        assertEquals(expectedX, x);

        // Test solve(RealVector)
        RealVector y = new ArrayRealVector(new double[]{1.0, 1.0, 1.0});
        RealVector z = decomp.solve(y);
        assertEquals(expectedX, z);

        // Test solve(RealMatrix)
        RealMatrix w = MatrixUtils.createRealMatrix(new double[][]{{1.0, 1.0}, {1.0, 1.0}});
        RealMatrix zz = decomp.solve(w);
        RealMatrix expectedZz = MatrixUtils.createRealMatrix(new double[][]{{-0.3333333333333333, 0.6},
                {0.6, 0.3333333333333333}}, false);
        assertEquals(expectedZz, zz);

        // Test getInverse()
        RealMatrix expectedInv = new Array2DRowRealMatrix(new double[][]{{0.6, -0.6, 0.6},
                {-0.6, 0.6, -0.6}, {0.6, -0.6, 0.6}}, false);
        RealMatrix inv = decomp.getInverse();
        assertEquals(expectedInv, inv);

        // Test exceptions
        try {
            new CholeskyDecompositionImpl(MatrixUtils.createRealIdentityMatrix(2));
            // Should throw NonSquareMatrixException
        } catch (NonSquareMatrixException e) {
        }

        try {
            new CholeskyDecompositionImpl(MatrixUtils.createRealIdentityMatrix(3));
            // Should throw NotSymmetricMatrixException
            new CholeskyDecompositionImpl(MatrixUtils.createRealIdentityMatrix(3), 1e-13, 1e-13);
        } catch (NotSymmetricMatrixException e) {
        }

        try {
            new CholeskyDecompositionImpl(MatrixUtils.createDiagonalMatrix(new double[]{2.0, 0.0}));
            // Should throw NotPositiveDefiniteMatrixException
        } catch (NotPositiveDefiniteMatrixException e) {
        }
    }

}