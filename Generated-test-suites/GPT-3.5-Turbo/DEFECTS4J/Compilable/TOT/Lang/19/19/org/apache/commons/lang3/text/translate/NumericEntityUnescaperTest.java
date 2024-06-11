package org.apache.commons.lang3.text.translate;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class NumericEntityUnescaperTest {
    
    private NumericEntityUnescaper unescaper;
    private StringWriter writer;
    
    @Before
    public void setup() {
        unescaper = new NumericEntityUnescaper();
        writer = new StringWriter();
    }
    
    @Test
    public void testTranslate_withValidHexEntity_returnsCorrectCodepoint() throws IOException {
        String input = "&#x41;";
        int index = 0;
        int expectedRetVal = 7;
        int actualRetVal = unescaper.translate(input, index, writer);
        
        assertEquals(expectedRetVal, actualRetVal);
        assertEquals("A", writer.toString());
    }
    
    @Test
    public void testTranslate_withValidDecimalEntity_returnsCorrectCodepoint() throws IOException {
        String input = "&#65;";
        int index = 0;
        int expectedRetVal = 6;
        int actualRetVal = unescaper.translate(input, index, writer);
        
        assertEquals(expectedRetVal, actualRetVal);
        assertEquals("A", writer.toString());
    }
    
    @Test
    public void testTranslate_withMissingSemicolonInHexEntity_returnsZero() throws IOException {
        String input = "&#x41";
        int index = 0;
        int expectedRetVal = 0;
        int actualRetVal = unescaper.translate(input, index, writer);
        
        assertEquals(expectedRetVal, actualRetVal);
        assertEquals("", writer.toString());
    }
    
    @Test
    public void testTranslate_withInvalidEntity_returnsZero() throws IOException {
        String input = "&#test;";
        int index = 0;
        int expectedRetVal = 0;
        int actualRetVal = unescaper.translate(input, index, writer);
        
        assertEquals(expectedRetVal, actualRetVal);
        assertEquals("", writer.toString());
    }
    
    @Test
    public void testTranslate_withEntityValueGreaterThan0xFFFF_returnsCorrectCodepoints() throws IOException {
        String input = "&#119947;";
        int index = 0;
        int expectedRetVal = 10;
        int actualRetVal = unescaper.translate(input, index, writer);
        
        assertEquals(expectedRetVal, actualRetVal);
        assertEquals("\uD83D\uDE03", writer.toString());
    }
    
    @Test
    public void testTranslate_withEntityValueEqualTo0xFFFF_returnsCorrectCodepoint() throws IOException {
        String input = "&#65535;";
        int index = 0;
        int expectedRetVal = 9;
        int actualRetVal = unescaper.translate(input, index, writer);
        
        assertEquals(expectedRetVal, actualRetVal);
        assertEquals("\uFFFF", writer.toString());
    }
}