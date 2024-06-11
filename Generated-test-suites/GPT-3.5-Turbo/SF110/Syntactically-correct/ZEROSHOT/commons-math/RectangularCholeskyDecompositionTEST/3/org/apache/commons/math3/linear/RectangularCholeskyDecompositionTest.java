import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructorWithPositiveDefiniteMatrix() {
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        RectangularCholeskyDecomposition cholesky = new RectangularCholeskyDecomposition(matrix);
        
        Assert.assertEquals(0.0, cholesky.getRootMatrix().getEntry(0, 0), 0.0001);
        Assert.assertEquals(0.0, cholesky.getRootMatrix().getEntry(0, 1), 0.0001);
        Assert.assertEquals(0.0, cholesky.getRootMatrix().getEntry(0, 2), 0.0001);
        Assert.assertEquals(0.0, cholesky.getRootMatrix().getEntry(1, 0), 0.0001);
        Assert.assertEquals(0.0, cholesky.getRootMatrix().getEntry(1, 1), 0.0001);
        Assert.assertEquals(0.0, cholesky.getRootMatrix().getEntry(1, 2), 0.0001);
        Assert.assertEquals(0.0, cholesky.getRootMatrix().getEntry(2, 0), 0.0001);
        Assert.assertEquals(0.0, cholesky.getRootMatrix().getEntry(2, 1), 0.0001);
        Assert.assertEquals(0.0, cholesky.getRootMatrix().getEntry(2, 2), 0.0001);
        
        Assert.assertEquals(3, cholesky.getRank());
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrix() {
        double[][] data = {{4, -12, -16}, {-12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        new RectangularCholeskyDecomposition(matrix);
    }
    
    @Test
    public void testConstructorWithSmallThreshold() {
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        RectangularCholeskyDecomposition cholesky = new RectangularCholeskyDecomposition(matrix, 1e-10);
        
        Assert.assertEquals(0.0, cholesky.getRootMatrix().getEntry(0, 0), 0.0001);
        Assert.assertEquals(0.0, cholesky.getRootMatrix().getEntry(0, 1), 0.0001);
        Assert.assertEquals(0.0, cholesky.getRootMatrix().getEntry(0, 2), 0.0001);
        Assert.assertEquals(0.0, cholesky.getRootMatrix().getEntry(1, 0), 0.0001);
        Assert.assertEquals(0.0, cholesky.getRootMatrix().getEntry(1, 1), 0.0001);
        Assert.assertEquals(0.0, cholesky.getRootMatrix().getEntry(1, 2), 0.0001);
        Assert.assertEquals(0.0, cholesky.getRootMatrix().getEntry(2, 0), 0.0001);
        Assert.assertEquals(0.0, cholesky.getRootMatrix().getEntry(2, 1), 0.0001);
        Assert.assertEquals(0.0, cholesky.getRootMatrix().getEntry(2, 2), 0.0001);
        
        Assert.assertEquals(3, cholesky.getRank());
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithNonPositiveDefiniteMatrixAndSmallThreshold() {
        double[][] data = {{4, -12, -16}, {-12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        new RectangularCholeskyDecomposition(matrix, 1e-10);
    }
    
    @Test
    public void testGetRootMatrix() {
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        RectangularCholeskyDecomposition cholesky = new RectangularCholeskyDecomposition(matrix);
        
        RealMatrix root = cholesky.getRootMatrix();
        
        Assert.assertNotNull(root);
    }
    
    @Test
    public void testGetRank() {
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        RectangularCholeskyDecomposition cholesky = new RectangularCholeskyDecomposition(matrix);
        
        int rank = cholesky.getRank();
        
        Assert.assertEquals(3, rank);
    }
}