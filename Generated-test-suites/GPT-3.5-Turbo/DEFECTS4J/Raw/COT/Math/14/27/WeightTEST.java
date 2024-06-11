
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class WeightTest {
    @Test
    public void testWeightConstructor1() {
        double[] input = {1.0, 2.0, 3.0};
        Weight weight = new Weight(input);
        
        double[][] expected = {{1.0, 0.0, 0.0},
                               {0.0, 2.0, 0.0},
                               {0.0, 0.0, 3.0}};
        RealMatrix expectedMatrix = new Array2DRowRealMatrix(expected);
        
        assertEquals(expectedMatrix, weight.getWeight());
    }
    
    @Test
    public void testWeightConstructor2() {
        double[][] input = {{1.0, 0.0, 0.0},
                            {0.0, 2.0, 0.0},
                            {0.0, 0.0, 3.0}};
        RealMatrix inputMatrix = new Array2DRowRealMatrix(input);
        
        Weight weight = new Weight(inputMatrix);
        
        assertEquals(inputMatrix, weight.getWeight());
    }
    
    @Test(expected = NonSquareMatrixException.class)
    public void testWeightConstructor2WithNonSquareMatrixException() {
        double[][] input = {{1.0, 0.0},
                            {0.0, 2.0},
                            {0.0, 0.0}};
        RealMatrix inputMatrix = new Array2DRowRealMatrix(input);
        
        Weight weight = new Weight(inputMatrix);
    }
}
