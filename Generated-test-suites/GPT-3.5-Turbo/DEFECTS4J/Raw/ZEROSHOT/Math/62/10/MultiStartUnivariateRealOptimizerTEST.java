import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.random.RandomGenerator;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathIllegalStateException;

public class MultiStartUnivariateRealOptimizerTest {
  
  private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer;
  
  @Before
  public void setup() {
    BaseUnivariateRealOptimizer<UnivariateRealFunction> singleStartOptimizer = new FakeUnivariateRealOptimizer();
    optimizer = new MultiStartUnivariateRealOptimizer<>(singleStartOptimizer, 3, new FakeRandomGenerator());
  }
  
  @Test
  public void testSetConvergenceChecker() {
    ConvergenceChecker<UnivariateRealPointValuePair> checker = new FakeConvergenceChecker();
    optimizer.setConvergenceChecker(checker);
    assertEquals(checker, optimizer.getConvergenceChecker());
  }
  
  @Test
  public void testGetMaxEvaluations() {
    optimizer.setMaxEvaluations(100);
    assertEquals(100, optimizer.getMaxEvaluations());
  }
  
  @Test
  public void testGetEvaluations() {
    assertEquals(0, optimizer.getEvaluations());
    optimizer.optimize(null, GoalType.MINIMIZE, 0, 1);
    assertEquals(0, optimizer.getEvaluations());
  }
  
  @Test
  public void testOptimize() throws Exception {
    UnivariateRealPointValuePair result = optimizer.optimize(null, GoalType.MINIMIZE, 0, 1);
    assertNotNull(result);
    assertNotNull(optimizer.getOptima());
  }
  
  @Test(expected = MathIllegalStateException.class)
  public void testGetOptimaWithoutOptimize() {
    optimizer.getOptima();
  }
  
  private class FakeUnivariateRealOptimizer implements BaseUnivariateRealOptimizer<UnivariateRealFunction> {
    private ConvergenceChecker<UnivariateRealPointValuePair> checker;
    private int maxEvaluations;
    private int evaluations;
    
    public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {
      this.checker = checker;
    }
    
    public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
      return checker;
    }
    
    public int getMaxEvaluations() {
      return maxEvaluations;
    }
    
    public void setMaxEvaluations(int maxEvaluations) {
      this.maxEvaluations = maxEvaluations;
    }
    
    public int getEvaluations() {
      return evaluations;
    }
    
    public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max) {
      evaluations++;
      return new UnivariateRealPointValuePair(0, 0);
    }
    
    public UnivariateRealPointValuePair optimize(UnivariateRealFunction f, GoalType goal, double min, double max, double startValue) {
      evaluations++;
      return new UnivariateRealPointValuePair(0, 0);
    }
    
  }
  
  private class FakeRandomGenerator implements RandomGenerator {
    public double nextDouble() {
      return 0;
    }
    
    public boolean nextBoolean() {
      return true;
    }
    
    public void nextBytes(byte[] bytes) {
      
    }

    public float nextFloat() {
      return 0;
    }

    public double nextGaussian() {
      return 0;
    }

    public int nextInt() {
      return 0;
    }

    public int nextInt(int n) {
      return 0;
    }

    public long nextLong() {
      return 0;
    }

    public void setSeed(int seed) {
      
    }

    public void setSeed(int[] seed) {
      
    }

    public void setSeed(long seed) {
      
    }
  }
  
  private class FakeConvergenceChecker implements ConvergenceChecker<UnivariateRealPointValuePair> {
    public boolean converged(int iteration, UnivariateRealPointValuePair previous, UnivariateRealPointValuePair current) {
      return true;
    }
  }
}