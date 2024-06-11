import org.junit.Test;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

public class WeightTest {

    private final double[] weights = {1, 2, 3, 4};
    private final RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][]{{1, 0, 0, 0},
                                                                                     {0, 2, 0, 0},
                                                                                     {0, 0, 3, 0},
                                                                                     {0, 0, 0, 4}});


    @Test
    public void testConstructorWithArray() {
        Weight weight = new Weight(weights);
        RealMatrix result = weight.getWeight();
        assertEquals(weightMatrix, result);
    }

    @Test
    public void testConstructorWithMatrix() {
        Weight weight = new Weight(weightMatrix);
        RealMatrix result = weight.getWeight();
        assertEquals(weightMatrix, result);
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        RealMatrix nonSquareMatrix = new Array2DRowRealMatrix(new double[][]{{1, 2, 3},
                                                                             {4, 5, 6}});
        new Weight(nonSquareMatrix);
    }
}