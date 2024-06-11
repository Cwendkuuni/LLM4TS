import org.junit.Test;
import org.apache.commons.math3.linear.*;
import static org.junit.Assert.*;

public class SchurTransformerTest {

    @Test
    public void testConstructorWithSquareMatrix() {
        // Test case for constructor - creating SchurTransformer with a square RealMatrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        assertNotNull(transformer);
    }

    @Test
    public void testGetP() {
        // Test case for getP() - retrieving the P matrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix P = transformer.getP();
        assertNotNull(P);
    }

    @Test
    public void testGetPT() {
        // Test case for getPT() - retrieving the transpose of P matrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix PT = transformer.getPT();
        assertNotNull(PT);
    }

    @Test
    public void testGetT() {
        // Test case for getT() - retrieving the T matrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix T = transformer.getT();
        assertNotNull(T);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        // Test case for constructor with a non-square RealMatrix - expect NonSquareMatrixException
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}});
        SchurTransformer transformer = new SchurTransformer(matrix);
    }

    // Add more test cases for edge cases and exception handling scenarios

}