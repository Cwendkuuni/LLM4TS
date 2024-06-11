import org.apache.commons.math3.linear.*;
import org.junit.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testRectangularCholeskyDecomposition() {
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
            
            Assert.assertEquals(decomposition.getRank(), 3);
            Assert.assertArrayEquals(decomposition.getRootMatrix().getData(), 
                                     new double[][] {{2, 6, -8}, {0, 1, -5}, {0, 0, 3}}, 0.0001);
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Exception was thrown");
        }
    }
    
    @Test
    public void testRectangularCholeskyDecompositionWithSmall() {
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        
        try {
            RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.1);
            
            Assert.assertEquals(decomposition.getRank(), 3);
            Assert.assertArrayEquals(decomposition.getRootMatrix().getData(), 
                                     new double[][] {{2, 6, -8}, {0, 1, -5}, {0, 0, 3}}, 0.0001);
        } catch (NonPositiveDefiniteMatrixException e) {
            Assert.fail("Exception was thrown");
        }
    }
    
    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testRectangularCholeskyDecompositionWithSmallException() throws NonPositiveDefiniteMatrixException {
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 10);
    }

}