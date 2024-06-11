package org.apache.commons.math.fraction;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.fraction.ProperFractionFormat;
import org.apache.commons.math.fraction.Fraction;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat properFormat = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 2);
        StringBuffer buffer = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        StringBuffer result = properFormat.format(fraction, buffer, pos);
        assertEquals("3 / 2", result.toString());
    }

    @Test
    public void testParse() {
        ProperFractionFormat properFormat = new ProperFractionFormat();
        ParsePosition pos = new ParsePosition(0);
        Fraction result = properFormat.parse("3 / 2", pos);
        assertEquals(3, result.getNumerator());
        assertEquals(2, result.getDenominator());
    }

    @Test
    public void testWholeFormat() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        ProperFractionFormat properFormat = new ProperFractionFormat(numberFormat, numberFormat, numberFormat);
        assertEquals(numberFormat, properFormat.getWholeFormat());
    }

    @Test
    public void testSetWholeFormat() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        ProperFractionFormat properFormat = new ProperFractionFormat();
        properFormat.setWholeFormat(numberFormat);
        assertEquals(numberFormat, properFormat.getWholeFormat());
    }

    // Edge cases and exception handling scenarios for format and parse methods can be included here

}