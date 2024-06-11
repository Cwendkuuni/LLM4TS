import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;

public class WeightTest {

    private Weight weight;

    @Before
    public void setUp() {
        double[] w = {1.0, 2.0, 3.0};
        weight = new Weight(w);
    }

    @Test
    public void testConstructorWithArray() {
        double[] expectedEntries = {1.0, 0.0, 0.0,
                                    0.0, 2.0, 0.0,
                                    0.0, 0.0, 3.0};
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(expectedEntries);

        RealMatrix actualMatrix = weight.getWeight();

        assertArrayEquals(expectedMatrix.getData(), actualMatrix.getData());
    }

    @Test
    public void testConstructorWithRealMatrix() {
        double[][] entries = {{1.0, 0.0, 0.0},
                              {0.0, 2.0, 0.0},
                              {0.0, 0.0, 3.0}};
        RealMatrix inputMatrix = new Array2DRowRealMatrix(entries);

        weight = new Weight(inputMatrix);

        RealMatrix actualMatrix = weight.getWeight();

        assertArrayEquals(inputMatrix.getData(), actualMatrix.getData());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        double[][] entries = {{1.0, 0.0},
                              {0.0, 2.0}};
        RealMatrix inputMatrix = new Array2DRowRealMatrix(entries);

        weight = new Weight(inputMatrix);
    }
}