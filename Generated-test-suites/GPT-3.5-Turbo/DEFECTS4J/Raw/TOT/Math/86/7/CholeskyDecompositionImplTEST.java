import org.apache.commons.math.MathRuntimeException;
import org.apache.commons.math.linear.*;
import org.junit.Assert;
import org.junit.Test;


public class CholeskyDecompositionImplTest {

    @Test
    public void testCholeskyDecomposition() {
        // create a positive definite matrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {
            {4, 12, -16},
            {12, 37, -43},
            {-16, -43, 98}
        });

        try {
            CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);

            // test getL() method
            RealMatrix expectedL = MatrixUtils.createRealMatrix(new double[][] {
                {2, 0, 0},
                {6, 1, 0},
                {-8, 5, 3}
            });
            Assert.assertEquals(expectedL, decomposition.getL());

            // test getDeterminant() method
            double expectedDeterminant = 36;
            Assert.assertEquals(expectedDeterminant, decomposition.getDeterminant(), 0);

            // test getSolver() method
            double[] b = {1, 2, -3};
            RealVector expectedSolution = MatrixUtils.createRealVector(new double[] {0.5, -4, 2});
            Assert.assertEquals(expectedSolution, decomposition.getSolver().solve(MatrixUtils.createRealVector(b)));

        } catch (Exception e) {
            Assert.fail("Exception caught: " + e.getMessage());
        }
    }

    @Test
    public void testInvalidMatrix() {
        // create an invalid matrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {
            {1, 2, 3},
            {4, 5, 6}
        });

        try {
            CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
            Assert.fail("Exception not caught");
        } catch (NonSquareMatrixException e) {
            // expected exception
        } catch (Exception e) {
            Assert.fail("Unexpected exception caught: " + e.getMessage());
        }
    }

    @Test
    public void testNotSymmetricMatrix() {
        // create a non-symmetric matrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        });

        try {
            CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
            Assert.fail("Exception not caught");
        } catch (NotSymmetricMatrixException e) {
            // expected exception
        } catch (Exception e) {
            Assert.fail("Unexpected exception caught: " + e.getMessage());
        }

        // create a nearly symmetric matrix
        matrix = MatrixUtils.createRealMatrix(new double[][] {
            {4, 12, -15.9999},
            {12, 37, -43},
            {-16, -43, 98}
        });

        try {
            CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
            Assert.fail("Exception not caught");
        } catch (NotSymmetricMatrixException e) {
            // expected exception
        } catch (Exception e) {
            Assert.fail("Unexpected exception caught: " + e.getMessage());
        }
    }

    @Test
    public void testNotPositiveDefiniteMatrix() {
        // create a non-positive definite matrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {
            {4, 12, -16},
            {12, 37, -43},
            {-16, -43, 99}
        });

        try {
            CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
            Assert.fail("Exception not caught");
        } catch (NotPositiveDefiniteMatrixException e) {
            // expected exception
        } catch (Exception e) {
            Assert.fail("Unexpected exception caught: " + e.getMessage());
        }

        // create a nearly positive definite matrix
        matrix = MatrixUtils.createRealMatrix(new double[][] {
            {4, 12, -16},
            {12, 37, -43},
            {-16, -43, 98.9999}
        });

        try {
            CholeskyDecompositionImpl decomposition = new CholeskyDecompositionImpl(matrix);
            Assert.fail("Exception not caught");
        } catch (NotPositiveDefiniteMatrixException e) {
            // expected exception
        } catch (Exception e) {
            Assert.fail("Unexpected exception caught: " + e.getMessage());
        }
    }

}