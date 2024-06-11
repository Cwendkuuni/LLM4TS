package org.apache.commons.math3.fraction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

import org.apache.commons.math3.util.MathUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ProperFractionFormatTest {
    private ProperFractionFormat properFractionFormat;
    private NumberFormat numberFormat;

    @Before
    public void setUp() {
        numberFormat = Mockito.mock(NumberFormat.class);
        properFractionFormat = new ProperFractionFormat(numberFormat, numberFormat, numberFormat);
    }

    @Test
    public void testConstructor() {
        ProperFractionFormat fractionFormat = new ProperFractionFormat();
        ProperFractionFormat fractionFormatMock = Mockito.mock(ProperFractionFormat.class);
        ProperFractionFormat fractionFormatInstance = new ProperFractionFormat(numberFormat, numberFormat, numberFormat);
        Mockito.when(fractionFormatMock.getWholeFormat()).thenReturn(null);
        Mockito.doThrow(new NullPointerException()).when(fractionFormatMock).setWholeFormat(null);
        
        assertEquals(FractionFormat.getDefaultNumberFormat(), fractionFormat.getWholeFormat());
        assertEquals(fractionFormatMock.getWholeFormat(), null);
        assertNotNull(fractionFormatInstance);
    }
    
    @Test
    public void testFormat() {
        StringBuffer stringBuffer = new StringBuffer();
        FieldPosition fieldPosition = new FieldPosition(0);
        
        assertEquals(stringBuffer, properFractionFormat.format(new Fraction(3, 4), stringBuffer, fieldPosition));
    }

    @Test
    public void testGetWholeFormat() {
        properFractionFormat.setWholeFormat(numberFormat);
        assertEquals(properFractionFormat.getWholeFormat(), numberFormat);
    }

    @Test
    public void testParse() {
        ParsePosition parsePosition = new ParsePosition(0);
        properFractionFormat.parse("3/4", parsePosition);
        properFractionFormat.parse("2 / 3", parsePosition);
        properFractionFormat.parse("1 / 2", parsePosition);
        assertFalse(parsePosition.getErrorIndex() != -1);
    }

    @Test(expected = NullPointerException.class)
    public void testSetWholeFormat() {
        properFractionFormat.setWholeFormat(null);
    }
}