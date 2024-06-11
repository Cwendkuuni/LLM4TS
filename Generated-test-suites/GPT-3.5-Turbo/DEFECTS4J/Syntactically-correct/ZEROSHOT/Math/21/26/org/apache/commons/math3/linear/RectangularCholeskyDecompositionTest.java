import org.junit.Test;
import org.apache.commons.math3.linear.RealMatrix;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testGetRootMatrix() {
        // Test case 1
        double[][] data1 = {{1.0, 0.0}, {0.0, 1.0}};
        RealMatrix matrix1 = MatrixUtils.createRealMatrix(data1);
        RectangularCholeskyDecomposition decomp1 = new RectangularCholeskyDecomposition(matrix1, 0.0);
        RealMatrix root1 = decomp1.getRootMatrix();
        Assert.assertEquals(matrix1, root1.multiply(root1.transpose()));
        
        // Test case 2
        double[][] data2 = {{2.0, 1.0}, {1.0, 2.0}};
        RealMatrix matrix2 = MatrixUtils.createRealMatrix(data2);
        RectangularCholeskyDecomposition decomp2 = new RectangularCholeskyDecomposition(matrix2, 0.0);
        RealMatrix root2 = decomp2.getRootMatrix();
        Assert.assertEquals(matrix2, root2.multiply(root2.transpose()));
    }

    @Test
    public void testGetRank() {
        // Test case 1
        double[][] data1 = {{1.0, 0.0}, {0.0, 1.0}};
        RealMatrix matrix1 = MatrixUtils.createRealMatrix(data1);
        RectangularCholeskyDecomposition decomp1 = new RectangularCholeskyDecomposition(matrix1, 0.0);
        int rank1 = decomp1.getRank();
        Assert.assertEquals(2, rank1);
        
        // Test case 2
        double[][] data2 = {{2.0, 1.0}, {1.0, 2.0}};
        RealMatrix matrix2 = MatrixUtils.createRealMatrix(data2);
        RectangularCholeskyDecomposition decomp2 = new RectangularCholeskyDecomposition(matrix2, 0.0);
        int rank2 = decomp2.getRank();
        Assert.assertEquals(2, rank2);
    }

}