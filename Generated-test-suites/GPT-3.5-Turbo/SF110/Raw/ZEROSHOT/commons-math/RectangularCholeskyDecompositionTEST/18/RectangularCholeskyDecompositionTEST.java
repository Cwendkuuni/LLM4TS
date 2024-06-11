import org.apache.commons.math3.linear.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testConstructor() {
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        
        assertEquals(matrix, decomposition.getRootMatrix());
        assertEquals(3, decomposition.getRank());
    }
    
    @Test
    public void testGetRootMatrix() {
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        
        assertEquals(4, rootMatrix.getEntry(0, 0), 0.001);
        assertEquals(12, rootMatrix.getEntry(1, 0), 0.001);
        assertEquals(-16, rootMatrix.getEntry(2, 0), 0.001);
    }
    
    @Test
    public void testGetRank() {
        double[][] data = {{4, 12, -16}, {12, 37, -43}, {-16, -43, 98}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix);
        
        assertEquals(3, decomposition.getRank());
    }
}