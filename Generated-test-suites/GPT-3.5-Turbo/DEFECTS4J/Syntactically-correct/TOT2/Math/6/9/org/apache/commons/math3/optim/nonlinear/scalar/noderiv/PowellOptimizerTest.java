import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PowellOptimizerTest {

  private PowellOptimizer optimizer;

  @Before
  public void setUp() {
    optimizer = new PowellOptimizer(1e-6, 1e-6);
  }

  @Test
  public void testConstructor() {
    assertNotNull(optimizer);
  }

  @Test
  public void testDoOptimize() {
    double[] startPoint = {1.0, 2.0, 3.0};
    PointValuePair result = optimizer.doOptimize(startPoint);
    assertNotNull(result);
  }

  @Test
  public void testNewPointAndDirection() {
    double[] point = {2.0, 3.0, 4.0};
    double[] direction = {0.5, 0.6, 0.7};
    double optimum = 0.8;
    double[][] result = optimizer.newPointAndDirection(point, direction, optimum);
    assertNotNull(result);
  }

  @Test
  public void testLineSearch() {
    double[] startPoint = {1.0, 2.0, 3.0};
    double[] direction = {0.2, 0.3, 0.4};
    UnivariatePointValuePair result = optimizer.line.search(startPoint, direction);
    assertNotNull(result);
  }

  @Test
  public void testSearch() {
    double[] startPoint = {1.0, 2.0, 3.0};
    double[] direction = {0.2, 0.3, 0.4};
    UnivariatePointValuePair result = optimizer.line.search(startPoint, direction);
    assertNotNull(result);
  }

}