import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {
    
    private NumericEntityUnescaper unescaper;
    
    @Before
    public void setUp() {
        unescaper = new NumericEntityUnescaper();
    }
    
    @Test
    public void testTranslate_WithNumberHexEntity_ReturnsTranslatedValue() throws IOException {
        StringWriter writer = new StringWriter();
        char[] input = "&#x61;".toCharArray();
        int result = unescaper.translate(input, 0, writer);
        assertEquals(7, result);
        assertEquals("a", writer.toString());
    }
    
    @Test
    public void testTranslate_WithNumberDecimalEntity_ReturnsTranslatedValue() throws IOException {
        StringWriter writer = new StringWriter();
        char[] input = "&#97;".toCharArray();
        int result = unescaper.translate(input, 0, writer);
        assertEquals(6, result);
        assertEquals("a", writer.toString());
    }
    
    @Test
    public void testTranslate_WithNumberDecimalEntityBeyondUnicodeRange_WritesSeparateCharacters() throws IOException {
        StringWriter writer = new StringWriter();
        char[] input = "&#65536;".toCharArray();
        int result = unescaper.translate(input, 0, writer);
        assertEquals(9, result);
        assertEquals(1, writer.toString().length());
        assertEquals('\uD800', writer.toString().charAt(0));
    }
    
    @Test
    public void testTranslate_WithNumberHexEntityWithoutSemiColon_IfSemiColonRequiredOptionNotSet_ReturnsTranslatedValue() throws IOException {
        StringWriter writer = new StringWriter();
        char[] input = "&#x61".toCharArray();
        int result = unescaper.translate(input, 0, writer);
        assertEquals(5, result);
        assertEquals("a", writer.toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_WithNumberHexEntityWithoutSemiColon_IfErrorIfNoSemiColonOptionSet_ThrowsIllegalArgumentException() throws IOException {
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        StringWriter writer = new StringWriter();
        char[] input = "&#x61".toCharArray();
        unescaper.translate(input, 0, writer);
    }
    
    @Test
    public void testTranslate_WithInvalidNumberEntity_ReturnsZero() throws IOException {
        StringWriter writer = new StringWriter();
        char[] input = "&#abc;".toCharArray();
        int result = unescaper.translate(input, 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_WithNoEntity_ReturnsZero() throws IOException {
        StringWriter writer = new StringWriter();
        char[] input = "abc".toCharArray();
        int result = unescaper.translate(input, 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

}