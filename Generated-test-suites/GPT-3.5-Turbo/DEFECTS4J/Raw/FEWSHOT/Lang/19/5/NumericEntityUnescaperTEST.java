
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class NumericEntityUnescaperTest {
    private NumericEntityUnescaper unescaper;
    private Writer writer;
    
    @Before
    public void setup() {
        unescaper = new NumericEntityUnescaper();
        writer = new StringWriter();
    }
    
    @After
    public void teardown() {
        unescaper = null;
        writer = null;
    }
    
    @Test
    public void testTranslate_withValidHexCodepoint() throws IOException {
        CharSequence input = "&#x0041;";
        int index = 0;
        int result = unescaper.translate(input, index, writer);
        
        assertEquals(8, result);
        assertEquals("A", writer.toString());
    }
    
    @Test
    public void testTranslate_withValidDecimalCodepoint() throws IOException {
        CharSequence input = "&#65;";
        int index = 0;
        int result = unescaper.translate(input, index, writer);
        
        assertEquals(6, result);
        assertEquals("A", writer.toString());
    }
    
    @Test
    public void testTranslate_withInvalidCodepointFormat() throws IOException {
        CharSequence input = "&#G;";
        int index = 0;
        int result = unescaper.translate(input, index, writer);
        
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }
    
    @Test
    public void testTranslate_withCodepointOutOfRange() throws IOException {
        CharSequence input = "&#1114112;";
        int index = 0;
        int result = unescaper.translate(input, index, writer);
        
        assertEquals(10, result);
        assertEquals("", writer.toString());
    }
    
    @Test
    public void testTranslate_withNoSemicolon() throws IOException {
        CharSequence input = "&#65";
        int index = 0;
        int result = unescaper.translate(input, index, writer);
        
        assertEquals(5, result);
        assertEquals("", writer.toString());
    }
    
    @Test
    public void testTranslate_withInvalidStartChar() throws IOException {
        CharSequence input = "$#65;";
        int index = 0;
        int result = unescaper.translate(input, index, writer);
        
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }
}
