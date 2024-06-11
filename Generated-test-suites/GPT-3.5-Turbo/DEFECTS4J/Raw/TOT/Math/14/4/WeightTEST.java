import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.apache.commons.math3.optimization.Weight;

import org.junit.Assert;
import org.junit.Test;

public class WeightTest {

    // Test case for constructor 1: Weight(double[] weight)
    @Test
    public void testConstructor1() {
        // Typical use case
        double[] weight = {1, 2, 3};
        Weight obj = new Weight(weight);
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(3, 3);
        expectedMatrix.setEntry(0, 0, 1);
        expectedMatrix.setEntry(1, 1, 2);
        expectedMatrix.setEntry(2, 2, 3);
        Assert.assertEquals(expectedMatrix, obj.getWeight());

        // Edge case: Empty weight array
        double[] emptyWeight = {};
        obj = new Weight(emptyWeight);
        expectedMatrix = new Array2DRowRealMatrix(0, 0);
        Assert.assertEquals(expectedMatrix, obj.getWeight());

        // Error scenario: Negative weight values
        double[] negativeWeight = {1, -2, 3};
        try {
            obj = new Weight(negativeWeight);
            Assert.fail("Expected NonSquareMatrixException to be thrown");
        } catch (NonSquareMatrixException e) {
            // Exception expected
        }
    }

    // Test case for constructor 2: Weight(RealMatrix weight)
    @Test
    public void testConstructor2() {
        // Typical use case
        double[][] matrixData = {{1, 2}, {3, 4}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(matrixData);
        Weight obj = new Weight(weightMatrix);
        Assert.assertEquals(weightMatrix, obj.getWeight());

        // Error scenario: Non-square weight matrix
        double[][] nonSquareMatrixData = {{1, 2, 3}, {4, 5, 6}};
        RealMatrix nonSquareWeightMatrix = new Array2DRowRealMatrix(nonSquareMatrixData);
        try {
            obj = new Weight(nonSquareWeightMatrix);
            Assert.fail("Expected NonSquareMatrixException to be thrown");
        } catch (NonSquareMatrixException e) {
            // Exception expected
        }
    }
}