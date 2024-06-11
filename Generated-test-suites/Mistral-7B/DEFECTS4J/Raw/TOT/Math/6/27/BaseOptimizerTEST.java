package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.optim.MaxEvalException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnconvergedException;
import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest<PAIR extends PointValuePair> {

 private BaseOptimizer<PAIR> optimizer;
 private ConvergenceChecker<PAIR> convergenceChecker;

 @Before
 public void setUp() {
 optimizer = new DummyBaseOptimizer<>();
 convergenceChecker = new DefaultConvergenceChecker<>();
 optimizer.setConvergenceChecker(convergenceChecker);
 }

 @Test
 public void testGetMaxEvaluations() {
 int maxEvaluations = optimizer.getMaxEvaluations();
 assertEquals("Maximum number of function evaluations should be set", 100, maxEvaluations);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetMaxEvaluationsIfNotSet() {
 optimizer = new DummyBaseOptimizer<>();
 int maxEvaluations = optimizer.getMaxEvaluations();
 fail("Maximum number of function evaluations should not be set by default");
 }

 @Test
 public void testGetEvaluations() {
 optimizer.optimize(new DummyFunction(), new DummyPoint());
 int evaluations = optimizer.getEvaluations();
 assertEquals("Number of evaluations should be 1 after first optimization call", 1, evaluations);
 }

 @Test
 public void testGetEvaluationsAfterMaxEvaluations() {
 optimizer.setMaxEvaluations(5);
 optimizer.optimize(new DummyFunction(), new DummyPoint());
 optimizer.optimize(new DummyFunction(), new DummyPoint());
 int evaluations = optimizer.getEvaluations();
 assertEquals("Number of evaluations should be 5 after reaching the maximum", 5, evaluations);
 }

 @Test(expected = MaxEvalException.class)
 public void testGetEvaluationsAfterMaxEvaluationsReached() {
 optimizer.setMaxEvaluations(5);
 optimizer.optimize(new DummyFunction(), new DummyPoint());
 optimizer.optimize(new DummyFunction(), new DummyPoint());
 optimizer.optimize(new DummyFunction(), new DummyPoint());
 optimizer.optimize(new DummyFunction(), new DummyPoint());
 optimizer.optimize(new DummyFunction(), new DummyPoint());
 optimizer.optimize(new DummyFunction(), new DummyPoint()); // Should throw MaxEvalException
 }

 @Test
 public void testGetConvergenceChecker() {
 ConvergenceChecker<PAIR> convergenceChecker = optimizer.getConvergenceChecker();
 assertSame("Convergence checker should be the one set", this.convergenceChecker, convergenceChecker);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetConvergenceCheckerIfNotSet() {
 optimizer = new DummyBaseOptimizer<>();
 ConvergenceChecker<PAIR> convergenceChecker = optimizer.getConvergenceChecker();
 fail("Convergence checker should not be set by default");
 }
}

class DummyFunction implements org.apache.commons.math3.optim.ObjectiveFunction<Double> {
 @Override
 public Double valueOf(Double point) {
 return point * point;
 }

 @Override
 public Double gradientValue(Double point) {
 return 2.0 * point;
 }
}

class DummyPoint implements org.apache.commons.math3.optim.PointValuePair {
 @Override
 public Double getPoint() {
 return 0.0;
 }

 @Override
 public Double getValue() {
 return 0.0;
 }
}

class DefaultConvergenceChecker implements ConvergenceChecker<PAIR> {
 @Override
 public boolean isConverged(PAIR currentValue, PAIR previousValue) {
 return Math.abs(currentValue.getValue() - previousValue.getValue()) < 1e-6;
 }
}