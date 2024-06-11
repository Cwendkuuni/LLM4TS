import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    // Test input matrix with no negative diagonal elements
    @Test
    public void testNoNegativeDiagonalElements() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 2, 1}, {2, 5, 3}, {1, 3, 6}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.001);
        
        RealMatrix root = decomposition.getRootMatrix();
        Assert.assertEquals(3, decomposition.getRank());
        Assert.assertEquals(matrix, root.multiply(root.transpose()));
    }
    
    // Test input matrix with negative diagonal elements
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNegativeDiagonalElements() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 2, 1}, {2, 5, 3}, {1, 3, -2}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.001);
    }
    
    // Test input matrix with diagonal elements below the given threshold
    @Test
    public void testDiagonalElementsBelowThreshold() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 0, 0}, {0, 5, 0}, {0, 0, 6}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 1);
        
        RealMatrix root = decomposition.getRootMatrix();
        Assert.assertEquals(3, decomposition.getRank());
        Assert.assertEquals(matrix, root.multiply(root.transpose()));
    }
    
    // Test input matrix with all zero diagonal elements
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testAllZeroDiagonalElements() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 2, 1}, {2, 0, 3}, {1, 3, 0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.001);
    }
    
    // Test input matrix with all zero elements
    @Test
    public void testAllZeroElements() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.001);
        
        RealMatrix root = decomposition.getRootMatrix();
        Assert.assertEquals(0, decomposition.getRank());
        Assert.assertEquals(matrix, root.multiply(root.transpose()));
    }
    
    // Test input matrix with large values
    @Test
    public void testLargeValues() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1e10, 1e5, 1e2}, {1e5, 1e10, 1e5}, {1e2, 1e5, 1e10}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.001);
        
        RealMatrix root = decomposition.getRootMatrix();
        Assert.assertEquals(3, decomposition.getRank());
        Assert.assertEquals(matrix, root.multiply(root.transpose()));
    }
    
    // Test input matrix with small values
    @Test
    public void testSmallValues() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{1e-6, 2e-6, 1e-5}, {2e-6, 1e-6, 3e-6}, {1e-5, 3e-6, 1e-6}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.001);
        
        RealMatrix root = decomposition.getRootMatrix();
        Assert.assertEquals(3, decomposition.getRank());
        Assert.assertEquals(matrix, root.multiply(root.transpose()));
    }

    // Test input matrix with non-square dimensions
    @Test
    public void testNonSquareMatrix() {
        RealMatrix matrix = MatrixUtils.createRealMatrix(new double[][]{{4, 2, 1}, {2, 5, 3}});
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.001);
        
        RealMatrix root = decomposition.getRootMatrix();
        Assert.assertEquals(2, decomposition.getRank());
        Assert.assertEquals(matrix, root.multiply(root.transpose()));
    }

}

