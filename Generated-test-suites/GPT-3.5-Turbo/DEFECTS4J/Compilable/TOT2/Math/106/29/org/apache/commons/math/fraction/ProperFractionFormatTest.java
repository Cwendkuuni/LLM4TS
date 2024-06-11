package org.apache.commons.math.fraction;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

import org.apache.commons.math.fraction.Fraction;
import org.junit.Test;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        StringBuffer buffer = new StringBuffer();
        FieldPosition position = new FieldPosition(0);

        Fraction fraction = new Fraction(5, 2);
        format.format(fraction, buffer, position);
        assertEquals("2 1/2", buffer.toString());

        buffer.setLength(0);
        fraction = new Fraction(7, 3);
        format.format(fraction, buffer, position);
        assertEquals("2 1/3", buffer.toString());
    }

    @Test
    public void testParse() {
        ProperFractionFormat format = new ProperFractionFormat();
        String input = "3 1/4";
        
        ParsePosition position = new ParsePosition(0);
        Fraction fraction = format.parse(input, position);
        assertEquals(13, fraction.getNumerator());
        assertEquals(4, fraction.getDenominator());

        input = "10";
        position.setIndex(0);
        fraction = format.parse(input, position);
        assertEquals(10, fraction.getNumerator());
        assertEquals(1, fraction.getDenominator());

        input = "-4 2/3";
        position.setIndex(0);
        fraction = format.parse(input, position);
        assertEquals(-14, fraction.getNumerator());
        assertEquals(3, fraction.getDenominator());

        input = "-3 -1/2";
        position.setIndex(0);
        fraction = format.parse(input, position);
        assertNull(fraction);
    }

    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat format = new ProperFractionFormat();
        NumberFormat nf = NumberFormat.getInstance();
        format.setWholeFormat(nf);
        assertEquals(nf, format.getWholeFormat());
    }

    // Add more test cases for edge cases and error scenarios as needed

}