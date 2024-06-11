package org.apache.commons.math3.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ProperFractionFormatTest {
    private ProperFractionFormat properFractionFormat;

    @Before
    public void setUp() {
        properFractionFormat = new ProperFractionFormat();
    }

    @Test
    public void testConstructor() {
        NumberFormat format = mock(NumberFormat.class);
        ProperFractionFormat properFractionFormat = new ProperFractionFormat(format);
        assertEquals(format, properFractionFormat.getWholeFormat());
    }

    @Test
    public void testConstructorWithWholeFormat() {
        NumberFormat wholeFormat = mock(NumberFormat.class);
        NumberFormat numeratorFormat = mock(NumberFormat.class);
        NumberFormat denominatorFormat = mock(NumberFormat.class);
        ProperFractionFormat properFractionFormat = new ProperFractionFormat(wholeFormat, numeratorFormat,
                denominatorFormat);
        assertEquals(wholeFormat, properFractionFormat.getWholeFormat());
    }

    @Test
    public void testFormat() {
        Fraction fraction = mock(Fraction.class);
        StringBuffer toAppendTo = new StringBuffer();
        FieldPosition pos = mock(FieldPosition.class);
        Mockito.when(fraction.getNumerator()).thenReturn(2);
        Mockito.when(fraction.getDenominator()).thenReturn(3);
        
        properFractionFormat.format(fraction, toAppendTo, pos);
        
        assertEquals("2 / 3", toAppendTo.toString());
    }

    @Test
    public void testGetWholeFormat() {
        assertNull(properFractionFormat.getWholeFormat());
    }

    @Test
    public void testParse() {
        ParsePosition pos = mock(ParsePosition.class);
        Fraction fraction = mock(Fraction.class);
        String source = "2 / 3";

        properFractionFormat.parse(source, pos);
        
        Mockito.verify(pos).setBeginIndex(0);
        Mockito.verify(pos).setEndIndex(0);
        Mockito.verify(fraction).getNumerator();
        Mockito.verify(fraction).getDenominator();
        Mockito.verify(fraction).getWholeFormat();
        Mockito.verify(fraction).getNumeratorFormat();
        Mockito.verify(fraction).getDenominatorFormat();
        Mockito.verify(fraction).parse(source, pos);
        Mockito.verify(fraction).intValue();
        Mockito.verify(fraction).parse(source, pos);
        Mockito.verify(fraction).intValue();
        Mockito.verify(fraction).intValue();
        Mockito.verify(fraction).intValue();
        Mockito.verify(fraction).intValue();
        Mockito.verify(fraction).intValue();
        Mockito.verify(fraction).intValue();
        Mockito.verify(fraction).intValue();
        Mockito.verify(fraction).intValue();
        Mockito.verify(fraction).intValue();
        Mockito.verify(fraction).intValue();
    }

    @Test(expected = NullArgumentException.class)
    public void testSetWholeFormatWithNull() {
        properFractionFormat.setWholeFormat(null);
    }
}