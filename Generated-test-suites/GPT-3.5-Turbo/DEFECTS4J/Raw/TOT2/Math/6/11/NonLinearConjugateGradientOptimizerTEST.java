import org.junit.Test;

public class NonLinearConjugateGradientOptimizerTest {
  
  @Test
  public void testConstructorWithSimpleValueChecker() {
    NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
    assertNotNull(optimizer);
  }
  
  @Test
  public void testConstructorWithBrentSolver() {
    NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver());
    assertNotNull(optimizer);
  }
  
  @Test
  public void testSetInitialStepWithPositiveValue() {
    NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
    optimizer.setInitialStep(2.0);
    assertEquals(2.0, optimizer.initialStep, 0.0);
  }
  
  @Test
  public void testSetInitialStepWithNegativeValue() {
    NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
    optimizer.setInitialStep(-1.0);
    assertEquals(1.0, optimizer.initialStep, 0.0);
  }
  
  @Test
  public void testFindUpperBound() {
    NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
    UnivariateFunction f = new UnivariateFunction() {
      @Override
      public double value(double x) {
        return x * x - 4;
      }
    };
    double upperBound = optimizer.findUpperBound(f, 0, 1.0);
    assertEquals(3.0, upperBound, 0.0);
  }
}