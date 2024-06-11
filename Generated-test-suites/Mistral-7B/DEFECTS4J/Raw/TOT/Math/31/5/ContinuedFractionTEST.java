import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.ContinuedFraction;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class ContinuedFractionTest {

 private static final double EPSILON = 1e-9;
 private static final int MAX_ITERATIONS = 100;

 @Test
 public void testEvaluateSimpleContinuedFraction() {
 ContinuedFraction continuedFraction = new MySimpleContinuedFraction();
 double result = continuedFraction.evaluate(1.0);
 assertEquals(Math.PI / 4.0, result, EPSILON);
 }

 @Test
 public void testEvaluateConvergentContinuedFraction() {
 ContinuedFraction continuedFraction = new MyConvergentContinuedFraction();
 double result = continuedFraction.evaluate(Math.E);
 assertEquals(Math.E, result, EPSILON);
 }

 @Test(expected = ConvergenceException.class)
 public void testEvaluateDivergentContinuedFraction() {
 ContinuedFraction continuedFraction = new MyDivergentContinuedFraction();
 continuedFraction.evaluate(0.0);
 }

 @Test(expected = MaxCountExceededException.class)
 public void testEvaluateNonConvergentContinuedFraction() {
 ContinuedFraction continuedFraction = new MyNonConvergentContinuedFraction();
 continuedFraction.evaluate(1.0, EPSILON, MAX_ITERATIONS);
 }

 @Test
 public void testEvaluateWithCustomEpsilonAndMaxIterations() {
 ContinuedFraction continuedFraction = new MyCustomContinuedFraction();
 double result = continuedFraction.evaluate(1.0, 1e-12, 50);
 assertEquals(Math.sqrt(2.0), result, 1e-12);
 }
}

class MySimpleContinuedFraction extends ContinuedFraction {
 @Override
 protected double getA(int n, double x) {
 return 1.0 / (2.0 * n + 1.0);
 }

 @Override
 protected double getB(int n, double x) {
 return 1.0;
 }
}

class MyConvergentContinuedFraction extends ContinuedFraction {
 @Override
 protected double getA(int n, double x) {
 return 1.0 / (2.0 * n + 1.0);
 }

 @Override
 protected double getB(int n, double x) {
 return n;
 }
}

class MyDivergentContinuedFraction extends ContinuedFraction {
 @Override
 protected double getA(int n, double x) {
 return n;
 }

 @Override
 protected double getB(int n, double x) {
 return -n;
 }
}

class MyNonConvergentContinuedFraction extends ContinuedFraction {
 @Override
 protected double getA(int n, double x) {
 return Math.sin(n) / (2.0 * n + 1.0);
 }

 @Override
 protected double getB(int n, double x) {
 return Math.cos(n);
 }
}

class MyCustomContinuedFraction extends ContinuedFraction {
 @Override
 protected double getA(int n, double x) {
 return Math.sqrt(x + 1.0) / (2.0 * n + 1.0);
 }

 @Override
 protected double getB(int n, double x) {
 return (n + 1.0) / (2.0 * n + 1.0);
 }
}