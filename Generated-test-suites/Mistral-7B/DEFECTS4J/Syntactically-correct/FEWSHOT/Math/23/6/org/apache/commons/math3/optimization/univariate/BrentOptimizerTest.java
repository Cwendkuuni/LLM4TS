package org.apache.commons.cli;

import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 private BrentOptimizer optimizer;

 @Before
 public void setup() {
 optimizer = new BrentOptimizer(1e-5, 1e-5);
 }

 @Test
 public void testBrentOptimizerMinimumAtZero() {
 Function1D f = new Function1D() {
 @Override
 public double value(double x) {
 return x * x;
 }
 };
 UnivariatePointValuePair result = optimizer.optimize(f, 0.0, 1.0);
 assertEquals(0.0, result.getPoint(), 1e-5);
 }

 @Test
 public void testBrentOptimizerMinimumAtOne() {
 Function1D f = new Function1D() {
 @Override
 public double value(double x) {
 return Math.pow(x - 1.0, 2.0);
 }
 };
 UnivariatePointValuePair result = optimizer.optimize(f, 0.0, 2.0);
 assertEquals(1.0, result.getPoint(), 1e-5);
 }

 @Test
 public void testBrentOptimizerMinimumAtTwo() {
 Function1D f = new Function1D() {
 @Override
 public double value(double x) {
 return Math.pow(x - 2.0, 2.0);
 }
 };
 UnivariatePointValuePair result = optimizer.optimize(f, 1.0, 3.0);
 assertEquals(2.0, result.getPoint(), 1e-5);
 }

 @Test
 public void testBrentOptimizerMinimumNegative() {
 Function1D f = new Function1D() {
 @Override
 public double value(double x) {
 return Math.pow(x + 1.0, 2.0) - 1.0;
 }
 };
 UnivariatePointValuePair result = optimizer.optimize(f, -2.0, 0.0);
 assertEquals(-1.0, result.getPoint(), 1e-5);
 }

 @Test
 public void testBrentOptimizerMinimumThreePeaks() {
 Function1D f = new Function1D() {
 @Override
 public double value(double x) {
 if (x < 1.0) {
 return Math.pow(x, 2.0);
 } else if (x < 2.0) {
 return 2.0 * x - 3.0;
 } else {
 return Math.pow(x - 2.0, 2.0);
 }
 }
 };
 UnivariatePointValuePair result = optimizer.optimize(f, 0.0, 3.0);
 assertEquals(1.0, result.getPoint(), 1e-5);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testBrentOptimizerNegativeAbsoluteThreshold() {
 optimizer = new BrentOptimizer(1e-5, -1e-5);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testBrentOptimizerRelativeThresholdTooSmall() {
 optimizer = new BrentOptimizer(0.0, 1e-5);
 }
}