import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WeightTest {
    
    private Weight weight;
    
    @Before
    public void setUp() {
        double[] weightArray = {1.0, 2.0, 3.0};
        weight = new Weight(weightArray);
    }
    
    @Test
    public void testGetWeight() {
        double[][] expectedArray = {{1.0, 0.0, 0.0},
                                    {0.0, 2.0, 0.0},
                                    {0.0, 0.0, 3.0}};
        RealMatrix expected = new Array2DRowRealMatrix(expectedArray);
        RealMatrix actual = weight.getWeight();
        
        assertArrayEquals(expected.getData(), actual.getData());
    }
    
    @Test
    public void testWeightConstructorWithArray() {
        double[] weightArray = {4.0, 5.0, 6.0};
        Weight newWeight = new Weight(weightArray);
        
        double[][] expectedArray = {{4.0, 0.0, 0.0},
                                    {0.0, 5.0, 0.0},
                                    {0.0, 0.0, 6.0}};
        RealMatrix expected = new Array2DRowRealMatrix(expectedArray);
        RealMatrix actual = newWeight.getWeight();
        
        assertArrayEquals(expected.getData(), actual.getData());
    }
    
    @Test
    public void testWeightConstructorWithRealMatrix() {
        double[][] weightArray = {{7.0, 0.0, 0.0},
                                  {0.0, 8.0, 0.0},
                                  {0.0, 0.0, 9.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weightArray);
        Weight newWeight = new Weight(weightMatrix);
        
        RealMatrix expected = weightMatrix.copy();
        RealMatrix actual = newWeight.getWeight();
        
        assertArrayEquals(expected.getData(), actual.getData());
    }
    
    @Test(expected = NonSquareMatrixException.class)
    public void testWeightConstructorWithNonSquareMatrix() {
        double[][] weightArray = {{7.0, 0.0, 0.0},
                                  {0.0, 8.0, 0.0}};
        RealMatrix weightMatrix = new Array2DRowRealMatrix(weightArray);
        Weight newWeight = new Weight(weightMatrix);
    }
    
}