import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition_withValidMatrix() {
        double[][] matrixData = {{4, 12}, {12, 37}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        
        RectangularCholeskyDecomposition choleskyDecomposition = new RectangularCholeskyDecomposition(matrix);
        
        Assert.assertEquals(matrix, choleskyDecomposition.getRootMatrix());
        Assert.assertEquals(2, choleskyDecomposition.getRank());
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testRectangularCholeskyDecomposition_withNonPositiveDefiniteMatrix() {
        double[][] matrixData = {{-1, 2}, {2, 5}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        
        RectangularCholeskyDecomposition choleskyDecomposition = new RectangularCholeskyDecomposition(matrix);
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testRectangularCholeskyDecomposition_withZeroDiagonalElementMatrix() {
        double[][] matrixData = {{1, 2}, {2, 0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        
        RectangularCholeskyDecomposition choleskyDecomposition = new RectangularCholeskyDecomposition(matrix);
    }
    
    @Test
    public void testRectangularCholeskyDecomposition_withSmallThreshold() {
        double[][] matrixData = {{1, 2}, {2, 5}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        
        RectangularCholeskyDecomposition choleskyDecomposition = new RectangularCholeskyDecomposition(matrix, 0.01);
        
        Assert.assertEquals(matrix, choleskyDecomposition.getRootMatrix());
        Assert.assertEquals(2, choleskyDecomposition.getRank());
    }
    
    @Test
    public void testRectangularCholeskyDecomposition_withLargeThreshold() {
        double[][] matrixData = {{1, 2}, {2, 5}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        
        RectangularCholeskyDecomposition choleskyDecomposition = new RectangularCholeskyDecomposition(matrix, 100);
        
        Assert.assertEquals(matrix, choleskyDecomposition.getRootMatrix());
        Assert.assertEquals(2, choleskyDecomposition.getRank());
    }
}