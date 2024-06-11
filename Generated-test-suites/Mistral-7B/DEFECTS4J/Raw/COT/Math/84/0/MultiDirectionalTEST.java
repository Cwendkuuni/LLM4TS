import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleOptimizer;
import org.junit.Assert;
import org.junit.Test;

public class MultiDirectionalTest {

 private MultiDirectional optimizer;
 private double[] xSmallest = new double[]{1.0, 2.0};
 private Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
 @Override
 public int compare(RealPointValuePair o1, RealPointValuePair o2) {
 return Double.compare(o1.getValue(), o2.getValue());
 }
 };

 @Test
 public void testMultiDirectionalDefaultConstructor() {
 optimizer = new MultiDirectional();
 Assert.assertEquals(2.0, optimizer.khi, 0.01);
 Assert.assertEquals(0.5, optimizer.gamma, 0.01);
 }

 @Test
 public void testMultiDirectionalCustomConstructor() {
 optimizer = new MultiDirectional(3.0, 0.8);
 Assert.assertEquals(3.0, optimizer.khi, 0.01);
 Assert.assertEquals(0.8, optimizer.gamma, 0.01);
 }

 @Test
 public void testIterateSimplexNormalCase() throws Exception {
 optimizer = new MultiDirectional();
 RealPointValuePair[] original = new RealPointValuePair[2];
 original[0] = new RealPointValuePair(new double[]{1.0, 1.0}, 1.0, false);
 original[1] = new RealPointValuePair(new double[]{2.0, 2.0}, 2.0, false);
 optimizer.simplex = original;
 optimizer.comparator = comparator;

 optimizer.iterateSimplex(comparator);

 RealPointValuePair best = optimizer.simplex[0];
 Assert.assertNotEquals(best.getValue(), 1.0);
 Assert.assertNotEquals(best.getValue(), 2.0);
 }

 @Test(expected = FunctionEvaluationException.class)
 public void testIterateSimplexFunctionEvaluationException() throws Exception {
 optimizer = new MultiDirectional();
 RealPointValuePair[] original = new RealPointValuePair[2];
 original[0] = new RealPointValuePair(new double[]{1.0, 1.0}, 1.0, false);
 original[1] = new RealPointValuePair(new double[]{2.0, 2.0}, 2.0, false);
 optimizer.simplex = original;
 optimizer.comparator = comparator;

 mockFunctionEvaluationException();
 optimizer.iterateSimplex(comparator);
 }

 @Test(expected = OptimizationException.class)
 public void testIterateSimplexOptimizationException() throws Exception {
 optimizer = new MultiDirectional();
 RealPointValuePair[] original = new RealPointValuePair[2];
 original[0] = new RealPointValuePair(new double[]{1.0, 1.0}, 1.0, false);
 original[1] = new RealPointValuePair(new double[]{2.0, 2.0}, 2.0, false);
 optimizer.simplex = original;
 optimizer.comparator = comparator;

 mockOptimizationException();
 optimizer.iterateSimplex(comparator);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIterateSimplexIllegalArgumentException() throws Exception {
 optimizer = new MultiDirectional();
 RealPointValuePair[] original = new RealPointValuePair[0];
 optimizer.simplex = original;
 optimizer.comparator = comparator;

 optimizer.iterateSimplex(comparator);
 }

 @Test
 public void testEvaluateNewSimplexNormalCase() throws Exception {
 optimizer = new MultiDirectional();
 RealPointValuePair[] original = new RealPointValuePair[2];
 original[0] = new RealPointValuePair(new double[]{1.0, 1.0}, 1.0, false);
 original[1] = new RealPointValuePair(new double[]{2.0, 2.0}, 2.0, false);
 optimizer.simplex = original;
 optimizer.comparator = comparator;

 optimizer.evaluateNewSimplex(original, 1.5, comparator);

 RealPointValuePair best = optimizer.simplex[0];
 Assert.assertNotEquals(best.getValue(), 1.0);
 Assert.assertNotEquals(best.getValue(), 2.0);
 }

 @Test(expected = FunctionEvaluationException.class)
 public void testEvaluateNewSimplexFunctionEvaluationException() throws Exception {
 optimizer = new MultiDirectional();
 RealPointValuePair[] original = new RealPointValuePair[2];
 original[0] = new RealPointValuePair(new double[]{1.0, 1.0}, 1.0, false);
 original[1] = new RealPointValuePair(new double[]{2.0, 2.0}, 2.0, false);
 optimizer.simplex = original;
 optimizer.comparator = comparator;

 mockFunctionEvaluationException();
 optimizer.evaluateNewSimplex(original, 1.5, comparator);
 }

 @Test(expected = OptimizationException.class)
 public void testEvaluateNewSimplexOptimizationException() throws Exception {
 optimizer = new MultiDirectional();
 RealPointValuePair[] original = new RealPointValuePair[2];
 original[0] = new RealPointValuePair(new double[]{1.0, 1.0}, 1.0, false);
 original[1] = new RealPointValuePair(new double[]{2.0, 2.0}, 2.0, false);
 optimizer.simplex = original;
 optimizer.comparator = comparator;

 mockOptimizationException();
 optimizer.evaluateNewSimplex(original, 1.5, comparator);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEvaluateNewSimplexIllegalArgumentException() throws Exception {
 optimizer = new MultiDirectional();
 RealPointValuePair[] original = new RealPointValuePair[0];
 optimizer.simplex = original;
 optimizer.comparator = comparator;

 optimizer.evaluateNewSimplex(original, 1.5, comparator);
 }

 private void mockFunctionEvaluationException() throws FunctionEvaluationException {
 throw new FunctionEvaluationException("Function evaluation exception");
 }

 private void mockOptimizationException() throws OptimizationException {
 throw new OptimizationException("Optimization exception");
 }

}