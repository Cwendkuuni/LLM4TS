import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SchurTransformer;
import org.apache.commons.math3.exception.NonSquareMatrixException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SchurTransformerTest {
    private double[][] invalidMatrix;
    private double[][] squareMatrix;
    
    @Before
    public void setUp() {
        invalidMatrix = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        squareMatrix = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
    }
    
    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithInvalidMatrix() {
        RealMatrix invalidRealMatrix = new Array2DRowRealMatrix(invalidMatrix);
        new SchurTransformer(invalidRealMatrix);
    }
    
    @Test
    public void testGetP() {
        RealMatrix squareRealMatrix = new Array2DRowRealMatrix(squareMatrix);
        SchurTransformer transformer = new SchurTransformer(squareRealMatrix);
        
        RealMatrix pMatrix = transformer.getP();
        
        assertEquals(1.0, pMatrix.getEntry(0, 0), 0.00001);
        assertEquals(2.0, pMatrix.getEntry(0, 1), 0.00001);
        assertEquals(3.0, pMatrix.getEntry(0, 2), 0.00001);
        assertEquals(4.0, pMatrix.getEntry(1, 0), 0.00001);
        assertEquals(5.0, pMatrix.getEntry(1, 1), 0.00001);
        assertEquals(6.0, pMatrix.getEntry(1, 2), 0.00001);
        assertEquals(7.0, pMatrix.getEntry(2, 0), 0.00001);
        assertEquals(8.0, pMatrix.getEntry(2, 1), 0.00001);
        assertEquals(9.0, pMatrix.getEntry(2, 2), 0.00001);
    }
    
    @Test
    public void testGetPT() {
        RealMatrix squareRealMatrix = new Array2DRowRealMatrix(squareMatrix);
        SchurTransformer transformer = new SchurTransformer(squareRealMatrix);
        
        RealMatrix ptMatrix = transformer.getPT();
        
        assertEquals(1.0, ptMatrix.getEntry(0, 0), 0.00001);
        assertEquals(4.0, ptMatrix.getEntry(0, 1), 0.00001);
        assertEquals(7.0, ptMatrix.getEntry(0, 2), 0.00001);
        assertEquals(2.0, ptMatrix.getEntry(1, 0), 0.00001);
        assertEquals(5.0, ptMatrix.getEntry(1, 1), 0.00001);
        assertEquals(8.0, ptMatrix.getEntry(1, 2), 0.00001);
        assertEquals(3.0, ptMatrix.getEntry(2, 0), 0.00001);
        assertEquals(6.0, ptMatrix.getEntry(2, 1), 0.00001);
        assertEquals(9.0, ptMatrix.getEntry(2, 2), 0.00001);
    }
    
    @Test
    public void testGetT() {
        RealMatrix squareRealMatrix = new Array2DRowRealMatrix(squareMatrix);
        SchurTransformer transformer = new SchurTransformer(squareRealMatrix);
        
        RealMatrix tMatrix = transformer.getT();
        
        assertEquals(25.0, tMatrix.getEntry(0, 0), 0.00001);
        assertEquals(-1.0, tMatrix.getEntry(0, 1), 0.00001);
        assertEquals(0.0, tMatrix.getEntry(0, 2), 0.00001);
        assertEquals(1.0, tMatrix.getEntry(1, 0), 0.00001);
        assertEquals(0.0, tMatrix.getEntry(1, 1), 0.00001);
        assertEquals(-0.0, tMatrix.getEntry(1, 2), 0.00001);
        assertEquals(0.0, tMatrix.getEntry(2, 0), 0.00001);
        assertEquals(0.0, tMatrix.getEntry(2, 1), 0.00001);
        assertEquals(0.0, tMatrix.getEntry(2, 2), 0.00001);
    }
    
    // More test cases for other methods...
    
}