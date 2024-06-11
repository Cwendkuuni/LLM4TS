import org.junit.Test;
import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

import static org.junit.Assert.*;

public class WeightTest {

    @Test
    public void testWeightMatrixConstructor() {
        double[][] values = {{1, 0}, {0, 1}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(values);
        Weight weight = new Weight(weightMatrix);
        assertEquals(weightMatrix, weight.getWeight());
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testInvalidWeightMatrix() {
        double[][] values = {{1, 0, 0}, {0, 1, 0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(values);
        new Weight(weightMatrix);
    }

    @Test
    public void testDiagonalWeightConstructor() {
        double[] weightValues = {1, 2, 3};
        Weight weight = new Weight(weightValues);
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(new double[][]{{1, 0, 0}, {0, 2, 0}, {0, 0, 3}});
        assertEquals(expectedMatrix, weight.getWeight());
    }

    @Test
    public void testEmptyWeightConstructor() {
        double[] weightValues = {};
        Weight weight = new Weight(weightValues);
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(0, 0);
        assertEquals(expectedMatrix, weight.getWeight());
    }

    @Test
    public void testImmutableWeightObject() {
        double[] weightValues = {1, 2, 3};
        Weight weight = new Weight(weightValues);
        RealMatrix initialMatrix = weight.getWeight();
        
        weightValues[0] = 5;
        
        RealMatrix updatedMatrix = weight.getWeight();
        assertNotEquals(initialMatrix, updatedMatrix);
        assertEquals(new Array2DRowRealMatrix(new double[][]{{1, 0, 0}, {0, 2, 0}, {0, 0, 3}}), updatedMatrix);
    }
}