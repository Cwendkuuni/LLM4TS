
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

public class WeightTest {

    private Weight weight;

    @Before
    public void setUp() {
        double[] diagonal = {1.0, 2.0, 3.0};
        weight = new Weight(diagonal);
    }

    @Test
    public void testConstructorWithDiagonalArray() {
        double[] diagonal = {1.0, 2.0, 3.0};
        Array2DRowRealMatrix expectedMatrix = new Array2DRowRealMatrix(3, 3);
        expectedMatrix.setEntry(0, 0, 1.0);
        expectedMatrix.setEntry(1, 1, 2.0);
        expectedMatrix.setEntry(2, 2, 3.0);

        Assert.assertEquals(expectedMatrix, weight.getWeight());
    }

    @Test
    public void testConstructorWithWeightMatrix() {
        double[][] matrixData = {{1.0, 0.0}, {0.0, 1.0}};
        RealMatrix inputMatrix = new Array2DRowRealMatrix(matrixData);
        weight = new Weight(inputMatrix);

        Assert.assertEquals(inputMatrix, weight.getWeight());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorThrowsNonSquareMatrixException() {
        double[][] matrixData = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        RealMatrix inputMatrix = new Array2DRowRealMatrix(matrixData);
        weight = new Weight(inputMatrix);
    }   
}