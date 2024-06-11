import static org.junit.Assert.*;
import java.io.IOException;
import java.io.StringWriter;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        
        int result = unescaper.translate("&#65;", 0, writer);
        assertEquals(6, result);
        assertEquals("A", writer.toString());
        
        writer = new StringWriter();
        result = unescaper.translate("&#x41;", 0, writer);
        assertEquals(7, result);
        assertEquals("A", writer.toString());
        
        writer = new StringWriter();
        result = unescaper.translate("&#169;", 0, writer);
        assertEquals(7, result);
        assertEquals("©", writer.toString());
        
        writer = new StringWriter();
        result = unescaper.translate("&#8364;", 0, writer);
        assertEquals(8, result);
        assertEquals("€", writer.toString());
    }
    
    @Test
    public void testTranslateWithNoSemicolon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        
        int result = unescaper.translate("&#65", 0, writer);
        assertEquals(5, result);
        assertEquals("A", writer.toString());
        
        writer = new StringWriter();
        result = unescaper.translate("&#x41", 0, writer);
        assertEquals(6, result);
        assertEquals("A", writer.toString());
    }
    
    @Test
    public void testTranslateInvalidInput() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        
        int result = unescaper.translate("&#invalid;", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
        
        writer = new StringWriter();
        result = unescaper.translate("&#x;", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }
    
    @Test
    public void testTranslateHighSurrogate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        
        int result = unescaper.translate("&#128169;", 0, writer);
        assertEquals(10, result);
        assertEquals("\uD83D\uDC69", writer.toString());
    }
}