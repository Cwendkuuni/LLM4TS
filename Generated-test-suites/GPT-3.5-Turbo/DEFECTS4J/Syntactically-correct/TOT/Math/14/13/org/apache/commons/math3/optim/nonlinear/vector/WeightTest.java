import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.apache.commons.math3.optimization.Weight;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WeightTest {

    private Weight weight;

    @Before
    public void setUp() {
        double[] weights = {1.0, 2.0, 3.0};
        weight = new Weight(weights);
    }

    @Test
    public void testWeightConstructor() {
        double[] weights = {1.0, 2.0, 3.0};
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(3, 3);
        expectedMatrix.setEntry(0, 0, 1.0);
        expectedMatrix.setEntry(1, 1, 2.0);
        expectedMatrix.setEntry(2, 2, 3.0);

        RealMatrix weightMatrix = weight.getWeight();

        assertArrayEquals("Weight constructor should set the weight matrix correctly", expectedMatrix.getData(),
                weightMatrix.getData());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testWeightConstructorNonSquareMatrix() {
        double[][] invalidMatrixData = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        RealMatrix invalidMatrix = new Array2DRowRealMatrix(invalidMatrixData);
        new Weight(invalidMatrix);
    }

    @Test
    public void testGetWeight() {
        RealMatrix weightMatrix = weight.getWeight();
        assertEquals("getWeight should return a copy of the weight matrix", weightMatrix, weight.getWeight());

        weightMatrix.setEntry(0, 0, 5.0);
        RealMatrix updatedWeightMatrix = weight.getWeight();
        assertNotEquals("getWeight should return a copy of the weight matrix, not a reference to the original matrix",
                weightMatrix, updatedWeightMatrix);
    }

}