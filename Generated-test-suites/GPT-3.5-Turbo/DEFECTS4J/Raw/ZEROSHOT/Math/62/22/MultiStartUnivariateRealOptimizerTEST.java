import static org.junit.Assert.*;

import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.ConvergenceChecker;
import org.apache.commons.math.random.RandomGenerator;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.util.FastMath;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {
  
  private MultiStartUnivariateRealOptimizer<FUNC> optimizer;
  private BaseUnivariateRealOptimizer<FUNC> singleStartOptimizer;
  
  @Before
  public void setUp() {
    singleStartOptimizer = new BaseUnivariateRealOptimizer<FUNC>() {
      public void setConvergenceChecker(ConvergenceChecker<UnivariateRealPointValuePair> checker) {}
      public ConvergenceChecker<UnivariateRealPointValuePair> getConvergenceChecker() {
        return null;
      }
      
      public int getMaxEvaluations() {
        return 0;
      }
      
      public int getEvaluations() {
        return 0;
      }
      
      public void setMaxEvaluations(int maxEvaluations) {}
      
      public UnivariateRealPointValuePair optimize(final FUNC f, final GoalType goal,
                                                 final double min, final double max,
                                                 final double startValue)
        throws FunctionEvaluationException {
          return null;
        }
    };
    optimizer = new MultiStartUnivariateRealOptimizer<FUNC>(singleStartOptimizer, 5, null);
  }
  
  @Test
  public void testSetConvergenceChecker() {
    ConvergenceChecker<UnivariateRealPointValuePair> convergenceChecker = null;
    optimizer.setConvergenceChecker(convergenceChecker);
    assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
  }
  
  @Test
  public void testGetMaxEvaluations() {
    assertEquals(0, optimizer.getMaxEvaluations());
  }
  
  @Test
  public void testGetEvaluations() {
    assertEquals(0, optimizer.getEvaluations());
  }
  
  @Test
  public void testSetMaxEvaluations() {
    int maxEvaluations = 100;
    optimizer.setMaxEvaluations(maxEvaluations);
    assertEquals(maxEvaluations, optimizer.getMaxEvaluations());
  }
  
  @Test
  public void testGetOptima() {
    try {
      optimizer.getOptima();
      fail("No optimum computed yet");
    } catch (MathIllegalStateException e) {
      assertEquals(LocalizedFormats.NO_OPTIMUM_COMPUTED_YET, e.getSpecifier());
    }
  }
  
  @Test
  public void testOptimize() {
    UnivariateRealPointValuePair result = null;
    UnivariateRealFunction f = null;
    GoalType goal = null;
    double min = 0;
    double max = 0;
    
    try {
      result = optimizer.optimize(f, goal, min, max);
      fail("Multi-start optimizer didn't throw ConvergenceException when no convergence with any start point");
    } catch (ConvergenceException e) {
      assertEquals(LocalizedFormats.NO_CONVERGENCE_WITH_ANY_START_POINT, e.getSpecifier());
    }
    assertNull(result);
  }
  
  @Test
  public void testSortPairs() {
    GoalType goal = GoalType.MINIMIZE;
    UnivariateRealPointValuePair[] pointValuePairs = new UnivariateRealPointValuePair[3];
    pointValuePairs[0] = new UnivariateRealPointValuePair(1.0, 1.0);
    pointValuePairs[1] = new UnivariateRealPointValuePair(2.0, 2.0);
    pointValuePairs[2] = new UnivariateRealPointValuePair(3.0, 3.0);
    
    optimizer.sortPairs(goal);
    
    assertEquals(3.0, pointValuePairs[0].getValue());
    assertEquals(2.0, pointValuePairs[1].getValue());
    assertEquals(1.0, pointValuePairs[2].getValue());
  }
}