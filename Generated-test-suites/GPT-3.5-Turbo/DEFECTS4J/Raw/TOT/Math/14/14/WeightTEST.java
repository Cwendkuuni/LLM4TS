import static org.junit.Assert.*;
import org.junit.*;

public class WeightTest {
  
  @Test
  public void testDiagonalWeightMatrixConstructor() {
    double[] weights = {1.0, 2.0, 3.0};
    Weight weight = new Weight(weights);
    
    RealMatrix expectedMatrix = new Array2DRowRealMatrix(3, 3);
    expectedMatrix.setEntry(0, 0, 1.0);
    expectedMatrix.setEntry(1, 1, 2.0);
    expectedMatrix.setEntry(2, 2, 3.0);
    
    RealMatrix actualMatrix = weight.getWeight();
    
    assertTrue(actualMatrix.isEqualTo(expectedMatrix));
  }
  
  @Test
  public void testSquareWeightMatrixConstructor() {
    double[][] data = {{1.0, 0.5}, {0.5, 2.0}};
    RealMatrix weightMatrix = new Array2DRowRealMatrix(data);
    Weight weight = new Weight(weightMatrix);
    
    RealMatrix expectedMatrix = weightMatrix.copy();
    RealMatrix actualMatrix = weight.getWeight();
    
    assertTrue(actualMatrix.isEqualTo(expectedMatrix));
  }
  
  @Test(expected = NonSquareMatrixException.class)
  public void testNonSquareWeightMatrixConstructor() {
    double[][] data = {{1.0, 0.5, 1.0}, {0.5, 2.0, 0.5}};
    RealMatrix weightMatrix = new Array2DRowRealMatrix(data);
    Weight weight = new Weight(weightMatrix);
  }
  
  
  @Test
  public void testGetWeight() {
    double[][] data = {{1.0, 0.5}, {0.5, 2.0}};
    RealMatrix weightMatrix = new Array2DRowRealMatrix(data);
    Weight weight = new Weight(weightMatrix);
    
    RealMatrix expectedMatrix = weightMatrix.copy();
    RealMatrix actualMatrix = weight.getWeight();
    
    assertTrue(actualMatrix.isEqualTo(expectedMatrix));
  }
}