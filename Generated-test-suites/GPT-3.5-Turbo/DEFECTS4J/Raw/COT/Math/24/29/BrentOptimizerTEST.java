

import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {
  
  private static final double EPSILON = 1e-10;
  
  private BrentOptimizer brentOptimizer;
  
  private double expected;
  private double result;
  
  @Before
  public void setUp() {
    brentOptimizer = new BrentOptimizer(1e-10, 1e-10);
  }

  @Test
  public void testConstructorWithRelAndAbs() {
    brentOptimizer = new BrentOptimizer(1e-10, 1e-10);
    assertNotNull(brentOptimizer);
  }

  @Test(expected = NumberIsTooSmallException.class)
  public void testConstructorWithInvalidRel() {
    brentOptimizer = new BrentOptimizer(1e-12, 1e-10);
  }

  @Test(expected = NotStrictlyPositiveException.class)
  public void testConstructorWithInvalidAbs() {
    brentOptimizer = new BrentOptimizer(1e-10, -1e-10);
  }

  @Test
  public void testConstructorWithRelAndAbsAndChecker() {
    ConvergenceChecker<UnivariatePointValuePair> checker = new CustomConvergenceChecker();
    brentOptimizer = new BrentOptimizer(1e-10, 1e-10, checker);
    assertNotNull(brentOptimizer);
  }

  @Test
  public void testConstructorWithRelAndAbsAndNullChecker() {
    brentOptimizer = new BrentOptimizer(1e-10, 1e-10, null);
    assertNotNull(brentOptimizer);
  }

  @Test
  public void testConstructorWithRelAndAbsAndNullCheckerAndGoalType() {
    brentOptimizer = new BrentOptimizer(1e-10, 1e-10, GoalType.MINIMIZE, null);
    assertNotNull(brentOptimizer);
  }

  @Test(expected = NumberIsTooSmallException.class)
  public void testConstructorWithInvalidRelAndAbsAndNullCheckerAndGoalType() {
    brentOptimizer = new BrentOptimizer(1e-12, 1e-10, GoalType.MINIMIZE, null);
  }

  @Test(expected = NotStrictlyPositiveException.class)
  public void testConstructorWithRelAndInvalidAbsAndNullCheckerAndGoalType() {
    brentOptimizer = new BrentOptimizer(1e-10, -1e-10, GoalType.MINIMIZE, null);
  }

  @Test
  public void testOptimize() {
    double expected = 0;
    double result = 0;
    UnivariatePointValuePair actualResult = brentOptimizer.optimize(-10, 10);
    assertEquals(expected, actualResult.getPoint(), EPSILON);
    assertEquals(result, actualResult.getValue(), EPSILON);
  }

}

