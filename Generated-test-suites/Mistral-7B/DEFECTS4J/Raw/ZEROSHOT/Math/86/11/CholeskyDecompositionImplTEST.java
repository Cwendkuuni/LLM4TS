import org.apache.commons.math3.linear.CholeskyDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class CholeskyDecompositionImplTest {

    @Test
    public void testCholeskyDecompositionImpl() {
        double[][] matrixData = {{1.0, 2.0, 2.0}, {2.0, 5.0, 3.0}, {2.0, 3.0, 5.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);

        try {
            CholeskyDecomposition choleskyDecomposition = new CholeskyDecompositionImpl(matrix);
            RealMatrix l = choleskyDecomposition.getL();
            RealMatrix lt = choleskyDecomposition.getLT();
            assertEquals(5.0, choleskyDecomposition.getDeterminant(), 0.0);

            RealVector b = new ArrayRealVector(new double[]{1.0, 2.0, 3.0});
            RealVector x = choleskyDecomposition.getSolver().solve(b);
            assertArrayEquals(new double[]{0.42261836, -0.84523672, 0.42261836}, x.toArray(), 0.0);

            RealMatrix A = new Array2DRowRealMatrix(matrixData, false);
            RealMatrix X = choleskyDecomposition.getSolver().solve(A);
            RealMatrix expectedX = new Array2DRowRealMatrix(new double[][]{{0.42261836, -0.84523672},
                    {-1.27939796, 1.98653581}, {0.42261836, -0.84523672}});
            assertEquals(expectedX, X);

        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonSquareMatrixException() {
        double[][] matrixData = {{1.0, 2.0}, {2.0, 5.0}, {2.0, 3.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        new CholeskyDecompositionImpl(matrix);
    }

    @Test(expected = NotSymmetricMatrixException.class)
    public void testNotSymmetricMatrixException() {
        double[][] matrixData = {{1.0, 2.0, 2.0}, {2.0, 5.0, 3.0}, {2.0, 3.0, 4.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        new CholeskyDecompositionImpl(matrix, 0.0, 0.0);
    }

    @Test(expected = NotPositiveDefiniteMatrixException.class)
    public void testNotPositiveDefiniteMatrixException() {
        double[][] matrixData = {{1.0, 2.0, 2.0}, {2.0, -1.0, 3.0}, {2.0, 3.0, -5.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData, false);
        new CholeskyDecompositionImpl(matrix, 0.0, 0.0);
    }

}