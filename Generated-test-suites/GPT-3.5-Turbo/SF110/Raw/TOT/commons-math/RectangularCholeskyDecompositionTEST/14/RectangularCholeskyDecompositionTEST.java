import org.junit.Test;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.*;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor1() {
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
            
            RealMatrix expectedRoot = matrix;
            RealMatrix actualRoot = decomp.getRootMatrix();
            assertEquals(expectedRoot, actualRoot);
            
            int expectedRank = 3;
            int actualRank = decomp.getRank();
            assertEquals(expectedRank, actualRank);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testConstructor2() {
        double[][] data = {{2, 1}, {1, 4}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
            
            RealMatrix expectedRoot = matrix;
            RealMatrix actualRoot = decomp.getRootMatrix();
            assertEquals(expectedRoot, actualRoot);
            
            int expectedRank = 2;
            int actualRank = decomp.getRank();
            assertEquals(expectedRank, actualRank);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testConstructor3() {
        double[][] data = {{-1, -2, -3}, {-2, -5, -3}, {-3, -3, -9}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
            
            RealMatrix expectedRoot = matrix;
            RealMatrix actualRoot = decomp.getRootMatrix();
            assertEquals(expectedRoot, actualRoot);
            
            int expectedRank = 3;
            int actualRank = decomp.getRank();
            assertEquals(expectedRank, actualRank);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testConstructor4() {
        double[][] data = {{-0.5, 0.5}, {0.5, -0.5}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        try {
            RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
            
            RealMatrix expectedRoot = matrix;
            RealMatrix actualRoot = decomp.getRootMatrix();
            assertEquals(expectedRoot, actualRoot);
            
            int expectedRank = 2;
            int actualRank = decomp.getRank();
            assertEquals(expectedRank, actualRank);
        } catch (NonPositiveDefiniteMatrixException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor5() throws NonPositiveDefiniteMatrixException {
        double[][] data = {{-2, 1}, {1, -3}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructor6() throws NonPositiveDefiniteMatrixException {
        double[][] data = {{1, -2, 3}, {-2, 5, -3}, {3, -3, 9}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        RectangularCholeskyDecomposition decomp = new RectangularCholeskyDecomposition(matrix);
    }
    
    // Other test methods for the remaining public methods
    
}