package org.apache.commons.math3.linear;

import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {
    
    @Test
    public void testConstructor() {
        // Test case for default constructor
        RealMatrix matrix1 = MatrixUtils.createRealMatrix(new double[][] { 
            { 4, 2, 1 },
            { 2, 9, 3 },
            { 1, 3, 6 }
        });
        RectangularCholeskyDecomposition decomp1 = new RectangularCholeskyDecomposition(matrix1);
        assertNotNull(decomp1);
        assertEquals(matrix1, decomp1.getRootMatrix());
        assertEquals(3, decomp1.getRank());
        
        // Test case for constructor with small parameter
        RealMatrix matrix2 = MatrixUtils.createRealMatrix(new double[][] { 
            { 4, 2, 1 },
            { 2, 9, 3 },
            { 1, 3, 6 }
        });
        double small = 0.1;
        RectangularCholeskyDecomposition decomp2 = new RectangularCholeskyDecomposition(matrix2, small);
        assertNotNull(decomp2);
        assertEquals(matrix2, decomp2.getRootMatrix());
        assertEquals(3, decomp2.getRank());
    }
}