import org.apache.commons.math.optimization.univariate.*;
import org.apache.commons.math.analysis.*;

import static org.junit.Assert.*;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {
  
  @Test
  public void testSetConvergenceChecker() {
    ConvergenceChecker<UnivariateRealPointValuePair> checker = new DummyConvergenceChecker();
    BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new DummyUnivariateRealOptimizer();
    MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, 5, null);
    multiStartOptimizer.setConvergenceChecker(checker);
    assertEquals(checker, multiStartOptimizer.getConvergenceChecker());
  }
  
  @Test
  public void testGetMaxEvaluations() {
    BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new DummyUnivariateRealOptimizer();
    MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, 5, null);
    assertEquals(0, multiStartOptimizer.getMaxEvaluations());
  }
  
  @Test
  public void testGetEvaluations() {
    BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new DummyUnivariateRealOptimizer();
    MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, 5, null);
    assertEquals(0, multiStartOptimizer.getEvaluations());
  }
  
  @Test
  public void testSetMaxEvaluations() {
    BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new DummyUnivariateRealOptimizer();
    MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, 5, null);
    multiStartOptimizer.setMaxEvaluations(10);
    assertEquals(10, multiStartOptimizer.getMaxEvaluations());
  }
  
  @Test
  public void testGetOptima() {
    BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new DummyUnivariateRealOptimizer();
    MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, 5, null);
    assertNull(multiStartOptimizer.getOptima());
  }
  
  @Test
  public void testOptimize1() {
    UnivariateRealFunction function = new DummyUnivariateRealFunction();
    GoalType goal = GoalType.MAXIMIZE;
    double min = 1.0;
    double max = 5.0;
    BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new DummyUnivariateRealOptimizer();
    MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, 5, null);
    UnivariateRealPointValuePair result = multiStartOptimizer.optimize(function, goal, min, max);
    assertNotNull(result);
  }
  
  @Test
  public void testOptimize2() {
    UnivariateRealFunction function = new DummyUnivariateRealFunction();
    GoalType goal = GoalType.MAXIMIZE;
    double min = 1.0;
    double max = 5.0;
    double startValue = 3.0;
    BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new DummyUnivariateRealOptimizer();
    MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, 5, null);
    UnivariateRealPointValuePair result = multiStartOptimizer.optimize(function, goal, min, max, startValue);
    assertNotNull(result);
  }
  
  private class DummyConvergenceChecker implements ConvergenceChecker<UnivariateRealPointValuePair> {
    public boolean converged(int iteration,
                             UnivariateRealPointValuePair previous,
                             UnivariateRealPointValuePair current) {
      return false;
    }
  }
  
  private class DummyUnivariateRealOptimizer implements BaseUnivariateRealOptimizer<UnivariateRealFunction> {
    public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {
      
    }
    
    public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
      return null;
    }
    
    public int getMaxEvaluations() {
      return 0;
    }
    
    public int getEvaluations() {
      return 0;
    }
    
    public void setMaxEvaluations(int maxEvaluations) {
      
    }
    
    public UnivariateRealPointValuePair optimize(final UnivariateRealFunction f, final GoalType goal,
                                                 final double min, final double max)
        throws FunctionEvaluationException {
      return null;
    }
    
    public UnivariateRealPointValuePair optimize(final UnivariateRealFunction f, final GoalType goal,
                                                 final double min, final double max,
                                                 final double startValue)
        throws FunctionEvaluationException {
      return null;
    }
  }
  
  private class DummyUnivariateRealFunction implements UnivariateRealFunction {
    public double value(double x) {
      return 0.0;
    }
  }

}