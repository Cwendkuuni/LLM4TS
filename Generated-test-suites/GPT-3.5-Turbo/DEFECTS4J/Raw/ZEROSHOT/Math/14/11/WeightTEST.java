import static org.junit.Assert.*;
import org.junit.Test;

public class WeightTest {

    @Test
    public void testWeightConstructor() {
        double[] weightArr = {1.0, 2.0, 3.0};
        Weight weight = new Weight(weightArr);

        RealMatrix weightMatrix = weight.getWeight();
        assertEquals(weightArr.length, weightMatrix.getColumnDimension());
        assertEquals(weightArr.length, weightMatrix.getRowDimension());

        for (int i = 0; i < weightArr.length; i++) {
            assertEquals(weightArr[i], weightMatrix.getEntry(i, i), 0.0);
        }
    }

    @Test
    public void testWeightConstructorWithSquareMatrix() {
        double[][] matrixData = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(matrixData);
        Weight weight = new Weight(weightMatrix);

        assertSame(weightMatrix, weight.getWeight());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightConstructorWithNonSquareMatrix() {
        double[][] matrixData = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(matrixData);
        new Weight(weightMatrix);
    }
}