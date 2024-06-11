import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.ConvergenceChecker;
import org.apache.commons.math.optimization.univariate.BaseUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;

public class MultiStartUnivariateRealOptimizerTest {
  
  private static class DummyOptimzer implements BaseUnivariateRealOptimizer<UnivariateRealFunction> {
  
    private int evaluations = 0;
    
    public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {
      // Dummy implementation
    }
  
    public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
      // Dummy implementation
      return null;
    }
    
    public int getMaxEvaluations() {
      // Dummy implementation
      return 0;
    }
    
    public void setMaxEvaluations(int maxEvaluations) {
      // Dummy implementation
    }
    
    public int getEvaluations() {
      return evaluations;
    }
    
    public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max) {
      evaluations++;
      return new UnivariateRealPointValuePair(0.0, 0.0);
    }
    
    public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max, double startValue) {
      evaluations++;
      return new UnivariateRealPointValuePair(0.0, 0.0);
    }
  
  }
  
  private DummyOptimizer dummyOptimizer;
  private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer;
  
  @Before
  public void setUp() {
    dummyOptimizer = new DummyOptimizer();
    optimizer = new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(dummyOptimizer, 3, null);
  }
  
  @Test
  public void testOptimize_singleStart() throws Exception {
    dummyOptimizer.setMaxEvaluations(50);
    UnivariateRealPointValuePair result = optimizer.optimize(null, GoalType.MINIMIZE, 0.0, 10.0);
    Assert.assertEquals(1, dummyOptimizer.getEvaluations());
    Assert.assertNotNull(result);
  }
  
  @Test
  public void testOptimize_multipleStarts() throws Exception {
    dummyOptimizer.setMaxEvaluations(50);
    UnivariateRealPointValuePair result = optimizer.optimize(null, GoalType.MINIMIZE, 0.0, 10.0);
    Assert.assertEquals(3, dummyOptimizer.getEvaluations());
    Assert.assertNotNull(result);
  }
  
  @Test(expected = MathIllegalStateException.class)
  public void testGetOptima_noOptimumComputedYet() throws Exception {
    optimizer.getOptima();
  }
  
  @Test(expected = ConvergenceException.class)
  public void testOptimize_noConvergenceWithAnyStartPoint() throws Exception {
    dummyOptimizer.setConvergenceChecker(new ConvergenceChecker<UnivariateRealPointValuePair>() {
      public boolean converged(int iteration, UnivariateRealPointValuePair previous, UnivariateRealPointValuePair current) {
        return false;
      }
    });
    optimizer.optimize(null, GoalType.MINIMIZE, 0.0, 10.0);
  }
  
}