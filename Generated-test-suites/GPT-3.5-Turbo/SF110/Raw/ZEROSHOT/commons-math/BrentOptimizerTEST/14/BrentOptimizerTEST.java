package org.apache.commons.math3.optim.univariate;

import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.util.*;

import static org.junit.Assert.*;

public class BrentOptimizerTest {

  private BrentOptimizer optimizer;

  @Before
  public void setup() {
    optimizer = new BrentOptimizer(0.001, 0.001);
  }

  @Test(expected = NumberIsTooSmallException.class)
  public void testConstructorThrowsNumberIsTooSmallException() {
    new BrentOptimizer(0.0001, 0.001);
  }

  @Test(expected = NotStrictlyPositiveException.class)
  public void testConstructorThrowsNotStrictlyPositiveException() {
    new BrentOptimizer(0.001, 0.0);
  }

  @Test
  public void testDoOptimize() {
    optimizer.setMin(1.0);
    optimizer.setMax(5.0);
    optimizer.setStartValue(3.0);

    UnivariatePointValuePair result = optimizer.doOptimize();
    assertEquals(3.0, result.getPoint(), 0.001);
  }

  @Test
  public void testBest() {
    UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 2.0);
    UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 1.0);

    UnivariatePointValuePair best = optimizer.best(a, b, true);
    assertEquals(1.0, best.getPoint(), 0.001);
    assertEquals(2.0, best.getValue(), 0.001);
  }
}