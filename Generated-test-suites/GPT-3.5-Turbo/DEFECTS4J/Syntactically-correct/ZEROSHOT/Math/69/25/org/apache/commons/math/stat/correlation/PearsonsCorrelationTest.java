import org.junit.Test;
import static org.junit.Assert.*;

public class PearsonsCorrelationTest {

  @Test
  public void testComputeCorrelationMatrix() {
    double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
    PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);
    RealMatrix correlationMatrix = pearsonsCorrelation.getCorrelationMatrix();
    double[][] expected = {{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}};
    assertArrayEquals(expected, correlationMatrix.getData());
  }

  @Test
  public void testCorrelation() {
    double[] xArray = {1.0, 2.0, 3.0};
    double[] yArray = {2.0, 4.0, 6.0};
    PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
    double correlation = pearsonsCorrelation.correlation(xArray, yArray);
    assertEquals(1.0, correlation, 0.000001);
  }

  @Test(expected=IllegalArgumentException.class)
  public void testInvalidCorrelationDimensions() {
    double[] xArray = {1.0};
    double[] yArray = {2.0, 4.0, 6.0};
    PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
    pearsonsCorrelation.correlation(xArray, yArray);
  }
}