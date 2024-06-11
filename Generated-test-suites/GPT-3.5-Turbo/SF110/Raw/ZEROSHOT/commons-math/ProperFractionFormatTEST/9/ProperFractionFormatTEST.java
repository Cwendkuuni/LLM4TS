package org.apache.commons.math3.fraction;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProperFractionFormatTest {

    @Test
    public void testFormat() {
        ProperFractionFormat pfFormat = new ProperFractionFormat();
        Fraction fraction = new Fraction(3, 4);
        String expectedResult = "3 / 4";
        StringBuffer result = new StringBuffer();
        FieldPosition pos = new FieldPosition(0);
        pfFormat.format(fraction, result, pos);
        assertEquals(expectedResult, result.toString());
    }
    
    @Test
    public void testGetWholeFormat() {
        ProperFractionFormat pfFormat = new ProperFractionFormat();
        NumberFormat wholeFormat = pfFormat.getWholeFormat();
        assertNotNull(wholeFormat);
    }
    
    @Test
    public void testParse() {
        ProperFractionFormat pfFormat = new ProperFractionFormat();
        String source = "1 / 2";
        ParsePosition pos = new ParsePosition(0);
        Fraction result = pfFormat.parse(source, pos);
        assertNotNull(result);
        assertEquals(new Fraction(1, 2), result);
    }
    
    @Test
    public void testSetWholeFormat() {
        ProperFractionFormat pfFormat = new ProperFractionFormat();
        NumberFormat wholeFormat = NumberFormat.getInstance();
        pfFormat.setWholeFormat(wholeFormat);
        assertEquals(wholeFormat, pfFormat.getWholeFormat());
    }
}