package org.apache.commons.math.optimization.direct;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValueFunction;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class MultiDirectionalTest {

 private PointValueFunction testFunction;
 private MultiDirectional optimizer;
 private InitialGuess initialGuess;
 private SimpleBounds bounds;
 private Comparator<PointValuePair> comparator;

 @Before
 public void setUp() {
 testFunction = new TestFunction();
 optimizer = new MultiDirectional();
 initialGuess = new InitialGuess(new double[]{1.0, 1.0});
 bounds = new SimpleBounds(new double[]{-10.0, -10.0}, new double[]{10.0, 10.0});
 comparator = new Comparator<PointValuePair>() {
 @Override
 public int compare(PointValuePair o1, PointValuePair o2) {
 return Double.compare(o1.getValue(), o2.getValue());
 }
 };
 }

 @After
 public void tearDown() {
 optimizer = null;
 testFunction = null;
 initialGuess = null;
 bounds = null;
 comparator = null;
 }

 @Test
 public void testDefaultConstructor() {
 optimizer = new MultiDirectional();
 assertEquals(2.0, optimizer.khi, 0.001);
 assertEquals(0.5, optimizer.gamma, 0.001);
 }

 @Test
 public void testCustomConstructor() {
 optimizer = new MultiDirectional(3.0, 0.7);
 assertEquals(3.0, optimizer.khi, 0.001);
 assertEquals(0.7, optimizer.gamma, 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidConstructor() {
 optimizer = new MultiDirectional(-1.0, 1.0);
 }

 @Test
 public void testIterateSimplex() throws FunctionEvaluationException, MaxEvalExceededException, OptimizationException {
 optimizer.setFunction(testFunction);
 optimizer.setInitialGuess(initialGuess);
 optimizer.optimize(bounds, GoalType.MINIMIZE, 100, comparator);
 PointValuePair best = optimizer.getBestSolution();
 assertNotNull(best);
 assertEquals(0.0, best.getValue(), 0.001);
 }

 @Test
 public void testIterateSimplexReflection() throws FunctionEvaluationException, MaxEvalExceededException, OptimizationException {
 optimizer.setFunction(testFunction);
 optimizer.setInitialGuess(initialGuess);
 optimizer.setMaxIterations(1);
 optimizer.optimize(bounds, GoalType.MINIMIZE, comparator);
 PointValuePair best = optimizer.getBestSolution();
 assertNotNull(best);
 assertEquals(0.0, best.getValue(), 0.001);
 }

 @Test
 public void testIterateSimplexExpansion() throws FunctionEvaluationException, MaxEvalExceededException, OptimizationException {
 optimizer.setFunction(testFunction);
 optimizer.setInitialGuess(initialGuess);
 optimizer.setMaxIterations(2);
 optimizer.optimize(bounds, GoalType.MINIMIZE, comparator);
 PointValuePair best = optimizer.getBestSolution();
 assertNotNull(best);
 assertNull(optimizer.getLastIterationPoint());
 }

 @Test
 public void testIterateSimplexContraction() throws FunctionEvaluationException, MaxEvalExceededException, OptimizationException {
 optimizer.setFunction(testFunction);
 optimizer.setInitialGuess(initialGuess);
 optimizer.setMaxIterations(1);
 optimizer.optimize(bounds, GoalType.MINIMIZE, comparator);
 PointValuePair best = optimizer.getBestSolution();
 assertNotNull(best);
 assertEquals(0.0, best.getValue(), 0.001);
 }

 @Test
 public void testIterateSimplexConvergence() throws FunctionEvaluationException, MaxEvalExceededException, OptimizationException {
 optimizer.setFunction(testFunction);
 optimizer.setInitialGuess(initialGuess);
 optimizer.setMaxIterations(100);
 optimizer.optimize(bounds, GoalType.MINIMIZE, comparator);
 PointValuePair best = optimizer.getBestSolution();
 assertNotNull(best);
 assertEquals(0.0, best.getValue(), 0.001);
 }

}

class TestFunction implements PointValueFunction {

 @Override
 public double value(double[] x) {
 return Math.pow(Math.sin(Math.PI * x[0]) + Math.sin(Math.PI * x[1]), 2.0);
 }

 @Override
 public int getNumberOfVariables() {
 return 2;
 }

}