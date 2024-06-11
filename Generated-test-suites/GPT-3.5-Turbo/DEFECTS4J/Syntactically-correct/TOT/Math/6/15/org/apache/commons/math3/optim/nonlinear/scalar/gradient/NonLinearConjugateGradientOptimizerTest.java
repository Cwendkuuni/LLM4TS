import static org.junit.Assert.*;
import org.junit.Test;

public class NonLinearConjugateGradientOptimizerTest {

  @Test
  public void testConstructorUpdateFormula() {
    // Test constructor with update formula
    NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
    assertNotNull(optimizer);
    assertEquals(ConjugateGradientFormula.FLETCHER_REEVES, optimizer.updateFormula);
    assertEquals(1.0, optimizer.initialStep, 0.001);
  }
  
  @Test
  public void testConstructorUpdateFormulaAndChecker() {
    // Test constructor with update formula and checker
    NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES, new SimpleValueChecker());
    assertNotNull(optimizer);
    assertEquals(ConjugateGradientFormula.FLETCHER_REEVES, optimizer.updateFormula);
    assertEquals(1.0, optimizer.initialStep, 0.001);
    assertNotNull(optimizer.getConvergenceChecker());
  }

  @Test
  public void testConstructorAllParameters() {
    // Test constructor with all parameters
    NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.POLAK_RIBIERE, new SimpleValueChecker(), new BrentSolver(), new IdentityPreconditioner());
    assertNotNull(optimizer);
    assertEquals(ConjugateGradientFormula.POLAK_RIBIERE, optimizer.updateFormula);
    assertEquals(1.0, optimizer.initialStep, 0.001);
    assertNotNull(optimizer.getConvergenceChecker());
    assertNotNull(optimizer.solver);
    assertNotNull(optimizer.preconditioner);
  }

  @Test
  public void testSetInitialStep() {
    // Test setInitialStep method
    NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
    optimizer.setInitialStep(2.0);
    assertEquals(2.0, optimizer.initialStep, 0.001);
  }

  @Test
  public void testFindUpperBound() {
    // Test findUpperBound method - bracketing case
    NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
    LineSearchFunction f = optimizer.new LineSearchFunction(new double[]{1, 1});
    double b = optimizer.findUpperBound(f, 0, 1);
    assertEquals(1.0, b, 0.001);
  }

  @Test(expected = MathIllegalStateException.class)
  public void testFindUpperBoundException() {
    // Test findUpperBound method - exception case
    NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
    LineSearchFunction f = optimizer.new LineSearchFunction(new double[]{1, 1});
    optimizer.findUpperBound(f, 0, 0);
  }

  @Test
  public void testDoOptimize() {
    // Test doOptimize method
    NonLinearConjugateGradientOptimizer optimizer = new NonLinearConjugateGradientOptimizer(ConjugateGradientFormula.FLETCHER_REEVES);
    optimizer.setMaxEvaluations(10000);
    optimizer.setStartPoint(new double[]{2});
    optimizer.setGoalType(GoalType.MINIMIZE);
    PointValuePair result = optimizer.doOptimize();
    assertNotNull(result);
    assertEquals(0, result.getPoint()[0], 0.001);
    assertEquals(0, result.getValue(), 0.001);
  }

}