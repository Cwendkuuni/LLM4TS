package org.apache.commons.math.special.GammaTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNaN;

import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.junit.Test;

public class GammaTest {

 @Test
 public void testLogGamma() {
 // Test valid inputs
 double input = 1.5;
 double expected = 0.4058327636606422;
 double result = Gamma.logGamma(input);
 assertEquals(expected, result, 1e-10);

 input = 2.0;
 expected = 1.178097245115027;
 result = Gamma.logGamma(input);
 assertEquals(expected, result, 1e-10);

 // Test edge cases
 input = 0.0;
 result = Gamma.logGamma(input);
 assertNaN(result);

 input = -1.0;
 result = Gamma.logGamma(input);
 assertNaN(result);

 // Test invalid inputs
 input = Double.NaN;
 result = Gamma.logGamma(input);
 assertNaN(result);
 }

 @Test
 public void testRegularizedGammaP() throws MaxIterationsExceededException {
 // Test valid inputs
 double a = 1.5;
 double x = 1.5;
 double expected = 0.6871778527725932;
 double result = Gamma.regularizedGammaP(a, x);
 assertEquals(expected, result, 1e-10);

 a = 2.0;
 x = 2.0;
 expected = 0.9182883438197056;
 result = Gamma.regularizedGammaP(a, x);
 assertEquals(expected, result, 1e-10);

 // Test edge cases
 a = 0.0;
 x = 1.0;
 result = Gamma.regularizedGammaP(a, x);
 assertEquals(0.0, result, 1e-10);

 x = 0.0;
 result = Gamma.regularizedGammaP(a, x);
 assertEquals(1.0, result, 1e-10);

 a = 1.0;
 x = 1.0;
 result = Gamma.regularizedGammaP(a, x);
 assertEquals(1.0, result, 1e-10);

 a = 1.0;
 x = 2.0;
 result = Gamma.regularizedGammaP(a, x);
 assertEquals(1.0, result, 1e-10);

 // Test invalid inputs
 a = Double.NaN;
 result = Gamma.regularizedGammaP(a, x);
 assertNaN(result);

 x = Double.NaN;
 result = Gamma.regularizedGammaP(a, x);
 assertNaN(result);
 }

 @Test
 public void testRegularizedGammaQ() throws MaxIterationsExceededException {
 // Test valid inputs
 double a = 1.5;
 double x = 1.5;
 double expected = 0.3128221472274068;
 double result = Gamma.regularizedGammaQ(a, x);
 assertEquals(expected, result, 1e-10);

 a = 2.0;
 x = 2.0;
 expected = 0.08212293685264268;
 result = Gamma.regularizedGammaQ(a, x);
 assertEquals(expected, result, 1e-10);

 // Test edge cases
 a = 0.0;
 x = 1.0;
 result = Gamma.regularizedGammaQ(a, x);
 assertEquals(1.0, result, 1e-10);

 x = 0.0;
 result = Gamma.regularizedGammaQ(a, x);
 assertEquals(1.0, result, 1e-10);

 a = 1.0;
 x = 1.0;
 result = Gamma.regularizedGammaQ(a, x);
 assertEquals(0.0, result, 1e-10);

 a = 1.0;
 x = 2.0;
 result = Gamma.regularizedGammaQ(a, x);
 assertEquals(0.0, result, 1e-10);

 // Test invalid inputs
 a = Double.NaN;
 result = Gamma.regularizedGammaQ(a, x);
 assertNaN(result);

 x = Double.NaN;
 result = Gamma.regularizedGammaQ(a, x);
 assertNaN(result);
 }

}