import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math3.linear.NonSquareMatrixException;

public class WeightTest {

    private double[] diagonalWeights;
    private RealMatrix squareMatrix;
    private RealMatrix nonSquareMatrix;

    @Before
    public void setUp() {
        diagonalWeights = new double[]{1.0, 2.0, 3.0};
        squareMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}});
        nonSquareMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}});
    }

    @Test
    public void testConstructorWithDoubleArray() {
        Weight weight = new Weight(diagonalWeights);

        RealMatrix expectedWeightMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 3.0}});
        Assert.assertEquals(expectedWeightMatrix, weight.getWeight());
    }

    @Test
    public void testConstructorWithRealMatrix() {
        Weight weight = new Weight(squareMatrix);

        Assert.assertEquals(squareMatrix, weight.getWeight());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        Weight weight = new Weight(nonSquareMatrix);
    }
}