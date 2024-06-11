import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class WeightTest {

    private Weight weight;

    @Before
    public void setUp() {
        double[] diagonal = {1.0, 2.0, 3.0};
        weight = new Weight(diagonal);
    }

    @Test
    public void testConstructorWithArray() {
        double[][] expectedData = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}};
        RealMatrix expectedWeight = new Array2DRowRealMatrix(expectedData);
        RealMatrix actualWeight = weight.getWeight();
        assertTrue(actualWeight.equals(expectedWeight));
    }

    @Test
    public void testConstructorWithMatrix() {
        double[][] data = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}};
        RealMatrix inputWeight = new Array2DRowRealMatrix(data);
        weight = new Weight(inputWeight);
        RealMatrix actualWeight = weight.getWeight();
        assertTrue(actualWeight.equals(inputWeight));
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        RealMatrix invalidWeight = new Array2DRowRealMatrix(data);
        weight = new Weight(invalidWeight);
    }

}