import org.apache.commons.math3.util.ContinuedFraction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ContinuedFractionTest {

 @Test
 public void testEvaluate_DefaultParameters() {
 ContinuedFraction continuedFraction = new ContinuedFraction() {
 @Override
 protected double getA(int n, double x) {
 return n == 0 ? 1.0 : n % 2 == 0 ? 1.0 / (2.0 * n - 1.0) : 1.0 / (2.0 * n + 1.0);
 }

 @Override
 protected double getB(int n, double x) {
 return n;
 }
 };

 try {
 double result = continuedFraction.evaluate(Math.PI);
 assertEquals(Math.PI, result, 0.001);
 } catch (ConvergenceException e) {
 fail("Evaluation should have converged for PI");
 }
 }

 @Test
 public void testEvaluate_SmallEpsilon() {
 ContinuedFraction continuedFraction = new ContinuedFraction() {
 @Override
 protected double getA(int n, double x) {
 return n == 0 ? 1.0 : n % 2 == 0 ? 1.0 / (2.0 * n - 1.0) : 1.0 / (2.0 * n + 1.0);
 }

 @Override
 protected double getB(int n, double x) {
 return n;
 }
 };

 try {
 double result = continuedFraction.evaluate(Math.E, 1e-12);
 assertEquals(Math.E, result, 1e-12);
 } catch (ConvergenceException e) {
 fail("Evaluation should have converged for E");
 }
 }

 @Test
 public void testEvaluate_LargeMaxIterations() {
 ContinuedFraction continuedFraction = new ContinuedFraction() {
 @Override
 protected double getA(int n, double x) {
 return Math.pow(2.0, n);
 }

 @Override
 protected double getB(int n, double x) {
 return Math.sqrt(2.0);
 }
 };

 try {
 double result = continuedFraction.evaluate(0.5, 100000);
 assertEquals(Math.sqrt(2.0), result, 1e-6);
 } catch (ConvergenceException e) {
 fail("Evaluation should have converged for given maxIterations");
 } catch (MaxCountExceededException e) {
 fail("Max count should not have been exceeded");
 }
 }

 @Test
 public void testEvaluate_InvalidInput() {
 ContinuedFraction continuedFraction = new ContinuedFraction() {
 @Override
 protected double getA(int n, double x) {
 throw new UnsupportedOperationException();
 }

 @Override
 protected double getB(int n, double x) {
 throw new UnsupportedOperationException();
 }
 };

 try {
 continuedFraction.evaluate(0.0);
 fail("Should have thrown an exception for unsupported operation");
 } catch (UnsupportedOperationException e) {
 // expected
 }
 }
}