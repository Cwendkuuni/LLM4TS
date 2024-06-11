import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.apache.commons.math.fraction.Fraction;
import org.junit.Before;
import org.junit.Test;

import java.text.NumberFormat;
import java.text.ParsePosition;

public class ProperFractionFormatTest {
    
    private ProperFractionFormat format;
    
    @Before
    public void setUp() {
        format = new ProperFractionFormat();
    }
    
    @Test
    public void testFormat() {
        Fraction fraction = new Fraction(3, 2);
        StringBuffer buffer = new StringBuffer();
        format.format(fraction, buffer, null);
        assertEquals("1 1/2", buffer.toString());
    }
    
    @Test
    public void testGetWholeFormat() {
        NumberFormat wholeFormat = mock(NumberFormat.class);
        format.setWholeFormat(wholeFormat);
        assertEquals(wholeFormat, format.getWholeFormat());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetWholeFormatNull() {
        format.setWholeFormat(null);
    }
    
    @Test
    public void testParse() {
        String source = "3 1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse(source, pos);
        assertEquals(3, fraction.getNumerator());
        assertEquals(2, fraction.getDenominator());
        assertEquals(6, fraction.intValue());
        assertEquals(5, fraction.toProperString().length());
        assertEquals(5, pos.getIndex());
    }
    
    @Test
    public void testParseInvalidInput() {
        String source = "3 1 1/2";
        ParsePosition pos = new ParsePosition(0);
        Fraction fraction = format.parse(source, pos);
        assertNull(fraction);
        assertEquals(0, pos.getIndex());
        assertEquals(3, pos.getErrorIndex());
    }
    
    private void parseAndIgnoreWhitespace(String source, ParsePosition pos) {
        int index = pos.getIndex();
        char c = parseNextCharacter(source, pos);
        while (Character.isWhitespace(c)) {
            index = pos.getIndex();
            c = parseNextCharacter(source, pos);
        }
        pos.setIndex(index);
    }
    
    private char parseNextCharacter(String source, ParsePosition pos) {
        int index = pos.getIndex();
        if (index >= source.length()) {
            return 0;
        }
        pos.setIndex(index + 1);
        return source.charAt(index);
    }
}