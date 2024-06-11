
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

public class WeightTest {

    @Test
    public void testWeightWithDiagonalMatrix() {
        double[] weight = {1, 2, 3};
        Weight w = new Weight(weight);
        
        RealMatrix expected = new Array2DRowRealMatrix(3, 3);
        expected.setEntry(0, 0, 1);
        expected.setEntry(1, 1, 2);
        expected.setEntry(2, 2, 3);
        
        RealMatrix actual = w.getWeight();
        
        assertTrue(actual.equals(expected));
    }

    @Test
    public void testWeightWithNonSquareMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}};
        RealMatrix weight = new Array2DRowRealMatrix(data);
        
        try {
            Weight w = new Weight(weight);
            fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            // Exception expected
        }
    }
}