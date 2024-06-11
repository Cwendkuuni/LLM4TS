import org.junit.Test;
import static org.junit.Assert.*;

public class SchurTransformerTest {
    
    @Test
    public void testConstructor() {
        // Typical use case
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        assertNotNull(transformer);
        
        // Edge case: Non square matrix
        RealMatrix nonSquareMatrix = MatrixUtils.createRealMatrix(new double[][] {{1, 2, 3}, {4, 5, 6}});
        try {
            SchurTransformer nonSquareTransformer = new SchurTransformer(nonSquareMatrix);
            fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testGetP() {
        // Typical use case
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix P = transformer.getP();
        assertNotNull(P);
        
        // Edge case: Cached P is null
        transformer.cachedP = null;
        RealMatrix cachedP = transformer.getP();
        assertNotNull(cachedP);
    }
    
    @Test
    public void testGetPT() {
        // Typical use case
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix PT = transformer.getPT();
        assertNotNull(PT);
        
        // Edge case: Cached PT is null
        transformer.cachedPt = null;
        RealMatrix cachedPT = transformer.getPT();
        assertNotNull(cachedPT);
    }
    
    @Test
    public void testGetT() {
        // Typical use case
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][] {{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix T = transformer.getT();
        assertNotNull(T);
        
        // Edge case: Cached T is null
        transformer.cachedT = null;
        RealMatrix cachedT = transformer.getT();
        assertNotNull(cachedT);
    }
    
    // Additional tests for other methods may be added
    
}