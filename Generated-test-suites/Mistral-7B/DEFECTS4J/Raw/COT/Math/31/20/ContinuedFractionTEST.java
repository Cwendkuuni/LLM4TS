import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class ContinuedFractionTest {

 private ContinuedFraction continuedFraction = new ContinuedFraction() {
 @Override
 protected double getA(int n, double x) {
 return n > 0 ? (double) (n - 1) / n : 1.0;
 }

 @Override
 protected double getB(int n, double x) {
 return 1.0;
 }
 };

 @Test
 public void testEvaluate() {
 // Test evaluate with default parameters
 assertEquals(0.25, continuedFraction.evaluate(1.0), 1e-8);

 // Test evaluate with custom epsilon
 assertEquals(0.25, continuedFraction.evaluate(1.0, 1e-9), 1e-9);

 // Test evaluate with custom maxIterations
 assertEquals(0.25, continuedFraction.evaluate(1.0, 1e-8, 10), 1e-8);
 }

 @Test(expected = ConvergenceException.class)
 public void testConvergenceException() {
 continuedFraction = new ContinuedFraction() {
 @Override
 protected double getA(int n, double x) {
 return n > 0 ? (double) Math.pow(n, 2) : 1.0;
 }

 @Override
 protected double getB(int n, double x) {
 return 1.0;
 }
 };
 continuedFraction.evaluate(1.0, 1e-10, 100);
 }

 @Test(expected = MaxCountExceededException.class)
 public void testMaxCountExceededException() {
 continuedFraction = new ContinuedFraction() {
 @Override
 protected double getA(int n, double x) {
 return 1.0;
 }

 @Override
 protected double getB(int n, double x) {
 return n > 0 ? (double) (n - 1) / n : 0.0;
 }
 };
 continuedFraction.evaluate(1.0, 1e-9, 1);
 }

 @Test
 public void testZeroResult() {
 continuedFraction = new ContinuedFraction() {
 @Override
 protected double getA(int n, double x) {
 return 0.0;
 }

 @Override
 protected double getB(int n, double x) {
 return 1.0;
 }
 };
 assertEquals(0.0, continuedFraction.evaluate(1.0), 1e-8);
 }

 @Test
 public void testNegativeValues() {
 continuedFraction = new ContinuedFraction() {
 @Override
 protected double getA(int n, double x) {
 return n > 0 ? (double) (n - 1) / n : -1.0;
 }

 @Override
 protected double getB(int n, double x) {
 return 1.0;
 }
 };
 assertTrue(Double.isNaN(continuedFraction.evaluate(-1.0, 1e-8, 100)));
 }

 @Test
 public void testInfiniteValues() {
 continuedFraction = new ContinuedFraction() {
 @Override
 protected double getA(int n, double x) {
 return n > 0 ? (double) Math.pow(n, 2) : Double.POSITIVE_INFINITY;
 }

 @Override
 protected double getB(int n, double x) {
 return 1.0;
 }
 };
 assertFalse(continuedFraction.evaluate(1.0, 1e-8, 100) < Double.POSITIVE_INFINITY);
 }

}