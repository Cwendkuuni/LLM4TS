import org.junit.Test;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.util.FastMath;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testGetRootMatrix() {
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        
        double small = FastMath.ulp(1.0d);
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        
        RealMatrix expectedRootMatrix = MatrixUtils.createRealMatrix(new double[][]{{2, 0, 0}, {6, 1, 0}, {-8, 5, 3}});
        
        Assert.assertEquals(expectedRootMatrix, decomposition.getRootMatrix());
    }
    
    @Test
    public void testGetRank() {
        double[][] matrixData = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        
        double small = FastMath.ulp(1.0d);
        
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        
        int expectedRank = 3;
        
        Assert.assertEquals(expectedRank, decomposition.getRank());
    }
}