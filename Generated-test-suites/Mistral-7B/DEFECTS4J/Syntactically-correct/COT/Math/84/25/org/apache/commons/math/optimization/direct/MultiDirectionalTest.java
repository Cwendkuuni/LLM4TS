import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleOptimizationTest;
import org.apache.commons.math3.optim.direct.MultiDirectional;
import org.apache.commons.math3.optim.DirectSearchOptimizer;
import org.apache.commons.math3.optim.PointValueFunction;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class MultiDirectionalTest extends SimpleOptimizationTest {

 private MultiDirectional optimizer;
 private PointValueFunction testFunction;

 @Override
 public void setUp() {
 super.setUp();
 optimizer = new MultiDirectional();
 testFunction = new PointValueFunction() {
 @Override
 public double value(double[] x) {
 return Math.pow(Math.sin(Math.PI * x[0]), 2) + Math.pow(Math.sin(Math.PI * x[1]), 2);
 }
 };
 }

 /**
 * Test constructor with default coefficients
 */
 @Test
 public void testDefaultConstructor() {
 optimizer = new MultiDirectional();
 }

 /**
 * Test constructor with specified coefficients
 */
 @Test
 public void testCustomConstructor() {
 optimizer = new MultiDirectional(3.0, 0.8);
 }

 /**
 * Test iterateSimplex method with reflection
 */
 @Test
 public void testReflection() throws Exception {
 RealPointValuePair[] original = new RealPointValuePair[] {
 new RealPointValuePair(new double[] {-1.0, -1.0}, 4.0),
 new RealPointValuePair(new double[] {1.0, -1.0}, 8.0),
 new RealPointValuePair(new double[] {-1.0, 1.0}, 5.0),
 new RealPointValuePair(new double[] {1.0, 1.0}, 3.0),
 new RealPointValuePair(new double[] {0.0, 0.0}, Double.NaN, false)
 };
 optimizer.iterateSimplex(new Comparator<RealPointValuePair>() {
 @Override
 public int compare(RealPointValuePair p1, RealPointValuePair p2) {
 return (int) (Double.doubleToLongBits(p1.getValue()) < Double.doubleToLongBits(p2.getValue()) ? -1 : 1);
 }
 });
 PointValuePair best = original[0];
 optimizer.iterateSimplex(new Comparator<RealPointValuePair>() {
 @Override
 public int compare(RealPointValuePair p1, RealPointValuePair p2) {
 return (int) (Double.doubleToLongBits(p1.getValue()) < Double.doubleToLongBits(p2.getValue()) ? -1 : 1);
 }
 });
 RealPointValuePair reflected = (RealPointValuePair) optimizer.simplex[1];
 assertEquals(reflected, new RealPointValuePair(new double[] {-1.0, -1.0}, 4.0));
}

 /**
 * Test iterateSimplex method with reflection and expansion
 */
 @Test
 public void testReflectionAndExpansion() throws Exception {
 RealPointValuePair[] original = new RealPointValuePair[] {
 new RealPointValuePair(new double[] {-1.0, -1.0}, 4.0),
 new RealPointValuePair(new double[] {1.0, -1.0}, 8.0),
 new RealPointValuePair(new double[] {-1.0, 1.0}, 5.0),
 new RealPointValuePair(new double[] {1.0, 1.0}, 3.0),
 new RealPointValuePair(new double[] {0.0, 0.0}, Double.NaN, false)
 };
 optimizer.iterateSimplex(new Comparator<RealPointValuePair>() {
 @Override
 public int compare(RealPointValuePair p1, RealPointValuePair p2) {
 return (int) (Double.doubleToLongBits(p1.getValue()) < Double.doubleToLongBits(p2.getValue()) ? -1 : 1);
 }
 });
 PointValuePair best = original[0];
 optimizer.iterateSimplex(new Comparator<RealPointValuePair>() {
 @Override
 public int compare(RealPointValuePair p1, RealPointValuePair p2) {
 return (int) (Double.doubleToLongBits(p1.getValue()) < Double.doubleToLongBits(p2.getValue()) ? -1 : 1);
 }
 });
 RealPointValuePair reflected = (RealPointValuePair) optimizer.simplex[1];
 optimizer.iterateSimplex(new Comparator<RealPointValuePair>() {
 @Override
 public int compare(RealPointValuePair p1, RealPointValuePair p2) {
 return (int) (Double.doubleToLongBits(p1.getValue()) < Double.doubleToLongBits(p2.getValue()) ? -1 : 1);
 }
 });
 RealPointValuePair expanded = (RealPointValuePair) optimizer.simplex[2];
 assertEquals(expanded, new RealPointValuePair(new double[] {-3.4, -3.4}, Double.NaN));
}

 /**
 * Test iterateSimplex method with reflection, expansion, and contraction
 */
 @Test
 public void testReflectionExpansionContraction() throws Exception {
 RealPointValuePair[] original = new RealPointValuePair[] {
 new RealPointValuePair(new double[] {-1.0, -1.0}, 4.0),
 new RealPointValuePair(new double[] {1.0, -1.0}, 8.0),
 new RealPointValuePair(new double[] {-1.0, 1.0}, 5.0),
 new RealPointValuePair(new double[] {1.0, 1.0}, 3.0),
 new RealPointValuePair(new double[] {0.0, 0.0}, Double.NaN, false)
 };
 optimizer.iterateSimplex(new Comparator<RealPointValuePair>() {
 @Override
 public int compare(RealPointValuePair p1, RealPointValuePair p2) {
 return (int) (Double.doubleToLongBits(p1.getValue()) < Double.doubleToLongBits(p2.getValue()) ? -1 : 1);
 }
 });
 PointValuePair best = original[0];
 optimizer.iterateSimplex(new Comparator<RealPointValuePair>() {
 @Override
 public int compare(RealPointValuePair p1, RealPointValuePair p2) {
 return (int) (Double.doubleToLongBits(p1.getValue()) < Double.doubleToLongBits(p2.getValue()) ? -1 : 1);
 }
 });
 RealPointValuePair reflected = (RealPointValuePair) optimizer.simplex[1];
 optimizer.iterateSimplex(new Comparator<RealPointValuePair>() {
 @Override
 public int compare(RealPointValuePair p1, RealPointValuePair p2) {
 return (int) (Double.doubleToLongBits(p1.getValue()) < Double.doubleToLongBits(p2.getValue()) ? -1 : 1);
 }
 });
 RealPointValuePair contracted = (RealPointValuePair) optimizer.simplex[3];
 assertEquals(contracted, new RealPointValuePair(new double[] {-1.0, -1.0}, 4.0));
}

 /**
 * Test iterateSimplex method with reflection, expansion, and contraction, and convergence
 */
 @Test
 public void testReflectionExpansionContractionAndConvergence() throws Exception {
 RealPointValuePair[] original = new RealPointValuePair[] {
 new RealPointValuePair(new double[] {-1.0, -1.0}, 4.0),
 new RealPointValuePair(new double[] {1.0, -1.0}, 8.0),
 new RealPointValuePair(new double[] {-1.0, 1.0}, 5.0),
 new RealPointValuePair(new double[] {1.0, 1.0}, 3.0),
 new RealPointValuePair(new double[] {0.0, 0.0}, Double.NaN, false)
 };
 optimizer.iterateSimplex(new Comparator<RealPointValuePair>() {
 @Override
 public int compare(RealPointValuePair p1, RealPointValuePair p2) {
 return (int) (Double.doubleToLongBits(p1.getValue()) < Double.doubleToLongBits(p2.getValue()) ? -1 : 1);
 }
 });
 PointValuePair best = original[0];
 optimizer.iterateSimplex(new Comparator<RealPointValuePair>() {
 @Override
 public int compare(RealPointValuePair p1, RealPointValuePair p2) {
 return (int) (Double.doubleToLongBits(p1.getValue()) < Double.doubleToLongBits(p2.getValue()) ? -1 : 1);
 }
 });
 RealPointValuePair reflected = (RealPointValuePair) optimizer.simplex[1];
 optimizer.iterateSimplex(new Comparator<RealPointValuePair>() {
 @Override
 public int compare(RealPointValuePair p1, RealPointValuePair p2) {
 return (int) (Double.doubleToLongBits(p1.getValue()) < Double.doubleToLongBits(p2.getValue()) ? -1 : 1);
 }
 });
 RealPointValuePair contracted = (RealPointValuePair) optimizer.simplex[3];
 optimizer.iterateSimplex(new Comparator<RealPointValuePair>() {
 @Override
 public int compare(RealPointValuePair p1, RealPointValuePair p2) {
 return (int) (Double.doubleToLongBits(p1.getValue()) < Double.doubleToLongBits(p2.getValue()) ? -1 : 1);
 }
 });
 RealPointValuePair converged = (RealPointValuePair) optimizer.simplex[0];
 assertEquals(converged, new RealPointValuePair(new double[] {0.0, 0.0}, 0.0));
}

 /**
 * Test evaluateNewSimplex method
 */
 @Test
 public void testEvaluateNewSimplex() throws Exception {
 RealPointValuePair[] original = new RealPointValuePair[] {
 new RealPointValuePair(new double[] {-1.0, -1.0}, 4.0),
 new RealPointValuePair(new double[] {1.0, -1.0}, 8.0),
 new RealPointValuePair(new double[] {-1.0, 1.0}, 5.0),
 new RealPointValuePair(new double[] {1.0, 1.0}, 3.0),
 new RealPointValuePair(new double[] {0.0, 0.0}, Double.NaN, false)
 };
 optimizer.iterateSimplex(new Comparator<RealPointValuePair>() {
 @Override
 public int compare(RealPointValuePair p1, RealPointValuePair p2) {
 return (int) (Double.doubleToLongBits(p1.getValue()) < Double.doubleToLongBits(p2.getValue()) ? -1 : 1);
 }
 });
 PointValuePair best = original[0];
 RealPointValuePair evaluated = optimizer.evaluateNewSimplex(original, 1.5, new Comparator<RealPointValuePair>() {
 @Override
 public int compare(RealPointValuePair p1, RealPointValuePair p2) {
 return (int) (Double.doubleToLongBits(p1.getValue()) < Double.doubleToLongBits(p2.getValue()) ? -1 : 1);
 }
 });
 assertEquals(evaluated, new RealPointValuePair(new double[] {-0.3, -2.1}, Double.NaN));
}

 /**
 * Test evaluateNewSimplex method with FunctionEvaluationException
 */
 @Test
 public void testEvaluateNewSimplexException() {
 assertThrows(FunctionEvaluationException.class, () -> {
 optimizer.evaluateNewSimplex(new RealPointValuePair[] {
 new RealPointValuePair(new double[] {-1.0, -1.0}, 4.0),
 new RealPointValuePair(new double[] {1.0, -1.0}, 8.0),
 new RealPointValuePair(new double[] {-1.0, 1.0}, 5.0),
 new RealPointValuePair(new double[] {1.0, 1.0}, 3.0),
 new RealPointValuePair(new double[] {0.0, 0.0}, Double.NaN, false)
 }, 1.5, new Comparator<RealPointValuePair>() {
 @Override
 public int compare(RealPointValuePair p1, RealPointValuePair p2) {
 return (int) (Double.doubleToLongBits(p1.getValue()) < Double.doubleToLongBits(p2.getValue()) ? -1 : 1);
 }
 });
 });
}

 /**
 * Test evaluateNewSimplex method with OptimizationException
 */
 @Test
 public void testEvaluateNewSimplexMaxEvaluations() {
 optimizer.setMaxEvaluations(2);
 assertThrows(OptimizationException.class, () -> {
 optimizer.iterateSimplex(new Comparator<RealPointValuePair>() {
 @Override
 public int compare(RealPointValuePair p1, RealPointValuePair p2) {
 return (int) (Double.doubleToLongBits(p1.getValue()) < Double.doubleToLongBits(p2.getValue()) ? -1 : 1);
 }
 });
 });
}

}