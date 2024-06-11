import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.RectangularCholeskyDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.FastMath;

import org.junit.Assert;
import org.junit.Test;

public class RectangularCholeskyDecompositionTest {
    
    @Test
    public void testGetRootMatrix() {
        // Arrange
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        
        // Act
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        
        // Assert
        Assert.assertEquals(1.0, rootMatrix.getEntry(0, 0), 0.001);
        Assert.assertEquals(0.0, rootMatrix.getEntry(0, 1), 0.001);
        Assert.assertEquals(0.0, rootMatrix.getEntry(0, 2), 0.001);
        Assert.assertEquals(0.0, rootMatrix.getEntry(1, 0), 0.001);
        Assert.assertEquals(2.236, rootMatrix.getEntry(1, 1), 0.001);
        Assert.assertEquals(0.0, rootMatrix.getEntry(1, 2), 0.001);
        Assert.assertEquals(0.0, rootMatrix.getEntry(2, 0), 0.001);
        Assert.assertEquals(0.0, rootMatrix.getEntry(2, 1), 0.001);
        Assert.assertEquals(3.0, rootMatrix.getEntry(2, 2), 0.001);
    }
    
    @Test
    public void testGetRank() {
        // Arrange
        double[][] data = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(data);
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(matrix, 0.0);
        
        // Act
        int rank = decomposition.getRank();
        
        // Assert
        Assert.assertEquals(3, rank);
    }
}