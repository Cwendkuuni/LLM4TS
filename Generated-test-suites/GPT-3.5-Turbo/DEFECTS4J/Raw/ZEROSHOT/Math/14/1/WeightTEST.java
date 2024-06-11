import org.junit.Test;
import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

import static org.junit.Assert.*;

public class WeightTest {

    @Test
    public void testConstructor1() {
        double[] weight = {1, 2, 3};
        Weight w = new Weight(weight);

        RealMatrix expectedWeightMatrix = new Array2DRowRealMatrix(3, 3);
        expectedWeightMatrix.setEntry(0, 0, 1);
        expectedWeightMatrix.setEntry(1, 1, 2);
        expectedWeightMatrix.setEntry(2, 2, 3);

        assertTrue(expectedWeightMatrix.equals(w.getWeight()));
    }

    @Test
    public void testConstructor2() {
        double[][] entries = {{1, 0}, {0, 1}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(entries);
        Weight w = new Weight(weightMatrix);

        RealMatrix expectedWeightMatrix = new Array2DRowRealMatrix(entries);

        assertTrue(expectedWeightMatrix.equals(w.getWeight()));
    }

    @Test(expected = NonSquareMatrixException.class)
    public void testConstructor2NonSquareMatrixException() {
        double[][] entries = {{1, 0}, {0, 1}, {0, 0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(entries);
        Weight w = new Weight(weightMatrix);        
    }

}