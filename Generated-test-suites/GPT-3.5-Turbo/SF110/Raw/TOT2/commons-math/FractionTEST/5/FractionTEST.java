package org.apache.commons.math3.fraction;

import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {

    @Test
    public void testAbs() {
        Fraction fraction = new Fraction(-3, 5);
        Fraction absFraction = fraction.abs();
        assertEquals(absFraction, new Fraction(3, 5));
    }

    @Test
    public void testCompareTo() {
        Fraction fraction1 = new Fraction(2, 3);
        Fraction fraction2 = new Fraction(3, 4);
        int result = fraction1.compareTo(fraction2);
        assertTrue(result < 0);
    }

    @Test
    public void testDoubleValue() {
        Fraction fraction = new Fraction(5, 3);
        double result = fraction.doubleValue();
        assertEquals(result, 1.6666666666666667, 0.0001);
    }

    // Add more test cases for other methods...

}