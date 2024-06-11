package org.apache.commons.math3.linear;
import static org.junit.Assert.*;
import org.junit.Test;

public class SchurTransformerTest {
    
    @Test
    public void testSchurTransformerConstructor() {
        // Test constructor with valid square matrix
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        assertNotNull(transformer);
        
        // Test constructor with invalid non-square matrix
        try {
            RealMatrix nonSquareMatrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}});
            SchurTransformer invalidTransformer = new SchurTransformer(nonSquareMatrix);
            fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            // expected
        }
    }
    
    @Test
    public void testGetP() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix P = transformer.getP();
        assertEquals(1.0, P.getEntry(0, 0), 0.0);
        assertEquals(2.0, P.getEntry(0, 1), 0.0);
        assertEquals(3.0, P.getEntry(1, 0), 0.0);
        assertEquals(4.0, P.getEntry(1, 1), 0.0);
    }
    
    @Test
    public void testGetPT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix PT = transformer.getPT();
        assertEquals(1.0, PT.getEntry(0, 0), 0.0);
        assertEquals(3.0, PT.getEntry(0, 1), 0.0);
        assertEquals(2.0, PT.getEntry(1, 0), 0.0);
        assertEquals(4.0, PT.getEntry(1, 1), 0.0);
    }
    
    @Test
    public void testGetT() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1, 2}, {3, 4}});
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix T = transformer.getT();
        assertEquals(1.0, T.getEntry(0, 0), 0.0);
        assertEquals(2.0, T.getEntry(0, 1), 0.0);
        assertEquals(3.0, T.getEntry(1, 0), 0.0);
        assertEquals(4.0, T.getEntry(1, 1), 0.0);
    }
    
    // Add more test cases for other methods
    
}