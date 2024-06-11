import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Before;
import org.junit.Test;

public class NumericEntityUnescaperTest {
    private NumericEntityUnescaper unescaper;
    
    @Before
    public void setup() {
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
    }
    
    @Test
    public void testIsSet() {
        assertEquals(true, unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertEquals(false, unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertEquals(false, unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
    }
    
    @Test
    public void testTranslate_ValidEntity() throws IOException {
        String input = "&#65;";
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(input, 0, writer);
        
        assertEquals(5, result);
        assertEquals("A", writer.toString());
    }
    
    @Test
    public void testTranslate_SemiColonRequired() throws IOException {
        String input = "&#65";
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(input, 0, writer);
        
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_ErrorIfNoSemiColon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        String input = "&#65";
        StringWriter writer = new StringWriter();
        unescaper.translate(input, 0, writer);
    }
    
    @Test
    public void testTranslate_InvalidHexEntity() throws IOException {
        String input = "&#Z1;";
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(input, 0, writer);
        
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }
    
    @Test
    public void testTranslate_InvalidDecimalEntity() throws IOException {
        String input = "&#1000a;";
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(input, 0, writer);
        
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }
    
    @Test
    public void testTranslate_EntityValueGreaterThan65535() throws IOException {
        String input = "&#65536;";
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(input, 0, writer);
        
        assertEquals(7, result);
        assertEquals("\uD800\uDC00", writer.toString());
    }
    
    @Test
    public void testTranslate_InvalidInput() throws IOException {
        String input = "&65;";
        StringWriter writer = new StringWriter();
        int result = unescaper.translate(input, 0, writer);
        
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }
    
}