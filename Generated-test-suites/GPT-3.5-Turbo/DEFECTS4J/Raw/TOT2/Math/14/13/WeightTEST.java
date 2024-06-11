import static org.junit.Assert.*;
import org.junit.Test;

public class WeightTest {

    @Test
    public void testWeightConstructorWithArray() {
        double[] weightValues = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weightValues);

        RealMatrix expectedMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                            {0.0, 2.0, 0.0},
                                                                            {0.0, 0.0, 3.0}});
        RealMatrix actualMatrix = weight.getWeight();

        assertArrayEquals(expectedMatrix.getData(), actualMatrix.getData());
    }

    @Test
    public void testWeightConstructorWithRealMatrix() {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0},
                                                                         {0.0, 2.0}});
        Weight weight = new Weight(inputMatrix);

        RealMatrix actualMatrix = weight.getWeight();

        assertArrayEquals(inputMatrix.getData(), actualMatrix.getData());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightConstructorWithNonSquareMatrix() {
        RealMatrix inputMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0},
                                                                         {0.0, 2.0},
                                                                         {0.0, 0.0}});
        Weight weight = new Weight(inputMatrix);
    }
}