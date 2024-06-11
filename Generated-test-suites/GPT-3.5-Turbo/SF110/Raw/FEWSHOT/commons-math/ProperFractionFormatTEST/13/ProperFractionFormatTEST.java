
package org.apache.commons.math3.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ProperFractionFormatTest {
    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        Fraction fraction1 = new Fraction(1, 2);
        assertEquals("1 / 2", format.format(fraction1, new StringBuffer(), new FieldPosition(0)).toString());
        Fraction fraction2 = new Fraction(3, 4);
        assertEquals("3 / 4", format.format(fraction2, new StringBuffer(), new FieldPosition(0)).toString());
        Fraction fraction3 = new Fraction(5, 6);
        assertEquals("5 / 6", format.format(fraction3, new StringBuffer(), new FieldPosition(0)).toString());
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        String str1 = "1 / 2";
        assertEquals(new Fraction(1, 2), format.parse(str1, new ParsePosition(0)));
        String str2 = "3 / 4";
        assertEquals(new Fraction(3, 4), format.parse(str2, new ParsePosition(0)));
        String str3 = "5 / 6";
        assertEquals(new Fraction(5, 6), format.parse(str3, new ParsePosition(0)));
    }
}
