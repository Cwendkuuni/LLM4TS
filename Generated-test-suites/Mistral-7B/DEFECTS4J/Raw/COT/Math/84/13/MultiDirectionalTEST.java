import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.direct.MultiDirectional;
import org.apache.commons.math3.optim.direct.MultiDirectionalDefaultSimplex;
import org.apache.commons.math3.optim.MathOptimizationException;
import org.apache.commons.math3.optim.PointValuePairComparator;
import org.junit.Assert;
import org.junit.Test;

public class MultiDirectionalTest {

 private final MultiDirectional optimizer = new MultiDirectional();
 private final RealPointValuePair[] simplex = new RealPointValuePair[MultiDirectionalDefaultSimplex.SIZE + 1];
 private final PointValuePairComparator comparator = new PointValuePairComparator();

 @Test
 public void testIterateSimplex() throws MathOptimizationException {
 final Function f = new FunctionWithConvergenceCheck(); // Assuming you have a Function with ConvergenceCheck

 optimizer.setMaxEvaluations(10);
 optimizer.setConvergenceChecker(new RealConvergenceChecker(1e-6, 1e-6, 1000));
 optimizer.optimize(f, null, simplex);

 // Check best point
 PointValuePair best = f.getValue();
 RealPointValuePair expectedBest = new RealPointValuePair(best.getPoint(), best.getValue(), false);
 Assert.assertEquals(expectedBest, simplex[0]);

 // Check convergence
 Assert.assertTrue(optimizer.isConverged());
 }

 @Test
 public void testIterateSimplex_IllegalArgumentException() throws MathOptimizationException {
 optimizer.setMaxEvaluations(-1);

 try {
 optimizer.optimize(new FunctionWithNoArgument(), null, simplex);
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testIterateSimplex_FunctionEvaluationException() throws MathOptimizationException {
 optimizer.setMaxEvaluations(2);

 try {
 optimizer.optimize(new FunctionWithThrowsFunctionEvaluationException(), null, simplex);
 } catch (FunctionEvaluationException e) {
 // Expected
 }
 }

 @Test
 public void testIterateSimplex_OptimizationException() throws MathOptimizationException {
 optimizer.setMaxEvaluations(1);
 try {
 optimizer.optimize(new FunctionWithMaxEvaluationsExceeded(), null, simplex);
 } catch (OptimizationException e) {
 // Expected
 }
 }

 @Test
 public void testEvaluateNewSimplex_FunctionEvaluationException() throws MathOptimizationException {
 RealPointValuePair[] original = new RealPointValuePair[2];
 PointValuePair[] points = {new PointValuePair(new double[]{0.0, 0.0}, 0.0), new PointValuePair(new double[]{1.0, 1.0}, 1.0)};
 for (int i = 0; i < original.length; i++) {
 simplex[i] = new RealPointValuePair(points[i].getPoint(), Double.NaN, false);
 original[i] = simplex[i];
 }
 optimizer.setMaxEvaluations(1);

 try {
 optimizer.evaluateNewSimplex(original, 2.0, comparator);
 } catch (FunctionEvaluationException e) {
 // Expected
 }
 }

 @Test
 public void testEvaluateNewSimplex_OptimizationException() throws MathOptimizationException {
 RealPointValuePair[] original = new RealPointValuePair[2];
 PointValuePair[] points = {new PointValuePair(new double[]{0.0, 0.0}, 0.0), new PointValuePair(new double[]{1.0, 1.0}, 1.0)};
 for (int i = 0; i < original.length; i++) {
 simplex[i] = new RealPointValuePair(points[i].getPoint(), Double.NaN, false);
 original[i] = simplex[i];
 }
 optimizer.setMaxEvaluations(1);
 optimizer.setConvergenceChecker(new RealConvergenceChecker(1e-6, 1e-6, 1000));

 try {
 optimizer.evaluateNewSimplex(original, 3.0, comparator);
 } catch (OptimizationException e) {
 // Expected
 }
 }

}