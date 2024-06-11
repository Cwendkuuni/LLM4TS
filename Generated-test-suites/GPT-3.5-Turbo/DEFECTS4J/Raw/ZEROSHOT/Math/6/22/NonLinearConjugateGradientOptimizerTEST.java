import org.junit.*;
import static org.junit.Assert.*;

public class NonLinearConjugateGradientOptimizerTest {

  @Test
  public void testSetInitialStep() {
    NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
    optimizer.setInitialStep(0.5);
    assertEquals(0.5, optimizer.getInitialStep(), 0.0);
  }

  @Test
  public void testDoOptimize() {
    NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
    optimizer.setObjectiveFunction(new TestUnivariateFunction());
    double[] startPoint = {1, 2, 3};
    optimizer.setInitialPoint(startPoint);
    optimizer.optimize();
    double[] result = optimizer.getOptimalPoint();
    assertNotNull(result);
    assertEquals(3, result.length);
    assertEquals(0.0, result[0], 0.0);
    assertEquals(0.0, result[1], 0.0);
    assertEquals(0.0, result[2], 0.0);
  }

  @Test
  public void testFindUpperBound() {
    NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
    UnivariateFunction f = new TestUnivariateFunction();
    double a = 0.0;
    double h = 0.5;
    double upperBound = optimizer.findUpperBound(f, a, h);
    assertEquals(1.0, upperBound, 0.0);
  }
  
  class TestUnivariateFunction implements UnivariateFunction {
    public double value(double x) {
      return x * x * x;
    }
  }
}