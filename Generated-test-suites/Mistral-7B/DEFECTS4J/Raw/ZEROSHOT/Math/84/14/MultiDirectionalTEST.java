import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.RealPointValuePair;
import org.apache.commons.math3.optim.SimpleOptimizer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiDirectionalTest {

 private MultiDirectional optimizer;
 private DoubleFunction function;
 private Comparator<RealPointValuePair> comparator;
 private RealPointValuePair[] simplex;

 @Before
 public void setup() {
 function = new DoubleFunction() {
 @Override
 public double value(double x) {
 return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
 }

 @Override
 public double value(DoublePoint value) {
 return value.getPoint()[0];
 }
 };
 comparator = new Comparator<RealPointValuePair>() {
 @Override
 public int compare(RealPointValuePair o1, RealPointValuePair o2) {
 return Double.compare(o1.getValue(), o2.getValue());
 }
 };
 optimizer = new MultiDirectional(2.0, 0.5);
 }

 @Test
 public void testIterateSimplex() throws Exception {
 // test default constructor
 optimizer = new MultiDirectional();
 optimizer.setFunction(function);
 optimizer.optimize();
 RealPointValuePair best = optimizer.getBestPoint();
 assertEquals(Math.PI, best.getPoint()[0], 0.001);

 // test constructor with specified coefficients
 optimizer = new MultiDirectional(3.0, 0.7);
 optimizer.optimize();
 best = optimizer.getBestPoint();
 assertEquals(Math.PI * Math.sqrt(2.0 / 3.0), best.getPoint()[0], 0.001);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIterateSimplexWithNullComparator() {
 optimizer.setFunction(function);
 optimizer.setComparator(null);
 optimizer.optimize();
 }

 @Test(expected = OptimizationException.class)
 public void testIterateSimplexWithMaxIterationsReached() throws Exception {
 SimpleOptimizer optim = new SimpleOptimizer() {
 @Override
 public void optimize() throws Exception {
 throw new OptimizationException("Maximum number of evaluations reached");
 }
 };
 optimizer.setFunction(function);
 optimizer.setMaxIterations(1);
 optimizer.optimize();
 optimizer.setComparator(comparator);
 optimizer.iterateSimplex(comparator);
 }

 @Test(expected = FunctionEvaluationException.class)
 public void testIterateSimplexWithFunctionEvaluationException() throws Exception {
 DoublePoint point = new DoublePoint(Double.NaN);
 function.setValue(point, Double.NaN);
 optimizer.setFunction(function);
 optimizer.setComparator(comparator);
 optimizer.iterateSimplex(comparator);
 }

 @Test
 public void testEvaluateNewSimplex() throws Exception {
 RealPointValuePair[] original = new RealPointValuePair[5];
 for (int i = 0; i < 5; i++) {
 original[i] = new RealPointValuePair(new DoublePoint(i), i, false);
 }
 optimizer.setFunction(function);
 optimizer.setComparator(comparator);
 optimizer.iterateSimplex(comparator);
 RealPointValuePair reflected = optimizer.evaluateNewSimplex(original, 1.0, comparator);
 RealPointValuePair reflectedSimplex = new RealPointValuePair[5];
 System.arraycopy(optimizer.simplex, 1, reflectedSimplex, 0, 5);
 Reflection reflection = new Reflection(optimizer.khi, comparator);
 assertEquals(reflection.getReflectedPoint(original[1], reflected), reflectedSimplex[1]);
 reflected = optimizer.evaluateNewSimplex(original, optimizer.khi, comparator);
 assertEquals(reflection.getReflectedPoint(original[1], reflected), optimizer.simplex[1]);
 }

}