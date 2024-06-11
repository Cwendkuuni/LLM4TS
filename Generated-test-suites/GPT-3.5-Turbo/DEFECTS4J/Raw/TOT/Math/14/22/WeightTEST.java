import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.linear.MatrixUtils;

public class WeightTest {

    // Test case for the constructor that accepts an array of weights
    @Test
    public void testWeightArrayConstructor() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        
        RealMatrix expectedMatrix = MatrixUtils.createRealIdentityMatrix(3).scalarMultiply(1.0);
        
        assertArrayEquals(expectedMatrix.getData(), weight.getWeight().getData());
    }

    // Test case for the constructor that accepts a RealMatrix
    @Test
    public void testWeightMatrixConstructor() {
        double[][] matrixData = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        
        Weight weight = new Weight(matrix);
        
        assertArrayEquals(matrix.getData(), weight.getWeight().getData());
    }

    // Test case for the constructor that throws NonSquareMatrixException
    @Test(expected = NonSquareMatrixException.class)
    public void testWeightMatrixConstructorNonSquareMatrix() {
        double[][] matrixData = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        RealMatrix matrix = MatrixUtils.createRealMatrix(matrixData);
        
        Weight weight = new Weight(matrix);
    }

    // Test case for the getWeight method
    @Test
    public void testGetWeight() {
        double[] weights = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weights);
        
        RealMatrix expectedMatrix = MatrixUtils.createRealIdentityMatrix(3).scalarMultiply(1.0);
        
        assertArrayEquals(expectedMatrix.getData(), weight.getWeight().getData());
    }

}