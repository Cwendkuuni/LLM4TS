package org.apache.commons.math3.linear;

import org.apache.commons.math3.util.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.exception.util.*;

import static org.junit.Assert.*;
import org.junit.Test;

public class SchurTransformerTest {

    @Test
    public void testGetP() {
        RealMatrix expectedP = MatrixUtils.createRealMatrix(new double[][]{
            {0.0, 1.0},
            {1.0, 0.0}
        });
        
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
            {0.0, 1.0},
            {1.0, 0.0}
        });
        
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix actualP = transformer.getP();
        
        assertArrayEquals(expectedP.getData(), actualP.getData());
    }
    
    @Test
    public void testGetPT() {
        RealMatrix expectedPT = MatrixUtils.createRealMatrix(new double[][]{
            {0.0, 1.0},
            {1.0, 0.0}
        });
        
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
            {0.0, 1.0},
            {1.0, 0.0}
        });
        
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix actualPT = transformer.getPT();
        
        assertArrayEquals(expectedPT.getData(), actualPT.getData());
    }
    
    @Test
    public void testGetT() {
        RealMatrix expectedT = MatrixUtils.createRealMatrix(new double[][]{
            {0.0, 1.0},
            {1.0, 0.0}
        });
        
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
            {0.0, 1.0},
            {1.0, 0.0}
        });
        
        SchurTransformer transformer = new SchurTransformer(matrix);
        RealMatrix actualT = transformer.getT();
        
        assertArrayEquals(expectedT.getData(), actualT.getData());
    }
    
    @Test
    public void testConstructorThrowsExceptionForNonSquareMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{
            {0.0, 1.0, 2.0},
            {3.0, 4.0, 5.0},
            {6.0, 7.0, 8.0}
        });
        
        try {
            new SchurTransformer(matrix);
            fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            // expected
        }
    }
    
    // ... more test methods for other methods ...
    
}