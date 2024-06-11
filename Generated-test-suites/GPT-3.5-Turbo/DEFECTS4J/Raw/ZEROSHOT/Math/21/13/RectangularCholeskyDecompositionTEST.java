import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {

    private final double[][] matrixData = {
        {10, 3, 4},
        {3, 10, 5},
        {4, 5, 10}
    };

    private final RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
    private final double small = 1e-10;

    @Test
    public void testGetRootMatrix() {
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        
        double[][] expectedData = {
            {3.1622776601683795, 0.9486832980505138},
            {0.0, 2.886751345948129},
            {1.2649110640673518, 0.632455532033676}
        };

        RealMatrix expectedRootMatrix = MatrixUtils.createRealMatrix(expectedData);
        
        Assert.assertEquals(expectedRootMatrix, rootMatrix);
    }
    
    @Test
    public void testGetRank() {
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, small);
        int rank = decomposition.getRank();
        
        int expectedRank = 2;
        
        Assert.assertEquals(expectedRank, rank);
    }
}