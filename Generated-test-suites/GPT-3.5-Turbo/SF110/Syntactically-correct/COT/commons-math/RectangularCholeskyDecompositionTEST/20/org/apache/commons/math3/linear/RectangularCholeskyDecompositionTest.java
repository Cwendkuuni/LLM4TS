
import org.junit.Test;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.MathUtils;

public class RectangularCholeskyDecompositionTest {
    
    @Test
    public void testConstructorNoSmallValue() {
        double[][] data = {{4, 12, -16},
                           {12, 37, -43},
                           {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            
            RealMatrix rootMatrix = decomposition.getRootMatrix();
            int rank = decomposition.getRank();
            
            Assert.assertEquals(3, rootMatrix.getRowDimension());
            Assert.assertEquals(1, rootMatrix.getColumnDimension());
            Assert.assertEquals(3, rank);
        } catch (NonPositiveDefiniteMatrixException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testConstructorWithSmallValue() {
        double[][] data = {{4, 12, -16},
                           {12, 36.9, -43},
                           {-16, -43, 97.9}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.1);
            
            RealMatrix rootMatrix = decomposition.getRootMatrix();
            int rank = decomposition.getRank();
            
            Assert.assertEquals(3, rootMatrix.getRowDimension());
            Assert.assertEquals(1, rootMatrix.getColumnDimension());
            Assert.assertEquals(3, rank);
        } catch (NonPositiveDefiniteMatrixException e) {
            e.printStackTrace();
        }
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorNoSmallValueNonPositiveDefiniteMatrixException() throws NonPositiveDefiniteMatrixException {
        double[][] data = {{4, 12, -16},
                           {12, 37, -43},
                           {-16, -43, -98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorWithSmallValueNonPositiveDefiniteMatrixException() throws NonPositiveDefiniteMatrixException {
        double[][] data = {{4, 12, -16},
                           {12, 37, -43},
                           {-16, -43, -97.9}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.1);
    }
}
