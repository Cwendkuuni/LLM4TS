import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {
    
    @Test
    public void testConstructorWithArray() {
        double[] weight = {1.0, 2.0, 3.0};
        Weight w = new Weight(weight);
        RealMatrix expected = new Array2DRowRealMatrix(3, 3);
        expected.setEntry(0, 0, 1.0);
        expected.setEntry(1, 1, 2.0);
        expected.setEntry(2, 2, 3.0);
        
        assertTrue(w.getWeight().equals(expected));
    }
    
    @Test
    public void testConstructorWithMatrix() {
        double[][] matrixData = {{1.0, 0.0, 0.0},
                                 {0.0, 2.0, 0.0},
                                 {0.0, 0.0, 3.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(matrixData);
        Weight w = new Weight(weightMatrix);
        
        assertTrue(w.getWeight().equals(weightMatrix));
    }
    
    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        double[][] matrixData = {{1.0, 0.0, 0.0},
                                 {0.0, 2.0, 0.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(matrixData);
        Weight w = new Weight(weightMatrix);
    }
}