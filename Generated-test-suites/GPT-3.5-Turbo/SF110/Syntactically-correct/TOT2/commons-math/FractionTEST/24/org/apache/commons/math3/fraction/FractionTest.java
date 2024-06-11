package org.apache.commons.math3.fraction;
import org.junit.Test;
import static org.junit.Assert.*;

public class FractionTest {

    @Test
    public void testAbs() {
        Fraction fraction = new Fraction(-3, 4);
        Fraction absFraction = fraction.abs();
        assertEquals(new Fraction(3, 4), absFraction);
    }

    @Test
    public void testAdd() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 4);
        Fraction result = fraction1.add(fraction2);
        assertEquals(new Fraction(3, 4), result);
    }

    @Test
    public void testDivide() {
        Fraction fraction1 = new Fraction(3, 5);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction result = fraction1.divide(fraction2);
        assertEquals(new Fraction(6, 5), result);
    }

    // More test cases can be added for other methods

}