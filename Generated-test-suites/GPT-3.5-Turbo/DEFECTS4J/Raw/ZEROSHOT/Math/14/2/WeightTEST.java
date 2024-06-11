import org.junit.Test;
import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;

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
        
        assertEquals(expected, w.getWeight());
    }
    
    @Test
    public void testConstructorWithMatrix() {
        RealMatrix weight = new Array2DRowRealMatrix(3, 3);
        weight.setEntry(0, 0, 1.0);
        weight.setEntry(1, 1, 2.0);
        weight.setEntry(2, 2, 3.0);
        
        Weight w = new Weight(weight);
        
        assertEquals(weight, w.getWeight());
    }
    
    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        RealMatrix weight = new Array2DRowRealMatrix(3, 2);
        
        Weight w = new Weight(weight);
    }
}