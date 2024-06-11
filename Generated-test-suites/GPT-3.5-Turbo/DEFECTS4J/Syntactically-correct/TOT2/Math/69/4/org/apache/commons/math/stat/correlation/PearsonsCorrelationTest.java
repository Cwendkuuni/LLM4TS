import static org.junit.Assert.*;
import org.junit.Test;

public class PearsonsCorrelationTest {

  @Test
  public void testCorrelationWithValidArrays() {
    PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
    double[] xArray = {1.0, 2.0, 3.0};
    double[] yArray = {2.0, 4.0, 6.0};
    
    double expected = 1.0;
    double actual = pearsonsCorrelation.correlation(xArray, yArray);
    
    assertEquals(expected, actual, 0.0001);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCorrelationWithInvalidArrays() {
    PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
    double[] xArray = {1.0};
    double[] yArray = {2.0};
    
    pearsonsCorrelation.correlation(xArray, yArray);
  }
  
  @Test
  public void testComputeCorrelationMatrix() {
    PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
    double[][] data = {{1.0, 2.0, 3.0}, {2.0, 4.0, 6.0}};
    
    RealMatrix expected = new BlockRealMatrix(new double[][] {{1.0, 1.0}, {1.0, 1.0}});
    RealMatrix actual = pearsonsCorrelation.computeCorrelationMatrix(data);
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetCorrelationStandardErrors() {
    PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
    RealMatrix correlationMatrix = new BlockRealMatrix(new double[][] {{1.0, 0.5}, {0.5, 1.0}});
    
    RealMatrix expected = new BlockRealMatrix(new double[][] {{0.0, 0.5}, {0.5, 0.0}});
    RealMatrix actual = pearsonsCorrelation.getCorrelationStandardErrors(correlationMatrix);
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetCorrelationPValues() {
    PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
    RealMatrix correlationMatrix = new BlockRealMatrix(new double[][] {{1.0, 0.5}, {0.5, 1.0}});
    
    try {
      RealMatrix pValues = pearsonsCorrelation.getCorrelationPValues(correlationMatrix);
      assertNotNull(pValues);
    } catch (MathException ex) {
      fail("Exception thrown");
    }
  }

}