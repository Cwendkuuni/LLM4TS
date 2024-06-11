import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ContinuedFractionTest {

 private ContinuedFraction cf = new ContinuedFraction() {
 @Override
 protected double getA(int n, double x) {
 return n == 0 ? x : (n % 2 == 0 ? 1.0 : -1.0);
 }

 @Override
 protected double getB(int n, double x) {
 return n;
 }
 };

 @Test
 public void testEvaluateWithDefault() {
 double result = cf.evaluate(0.5);
 double expected = 2.0;
 assertEquals(expected, result, 1e-6);
 }

 @Test
 public void testEvaluateWithEpsilon() {
 double result = cf.evaluate(0.5, 1e-8);
 double expected = 2.0;
 assertEquals(expected, result, 1e-8);
 }

 @Test
 public void testEvaluateWithMaxIterations() {
 double result = cf.evaluate(0.5, 5);
 double expected = 2.0;
 assertEquals(expected, result, 1e-6);
 }

 @Test(expected = ConvergenceException.class)
 public void testConvergenceException() {
 double result = cf.evaluate(0.5, 0); ;
 }

 @Test(expected = MaxCountExceededException.class)
 public void testMaxCountExceededException() {
 double result = cf.evaluate(0.5, 1);
 }

 @Test
 public void testInfiniteDivergence() {
 try {
 cf.evaluate(0.0, 1e-9, 1);
 fail("Should have thrown an ConvergenceException");
 } catch (ConvergenceException e) {
 // Expected
 }
 }

 @Test
 public void testNaN() {
 try {
 cf.evaluate(Double.NaN, 1e-9, 1);
 fail("Should have thrown an ConvergenceException");
 } catch (ConvergenceException e) {
 // Expected
 }
 }

 @Test
 public void testEvaluateWithNegativeX() {
 double result = cf.evaluate(-0.5, 1e-8);
 double expected = -2.0;
 assertEquals(expected, result, 1e-8);
 }

 @Test
 public void testEvaluateWithZeroX() {
 double result = cf.evaluate(0.0, 1e-8);
 double expected = 1.0;
 assertEquals(expected, result, 1e-8);
 }

 @Test
 public void testEvaluateWithLargePositiveX() {
 double result = cf.evaluate(1e9, 1e-8);
 double expected = 355.0 / 113.0;
 assertEquals(expected, result, 1e-8);
 }

 @Test
 public void testEvaluateWithLargeNegativeX() {
 double result = cf.evaluate(-1e9, 1e-8);
 double expected = -355.0 / 113.0;
 assertEquals(expected, result, 1e-8);
 }

}