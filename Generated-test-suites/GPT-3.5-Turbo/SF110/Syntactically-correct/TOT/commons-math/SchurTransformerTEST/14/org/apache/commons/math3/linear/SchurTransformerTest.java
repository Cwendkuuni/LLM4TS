import org.junit.Test;
import org.apache.commons.math3.linear.RealMatrix;

public class SchurTransformerTest {

    @Test
    public void testSchurTransformation_typicalCase() {
        // Typical case: a square matrix is used for Schur transformation
        double[][] inputArray = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(inputArray);

        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        RealMatrix pMatrix = transformer.getP();
        RealMatrix ptMatrix = transformer.getPT();
        RealMatrix tMatrix = transformer.getT();

        // Assertion statements
        // Test the dimensions of output matrices
        assertEquals(3, pMatrix.getRowDimension());
        assertEquals(3, pMatrix.getColumnDimension());
        assertEquals(3, ptMatrix.getRowDimension());
        assertEquals(3, ptMatrix.getColumnDimension());
        assertEquals(3, tMatrix.getRowDimension());
        assertEquals(3, tMatrix.getColumnDimension());

        // Test the values of output matrices
        double[][] expectedPArray = {{0.6947462, -0.6324555, -0.3333333}, {0.2782948, 0.7453559, -0.6666667},
                {0.6947462, -0.1961161, 0.6666667}};
        double[][] expectedPtArray = {{0.6947462, 0.2782948, 0.6947462}, {-0.6324555, 0.7453559, -0.1961161},
                {-0.3333333, -0.6666667, 0.6666667}};
        double[][] expectedTArray = {{11.3448143, -1.1206997, -0.7427814}, {0.0210557, -0.5157294, -0.7721756},
                {-0.0125134, 0.0651522, -0.2063249}};
        RealMatrix expectedPMatrix = MatrixUtils.createRealMatrix(expectedPArray);
        RealMatrix expectedPtMatrix = MatrixUtils.createRealMatrix(expectedPtArray);
        RealMatrix expectedTMatrix = MatrixUtils.createRealMatrix(expectedTArray);

        // Test the values of the output matrices with a tolerance of 1e-6
        assertArrayEquals(expectedPMatrix.getData(), pMatrix.getData(), 1e-6);
        assertArrayEquals(expectedPtMatrix.getData(), ptMatrix.getData(), 1e-6);
        assertArrayEquals(expectedTMatrix.getData(), tMatrix.getData(), 1e-6);
    }

    @Test
    public void testSchurTransformation_edgeCase() {
        // Edge case: a 1x1 matrix is used for Schur transformation
        double[][] inputArray = {{5}};
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(inputArray);

        SchurTransformer transformer = new SchurTransformer(inputMatrix);
        RealMatrix pMatrix = transformer.getP();
        RealMatrix ptMatrix = transformer.getPT();
        RealMatrix tMatrix = transformer.getT();

        // Assertion statements
        // Test the dimensions of output matrices
        assertEquals(1, pMatrix.getRowDimension());
        assertEquals(1, pMatrix.getColumnDimension());
        assertEquals(1, ptMatrix.getRowDimension());
        assertEquals(1, ptMatrix.getColumnDimension());
        assertEquals(1, tMatrix.getRowDimension());
        assertEquals(1, tMatrix.getColumnDimension());

        // Test the values of output matrices
        double[][] expectedPArray = {{1.0}};
        double[][] expectedPtArray = {{1.0}};
        double[][] expectedTArray = {{5.0}};
        RealMatrix expectedPMatrix = MatrixUtils.createRealMatrix(expectedPArray);
        RealMatrix expectedPtMatrix = MatrixUtils.createRealMatrix(expectedPtArray);
        RealMatrix expectedTMatrix = MatrixUtils.createRealMatrix(expectedTArray);

        // Test the values of the output matrices with a tolerance of 1e-6
        assertArrayEquals(expectedPMatrix.getData(), pMatrix.getData(), 1e-6);
        assertArrayEquals(expectedPtMatrix.getData(), ptMatrix.getData(), 1e-6);
        assertArrayEquals(expectedTMatrix.getData(), tMatrix.getData(), 1e-6);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testSchurTransformation_errorCase() {
        // Error case: a non-square matrix is used for Schur transformation
        double[][] inputArray = {{1, 2, 3}, {4, 5, 6}};
        RealMatrix inputMatrix = MatrixUtils.createRealMatrix(inputArray);

        SchurTransformer transformer = new SchurTransformer(inputMatrix);
    }
}