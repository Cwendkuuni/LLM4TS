package org.apache.commons.math.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ProperFractionFormatTest {
    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction f1 = new Fraction(3, 2);
        assertEquals("1 1/2", format.format(f1));
        Fraction f2 = new Fraction(-3, 2);
        assertEquals("-1 1/2", format.format(f2));
        Fraction f3 = new Fraction(0, 3);
        assertEquals("0", format.format(f3));
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        String s1 = "1 1/2";
        Fraction f1 = format.parse(s1);
        assertEquals(3, f1.getNumerator());
        assertEquals(2, f1.getDenominator());
        String s2 = "-1 1/2";
        Fraction f2 = format.parse(s2);
        assertEquals(-3, f2.getNumerator());
        assertEquals(2, f2.getDenominator());
        String s3 = "0";
        Fraction f3 = format.parse(s3);
        assertEquals(0, f3.getNumerator());
        assertEquals(1, f3.getDenominator());
    }
}