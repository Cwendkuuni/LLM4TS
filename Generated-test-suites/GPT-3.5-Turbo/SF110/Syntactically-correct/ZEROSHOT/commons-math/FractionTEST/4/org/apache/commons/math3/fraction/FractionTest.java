package org.apache.commons.math3.fraction;
import static org.junit.Assert.*;
import org.junit.Test;

public class FractionTest {

    @Test
    public void testConstructor() {
        Fraction fraction = new Fraction(5);
        assertEquals(5, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());

        fraction = new Fraction(3, 4);
        assertEquals(3, fraction.getNumerator());
        assertEquals(4, fraction.getDenominator());
    }

    @Test(expected = MathArithmeticException.class)
    public void testConstructorWithZeroDenominator() {
        new Fraction(5, 0);
    }

    @Test
    public void testEquals() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(6, 8);
        assertEquals(fraction1, fraction2);

        fraction1 = new Fraction(3, 5);
        fraction2 = new Fraction(2, 3);
        assertNotEquals(fraction1, fraction2);
    }

    @Test
    public void testAdd() {
        Fraction fraction1 = new Fraction(1, 2);
        Fraction fraction2 = new Fraction(1, 4);
        Fraction result = fraction1.add(fraction2);
        assertEquals(new Fraction(3, 4), result);

        fraction1 = new Fraction(3, 4);
        fraction2 = new Fraction(1, 2);
        result = fraction1.add(fraction2);
        assertEquals(new Fraction(5, 4), result);

        fraction1 = new Fraction(3, 4);
        int num = 2;
        result = fraction1.add(num);
        assertEquals(new Fraction(11, 4), result);
    }

    @Test
    public void testSubtract() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(1, 4);
        Fraction result = fraction1.subtract(fraction2);
        assertEquals(new Fraction(1, 2), result);

        fraction1 = new Fraction(3, 4);
        fraction2 = new Fraction(1, 2);
        result = fraction1.subtract(fraction2);
        assertEquals(new Fraction(1, 4), result);

        fraction1 = new Fraction(3, 4);
        int num = 2;
        result = fraction1.subtract(num);
        assertEquals(new Fraction(-5, 4), result);
    }

    @Test
    public void testMultiply() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction result = fraction1.multiply(fraction2);
        assertEquals(new Fraction(3, 8), result);

        fraction1 = new Fraction(3, 4);
        int num = 2;
        result = fraction1.multiply(num);
        assertEquals(new Fraction(3, 2), result);
    }

    @Test
    public void testDivide() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(1, 2);
        Fraction result = fraction1.divide(fraction2);
        assertEquals(new Fraction(3, 2), result);

        fraction1 = new Fraction(3, 4);
        int num = 2;
        result = fraction1.divide(num);
        assertEquals(new Fraction(3, 8), result);
    }

    @Test(expected = MathArithmeticException.class)
    public void testDivideByZero() {
        Fraction fraction1 = new Fraction(3, 4);
        Fraction fraction2 = new Fraction(0, 1);
        fraction1.divide(fraction2);
    }

    @Test
    public void testToString() {
        Fraction fraction1 = new Fraction(3, 4);
        assertEquals("3 / 4", fraction1.toString());

        Fraction fraction2 = new Fraction(5);
        assertEquals("5", fraction2.toString());
    }
}