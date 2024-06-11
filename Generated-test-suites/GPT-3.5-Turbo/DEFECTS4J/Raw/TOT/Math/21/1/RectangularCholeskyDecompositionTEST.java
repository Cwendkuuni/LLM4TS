import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testGetRootMatrix() {
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.001);
        RealMatrix result = decomposition.getRootMatrix();
        
        Assert.assertTrue(result.getRowDimension() == 3);
        Assert.assertTrue(result.getColumnDimension() == 2);
        Assert.assertEquals(2.0, result.getEntry(0, 0), 0.001);
        Assert.assertEquals(6.0, result.getEntry(0, 1), 0.001);
        Assert.assertEquals(1.0, result.getEntry(1, 0), 0.001);
        Assert.assertEquals(5.0, result.getEntry(1, 1), 0.001);
        Assert.assertEquals(-2.0, result.getEntry(2, 0), 0.001);
        Assert.assertEquals(-7.0, result.getEntry(2, 1), 0.001);
    }
    
    @Test
    public void testGetRank() {
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.001);
        int rank = decomposition.getRank();
        
        Assert.assertEquals(2, rank);
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNegativeDiagonalElement() {
        double[][] data = {{4, 12, -16}, {12, -37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.001);
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testSmallDiagonalElement() {
        double[][] data = {{4, 12, -16}, {12, 0.0001, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.001);
    }

}