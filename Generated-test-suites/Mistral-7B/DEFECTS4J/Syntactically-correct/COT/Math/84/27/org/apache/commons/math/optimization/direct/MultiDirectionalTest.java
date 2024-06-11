import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.UnboundedDomainException;
import org.apache.commons.math3.optim.nonlinear.math.OpenInterval;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MultiDirectionalTest {

 private MultiDirectional optimizer;
 private double[] xSmallest;
 private double[] xReflected;
 private double[] xContracted;
 private RealPointValuePair bestPoint;

 @SuppressWarnings("deprecation")
 @Before
 public void setup() {
 this.optimizer = new MultiDirectional();
 this.xSmallest = new double[]{1.0, 2.0};
 this.bestPoint = new RealPointValuePair(this.xSmallest, 3.0);
 }

 @Test
 public void testMultiDirectionalConstructor() {
 MultiDirectional optimizer = new MultiDirectional(1.5, 0.7);
 assertNotNull(optimizer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMultiDirectionalNegativeKhi() {
 MultiDirectional optimizer = new MultiDirectional(-1.5, 0.7);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMultiDirectionalNegativeGamma() {
 MultiDirectional optimizer = new MultiDirectional(1.5, -0.7);
 }

 @Test
 public void testIterateSimplex() throws OptimizationException, FunctionEvaluationException {
 // test reflection
 this.xReflected = new double[]{1.5, 2.5};
 optimizer.iterateSimplex(new SimpleValueChecker(new OpenInterval(0.0, Double.POSITIVE_INFINITY), new Double[]{}));
 PointValuePair reflected = optimizer.getBestPoint();
 assertNotNull(reflected);
 assertFalse(reflected.isFeasible());
 assertTrue(optimizer.hasConverged());
 assertEquals(this.xReflected, reflected.getPointRef(), 0.001);
 // test expansion
 optimizer.reset();
 optimizer.addPoint(new RealPointValuePair(this.xSmallest, 3.0));
 optimizer.iterateSimplex(new SimpleValueChecker(new OpenInterval(0.0, Double.POSITIVE_INFINITY), new Double[]{}));
 PointValuePair expanded = optimizer.getBestPoint();
 assertNotNull(expanded);
 assertFalse(expanded.isFeasible());
 assertTrue(optimizer.hasConverged());
 assertEquals(this.bestPoint, expanded, 0.001);
}

 @Test
 public void testEvaluateNewSimplexWithReflected() throws OptimizationException, FunctionEvaluationException {
 RealPointValuePair original[] = new RealPointValuePair[]{
 new RealPointValuePair(this.xSmallest, 3.0),
 new RealPointValuePair(this.xReflected, Double.NaN)
 };
 RealPointValuePair reflected = optimizer.evaluateNewSimplex(original, 1.0, new SimpleValueChecker(new OpenInterval(0.0, Double.POSITIVE_INFINITY), new Double[]{}));
 assertEquals(this.xReflected, reflected.getPointRef(), 0.001);
 }

 @Test(expected = UnboundedDomainException.class)
 public void testEvaluateNewSimplexWithNegativeCoef() throws OptimizationException, FunctionEvaluationException {
 RealPointValuePair original[] = new RealPointValuePair[]{
 new RealPointValuePair(this.xSmallest, 3.0),
 new RealPointValuePair(this.xReflected, Double.NaN)
 };
 optimizer.evaluateNewSimplex(original, -1.0, new SimpleValueChecker(new OpenInterval(0.0, Double.POSITIVE_INFINITY), new Double[]{}));
 }

 @Test
 public void testEvaluateNewSimplexWithContracted() throws OptimizationException, FunctionEvaluationException {
 RealPointValuePair original[] = new RealPointValuePair[]{
 new RealPointValuePair(this.xSmallest, 3.0),
 new RealPointValuePair(this.xContracted, Double.NaN)
 };
 RealPointValuePair contracted = optimizer.evaluateNewSimplex(original, 0.5, new SimpleValueChecker(new OpenInterval(0.0, Double.POSITIVE_INFINITY), new Double[]{}));
 assertEquals(this.xContracted, contracted.getPointRef(), 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEvaluateNewSimplexWithInvalidComparator() throws OptimizationException, FunctionEvaluationException {
 optimizer.evaluateNewSimplex(new Object[]{}, 1.0, null);
 }

 @Test
 public void testGetBestPoint() {
 PointValuePair bestPoint = optimizer.getBestPoint();
 assertNull(bestPoint);
 }

 @Test
 public void testGetIterationCount() {
 int count = optimizer.getIterationCount();
 assertEquals(0, count);
 }

 @Test
 public void testHasConverged() {
 assertFalse(optimizer.hasConverged());
 }

 @Test
 public void testReset() {
 optimizer.reset();
 assertFalse(optimizer.hasConverged());
 }

 @Test
 public void testAddPoint() {
 optimizer.addPoint(new RealPointValuePair(new double[]{1.0, 2.0}, 3.0));
 assertFalse(optimizer.hasConverged());
 }

 @Test
 public void testAddPointWithNull() {
 optimizer.addPoint(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddPointWithInvalidPoint() {
 optimizer.addPoint(new RealPointValuePair(null, 3.0));
 }

}