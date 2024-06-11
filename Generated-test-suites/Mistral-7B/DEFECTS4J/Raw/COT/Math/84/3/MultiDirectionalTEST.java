import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleMaxEvaluator;
import org.apache.commons.math3.optim.StoppingCondition;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiDirectionalTest {

 private MultiDirectional optimizer;
 private RealPointValuePair[] simplex;
 private Comparator<RealPointValuePair> comparator;
 private double[] xSmallest;
 private FunctionEvaluationException functionEvaluationException;
 private OptimizationException optimizationException;

 @Before
 public void setUp() {
 optimizer = new MultiDirectional();
 simplex = new RealPointValuePair[5];
 comparator = new Comparator<RealPointValuePair>() {
 @Override
 public int compare(RealPointValuePair o1, RealPointValuePair o2) {
 return o1.getValue().compareTo(o2.getValue());
 }
 };
 xSmallest = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
 }

 @After
 public void tearDown() {
 optimizer = null;
 simplex = null;
 comparator = null;
 xSmallest = null;
 }

 @Test
 public void testIterateSimplexWithDefaultCoefficients() throws FunctionEvaluationException, OptimizationException {
 // given
 PointValuePair goal = new PointValuePair(new double[]{2.0, 2.0}, 0.0);
 optimizer.setGoalType(GoalType.MINIMIZE);
 optimizer.setMaxIterations(10);
 optimizer.setStoppingCondition(new RealConvergenceChecker(1e-6));
 SimpleMaxEvaluator evaluator = new SimpleMaxEvaluator(new FunctionUnderTest(), simplex, comparator);
 optimizer.setMaxEvaluations(100);
 optimizer.optimize(evaluator);

 // then
 assertNotNull(optimizer.getBestPoint());
 assertArrayEquals(goal.getPoint().toArray(), optimizer.getBestPoint().getPointRef(), 0.001);
 assertEquals(goal.getValue(), optimizer.getFunctionValue(), 0.001);
 }

 @Test
 public void testIterateSimplexWithCustomCoefficients() throws FunctionEvaluationException, OptimizationException {
 // given
 PointValuePair goal = new PointValueValuePair(new double[]{2.0, 2.0}, 0.0);
 optimizer = new MultiDirectional(3.0, 0.6);
 optimizer.setGoalType(GoalType.MINIMIZE);
 optimizer.setMaxIterations(10);
 optimizer.setStoppingCondition(new RealConvergenceChecker(1e-6));
 SimpleMaxEvaluator evaluator = new SimpleMaxEvaluator(new FunctionUnderTest(), simplex, comparator);
 optimizer.setMaxEvaluations(100);
 optimizer.optimize(evaluator);

 // then
 assertNotNull(optimizer.getBestPoint());
 assertArrayEquals(goal.getPoint().toArray(), optimizer.getBestPoint().getPointRef(), 0.001);
 assertEquals(goal.getValue(), optimizer.getFunctionValue(), 0.001);
 }

 @Test(expected = FunctionEvaluationException.class)
 public void testFunctionEvaluationException() throws FunctionEvaluationException, OptimizationException {
 // given
 optimizer.setGoalType(GoalType.MINIMIZE);
 optimizer.setMaxIterations(1);
 optimizer.setStoppingCondition(new RealConvergenceChecker(1e-6));
 SimpleMaxEvaluator evaluator = new SimpleMaxEvaluator(new FunctionUnderThrowingException(), simplex, comparator);
 optimizer.setMaxEvaluations(1);
 optimizer.optimize(evaluator);

 // then
 fail("FunctionEvaluationException should be thrown");
 }

 @Test(expected = OptimizationException.class)
 public void testOptimizationException() throws FunctionEvaluationException, OptimizationException {
 // given
 optimizer.setGoalType(GoalType.MINIMIZE);
 optimizer.setMaxIterations(1);
 optimizer.setStoppingCondition(new RealConvergenceChecker(1e-6));
 SimpleMaxEvaluator evaluator = new SimpleMaxEvaluator(new FunctionUnderMinimalValue(), simplex, comparator);
 optimizer.setMaxEvaluations(0);
 optimizer.optimize(evaluator);

 // then
 fail("OptimizationException should be thrown");
 }

 @Test
 public void testEvaluateNewSimplex() throws FunctionEvaluationException, OptimizationException {
 // given
 RealPointValuePair[] original = new RealPointValuePair[5];
 double[] xOriginal1 = new double[]{1.0, 2.0};
 double[] xOriginal2 = new double[]{3.0, 3.0};
 double[] xOriginal3 = new double[]{4.0, 4.0};
 double[] xOriginal4 = new double[]{5.0, 5.0};
 double[] xOriginal5 = new double[]{6.0, 6.0};
 double[] xSmallest = new double[]{1.0, 1.0};
 double coeff = 2.0;

 for (int i = 0; i < 5; i++) {
 original[i] = new RealPointValuePair(xOriginalI, Double.NaN, false);
 }
 simplex = original;

 // when
 RealPointValuePair result = optimizer.evaluateNewSimplex(simplex, coeff, comparator);

 // then
 assertEquals(xSmallest[0], result.getPointRef()[0], 0.001);
 assertEquals(xSmallest[1], result.getPointRef()[1], 0.001);
 }

}

class FunctionUnderTest implements org.apache.commons.math3.optim.UnaryFunction {
 @Override
 public double value(double x) {
 return Math.pow(x, 2);
 }
}

class FunctionUnderThrowingException implements org.apache.commons.math3.optim.UnaryFunction {
 @Override
 public double value(double x) throws FunctionEvaluationException {
 throw new FunctionEvaluationException("Test Exception");
 }
}

class FunctionUnderMinimalValue implements org.apache.commons.math3.optim.UnaryFunction {
 @Override
 public double value(double x) {
 return Double.MIN_VALUE;
 }
}