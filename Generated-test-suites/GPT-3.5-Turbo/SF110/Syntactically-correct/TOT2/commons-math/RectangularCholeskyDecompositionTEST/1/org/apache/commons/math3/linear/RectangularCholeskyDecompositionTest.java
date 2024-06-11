package org.apache.commons.math3.linear;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecompositionConstructor() {
        double[][] testData = {{4, 1}, {1, 3}};
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(testData);
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(testMatrix);
        assertNotNull(decomposition);
    }
    
    @Test
    public void testGetRootMatrix() {
        double[][] testData = {{4, 1}, {1, 3}};
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(testData);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(testMatrix);
        
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        assertNotNull(rootMatrix);
    }
    
    @Test
    public void testGetRank() {
        double[][] testData = {{4, 1}, {1, 3}};
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(testData);
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(testMatrix);
        assertEquals(2, decomposition.getRank());
    }
    
    @Test
    public void testNonPositiveDefiniteMatrixException() {
        double[][] testData = {{-1, 1}, {1, -3}};
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(testData);
        
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(testMatrix);
            fail("Expected NonPositiveDefiniteMatrixException");
        } catch (NonPositiveDefiniteMatrixException e) {
            // Exception caught, test passed
        }
    }
  
}