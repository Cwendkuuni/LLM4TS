import org.junit.Test;
import org.junit.Assert;

public class RectangularCholeskyDecompositionTest {

    @Test
    public void testGetRootMatrix() {
        // Create a test matrix
        double[][] testData = {{1, 2}, {2, 5}};
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(testData);

        // Create the decomposition
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(testMatrix, 1e-10);

        // Test the getRootMatrix() method
        RealMatrix rootMatrix = decomposition.getRootMatrix();
        Assert.assertEquals(1.0, rootMatrix.getEntry(0, 0), 1e-10);
        Assert.assertEquals(0.6324555320336759, rootMatrix.getEntry(0, 1), 1e-10);
        Assert.assertEquals(0.0, rootMatrix.getEntry(1, 0), 1e-10);
        Assert.assertEquals(0.31622776601683794, rootMatrix.getEntry(1, 1), 1e-10);
    }

    @Test
    public void testGetRank() {
        // Create a test matrix
        double[][] testData = {{1, 2}, {2, 5}};
        RealMatrix testMatrix = MatrixUtils.createRealMatrix(testData);

        // Create the decomposition
        RectangularCholeskyDecomposition decomposition = new RectangularCholeskyDecomposition(testMatrix, 1e-10);

        // Test the getRank() method
        int rank = decomposition.getRank();
        Assert.assertEquals(2, rank);
    }

}