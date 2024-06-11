import org.apache.commons.math3.optimization.Weight;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WeightTest {
    
    private Weight weight;
    
    @Before
    public void setUp() {
        double[] weightValues = {1.0, 2.0, 3.0};
        weight = new Weight(weightValues);
    }
    
    @Test
    public void testConstructorWithValues() {
        RealMatrix expectedWeightMatrix = new Array2DRowRealMatrix(3, 3);
        expectedWeightMatrix.setEntry(0, 0, 1.0);
        expectedWeightMatrix.setEntry(1, 1, 2.0);
        expectedWeightMatrix.setEntry(2, 2, 3.0);
        
        assertEquals(expectedWeightMatrix, weight.getWeight());
    }
    
    @Test
    public void testConstructorWithMatrix() {
        RealMatrix weightMatrix = new Array2DRowRealMatrix(3, 3);
        weightMatrix.setEntry(0, 0, 1.0);
        weightMatrix.setEntry(1, 1, 2.0);
        weightMatrix.setEntry(2, 2, 3.0);
        
        Weight weight = new Weight(weightMatrix);
        
        assertEquals(weightMatrix, weight.getWeight());
    }
    
    @Test(expected = NonSquareMatrixException.class)
    public void testConstructorWithNonSquareMatrix() {
        RealMatrix weightMatrix = new Array2DRowRealMatrix(3, 2);
        
        Weight weight = new Weight(weightMatrix);
    }
}