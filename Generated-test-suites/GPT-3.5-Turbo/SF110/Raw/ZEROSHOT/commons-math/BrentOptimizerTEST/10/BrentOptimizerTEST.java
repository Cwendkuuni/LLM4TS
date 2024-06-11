package org.apache.commons.math3.optim.univariate;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.util.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {
  
  private BrentOptimizer optimizer;
  
  @Before
  public void setUp() {
    optimizer = new BrentOptimizer(0.001, 0.0001);
  }
  
  @After
  public void tearDown() {
    optimizer = null;
  }
  
  @Test(expected = NumberIsTooSmallException.class)
  public void testConstructorWithInvalidRel() {
    optimizer = new BrentOptimizer(-0.001, 0.0001);
  }
  
  @Test(expected = NotStrictlyPositiveException.class)
  public void testConstructorWithInvalidAbs() {
    optimizer = new BrentOptimizer(0.001, 0.0);
  }
  
  @Test
  public void testDoOptimize() {
    double min = 0;
    double max = 10;
    GoalType goalType = GoalType.MINIMIZE;
    double expected = 0.0;
    double result = optimizer.doOptimize();
    assertEquals(expected, result, 0.0001);
  }
  
  @Test
  public void testBest() {
    UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 0.1);
    UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 0.2);
    boolean isMinim = true;
    UnivariatePointValuePair expected = a;
    UnivariatePointValuePair result = optimizer.best(a, b, isMinim);
    assertEquals(expected, result);
  }
  
}