import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

public class WeightTest {
    private final double DELTA = 1e-15;
    
    /** Test case for the constructor Weight(double[] weight) */
    @Test
    public void testWeightConstructor1() {
        // Typical use case
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        RealMatrix result = w.getWeight();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                      {0.0, 2.0, 0.0},
                                                                      {0.0, 0.0, 3.0}});
        assertTrue(result.equals(expected, DELTA));
        
        // Edge case: empty array
        double[] emptyWeight = {};
        w = new Weight(emptyWeight);
        result = w.getWeight();
        expected = new Array2DRowRealMatrix(new double[][]{});
        assertTrue(result.equals(expected, DELTA));
    }
    
    /** Test case for the constructor Weight(RealMatrix weight) */
    @Test
    public void testWeightConstructor2() {
        // Typical use case
        RealMatrix weightMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                          {0.0, 2.0, 0.0},
                                                                          {0.0, 0.0, 3.0}});
        Weight w = new Weight(weightMatrix);
        RealMatrix result = w.getWeight();
        RealMatrix expected = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                                                                      {0.0, 2.0, 0.0},
                                                                      {0.0, 0.0, 3.0}});
        assertTrue(result.equals(expected, DELTA));
        
        // Error scenario: Non-square matrix
        RealMatrix nonSquareMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 2.0, 3.0},
                                                                             {4.0, 5.0, 6.0}});
        try {
            w = new Weight(nonSquareMatrix);
            fail("Expected NonSquareMatrixException");
        } catch (NonSquareMatrixException e) {
            // Passed the test
        }
    }
}