import org.junit.Test;
import static org.junit.Assert.*;

public class SchurTransformerTest {

    @Test
    public void testDoubleQRStep() {
        // Initialize test matrix
        double[][] matrixT = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        
        // Create SchurTransformer object
        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(matrixT));
        
        // Perform double QR step
        transformer.performDoubleQRStep(0, 0, 2, new SchurTransformer.ShiftInfo(), new double[3]);
        
        // Check if double QR step was performed correctly
        double[][] expectedMatrixT = {{-11.548, -18.392, 3.0}, {-28.542, -4.827, 6.0}, {-45.536, 8.739, 9.0}};
        RealMatrix expectedT = MatrixUtils.createRealMatrix(expectedMatrixT);
        assertTrue(transformer.getT().subtract(expectedT).getNorm() < 1e-6);
    }

    @Test(expected = MaxCountExceededException.class)
    public void testTransformWithMaxIterationsExceeded() {
        // Initialize test matrix
        double[][] matrixT = {{1.0, 2.0}, {3.0, 4.0}};
        
        // Create SchurTransformer object
        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(matrixT));
        
        // Call transform method
        transformer.transform();
    }

    @Test
    public void testGetP() {
        // Initialize test matrix
        double[][] matrixT = {{1.0, 2.0}, {3.0, 4.0}};
        
        // Create SchurTransformer object
        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(matrixT));
        
        // Get P matrix
        RealMatrix P = transformer.getP();
        
        // Check if P matrix is correct
        double[][] expectedMatrixP = {{1.0, -2.0}, {3.0, -7.0}};
        RealMatrix expectedP = MatrixUtils.createRealMatrix(expectedMatrixP);
        assertTrue(P.subtract(expectedP).getNorm() < 1e-6);
    }

    @Test
    public void testGetPT() {
        // Initialize test matrix
        double[][] matrixT = {{1.0, 2.0}, {3.0, 4.0}};
        
        // Create SchurTransformer object
        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(matrixT));
        
        // Get PT matrix
        RealMatrix PT = transformer.getPT();
        
        // Check if PT matrix is correct
        double[][] expectedMatrixPT = {{1.0, 3.0}, {-2.0, -7.0}};
        RealMatrix expectedPT = MatrixUtils.createRealMatrix(expectedMatrixPT);
        assertTrue(PT.subtract(expectedPT).getNorm() < 1e-6);
    }

    @Test
    public void testGetT() {
        // Initialize test matrix
        double[][] matrixT = {{1.0, 2.0}, {3.0, 4.0}};
        
        // Create SchurTransformer object
        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(matrixT));
        
        // Get T matrix
        RealMatrix T = transformer.getT();
        
        // Check if T matrix is correct
        double[][] expectedMatrixT = {{1.0, 2.0}, {3.0, 4.0}};
        RealMatrix expectedT = MatrixUtils.createRealMatrix(expectedMatrixT);
        assertTrue(T.subtract(expectedT).getNorm() < 1e-6);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        // Initialize non-square test matrix
        double[][] matrixT = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        
        // Create SchurTransformer object with non-square matrix
        SchurTransformer transformer = new SchurTransformer(MatrixUtils.createRealMatrix(matrixT));
    }

}