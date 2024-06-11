import static org.apache.commons.math3.util.Precision.equals;
import static org.junit.Assert.*;

import org.apache.commons.math3.special.Gamma;

public class GammaTest {

 // Test for logGamma method
 @Test
 public void testLogGamma() {
  double result = Gamma.logGamma(1.5);
  double expected = 0.40582236612094535;
  assertEquals(expected, result, 1e-9);

  result = Gamma.logGamma(0.5);
  expected = -0.57721566490153286;
  assertEquals(expected, result, 1e-9);

  result = Gamma.logGamma(1.0);
  expected = 0.0;
  assertEquals(expected, result, 1e-9);

  result = Gamma.logGamma(-1.0);
  assertNaN(result);
 }

 // Test for regularizedGammaP method with positive a and x
 @Test
 public void testRegularizedGammaPPositiveArguments() {
  double result = Gamma.regularizedGammaP(1.5, 1.5);
  double expected = 0.71236437995827373;
  assertEquals(expected, result, 1e-9);

  result = Gamma.regularizedGammaP(2.5, 2.5);
  expected = 0.91893400793988336;
  assertEquals(expected, result, 1e-9);
 }

 // Test for regularizedGammaP method with negative a and positive x
 @Test
 public void testRegularizedGammaPNegativeAandPositiveX() {
  double result = Gamma.regularizedGammaP(-1.5, 1.5);
  double expected = 1.2027444985225137;
  assertEquals(expected, result, 1e-9);

  result = Gamma.regularizedGammaP(-2.5, 2.5);
  expected = 0.82254901639344233;
  assertEquals(expected, result, 1e-9);
 }

 // Test for regularizedGammaP method with positive a and negative x
 @Test(expected = MathException.class)
 public void testRegularizedGammaPPositiveAandNegativeX() {
  double result = Gamma.regularizedGammaP(1.5, -1.5);
 }

 // Test for regularizedGammaP method with a = 0
 @Test
 public void testRegularizedGammaPZeroA() {
  double result = Gamma.regularizedGammaP(0.5, 1.0);
  double expected = 1.0;
  assertEquals(expected, result, 1e-9);
 }

 // Test for regularizedGammaQ method with positive a and x
 @Test
 public void testRegularizedGammaQPositiveArguments() {
  double result = Gamma.regularizedGammaQ(1.5, 1.5);
  double expected = 0.28763562046172627;
  assertEquals(expected, result, 1e-9);

  result = Gamma.regularizedGammaQ(2.5, 2.5);
  expected = 0.08110255820528933;
  assertEquals(expected, result, 1e-9);
 }

 // Test for regularizedGammaQ method with negative a and positive x
 @Test
 public void testRegularizedGammaQNegativeAandPositiveX() {
  double result = Gamma.regularizedGammaQ(-1.5, 1.5);
  double expected = 1.2124510245310586;
  assertEquals(expected, result, 1e-9);

  result = Gamma.regularizedGammaQ(-2.5, 2.5);
  expected = 0.91828401331017832;
  assertEquals(expected, result, 1e-9);
 }

 // Test for regularizedGammaQ method with positive a and negative x
 @Test(expected = MathException.class)
 public void testRegularizedGammaQPositiveAandNegativeX() {
  double result = Gamma.regularizedGammaQ(1.5, -1.5);
 }

 // Test for regularizedGammaQ method with a = 1
 @Test
 public void testRegularizedGammaQOneA() {
  double result = Gamma.regularizedGammaQ(1.5, 1.0);
  double expected = 0.65211054533455315;
  assertEquals(expected, result, 1e-9);
 }

 // Test for regularizedGammaQ method with a = 0
 @Test
 public void testRegularizedGammaQZeroA() {
  double result = Gamma.regularizedGammaQ(0.5, 1.0);
  double expected = 1.0;
  assertEquals(expected, result, 1e-9);
 }
}