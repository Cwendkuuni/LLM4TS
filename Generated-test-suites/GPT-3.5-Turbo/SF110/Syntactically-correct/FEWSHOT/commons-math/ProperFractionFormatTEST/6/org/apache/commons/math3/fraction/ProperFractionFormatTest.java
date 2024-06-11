package org.apache.commons.math3.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

public class ProperFractionFormatTest {

    private ProperFractionFormat properFractionFormat;

    @Before
    public void setup() {
        NumberFormat wholeFormat = mock(NumberFormat.class);
        NumberFormat numeratorFormat = mock(NumberFormat.class);
        NumberFormat denominatorFormat = mock(NumberFormat.class);

        properFractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat, denominatorFormat);
    }

    @Test
    public void testFormat() {
        Fraction fraction = new Fraction(1, 2);
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);

        when(properFractionFormat.getWholeFormat().format(1, toAppendTo, pos)).thenReturn(toAppendTo.append("1 "));
        when(properFractionFormat.getNumeratorFormat().format(0, toAppendTo, pos)).thenReturn(toAppendTo.append("0"));
        when(properFractionFormat.getDenominatorFormat().format(2, toAppendTo, pos)).thenReturn(toAppendTo.append(" / 2"));

        StringBuffer expected = new StringBuffer("1 0 / 2");
        StringBuffer result = properFractionFormat.format(fraction, toAppendTo, pos);

        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testParse() {
        String source = "1 0 / 2";
        ParsePosition pos = new ParsePosition(0);

        when(properFractionFormat.getWholeFormat().parse(source, pos)).thenReturn(1);
        when(properFractionFormat.getNumeratorFormat().parse(source, pos)).thenReturn(0);
        when(properFractionFormat.getDenominatorFormat().parse(source, pos)).thenReturn(2);

        Fraction expected = new Fraction(0, 1);
        Fraction result = properFractionFormat.parse(source, pos);

        assertEquals(expected, result);
    }

    @Test
    public void testSetWholeFormat() {
        NumberFormat format = mock(NumberFormat.class);

        properFractionFormat.setWholeFormat(format);

        verify(format, times(1)).clone();
    }
}