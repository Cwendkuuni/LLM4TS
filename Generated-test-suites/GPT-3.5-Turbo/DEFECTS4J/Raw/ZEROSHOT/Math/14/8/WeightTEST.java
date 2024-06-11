import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class WeightTest {

    private Weight weight;
    private double[] diagonal;

    @Before
    public void setUp() {
        diagonal = new double[]{1.0, 2.0, 3.0};
        weight = new Weight(diagonal);
    }

    @Test
    public void testConstructorWithArray() {
        RealMatrix result = weight.getWeight();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}});
        assertArrayEquals(expected.getData(), result.getData());
    }

    @Test
    public void testConstructorWithMatrix() {
        double[][] matrixData = {{4.0, 0.0, 0.0}, {0.0, 5.0, 0.0}, {0.0, 0.0, 6.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        weight = new Weight(matrix);

        RealMatrix result = weight.getWeight();
        RealMatrix expected = new Array2DRowRealMatrix(matrixData);
        assertArrayEquals(expected.getData(), result.getData());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        double[][] matrixData = {{1.0, 2.0, 3.0}};
        RealMatrix matrix = new Array2DRowRealMatrix(matrixData);
        weight = new Weight(matrix);
    }

}