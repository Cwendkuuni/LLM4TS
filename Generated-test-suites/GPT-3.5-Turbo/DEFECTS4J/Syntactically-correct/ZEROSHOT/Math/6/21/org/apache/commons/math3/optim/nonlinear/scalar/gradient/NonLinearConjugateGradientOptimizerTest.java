import static org.junit.Assert.*;
import org.junit.Test;

public class NonLinearConjugateGradientOptimizerTest {

  @Test
  public void testSetInitialStep() {
    NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
    optimizer.setInitialStep(0.5);
    double initialStep = optimizer.getInitialStep();
    assertEquals(0.5, initialStep, 0.0);
  }

  @Test
  public void testConstructor1() {
    NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
    assertNotNull(optimizer);
  }

  @Test
  public void testConstructor2() {
    NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker());
    assertNotNull(optimizer);
  }

  @Test
  public void testConstructor3() {
    NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver());
    assertNotNull(optimizer);
  }

  @Test
  public void testConstructor4() {
    NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker(), new BrentSolver(), new IdentityPreconditioner());
    assertNotNull(optimizer);
  }
}