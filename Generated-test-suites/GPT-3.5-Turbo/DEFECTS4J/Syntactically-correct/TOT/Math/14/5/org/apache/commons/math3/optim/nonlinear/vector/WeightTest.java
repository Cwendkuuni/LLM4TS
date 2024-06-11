import org.junit.Test;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.apache.commons.math3.optimization.Weight;

import static org.junit.Assert.*;

public class WeightTest {

    @Test
    public void testConstructorWithDoubleArray() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);

        RealMatrix expectedMatrix = new Array2DRowRealMatrix(3, 3);
        expectedMatrix.setEntry(0, 0, 1.0);
        expectedMatrix.setEntry(1, 1, 2.0);
        expectedMatrix.setEntry(2, 2, 3.0);

        assertArrayEquals(expectedMatrix.getData(), w.getWeight().getData());
    }

    @Test
    public void testConstructorWithRealMatrix() {
        double[][] data = {{1.0, 0.0, 0.0},
                           {0.0, 2.0, 0.0},
                           {0.0, 0.0, 3.0}};

        RealMatrix weightMatrix = new Array2DRowRealMatrix(data);
        Weight w = new Weight(weightMatrix);

        assertArrayEquals(weightMatrix.getData(), w.getWeight().getData());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        double[][] data = {{1.0, 0.0},
                           {0.0, 2.0},
                           {0.0, 0.0}};

        RealMatrix weightMatrix = new Array2DRowRealMatrix(data);
        Weight w = new Weight(weightMatrix);
    }
}