package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.*;
import org.junit.Test;

public class BrentOptimizerTest {

  // Test cases for BrentOptimizer class methods

  @Test
  public void testDoOptimize() {
    // Test case to check the optimization functionality
    BrentOptimizer optimizer = new BrentOptimizer(1e-12, 1e-12);
    UnivariatePointValuePair result = optimizer.doOptimize();
    assertNotNull(result);
  }

  @Test
  public void testBest() {
    // Test case to check the best method functionality
    UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
    UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 5.0);
    boolean isMinim = true;
    BrentOptimizer optimizer = new BrentOptimizer(1e-12, 1e-12);
    UnivariatePointValuePair result = optimizer.best(a, b, isMinim);
    assertEquals(b, result);
  }

  // Additional test cases for edge cases and exceptions can be added here

}