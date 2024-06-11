import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        StringWriter writer = new StringWriter();
        unescaper.translate("&#65;", 0, writer);
        assertEquals("A", writer.toString());
    }
    
    @Test
    public void testTranslateWithHex() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        StringWriter writer = new StringWriter();
        unescaper.translate("&#x41;", 0, writer);
        assertEquals("A", writer.toString());
    }
    
    @Test
    public void testTranslateWithInvalidEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        StringWriter writer = new StringWriter();
        unescaper.translate("&#invalid;", 0, writer);
        assertEquals("", writer.toString());
    }
    
    @Test
    public void testTranslateWithEmptyInput() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        StringWriter writer = new StringWriter();
        unescaper.translate("", 0, writer);
        assertEquals("", writer.toString());
    }
    
    @Test
    public void testTranslateWithInvalidStart() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        StringWriter writer = new StringWriter();
        unescaper.translate("&#65;", 1, writer);
        assertEquals(0, writer.toString().length());
    }
    
    @Test
    public void testTranslateWithInvalidHex() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        StringWriter writer = new StringWriter();
        unescaper.translate("&#x41;", 1, writer);
        assertEquals(0, writer.toString().length());
    }

}