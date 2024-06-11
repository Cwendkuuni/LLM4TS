import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorImpl;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class CholeskyDecompositionImplTest {

    @Test
    public void testCholeskyDecomposition() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0, 1.0}, {2.0, 5.0, 2.0}, {1.0, 2.0, 6.0}});
        CholeskyDecomposition choleskyDecomposition = new CholeskyDecompositionImpl(matrix);

        RealMatrix expectedLT = new LUDecomposition(matrix.transpose()).getSolver().getInverse();
        RealMatrix actualLT = choleskyDecomposition.getLT();
        assertEquals(expectedLT.toString(), actualLT.toString());

        RealMatrix expectedL = choleskyDecomposition.getL();
        RealMatrix actualL = expectedLT.transpose();
        assertEquals(expectedL.toString(), actualL.toString());

        double expectedDeterminant = 3.0 * 5.0 * 6.0 - 2.0 * 2.0 * 6.0 + 1.0 * 2.0 * 2.0;
        assertEquals(expectedDeterminant, choleskyDecomposition.getDeterminant(), 1e-9);

        DecompositionSolver solver = choleskyDecomposition.getSolver();
        RealVector vector = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
        RealVector result = solver.solve(vector);
        RealVector expectedResult = new ArrayRealVector(new double[]{1.5, -0.4, 0.5});
        assertEquals(expectedResult.toString(), result.toString());

        RealVector vector2 = new ArrayRealVector(new double[]{1.0, 2.0, 3.0, 4.0});
        RealVector expectedResult2 = new ArrayRealVector(new double[]{1.0, 0.4, -0.2, 0.125});
        RealVector result2 = solver.solve(new RealVector[] {vector, vector2});
        assertEquals(expectedResult2.toString(), result2.toString());

        RealMatrix matrix2 = new Array2DRowRealMatrix(new double[][]{{4.0, 2.0, 1.0, 3.0}, {2.0, 9.0, 2.0, 8.0},
                {1.0, 2.0, 5.0, 6.0}, {3.0, 8.0, 6.0, 12.0}});
        CholeskyDecomposition choleskyDecomposition2 = new CholeskyDecompositionImpl(matrix2, 1e-12, 1e-12);

        RealMatrix expectedLT2 = new LUDecomposition(matrix2.transpose()).getSolver().getInverse();
        RealMatrix actualLT2 = choleskyDecomposition2.getLT();
        assertEquals(expectedLT2.toString(), actualLT2.toString());

        RealMatrix expectedL2 = choleskyDecomposition2.getL();
        RealMatrix actualL2 = expectedLT2.transpose();
        assertEquals(expectedL2.toString(), actualL2.toString());

        double expectedDeterminant2 = 4.0 * 9.0 * 12.0 - 2.0 * 2.0 * 12.0 + 1.0 * 3.0 * 3.0;
        assertEquals(expectedDeterminant2, choleskyDecomposition2.getDeterminant(), 1e-9);

        RealVector vector3 = new ArrayRealVector(new double[]{1.0, 3.0, 5.0, 7.0});
        RealVector result3 = solver.solve(vector3);
        RealVector expectedResult3 = new ArrayRealVector(new double[]{-0.155740772, 0.463637234, 1.236842105, -1.543080635});
        assertEquals(expectedResult3.toString(), result3.toString());

        RealVector vector4 = new ArrayRealVector(new double[]{1.0, 3.0, 5.0, 7.0, 9.0});
        RealVector expectedResult4 = new ArrayRealVector(new double[]{0.306869563, -0.124315226, 0.203563791, -0.130434783, 0.115129259});
        RealVector result4 = solver.solve(new RealVector[] {vector3, vector4});
        assertEquals(expectedResult4.toString(), result4.toString());
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, -1.0}, {-1.0, 3.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0}, {2.0, 3.0}, {1.0, 1.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testNonSquareMatrixException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0}, {2.0}});
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() {
        RealMatrix matrix = new Array2DRowRealMatrix(new double[][]{{3.0, 2.0}, {2.0, 2.0}, {1.0, 1.0}, {1.0, 1.0}});
        new CholeskyDecompositionImpl(matrix, 1.0, -1.0);
    }

}