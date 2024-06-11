
import org.junit.Test;
import static org.junit.Assert.*;

public class PowellOptimizerTest {

  // Test constructor 1
  @Test
  public void testConstructor1() {
    PowellOptimizer optimizer = new PowellOptimizer(0.0001, 0.001);
    assertNotNull(optimizer);
  }

  // Test constructor 2
  @Test
  public void testConstructor2() {
    PowellOptimizer optimizer = new PowellOptimizer(0.0001, 0.001, null);
    assertNotNull(optimizer);
  }

  // Test constructor 3
  @Test
  public void testConstructor3() {
    PowellOptimizer optimizer = new PowellOptimizer(0.0001, 0.001, 0.1, 0.01);
    assertNotNull(optimizer);
  }

  // Test constructor 4
  @Test
  public void testConstructor4() {
    PowellOptimizer optimizer = new PowellOptimizer(0.0001, 0.001, 0.1, 0.01, null);
    assertNotNull(optimizer);
  }

  // Test doOptimize() method
  @Test
  public void testDoOptimize() {
    PowellOptimizer optimizer = new PowellOptimizer(0.0001, 0.001);
    double[] startPoint = {1.0, 2.0, 3.0};
    PointValuePair result = optimizer.doOptimize(startPoint);
    assertNotNull(result);
  }

  // Test deprecated methods
  @Test
  public void testDeprecatedMethods() {
    PowellOptimizer optimizer = new PowellOptimizer(0.0001, 0.001);
    double[] startPoint = {1.0, 2.0, 3.0};
    PointValuePair result = optimizer.optimize(1, new TestFunction(), GoalType.MINIMIZE, startPoint);
    assertNotNull(result);
  }
}
