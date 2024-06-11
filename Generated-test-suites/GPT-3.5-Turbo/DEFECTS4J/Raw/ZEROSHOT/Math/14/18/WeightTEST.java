import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {

    private Weight weight;

    @Before
    public void setUp() {
        double[] values = {1.0, 2.0, 3.0};
        weight = new Weight(values);
    }

    @Test
    public void testConstructorWithArray() {
        double[] expectedValues = {1.0, 2.0, 3.0};
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(3, 3);
        expectedMatrix.setEntry(0, 0, 1.0);
        expectedMatrix.setEntry(1, 1, 2.0);
        expectedMatrix.setEntry(2, 2, 3.0);

        RealMatrix actualMatrix = weight.getWeight();

        assertArrayEquals(expectedValues, weight.getWeight().getData(), 0.001);
        assertEquals(expectedMatrix, actualMatrix);
    }

    @Test
    public void testConstructorWithRealMatrix() {
        double[][] values = {{1.0, 0.0}, {0.0, 2.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(values);

        weight = new Weight(matrix);

        assertEquals(matrix, weight.getWeight());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        double[][] values = {{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(values);

        weight = new Weight(matrix);
    }
}