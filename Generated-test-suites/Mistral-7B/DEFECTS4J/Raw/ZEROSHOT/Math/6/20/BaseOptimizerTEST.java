package org.apache.commons.math3.optimization.testing;

import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.SimpleBounds;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest {

 @Test
 public void testGetMaxEvaluations() {
 BaseOptimizer<Object> optimizer = new BaseMockOptimizer(10);
 assertEquals(10, optimizer.getMaxEvaluations());
 }

 @Test
 public void testGetEvaluations() {
 BaseOptimizer<Object> optimizer = new BaseMockOptimizer(10);
 optimizer.optimize(new MockFunction(), new SimpleBounds(0.0, 1.0), GoalType.MINIMIZE, new MaxEval(5));
 assertEquals(5, optimizer.getEvaluations());
 }

 @Test
 public void testGetConvertergenceChecker() {
 BaseOptimizer<Object> optimizer = new BaseMockOptimizer(10);
 assertNull(optimizer.getConvergenceChecker());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetConvertergenceCheckerWhenSet() {
 BaseOptimizer<Object> optimizer = new BaseMockOptimizer(10);
 optimizer.setConvergenceChecker(new ConvergenceChecker<Object>() {});
 optimizer.getConvergenceChecker();
 }

 @Test
 public void testSetConvertergenceChecker() {
 BaseOptimizer<Object> optimizer = new BaseMockOptimizer(10);
 ConvergenceChecker<Object> convergenceChecker = new ConvergenceChecker<Object>() {};
 optimizer.setConvergenceChecker(convergenceChecker);
 assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
 }

 public static class BaseMockOptimizer<PAIR> implements BaseOptimizer<PAIR> {
 private int maxEvaluations;
 private int evaluations;
 private ConvergenceChecker<PAIR> convergenceChecker;

 public BaseMockOptimizer(int maxEvaluations) {
 this.maxEvaluations = maxEvaluations;
 }

 @Override
 public int getMaxEvaluations() {
 return maxEvaluations;
 }

 @Override
 public int getEvaluations() {
 return evaluations;
 }

 @Override
 public ConvergenceChecker<PAIR> getConvergenceChecker() {
 return convergenceChecker;
 }

 @Override
 public void setConvergenceChecker(ConvergenceChecker<PAIR> convergenceChecker) {
 this.convergenceChecker = convergenceChecker;
 }
 }

 public static class MockFunction implements UnivariateFunction {
 @Override
 public double value(double x) {
 return Math.pow(x, 2);
 }
 }
}