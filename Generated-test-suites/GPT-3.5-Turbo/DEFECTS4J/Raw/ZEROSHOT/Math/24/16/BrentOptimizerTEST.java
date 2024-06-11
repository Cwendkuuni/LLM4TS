package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;

public class BrentOptimizerTest {
  
  @Test
  public void testDoOptimize() {
    double rel = 0.01;
    double abs = 0.1;
    BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    
    optimizer.setMin(0);
    optimizer.setStartValue(1);
    optimizer.setMax(3);
    optimizer.setGoalType(GoalType.MINIMIZE);
    
    UnivariatePointValuePair result = optimizer.doOptimize();
    assertEquals(1.0, result.getPoint(), 0.0001);
  }
  
  @Test(expected = NotStrictlyPositiveException.class)
  public void testConstructorWithInvalidAbs() {
    double rel = 0.01;
    double abs = 0;
    BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
  }
  
  @Test(expected = NumberIsTooSmallException.class)
  public void testConstructorWithInvalidRel() {
    double rel = 0;
    double abs = 0.1;
    BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
  }
}