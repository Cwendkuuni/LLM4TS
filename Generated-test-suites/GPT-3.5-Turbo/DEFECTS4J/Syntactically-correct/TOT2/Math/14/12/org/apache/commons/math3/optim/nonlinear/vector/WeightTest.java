import static org.junit.Assert.*;
import org.junit.Test;

public class WeightTest {
    
    @Test
    public void testWeightConstructorWithArray() {
        double[] weightValues = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weightValues);
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}});
        assertArrayEquals(expectedMatrix.getData(), weight.getWeight().getData());
    }

    @Test
    public void testWeightConstructorWithMatrix() {
        double[][] matrixValues = {{1.0, 0.0}, {0.0, 2.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(matrixValues);
        Weight weight = new Weight(weightMatrix);
        assertArrayEquals(matrixValues, weight.getWeight().getData());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightConstructorWithNonSquareMatrix() {
        double[][] nonSquareMatrixValues = {{1.0, 0.0}, {0.0, 2.0}, {0.0, 0.0}};
        RealMatrix nonSquareMatrix = new Array2DRowRealMatrix(nonSquareMatrixValues);
        Weight weight = new Weight(nonSquareMatrix);
    }

}