import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {

    private Weight weight;

    @Before
    public void setUp() {
        double[] diagonal = {1.0, 2.0, 3.0};
        weight = new Weight(diagonal);
    }

    @Test
    public void testConstructorWithWeightArray() {
        double[] diagonal = {1.0, 2.0, 3.0};
        Weight weight = new Weight(diagonal);
        RealMatrix expectedWeightMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                                 {0.0, 2.0, 0.0},
                                                                                 {0.0, 0.0, 3.0}});
        assertEquals(expectedWeightMatrix, weight.getWeight());
    }

    @Test
    public void testConstructorWithWeightMatrix() {
        double[][] array = {{1.0, 2.0, 3.0},
                            {4.0, 5.0, 6.0},
                            {7.0, 8.0, 9.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(array);
        Weight weight = new Weight(matrix);
        assertEquals(matrix, weight.getWeight());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        double[][] array = {{1.0, 2.0, 3.0},
                            {4.0, 5.0, 6.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(array);
        Weight weight = new Weight(matrix);
    }

    @Test
    public void testGetWeight() {
        RealMatrix expectedWeightMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                                 {0.0, 2.0, 0.0},
                                                                                 {0.0, 0.0, 3.0}});
        assertEquals(expectedWeightMatrix, weight.getWeight());
    }

}