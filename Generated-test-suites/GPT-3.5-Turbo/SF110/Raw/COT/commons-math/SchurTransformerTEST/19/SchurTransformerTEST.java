import static org.junit.Assert.*;
import org.junit.Test;

public class SchurTransformerTest {
    
    @Test
    public void getP_test() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{{1, 0}, {0, 1}});
        RealMatrix actualP = transformer.getP();
        assertEquals(expectedP, actualP);
    }
    
    @Test
    public void getPT_test() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedPT = MatrixUtils.createRealMatrix(new double[][]{{1, 3}, {2, 4}});
        RealMatrix actualPT = transformer.getPT();
        assertEquals(expectedPT, actualPT);
    }
    
    @Test
    public void getT_test() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {0, -2}});
        RealMatrix actualT = transformer.getT();
        assertEquals(expectedT, actualT);
    }
    
    @Test
    public void constructor_invalidMatrix_test() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}});
        try {
            SchurTransformer transformer = new SchurTransformer(matrix);
            fail("Expected NonSquareMatrixException but got no exception");
        } catch (NonSquareMatrixException e) {
            // exception successfully caught
        }
    }
    
    // Add more test cases for other methods
    
}