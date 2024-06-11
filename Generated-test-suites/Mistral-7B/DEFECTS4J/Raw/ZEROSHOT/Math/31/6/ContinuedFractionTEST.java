import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class ContinuedFractionTest {

 private static class TestContinuedFraction extends ContinuedFraction {
  @Override
  protected double getA(int n, double x) {
   switch (n) {
    case 0:
     return 1.0;
    case 1:
     return x;
    default:
     return -1.0 / (x + 2 * n);
   }
  }

  @Override
  protected double getB(int n, double x) {
   return 1.0;
  }
 }

 @Test
 public void testSquareRoot() {
  ContinuedFraction cf = new TestContinuedFraction();
  for (double x = 0.0; x <= 10.0; x += 1.0) {
   double trueValue = Math.sqrt(x);
   double approximatedValue = cf.evaluate(x);
   assertEquals("Error in square root calculation for x = " + x, trueValue, approximatedValue, 1e-6);
  }
 }

 @Test(expected = ConvergenceException.class)
 public void testNonConvergentExample() {
  ContinuedFraction cf = new TestContinuedFraction();
  double x = 0.0;
  try {
   cf.evaluate(x, 1e-15, 1000);
  } catch (MaxCountExceededException e) {
   fail("Expected ConvergenceException not MaxCountExceededException");
  }
 }

 @Test(expected = ConvergenceException.class)
 public void testInfinityDivergenceExample() {
  ContinuedFraction cf = new TestContinuedFraction();
  double x = -1.0;
  try {
   cf.evaluate(x, 1e-15, 1000);
  } catch (MaxCountExceededException e) {
   fail("Expected ConvergenceException not MaxCountExceededException");
  }
 }

 @Test
 public void testGoldenRatio() {
  ContinuedFraction cf = new TestContinuedFractionGoldenRatio();
  double x = 0.0;
  double approximatedValue = cf.evaluate(x);
  double trueValue = (1.0 + Math.sqrt(5.0)) / 2.0;
  assertEquals("Error in golden ratio calculation", trueValue, approximatedValue, 1e-14);
 }

 @Test
 public void testPi() {
  ContinuedFraction cf = new TestContinuedFractionPi();
  double x = 0.0;
  double approximatedValue = cf.evaluate(x);
  double trueValue = Math.PI;
  assertEquals("Error in pi calculation", trueValue, approximatedValue, 1e-14);
 }

 @Test
 public void testEulerNumber() {
  ContinuedFraction cf = new TestContinuedFractionEulerNumber();
  double x = 0.0;
  double approximatedValue = cf.evaluate(x);
  double trueValue = Math.E;
  assertEquals("Error in euler number calculation", trueValue, approximatedValue, 1e-14);
 }

 private static class TestContinuedFractionGoldenRatio extends ContinuedFraction {
  @Override
  protected double getA(int n, double x) {
   switch (n) {
    case 0:
     return 1.0;
    case 1:
     return (x + 1.0) / (x + 2.0);
    default:
     return -1.0 / (x + 2 * n);
   }
  }

  @Override
  protected double getB(int n, double x) {
   return (x + 1.0) / (x + 2.0);
  }
 }

 private static class TestContinuedFractionPi extends ContinuedFraction {
  @Override
  protected double getA(int n, double x) {
   switch (n) {
    case 0:
     return 1.0;
    case 1:
     return x;
    case 2:
     return 1.0 / 4.0;
    default:
     return -1.0 / (2.0 * n + 1.0);
   }
  }

  @Override
  protected double getB(int n, double x) {
   switch (n) {
    case 0:
     return 1.0;
    case 1:
     return x;
    default:
     return 2.0 * n;
   }
  }
 }

 private static class TestContinuedFractionEulerNumber extends ContinuedFraction {
  @Override
  protected double getA(int n, double x) {
   switch (n) {
    case 0:
     return 0.0;
    case 1:
     return 1.0 / (1.0 + x);
    default:
     return -1.0 / (1.0 + 2 * n);
   }
  }

  @Override
  protected double getB(int n, double x) {
   switch (n) {
    case 0:
     return 1.0;
    case 1:
     return 1.0 / (1.0 + x);
    default:
     return 1.0 + 2 * n;
   }
  }
 }

}